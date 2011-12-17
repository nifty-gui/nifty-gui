package de.lessvoid.nifty.slick2d.render.font.loader;

import java.awt.Font;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.HieroSettings;

import de.lessvoid.nifty.slick2d.render.font.SlickLoadFontException;
import de.lessvoid.nifty.slick2d.render.font.SlickRenderFont;
import de.lessvoid.nifty.slick2d.render.font.UnicodeSlickRenderFont;

/**
 * Load the font as Unicode font using Hiero settings to specify how the font is
 * supposed to look like.
 * 
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public final class HieroUnicodeSlickRenderFontLoader extends AbstractJavaSlickRenderFontLoader {
  /**
   * Load the font. The name of the font will be used as name of the Hiero
   * settings file. The true type font file will be load by trying to add ".ttf"
   * to the filename and by replacing the file ending with "ttf".
   */
  @Override
  public SlickRenderFont loadFont(final Graphics g, final String filename) throws SlickLoadFontException {
    try {
      final HieroSettings hieroSettings = new HieroSettings(filename);
      Font javaFont = loadJavaFont(filename + ".ttf");
      if (javaFont == null) {
        javaFont = loadJavaFont(filename.substring(0, filename.lastIndexOf('.')).concat("ttf"));

        if (javaFont == null) {
          throw new SlickLoadFontException("Loading TTF Font failed.");
        }
      }

      return new UnicodeSlickRenderFont(new UnicodeFont(javaFont, hieroSettings), javaFont);
    } catch (final SlickException e) {
      throw new SlickLoadFontException("Loading the font failed.", e);
    } catch (final RuntimeException e) {
      throw new SlickLoadFontException("Loading the font failed.", e);
    }
  }
}
