package de.lessvoid.nifty.controls.textfield.filter.input;

/**
 * This interface defines a text field input filter that filters per character.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public interface TextFieldInputCharFilter {
  /**
   * Check if the input of a new character is acceptable.
   *
   * @param index the position within the text where the character is added
   * @param newChar the character that is now about to be added to the text
   * @return {@code true} in case adding the character is allowed
   */
  boolean acceptInput(int index, char newChar);
}
