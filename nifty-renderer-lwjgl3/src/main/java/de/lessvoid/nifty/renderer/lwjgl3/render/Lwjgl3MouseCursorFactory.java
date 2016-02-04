package de.lessvoid.nifty.renderer.lwjgl3.render;

import de.lessvoid.nifty.render.batch.spi.MouseCursorFactory;
import de.lessvoid.nifty.spi.render.MouseCursor;
import de.lessvoid.nifty.tools.resourceloader.NiftyResourceLoader;

import java.io.IOException;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class Lwjgl3MouseCursorFactory implements MouseCursorFactory {
  
  public final long glfwWindow;
  
  public Lwjgl3MouseCursorFactory (final long glfwWindow) {
    this.glfwWindow = glfwWindow;
  }

  @Nullable
  @Override
  public MouseCursor create(
          @Nonnull final String filename,
          final int hotspotX,
          final int hotspotY,
          @Nonnull final NiftyResourceLoader resourceLoader) throws IOException {
    return new Lwjgl3MouseCursor(glfwWindow, filename, hotspotX, hotspotY, resourceLoader);
  }
}
