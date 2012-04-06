package de.lessvoid.nifty.controls.textfield.filter;

/**
 * This filter only accepts numeric input. So the only content the text input allows is a unsigned integer value.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class FilterAcceptDigits extends AbstractCharacterFilter {
  @Override
  protected boolean isAllowedCharacter(final int index, final char character) {
    return Character.isDigit(character);
  }
}
