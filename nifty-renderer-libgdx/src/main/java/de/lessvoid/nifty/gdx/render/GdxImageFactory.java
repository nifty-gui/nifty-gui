package de.lessvoid.nifty.gdx.render;

import de.lessvoid.nifty.render.batch.spi.ImageFactory;
import de.lessvoid.nifty.render.batch.spi.BatchRenderBackend.Image;

import java.nio.ByteBuffer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class GdxImageFactory implements ImageFactory {
  @Nonnull
  @Override
  public Image create(@Nullable final ByteBuffer buffer, final int imageWidth, final int imageHeight) {
    return new GdxImage(buffer, imageWidth, imageHeight);
  }

  @Nullable
  @Override
  public ByteBuffer asByteBuffer(@Nullable final Image image) {
    return image instanceof GdxImage ? ((GdxImage)image).getBuffer() : null;
  }
}
