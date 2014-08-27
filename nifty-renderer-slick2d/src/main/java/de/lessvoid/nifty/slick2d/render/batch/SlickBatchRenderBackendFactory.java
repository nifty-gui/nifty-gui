package de.lessvoid.nifty.slick2d.render.batch;

import de.lessvoid.nifty.render.batch.BatchRenderBackendInternal;
import de.lessvoid.nifty.render.batch.spi.BatchRenderBackend;

import javax.annotation.Nonnull;

/**
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class SlickBatchRenderBackendFactory {
  @Nonnull
  public static BatchRenderBackend create() {
    return new BatchRenderBackendInternal(
            new SlickGL(),
            new SlickBufferFactory(),
            new SlickImageFactory(),
            new SlickMouseCursorFactory());
  }
}
