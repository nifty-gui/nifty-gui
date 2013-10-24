package de.lessvoid.nifty.renderer.lwjgl.render.batch;

import de.lessvoid.nifty.batch.Batch;
import de.lessvoid.nifty.render.BlendMode;
import de.lessvoid.nifty.renderer.lwjgl.render.batch.core.CoreElementVBO;
import de.lessvoid.nifty.renderer.lwjgl.render.batch.core.CoreRender;
import de.lessvoid.nifty.renderer.lwjgl.render.batch.core.CoreShader;
import de.lessvoid.nifty.renderer.lwjgl.render.batch.core.CoreVAO;
import de.lessvoid.nifty.renderer.lwjgl.render.batch.core.CoreVBO;
import de.lessvoid.nifty.tools.Color;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;

public class LwjglBatchCoreProfile implements Batch {
  // 4 vertices per quad and 8 vertex attributes per vertex:
  // - 2 x pos
  // - 2 x texture
  // - 4 x color
  private final static int PRIMITIVE_SIZE = 4 * 8;
  private static final int SIZE = 64 * 1024; // 64k
  private final int primitiveRestartIndex;
  @Nonnull
  private float[] primitiveBuffer = new float[PRIMITIVE_SIZE];
  @Nonnull
  private int[] elementIndexBuffer = new int[5];
  @Nonnull
  private BlendMode blendMode = BlendMode.BLEND;
  @Nonnull
  private final CoreVAO vao;
  @Nonnull
  private final CoreVBO vbo;
  @Nonnull
  private final CoreElementVBO elementVbo;
  private int primitiveCount;
  private int indexCount;
  private int globalIndex;

  public LwjglBatchCoreProfile(@Nonnull final CoreShader shader, final int primitiveRestartIndex) {
    this.primitiveRestartIndex = primitiveRestartIndex;
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
    indexCount = 0;
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
    indexCount = 0;
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

    if (blendMode.equals(BlendMode.BLEND)) {
      GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    } else if (blendMode.equals(BlendMode.MULIPLY)) {
      GL11.glBlendFunc(GL11.GL_DST_COLOR, GL11.GL_ZERO);
    }

    vao.bind();
    vbo.getBuffer().flip();
    vbo.bind();
    vbo.send();
    elementVbo.getBuffer().flip();
    elementVbo.bind();
    elementVbo.send();
    CoreRender.renderTriangleStripIndexed(indexCount);
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
    int elementIndexBufferIndex = 0;

    primitiveBuffer[bufferIndex++] = x;
    primitiveBuffer[bufferIndex++] = y + height;
    primitiveBuffer[bufferIndex++] = color3.getRed();
    primitiveBuffer[bufferIndex++] = color3.getGreen();
    primitiveBuffer[bufferIndex++] = color3.getBlue();
    primitiveBuffer[bufferIndex++] = color3.getAlpha();
    primitiveBuffer[bufferIndex++] = textureX;
    primitiveBuffer[bufferIndex++] = textureY + textureHeight;
    elementIndexBuffer[elementIndexBufferIndex++] = globalIndex++;

    primitiveBuffer[bufferIndex++] = x + width;
    primitiveBuffer[bufferIndex++] = y + height;
    primitiveBuffer[bufferIndex++] = color4.getRed();
    primitiveBuffer[bufferIndex++] = color4.getGreen();
    primitiveBuffer[bufferIndex++] = color4.getBlue();
    primitiveBuffer[bufferIndex++] = color4.getAlpha();
    primitiveBuffer[bufferIndex++] = textureX + textureWidth;
    primitiveBuffer[bufferIndex++] = textureY + textureHeight;
    elementIndexBuffer[elementIndexBufferIndex++] = globalIndex++;

    primitiveBuffer[bufferIndex++] = x;
    primitiveBuffer[bufferIndex++] = y;
    primitiveBuffer[bufferIndex++] = color1.getRed();
    primitiveBuffer[bufferIndex++] = color1.getGreen();
    primitiveBuffer[bufferIndex++] = color1.getBlue();
    primitiveBuffer[bufferIndex++] = color1.getAlpha();
    primitiveBuffer[bufferIndex++] = textureX;
    primitiveBuffer[bufferIndex++] = textureY;
    elementIndexBuffer[elementIndexBufferIndex++] = globalIndex++;

    primitiveBuffer[bufferIndex++] = x + width;
    primitiveBuffer[bufferIndex++] = y;
    primitiveBuffer[bufferIndex++] = color2.getRed();
    primitiveBuffer[bufferIndex++] = color2.getGreen();
    primitiveBuffer[bufferIndex++] = color2.getBlue();
    primitiveBuffer[bufferIndex++] = color2.getAlpha();
    primitiveBuffer[bufferIndex++] = textureX + textureWidth;
    primitiveBuffer[bufferIndex] = textureY;
    elementIndexBuffer[elementIndexBufferIndex++] = globalIndex++;
    elementIndexBuffer[elementIndexBufferIndex] = primitiveRestartIndex;

    indexCount += 5;

    vbo.getBuffer().put(primitiveBuffer);
    elementVbo.getBuffer().put(elementIndexBuffer);
    primitiveCount++;
  }
}
