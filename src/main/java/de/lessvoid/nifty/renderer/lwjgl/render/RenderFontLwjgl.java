package de.lessvoid.nifty.renderer.lwjgl.render;

import de.lessvoid.nifty.renderer.lwjgl.render.font.CharacterInfo;
import de.lessvoid.nifty.renderer.lwjgl.render.font.Font;
import de.lessvoid.nifty.spi.render.RenderDevice;
import de.lessvoid.nifty.spi.render.RenderFont;

/**
 * RenderFontLwjgl.
 * @author void
 */
public class RenderFontLwjgl implements RenderFont {
  private Font font;

  /**
   * Initialize the font.
   * @param name font filename
   */
  public RenderFontLwjgl(final String name, final RenderDevice device) {
    font = new Font(device);
    font.init(name);
  }

  /**
   * get font height.
   * @return height
   */
  public int getHeight() {
    return font.getHeight();
  }

  /**
   * get font width of the given string.
   * @param text text
   * @return width of the given text for the current font
   */
  public int getWidth(final String text) {
    return font.getStringWidth(text);
  }

  /**
   * @param charInfoC
   * @param charInfoC charInfoC
   * @param nextc character
   * @return kerning
   */
  public static int getKerning(final CharacterInfo charInfoC, final char nextc) {
    Integer kern = charInfoC.getKerning().get(Character.valueOf(nextc));
    if (kern != null) {
      return kern.intValue();
    }
    return 0;
  }

  /**
   * Return the width of the given character including kerning information.
   * @param currentCharacter current character
   * @param nextCharacter next character
   * @param size font size
   * @return width of the character or null when no information for the character is available
   */
  public Integer getCharacterAdvance(final char currentCharacter, final char nextCharacter, final float size) {
    CharacterInfo currentCharacterInfo = font.getChar(currentCharacter);
    if (currentCharacterInfo == null) {
      return null;
    } else {
      return new Integer(
          (int) (currentCharacterInfo.getXadvance() * size + getKerning(currentCharacterInfo, nextCharacter)));
    }
  }

  public Font getFont() {
    return font;
  }
}
