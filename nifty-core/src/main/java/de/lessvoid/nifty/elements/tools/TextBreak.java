package de.lessvoid.nifty.elements.tools;

import java.util.ArrayList;
import java.util.List;

import de.lessvoid.nifty.spi.render.RenderFont;

public class TextBreak {
  private String[] words;
  private int width;
  private RenderFont font;

  public TextBreak(final String line, final int width, final RenderFont font) {
    this.words = line.split(" ", -1);
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
    int i = 0, length;
    String currentWord = "";
    String lastColorValue = null;
    StringBuffer currentLine = new StringBuffer();
    while (isValidIndex(i)) {
    	//Empty StringBuffer
      currentLine.setLength(0);
      length = 0;
      while (isBelowLimit(length) && isValidIndex(i)) {
        currentWord = getWord(i);
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
      if(currentLine.length() > 0) {
        addResult(result, lastColorValue, currentLine.toString());
      } else { //If we get here the word itself is longer than the wrapping width
    	  //We break it up
    	  String wordPart = currentWord;
    	  int p;
    	  do {
    		  p = 0;
	    	  while(!isBelowLimit(font.getWidth(wordPart)) && wordPart.length() > 0) {
	    		  //Remove one character from the end and see if the new word fits
	    		  wordPart = wordPart.substring(0, wordPart.length()-1);
	    		  p++;
	    	  }
	    	  addResult(result, lastColorValue, wordPart);
	    	  //Set the new word part to the rest of the word
	    	  wordPart = currentWord.substring(currentWord.length()-p);
	        String colorValue = extractColorValue(wordPart);
	        if (colorValue != null) {
	          lastColorValue = colorValue;
	        }
    	  } while(p > 0);
    	  i++;
      }
    }
    return result;
  }

  private void addResult(final List<String> result, final String lastColorValue, final String currentLine) {
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

  private String getWord(final int i) {
    String currentWord = words[i];
    if (i < words.length-1) {
      currentWord += " ";
    }
    return currentWord;
  }

  private boolean isSingleLine() {
	//Check if there is only one word and it fits in one line
    return (words.length == 1 && isBelowLimit(font.getWidth(words[0])));
  }

  String extractColorValue(final String text) {
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
