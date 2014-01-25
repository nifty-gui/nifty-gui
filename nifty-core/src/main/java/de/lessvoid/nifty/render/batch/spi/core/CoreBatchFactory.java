package de.lessvoid.nifty.render.batch.spi.core;

import de.lessvoid.nifty.render.batch.core.CoreShader;
import de.lessvoid.nifty.render.batch.spi.BufferFactory;

import javax.annotation.Nonnull;

/**
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public interface CoreBatchFactory {
  @Nonnull
  public CoreBatch create(
          @Nonnull final CoreGL gl,
          @Nonnull final BufferFactory bufferFactory,
          @Nonnull final CoreShader shader,
          final int primitiveRestartIndex);
}
