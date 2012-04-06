package de.lessvoid.nifty.elements.tools;

import de.lessvoid.nifty.spi.render.RenderFont;

public class FontHelper {

  /**
   * Get character index into the given text that is no more pixel as the given width.
   *
   * @param font font
   * @param text the string to check
   * @param width the minimum width
   * @return the character index into the string.
   */
  public static int getVisibleCharactersFromStart(
      final RenderFont font, final CharSequence text, final int width, final float size) {
    int widthRemaining = width;

    for (int i = 0; i < text.length(); i++) {
      char currentCharacter = text.charAt(i);
      char nextCharacter = getNextCharacter(text, i);

      int w = font.getCharacterAdvance(currentCharacter, nextCharacter, size);
      if (w != -1) {
        widthRemaining -= w;
        if (widthRemaining < 0) {
          // this character will underflow the width. we return the last save index.
          return i;
        }
      }
    }
    return text.length();
  }

  /**
   * Get character index into the given text that is no more pixel as the given width.
   *
   * @param font font
   * @param text the string to check
   * @param width the minimum width
   * @return the character index into the string.
   */
  public static int getVisibleCharactersFromEnd(
      final RenderFont font, final CharSequence text, final int width, final float size) {
    int widthRemaining = width;

    for (int i = text.length() - 1; i >= 0; i--) {
      char currentCharacter = text.charAt(i);
      char prevCharacter = getPrevCharacter(text, i);

      int w = font.getCharacterAdvance(prevCharacter, currentCharacter, size);
      if (w != -1) {
        widthRemaining -= w;
        if (widthRemaining < 0) {
          // this character will underflow the width. we return the last save index.
          return i;
        }
      }
    }
    return 0;
  }

  /**
   * get index into text from a given pixel position.
   *
   * @param font font
   * @param text text string
   * @param pixel pixel index
   * @param size font size
   * @return index into text string
   */
  public static int getCharacterIndexFromPixelPosition(
      final RenderFont font, final CharSequence text, final int pixel, final float size) {
    if (pixel < 0) {
      return -1;
    }

    int current = 0;
    for (int i = 0; i < text.length(); i++) {
      char currentCharacter = text.charAt(i);
      char nextCharacter = getNextCharacter(text, i);

      int w = font.getCharacterAdvance(currentCharacter, nextCharacter, size);
      if (w != -1) {
        if ((pixel >= current) && (pixel <= current + w)) {
          return i;
        }
        current += w;
      }
    }

    return text.length();
  }

  /**
   * @param text
   * @param i
   * @return
   */
  public static char getNextCharacter(final CharSequence text, int i) {
    char nextc = 0;
    if (i < text.length() - 1) {
      nextc = text.charAt(i + 1);
    }
    return nextc;
  }

  /**
   * @param text
   * @param i
   * @return
   */
  public static char getPrevCharacter(final CharSequence text, int i) {
    char prevc = 0;
    if (i > 0) {
      prevc = text.charAt(i - 1);
    }
    return prevc;
  }
}
