package de.lessvoid.nifty.elements.tools;

import java.util.ArrayList;
import java.util.List;

import de.lessvoid.nifty.spi.render.RenderFont;

public class TextBreak {
  private String[] words;
  private int width;
  private RenderFont font;

  public TextBreak(final String line, final int width, final RenderFont font) {
    this.words = line.split(" ");
    this.width = width;
    this.font = font;
  }

  public List < String > split() {
    if (isSingleLine()) {
      return singleResult();
    }

    return processWords();
  }

  private List < String > singleResult() {
    List < String > result = new ArrayList < String > ();
    result.add(words[0]);
    return result;
  }

  private List < String > processWords() {
    List < String > result = new ArrayList < String > ();
    int i = 0;
    while (isValidIndex(i)) {
      StringBuffer currentLine = new StringBuffer();
      int length = 0;
      while (isBelowLimit(length) && isValidIndex(i)) {
        String currentWord = getWord(i);
        length += font.getWidth(currentWord);
        if (isBelowLimit(length)) {
          currentLine.append(currentWord);
          i++;
        }
      }
      result.add(currentLine.toString());
    }
    return result;
  }

  private boolean isValidIndex(final int i) {
    return i < words.length;
  }

  private boolean isBelowLimit(final int currentLineLength) {
    return currentLineLength < width;
  }

  private String getWord(final int i) {
    String currentWord = words[i];
    if (i < words.length-1) {
      currentWord += " ";
    }
    return currentWord;
  }

  private boolean isSingleLine() {
    return words.length == 1;
  }
}
