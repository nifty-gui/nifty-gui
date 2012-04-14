package de.lessvoid.nifty.controls.textfield.filter.input;

/**
 * This filter only accepts numeric input. So the only content the text input allows is a unsigned integer value.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class FilterAcceptDigits implements TextFieldInputCharFilter {
  @Override
  public boolean acceptInput(final int index, final char newChar) {
    return Character.isDigit(newChar);
  }
}
