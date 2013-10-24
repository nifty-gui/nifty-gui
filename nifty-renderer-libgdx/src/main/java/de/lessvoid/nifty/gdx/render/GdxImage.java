package de.lessvoid.nifty.gdx.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;

import de.lessvoid.nifty.batch.spi.BatchRenderBackend;

import java.nio.ByteBuffer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * {@inheritDoc}
 *
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class GdxImage extends BatchRenderBackend.ByteBufferedImage {
  private Pixmap pixmap;

  public GdxImage(@Nullable final String filename) {
    if (filename == null) {
      return;
    }
    pixmap = new Pixmap(Gdx.files.internal(filename));
    pixmap = convertPixmapToFormat(pixmap, Pixmap.Format.RGBA8888);
  }

  public GdxImage(final ByteBuffer buffer, final int width, final int height) {
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

  @Override
  public ByteBuffer getBuffer() {
    return pixmap != null ? pixmap.getPixels() : super.getBuffer();
  }

  public boolean hasPixmap() {
    return pixmap != null;
  }

  @Nullable
  public Pixmap getPixmap() {
    return pixmap;
  }

  public void dispose() {
    if (pixmap != null) {
      pixmap.dispose();
      pixmap = null;
    }
  }

  // Internal implementations

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
