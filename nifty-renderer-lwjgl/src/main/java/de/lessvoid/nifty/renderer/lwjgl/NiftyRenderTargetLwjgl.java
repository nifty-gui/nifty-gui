package de.lessvoid.nifty.renderer.lwjgl;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Matrix4f;

import de.lessvoid.coregl.CoreFBO;
import de.lessvoid.coregl.CoreMatrixFactory;
import de.lessvoid.coregl.CoreRender;
import de.lessvoid.coregl.CoreShader;
import de.lessvoid.coregl.CoreTexture2D;
import de.lessvoid.coregl.CoreVAO;
import de.lessvoid.coregl.CoreVBO;
import de.lessvoid.nifty.api.NiftyColor;
import de.lessvoid.nifty.internal.math.Mat4;
import de.lessvoid.nifty.spi.NiftyRenderTarget;

public class NiftyRenderTargetLwjgl implements NiftyRenderTarget {
  private static final int VERTEX_SIZE = 5*6;
  private static final int MAX_QUADS = 2000;
  private static final int VBO_SIZE = MAX_QUADS * VERTEX_SIZE;

  private final CoreTexture2D texture;
  private final CoreFBO fbo;
  private final CoreShader plainColor;
  private final CoreVAO vao;
  private final CoreVBO vbo;
  private int quadCount;
  private final Matrix4f projection;
  private final FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
  private final Matrix4f model = new Matrix4f();
  private final Matrix4f modelProjection = new Matrix4f();

  public NiftyRenderTargetLwjgl(final CoreTexture2D texture, final CoreFBO fbo, final int countX, final int countY) {
    this.texture = texture;
    this.fbo = fbo;

    plainColor = CoreShader.newShaderWithVertexAttributes("aVertex");
    plainColor.vertexShader("de/lessvoid/nifty/renderer/lwjgl/plain-color.vs");
    plainColor.fragmentShader("de/lessvoid/nifty/renderer/lwjgl/plain-color.fs");
    plainColor.link();
    plainColor.activate();
    plainColor.setUniformi("uTexture", 0);

    projection = CoreMatrixFactory.createOrtho(0, texture.getWidth(), 0, texture.getHeight());

    fbo.bindFramebuffer(texture.getWidth(), texture.getHeight());
    for (int i=0; i<countX*countY; i++) {
      fbo.attachTexture(texture.getTextureId(), 0, i);
      glClearColor(0.25f, 0.5f, 1.0f, 1.f);
      glClear(GL_COLOR_BUFFER_BIT);

    }
    fbo.disableAndResetViewport();

    vao = new CoreVAO();
    vao.bind();

    vbo = CoreVBO.createStream(new float[VBO_SIZE]);
    vbo.bind();

    vao.enableVertexAttributef(0, 2, 2, 0);
    vao.unbind();
  }

  public void bind() {
    texture.bind();
  }

  @Override
  public void setMatrix(final Mat4 matrix) {
    matrixBuffer.clear();
    matrix.store(matrixBuffer);
    matrixBuffer.flip();
    model.load(matrixBuffer);
  }

  @Override
  public void filledRect(final double x0, final double y0, final double x1, final double y1, final NiftyColor color) {
    fbo.bindFramebuffer(texture.getWidth(), texture.getHeight());
    fbo.attachTexture(texture.getTextureId(), 0, 0);

    plainColor.activate();
    plainColor.setUniformf("uColor", (float)color.getRed(), (float)color.getGreen(), (float)color.getBlue(), (float)color.getAlpha());

    addQuad(vbo.getBuffer(), (float)x0, (float)y0, (float)x1, (float)y1);
    quadCount++;
    flush();

    fbo.disableAndResetViewport();
  }

  private void addQuad(final FloatBuffer buffer, final float x0, final float y0, final float x1, final float y1) {
    // first
    buffer.put(x0);
    buffer.put(y0);

    buffer.put(x0);
    buffer.put(y1);

    buffer.put(x1);
    buffer.put(y0);

    // second
    buffer.put(x0);
    buffer.put(y1);

    buffer.put(x1);
    buffer.put(y0);

    buffer.put(x1);
    buffer.put(y1);
  }

  private void flush() {
    if (quadCount == 0) {
      return;
    }

    Matrix4f.mul(projection, model, modelProjection);
    plainColor.activate();
    plainColor.setUniformMatrix4f("uMvp", modelProjection);
    plainColor.setUniformf("uOffset", 0, 0, 0.f);

    vao.bind();

    vbo.bind();
    vbo.getBuffer().flip();
    vbo.send();

    CoreRender.renderTriangles(6*quadCount);
    vao.unbind();
    vbo.getBuffer().clear();
    quadCount = 0;
  }
}
