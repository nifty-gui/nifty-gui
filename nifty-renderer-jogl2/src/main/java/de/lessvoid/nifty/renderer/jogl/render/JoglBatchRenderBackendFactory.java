package de.lessvoid.nifty.renderer.jogl.render;

import de.lessvoid.nifty.render.batch.BatchRenderBackendInternal;
import de.lessvoid.nifty.render.batch.spi.BatchRenderBackend;
import de.lessvoid.nifty.renderer.jogl.render.JoglBufferFactory;
import de.lessvoid.nifty.renderer.jogl.render.JoglGL;
import de.lessvoid.nifty.renderer.jogl.render.JoglImageFactory;
import de.lessvoid.nifty.renderer.jogl.render.JoglMouseCursorFactory;

import javax.annotation.Nonnull;

/**
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class JoglBatchRenderBackendFactory {
  @Nonnull
  public static BatchRenderBackend create() {
    return  new BatchRenderBackendInternal(
            new JoglGL(),
            new JoglBufferFactory(),
            new JoglImageFactory(),
            new JoglMouseCursorFactory());
  }
}
