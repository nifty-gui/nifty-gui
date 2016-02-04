package de.lessvoid.nifty.renderer.lwjgl3.render;

import de.lessvoid.nifty.render.batch.core.BatchRenderBackendCoreProfileInternal;
import de.lessvoid.nifty.render.batch.spi.BatchRenderBackend;

import javax.annotation.Nonnull;

/**
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class Lwjgl3BatchRenderBackendCoreProfileFactory {
    
  @Nonnull
  public static BatchRenderBackend create(final long glfwWindow) {
    return new BatchRenderBackendCoreProfileInternal(
            new Lwjgl3CoreGL(),
            new Lwjgl3BufferFactory(),
            new Lwjgl3ImageFactory(),
            new Lwjgl3MouseCursorFactory(glfwWindow));
  }
}

