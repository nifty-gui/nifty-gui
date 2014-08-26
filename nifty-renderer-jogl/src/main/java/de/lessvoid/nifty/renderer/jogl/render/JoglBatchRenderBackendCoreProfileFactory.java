package de.lessvoid.nifty.renderer.jogl.render;

import de.lessvoid.nifty.render.batch.core.BatchRenderBackendCoreProfileInternal;
import de.lessvoid.nifty.render.batch.spi.BatchRenderBackend;

import javax.annotation.Nonnull;

/**
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class JoglBatchRenderBackendCoreProfileFactory {
  @Nonnull
  public static BatchRenderBackend create() {
    return new BatchRenderBackendCoreProfileInternal(
            new JoglCoreGL(),
            new JoglBufferFactory(),
            new JoglImageFactory(),
            new JoglMouseCursorFactory());
  }
}

