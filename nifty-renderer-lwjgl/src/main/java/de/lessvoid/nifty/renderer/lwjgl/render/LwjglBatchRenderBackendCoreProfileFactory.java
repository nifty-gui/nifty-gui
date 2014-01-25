package de.lessvoid.nifty.renderer.lwjgl.render;

import de.lessvoid.nifty.render.batch.core.BatchRenderBackendCoreProfileInternal;
import de.lessvoid.nifty.render.batch.spi.BatchRenderBackend;

import javax.annotation.Nonnull;

/**
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class LwjglBatchRenderBackendCoreProfileFactory {
  @Nonnull
  public static BatchRenderBackend create() {
    return new BatchRenderBackendCoreProfileInternal(
            new LwjglCoreGL(),
            new LwjglBufferFactory(),
            new LwjglImageFactory(),
            new LwjglMouseCursorFactory());
  }
}

