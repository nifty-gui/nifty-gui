package de.lessvoid.nifty.renderer.lwjgl.render;

import de.lessvoid.nifty.batch.spi.BatchRenderBackend;

import java.nio.ByteBuffer;

/**
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class LwjglImage extends BatchRenderBackend.ByteBufferedImage {
  public LwjglImage(ByteBuffer buffer, int width, int height) {
    super(buffer, width, height);
  }
}
