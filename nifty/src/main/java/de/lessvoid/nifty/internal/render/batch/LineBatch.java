package de.lessvoid.nifty.internal.render.batch;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import de.lessvoid.nifty.internal.math.Mat4;
import de.lessvoid.nifty.internal.math.Vec4;
import de.lessvoid.nifty.spi.NiftyRenderDevice;
import de.lessvoid.nifty.spi.NiftyRenderDevice.LineParameters;

/**
 * A line batch is a number of lines rendered with the same cap and join styles. Changing cap or join style will
 * create a new batch.
 */
public class LineBatch implements Batch<LineParameters> {
  private final static int NUM_PRIMITIVES = 100;
  public final static int PRIMITIVE_SIZE = 2;

  private final FloatBuffer b;
  private final LineParameters lineParameters;

  // Vec4 buffer data
  private final Vec4 vsrc = new Vec4();
  private final Vec4 vdst = new Vec4();
  private boolean isStartPathBatch;
  private boolean isEndPathBatch;

  public LineBatch(final LineParameters lineParameters) {
    this.b = createBuffer(NUM_PRIMITIVES * PRIMITIVE_SIZE);
    this.lineParameters = new LineParameters(lineParameters);
  }

  @Override
  public void render(final NiftyRenderDevice renderDevice) {
    if (isStartPathBatch) {
      renderDevice.pathBegin(lineParameters);
    }

    renderDevice.pathLines(b, lineParameters);

    if (isEndPathBatch) {
      renderDevice.pathEnd(lineParameters);
    }
  }

  @Override
  public boolean requiresNewBatch(final LineParameters params) {
    return !lineParameters.equals(params) || (b.remaining() < PRIMITIVE_SIZE);
  }

  public boolean add(final float x, final float y, final Mat4 mat) {
    addTransformed(x, y, mat);
    return true;
  }

  private void addTransformed(final float x, final float y, final Mat4 mat) {
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

  public void enableStartPathBatch() {
    isStartPathBatch = true;
  }

  public void enableEndPathBatch() {
    isEndPathBatch = true;
  }
}
