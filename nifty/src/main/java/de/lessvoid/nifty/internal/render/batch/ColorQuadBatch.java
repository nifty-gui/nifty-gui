package de.lessvoid.nifty.internal.render.batch;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import de.lessvoid.nifty.api.NiftyColor;
import de.lessvoid.nifty.spi.NiftyRenderDevice;

/**
 *
 */
public class ColorQuadBatch implements Batch {
  private final static int NUM_PRIMITIVES = 100;
  public final static int PRIMITIVE_SIZE = 6 * 6;

  private final FloatBuffer b;

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
      final NiftyColor c4) {
    // first
    b.put((float)x0); b.put((float)y0); b.put((float) c1.getRed()); b.put((float) c1.getGreen()); b.put((float) c1.getBlue()); b.put((float) c1.getAlpha());
    b.put((float)x0); b.put((float)y1); b.put((float) c3.getRed()); b.put((float) c3.getGreen()); b.put((float) c3.getBlue()); b.put((float) c3.getAlpha());
    b.put((float)x1); b.put((float)y0); b.put((float) c2.getRed()); b.put((float) c2.getGreen()); b.put((float) c2.getBlue()); b.put((float) c2.getAlpha());

    // second
    b.put((float)x0); b.put((float)y1); b.put((float) c3.getRed()); b.put((float) c3.getGreen()); b.put((float) c3.getBlue()); b.put((float) c3.getAlpha());
    b.put((float)x1); b.put((float)y1); b.put((float) c4.getRed()); b.put((float) c4.getGreen()); b.put((float) c4.getBlue()); b.put((float) c4.getAlpha());
    b.put((float)x1); b.put((float)y0); b.put((float) c2.getRed()); b.put((float) c2.getGreen()); b.put((float) c2.getBlue()); b.put((float) c2.getAlpha());

    return true;
  }

  @Override
  public void render(final NiftyRenderDevice renderDevice) {
    renderDevice.renderColorQuads(b);
  }

  private FloatBuffer createBuffer(final int size) {
    return ByteBuffer.allocateDirect(size << 2).order(ByteOrder.nativeOrder()).asFloatBuffer();
  }
}
