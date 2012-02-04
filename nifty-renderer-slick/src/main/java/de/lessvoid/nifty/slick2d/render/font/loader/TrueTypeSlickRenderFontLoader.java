package de.lessvoid.nifty.slick2d.render.font.loader;

import java.awt.*;

import de.lessvoid.nifty.slick2d.render.font.SlickLoadFontException;
import de.lessvoid.nifty.slick2d.render.font.SlickRenderFont;
import org.newdawn.slick.Graphics;

/**
 * This loader is able to load fonts that base on TrueType fonts.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 * @deprecated This loader uses the {@link org.newdawn.slick.TrueTypeFont} that is marked as deprecated
 */
@SuppressWarnings("UnnecessaryFullyQualifiedName")
@Deprecated
public final class TrueTypeSlickRenderFontLoader extends AbstractJavaSlickRenderFontLoader {
  /**
   * Load the requested font.
   */
  @Override
  public SlickRenderFont loadFont(final Graphics g, final String filename) throws SlickLoadFontException {
    final Font javaFont;
    try {
      javaFont = loadJavaFont(filename);
    } catch (final Exception e) {
      throw new SlickLoadFontException("Can't find font resource", e);
    }
    if (javaFont == null) {
      throw new SlickLoadFontException("Can't load font as true type.");
    }

    return new de.lessvoid.nifty.slick2d.render.font.TrueTypeSlickRenderFont(
        new org.newdawn.slick.TrueTypeFont(javaFont, true), javaFont);
  }

}
