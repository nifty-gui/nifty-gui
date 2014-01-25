package de.lessvoid.nifty.renderer.jogl.render.batch.core;

import de.lessvoid.nifty.render.batch.core.BatchRenderBackendCoreProfileInternal;
import de.lessvoid.nifty.render.batch.spi.BatchRenderBackend;
import de.lessvoid.nifty.renderer.jogl.render.JoglBufferFactory;
import de.lessvoid.nifty.renderer.jogl.render.JoglImageFactory;
import de.lessvoid.nifty.renderer.jogl.render.JoglMouseCursorFactory;

import javax.annotation.Nonnull;

/**
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class JoglBatchRenderBackendCoreProfileFactory {
  @Nonnull
  public static BatchRenderBackend create() {
    return new JoglBatchRenderBackendCoreProfile(
            new BatchRenderBackendCoreProfileInternal(
                    new JoglCoreGL(),
                    new JoglCoreBatchFactory(),
                    new JoglBufferFactory(),
                    new JoglImageFactory(),
                    new JoglMouseCursorFactory()));
  }
}

