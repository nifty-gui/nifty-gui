package de.lessvoid.nifty.renderer.lwjgl.render.batch.core;

import de.lessvoid.nifty.render.batch.core.BatchRenderBackendCoreProfileInternal;
import de.lessvoid.nifty.render.batch.spi.BatchRenderBackend;
import de.lessvoid.nifty.renderer.lwjgl.render.LwjglBufferFactory;
import de.lessvoid.nifty.renderer.lwjgl.render.LwjglImageFactory;
import de.lessvoid.nifty.renderer.lwjgl.render.LwjglMouseCursorFactory;

import javax.annotation.Nonnull;

/**
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class LwjglBatchRenderBackendCoreProfileFactory {
  @Nonnull
  public static BatchRenderBackend create() {
    return new LwjglBatchRenderBackendCoreProfile(
            new BatchRenderBackendCoreProfileInternal(
                    new LwjglCoreGL(),
                    new LwjglCoreBatchFactory(),
                    new LwjglBufferFactory(),
                    new LwjglImageFactory(),
                    new LwjglMouseCursorFactory()));
  }
}

