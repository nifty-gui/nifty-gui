package de.lessvoid.nifty.slick2d.render.batch;

import de.lessvoid.nifty.render.batch.spi.MouseCursorFactory;
import de.lessvoid.nifty.slick2d.render.cursor.SlickLoadCursorException;
import de.lessvoid.nifty.slick2d.render.cursor.loader.LwjglCursorSlickMouseCursorLoader;
import de.lessvoid.nifty.spi.render.MouseCursor;
import de.lessvoid.nifty.tools.resourceloader.NiftyResourceLoader;
import org.newdawn.slick.GameContainer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;

/**
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class SlickMouseCursorFactory implements MouseCursorFactory {
  @Nonnull
  private final LwjglCursorSlickMouseCursorLoader loader = new LwjglCursorSlickMouseCursorLoader();
  @Nonnull
  private final GameContainer gameContainer;

  public SlickMouseCursorFactory (@Nonnull final GameContainer gameContainer)
  {
    this.gameContainer = gameContainer;
  }

  @Nullable
  @Override
  public MouseCursor create(
          @Nonnull final String filename,
          final int hotspotX,
          final int hotspotY,
          @Nonnull final NiftyResourceLoader resourceLoader) throws IOException {
    try {
      return loader.loadCursor(filename, hotspotX, hotspotY, gameContainer);
    } catch (SlickLoadCursorException e) {
      throw new IOException(e);
    }
  }
}
