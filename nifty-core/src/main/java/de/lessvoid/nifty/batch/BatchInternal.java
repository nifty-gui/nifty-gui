package de.lessvoid.nifty.batch;

import de.lessvoid.nifty.batch.spi.Batch;
import de.lessvoid.nifty.batch.spi.BufferFactory;
import de.lessvoid.nifty.batch.spi.GL;
import de.lessvoid.nifty.render.BlendMode;
import de.lessvoid.nifty.tools.Color;

import java.nio.FloatBuffer;
import java.util.logging.Logger;
import javax.annotation.Nonnull;

/**
 * Internal implementation for OpenGL batch management that gives OpenGL (& OpenGL ES) - based
 * {@link de.lessvoid.nifty.batch.spi.BatchRenderBackend} implementations some default functionality to avoid having to
 * reinvent the wheel and to prevent unnecessary code duplication. Fully OpenGL ES compatible - this class doesn't
 * require the implementation of any OpenGL methods that are not available in OpenGL ES. This implementation will be
 * the most backwards-compatible because it doesn't use any functions beyond OpenGL 1.1. It is suitable for both mobile
 * & desktop devices.
 *
 * {@inheritDoc}
 *
 * @author void256
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class BatchInternal implements Batch {
  @Nonnull
  private static final Logger log = Logger.getLogger(BatchInternal.class.getName());
  private final static int VERTICES_PER_QUAD = 6;
  private final static int POSITION_ATTRIBUTES_PER_VERTEX = 2;
  private final static int COLOR_ATTRIBUTES_PER_VERTEX = 4;
  private final static int TEXTURE_ATTRIBUTES_PER_VERTEX = 2;
  private final static int ATTRIBUTES_PER_VERTEX = POSITION_ATTRIBUTES_PER_VERTEX + COLOR_ATTRIBUTES_PER_VERTEX + TEXTURE_ATTRIBUTES_PER_VERTEX;
  private final static int BYTES_PER_ATTRIBUTE = 4;
  private final static int PRIMITIVE_SIZE = VERTICES_PER_QUAD * ATTRIBUTES_PER_VERTEX;
  private final static int STRIDE = ATTRIBUTES_PER_VERTEX * BYTES_PER_ATTRIBUTE;
  private final static int SIZE = 64 * 1024; // 64k
  @Nonnull
  private final GL gl;
  @Nonnull
  private final FloatBuffer vertexBuffer;
  @Nonnull
  private final float[] primitiveBuffer = new float[PRIMITIVE_SIZE];
  @Nonnull
  private BlendMode blendMode = BlendMode.BLEND;
  private int primitiveCount;
  private int textureId;

  public BatchInternal(@Nonnull final GL gl, @Nonnull final BufferFactory bufferFactory) {
    this.gl = gl;
    vertexBuffer = bufferFactory.createNativeOrderedFloatBuffer(SIZE);
  }

  @Override
  public void begin(@Nonnull final BlendMode blendMode, final int textureId) {
    this.blendMode = blendMode;
    this.textureId = textureId;
    primitiveCount = 0;
    vertexBuffer.clear();
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

    // We must bind the texture here because the previous batch may have rendered a non-atlas texture, or we may be
    // using a different atlas than the previous batch, which means that a different texture id may be currently
    // bound than the one we are about to draw with here. Another possibility is that client code is directly
    // manipulating openGL textures outside of our knowledge. It is SIGNIFICANTLY more efficient to bind every batch
    // than it is to use glGet* calls to find out if our texture is already bound (glGet* commands are extremely
    // slow, and should not be used in production code).
    gl.glBindTexture(gl.GL_TEXTURE_2D(), textureId);

    if (blendMode.equals(BlendMode.BLEND)) {
      gl.glBlendFunc(gl.GL_SRC_ALPHA(), gl.GL_ONE_MINUS_SRC_ALPHA());
    } else if (blendMode.equals(BlendMode.MULIPLY)) {
      gl.glBlendFunc(gl.GL_DST_COLOR(), gl.GL_ZERO());
    }

    vertexBuffer.flip();
    vertexBuffer.position(0);
    gl.glVertexPointer(POSITION_ATTRIBUTES_PER_VERTEX, gl.GL_FLOAT(), STRIDE, vertexBuffer);

    vertexBuffer.position(2);
    gl.glColorPointer(COLOR_ATTRIBUTES_PER_VERTEX, gl.GL_FLOAT(), STRIDE, vertexBuffer);

    vertexBuffer.position(6);
    gl.glTexCoordPointer(TEXTURE_ATTRIBUTES_PER_VERTEX, gl.GL_FLOAT(), STRIDE, vertexBuffer);

    // While we could use GL_QUADS here instead, requiring a different setup, but GL_TRIANGLES is very efficient and
    // more compatible with different implementations (For example, both OpenGL (desktop platforms) & OpenGL ES
    // (mobile platforms) can use this render() method as-is.
    gl.glDrawArrays(gl.GL_TRIANGLES(), 0, primitiveCount * VERTICES_PER_QUAD);
  }

  @Override
  public boolean canAddQuad() {
    return ((primitiveCount + 1) * PRIMITIVE_SIZE) < SIZE;
  }

  // This could either be an atlas or non-atlas quad.
  @Override
  public void addQuad(
          final float x,
          final float y,
          final float width,
          final float height,
          @Nonnull final Color color1,
          @Nonnull final Color color2,
          @Nonnull final Color color3,
          @Nonnull final Color color4,
          final float textureX,
          final float textureY,
          final float textureWidth,
          final float textureHeight) {
    int bufferIndex = 0;

    // Quad, tessellated into a triangle list with clockwise-winded vertices - (0,1,2), (0,2,3)
    //
    // 0---1
    // | \ |
    // |  \|
    // 3---2

    // Triangle 1 (0,1,2)

    // 0
    primitiveBuffer[bufferIndex++] = x;
    primitiveBuffer[bufferIndex++] = y;
    primitiveBuffer[bufferIndex++] = color1.getRed();
    primitiveBuffer[bufferIndex++] = color1.getGreen();
    primitiveBuffer[bufferIndex++] = color1.getBlue();
    primitiveBuffer[bufferIndex++] = color1.getAlpha();
    primitiveBuffer[bufferIndex++] = textureX;
    primitiveBuffer[bufferIndex++] = textureY;

    // 1
    primitiveBuffer[bufferIndex++] = x + width;
    primitiveBuffer[bufferIndex++] = y;
    primitiveBuffer[bufferIndex++] = color2.getRed();
    primitiveBuffer[bufferIndex++] = color2.getGreen();
    primitiveBuffer[bufferIndex++] = color2.getBlue();
    primitiveBuffer[bufferIndex++] = color2.getAlpha();
    primitiveBuffer[bufferIndex++] = textureX + textureWidth;
    primitiveBuffer[bufferIndex++] = textureY;

    // 2
    primitiveBuffer[bufferIndex++] = x + width;
    primitiveBuffer[bufferIndex++] = y + height;
    primitiveBuffer[bufferIndex++] = color4.getRed();
    primitiveBuffer[bufferIndex++] = color4.getGreen();
    primitiveBuffer[bufferIndex++] = color4.getBlue();
    primitiveBuffer[bufferIndex++] = color4.getAlpha();
    primitiveBuffer[bufferIndex++] = textureX + textureWidth;
    primitiveBuffer[bufferIndex++] = textureY + textureHeight;

    // Triangle 2 (0,2,3)

    // 0
    primitiveBuffer[bufferIndex++] = x;
    primitiveBuffer[bufferIndex++] = y;
    primitiveBuffer[bufferIndex++] = color1.getRed();
    primitiveBuffer[bufferIndex++] = color1.getGreen();
    primitiveBuffer[bufferIndex++] = color1.getBlue();
    primitiveBuffer[bufferIndex++] = color1.getAlpha();
    primitiveBuffer[bufferIndex++] = textureX;
    primitiveBuffer[bufferIndex++] = textureY;

    // 2
    primitiveBuffer[bufferIndex++] = x + width;
    primitiveBuffer[bufferIndex++] = y + height;
    primitiveBuffer[bufferIndex++] = color4.getRed();
    primitiveBuffer[bufferIndex++] = color4.getGreen();
    primitiveBuffer[bufferIndex++] = color4.getBlue();
    primitiveBuffer[bufferIndex++] = color4.getAlpha();
    primitiveBuffer[bufferIndex++] = textureX + textureWidth;
    primitiveBuffer[bufferIndex++] = textureY + textureHeight;

    // 3
    primitiveBuffer[bufferIndex++] = x;
    primitiveBuffer[bufferIndex++] = y + height;
    primitiveBuffer[bufferIndex++] = color3.getRed();
    primitiveBuffer[bufferIndex++] = color3.getGreen();
    primitiveBuffer[bufferIndex++] = color3.getBlue();
    primitiveBuffer[bufferIndex++] = color3.getAlpha();
    primitiveBuffer[bufferIndex++] = textureX;
    primitiveBuffer[bufferIndex] = textureY + textureHeight;

    vertexBuffer.put(primitiveBuffer);
    primitiveCount++;
  }
}
