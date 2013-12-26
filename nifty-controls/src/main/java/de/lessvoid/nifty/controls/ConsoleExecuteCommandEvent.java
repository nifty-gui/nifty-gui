package de.lessvoid.nifty.controls;

import de.lessvoid.nifty.NiftyEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Executed when a new command is being issued on the console.
 *
 * @author void
 */
public class ConsoleExecuteCommandEvent implements NiftyEvent {
  @Nonnull
  private final Console console;
  @Nonnull
  private final String commandLine;
  @Nonnull
  private final String command;
  @Nonnull
  private final String[] arguments;

  public ConsoleExecuteCommandEvent(@Nonnull final Console console, @Nullable final String commandLine) {
    this.console = console;

    if (commandLine == null || commandLine.length() == 0) {
      this.commandLine = "";
      this.command = "";
      this.arguments = new String[0];
    } else {
      this.commandLine = commandLine;

      ConsoleCommandSplitter splitter = new ConsoleCommandSplitter();
      String[] splits = splitter.split(commandLine);
      this.command = splits[0];
      this.arguments = new String[splits.length - 1];
      System.arraycopy(splits, 1, arguments, 0, splits.length - 1);
    }
  }

  @Nonnull
  public Console getConsole() {
    return console;
  }

  /**
   * Get the complete command line as send from the console.
   *
   * @return command line
   */
  @Nonnull
  public String getCommandLine() {
    return commandLine;
  }

  /**
   * Get the number of arguments, that means all arguments (separated by whitespace).
   *
   * @return argument counts
   */
  public int getArgumentCount() {
    return arguments.length;
  }

  /**
   * Get the command (the first string in the whitespace separated commandline).
   */
  @Nonnull
  public String getCommand() {
    return command;
  }

  /**
   * Get all of the command arguments as an array.
   *
   * @return arguments as String array.
   */
  @Nonnull
  public String[] getArguments() {
    return arguments;
  }

}
