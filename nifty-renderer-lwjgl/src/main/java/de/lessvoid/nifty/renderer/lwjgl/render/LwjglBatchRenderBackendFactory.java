package de.lessvoid.nifty.renderer.lwjgl.render;

import de.lessvoid.nifty.render.batch.BatchRenderBackendInternal;
import de.lessvoid.nifty.render.batch.spi.BatchRenderBackend;

import javax.annotation.Nonnull;

/**
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class LwjglBatchRenderBackendFactory {
  @Nonnull
  public static BatchRenderBackend create() {
    return new BatchRenderBackendInternal(
            new LwjglGL(),
            new LwjglBufferFactory(),
            new LwjglImageFactory(),
            new LwjglMouseCursorFactory());
  }
}
