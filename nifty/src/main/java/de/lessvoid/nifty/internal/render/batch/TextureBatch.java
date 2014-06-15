package de.lessvoid.nifty.internal.render.batch;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import de.lessvoid.nifty.api.NiftyColor;
import de.lessvoid.nifty.internal.math.Mat4;
import de.lessvoid.nifty.internal.math.Vec4;
import de.lessvoid.nifty.spi.NiftyRenderDevice;
import de.lessvoid.nifty.spi.NiftyTexture;

/**
 *
 */
public class TextureBatch implements Batch {
  private final static int NUM_PRIMITIVES = 1000;
  public final static int PRIMITIVE_SIZE = 8 * 6;

  private final FloatBuffer vertices;
  private final NiftyTexture texture;
  private final int atlasId;

  // Vec4 buffer data
  private final Vec4 vsrc = new Vec4();
  private final Vec4 vdst = new Vec4();

  public TextureBatch(final NiftyTexture texture) {
    this.atlasId = texture.getAtlasId();
    this.texture = texture;
    this.vertices = createBuffer(NUM_PRIMITIVES * PRIMITIVE_SIZE);
  }

  public boolean needsNewBatch(final NiftyTexture niftyTexture) {
    return niftyTexture.getAtlasId() != atlasId;
  }

  public void reset() {
    this.vertices.clear();
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
      final Mat4 mat,
      final NiftyColor color) {
    // FIXME check number of primitives and reject adding if we would overflow

    addTransformed(x,         y,          mat); vertices.put((float) u0); vertices.put((float) v0); addColor(color);
    addTransformed(x,         y + height, mat); vertices.put((float) u0); vertices.put((float) v1); addColor(color);
    addTransformed(x + width, y,          mat); vertices.put((float) u1); vertices.put((float) v0); addColor(color);

    addTransformed(x + width, y,          mat); vertices.put((float) u1); vertices.put((float) v0); addColor(color);
    addTransformed(x,         y + height, mat); vertices.put((float) u0); vertices.put((float) v1); addColor(color);
    addTransformed(x + width, y + height, mat); vertices.put((float) u1); vertices.put((float) v1); addColor(color);

    return true;
  }

  @Override
  public void render(final NiftyRenderDevice renderDevice) {
    renderDevice.render(texture, vertices);
  }

  private void addTransformed(final double x, final double y, final Mat4 mat) {
    vsrc.x = (float) x;
    vsrc.y = (float) y;
    vsrc.z = 0.0f;
    Mat4.transform(mat, vsrc, vdst);
    vertices.put(vdst.x);
    vertices.put(vdst.y);
  }

  private FloatBuffer createBuffer(final int size) {
    return ByteBuffer.allocateDirect(size << 2).order(ByteOrder.nativeOrder()).asFloatBuffer();
  }

  private void addColor(final NiftyColor color) {
    vertices.put((float) color.getRed());
    vertices.put((float) color.getGreen());
    vertices.put((float) color.getBlue());
    vertices.put((float) color.getAlpha());
  }
}
