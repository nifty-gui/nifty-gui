package de.lessvoid.nifty.controls.textfield.filter.input;

/**
 * This interface defines a text field input filter that filters the input by longer scale sequences.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public interface TextFieldInputCharSequenceFilter {
  /**
   * Check if the input of a new set of characters is acceptable.
   *
   * @param index the position within the text where the character is added
   * @param newChars the characters that is now about to be added to the text
   * @return {@code true} in case adding the character is allowed
   */
  boolean acceptInput(int index, CharSequence newChars);
}
