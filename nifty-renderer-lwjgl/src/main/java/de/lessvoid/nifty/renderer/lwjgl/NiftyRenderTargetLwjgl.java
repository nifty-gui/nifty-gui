package de.lessvoid.nifty.renderer.lwjgl;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL31.*;
import static org.lwjgl.opengl.GL32.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL11.GL_BACK;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_NONE;
import static org.lwjgl.opengl.GL11.GL_REPLACE;
import static org.lwjgl.opengl.GL11.GL_STENCIL_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_STENCIL_TEST;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glClearStencil;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glDrawBuffer;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glStencilFunc;
import static org.lwjgl.opengl.GL11.glStencilOp;
import static org.lwjgl.opengl.GL11.glViewport;

import java.io.IOException;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.OpenGLException;
import org.lwjgl.util.vector.Matrix4f;

import de.lessvoid.Screenshot;
import de.lessvoid.coregl.CoreCheckGL;
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
  private final int width;
  private final int height;
  private final Screenshot screenshot = new Screenshot();

  public NiftyRenderTargetLwjgl(final CoreTexture2D texture, final CoreFBO fbo, final int countX, final int countY) {
    this.texture = texture;
    this.fbo = fbo;
    this.width = texture.getWidth();
    this.height = texture.getHeight();

    plainColor = CoreShader.newShaderWithVertexAttributes("aVertex");
    plainColor.vertexShader("de/lessvoid/nifty/renderer/lwjgl/plain-color.vs");
    plainColor.fragmentShader("de/lessvoid/nifty/renderer/lwjgl/plain-color.fs");
    plainColor.link();
    plainColor.activate();
    plainColor.setUniformi("uTexture", 0);

    projection = CoreMatrixFactory.createOrtho(0, texture.getWidth(), 0, texture.getHeight());

    fbo.bindFramebuffer();
    for (int i=0; i<countX*countY; i++) {
      fbo.attachTexture(texture.getTextureId(), 0, i);
      fbo.attachStencil(texture.getWidth(), texture.getHeight());
      glClearColor(1.0f, 1.0f, 1.0f, 1.f);
      glClearStencil(0x00);
      glClear(GL_COLOR_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);

    }
    fbo.disable();
    glViewport(0, 0, Display.getWidth(), Display.getHeight());

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
    fbo.bindFramebuffer();
    fbo.attachTexture(texture.getTextureId(), 0, 0);
    glViewport(0, 0, texture.getWidth(), texture.getHeight());

    plainColor.activate();
    plainColor.setUniformf("uColor", (float)color.getRed(), (float)color.getGreen(), (float)color.getBlue(), (float)color.getAlpha());
    addQuad(vbo.getBuffer(), (float)x0, (float)y0, (float)x1, (float)y1);
    quadCount++;
    flush();

    fbo.disable();
    glViewport(0, 0, Display.getWidth(), Display.getHeight());
  }

  @Override
  public void beginStencil() {
    fbo.bindFramebuffer();
    glColorMask(false, false, false, false);
    glViewport(0, 0, texture.getWidth(), texture.getHeight());

    glClearStencil(0x00);
    glClear(GL_STENCIL_BUFFER_BIT);

    glStencilFunc(GL_ALWAYS, 0xFF, 0xFF);
    glStencilOp(GL_REPLACE, GL_REPLACE, GL_REPLACE);
    glEnable(GL_STENCIL_TEST);
  }

  @Override
  public void markStencil(final double x, final double y, final double width, final double height) {
    plainColor.activate();
    addQuad(vbo.getBuffer(), (float)Math.floor(x - 1), (float)Math.floor(y - 1), (float)Math.ceil(x + width + 1), (float)Math.ceil(y + height + 1));
    quadCount++;
    flush();
  }

  @Override
  public void endStencil() {
    fbo.disable();
    glViewport(0, 0, Display.getWidth(), Display.getHeight());
    glColorMask(true, true, true, true);

    glDisable(GL_STENCIL_TEST);
  }

  @Override
  public void save(final String filebase) {
    fbo.bindFramebuffer();
//    Screenshot.saveColor(filebase + "-color.png", texture.getWidth(), texture.getHeight());
//    Screenshot.saveStencil(filebase + "-stencil.png", texture.getWidth(), texture.getHeight());
    fbo.disable();
  }

  @Override
  public void enableStencil() {
    glStencilFunc(GL_EQUAL, 0x01, 0x01);
    glStencilOp(GL_KEEP, GL_KEEP, GL_KEEP);
    glEnable(GL_STENCIL_TEST);
  }

  @Override
  public void disableStencil() {

    // render stencil to color buffer BEGIN
    fbo.bindFramebuffer();
    glViewport(0, 0, texture.getWidth(), texture.getHeight());
    setMatrix(Mat4.createIdentity());
//    glClear(GL_COLOR_BUFFER_BIT);
    glStencilMask(0x00);
    glStencilFunc(GL_LEQUAL, 1, 0xFF);
    glDisable(GL_STENCIL_TEST);

    plainColor.activate();
    plainColor.setUniformf("uColor", 1.f, 1.f, 1.f, 0.2f);
    addQuad(vbo.getBuffer(), 0, 0, texture.getWidth(), texture.getHeight());
    quadCount++;
    flush();

    glStencilMask(0xFF);
    fbo.disable();
    glViewport(0, 0, Display.getWidth(), Display.getHeight());
    // render stencil to color buffer END

    glDisable(GL_STENCIL_TEST);
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

  @Override
  public int getWidth() {
    return width;
  }

  @Override
  public int getHeight() {
    return height;
  }
}
