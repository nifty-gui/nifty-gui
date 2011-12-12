package de.lessvoid.nifty.slick2d.render.font.loader;

import java.awt.Font;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

import de.lessvoid.nifty.slick2d.render.font.SlickLoadFontException;
import de.lessvoid.nifty.slick2d.render.font.SlickRenderFont;
import de.lessvoid.nifty.slick2d.render.font.UnicodeSlickRenderFont;

/**
 * Load the font as Unicode font using Hiero settings to specify how the font is
 * supposed to look like.
 * 
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public final class UnicodeSlickRenderFontLoader extends AbstractJavaSlickRenderFontLoader {
  /**
   * Load the font. The name of the font will be used as name of the Hiero
   * settings file. The true type font file will be load by trying to add ".ttf"
   * to the filename and by replacing the file ending with "ttf".
   */
  @Override
  @SuppressWarnings("unchecked")
  public SlickRenderFont loadFont(final Graphics g, final String filename) throws SlickLoadFontException {
    try {
      Font javaFont = loadJavaFont(filename + ".ttf");
      if (javaFont == null) {
        javaFont = loadJavaFont(filename.substring(0, filename.lastIndexOf(".")) + "ttf");

        if (javaFont == null) {
          throw new SlickLoadFontException("Loading TTF Font failed.");
        }
      }
      
      final UnicodeFont uniFont = new UnicodeFont(javaFont);
      uniFont.addAsciiGlyphs();
      uniFont.getEffects().add(new ColorEffect());

      return new UnicodeSlickRenderFont(uniFont, javaFont);
    } catch (final Exception e) {
      throw new SlickLoadFontException("Loading the font failed.", e);
    }
  }
}
