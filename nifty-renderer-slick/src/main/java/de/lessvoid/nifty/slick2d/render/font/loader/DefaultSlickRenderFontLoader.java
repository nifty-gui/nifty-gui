package de.lessvoid.nifty.slick2d.render.font.loader;

import de.lessvoid.nifty.slick2d.render.font.DefaultSlickRenderFont;
import de.lessvoid.nifty.slick2d.render.font.SlickLoadFontException;
import de.lessvoid.nifty.slick2d.render.font.SlickRenderFont;
import org.newdawn.slick.Graphics;

/**
 * The loader is able to load the default render font
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public final class DefaultSlickRenderFontLoader implements SlickRenderFontLoader {
  /**
   * Load the requested font.
   */
  @Override
  public SlickRenderFont loadFont(final Graphics g, final String filename) throws SlickLoadFontException {
    try {
      g.resetFont();
      return new DefaultSlickRenderFont(g.getFont());
    } catch (final SlickLoadFontException e) {
      throw new SlickLoadFontException("Loading font failed.", e);
    }
  }

}
