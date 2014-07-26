package de.lessvoid.nifty.renderer.lwjgl;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DST_COLOR;
import static org.lwjgl.opengl.GL11.GL_ONE;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_ZERO;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL14.glBlendFuncSeparate;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import javax.annotation.Nonnull;

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
import de.lessvoid.nifty.api.BlendMode;
import de.lessvoid.nifty.api.NiftyColorStop;
import de.lessvoid.nifty.api.NiftyLinearGradient;
import de.lessvoid.nifty.api.NiftyResourceLoader;
import de.lessvoid.nifty.internal.common.IdGenerator;
import de.lessvoid.nifty.internal.math.Mat4;
import de.lessvoid.nifty.internal.math.MatrixFactory;
import de.lessvoid.nifty.internal.render.batch.ColorQuadBatch;
import de.lessvoid.nifty.internal.render.batch.LinearGradientQuadBatch;
import de.lessvoid.nifty.internal.render.batch.TextureBatch;
import de.lessvoid.nifty.spi.NiftyRenderDevice;
import de.lessvoid.nifty.spi.NiftyTexture;

public class NiftyRenderDeviceLwgl implements NiftyRenderDevice {
  private static final int VERTEX_SIZE = 5*6;
  private static final int MAX_QUADS = 2000;
  private static final int VBO_SIZE = MAX_QUADS * VERTEX_SIZE;
  private static final float NANO_TO_MS_CONVERSION = 1000000.f;

  private final CoreFactory coreFactory;
  private final CoreShaderManager shaderManager = new CoreShaderManager();
  private final CoreVBO<FloatBuffer> quadVBO; 

  private CoreVAO vao;
  private CoreVBO<FloatBuffer> vbo;
  private final CoreFBO fbo;

  private Mat4 mvp;
  private int currentWidth;
  private int currentHeight;

  private boolean clearScreenOnRender = false;
  private NiftyResourceLoader resourceLoader;
  private long beginTime;

  private static final String TEXTURE_SHADER = "texture";
  private static final String PLAIN_COLOR_SHADER = "plain";
  private static final String LINEAR_GRADIENT_SHADER = "linearGradient";

  public NiftyRenderDeviceLwgl() {
    coreFactory = CoreFactoryLwjgl.create();
    mvp(getDisplayWidth(), getDisplayHeight());

    shaderManager.register(PLAIN_COLOR_SHADER, loadPlainShader());
    shaderManager.register(TEXTURE_SHADER, loadTextureShader());
    shaderManager.register(LINEAR_GRADIENT_SHADER, loadLinearGradientShader());

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

    quadVBO = coreFactory.createVBO(DataType.FLOAT, UsageType.STATIC_DRAW, new Float[] {
        0.0f, 0.0f,
        0.0f, 1.0f,
        1.0f, 0.0f,

        0.0f, 1.0f,
        1.0f, 1.0f,
        1.0f, 0.0f
    });
    quadVBO.bind();

    vao.unbind();
    beginTime = System.nanoTime();
  }

  @Override
  public void setResourceLoader(@Nonnull final NiftyResourceLoader resourceLoader) {
    this.resourceLoader = resourceLoader;
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
  }

  @Override
  public void end() {
  }

  @Override
  public NiftyTexture createTexture(final int width, final int height, final boolean filterLinear) {
    return new NiftyTextureLwjgl(coreFactory, width, height, filterLinear);
  }

  @Override
  public NiftyTexture createTexture(final int width, final int height, final ByteBuffer data, final boolean filterLinear) {
    return new NiftyTextureLwjgl(coreFactory, width, height, data, filterLinear);
  }

  @Override
  public NiftyTexture loadTexture(final String filename, final boolean filterLinear) {
    return new NiftyTextureLwjgl(coreFactory, resourceLoader, filename, filterLinear);
  }

  @Override
  public void render(final NiftyTexture renderTarget, final FloatBuffer vertices) {
    vbo.getBuffer().clear();
    FloatBuffer b = vbo.getBuffer();
    vertices.flip();
    b.put(vertices);

    CoreShader shader = shaderManager.activate(TEXTURE_SHADER);
    shader.setUniformMatrix("uMvp", 4, mvp.toBuffer());

    vao.bind();
    vbo.bind();
    vbo.getBuffer().rewind();
    vbo.send();

    vao.enableVertexAttribute(0);
    vao.vertexAttribPointer(0, 2, FloatType.FLOAT, 8, 0);
    vao.enableVertexAttribute(1);
    vao.vertexAttribPointer(1, 2, FloatType.FLOAT, 8, 2);
    vao.enableVertexAttribute(2);
    vao.vertexAttribPointer(2, 4, FloatType.FLOAT, 8, 4);

    internal(renderTarget).bind();
    coreFactory.getCoreRender().renderTriangles(vertices.position() / TextureBatch.PRIMITIVE_SIZE * 6);

    vao.disableVertexAttribute(2);

    vao.unbind();
    vbo.getBuffer().clear();
  }

  @Override
  public void renderColorQuads(final FloatBuffer vertices) {
    vbo.getBuffer().clear();
    FloatBuffer b = vbo.getBuffer();
    vertices.flip();
    b.put(vertices);

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

    coreFactory.getCoreRender().renderTriangles(vertices.position() / ColorQuadBatch.PRIMITIVE_SIZE * 6);
    vao.unbind();
    vbo.getBuffer().clear();
  }

