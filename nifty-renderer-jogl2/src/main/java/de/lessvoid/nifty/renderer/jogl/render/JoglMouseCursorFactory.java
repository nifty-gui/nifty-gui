package de.lessvoid.nifty.renderer.jogl.render;

import de.lessvoid.nifty.batch.spi.MouseCursorFactory;
import de.lessvoid.nifty.spi.render.MouseCursor;
import de.lessvoid.nifty.tools.resourceloader.NiftyResourceLoader;

import java.io.IOException;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class JoglMouseCursorFactory implements MouseCursorFactory {
  @Nullable
  @Override
  public MouseCursor create(
          @Nonnull String filename,
          int hotspotX,
          int hotspotY,
          @Nonnull NiftyResourceLoader resourceLoader) throws IOException {
    return new JoglMouseCursor(filename, hotspotX, hotspotY, resourceLoader);
  }
}
