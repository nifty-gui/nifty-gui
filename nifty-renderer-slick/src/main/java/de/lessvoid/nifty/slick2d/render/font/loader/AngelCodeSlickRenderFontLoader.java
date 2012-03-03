package de.lessvoid.nifty.slick2d.render.font.loader;

import de.lessvoid.nifty.slick2d.render.font.AngelCodeSlickRenderFont;
import de.lessvoid.nifty.slick2d.render.font.SlickLoadFontException;
import de.lessvoid.nifty.slick2d.render.font.SlickRenderFont;
import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

/**
 * The loader is able to load render fonts that are based on angel code fonts.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public final class AngelCodeSlickRenderFontLoader implements SlickRenderFontLoader {
  /**
   * Load the requested font.
   */
  @Override
  public SlickRenderFont loadFont(final Graphics g, final String filename) throws SlickLoadFontException {
    final String definition = filename.substring(0, filename.lastIndexOf('.') + 1) + "fnt";

    try {
      return new AngelCodeSlickRenderFont(new AngelCodeFont(filename, definition));
    } catch (final SlickLoadFontException e) {
      throw new SlickLoadFontException("Loading font failed.", e);
    } catch (final SlickException e) {
      throw new SlickLoadFontException("Loading font failed.", e);
    }
  }

}
