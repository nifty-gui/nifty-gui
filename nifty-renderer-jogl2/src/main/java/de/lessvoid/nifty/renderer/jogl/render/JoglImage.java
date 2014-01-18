package de.lessvoid.nifty.renderer.jogl.render;

import de.lessvoid.nifty.batch.spi.BatchRenderBackend;

import java.nio.ByteBuffer;

/**
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class JoglImage extends BatchRenderBackend.ByteBufferedImage {
  public JoglImage(ByteBuffer buffer, int width, int height) {
    super(buffer, width, height);
  }
}
