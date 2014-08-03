package de.lessvoid.nifty.internal.render.batch;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import de.lessvoid.nifty.api.NiftyLinearGradient;
import de.lessvoid.nifty.internal.math.Mat4;
import de.lessvoid.nifty.internal.math.Vec4;
import de.lessvoid.nifty.spi.NiftyRenderDevice;

/**
 *
 */
public class LinearGradientQuadBatch implements Batch {
  private final static int NUM_PRIMITIVES = 100;
  public final static int PRIMITIVE_SIZE = 2 * 6;

  private final FloatBuffer b;
  private final NiftyLinearGradient gradientParams;

  // Vec4 buffer data
  private final Vec4 vsrc = new Vec4();
  private final Vec4 vdst = new Vec4();

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
      final double y1,
      final Mat4 mat) {

    // first
    addTransformed(x0, y0, mat);
    addTransformed(x0, y1, mat);
    addTransformed(x1, y0, mat);

    // second
    addTransformed(x0, y1, mat);
    addTransformed(x1, y1, mat);
    addTransformed(x1, y0, mat);

    return true;
  }

  @Override
  public void render(final NiftyRenderDevice renderDevice) {
    renderDevice.renderLinearGradientQuads(gradientParams, b);
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
