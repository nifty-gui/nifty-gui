package de.lessvoid.nifty.renderer.lwjgl;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glViewport;

import java.nio.FloatBuffer;

import de.lessvoid.coregl.CoreFBO;
import de.lessvoid.coregl.CoreFactory;
import de.lessvoid.coregl.CoreShader;
import de.lessvoid.coregl.CoreShaderManager;
import de.lessvoid.coregl.CoreVAO;
import de.lessvoid.coregl.CoreVAO.FloatType;
import de.lessvoid.coregl.CoreVBO;
import de.lessvoid.coregl.CoreVBO.DataType;
import de.lessvoid.coregl.CoreVBO.UsageType;
import de.lessvoid.coregl.lwjgl.CoreFactoryLwjgl;
import de.lessvoid.nifty.api.NiftyColor;
import de.lessvoid.nifty.internal.math.Mat4;
import de.lessvoid.nifty.internal.math.MatrixFactory;
import de.lessvoid.nifty.internal.math.Vec4;
import de.lessvoid.nifty.spi.NiftyRenderDevice;
import de.lessvoid.nifty.spi.NiftyTexture;

public class NiftyRenderDeviceLwgl implements NiftyRenderDevice {
  private static final int VERTEX_SIZE = 5*6;
  private static final int MAX_QUADS = 2000;
  private static final int VBO_SIZE = MAX_QUADS * VERTEX_SIZE;

  private final CoreFactory coreFactory;
  private final CoreShaderManager shaderManager = new CoreShaderManager();

  private CoreVAO vao;
  private CoreVBO<FloatBuffer> vbo;
  private final CoreFBO fbo;
  private Mat4 mvp;
  private Vec4 vsrc = new Vec4();
  private Vec4 vdst = new Vec4();

  private int textureQuadCount;
  private int colorQuadCount;
  private boolean clearScreenOnRender = false;
  private NiftyTextureLwjgl lastTexture;

  private static final String TEXTURE_SHADER = "texture";
  private static final String PLAIN_COLOR_SHADER = "plain";

  public NiftyRenderDeviceLwgl() {
    coreFactory = CoreFactoryLwjgl.create();
    mvp = MatrixFactory.createOrtho(0, getDisplayWidth(), getDisplayHeight(), 0);

    shaderManager.register(PLAIN_COLOR_SHADER, loadPlainShader());
    shaderManager.register(TEXTURE_SHADER, loadTextureShader());

    CoreShader shader = shaderManager.get(TEXTURE_SHADER);
    shader.activate();
    shader.setUniformi("uTexture", 0);

    fbo = coreFactory.createCoreFBO();
    fbo.bindFramebuffer();
    fbo.disable();

    vao = coreFactory.createVAO();
    vao.bind();

    vbo = coreFactory.createVBO(DataType.FLOAT, UsageType.STREAM_DRAW, VBO_SIZE);
    vbo.bind();

    vao.unbind();
  }

  @Override
  public int getDisplayWidth() {
   return 1024;
  }

  @Override
  public int getDisplayHeight() {
    return 768;
  }

  @Override
  public void clearScreenBeforeRender(final boolean clearScreenOnRenderParam) {
    clearScreenOnRender = clearScreenOnRenderParam;
  }

  @Override
  public void begin() {
    if (clearScreenOnRender) {
      glClearColor((float)Math.random(), (float)Math.random(), (float)Math.random(), 1.f);
      // glClearColor(0.f, 0.f, 0.f, 1.f);
      glClear(GL_COLOR_BUFFER_BIT);
    }

    vbo.getBuffer().clear();
    textureQuadCount = 0;
    colorQuadCount = 0;
    lastTexture = null;
  }

  @Override
  public void end() {
    flushTextureQuads();
    flushColorQuads();
  }

  @Override
  public NiftyTexture createTexture(final int width, final int height) {
    return new NiftyTextureLwjgl(coreFactory, width, height);
  }

  @Override
  public void render(final NiftyTexture renderTarget, final Mat4 mat) {
    addTextureQuad(0, 0, renderTarget.getWidth(), renderTarget.getHeight(), 0, internal(renderTarget), mat);
  }

  @Override
  public void beginRenderToTexture(final NiftyTexture texture) {
    flushTextureQuads();
    flushColorQuads();

    fbo.bindFramebuffer();
    fbo.attachTexture(getTextureId(texture), 0);
    glViewport(0, 0, texture.getWidth(), texture.getHeight());
    mvp = MatrixFactory.createOrtho(0, texture.getWidth(), texture.getHeight(), 0);
  }

  @Override
  public void endRenderToTexture(final NiftyTexture texture) {
    flushTextureQuads();
    flushColorQuads();

    fbo.disableAndResetViewport();
    mvp = MatrixFactory.createOrtho(0, getDisplayWidth(), getDisplayHeight(), 0);
  }

  @Override
  public void filledRect(final double x0, final double y0, final double x1, final double y1, final NiftyColor color) {
    addColorQuad((float) x0, (float) y0, (float)  (x1 - x0), (float) (y1 - y0), color, color, color, color);
  }

