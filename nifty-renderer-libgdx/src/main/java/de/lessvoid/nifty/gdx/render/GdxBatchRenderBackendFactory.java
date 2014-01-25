package de.lessvoid.nifty.gdx.render;

import de.lessvoid.nifty.render.batch.BatchRenderBackendInternal;

import javax.annotation.Nonnull;

/**
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class GdxBatchRenderBackendFactory {
  @Nonnull
  public static GdxBatchRenderBackend create() {
    return new GdxBatchRenderBackend(
            new BatchRenderBackendInternal(
                    new GdxGL(),
                    new GdxBufferFactory(),
                    new GdxImageFactory(),
                    new GdxMouseCursorFactory()));
  }
}
