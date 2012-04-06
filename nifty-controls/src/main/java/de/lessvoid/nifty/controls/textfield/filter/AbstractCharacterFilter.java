package de.lessvoid.nifty.controls.textfield.filter;

/**
 * This is the abstract implementation of a filter that filters a character sequence by character without depending on
 * what ever character was written before.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public abstract class AbstractCharacterFilter implements TextFieldInputFilter {
  @Override
  public boolean acceptInput(final CharSequence oldText, final int index, final char newChar) {
    return isAllowedCharacter(index, newChar);
  }

  @Override
  public boolean acceptInput(final CharSequence oldText, final int index, final CharSequence newChars) {
    final int length = newChars.length();
    for (int i = 0; i < length; i++) {
      if (!isAllowedCharacter(index + i, newChars.charAt(i))) {
        return false;
      }
    }
    return true;
  }

  @Override
  public boolean acceptDelete(final CharSequence oldSequence, final int deleteStart, final int deleteEnd) {
    return true;
  }

  /**
   * Check if this character is allowed as input value.
   *
   * @param index the index where the character is added to
   * @param character the character to test
   * @return {@code true} in case the character is allowed
   */
  protected abstract boolean isAllowedCharacter(final int index, final char character);
}
