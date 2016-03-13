/*
 * Copyright (c) 2016, Nifty GUI Community
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


import com.lessvoid.coregl.CoreBuffer;
import com.lessvoid.coregl.CoreFBO;
import com.lessvoid.coregl.CoreRender;
import com.lessvoid.coregl.CoreShader;
import com.lessvoid.coregl.CoreShaderManager;
import com.lessvoid.coregl.CoreVAO;
import com.lessvoid.coregl.CoreVAO.FloatType;
import com.lessvoid.coregl.spi.CoreGL;
import de.lessvoid.nifty.spi.NiftyRenderDevice;
import de.lessvoid.nifty.spi.NiftyTexture;
import de.lessvoid.nifty.types.NiftyCompositeOperation;
import de.lessvoid.nifty.types.NiftyLineCapType;
import de.lessvoid.nifty.types.NiftyLineJoinType;
import de.lessvoid.niftyinternal.NiftyResourceLoader;
import de.lessvoid.niftyinternal.common.IdGenerator;
import de.lessvoid.niftyinternal.math.Mat4;
import de.lessvoid.niftyinternal.math.MatrixFactory;
import de.lessvoid.niftyinternal.render.batch.ColorQuadBatch;
import de.lessvoid.niftyinternal.render.batch.LineBatch;
import de.lessvoid.niftyinternal.render.batch.LinearGradientQuadBatch;
import de.lessvoid.niftyinternal.render.batch.TextureBatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.lessvoid.coregl.CoreBuffer.createCoreBufferObject;
import static com.lessvoid.coregl.CoreBufferDataType.FLOAT;
import static com.lessvoid.coregl.CoreBufferTargetType.ARRAY_BUFFER;
import static com.lessvoid.coregl.CoreBufferUsageType.STATIC_DRAW;
import static com.lessvoid.coregl.CoreBufferUsageType.STREAM_DRAW;


public class NiftyRenderDeviceOpenGL implements NiftyRenderDevice {
  private final static Logger log = LoggerFactory.getLogger(NiftyRenderDeviceOpenGL.class.getName());

  private static final int VERTEX_SIZE = 5*6;
  private static final int MAX_QUADS = 2000;
  private static final int VBO_SIZE = MAX_QUADS * VERTEX_SIZE;
  private static final float NANO_TO_MS_CONVERSION = 1000000.f;

  private final CoreGL gl;
  private final CoreRender coreRender;
  private final CoreShaderManager shaderManager = new CoreShaderManager();
  private final CoreFBO fbo;

  private CoreVAO vaoVertexUVColor;
  private CoreBuffer<FloatBuffer> vboVertexUVColor;

  private CoreVAO vaoVertexColor;
  private CoreBuffer<FloatBuffer> vboVertexColor;

  private CoreVAO vaoVertex;
  private CoreBuffer<FloatBuffer> vboVertex;

  private CoreVAO vaoCustomShader;
  private CoreBuffer<FloatBuffer> vboCustomerShader;

  private Mat4 mvp;
  private Map<String, Boolean> mvpMap = new HashMap<>();
  private int mvpWidth;
  private int mvpHeight;
  private boolean mvpFlipped = false;

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

    // setup vao and vbo for textured quads
    vaoVertexUVColor = CoreVAO.createCoreVAO(gl);
    vaoVertexUVColor.bind();
    vboVertexUVColor = createCoreBufferObject(gl, FLOAT, STREAM_DRAW, VBO_SIZE);
    vboVertexUVColor.send(ARRAY_BUFFER);
    vaoVertexUVColor.enableVertexAttribute(0);
    vaoVertexUVColor.vertexAttribPointer(0, 2, FloatType.FLOAT, 8, 0);
    vaoVertexUVColor.enableVertexAttribute(1);
    vaoVertexUVColor.vertexAttribPointer(1, 2, FloatType.FLOAT, 8, 2);
    vaoVertexUVColor.enableVertexAttribute(2);
    vaoVertexUVColor.vertexAttribPointer(2, 4, FloatType.FLOAT, 8, 4);
    vaoVertexUVColor.unbind();

    // setup vao and vbo for colored quads
    vaoVertexColor = CoreVAO.createCoreVAO(gl);
    vaoVertexColor.bind();
    vboVertexColor = createCoreBufferObject(gl, FLOAT, STREAM_DRAW, VBO_SIZE);
    vboVertexColor.send(ARRAY_BUFFER);
    vaoVertexColor.enableVertexAttribute(0);
    vaoVertexColor.vertexAttribPointer(0, 2, FloatType.FLOAT, 6, 0);
    vaoVertexColor.enableVertexAttribute(1);
    vaoVertexColor.vertexAttribPointer(1, 4, FloatType.FLOAT, 6, 2);
    vaoVertexColor.unbind();

    // setup vao and vbo for colored quads
    vaoVertex = CoreVAO.createCoreVAO(gl);
    vaoVertex.bind();
    vboVertex = createCoreBufferObject(gl, FLOAT, STREAM_DRAW, VBO_SIZE);
    vboVertex.send(ARRAY_BUFFER);
    vaoVertex.enableVertexAttribute(0);
    vaoVertex.vertexAttribPointer(0, 2, FloatType.FLOAT, 2, 0);
    vaoVertex.unbind();

    // setup vao and vbo for custom shader render
    vaoCustomShader = CoreVAO.createCoreVAO(gl);
    vaoCustomShader.bind();
    vboCustomerShader = createCoreBufferObject(gl, ARRAY_BUFFER, STATIC_DRAW, new float[] {
        0.0f, 0.0f,
        0.0f, 1.0f,
        1.0f, 0.0f,

        0.0f, 1.0f,
        1.0f, 1.0f,
        1.0f, 0.0f
    });
    vboCustomerShader.bind(ARRAY_BUFFER);
    vaoCustomShader.enableVertexAttribute(0);
    vaoCustomShader.vertexAttribPointer(0, 2, FloatType.FLOAT, 2, 0);
    vaoCustomShader.unbind();

    pathTexture = NiftyTextureOpenGL.newTextureRGBA(gl, getDisplayWidth(), getDisplayHeight(), FilterMode.Nearest);

    pathFBO = CoreFBO.createCoreFBO(gl);
    pathFBO.bindFramebuffer();
    pathFBO.attachTexture(getTextureId(pathTexture), 0);
    pathFBO.attachStencil(pathTexture.getWidth(), pathTexture.getHeight());
    pathFBO.disable();

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
    log.trace("beginRender()");

    if (clearScreenOnRender) {
      gl.glClearColor(0.f, 0.f, 0.f, 1.f);
      gl.glClear(gl.GL_COLOR_BUFFER_BIT());
    }

    // reset the composite operation to its default value in case someone changed it on the last frame
    changeCompositeOperation(NiftyCompositeOperation.SourceOver);
  }

  @Override
  public void endRender() {
    log.trace("endRender()");
    int error = gl.glGetError();
    if (error != gl.GL_NO_ERROR()) {
      log.error("glGetError(): {}", error);
    }
  }

  @Override
  public NiftyTexture createTexture(
      final int width,
      final int height,
      final FilterMode filterMode) {
    log.trace("createTexture()");
    return NiftyTextureOpenGL.newTextureRGBA(gl, width, height, filterMode);
  }

  @Override
  public NiftyTexture createTexture(
      final int width,
      final int height,
      final ByteBuffer data,
      final FilterMode filterMode) {
    log.trace("createTexture()");
    return new NiftyTextureOpenGL(gl, width, height, data, filterMode);
  }

  @Override
  public NiftyTexture loadTexture(
      final String filename,
      final FilterMode filterMode,
      final PreMultipliedAlphaMode preMultipliedAlphaMode) {
    log.trace("loadTexture()");
    return new NiftyTextureOpenGL(gl, resourceLoader, filename, filterMode, preMultipliedAlphaMode);
  }

  @Override
  public void renderTexturedQuads(final NiftyTexture texture, final FloatBuffer vertices) {
    log.trace("renderTexturedQuads()");
    vertices.flip();

    FloatBuffer b = vboVertexUVColor.getBuffer();
    b.clear();
    b.put(vertices);
    b.flip();
    vboVertexUVColor.send(ARRAY_BUFFER);

    activateShader(TEXTURE_SHADER);

    vaoVertexUVColor.bind();

    internal(texture).bind();
    coreRender.renderTriangles(vertices.position() / TextureBatch.PRIMITIVE_SIZE * 6);
  }

  @Override
  public void renderColorQuads(final FloatBuffer vertices) {
    log.trace("renderColorQuads()");
    vertices.flip();

    FloatBuffer b = vboVertexColor.getBuffer();
    b.clear();
    b.put(vertices);
    b.flip();
    vboVertexColor.send(ARRAY_BUFFER);

    activateShader(PLAIN_COLOR_SHADER);

    vaoVertexColor.bind();
    coreRender.renderTriangles(vertices.position() / ColorQuadBatch.PRIMITIVE_SIZE * 6);
  }

  @Override
  public void renderLinearGradientQuads(final double x0, final double y0, final double x1, final double y1, final List<ColorStop> colorStops, final FloatBuffer vertices) {
    log.trace("renderLinearGradientQuads()");
    vertices.flip();

    FloatBuffer b = vboVertex.getBuffer();
    b.clear();
    b.put(vertices);
    b.flip();
    vboVertex.send(ARRAY_BUFFER);

    CoreShader shader = activateShader(LINEAR_GRADIENT_SHADER);

    float[] gradientStop = new float[colorStops.size()];
    float[] gradientColor = new float[colorStops.size() * 4];
    int i = 0;
    for (ColorStop stop : colorStops) {
      gradientColor[i * 4 + 0] = (float) stop.getColor().getRed();
      gradientColor[i * 4 + 1] = (float) stop.getColor().getGreen();
      gradientColor[i * 4 + 2] = (float) stop.getColor().getBlue();
      gradientColor[i * 4 + 3] = (float) stop.getColor().getAlpha();
      gradientStop[i] = stop.getStop();
      i++;
    }

    shader.setUniformfv("gradientStop", 1, gradientStop);
    shader.setUniformfv("gradientColor", 4, gradientColor);
    shader.setUniformi("numStops", colorStops.size());
    shader.setUniformf("gradient", (float)x0, (float)y0, (float)x1, (float)y1);

    vaoVertex.bind();
    coreRender.renderTriangles(vertices.position() / LinearGradientQuadBatch.PRIMITIVE_SIZE * 6);
  }

  @Override
  public void beginRenderToTexture(final NiftyTexture texture) {
    log.trace("beginRenderToTexture()");
    fbo.bindFramebuffer();
    fbo.attachTexture(getTextureId(texture), 0);
    gl.glViewport(0, 0, texture.getWidth(), texture.getHeight());
    mvpFlipped(texture.getWidth(), texture.getHeight());
    currentFBO = fbo;
  }

  @Override
  public void endRenderToTexture(final NiftyTexture texture) {
    log.trace("endRenderToTexture()");
    fbo.disableAndResetViewport(getDisplayWidth(), getDisplayHeight());
    mvp(getDisplayWidth(), getDisplayHeight());
    currentFBO = null;
  }

  @Override
  public void maskBegin() {
    log.trace("maskBegin()");
    pathFBO.bindFramebuffer();
    gl.glViewport(0, 0, pathTexture.getWidth(), pathTexture.getHeight());
    mvpFlipped(pathTexture.getWidth(), pathTexture.getHeight());
  }

  @Override
  public void maskEnd() {
    log.trace("maskEnd()");

    // Third Pass
    //
    // Now render the pathTexture to the target FBO (or the screen)
    if (currentFBO != null) {
      currentFBO.bindFramebuffer();
    } else {
      pathFBO.disable();
    }

    FloatBuffer quad = vboVertexUVColor.getBuffer();
    quad.clear();
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
    quad.flip();

    vboVertexUVColor.send(ARRAY_BUFFER);
    vaoVertexUVColor.bind();

    activateShader(TEXTURE_SHADER);
    internal(pathTexture).bind();

    changeCompositeOperation(NiftyCompositeOperation.SourceOver);
    coreRender.renderTriangleStrip(4);
  }

  @Override
  public void maskClear() {
    log.trace("maskClear()");
    gl.glClearColor(0.f, 0.f, 0.f, 0.f);
    gl.glClear(gl.GL_COLOR_BUFFER_BIT());
  }

  @Override
  public void maskRenderLines(
    final FloatBuffer vertices,
    final float lineWidth,
    final NiftyLineCapType lineCapType,
    final NiftyLineJoinType lineJoinType) {
    log.trace("maskRenderLines()");

    vertices.flip();

    FloatBuffer b = vboVertex.getBuffer();
    b.clear();

    // we need the first vertex twice for the shader to work correctly
    b.put(vertices.get(0));
    b.put(vertices.get(1));

    // now put all the vertices into the buffer
    b.put(vertices);

    // we need the last vertex twice as well
    b.put(vertices.get(vertices.limit() - 2));
    b.put(vertices.get(vertices.limit() - 1));
    b.flip();

    // line parameters
    float w = lineWidth;
    float r = 1.f;

    // set up the shader
    CoreShader shader = activateShader(getLineShaderKey(lineCapType, lineJoinType));
    shader.setUniformf("lineColorAlpha", 1.f);
    shader.setUniformf("lineParameters", (2*r + w), (2*r + w) / 2.f, (2*r + w) / 2.f - 2 * r, (2*r));

    vboVertex.send(ARRAY_BUFFER);
    vaoVertex.bind();

    changeCompositeOperation(NiftyCompositeOperation.Max);
    coreRender.renderLinesAdjacent(vertices.limit() / LineBatch.PRIMITIVE_SIZE + 2);
  }

  @Override
  public void maskRenderFill(final FloatBuffer vertices) {
    log.trace("maskRenderFill()");

    // set up the shader
    activateShader(FILL_ALPHA_SHADER);
    vertices.flip();

    FloatBuffer b = vboVertex.getBuffer();
    b.clear();
    b.put(vertices);
    b.flip();
    vboVertex.send(ARRAY_BUFFER);
    vaoVertex.bind();

    gl.glEnable(gl.GL_STENCIL_TEST());

    gl.glStencilMask(0xFF);
    gl.glClear(gl.GL_STENCIL_BUFFER_BIT());

    gl.glColorMask(false, false, false, false);
    gl.glDepthMask(false);
    gl.glStencilFunc(gl.GL_NEVER(), 0, 0xFF);
    gl.glStencilOp(gl.GL_INVERT(), gl.GL_KEEP(), gl.GL_KEEP());

    coreRender.renderTriangleFan(vertices.limit() / 2);

    gl.glColorMask(true, true, true, true);
    gl.glDepthMask(true);
    gl.glStencilMask(0x00);
    gl.glStencilFunc(gl.GL_NOTEQUAL(), 0, 0xFF);

    coreRender.renderTriangleFan(vertices.limit() / 2);

    gl.glDisable(gl.GL_STENCIL_TEST());
  }

  @Override
  public String loadCustomShader(final String filename) {
    log.trace("loadCustomShader()");
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
    log.trace("activateCustomShader()");

    CoreShader shader = activateShader(shaderId);
    shader.setUniformf("time", (System.nanoTime() - beginTime) / NANO_TO_MS_CONVERSION / 1000.f);
    shader.setUniformf("resolution", mvpWidth, mvpHeight);

    vaoCustomShader.bind();
    vboCustomerShader.bind(ARRAY_BUFFER);
    vboCustomerShader.send(ARRAY_BUFFER);

    vaoCustomShader.enableVertexAttribute(0);
    vaoCustomShader.disableVertexAttribute(1);
    vaoCustomShader.vertexAttribPointer(0, 2, FloatType.FLOAT, 2, 0);

    coreRender.renderTriangles(3 * 2);
    vaoCustomShader.unbind();
  }

  @Override
  public void changeCompositeOperation(final NiftyCompositeOperation compositeOperation) {
    if (log.isTraceEnabled()) {
      log.trace("changeCompositeOperation(" + compositeOperation + ")");
    }
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
    boolean sameDimension = newWidth == mvpWidth && newHeight == mvpHeight;
    if (sameDimension && !mvpFlipped) {
      return;
    }
    mvp = MatrixFactory.createOrtho(0, newWidth, newHeight, 0);
    mvpWidth = newWidth;
    mvpHeight = newHeight;
    mvpFlipped = false;
    if (!sameDimension) {
      mvpMap.clear();
    }
  }

  private void mvpFlipped(final int newWidth, final int newHeight) {
    boolean sameDimension = newWidth == mvpWidth && newHeight == mvpHeight;
    if (sameDimension && mvpFlipped) {
      return;
    }
    mvp = MatrixFactory.createOrtho(0, newWidth, 0, newHeight);
    mvpWidth = newWidth;
    mvpHeight = newHeight;
    mvpFlipped = true;
    if (!sameDimension) {
      mvpMap.clear();
    }
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

  private CoreShader activateShader(final String shaderName) {
    CoreShader shader = shaderManager.activate(shaderName);
    Boolean shaderMvpFlipped = mvpMap.get(shaderName);
    if (shaderMvpFlipped != null && shaderMvpFlipped == mvpFlipped) {
      return shader;
    }
    shader.setUniformMatrix("uMvp", 4, mvp.toBuffer());
    mvpMap.put(shaderName, mvpFlipped);
    return shader;
  }
}
