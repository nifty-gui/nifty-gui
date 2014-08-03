package de.lessvoid.nifty.internal.render.batch;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import de.lessvoid.nifty.api.NiftyColor;
import de.lessvoid.nifty.internal.math.Mat4;
import de.lessvoid.nifty.internal.math.Vec4;
import de.lessvoid.nifty.spi.NiftyRenderDevice;

/**
 *
 */
public class ColorQuadBatch implements Batch {
  private final static int NUM_PRIMITIVES = 100;
  public final static int PRIMITIVE_SIZE = 6 * 6;

  private final FloatBuffer b;

  // Vec4 buffer data
  private final Vec4 vsrc = new Vec4();
  private final Vec4 vdst = new Vec4();

  public ColorQuadBatch() {
    this.b = createBuffer(NUM_PRIMITIVES * PRIMITIVE_SIZE);
  }

  public boolean add(
      final double x0,
      final double y0,
      final double x1,
      final double y1,
      final NiftyColor c1,
      final NiftyColor c2,
      final NiftyColor c3,
      final NiftyColor c4,
      final Mat4 mat) {
    // first
    addTransformed(x0, y0, mat);
    b.put((float) c1.getRed()); b.put((float) c1.getGreen()); b.put((float) c1.getBlue()); b.put((float) c1.getAlpha());

    addTransformed(x0, y1, mat);
    b.put((float) c3.getRed()); b.put((float) c3.getGreen()); b.put((float) c3.getBlue()); b.put((float) c3.getAlpha());

    addTransformed(x1, y0, mat);
    b.put((float) c2.getRed()); b.put((float) c2.getGreen()); b.put((float) c2.getBlue()); b.put((float) c2.getAlpha());

    // second
    addTransformed(x0, y1, mat);
    b.put((float) c3.getRed()); b.put((float) c3.getGreen()); b.put((float) c3.getBlue()); b.put((float) c3.getAlpha());

    addTransformed(x1, y1, mat);
    b.put((float) c4.getRed()); b.put((float) c4.getGreen()); b.put((float) c4.getBlue()); b.put((float) c4.getAlpha());

    addTransformed(x1, y0, mat);
    b.put((float) c2.getRed()); b.put((float) c2.getGreen()); b.put((float) c2.getBlue()); b.put((float) c2.getAlpha());

    return true;
  }

  @Override
  public void render(final NiftyRenderDevice renderDevice) {
    renderDevice.renderColorQuads(b);
  }

  private void addTransformed(final double x, final double y, final Mat4 mat) {
    vsrc.x = (float) x;
    vsrc.y = (float) y;
    vsrc.z = 0.0f;
    Mat4.transform(mat, vsrc, vdst);
    b.put(vdst.x);
    b.put(vdst.y);
  }

  private FloatBuffer createBuffer(final int size) {
    return ByteBuffer.allocateDirect(size << 2).order(ByteOrder.nativeOrder()).asFloatBuffer();
  }
}
