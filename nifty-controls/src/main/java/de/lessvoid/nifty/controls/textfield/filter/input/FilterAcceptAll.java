package de.lessvoid.nifty.controls.textfield.filter.input;

import javax.annotation.Nonnull;

/**
 * This is the implicit default input filter that simply allows all input operations.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class FilterAcceptAll implements TextFieldInputFilter {
  @Override
  public boolean acceptInput(final int index, final char newChar) {
    return true;
  }

  @Override
  public boolean acceptInput(final int index, @Nonnull final CharSequence newChars) {
    return true;
  }
}
