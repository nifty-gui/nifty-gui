package de.lessvoid.nifty.gdx.render.batch;

import de.lessvoid.nifty.batch.BatchRenderBackendInternal;
import de.lessvoid.nifty.gdx.render.GdxBufferFactory;
import de.lessvoid.nifty.gdx.render.GdxGL;
import de.lessvoid.nifty.gdx.render.GdxImageFactory;
import de.lessvoid.nifty.gdx.render.GdxMouseCursorFactory;

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
                    new GdxBatchFactory(),
                    new GdxBufferFactory(),
                    new GdxImageFactory(),
                    new GdxMouseCursorFactory()));
  }
}
