package de.lessvoid.nifty.renderer.lwjgl.render;

import de.lessvoid.nifty.render.batch.spi.BatchRenderBackend.Image;
import de.lessvoid.nifty.render.batch.spi.ImageFactory;

import java.nio.ByteBuffer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class LwjglImageFactory implements ImageFactory {
  @Nonnull
  @Override
  public Image create(@Nullable final ByteBuffer buffer, final int imageWidth, final int imageHeight) {
    return new LwjglImage(buffer, imageWidth, imageHeight);
  }

  @Nullable
  @Override
  public ByteBuffer asByteBuffer(@Nullable final Image image) {
    return image instanceof LwjglImage ? ((LwjglImage)image).getBuffer() : null;
  }
}
