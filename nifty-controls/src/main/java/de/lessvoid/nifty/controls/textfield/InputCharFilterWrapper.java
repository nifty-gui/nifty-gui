package de.lessvoid.nifty.controls.textfield;

import de.lessvoid.nifty.controls.textfield.filter.input.TextFieldInputCharFilter;
import de.lessvoid.nifty.controls.textfield.filter.input.TextFieldInputCharSequenceFilter;

/**
 * This wrapper is used in case a single char input filter is used. This class will implement the char sequence filter
 * and map it to the single char filter.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
class InputCharFilterWrapper implements TextFieldInputCharSequenceFilter {
  /**
   * The single character filter that will be queried on all input.
   */
  private final TextFieldInputCharFilter wrappedFilter;

  /**
   * The default constructor that is used to set the filter that is the target of this wrapper.
   *
   * @param filter the filter that will be asked if all input is accepted
   * @throws NullPointerException in case the {@code filter} parameter is {@code null}
   */
  InputCharFilterWrapper(final TextFieldInputCharFilter filter) {
    if (filter == null) {
      throw new NullPointerException("Filter must not be null");
    }
    wrappedFilter = filter;
  }

  @Override
  public boolean acceptInput(final int index, final CharSequence newChars) {
    final int length = newChars.length();
    for (int i = 0; i < length; i++) {
      if (!wrappedFilter.acceptInput(index + i, newChars.charAt(i))) {
        return false;
      }
    }
    return true;
  }
}