  @Override
  public void renderLinearGradientQuads(final NiftyLinearGradient params, final FloatBuffer vertices) {
    vbo.getBuffer().clear();
    FloatBuffer b = vbo.getBuffer();
    vertices.flip();
    b.put(vertices);

    CoreShader shader = shaderManager.activate(LINEAR_GRADIENT_SHADER);
    shader.setUniformMatrix("uMvp", 4, mvp.toBuffer());

    float[] gradientStop = new float[params.getColorStops().size()];
    float[] gradientColor = new float[params.getColorStops().size() * 4];
    int i = 0;
    for (NiftyColorStop stop : params.getColorStops()) {
      gradientColor[i * 4 + 0] = (float) stop.getColor().getRed();
      gradientColor[i * 4 + 1] = (float) stop.getColor().getGreen();
      gradientColor[i * 4 + 2] = (float) stop.getColor().getBlue();
      gradientColor[i * 4 + 3] = (float) stop.getColor().getAlpha();
      gradientStop[i] = (float) stop.getStop();
      i++;
    }

    shader.setUniformfv("gradientStop", 1, gradientStop);
    shader.setUniformfv("gradientColor", 4, gradientColor);
    shader.setUniformi("numStops", params.getColorStops().size());
    shader.setUniformf(
        "gradient",
        (float)params.getX0(), (float)params.getY0(),
        (float)params.getX1(), (float)params.getY1());

    vao.bind();
    vbo.bind();
    vbo.getBuffer().rewind();
    vbo.send();

    vao.enableVertexAttribute(0);
    vao.vertexAttribPointer(0, 2, FloatType.FLOAT, 2, 0);
    vao.disableVertexAttribute(1);

    coreFactory.getCoreRender().renderTriangles(vertices.position() / LinearGradientQuadBatch.PRIMITIVE_SIZE * 6);
    vao.unbind();
    vbo.getBuffer().clear();
  }

  @Override
  public void beginRenderToTexture(final NiftyTexture texture) {
    fbo.bindFramebuffer();
    fbo.attachTexture(getTextureId(texture), 0);
    glViewport(0, 0, texture.getWidth(), texture.getHeight());
    mvpFlipped(texture.getWidth(), texture.getHeight());
  }

  @Override
  public void endRenderToTexture(final NiftyTexture texture) {
    fbo.disableAndResetViewport();
    mvp(getDisplayWidth(), getDisplayHeight());
  }

  @Override
  public String loadCustomShader(final String filename) {
    CoreShader shader = coreFactory.newShaderWithVertexAttributes("aVertex");
    shader.vertexShader("de/lessvoid/nifty/renderer/lwjgl/custom.vs");
    shader.fragmentShader(filename);
    shader.link();

    String id = String.valueOf(IdGenerator.generate());
    shaderManager.register(id, shader);
    return id;
  }

  @Override
  public void renderWithShader(final String shaderId) {
    CoreShader shader = shaderManager.activate(shaderId);
    shader.setUniformMatrix("uMvp", 4, mvp.toBuffer());
    shader.setUniformf("time", (System.nanoTime() - beginTime) / NANO_TO_MS_CONVERSION / 1000.f);
    shader.setUniformf("resolution", currentWidth, currentHeight);

    vao.bind();
    quadVBO.bind();
    quadVBO.send();

    vao.enableVertexAttribute(0);
    vao.disableVertexAttribute(1);
    vao.vertexAttribPointer(0, 2, FloatType.FLOAT, 2, 0);

    coreFactory.getCoreRender().renderTriangles(3 * 2);
    vao.unbind();
  }

  @Override
  public void changeBlendMode(final BlendMode blendMode) {
    switch (blendMode) {
    case OFF:
        glDisable(GL_BLEND);
        break;

    case BLEND:
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        break;

    case BLEND_SEP:
        glEnable(GL_BLEND);
        glBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
        break;

    case MULTIPLY:
        glEnable(GL_BLEND);
        glBlendFunc(GL_DST_COLOR, GL_ZERO);
        break;
    }
  }

  private int getTextureId(final NiftyTexture texture) {
    return internal(texture).texture.getTextureId();
  }

  private NiftyTextureLwjgl internal(final NiftyTexture texture) {
    return (NiftyTextureLwjgl) texture;
  }

  private CoreShader loadTextureShader() {
    CoreShader shader = coreFactory.newShaderWithVertexAttributes("aVertex", "aUV", "aColor");
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

  private CoreShader loadLinearGradientShader() {
    CoreShader shader = coreFactory.newShaderWithVertexAttributes("aVertex");
    shader.vertexShader("de/lessvoid/nifty/renderer/lwjgl/linear-gradient.vs");
    shader.fragmentShader("de/lessvoid/nifty/renderer/lwjgl/linear-gradient.fs");
    shader.link();
    return shader;
  }

  private void mvp(final int newWidth, final int newHeight) {
    mvp = MatrixFactory.createOrtho(0, newWidth, newHeight, 0);
    currentWidth = newWidth;
    currentHeight = newHeight;
  }

  private void mvpFlipped(final int newWidth, final int newHeight) {
    mvp = MatrixFactory.createOrtho(0, newWidth, 0, newHeight);
    currentWidth = newWidth;
    currentHeight = newHeight;
  }
}
