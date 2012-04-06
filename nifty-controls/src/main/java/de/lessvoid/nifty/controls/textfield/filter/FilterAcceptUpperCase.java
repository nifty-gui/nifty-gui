package de.lessvoid.nifty.controls.textfield.filter;

/**
 * This filter accepts only upper case letters
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class FilterAcceptUpperCase extends AbstractCharacterFilter {
  @Override
  protected boolean isAllowedCharacter(final int index, final char character) {
    return Character.isUpperCase(character);
  }
}
