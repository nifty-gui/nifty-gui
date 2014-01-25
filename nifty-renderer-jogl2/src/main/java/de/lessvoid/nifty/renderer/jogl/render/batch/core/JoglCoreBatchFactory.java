package de.lessvoid.nifty.renderer.jogl.render.batch.core;

import de.lessvoid.nifty.render.batch.core.CoreBatchFactory;
import de.lessvoid.nifty.render.batch.core.CoreBatchInternal;
import de.lessvoid.nifty.render.batch.core.CoreShader;
import de.lessvoid.nifty.render.batch.spi.BufferFactory;
import de.lessvoid.nifty.render.batch.spi.core.CoreBatch;
import de.lessvoid.nifty.render.batch.spi.core.CoreGL;

import javax.annotation.Nonnull;

/**
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class JoglCoreBatchFactory implements CoreBatchFactory {
  @Nonnull
  @Override
  public CoreBatch create(
          @Nonnull final CoreGL gl,
          @Nonnull final CoreShader shader,
          @Nonnull final BufferFactory bufferFactory,
          final int primitiveRestartIndex) {
    return new JoglCoreBatch(new CoreBatchInternal(gl, shader, bufferFactory, primitiveRestartIndex));
  }
}
