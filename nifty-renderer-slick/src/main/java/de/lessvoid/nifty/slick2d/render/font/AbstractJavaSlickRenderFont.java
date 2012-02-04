package de.lessvoid.nifty.slick2d.render.font;

import java.awt.font.GlyphVector;

import org.newdawn.slick.Font;
import org.newdawn.slick.font.GlyphPage;

/**
 * This class extends the usual abstract slick render font to support fonts that base on Java AWT fonts.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public abstract class AbstractJavaSlickRenderFont extends AbstractSlickRenderFont {
  /**
   * The java font that is used to get the font data that is needed to render the font.
   */
  private final java.awt.Font internalFont;

  /**
   * This temporary array is needed to calculate the advancing values in a fast and easy way.
   */
  private final char[] tempCharArray;

  /**
   * The constructor to create this java AWT based render font.
   *
   * @param slickFont the true type font that is used to render
   * @param javaFont the java font that is used to render just the same font
   * @throws SlickLoadFontException in case loading the font fails
   */
  protected AbstractJavaSlickRenderFont(
      final Font slickFont, final java.awt.Font javaFont) throws SlickLoadFontException {
    super(slickFont);
    internalFont = javaFont;
    tempCharArray = new char[2];
  }

  /**
   * This function implements a faster method to calculate the advancing from one character to another compared to the
   * default implementation. Its optimized to use the Java AWT font implementation.
   */
  @SuppressWarnings("RefusedBequest")
  @Override
  public int getCharacterAdvance(final char currentCharacter, final char nextCharacter, final float size) {
    tempCharArray[0] = currentCharacter;
    tempCharArray[1] = nextCharacter;

    final GlyphVector vector = internalFont.createGlyphVector(GlyphPage.renderContext, tempCharArray);
    final double glyphOffset = vector.getGlyphPosition(1).getX() - vector.getGlyphPosition(0).getX();
    final long advanceValue = Math.round(glyphOffset * size);

    //noinspection NumericCastThatLosesPrecision
    return (advanceValue < Integer.MAX_VALUE) ? (int) advanceValue : Integer.MAX_VALUE;
  }
}
