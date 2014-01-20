package de.lessvoid.nifty.batch.core;

import de.lessvoid.nifty.batch.spi.BufferFactory;
import de.lessvoid.nifty.batch.spi.core.CoreBatch;
import de.lessvoid.nifty.batch.spi.core.CoreGL;

import javax.annotation.Nonnull;

/**
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public interface CoreBatchFactory {
  @Nonnull
  public CoreBatch create(
          @Nonnull final CoreGL gl,
          @Nonnull final CoreShader shader,
          @Nonnull final BufferFactory bufferFactory,
          final int primitiveRestartIndex);
}
