package de.lessvoid.nifty.render;

import de.lessvoid.nifty.tools.Color;

/**
 * RenderFontNull.
 * @author void
 */
public class RenderFontNull implements RenderFont {

  public int getHeight() {
    return 0;
  }

  public int getWidth(String text) {
    return 0;
  }

  public void render(String text, int x, int y, Color color, float size) {
  }

  public Integer getCharacterAdvance(char currentCharacter, char nextCharacter, float size) {
    return null;
  }
}
