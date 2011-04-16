package de.lessvoid.nifty.controls;




/**
 * The Console interface is the Nifty control API view of a Nifty Console control.
 * @author void
 */
public interface Console extends NiftyControl {

  /**
   * output a line of text (or multiple lines separated by \n) to the console.
   * @param line the line of text to output to the console
   */
  void output(String line);

  /**
   * Get the complete content of the console as an array of Strings.
   * @return console content
   */
  String[] getConsoleContent();

  /**
   * Clear the console.
   */
  void clear();

  /**
   * This gives you access to the Textfield that the Console uses for text input.
   * @return the Nifty TextField control
   */
  TextField getTextField();
}