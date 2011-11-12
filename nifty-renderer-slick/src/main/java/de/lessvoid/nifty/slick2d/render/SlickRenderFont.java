package de.lessvoid.nifty.slick2d.render;

import org.newdawn.slick.Font;

import de.lessvoid.nifty.spi.render.RenderDevice;
import de.lessvoid.nifty.spi.render.RenderFont;

/**
 * RenderFontLwjgl.
 * @author void
 */
public class SlickRenderFont implements RenderFont {
  private Font font;

  /**
   * Initialize the font.
   * @param name font filename
   */
  public SlickRenderFont(final String name, final RenderDevice device) {
//    font = new Font(device);
//    font.init(name);
  }

  /**
   * get font height.
   * @return height
   */
  public int getHeight() {
    return 0;//font.getHeight();
  }

  /**
   * get font width of the given string.
   * @param text text
   * @return width of the given text for the current font
   */
  public int getWidth(final String text, final float size) {
    return 0;//font.getStringWidth(text);
  }

  /**
   * get font width of the given string.
   * @param text text
   * @return width of the given text for the current font
   */
  public int getWidth(final String text) {
    return 0;//font.getStringWidth(text);
  }

  /**
   * Return the width of the given character including kerning information.
   * @param currentCharacter current character
   * @param nextCharacter next character
   * @param size font size
   * @return width of the character or null when no information for the character is available
   */
  public int getCharacterAdvance(final char currentCharacter, final char nextCharacter, final float size) {
//    CharacterInfo currentCharacterInfo = font.getChar(currentCharacter);
//    if (currentCharacterInfo == null) {
//      return null;
//    } else {
//      return new Integer(
//          (int) (currentCharacterInfo.getXadvance() * size + getKerning(currentCharacterInfo, nextCharacter)));
//    }
    return 0;
  }

  public Font getFont() {
    return font;
  }

  public void dispose() {
    
  }
}
