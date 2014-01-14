package de.lessvoid.nifty.renderer.jogl.render.batch;

import de.lessvoid.nifty.batch.Batch;
import de.lessvoid.nifty.render.BlendMode;
import de.lessvoid.nifty.renderer.jogl.render.batch.core.CoreElementVBO;
import de.lessvoid.nifty.renderer.jogl.render.batch.core.CoreRender;
import de.lessvoid.nifty.renderer.jogl.render.batch.core.CoreShader;
import de.lessvoid.nifty.renderer.jogl.render.batch.core.CoreVAO;
import de.lessvoid.nifty.renderer.jogl.render.batch.core.CoreVBO;
import de.lessvoid.nifty.tools.Color;

import javax.annotation.Nonnull;
import javax.media.opengl.GL;
import javax.media.opengl.GLContext;

public class JoglBatchCoreProfile implements Batch {
  // 4 vertices per quad and 8 vertex attributes per vertex:
  // - 2 x pos
  // - 2 x texture
  // - 4 x color
  private final static int PRIMITIVE_SIZE = 4 * 8;
  private final static int SIZE = 64 * 1024; // 64k
  @Nonnull
  private final float[] primitiveBuffer = new float[PRIMITIVE_SIZE];
  @Nonnull
  private final int[] elementIndexBuffer = new int[6];
  @Nonnull
  private final BlendMode blendMode = BlendMode.BLEND;
  @Nonnull
  private final CoreVAO vao;
  @Nonnull
  private final CoreVBO vbo;
  @Nonnull
  private final CoreElementVBO elementVbo;
  private int primitiveCount;
  private int triangleVertexCount;
  private int globalIndex;

  public JoglBatchCoreProfile(@Nonnull final CoreShader shader) {
    vao = new CoreVAO();
    vao.bind();

    elementVbo = CoreElementVBO.createStream(new int[SIZE]);
    elementVbo.bind();

    vbo = CoreVBO.createStream(new float[SIZE]);
    vbo.bind();

    vao.enableVertexAttributef(shader.getAttribLocation("aVertex"), 2, 8, 0);
    vao.enableVertexAttributef(shader.getAttribLocation("aColor"), 4, 8, 2);
    vao.enableVertexAttributef(shader.getAttribLocation("aTexture"), 2, 8, 6);

    primitiveCount = 0;
    globalIndex = 0;
    triangleVertexCount = 0;
    vao.unbind();
  }

  @Override
  public void begin(@Nonnull BlendMode blendMode, int textureId) {
    vao.bind();
    vbo.bind();
    vbo.getBuffer().clear();
    elementVbo.bind();
    elementVbo.getBuffer().clear();
    primitiveCount = 0;
    globalIndex = 0;
    triangleVertexCount = 0;
    vao.unbind();
  }

  @Nonnull
  @Override
  public BlendMode getBlendMode() {
    return blendMode;
  }

  @Override
  public void render() {
    if (primitiveCount == 0) {
      return; // Attempting to render with an empty vertex buffer crashes the program.
    }

    final GL gl = GLContext.getCurrentGL();
    if (blendMode.equals(BlendMode.BLEND)) {
      gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
    } else if (blendMode.equals(BlendMode.MULIPLY)) {
      gl.glBlendFunc(GL.GL_DST_COLOR, GL.GL_ZERO);
    }

    vao.bind();
    vbo.getBuffer().flip();
    vbo.bind();
    vbo.send();
    elementVbo.getBuffer().flip();
    elementVbo.bind();
    elementVbo.send();
    CoreRender.renderTrianglesIndexed(triangleVertexCount);
  }

  @Override
  public boolean canAddQuad() {
    return ((primitiveCount + 1) * PRIMITIVE_SIZE) < SIZE;
  }

  @Override
  public void addQuad(
          float x,
          float y,
          float width,
          float height,
          @Nonnull Color color1,
          @Nonnull Color color2,
          @Nonnull Color color3,
          @Nonnull Color color4,
          float textureX,
          float textureY,
          float textureWidth,
          float textureHeight) {
    int bufferIndex = 0;
    primitiveBuffer[bufferIndex++] = x;
    primitiveBuffer[bufferIndex++] = y;
    primitiveBuffer[bufferIndex++] = color1.getRed();
    primitiveBuffer[bufferIndex++] = color1.getGreen();
    primitiveBuffer[bufferIndex++] = color1.getBlue();
    primitiveBuffer[bufferIndex++] = color1.getAlpha();
    primitiveBuffer[bufferIndex++] = textureX;
    primitiveBuffer[bufferIndex++] = textureY;

    primitiveBuffer[bufferIndex++] = x + width;
    primitiveBuffer[bufferIndex++] = y;
    primitiveBuffer[bufferIndex++] = color2.getRed();
    primitiveBuffer[bufferIndex++] = color2.getGreen();
    primitiveBuffer[bufferIndex++] = color2.getBlue();
    primitiveBuffer[bufferIndex++] = color2.getAlpha();
    primitiveBuffer[bufferIndex++] = textureX + textureWidth;
    primitiveBuffer[bufferIndex++] = textureY;

    primitiveBuffer[bufferIndex++] = x + width;
    primitiveBuffer[bufferIndex++] = y + height;
    primitiveBuffer[bufferIndex++] = color4.getRed();
    primitiveBuffer[bufferIndex++] = color4.getGreen();
    primitiveBuffer[bufferIndex++] = color4.getBlue();
    primitiveBuffer[bufferIndex++] = color4.getAlpha();
    primitiveBuffer[bufferIndex++] = textureX + textureWidth;
    primitiveBuffer[bufferIndex++] = textureY + textureHeight;

    primitiveBuffer[bufferIndex++] = x;
    primitiveBuffer[bufferIndex++] = y + height;
    primitiveBuffer[bufferIndex++] = color3.getRed();
    primitiveBuffer[bufferIndex++] = color3.getGreen();
    primitiveBuffer[bufferIndex++] = color3.getBlue();
    primitiveBuffer[bufferIndex++] = color3.getAlpha();
    primitiveBuffer[bufferIndex++] = textureX;
    primitiveBuffer[bufferIndex] = textureY + textureHeight;

    int elementIndexBufferIndex = 0;
    elementIndexBuffer[elementIndexBufferIndex++] = globalIndex;
    elementIndexBuffer[elementIndexBufferIndex++] = globalIndex + 1;
    elementIndexBuffer[elementIndexBufferIndex++] = globalIndex + 2;

    elementIndexBuffer[elementIndexBufferIndex++] = globalIndex + 2;
    elementIndexBuffer[elementIndexBufferIndex++] = globalIndex + 3;
    elementIndexBuffer[elementIndexBufferIndex] = globalIndex;

    triangleVertexCount += 6;
    globalIndex += 4;

    vbo.getBuffer().put(primitiveBuffer);
    elementVbo.getBuffer().put(elementIndexBuffer);
    primitiveCount++;
  }
}
