package de.lessvoid.nifty.gdx.render.batch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;

import de.lessvoid.nifty.batch.spi.BatchRenderBackend;

import java.nio.ByteBuffer;
import java.util.logging.Logger;

/**
  * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
  */
public class GdxBatchRenderImage implements BatchRenderBackend.Image {
  private static Logger log = Logger.getLogger(GdxBatchRenderImage.class.getName());
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
    return pixmap == null? 0 : pixmap.getWidth();
  }

  @Override
  public int getHeight() {
    return pixmap == null? 0 : pixmap.getHeight();
  }

  public ByteBuffer getData() {
    return pixmap == null? null : pixmap.getPixels();
  }

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
