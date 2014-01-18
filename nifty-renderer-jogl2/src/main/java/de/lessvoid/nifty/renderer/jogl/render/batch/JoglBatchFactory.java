package de.lessvoid.nifty.renderer.jogl.render.batch;

import de.lessvoid.nifty.batch.spi.Batch;
import de.lessvoid.nifty.batch.OpenGLBatch;
import de.lessvoid.nifty.batch.spi.OpenGLBatchFactory;
import de.lessvoid.nifty.batch.spi.BufferFactory;
import de.lessvoid.nifty.batch.spi.GL;

import javax.annotation.Nonnull;

/**
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class JoglBatchFactory implements OpenGLBatchFactory {
  @Nonnull
  @Override
  public Batch create(@Nonnull final GL gl, @Nonnull final BufferFactory bufferFactory) {
    return new JoglBatch(new OpenGLBatch(gl, bufferFactory));
  }
}
