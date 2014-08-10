package de.lessvoid.nifty.internal.render.batch;

import de.lessvoid.nifty.spi.NiftyRenderDevice;

/**
 * A Batch stores pre-transformed vertices that share the same rendering state and that can be rendered.
 */
public interface Batch<T> {

  /**
   * Render this Batch using the given RenderDevice.
   * @param renderDevice the RenderDevice to render the batch to
   */
  void render(NiftyRenderDevice renderDevice);

  /**
   * Given the parameters param this Batch should check if the state of the params is the same as the state stored in
   * this Batch. If the state is the same the method is supposed to return true. In that case the Batch is reused. If
   * the state is different the method should return false and a new Batch will be created.
   *
   * Note: The method can return true if other, batch internal properties require the use of a new Batch as well. Like
   * a full Batch.
   *
   * @param param the Parameters to check the Batch for
   * @return true when the parameters are different from the ones stored inside the Batch or other criteria will require
   * a new Batch.
   */
  boolean requiresNewBatch(T param);
}
