package de.lessvoid.nifty.controls.textfield.filter;

/**
 * This is the implicit default input filter that simply allows all input operations.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class FilterAcceptAll implements TextFieldInputFilter {
  @Override
  public boolean acceptInput(final CharSequence oldText, final int index, final char newChar) {
    return true;
  }

  @Override
  public boolean acceptInput(final CharSequence oldText, final int index, final CharSequence newChars) {
    return true;
  }

  @Override
  public boolean acceptDelete(final CharSequence oldSequence, final int deleteStart, final int deleteEnd) {
    return true;
  }
}
