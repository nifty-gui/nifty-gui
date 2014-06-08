package de.lessvoid.nifty.internal.render.batch;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import de.lessvoid.nifty.api.NiftyLinearGradient;
import de.lessvoid.nifty.spi.NiftyRenderDevice;

/**
 *
 */
public class LinearGradientQuadBatch implements Batch {
  private final static int NUM_PRIMITIVES = 100;
  public final static int PRIMITIVE_SIZE = 2 * 6;

  private final FloatBuffer b;
  private final NiftyLinearGradient gradientParams;

  public LinearGradientQuadBatch(final NiftyLinearGradient gradientParams) {
    this.b = createBuffer(NUM_PRIMITIVES * PRIMITIVE_SIZE);
    this.gradientParams = gradientParams;
  }

  public boolean requiresNewBatch(final NiftyLinearGradient params) {
    return !gradientParams.equals(params);
  }

  public boolean add(
      final double x0,
      final double y0,
      final double x1,
      final double y1) {

    // first
    b.put((float)x0); b.put((float)y0);
    b.put((float)x0); b.put((float)y1);
    b.put((float)x1); b.put((float)y0);

    // second
    b.put((float)x0); b.put((float)y1);
    b.put((float)x1); b.put((float)y1);
    b.put((float)x1); b.put((float)y0);

    return true;
  }

  @Override
  public void render(final NiftyRenderDevice renderDevice) {
    renderDevice.renderLinearGradientQuads(gradientParams, b);
  }

  private FloatBuffer createBuffer(final int size) {
    return ByteBuffer.allocateDirect(size << 2).order(ByteOrder.nativeOrder()).asFloatBuffer();
  }
}
