package de.lessvoid.nifty.controls.textfield.filter;

/**
 * This is the general input filter for a text field.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public interface TextFieldInputFilter {
  /**
   * Check if the input of a new character is acceptable.
   *
   * @param oldText the text that was typed in until now
   * @param index the position within the text where the character is added
   * @param newChar the character that is now about to be added to the text
   * @return {@code true} in case adding the character is allowed
   */
  boolean acceptInput(CharSequence oldText, int index, char newChar);

  /**
   * Check if the input of a new set of characters is acceptable.
   *
   * @param oldText the text that was typed in until now
   * @param index the position within the text where the character is added
   * @param newChars the characters that is now about to be added to the text
   * @return {@code true} in case adding the character is allowed
   */
  boolean acceptInput(CharSequence oldText, int index, CharSequence newChars);

  /**
   * Check if the deletion of a part of the text that was put in is acceptable.
   *
   * @param oldSequence the character sequence before the delete is issued
   * @param deleteStart the index of the first character that is deleted
   * @param deleteEnd the index of the first character after the deletion range
   * @return {@code true} in case its okay to delete the character
   */
  boolean acceptDelete(CharSequence oldSequence, int deleteStart, int deleteEnd);
}
