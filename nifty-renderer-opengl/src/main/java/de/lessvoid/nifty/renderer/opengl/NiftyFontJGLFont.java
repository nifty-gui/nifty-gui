package de.lessvoid.nifty.renderer.opengl;

import de.lessvoid.nifty.renderer.opengl.font.jglfont.JGLFont;
import de.lessvoid.nifty.spi.NiftyFont;
import de.lessvoid.niftyinternal.render.batch.BatchManager;

/**
 * Created by void on 03.04.16.
 */
public class NiftyFontJGLFont implements NiftyFont {
  private final JGLFont font;
  private final String name;

  NiftyFontJGLFont(final JGLFont font, final String name) {
    this.font = font;
    this.name = name;
  }

  JGLFont getJGLFont() {
    return font;
  }

  /**
   * Return the width of the given text String in px using.
   *
   * @param text the String to get the width for
   * @return the width in px of the String
   */
  public int getWidth(final String text) {
    return font.getStringWidth(text);
  }

  /**
   * Return the height of the font.
   *
   * @return the height in px
   */
  public int getHeight() {
    return font.getHeight();
  }

  @Override
  public void renderText(final BatchManager batchManager, final int x, final int y, final String text, final float textSize, final float textSize1, final float red, final float green, final float blue, final float alpha) {
    font.setCustomRenderState(batchManager);
    font.renderText(x, y, text, textSize, textSize1, red, green, blue, alpha);
  }

  /**
   * Return the width in px of the given character including kerning information taking the next character into account.
   *
   * @param currentCharacter current character
   * @param nextCharacter next character
   * @param size font size
   * @return width of the character or {@code -1} when no information for the character is available
   */
  public int getCharacterWidth(final char currentCharacter, final char nextCharacter, final float size) {
    return font.getCharacterWidth(currentCharacter, nextCharacter, size);
  }

  /**
   * Return the filename this font originates from.
   * @return the filename of the font
   */
  public String getName() {
    return name;
  }

}
