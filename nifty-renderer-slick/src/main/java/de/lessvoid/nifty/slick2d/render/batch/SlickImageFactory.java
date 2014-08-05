package de.lessvoid.nifty.slick2d.render.batch;

import de.lessvoid.nifty.render.batch.spi.BatchRenderBackend.Image;
import de.lessvoid.nifty.render.batch.spi.ImageFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.nio.ByteBuffer;

/**
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class SlickImageFactory implements ImageFactory {
  @Nonnull
  @Override
  public Image create(@Nullable final ByteBuffer buffer, final int imageWidth, final int imageHeight) {
    return new SlickImage(buffer, imageWidth, imageHeight);
  }

  @Nullable
  @Override
  public ByteBuffer asByteBuffer(@Nullable final Image image) {
    return image instanceof SlickImage ? ((SlickImage)image).getBuffer() : null;
  }
}
