package de.lessvoid.nifty.internal.render;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import de.lessvoid.nifty.internal.math.Mat4;

public class Batch {
  private final static int NUM_MATRIX = 100;
  private final static int NUM_PRIMITIVES = 100;

  private final static int MATRIX_SIZE = 16;
  private final static int PRIMITIVE_SIZE = 4 * 6;

  private final FloatBuffer vertices;
  private final FloatBuffer matrices;
  private final int atlasId;

  public Batch(final int atlasId) {
    this.atlasId = atlasId;
    this.vertices = createBuffer(NUM_MATRIX * MATRIX_SIZE);
    this.matrices = createBuffer(NUM_PRIMITIVES * PRIMITIVE_SIZE);
  }

  public boolean needsNewBatch(final int newAtlasId) {
    return newAtlasId != atlasId;
  }

  public boolean add(
      final double x,
      final double y,
      final int width,
      final int height,
      final double u0,
      final double v0,
      final double u1,
      final double v1,
      final Mat4 mat) {
    // FIXME check number of primitives and reject adding if we would overflow

    vertices.put((float) x);         vertices.put((float) y);
    vertices.put((float) u0);        vertices.put((float) v0);
    vertices.put((float) x);         vertices.put((float) y + height);
    vertices.put((float) u0);        vertices.put((float) v1);
    vertices.put((float) x + width); vertices.put((float) y);
    vertices.put((float) u1);        vertices.put((float) v0);

    vertices.put((float) x + width); vertices.put((float) y);
    vertices.put((float) u1);        vertices.put((float) v0);
    vertices.put((float) x);         vertices.put((float) y + height);
    vertices.put((float) u0);        vertices.put((float) v1);
    vertices.put((float) x + width); vertices.put((float) y + height);
    vertices.put((float) u1);        vertices.put((float) v1);

    matrices.put(mat.toBuffer());

    return true;
  }

  private FloatBuffer createBuffer(final int size) {
    return ByteBuffer.allocateDirect(size << 2).order(ByteOrder.nativeOrder()).asFloatBuffer();
  }
}
