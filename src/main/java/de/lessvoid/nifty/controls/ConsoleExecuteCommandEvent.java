package de.lessvoid.nifty.controls;

import de.lessvoid.nifty.NiftyEvent;

/**
 * Executed when a new command is being issued on the console.
 * @author void
 */
@SuppressWarnings("rawtypes")
public class ConsoleExecuteCommandEvent implements NiftyEvent {
  private ConsoleCommandSplitter splitter = new ConsoleCommandSplitter();
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
    String[] splits = splitter.split(commandLine);
    this.command = splits[0];
    this.arguments = new String[splits.length - 1];
    for (int i=1; i<splits.length; i++) {
      arguments[i-1] = splits[i];
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
