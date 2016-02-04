package de.lessvoid.nifty.renderer.lwjgl3.render;

import de.lessvoid.nifty.render.batch.BatchRenderBackendInternal;
import de.lessvoid.nifty.render.batch.spi.BatchRenderBackend;

import javax.annotation.Nonnull;

/**
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class Lwjgl3BatchRenderBackendFactory {
  @Nonnull
  public static BatchRenderBackend create(final long glfwWindow) {
    return new BatchRenderBackendInternal(
            new Lwjgl3GL(),
            new Lwjgl3BufferFactory(),
            new Lwjgl3ImageFactory(),
            new Lwjgl3MouseCursorFactory(glfwWindow));
  }
}
