package de.lessvoid.nifty.elements.tools;

import de.lessvoid.nifty.spi.render.RenderFont;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class TextBreak {
  @Nonnull
  private final String[] words;
  private final int width;
  private final RenderFont font;

  public TextBreak(@Nonnull final String line, final int width, final RenderFont font) {
    this.words = line.split(" ", -1);
    this.width = width;
    this.font = font;
  }

  @Nonnull
  public List<String> split() {
    if (isSingleLine()) {
      return singleResult();
    }
    return processWords();
  }

  @Nonnull
  private List<String> singleResult() {
    List<String> result = new ArrayList<String>();
    result.add(words[0]);
    return result;
  }

  @Nonnull
  private List<String> processWords() {
    List<String> result = new ArrayList<String>();
    int i = 0, length;
    String currentWord = "";
    String lastColorValue = null;
    StringBuilder currentLine = new StringBuilder();
    while (isValidIndex(i)) {
      //Empty StringBuffer
      currentLine.setLength(0);
      length = 0;
      while (isBelowLimit(length) && isValidIndex(i)) {
        currentWord = getWord(i, length == 0);
        String colorValue = extractColorValue(currentWord);
        if (colorValue != null) {
          lastColorValue = colorValue;
        }
        length += font.getWidth(currentWord);
        if (isBelowLimit(length)) {
          currentLine.append(currentWord);
          i++;
        }
      }
      if (currentLine.length() > 0) {
        addResult(result, lastColorValue, currentLine.toString());
      } else { //If we get here the word itself is longer than the wrapping width
        //We break it up
        String wordPart = currentWord;
        int p;
        do {
          p = 0;
          while (!isBelowLimit(font.getWidth(wordPart)) && wordPart.length() > 0) {
            //Remove one character from the end and see if the new word fits
            wordPart = wordPart.substring(0, wordPart.length() - 1);
            p++;
          }
          addResult(result, lastColorValue, wordPart);
          //Set the new word part to the rest of the word
          wordPart = currentWord.substring(currentWord.length() - p);
          String colorValue = extractColorValue(wordPart);
          if (colorValue != null) {
            lastColorValue = colorValue;
          }
        } while (p > 0);
        i++;
      }
    }
    return result;
  }

  private void addResult(
      @Nonnull final List<String> result,
      @Nullable final String lastColorValue,
      final String currentLine) {
    if (lastColorValue != null) {
      result.add(lastColorValue + currentLine);
    } else {
      result.add(currentLine);
    }
  }

  private boolean isValidIndex(final int i) {
    return i < words.length;
  }

  private boolean isBelowLimit(final int currentLineLength) {
    return currentLineLength < width;
  }

  private String getWord(final int i, final boolean newLine) {
    String currentWord = words[i];
    if (i > 0 && !newLine) {
      currentWord = " " + currentWord;
    }
    return currentWord;
  }

  private boolean isSingleLine() {
    //Check if there is only one word and it fits in one line
    return (words.length == 1 && isBelowLimit(font.getWidth(words[0])));
  }

  @Nullable
  String extractColorValue(@Nullable final String text) {
    if (text == null) {
      return null;
    }
    int start = text.lastIndexOf("\\#");
    if (start != -1) {
      int end = text.indexOf("#", start + 2);
      if (end != -1) {
        return text.substring(start, end + 1);
      }
    }
    return null;
  }
}
