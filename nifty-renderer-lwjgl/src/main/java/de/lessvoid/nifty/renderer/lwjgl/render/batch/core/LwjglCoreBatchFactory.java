package de.lessvoid.nifty.renderer.lwjgl.render.batch.core;

import de.lessvoid.nifty.batch.core.CoreBatchFactory;
import de.lessvoid.nifty.batch.core.CoreBatchInternal;
import de.lessvoid.nifty.batch.core.CoreShader;
import de.lessvoid.nifty.batch.spi.BufferFactory;
import de.lessvoid.nifty.batch.spi.core.CoreBatch;
import de.lessvoid.nifty.batch.spi.core.CoreGL;

import javax.annotation.Nonnull;

/**
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class LwjglCoreBatchFactory implements CoreBatchFactory {
  @Nonnull
  @Override
  public CoreBatch create(
          @Nonnull final CoreGL gl,
          @Nonnull final CoreShader shader,
          @Nonnull final BufferFactory bufferFactory,
          final int primitiveRestartIndex) {
    return new LwjglCoreBatch(new CoreBatchInternal(gl, shader, bufferFactory, primitiveRestartIndex));
  }
}
