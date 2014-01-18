package de.lessvoid.nifty.renderer.jogl.render;

import de.lessvoid.nifty.batch.spi.ImageFactory;
import de.lessvoid.nifty.batch.spi.BatchRenderBackend.Image;

import java.nio.ByteBuffer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class JoglImageFactory implements ImageFactory {
  @Nonnull
  @Override
  public Image create(@Nullable final ByteBuffer buffer, final int imageWidth, final int imageHeight) {
    return new JoglImage(buffer, imageWidth, imageHeight);
  }

  @Nullable
  @Override
  public ByteBuffer asByteBuffer(@Nullable final Image image) {
    return image instanceof JoglImage ? ((JoglImage)image).getBuffer() : null;
  }
}
