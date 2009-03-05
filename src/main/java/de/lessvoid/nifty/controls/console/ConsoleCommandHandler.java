package de.lessvoid.nifty.controls.console;

/**
 * ConsoleCommandHandler.
 * @author void
 */
public interface ConsoleCommandHandler {

  /**
   * a command has been executed in the console, e.g. a line
   * has been completed with the return key.
   * @param line line
   */
  void execute(String line);
}
