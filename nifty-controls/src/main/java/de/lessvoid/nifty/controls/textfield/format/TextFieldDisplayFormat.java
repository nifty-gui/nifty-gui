package de.lessvoid.nifty.controls.textfield.format;

/**
 * This is the interface for the display format that can be applied to a text field. These formats are used to transform
 * the actual text into different visible text.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public interface TextFieldDisplayFormat {
  /**
   * Get a part of the original text converted into the one that is supposed to be displayed.
   *
   * @param original the original text
   * @param start the index of the first character that should be converted
   * @param end the index of the first character after the area that should be converted
   * @return the converted sub-sequence of the original text
   */
  CharSequence getDisplaySequence(CharSequence original, int start, int end);
}
