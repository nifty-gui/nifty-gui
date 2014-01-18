package de.lessvoid.nifty.batch.spi;

import de.lessvoid.nifty.batch.spi.BatchRenderBackend.Image;

import java.nio.ByteBuffer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public interface ImageFactory {
  /**
   * You must return a non-null instance of your {@link de.lessvoid.nifty.batch.spi.BatchRenderBackend.Image}
   * implementation, even if buffer is null.
   */
  @Nonnull
  public Image create(@Nullable final ByteBuffer buffer, final int imageWidth, final int imageHeight);

  /**
   * Get the image data in {@link java.nio.ByteBuffer} format.
   */
  @Nullable
  public ByteBuffer asByteBuffer(@Nullable final Image image);
}
