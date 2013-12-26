package de.lessvoid.nifty.controls;

import de.lessvoid.nifty.tools.Color;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * The Console interface is the Nifty control API view of a Nifty Console control.
 *
 * @author void
 */
public interface Console extends NiftyControl {

  /**
   * output a line of text (or multiple lines separated by \n) to the console.
   *
   * @param line the line of text to output to the console
   */
  void output(@Nonnull String line);

  /**
   * output a line of text (or multiple lines separated by \n) to the console.
   *
   * @param line the line of text to output to the console
   */
  void output(@Nonnull String line, @Nullable Color color);

  /**
   * output a line of text (or multiple lines separated by \n) with the error color
   *
   * @param line the line of text to output to the console
   */
  void outputError(@Nonnull String line);

  /**
   * Get the complete content of the console as an array of Strings.
   *
   * @return console content
   */
  @Nonnull
  String[] getConsoleContent();

  /**
   * Clear the console.
   */
  void clear();

  /**
   * This gives you access to the Textfield that the Console uses for text input.
   *
   * @return the Nifty TextField control
   */
  @Nullable
  TextField getTextField();

  /**
   * Change the output colors. A color can be null in which case the default color of the console is being used.
   *
   * @param standardColor the output color for the normal output
   * @param errorColor    the output color for the error output
   */
  void changeColors(@Nullable final Color standardColor, @Nonnull final Color errorColor);
}