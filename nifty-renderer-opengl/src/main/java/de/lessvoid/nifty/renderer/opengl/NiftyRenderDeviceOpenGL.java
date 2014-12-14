/*
 * Copyright (c) 2014, Jens Hohmuth 
 * All rights reserved. 
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are 
 * met: 
 * 
 *  * Redistributions of source code must retain the above copyright 
 *    notice, this list of conditions and the following disclaimer. 
 *  * Redistributions in binary form must reproduce the above copyright 
 *    notice, this list of conditions and the following disclaimer in the 
 *    documentation and/or other materials provided with the distribution. 
 * 
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND 
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR 
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF 
 * THE POSSIBILITY OF SUCH DAMAGE.
 */
package de.lessvoid.nifty.renderer.opengl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Nonnull;

import de.lessvoid.coregl.CoreFBO;
import de.lessvoid.coregl.CoreRender;
import de.lessvoid.coregl.CoreShader;
import de.lessvoid.coregl.CoreShaderManager;
import de.lessvoid.coregl.CoreVAO;
import de.lessvoid.coregl.CoreVAO.FloatType;
import de.lessvoid.coregl.CoreVBO;
import de.lessvoid.coregl.CoreVBO.DataType;
import de.lessvoid.coregl.CoreVBO.UsageType;
import de.lessvoid.coregl.spi.CoreGL;
import de.lessvoid.nifty.api.NiftyColor;
import de.lessvoid.nifty.api.NiftyColorStop;
import de.lessvoid.nifty.api.NiftyCompositeOperation;
import de.lessvoid.nifty.api.NiftyLineCapType;
import de.lessvoid.nifty.api.NiftyLineJoinType;
import de.lessvoid.nifty.api.NiftyLinearGradient;
import de.lessvoid.nifty.api.NiftyResourceLoader;
import de.lessvoid.nifty.internal.common.IdGenerator;
import de.lessvoid.nifty.internal.math.Mat4;
import de.lessvoid.nifty.internal.math.MatrixFactory;
import de.lessvoid.nifty.internal.render.batch.ArcBatch;
import de.lessvoid.nifty.internal.render.batch.ColorQuadBatch;
import de.lessvoid.nifty.internal.render.batch.LineBatch;
import de.lessvoid.nifty.internal.render.batch.LinearGradientQuadBatch;
import de.lessvoid.nifty.internal.render.batch.TextureBatch;
import de.lessvoid.nifty.internal.render.batch.TriangleFanBatch;
import de.lessvoid.nifty.spi.NiftyRenderDevice;
import de.lessvoid.nifty.spi.NiftyTexture;

public class NiftyRenderDeviceOpenGL implements NiftyRenderDevice {
  private final static Logger log = Logger.getLogger(NiftyRenderDeviceOpenGL.class.getName());

  private static final int VERTEX_SIZE = 5*6;
  private static final int MAX_QUADS = 2000;
  private static final int VBO_SIZE = MAX_QUADS * VERTEX_SIZE;
  private static final float NANO_TO_MS_CONVERSION = 1000000.f;

  private final CoreGL gl;
  private final CoreRender coreRender;
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
  private static final String FILL_ALPHA_SHADER = "fill-alpha";

  private NiftyTexture pathTexture;
  private CoreFBO pathFBO;
  private CoreFBO currentFBO;

