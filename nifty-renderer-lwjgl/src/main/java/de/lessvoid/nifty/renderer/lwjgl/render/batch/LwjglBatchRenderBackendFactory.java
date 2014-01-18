package de.lessvoid.nifty.renderer.lwjgl.render.batch;

import de.lessvoid.nifty.batch.OpenGLBatchRenderBackend;
import de.lessvoid.nifty.renderer.lwjgl.render.LwjglBufferFactory;
import de.lessvoid.nifty.renderer.lwjgl.render.LwjglGL;
import de.lessvoid.nifty.renderer.lwjgl.render.LwjglImageFactory;
import de.lessvoid.nifty.renderer.lwjgl.render.LwjglMouseCursorFactory;

import javax.annotation.Nonnull;

/**
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class LwjglBatchRenderBackendFactory {
  @Nonnull
  public static LwjglBatchRenderBackend create() {
    return new LwjglBatchRenderBackend(
            new OpenGLBatchRenderBackend(
                    new LwjglGL(),
                    new LwjglBatchFactory(),
                    new LwjglBufferFactory(),
                    new LwjglImageFactory(),
                    new LwjglMouseCursorFactory()));
  }
}
