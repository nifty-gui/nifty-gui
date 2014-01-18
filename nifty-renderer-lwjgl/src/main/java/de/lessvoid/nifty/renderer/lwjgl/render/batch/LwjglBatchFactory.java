package de.lessvoid.nifty.renderer.lwjgl.render.batch;

import de.lessvoid.nifty.batch.OpenGLBatch;
import de.lessvoid.nifty.batch.spi.Batch;
import de.lessvoid.nifty.batch.spi.BufferFactory;
import de.lessvoid.nifty.batch.spi.GL;
import de.lessvoid.nifty.batch.spi.OpenGLBatchFactory;

import javax.annotation.Nonnull;

/**
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class LwjglBatchFactory implements OpenGLBatchFactory {
  @Nonnull
  @Override
  public Batch create(@Nonnull final GL gl, @Nonnull final BufferFactory bufferFactory) {
    return new LwjglBatch(new OpenGLBatch(gl, bufferFactory));
  }
}
