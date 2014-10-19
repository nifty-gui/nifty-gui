package de.lessvoid.nifty.renderer.jogl.render;

import javax.annotation.Nonnull;

import com.jogamp.newt.Window;

import de.lessvoid.nifty.render.batch.BatchRenderBackendInternal;
import de.lessvoid.nifty.render.batch.spi.BatchRenderBackend;

/**
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class JoglBatchRenderBackendFactory {
  @Nonnull
  public static BatchRenderBackend create(Window newtWindow) {
    return  new BatchRenderBackendInternal(
            new JoglGL(),
            new JoglBufferFactory(),
            new JoglImageFactory(),
            new JoglMouseCursorFactory(newtWindow));
  }
}
