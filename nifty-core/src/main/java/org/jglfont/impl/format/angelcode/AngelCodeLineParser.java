package org.jglfont.impl.format.angelcode;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;

/**
 * Parses a line from an Angelcode font file into a Map of Strings.
 * @author void
 */
public class AngelCodeLineParser {
  private StreamTokenizer tokenizer;
  private String currentKey;
  private boolean hasValue;
  private int token;

  public void parse(final String source, final AngelCodeLineData parsed) throws IOException {
    tokenizer = createTokenizer(source);
    currentKey = "";
    hasValue = false;

    parsed.clear();
    while ((token = tokenizer.nextToken()) != StreamTokenizer.TT_EOF) {
      processToken(parsed);
    }
  }

  private void processToken(final AngelCodeLineData parsed) {
    switch (token) {
      case StreamTokenizer.TT_WORD:
        processWord(parsed);
        break;
      case '=':
        hasValue = true;
        break;
      case '"':
        if (currentKey.length() > 0) {
          parsed.put(currentKey, tokenizer.sval);
          currentKey = "";
        }
        break;
    }
  }

  private void processWord(final AngelCodeLineData parsed) {
    if (currentKey.length() == 0) {
      currentKey = tokenizer.sval;
    } else if (hasValue) {
      parsed.put(currentKey, tokenizer.sval);
      currentKey = "";
    } else {
      parsed.put(currentKey, "");
      currentKey = tokenizer.sval;
    }
  }

  private StreamTokenizer createTokenizer(final String source) {
    StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(source));
    initializeTokenizer(tokenizer);
    return tokenizer;
  }

  private void initializeTokenizer(final StreamTokenizer tokenizer) {
    tokenizer.whitespaceChars(' ', ' ');
    tokenizer.quoteChar('"');
    tokenizer.ordinaryChars('0', '9');
    tokenizer.wordChars('0', '9');
    tokenizer.ordinaryChar('.');
    tokenizer.wordChars('.', '.');
    tokenizer.ordinaryChar(',');
    tokenizer.wordChars(',', ',');
    tokenizer.ordinaryChar('-');
    tokenizer.wordChars('-', '-');
  }
}