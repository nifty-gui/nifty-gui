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
   * The default size of a font. This value is needed as TrueType fonts in general do not have a default size,
   * but Nifty
   * expects a font to be rendered at default size.
   */
  private static final float DEFAULT_SIZE = 12.0f;

  /**
   * The font size that will be actually used in case the font does not provide a default size.
   */
  private float fontSize;

  /**
   * Constructor that uses the provided default size of 12pt as font size.
   */
  public TrueTypeSlickRenderFontLoader() {
    this(DEFAULT_SIZE);
  }

  /**
   * This constructor allows setting the size all fonts are load with.
   *
   * @param defaultSize the new size of the font
   */
  public TrueTypeSlickRenderFontLoader(final float defaultSize) {
    fontSize = defaultSize;
  }

  /**
   * Load the requested font.
   */
  @Override
  public SlickRenderFont loadFont(final Graphics g, final String filename) throws SlickLoadFontException {
    Font javaFont;
    try {
      javaFont = loadJavaFont(filename);
    } catch (final Exception e) {
      throw new SlickLoadFontException("Can't find font resource", e);
    }
    if (javaFont == null) {
      throw new SlickLoadFontException("Can't load font as true type.");
    }

    javaFont = javaFont.deriveFont(fontSize);

    return new de.lessvoid.nifty.slick2d.render.font.TrueTypeSlickRenderFont(
        new org.newdawn.slick.TrueTypeFont(javaFont, true), javaFont);
  }

}
