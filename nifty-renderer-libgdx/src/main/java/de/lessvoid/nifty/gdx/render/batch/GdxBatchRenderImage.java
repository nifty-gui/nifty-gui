package de.lessvoid.nifty.gdx.render.batch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;

import de.lessvoid.nifty.batch.spi.BatchRenderBackend;

import java.nio.ByteBuffer;

/**
  * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
  */
public class GdxBatchRenderImage implements BatchRenderBackend.Image {
  private Pixmap pixmap;

  public GdxBatchRenderImage(final String filename) {
    if (filename == null) {
      return;
    }
    pixmap = new Pixmap(Gdx.files.internal(filename));
    pixmap = convertPixmapToFormat(pixmap, Pixmap.Format.RGBA8888);
  }

  @Override
  public int getWidth() {
    return pixmap != null? pixmap.getWidth() : 0;
  }

  @Override
  public int getHeight() {
    return pixmap != null? pixmap.getHeight() : 0;
  }

  public ByteBuffer asByteBuffer() {
    return pixmap != null? pixmap.getPixels() : null;
  }

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

  private Pixmap convertPixmapToFormat(Pixmap pixmap, Pixmap.Format format) {
    if(pixmap.getFormat() == format) {
      return pixmap;
    }
    Pixmap temp = new Pixmap (pixmap.getWidth(), pixmap.getHeight(), format);
    temp.drawPixmap(pixmap, 0, 0);
    pixmap.dispose();
    return temp;
  }
}
