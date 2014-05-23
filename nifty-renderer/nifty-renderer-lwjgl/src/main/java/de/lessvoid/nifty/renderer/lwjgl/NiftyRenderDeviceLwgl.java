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
import de.lessvoid.nifty.api.NiftyLinearGradient;
import de.lessvoid.nifty.internal.math.Mat4;
import de.lessvoid.nifty.internal.math.MatrixFactory;
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

  private int colorQuadCount;
  private boolean clearScreenOnRender = false;

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
    colorQuadCount = 0;
  }

  @Override
  public void end() {
    flushColorQuads();
  }

  @Override
  public NiftyTexture createTexture(final int width, final int height) {
    return new NiftyTextureLwjgl(coreFactory, width, height);
  }

  @Override
  public void render(final NiftyTexture renderTarget, final FloatBuffer vertices) {
    if (colorQuadCount > 0) {
      flushColorQuads();
    }

    vbo.getBuffer().clear();
    FloatBuffer b = vbo.getBuffer();
    vertices.flip();
    b.put(vertices);

    renderTexturedQuads(internal(renderTarget), vertices.position());
  }

  @Override
  public void beginRenderToTexture(final NiftyTexture texture) {
    flushColorQuads();

    fbo.bindFramebuffer();
    fbo.attachTexture(getTextureId(texture), 0);
    glViewport(0, 0, texture.getWidth(), texture.getHeight());
    mvp = MatrixFactory.createOrtho(0, texture.getWidth(), texture.getHeight(), 0);
  }

  @Override
  public void endRenderToTexture(final NiftyTexture texture) {
    flushColorQuads();

    fbo.disableAndResetViewport();
    mvp = MatrixFactory.createOrtho(0, getDisplayWidth(), getDisplayHeight(), 0);
  }

  @Override
  public void filledRect(final double x0, final double y0, final double x1, final double y1, final NiftyColor color) {
    addColorQuad((float) x0, (float) y0, (float)  (x1 - x0), (float) (y1 - y0), color, color, color, color);
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
    FloatBuffer b = vbo.getBuffer();

    // first
    b.put(x0); b.put(y0); b.put((float) c1.getRed()); b.put((float) c1.getGreen()); b.put((float) c1.getBlue()); b.put((float) c1.getAlpha());
    b.put(x0); b.put(y1); b.put((float) c3.getRed()); b.put((float) c3.getGreen()); b.put((float) c3.getBlue()); b.put((float) c3.getAlpha());
    b.put(x1); b.put(y0); b.put((float) c2.getRed()); b.put((float) c2.getGreen()); b.put((float) c2.getBlue()); b.put((float) c2.getAlpha());

    // second
    b.put(x0); b.put(y1); b.put((float) c3.getRed()); b.put((float) c3.getGreen()); b.put((float) c3.getBlue()); b.put((float) c3.getAlpha());
    b.put(x1); b.put(y1); b.put((float) c4.getRed()); b.put((float) c4.getGreen()); b.put((float) c4.getBlue()); b.put((float) c4.getAlpha());
    b.put(x1); b.put(y0); b.put((float) c2.getRed()); b.put((float) c2.getGreen()); b.put((float) c2.getBlue()); b.put((float) c2.getAlpha());

    colorQuadCount++;
  }

  private void renderTexturedQuads(final NiftyTextureLwjgl texture, final int size) {
    CoreShader shader = shaderManager.activate(TEXTURE_SHADER);
    shader.setUniformMatrix("uMvp", 4, mvp.toBuffer());

    vao.bind();
    vbo.bind();
    vbo.getBuffer().flip();
    vbo.send();

    vao.enableVertexAttribute(0);
    vao.vertexAttribPointer(0, 2, FloatType.FLOAT, 4, 0);
    vao.enableVertexAttribute(1);
    vao.vertexAttribPointer(1, 2, FloatType.FLOAT, 4, 2);

    texture.bind();
    coreFactory.getCoreRender().renderTriangles(size);
    vao.unbind();
    vbo.getBuffer().clear();
  }

  private void flushColorQuads() {
    if (colorQuadCount == 0) {
      return;
    }

    CoreShader shader = shaderManager.activate(PLAIN_COLOR_SHADER);
    shader.setUniformMatrix("uMvp", 4, mvp.toBuffer());

    vao.bind();
    vbo.bind();
    vbo.getBuffer().flip();
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

  @Override
  public void filledRect(double x0, double y0, double x1, double y1, NiftyLinearGradient gradient) {
    // TODO Auto-generated method stub
    
  }
}
  