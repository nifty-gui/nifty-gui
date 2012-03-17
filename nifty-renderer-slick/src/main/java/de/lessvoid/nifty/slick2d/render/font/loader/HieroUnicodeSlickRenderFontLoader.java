package de.lessvoid.nifty.slick2d.render.font.loader;

import java.awt.*;

import de.lessvoid.nifty.slick2d.render.font.SlickLoadFontException;
import de.lessvoid.nifty.slick2d.render.font.SlickRenderFont;
import de.lessvoid.nifty.slick2d.render.font.UnicodeSlickRenderFont;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.HieroSettings;

/**
 * Load the font as Unicode font using Hiero settings to specify how the font is supposed to look like.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public final class HieroUnicodeSlickRenderFontLoader extends AbstractJavaSlickRenderFontLoader {
  /**
   * Load the font. The name of the font will be used as name of the Hiero settings file. The true type font file will
   * be load by trying to add ".ttf" to the filename and by replacing the file ending with "ttf".
   */
  @Override
  public SlickRenderFont loadFont(final Graphics g, final String filename) throws SlickLoadFontException {
    try {
      final HieroSettings hieroSettings = new HieroSettings(filename);
      final Font javaFont = loadFont(filename);

      if (javaFont == null) {
        throw new SlickLoadFontException("Loading TTF Font failed.");
      }

      final UnicodeFont uniFont = new UnicodeFont(javaFont, hieroSettings);
      uniFont.addAsciiGlyphs();

      return new UnicodeSlickRenderFont(uniFont, javaFont);
    } catch (final SlickException e) {
      throw new SlickLoadFontException("Loading the font failed.", e);
    } catch (final RuntimeException e) {
      throw new SlickLoadFontException("Loading the font failed.", e);
    }
  }

  /**
   * Try to located the font using the filename and common naming conversations.
   *
   * @param filename the filename of the font
   * @return the loaded font or {@code null}
   */
  private static Font loadFont(final String filename) {

    Font javaFont = loadJavaFont(filename + ".ttf");
    if (javaFont == null) {
      javaFont = loadJavaFont(filename.substring(0, filename.lastIndexOf('.')) + "ttf");
    }

    return javaFont;
  }
}
