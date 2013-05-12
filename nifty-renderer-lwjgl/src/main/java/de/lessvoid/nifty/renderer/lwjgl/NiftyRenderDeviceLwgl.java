package de.lessvoid.nifty.renderer.lwjgl;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;

import java.nio.FloatBuffer;
import java.util.logging.Logger;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

import de.lessvoid.coregl.CoreCheckGL;
import de.lessvoid.coregl.CoreFBO;
import de.lessvoid.coregl.CoreMatrixFactory;
import de.lessvoid.coregl.CoreRender;
import de.lessvoid.coregl.CoreShader;
import de.lessvoid.coregl.CoreTexture2D;
import de.lessvoid.coregl.CoreLwjglSetup.RenderLoopCallback;
import de.lessvoid.coregl.CoreTexture2D.ColorFormat;
import de.lessvoid.coregl.CoreTexture2D.ResizeFilter;
import de.lessvoid.coregl.CoreVAO;
import de.lessvoid.coregl.CoreVBO;
import de.lessvoid.nifty.api.Nifty;
import de.lessvoid.nifty.internal.math.Mat4;
import de.lessvoid.nifty.spi.NiftyRenderDevice;
import de.lessvoid.nifty.spi.NiftyRenderTarget;

public class NiftyRenderDeviceLwgl implements NiftyRenderDevice {
  private static final int VERTEX_SIZE = 5*6;
  private static final int MAX_QUADS = 2000;
  private static final int VBO_SIZE = MAX_QUADS * VERTEX_SIZE;

  private final Logger log = Logger.getLogger(NiftyRenderDeviceLwgl.class.getName());

  private final FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
  private final CoreShader shader;
  private CoreVAO vao;
  private CoreVBO vbo;
  private final CoreFBO fbo;
  private final Matrix4f mvp;
  private Matrix4f mat = new Matrix4f();
  private int quadCount;

  public NiftyRenderDeviceLwgl() {
    mvp = CoreMatrixFactory.createOrtho(0, getWidth(), getHeight(), 0);

    shader = CoreShader.newShaderWithVertexAttributes("aVertex", "aUVL");
    shader.vertexShader("de/lessvoid/nifty/renderer/lwjgl/texture.vs");
    shader.fragmentShader("de/lessvoid/nifty/renderer/lwjgl/texture.fs");
    shader.link();
    shader.activate();
    shader.setUniformi("uTexture", 0);

    fbo = new CoreFBO();
    fbo.bindFramebuffer();
    fbo.disable();

    vao = new CoreVAO();
    vao.bind();

    vbo = CoreVBO.createStream(new float[VBO_SIZE]);
    vbo.bind();

    vao.enableVertexAttributef(0, 2, 5, 0);
    vao.enableVertexAttributef(1, 3, 5, 2);

    vao.unbind();
  }

  private void addQuad(final FloatBuffer buffer, final int x, final int y, final int width, final int height, final int texIdx) {
    // first
    buffer.put(x);
    buffer.put(y);
    buffer.put(0.0f);
    buffer.put(0.0f);
    buffer.put(texIdx);

    buffer.put(x);
    buffer.put(y + height);
    buffer.put(0.0f);
    buffer.put(1.0f);
    buffer.put(texIdx);

    buffer.put(x + width);
    buffer.put(y);
    buffer.put(1.0f);
    buffer.put(0.0f);
    buffer.put(texIdx);

    // second
    buffer.put(x);
    buffer.put(y + height);
    buffer.put(0.0f);
    buffer.put(1.0f);
    buffer.put(texIdx);

    buffer.put(x + width);
    buffer.put(y);
    buffer.put(1.0f);
    buffer.put(0.0f);
    buffer.put(texIdx);

    buffer.put(x + width);
    buffer.put(y + height);
    buffer.put(1.0f);
    buffer.put(1.0f);
    buffer.put(texIdx);
  }

  @Override
  public int getWidth() {
   return 1024;
  }

  @Override
  public int getHeight() {
    return 768;
  }

  @Override
  public NiftyRenderTarget createRenderTargets(final int width, final int height, final int countX, final int countY) {
    CoreTexture2D textureArray = CoreTexture2D.createEmptyTextureArray(ColorFormat.RGBA, GL11.GL_UNSIGNED_BYTE, width, height, countX*countY, ResizeFilter.Linear);
    return new NiftyRenderTargetLwjgl(textureArray, fbo, countX, countY);
  }

  private Matrix4f toLwjglMatrix(final Mat4 internal) {
    matrixBuffer.clear();
    internal.store(matrixBuffer);
    matrixBuffer.flip();

    Matrix4f matrix = new Matrix4f();
    matrix.load(matrixBuffer);
    return matrix;
  }

  private Mat4 toInternalMatrix(final Matrix4f matrix) {
    matrixBuffer.clear();
    matrix.store(matrixBuffer);
    matrixBuffer.flip();

    Mat4 internal = new Mat4();
    internal.load(matrixBuffer);
    return internal;
  }

  @Override
  public void render(final NiftyRenderTarget renderTarget, final Mat4 mat) {
    Matrix4f m = toLwjglMatrix(mat);
    Matrix4f.mul(mvp, m, this.mat);
//System.out.println("(" + x + ", " + y + ", " + width + ", " + height + ")\n" + m + "* " + this.mat);
    addQuad(vbo.getBuffer(), 0, 0, renderTarget.getWidth(), renderTarget.getHeight(), 0);
    quadCount++;
    ((NiftyRenderTargetLwjgl) renderTarget).bind();

    flush();
  }

  private void flush() {
    if (quadCount == 0) {
      return;
    }
    shader.activate();
    shader.setUniformMatrix4f("uMvp", mat);
    shader.setUniformf("uOffset", 0, 0, 0.f);

    vao.bind();

    vbo.bind();
    vbo.getBuffer().flip();
    vbo.send();

    CoreRender.renderTriangles(6*quadCount);
    vao.unbind();
    vbo.getBuffer().clear();
    quadCount = 0;
  }

  @Override
  public void begin() {
//    glClearColor((float)Math.random(), (float)Math.random(), (float)Math.random(), 1.f);
    glClearColor(0.f, 0.f, 0.f, 1.f);
    glClear(GL_COLOR_BUFFER_BIT);

    vbo.getBuffer().clear();
    quadCount = 0;
  }

  @Override
  public void end() {
    flush();
  }
}
