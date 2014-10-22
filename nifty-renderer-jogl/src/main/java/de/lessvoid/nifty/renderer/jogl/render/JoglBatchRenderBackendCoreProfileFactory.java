package de.lessvoid.nifty.renderer.jogl.render;

import javax.annotation.Nonnull;

import com.jogamp.newt.Window;

import de.lessvoid.nifty.render.batch.core.BatchRenderBackendCoreProfileInternal;
import de.lessvoid.nifty.render.batch.spi.BatchRenderBackend;

/**
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class JoglBatchRenderBackendCoreProfileFactory {
	
  @Nonnull
  public static BatchRenderBackend create(Window newtWindow) {
    return new BatchRenderBackendCoreProfileInternal(
            new JoglCoreGL(),
            new JoglBufferFactory(),
            new JoglImageFactory(),
            new JoglMouseCursorFactory(newtWindow));
  }
}

