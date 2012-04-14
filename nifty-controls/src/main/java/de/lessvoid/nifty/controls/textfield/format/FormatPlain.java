package de.lessvoid.nifty.controls.textfield.format;

/**
 * This is the implicit default format. It will display the text in exactly the way it was typed in.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class FormatPlain implements TextFieldDisplayFormat {
  @Override
  public CharSequence getDisplaySequence(final CharSequence original, final int start, final int end) {
    return original.subSequence(start, end);
  }
}
