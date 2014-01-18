package de.lessvoid.nifty.batch.spi;

import javax.annotation.Nonnull;

/**
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public interface OpenGLBatchFactory {
  @Nonnull
  public Batch create(@Nonnull final GL gl, @Nonnull final BufferFactory bufferFactory);
}
