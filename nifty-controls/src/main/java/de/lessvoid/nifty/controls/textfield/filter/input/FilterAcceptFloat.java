package de.lessvoid.nifty.controls.textfield.filter.input;

import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * This filter only accepts a floating point value. So numeric values, the &quot;-&quot; and the locale depended
 * separator between the integer and the fractional part.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class FilterAcceptFloat implements TextFieldInputCharFilter {
  /**
   * The character that represents the minus in a number.
   */
  private final char minusChar;

  /**
   * The character that is used as decimal separator in a number.
   */
  private final char decimalSeparator;

  /**
   * The default constructor needed to fetch the locale dependant values.
   */
  public FilterAcceptFloat() {
    final DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
    minusChar = symbols.getMinusSign();
    decimalSeparator = symbols.getDecimalSeparator();
  }

  @Override
  public boolean acceptInput(final int index, final char newChar) {
    if (Character.isDigit(newChar)) {
      return true;
    }

    if ((newChar == minusChar) && (index == 0)) {
      return true;
    }

    return newChar == decimalSeparator;

  }
}