  public NiftyRenderDeviceOpenGL(final CoreGL gl) throws Exception {
    this.gl = gl;
    this.coreRender = CoreRender.createCoreRender(gl);

    mvp(getDisplayWidth(), getDisplayHeight());

    shaderManager.register(PLAIN_COLOR_SHADER, loadPlainShader());
    shaderManager.register(TEXTURE_SHADER, loadTextureShader());
    shaderManager.register(LINEAR_GRADIENT_SHADER, loadLinearGradientShader());
    shaderManager.register(FILL_ALPHA_SHADER, loadFillAlphaShader());

    registerLineShader(NiftyLineCapType.Butt, NiftyLineJoinType.Miter, "CAP_BUTT", "JOIN_MITER");
    registerLineShader(NiftyLineCapType.Round, NiftyLineJoinType.Miter, "CAP_ROUND", "JOIN_MITER");
    registerLineShader(NiftyLineCapType.Square, NiftyLineJoinType.Miter, "CAP_SQUARE", "JOIN_MITER");
    registerLineShader(NiftyLineCapType.Butt, NiftyLineJoinType.None, "CAP_BUTT", "JOIN_NONE");
    registerLineShader(NiftyLineCapType.Round, NiftyLineJoinType.None, "CAP_ROUND", "JOIN_NONE");
    registerLineShader(NiftyLineCapType.Square, NiftyLineJoinType.None, "CAP_SQUARE", "JOIN_NONE");

    registerArcShader(NiftyLineCapType.Butt, "CAP_BUTT");
    registerArcShader(NiftyLineCapType.Round, "CAP_ROUND");
    registerArcShader(NiftyLineCapType.Square, "CAP_SQUARE");

    CoreShader shader = shaderManager.get(TEXTURE_SHADER);
    shader.activate();
    shader.setUniformi("uTexture", 0);

    fbo = CoreFBO.createCoreFBO(gl);
    fbo.bindFramebuffer();
    fbo.disable();

    pathFBO = CoreFBO.createCoreFBO(gl);
    pathFBO.bindFramebuffer();
    pathFBO.disable();

    vao = CoreVAO.createCoreVAO(gl);
    vao.bind();

    vbo = CoreVBO.createCoreVBO(gl, DataType.FLOAT, UsageType.STREAM_DRAW, VBO_SIZE);
    vbo.bind();

    quadVBO = CoreVBO.createCoreVBO(gl, DataType.FLOAT, UsageType.STATIC_DRAW, new Float[] {
        0.0f, 0.0f,
        0.0f, 1.0f,
        1.0f, 0.0f,

        0.0f, 1.0f,
        1.0f, 1.0f,
        1.0f, 0.0f
    });
    quadVBO.bind();

    vao.unbind();

    pathTexture = NiftyTextureOpenGL.newTextureRGBA(gl, getDisplayWidth(), getDisplayHeight(), FilterMode.Nearest);

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
  public void beginRender() {
    log.fine("beginRender()");

    if (clearScreenOnRender) {
      gl.glClearColor(0.f, 0.f, 0.f, 1.f);
      gl.glClear(gl.GL_COLOR_BUFFER_BIT());
    }

    vbo.getBuffer().clear();
  }

  @Override
  public void endRender() {
    log.fine("endRender()");
  }

  @Override
  public NiftyTexture createTexture(
      final int width,
      final int height,
      final FilterMode filterMode) {
    log.fine("createTexture()");
    return NiftyTextureOpenGL.newTextureRGBA(gl, width, height, filterMode);
  }

  @Override
  public NiftyTexture createTexture(
      final int width,
      final int height,
      final ByteBuffer data,
      final FilterMode filterMode) {
    log.fine("createTexture()");
    return new NiftyTextureOpenGL(gl, width, height, data, filterMode);
  }

  @Override
  public NiftyTexture loadTexture(
      final String filename,
      final FilterMode filterMode,
      final PreMultipliedAlphaMode preMultipliedAlphaMode) {
    log.fine("loadTexture()");
    return new NiftyTextureOpenGL(gl, resourceLoader, filename, filterMode, preMultipliedAlphaMode);
  }

  @Override
  public void render(final NiftyTexture texture, final FloatBuffer vertices) {
    log.fine("render()");
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

    internal(texture).bind();
    coreRender.renderTriangles(vertices.position() / TextureBatch.PRIMITIVE_SIZE * 6);

    vao.disableVertexAttribute(2);

    vao.unbind();
    vbo.getBuffer().clear();
  }

  @Override
  public void renderColorQuads(final FloatBuffer vertices) {
    log.fine("renderColorQuads()");
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

    coreRender.renderTriangles(vertices.position() / ColorQuadBatch.PRIMITIVE_SIZE * 6);
    vao.unbind();
    vbo.getBuffer().clear();
  }

  @Override
  public void renderLinearGradientQuads(final NiftyLinearGradient params, final FloatBuffer vertices) {
    log.fine("renderLinearGradientQuads()");
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

    coreRender.renderTriangles(vertices.position() / LinearGradientQuadBatch.PRIMITIVE_SIZE * 6);
    vao.unbind();
    vbo.getBuffer().clear();
  }

  @Override
  public void beginRenderToTexture(final NiftyTexture texture) {
    log.fine("beginRenderToTexture()");
    fbo.bindFramebuffer();
    fbo.attachTexture(getTextureId(texture), 0);
    gl.glViewport(0, 0, texture.getWidth(), texture.getHeight());
    mvpFlipped(texture.getWidth(), texture.getHeight());
    currentFBO = fbo;
  }

  @Override
  public void endRenderToTexture(final NiftyTexture texture) {
    log.fine("endRenderToTexture()");
    fbo.disableAndResetViewport(getDisplayWidth(), getDisplayHeight());
    mvp(getDisplayWidth(), getDisplayHeight());
    currentFBO = null;
  }

  @Override
  public String loadCustomShader(final String filename) {
    log.fine("loadCustomShader()");
    CoreShader shader = CoreShader.createShaderWithVertexAttributes(gl, "aVertex");
    shader.vertexShader("de/lessvoid/nifty/renderer/lwjgl/custom.vs");
    shader.fragmentShader(filename);
    shader.link();

    String id = String.valueOf(IdGenerator.generate());
    shaderManager.register(id, shader);
    return id;
  }

  @Override
  public void activateCustomShader(final String shaderId) {
    log.fine("activateCustomShader()");
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

    coreRender.renderTriangles(3 * 2);
    vao.unbind();
  }

  @Override
  public void changeCompositeOperation(final NiftyCompositeOperation compositeOperation) {
    log.fine("changeCompositeOperation(" + compositeOperation + ")");
    switch (compositeOperation) {
      case Clear:
        gl.glEnable(gl.GL_BLEND());
        gl.glBlendFunc(gl.GL_ZERO(), gl.GL_ZERO());
        gl.glBlendEquationSeparate(gl.GL_FUNC_ADD(), gl.GL_FUNC_ADD());
        break;
      case Destination:
        gl.glEnable(gl.GL_BLEND());
        gl.glBlendFunc(gl.GL_ZERO(), gl.GL_ONE());
        gl.glBlendEquationSeparate(gl.GL_FUNC_ADD(), gl.GL_FUNC_ADD());
        break;
      case SourceOver:
        gl.glEnable(gl.GL_BLEND());
        gl.glBlendFunc(gl.GL_ONE(), gl.GL_ONE_MINUS_SRC_ALPHA());
        gl.glBlendEquationSeparate(gl.GL_FUNC_ADD(), gl.GL_FUNC_ADD());
        break;
      case SourceAtop:
        gl.glEnable(gl.GL_BLEND());
        gl.glBlendFunc(gl.GL_DST_ALPHA(), gl.GL_ONE_MINUS_SRC_ALPHA());
        gl.glBlendEquationSeparate(gl.GL_FUNC_ADD(), gl.GL_FUNC_ADD());
        break;
      case SourceIn:
        gl.glEnable(gl.GL_BLEND());
        gl.glBlendFunc(gl.GL_DST_ALPHA(), gl.GL_ZERO());
        gl.glBlendEquationSeparate(gl.GL_FUNC_ADD(), gl.GL_FUNC_ADD());
        break;
      case SourceOut:
        gl.glEnable(gl.GL_BLEND());
        gl.glBlendFunc(gl.GL_ONE_MINUS_DST_ALPHA(), gl.GL_ZERO());
        gl.glBlendEquationSeparate(gl.GL_FUNC_ADD(), gl.GL_FUNC_ADD());
        break;
      case DestinationOver:
        gl.glEnable(gl.GL_BLEND());
        gl.glBlendFunc(gl.GL_ONE_MINUS_DST_ALPHA(), gl.GL_ONE());
        gl.glBlendEquationSeparate(gl.GL_FUNC_ADD(), gl.GL_FUNC_ADD());
        break;
      case DestinationAtop:
        gl.glEnable(gl.GL_BLEND());
        gl.glBlendFunc(gl.GL_ONE_MINUS_DST_ALPHA(), gl.GL_SRC_ALPHA());
        gl.glBlendEquationSeparate(gl.GL_FUNC_ADD(), gl.GL_FUNC_ADD());
        break;
      case DestinationIn:
        gl.glEnable(gl.GL_BLEND());
        gl.glBlendFunc(gl.GL_ZERO(), gl.GL_SRC_ALPHA());
        gl.glBlendEquationSeparate(gl.GL_FUNC_ADD(), gl.GL_FUNC_ADD());
        break;
      case DestinationOut:
        gl.glEnable(gl.GL_BLEND());
        gl.glBlendFunc(gl.GL_ZERO(), gl.GL_ONE_MINUS_SRC_ALPHA());
        gl.glBlendEquationSeparate(gl.GL_FUNC_ADD(), gl.GL_FUNC_ADD());
        break;
      case Lighter:
        gl.glEnable(gl.GL_BLEND());
        gl.glBlendFunc(gl.GL_ONE(), gl.GL_ONE());
        gl.glBlendEquationSeparate(gl.GL_FUNC_ADD(), gl.GL_FUNC_ADD());
        break;
      case Copy:
        gl.glEnable(gl.GL_BLEND());
        gl.glBlendFunc(gl.GL_ONE(), gl.GL_ZERO());
        gl.glBlendEquationSeparate(gl.GL_FUNC_ADD(), gl.GL_FUNC_ADD());
        break;
      case XOR:
        gl.glEnable(gl.GL_BLEND());
        gl.glBlendFunc(gl.GL_ONE_MINUS_DST_ALPHA(), gl.GL_ONE_MINUS_SRC_ALPHA());
        gl.glBlendEquationSeparate(gl.GL_FUNC_ADD(), gl.GL_FUNC_ADD());
        break;
      case Off:
        gl.glDisable(gl.GL_BLEND());
        break;
      case Max:
        gl.glEnable(gl.GL_BLEND());
        gl.glBlendFunc(gl.GL_ONE(), gl.GL_ONE_MINUS_SRC_ALPHA());
        gl.glBlendEquationSeparate(gl.GL_MAX(), gl.GL_MAX());
        break;
    }
  }

  @Override
  public void pathBegin() {
    log.fine("pathBegin()");
    pathFBO.bindFramebuffer();
    pathFBO.attachTexture(getTextureId(pathTexture), 0);
    gl.glViewport(0, 0, pathTexture.getWidth(), pathTexture.getHeight());
    mvpFlipped(pathTexture.getWidth(), pathTexture.getHeight());

    gl.glClearColor(0.f, 0.f, 0.f, 0.f);
    gl.glClear(gl.GL_COLOR_BUFFER_BIT());
  }

  @Override
  public void pathLines(
      final FloatBuffer vertices,
      final float lineWidth,
      final NiftyLineCapType lineCapType,
      final NiftyLineJoinType lineJoinType) {
    log.fine("pathLines()");
    vbo.getBuffer().clear();
    FloatBuffer b = vbo.getBuffer();
    vertices.flip();

    // we need the first vertex twice for the shader to work correctly
    b.put(vertices.get(0));
    b.put(vertices.get(1));

    // now put all the vertices into the buffer
    b.put(vertices);

    // we need the last vertex twice as well
    b.put(vertices.get(vertices.limit() - 2));
    b.put(vertices.get(vertices.limit() - 1));

    // line parameters
    float w = lineWidth;
    float r = 2.f;

    // set up the shader
    CoreShader shader = shaderManager.activate(getLineShaderKey(lineCapType, lineJoinType));
    Mat4 localMvp = mvpFlippedReturn(pathTexture.getWidth(), pathTexture.getHeight());
    shader.setUniformMatrix("uMvp", 4, localMvp.toBuffer());
    shader.setUniformf("lineColorAlpha", 1.f);
    shader.setUniformf("lineParameters", (2*r + w), (2*r + w) / 2.f, (2*r + w) / 2.f - 2 * r, (2*r));

    vao.bind();
    vbo.bind();
    vbo.getBuffer().rewind();
    vbo.send();

    vao.enableVertexAttribute(0);
    vao.vertexAttribPointer(0, 2, FloatType.FLOAT, 2, 0);
    vao.disableVertexAttribute(1);

    changeCompositeOperation(NiftyCompositeOperation.Max);
    coreRender.renderLinesAdjacent(vertices.limit() / LineBatch.PRIMITIVE_SIZE + 2);

    vbo.getBuffer().clear();
  }

  @Override
  public void pathArcs(
      final FloatBuffer vertices,
      final NiftyLineCapType lineCapType,
      final float startAngle,
      final float endAngle,
      final float lineWidth,
      final float radius,
      final double lineColorAlpha) {
    log.fine("pathArcs()");
    vbo.getBuffer().clear();
    FloatBuffer b = vbo.getBuffer();
    vertices.flip();
    b.put(vertices);

    // set up the shader
    CoreShader shader = shaderManager.activate(getArcShaderKey(lineCapType));
    Mat4 localMvp = mvpFlippedReturn(pathTexture.getWidth(), pathTexture.getHeight());
    shader.setUniformMatrix("uMvp", 4, localMvp.toBuffer());
    shader.setUniformf("param", startAngle, endAngle, lineWidth / radius / 2.f, (float) lineColorAlpha);

    vao.bind();
    vbo.bind();
    vbo.getBuffer().rewind();
    vbo.send();

    vao.enableVertexAttribute(0);
    vao.vertexAttribPointer(0, 2, FloatType.FLOAT, 4, 0);
    vao.enableVertexAttribute(1);
    vao.vertexAttribPointer(1, 2, FloatType.FLOAT, 4, 2);

    changeCompositeOperation(NiftyCompositeOperation.Max);
    coreRender.renderTriangleStrip(vertices.limit() / ArcBatch.PRIMITIVE_SIZE);

    vbo.getBuffer().clear();
    vao.unbind();
  }

  @Override
  public void pathFill(final FloatBuffer vertices) {
    log.fine("pathFill()");
    vbo.getBuffer().clear();
    FloatBuffer b = vbo.getBuffer();
    vertices.flip();

    // put all the vertices into the buffer
    b.put(vertices);

    // set up the shader
    CoreShader shader = shaderManager.activate(FILL_ALPHA_SHADER);
    Mat4 localMvp = mvpFlippedReturn(pathTexture.getWidth(), pathTexture.getHeight());
    shader.setUniformMatrix("uMvp", 4, localMvp.toBuffer());

    vao.bind();
    vbo.bind();
    vbo.getBuffer().rewind();
    vbo.send();

    vao.enableVertexAttribute(0);
    vao.vertexAttribPointer(0, 2, FloatType.FLOAT, 2, 0);
    vao.disableVertexAttribute(1);

    changeCompositeOperation(NiftyCompositeOperation.Max);
    coreRender.renderTriangleFan(vertices.limit() / TriangleFanBatch.PRIMITIVE_SIZE + 1);

    vbo.getBuffer().clear();
  }

  @Override
  public void pathEnd(final NiftyColor lineColor) {
    log.fine("pathEnd()");
    // Second Pass
    //
    // Now render the actual lines using the FBO texture as the mask.
    FloatBuffer quad = vbo.getBuffer();
    quad.put(0.f);
    quad.put(0.f);
    quad.put((float) lineColor.getRed());
    quad.put((float) lineColor.getGreen());
    quad.put((float) lineColor.getBlue());
    quad.put((float) lineColor.getAlpha());

    quad.put(0.f);
    quad.put(0.f + getDisplayHeight());
    quad.put((float) lineColor.getRed());
    quad.put((float) lineColor.getGreen());
    quad.put((float) lineColor.getBlue());
    quad.put((float) lineColor.getAlpha());

    quad.put(0.f + getDisplayWidth());
    quad.put(0.f);
    quad.put((float) lineColor.getRed());
    quad.put((float) lineColor.getGreen());
    quad.put((float) lineColor.getBlue());
    quad.put((float) lineColor.getAlpha());

    quad.put(0.f + getDisplayWidth());
    quad.put(0.f + getDisplayHeight());
    quad.put((float) lineColor.getRed());
    quad.put((float) lineColor.getGreen());
    quad.put((float) lineColor.getBlue());
    quad.put((float) lineColor.getAlpha());
    quad.rewind();

    vao.bind();
    vbo.bind();
    vbo.send();
    vao.enableVertexAttribute(0);
    vao.vertexAttribPointer(0, 2, FloatType.FLOAT, 6, 0);
    vao.enableVertexAttribute(1);
    vao.vertexAttribPointer(1, 4, FloatType.FLOAT, 6, 2);

    CoreShader shader = shaderManager.activate(PLAIN_COLOR_SHADER);
    Mat4 localMvp = mvpFlippedReturn(pathTexture.getWidth(), pathTexture.getHeight());
    shader.setUniformMatrix("uMvp", 4, localMvp.toBuffer());

    changeCompositeOperation(NiftyCompositeOperation.SourceIn);
    coreRender.renderTriangleStrip(4);
    vao.unbind();

    pathFBO.disable();

    // Third Pass
    //
    // Now render the pathTexture to the target FBO (or the screen)
    if (currentFBO != null) {
      currentFBO.bindFramebuffer();
    }

    quad = vbo.getBuffer();
    quad.put(0.f);
    quad.put(0.f);
    quad.put(0.f);
    quad.put(0.f);
    quad.put(1.0f);
    quad.put(1.0f);
    quad.put(1.0f);
    quad.put(1.0f);

    quad.put(0.f);
    quad.put(0.f + getDisplayHeight());
    quad.put(0.f);
    quad.put(1.f);
    quad.put(1.0f);
    quad.put(1.0f);
    quad.put(1.0f);
    quad.put(1.0f);

    quad.put(0.f + getDisplayWidth());
    quad.put(0.f);
    quad.put(1.f);
    quad.put(0.f);
    quad.put(1.0f);
    quad.put(1.0f);
    quad.put(1.0f);
    quad.put(1.0f);

    quad.put(0.f + getDisplayWidth());
    quad.put(0.f + getDisplayHeight());
    quad.put(1.f);
    quad.put(1.f);
    quad.put(1.0f);
    quad.put(1.0f);
    quad.put(1.0f);
    quad.put(1.0f);
    quad.rewind();

    vao.bind();
    vbo.bind();
    vbo.send();
    vao.enableVertexAttribute(0);
    vao.vertexAttribPointer(0, 2, FloatType.FLOAT, 8, 0);
    vao.enableVertexAttribute(1);
    vao.vertexAttribPointer(1, 2, FloatType.FLOAT, 8, 2);
    vao.enableVertexAttribute(2);
    vao.vertexAttribPointer(2, 4, FloatType.FLOAT, 8, 4);

    shader = shaderManager.activate(TEXTURE_SHADER);
    localMvp = mvpFlippedReturn(pathTexture.getWidth(), pathTexture.getHeight());
    shader.setUniformMatrix("uMvp", 4, localMvp.toBuffer());

    internal(pathTexture).bind();

    changeCompositeOperation(NiftyCompositeOperation.SourceOver);
    coreRender.renderTriangleStrip(4);

    vao.disableVertexAttribute(2);
    vao.unbind();
  }

  private int getTextureId(final NiftyTexture texture) {
    return internal(texture).texture.getTextureId();
  }

  private NiftyTextureOpenGL internal(final NiftyTexture texture) {
    return (NiftyTextureOpenGL) texture;
  }

  private CoreShader loadTextureShader() {
    CoreShader shader = CoreShader.createShaderWithVertexAttributes(gl, "aVertex", "aUV", "aColor");
    shader.vertexShader("de/lessvoid/nifty/renderer/lwjgl/texture.vs");
    shader.fragmentShader("de/lessvoid/nifty/renderer/lwjgl/texture.fs");
    shader.link();
    return shader;
  }

  private CoreShader loadPlainShader() {
    CoreShader shader = CoreShader.createShaderWithVertexAttributes(gl, "aVertex", "aColor");
    shader.vertexShader("de/lessvoid/nifty/renderer/lwjgl/plain-color.vs");
    shader.fragmentShader("de/lessvoid/nifty/renderer/lwjgl/plain-color.fs");
    shader.link();
    return shader;
  }

  private CoreShader loadLinearGradientShader() {
    CoreShader shader = CoreShader.createShaderWithVertexAttributes(gl, "aVertex");
    shader.vertexShader("de/lessvoid/nifty/renderer/lwjgl/linear-gradient.vs");
    shader.fragmentShader("de/lessvoid/nifty/renderer/lwjgl/linear-gradient.fs");
    shader.link();
    return shader;
  }

  private CoreShader loadLineShader(final String capStyle, final String joinType) throws Exception {
    CoreShader shader = CoreShader.createShaderWithVertexAttributes(gl, "aVertex");
    shader.vertexShader("de/lessvoid/nifty/renderer/lwjgl/line-alpha.vs");
    shader.geometryShader("de/lessvoid/nifty/renderer/lwjgl/line-alpha.gs", stream("#version 150 core\n#define " + capStyle + "\n#define " + joinType + "\n"), resource("de/lessvoid/nifty/renderer/lwjgl/line-alpha.gs"));
    shader.fragmentShader("de/lessvoid/nifty/renderer/lwjgl/line-alpha.fs", stream("#version 150 core\n#define " + capStyle + "\n#define " + joinType + "\n"), resource("de/lessvoid/nifty/renderer/lwjgl/line-alpha.fs"));
    shader.link();
    return shader;
  }

  private CoreShader loadArcShader(final String capStyle) throws Exception {
    CoreShader shader = CoreShader.createShaderWithVertexAttributes(gl, "aVertex");
    shader.vertexShader("de/lessvoid/nifty/renderer/lwjgl/circle-alpha.vs");
    shader.fragmentShader("de/lessvoid/nifty/renderer/lwjgl/circle-alpha.fs", stream("#version 150 core\n#define " + capStyle + "\n"), resource("de/lessvoid/nifty/renderer/lwjgl/circle-alpha.fs"));
    shader.link();
    return shader;
  }

  private CoreShader loadFillAlphaShader() throws Exception {
    CoreShader shader = CoreShader.createShaderWithVertexAttributes(gl, "aVertex");
    shader.vertexShader("de/lessvoid/nifty/renderer/lwjgl/fill-alpha.vs");
    shader.fragmentShader("de/lessvoid/nifty/renderer/lwjgl/fill-alpha.fs");
    shader.link();
    return shader;
  }

  private InputStream stream(final String data) {
    return new ByteArrayInputStream(data.getBytes(Charset.forName("ISO-8859-1")));
  }

  private InputStream resource(final String name) {
    return Thread.currentThread().getContextClassLoader().getResourceAsStream(name);
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

  private Mat4 mvpFlippedReturn(final int newWidth, final int newHeight) {
    Mat4 result = MatrixFactory.createOrtho(0, newWidth, 0, newHeight);
    currentWidth = newWidth;
    currentHeight = newHeight;
    return result;
  }

  private void registerLineShader(
      final NiftyLineCapType lineCapType,
      final NiftyLineJoinType lineJoinType,
      final String lineCapString,
      final String lineTypeString) throws Exception {
    shaderManager.register(getLineShaderKey(lineCapType, lineJoinType), loadLineShader(lineCapString, lineTypeString));
  }

  private String getLineShaderKey(final NiftyLineCapType lineCapType, final NiftyLineJoinType lineJoinType) {
    return lineCapType.toString() + ":" + lineJoinType.toString();
  }

  private void registerArcShader(final NiftyLineCapType lineCapType, final String lineCapString) throws Exception {
    shaderManager.register(getArcShaderKey(lineCapType), loadArcShader(lineCapString));
  }

  private String getArcShaderKey(final NiftyLineCapType lineCapType) {
    return lineCapType.toString();
  }
}
