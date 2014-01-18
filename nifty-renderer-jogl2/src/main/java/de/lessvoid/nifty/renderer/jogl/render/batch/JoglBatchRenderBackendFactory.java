package de.lessvoid.nifty.renderer.jogl.render.batch;

import de.lessvoid.nifty.batch.OpenGLBatchRenderBackend;
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
  public static JoglBatchRenderBackend create() {
    return new JoglBatchRenderBackend(
            new OpenGLBatchRenderBackend(
                    new JoglGL(),
                    new JoglBatchFactory(),
                    new JoglBufferFactory(),
                    new JoglImageFactory(),
                    new JoglMouseCursorFactory()));
  }
}