  private void addTextureQuad(
      final float xs,
      final float ys,
      final float ws,
      final float hs,
      final int texIdx,
      final NiftyTextureLwjgl texture,
      final Mat4 matrix) {
    if (colorQuadCount > 0) {
      flushColorQuads();
    }

    if (lastTexture == null) {
      lastTexture = texture;
    }

    if (texture != lastTexture) {
      flushTextureQuads();
      lastTexture = texture;
    }

    FloatBuffer b = vbo.getBuffer();

    vsrc.x = xs;
    vsrc.y = ys;
    vsrc.z = 0.0f;
    Mat4.transform(matrix, vsrc, vdst);
    float ax = vdst.x;
    float ay = vdst.y;

    vsrc.x = xs + ws;
    vsrc.y = ys;
    vsrc.z = 0.0f;
    Mat4.transform(matrix, vsrc, vdst);
    float bx = vdst.x;
    float by = vdst.y;

    vsrc.x = xs;
    vsrc.y = ys + hs;
    vsrc.z = 0.0f;
    Mat4.transform(matrix, vsrc, vdst);
    float cx = vdst.x;
    float cy = vdst.y;

    vsrc.x = xs + ws;
    vsrc.y = ys + hs;
    vsrc.z = 0.0f;
    Mat4.transform(matrix, vsrc, vdst);
    float dx = vdst.x;
    float dy = vdst.y;

    // first
    b.put(ax); b.put(ay); b.put(0.0f); b.put(0.0f);
    b.put(cx); b.put(cy); b.put(0.0f); b.put(1.0f);
    b.put(bx); b.put(by); b.put(1.0f); b.put(0.0f);

    // second
    b.put(cx); b.put(cy); b.put(0.0f); b.put(1.0f);
    b.put(bx); b.put(by); b.put(1.0f); b.put(0.0f);
    b.put(dx); b.put(dy); b.put(1.0f); b.put(1.0f);

    textureQuadCount++;
  }

  private void addColorQuad(
      final float x0,
      final float y0,
      final float x1,
      final float y1,
      final NiftyColor c1,
      final NiftyColor c2,
      final NiftyColor c3,
      final NiftyColor c4) {
    if (textureQuadCount > 0) {
      flushTextureQuads();
    }

    FloatBuffer b = vbo.getBuffer();

    // first
    b.put(x0); b.put(y0); b.put((float) c1.getRed()); b.put((float) c1.getGreen()); b.put((float) c1.getBlue()); b.put((float) c1.getAlpha());
    b.put(x0); b.put(y1); b.put((float) c3.getRed()); b.put((float) c3.getGreen()); b.put((float) c3.getBlue()); b.put((float) c3.getAlpha());
    b.put(x1); b.put(y0); b.put((float) c2.getRed()); b.put((float) c2.getGreen()); b.put((float) c2.getBlue()); b.put((float) c2.getAlpha());

    // second
    b.put(x0); b.put(y1); b.put((float) c3.getRed()); b.put((float) c3.getGreen()); b.put((float) c3.getBlue()); b.put((float) c3.getAlpha());
    b.put(x1); b.put(y0); b.put((float) c2.getRed()); b.put((float) c2.getGreen()); b.put((float) c2.getBlue()); b.put((float) c2.getAlpha());
    b.put(x1); b.put(y1); b.put((float) c4.getRed()); b.put((float) c4.getGreen()); b.put((float) c4.getBlue()); b.put((float) c4.getAlpha());

    colorQuadCount++;
  }

  private void flushTextureQuads() {
    if (textureQuadCount == 0) {
      return;
    }

    CoreShader shader = shaderManager.activate(TEXTURE_SHADER);
    shader.setUniformMatrix("uMvp", 4, mvp.toBuffer());

    vao.bind();
    vbo.bind();
    vbo.getBuffer().rewind();
    vbo.send();

    vao.enableVertexAttribute(0);
    vao.vertexAttribPointer(0, 2, FloatType.FLOAT, 4, 0);
    vao.enableVertexAttribute(1);
    vao.vertexAttribPointer(1, 2, FloatType.FLOAT, 4, 2);

    lastTexture.bind();
    coreFactory.getCoreRender().renderTriangles(6*textureQuadCount);
    vao.unbind();
    vbo.getBuffer().clear();
    textureQuadCount = 0;
  }

  private void flushColorQuads() {
    if (colorQuadCount == 0) {
      return;
    }

    CoreShader shader = shaderManager.activate(PLAIN_COLOR_SHADER);
    shader.setUniformMatrix("uMvp", 4, mvp.toBuffer());

    vao.bind();
    vbo.bind();
    vbo.getBuffer().rewind();
    vbo.send();

    vao.enableVertexAttribute(0);
    vao.vertexAttribPointer(0, 2, FloatType.FLOAT, 6, 0);
    vao.enableVertexAttribute(1);
    vao.vertexAttribPointer(1, 4, FloatType.FLOAT, 6, 2);

    coreFactory.getCoreRender().renderTriangles(6*colorQuadCount);
    vao.unbind();
    vbo.getBuffer().clear();
    colorQuadCount = 0;
  }

  private int getTextureId(final NiftyTexture texture) {
    return internal(texture).texture.getTextureId();
  }

  private NiftyTextureLwjgl internal(final NiftyTexture texture) {
    return (NiftyTextureLwjgl) texture;
  }

  private CoreShader loadTextureShader() {
    CoreShader shader = coreFactory.newShaderWithVertexAttributes("aVertex", "aUVL");
    shader.vertexShader("de/lessvoid/nifty/renderer/lwjgl/texture.vs");
    shader.fragmentShader("de/lessvoid/nifty/renderer/lwjgl/texture.fs");
    shader.link();
    return shader;
  }

  private CoreShader loadPlainShader() {
    CoreShader shader = coreFactory.newShaderWithVertexAttributes("aVertex", "aColor");
    shader.vertexShader("de/lessvoid/nifty/renderer/lwjgl/plain-color.vs");
    shader.fragmentShader("de/lessvoid/nifty/renderer/lwjgl/plain-color.fs");
    shader.link();
    return shader;
  }
}
