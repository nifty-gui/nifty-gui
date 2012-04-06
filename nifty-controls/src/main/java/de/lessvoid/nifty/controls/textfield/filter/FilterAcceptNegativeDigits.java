package de.lessvoid.nifty.controls.textfield.filter;

import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * This filter only accepts numeric input and the &quot;-&quot; character. So the only content the text input allows is
 * a signed integer value.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class FilterAcceptNegativeDigits extends AbstractCharacterFilter {
  /**
   * The character that represents the minus in a number.
   */
  private final char minusChar;

  /**
   * The default constructor needed to fetch the locale dependant values.
   */
  public FilterAcceptNegativeDigits() {
    final DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
    minusChar = symbols.getMinusSign();
  }

  @Override
  protected boolean isAllowedCharacter(final int index, final char character) {
    return Character.isDigit(character) || ((character == minusChar) && (index == 0));
  }
}
