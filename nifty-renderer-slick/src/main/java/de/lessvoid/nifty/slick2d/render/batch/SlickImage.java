package de.lessvoid.nifty.slick2d.render.batch;

import de.lessvoid.nifty.render.batch.spi.BatchRenderBackend;

import java.nio.ByteBuffer;

/**
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class SlickImage extends BatchRenderBackend.ByteBufferedImage {
  public SlickImage(ByteBuffer buffer, int width, int height) {
    super(buffer, width, height);
  }
}
