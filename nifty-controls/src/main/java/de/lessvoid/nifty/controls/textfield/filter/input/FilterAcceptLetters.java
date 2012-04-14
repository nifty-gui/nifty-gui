package de.lessvoid.nifty.controls.textfield.filter.input;

/**
 * This filter accepts only letters
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class FilterAcceptLetters implements TextFieldInputCharFilter {
  @Override
  public boolean acceptInput(final int index, final char newChar) {
    return Character.isLetter(newChar);
  }
}
