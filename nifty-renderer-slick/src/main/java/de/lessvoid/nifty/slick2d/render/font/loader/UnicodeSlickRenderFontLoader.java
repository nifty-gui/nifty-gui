package de.lessvoid.nifty.slick2d.render.font.loader;

import java.awt.*;

import de.lessvoid.nifty.slick2d.render.font.SlickLoadFontException;
import de.lessvoid.nifty.slick2d.render.font.SlickRenderFont;
import de.lessvoid.nifty.slick2d.render.font.UnicodeSlickRenderFont;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

/**
 * Load the font as Unicode font using Hiero settings to specify how the font is supposed to look like.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public final class UnicodeSlickRenderFontLoader extends AbstractJavaSlickRenderFontLoader {
  /**
   * The default size of a font. This value is needed as TrueType fonts in general do not have a default size,
   * but Nifty
   * expects a font to be rendered at default size.
   */
  private static final float DEFAULT_SIZE = 12.0f;

  /**
   * Load the font. The name of the font will be used as name of the Hiero settings file. The true type font file will
   * be load by trying to add ".ttf" to the filename and by replacing the file ending with "ttf".
   */
  @Override
  @SuppressWarnings("unchecked")
  public SlickRenderFont loadFont(final Graphics g, final String filename) throws SlickLoadFontException {
    Font javaFont = loadJavaFont(filename);

    if (javaFont == null) {
      throw new SlickLoadFontException("Loading TTF Font failed.");
    }

    if (javaFont.getSize() == 1) {
      javaFont = javaFont.deriveFont(DEFAULT_SIZE);
    }

    try {
      final UnicodeFont uniFont = new UnicodeFont(javaFont);
      uniFont.addAsciiGlyphs();
      uniFont.getEffects().add(new ColorEffect());

      return new UnicodeSlickRenderFont(uniFont, javaFont);
    } catch (final SlickLoadFontException e) {
      throw new SlickLoadFontException("Loading the font failed.", e);
    }
  }
}
