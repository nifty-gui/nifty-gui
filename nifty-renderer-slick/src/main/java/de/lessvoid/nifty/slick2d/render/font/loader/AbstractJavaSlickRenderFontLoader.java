package de.lessvoid.nifty.slick2d.render.font.loader;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

import org.newdawn.slick.util.ResourceLoader;

/**
 * This abstract font loader is used to support loading fonts that base on Java AWT fonts.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public abstract class AbstractJavaSlickRenderFontLoader implements SlickRenderFontLoader {
  /**
   * Load the file that is named in the String as a Java Font assuming the font is of the defined type.
   *
   * @param font the String that defines the file that should be loaded
   * @param type the font type that is assumed when loading the font
   * @return the loaded font or {@code null} in case loading the font failed
   */
  private static Font loadFont(final String font, final int type) {
    InputStream fontInStream = null;
    try {
      fontInStream = ResourceLoader.getResourceAsStream(font);
      return Font.createFont(type, fontInStream);
    } catch (final IOException ignored) {
      // IO Problem -> Ignore
    } catch (final FontFormatException ignored) {
      // not true type -> does not matter, more methods to try
    } finally {
      if (fontInStream != null) {
        try {
          fontInStream.close();
        } catch (final IOException ignored) {
          // Closing the stream failed -> no one cares
        }
      }
    }

    return null;
  }

  /**
   * Try loading the file defined with the String as a Java font on any way possible.
   *
   * @param font the String that defines the file that is supposed to be load as font
   * @return the loaded font or {@code null} in case loading the font failed
   */
  protected static Font loadJavaFont(final String font) {
    Font javaFont = loadFont(font, Font.TRUETYPE_FONT);
    if (javaFont == null) {
      javaFont = loadFont(font, Font.TYPE1_FONT);
    }
    return javaFont;
  }
}
