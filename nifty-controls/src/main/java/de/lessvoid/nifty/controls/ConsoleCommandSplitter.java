package de.lessvoid.nifty.controls;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.logging.Logger;

import static java.io.StreamTokenizer.TT_EOF;
import static java.io.StreamTokenizer.TT_WORD;

public class ConsoleCommandSplitter {
  private static final Logger log = Logger.getLogger(ConsoleCommandSplitter.class.getName());

  @Nonnull
  public String[] split(@Nonnull final String commandLine) {
    ArrayList<String> parts = new ArrayList<String>();

    Reader r = new StringReader(commandLine);
    StreamTokenizer st = new StreamTokenizer(r);
    st.resetSyntax();
    st.wordChars(32, 255);
    st.whitespaceChars(0, ' ');
    st.quoteChar('"');
    st.quoteChar('\'');

    int token;
    try {
      while ((token = st.nextToken()) != TT_EOF) {
        switch (token) {
          case TT_WORD:
            parts.add(st.sval);
            break;
          case '\'':
          case '"':
            parts.add(st.sval);
            break;
          default:
        }
      }
      return parts.toArray(new String[parts.size()]);
    } catch (IOException e) {
      log.warning("exception whild parsing '" + commandLine + "'");
      return new String[] { commandLine };
    }
  }
}
