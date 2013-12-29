package de.lessvoid.nifty.gdx.render.batch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import de.lessvoid.nifty.batch.spi.BatchRenderBackend;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.nio.ByteBuffer;

/**
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class GdxBatchRenderImage extends BatchRenderBackend.ByteBufferedImage implements BatchRenderBackend.Image {
  @Nullable
  private Pixmap pixmap;

  public GdxBatchRenderImage(@Nullable final String filename) {
    if (filename == null) {
      return;
    }
    pixmap = new Pixmap(Gdx.files.internal(filename));
    pixmap = convertPixmapToFormat(pixmap, Pixmap.Format.RGBA8888);
  }

  public GdxBatchRenderImage(final ByteBuffer buffer, final int width, final int height) {
    super(buffer, width, height);
  }

  @Override
  public int getWidth() {
    return pixmap != null ? pixmap.getWidth() : super.getWidth();
  }

  @Override
  public int getHeight() {
    return pixmap != null ? pixmap.getHeight() : super.getHeight();
  }

  @Nullable
  public ByteBuffer asByteBuffer() {
    return pixmap != null ? pixmap.getPixels() : super.getBuffer();
  }

  @Nullable
  public Pixmap asPixmap() {
    return pixmap;
  }

  /**
   * Disposes of the underlying image data. You can still safely call {@link #getWidth()} & {@link #getHeight()}, but
   * they will return 0, and {@link #asByteBuffer()} will return null.
   */
  public void dispose() {
    if (pixmap != null) {
      pixmap.dispose();
      pixmap = null;
    }
  }

  // internal implementations

  @Nonnull
  private Pixmap convertPixmapToFormat(@Nonnull Pixmap pixmap, Pixmap.Format format) {
    if (pixmap.getFormat() == format) {
      return pixmap;
    }
    Pixmap temp = new Pixmap(pixmap.getWidth(), pixmap.getHeight(), format);
    temp.drawPixmap(pixmap, 0, 0);
    pixmap.dispose();
    return temp;
  }
}
