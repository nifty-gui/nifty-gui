package de.lessvoid.nifty.controls.textfield.filter;

/**
 * This filter accepts only lower case letters
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class FilterAcceptLowerCase extends AbstractCharacterFilter {
  @Override
  protected boolean isAllowedCharacter(final int index, final char character) {
    return Character.isLowerCase(character);
  }
}
