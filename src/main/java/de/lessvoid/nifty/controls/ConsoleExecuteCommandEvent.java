package de.lessvoid.nifty.controls;

import static java.io.StreamTokenizer.TT_EOF;
import static java.io.StreamTokenizer.TT_WORD;

import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.logging.Logger;

import de.lessvoid.nifty.NiftyEvent;

/**
 * Executed when a new command is being issued on the console.
 * @author void
 */
@SuppressWarnings("rawtypes")
public class ConsoleExecuteCommandEvent implements NiftyEvent {
  private static Logger log = Logger.getLogger(ConsoleExecuteCommandEvent.class.getName());
  private String commandLine;
  private String command;
  private String[] arguments;

  public ConsoleExecuteCommandEvent(final String commandLine) {
    if (commandLine == null || commandLine.length() == 0) {
      this.commandLine = "";
      this.command = "";
      this.arguments = new String[0];
    } else {
      this.commandLine = commandLine;
      processCommandLine(commandLine);
    }
  }

  private void processCommandLine(final String commandLine) {
    String[] splits = split(commandLine);
    this.command = splits[0];
    this.arguments = new String[splits.length - 1];
    for (int i=1; i<splits.length; i++) {
      arguments[i-1] = splits[i];
    }
  }

  private String[] split(final String commandLine) {
    ArrayList<String> parts = new ArrayList<String>();

    Reader r = new StringReader(commandLine);
    StreamTokenizer st = new StreamTokenizer(r);
    st.resetSyntax();
    st.wordChars(32, 255);
    st.whitespaceChars(0, ' ');
    st.quoteChar('"');
    st.quoteChar('\'');

    int token = TT_EOF;
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
      return (String[]) parts.toArray(new String[parts.size()]);
    } catch (IOException e) {
      log.warning("exception whild parsing '" + commandLine + "'");
      return new String[] { commandLine };
    }
  }

  /**
   * Get the complete command line as send from the console.
   * @return command line
   */
  public String getCommandLine() {
    return commandLine;
  }

  /**
   * Get the number of arguments, that means all arguments (separated by whitespace).
   * @return argument counts
   */
  public int getArgumentCount() {
    return arguments.length;
  }

  /**
   * Get the command (the first string in the whitespace separated commandline).
   * @return
   */
  public String getCommand() {
    return command;
  }

  /**
   * Get all of the command arguments as an array.
   * @return arguments as String array.
   */
  public String[] getArguments() {
    return arguments;
  }
  
}
