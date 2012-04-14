package de.lessvoid.nifty.controls.textfield;

import de.lessvoid.nifty.controls.textfield.filter.input.TextFieldInputCharFilter;
import de.lessvoid.nifty.controls.textfield.filter.input.TextFieldInputCharSequenceFilter;

/**
 * This wrapper is used in case a char sequence input filter is used. This class will implement the single char filter
 * and map it to the char sequence filter.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
class InputCharSequenceFilterWrapper implements TextFieldInputCharFilter {
  /**
   * The filter that is wrapped by the class and that will be queries to find out if the input is acceptable.
   */
  private final TextFieldInputCharSequenceFilter wrappedFilter;

  /**
   * The default constructor that is used to set the filter that is the target of this wrapper.
   *
   * @param filter the filter that will be asked if all input is accepted
   * @throws NullPointerException in case the {@code filter} parameter is {@code null}
   */
  InputCharSequenceFilterWrapper(final TextFieldInputCharSequenceFilter filter) {
    if (filter == null) {
      throw new NullPointerException("Target filter must not be null");
    }
    wrappedFilter = filter;
  }

  @Override
  public boolean acceptInput(final int index, final char newChar) {
    return wrappedFilter.acceptInput(index, new SingleCharSequence(newChar, 1));
  }
}
