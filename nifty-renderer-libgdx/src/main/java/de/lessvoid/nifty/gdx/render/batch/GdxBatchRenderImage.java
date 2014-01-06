package de.lessvoid.nifty.gdx.render.batch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import de.lessvoid.nifty.batch.spi.BatchRenderBackend;
import de.lessvoid.nifty.batch.spi.BatchRendererTexture;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.nio.ByteBuffer;

/**
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class GdxBatchRenderImage implements BatchRendererTexture.Image {
  @Nullable
  private Pixmap pixmap;
  private final ByteBuffer buffer;
  private final int width;
  private final int height;

  public GdxBatchRenderImage(@Nullable final String filename) {
    this.buffer = null;
    this.width = 0;
    this.height = 0;

    if (filename == null) {
      return;
    }
    pixmap = new Pixmap(Gdx.files.internal(filename));
    pixmap = convertPixmapToFormat(pixmap, Pixmap.Format.RGBA8888);
  }

  public GdxBatchRenderImage(final ByteBuffer buffer, final int width, final int height) {
    this.buffer = buffer;
    this.width = width;
    this.height = height;
  }

  @Override
  public int getWidth() {
    return pixmap != null ? pixmap.getWidth() : width;
  }

  @Override
  public int getHeight() {
    return pixmap != null ? pixmap.getHeight() : height;
  }

  @Override
  public ByteBuffer getData() {
    return buffer;
  }

  @Nullable
  public ByteBuffer asByteBuffer() {
    return pixmap != null ? pixmap.getPixels() : buffer;
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
