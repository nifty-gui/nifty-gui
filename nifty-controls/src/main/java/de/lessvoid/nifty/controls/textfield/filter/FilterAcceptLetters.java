package de.lessvoid.nifty.controls.textfield.filter;

/**
 * This filter accepts only letters
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class FilterAcceptLetters extends AbstractCharacterFilter {
  @Override
  protected boolean isAllowedCharacter(final int index, final char character) {
    return Character.isLetter(character);
  }
}
