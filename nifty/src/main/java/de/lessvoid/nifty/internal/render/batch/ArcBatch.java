package de.lessvoid.nifty.internal.render.batch;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import de.lessvoid.nifty.internal.math.Mat4;
import de.lessvoid.nifty.internal.math.Vec4;
import de.lessvoid.nifty.spi.NiftyRenderDevice;
import de.lessvoid.nifty.spi.NiftyRenderDevice.ArcParameters;

/**
 * An arc batch is a number of arc rendered with the same line style. Changing line styles will create a new batch.
 */
public class ArcBatch implements Batch<ArcParameters> {
  private final static int NUM_PRIMITIVES = 100;
  public final static int PRIMITIVE_SIZE = 4;

  private final FloatBuffer b;
  private final ArcParameters arcParameters;

  // Vec4 buffer data
  private final Vec4 vsrc = new Vec4();
  private final Vec4 vdst = new Vec4();

  public ArcBatch(final ArcParameters arcParameters) {
    this.b = createBuffer(NUM_PRIMITIVES * PRIMITIVE_SIZE);
    this.arcParameters = new ArcParameters(arcParameters);
  }

  @Override
  public void render(final NiftyRenderDevice renderDevice) {
    renderDevice.renderArcs(b, arcParameters);
  }

  @Override
  public boolean requiresNewBatch(final ArcParameters params) {
    return !arcParameters.equals(params) || (b.remaining() < PRIMITIVE_SIZE);
  }

  public boolean add(final double centerX, final double centerY, final double radius, final Mat4 mat) {
    float lw = arcParameters.getLineParameters().getLineWidth() / 2 + 1; // 1 additional px because of anti-aliasing
    float uv = (float) ((arcParameters.getLineParameters().getLineWidth() / 2) / radius) + 1.f;
    addTransformed((float) (centerX - radius - lw), (float) (centerY + radius + lw), -uv, -uv, mat);
    addTransformed((float) (centerX - radius - lw), (float) (centerY - radius - lw), -uv,  uv, mat);
    addTransformed((float) (centerX + radius + lw), (float) (centerY + radius + lw),  uv, -uv, mat);
    addTransformed((float) (centerX + radius + lw), (float) (centerY - radius - lw),  uv,  uv, mat);
    return true;
  }

  private void addTransformed(final float x, final float y, final float u, final float v, final Mat4 mat) {
    vsrc.x = (float) x;
    vsrc.y = (float) y;
    vsrc.z = 0.0f;
    Mat4.transform(mat, vsrc, vdst);
    b.put(vdst.x);
    b.put(vdst.y);
    b.put(u);
    b.put(v);
  }

  private FloatBuffer createBuffer(final int size) {
    return ByteBuffer.allocateDirect(size << 2).order(ByteOrder.nativeOrder()).asFloatBuffer();
  }
}
