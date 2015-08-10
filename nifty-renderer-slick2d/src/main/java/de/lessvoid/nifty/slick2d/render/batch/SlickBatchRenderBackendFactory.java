package de.lessvoid.nifty.slick2d.render.batch;

import de.lessvoid.nifty.render.batch.spi.BatchRenderBackend;
import org.newdawn.slick.GameContainer;

import javax.annotation.Nonnull;

/**
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class SlickBatchRenderBackendFactory {
  @Nonnull
  public static BatchRenderBackend create(@Nonnull final GameContainer container) {
    return new SlickBatchRenderBackend(
            new SlickGL(),
            new SlickBufferFactory(),
            new SlickImageFactory(),
            new SlickMouseCursorFactory(container),
            container);
  }
}
