package de.lessvoid.nifty.controls.textfield.format;

import de.lessvoid.nifty.controls.textfield.SingleCharSequence;

/**
 * This format is used to generate a password field. It will overlay every character with a default character.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class FormatPassword implements TextFieldDisplayFormat {
  /**
   * The character that is used to hide the real text.
   */
  private final char pwChar;

  /**
   * Create a new instance with the default character to hide the real text. The character used it: {@code *}.
   */
  public FormatPassword() {
    this('*');
  }

  /**
   * Get the character that is used to replace the real contents of the text.
   *
   * @return the password character
   */
  public char getPasswordChar() {
    return pwChar;
  }

  /**
   * Create a new instance and set the character used to hide the password.
   *
   * @param passwordChar the character that is displayed instead of the real text
   */
  public FormatPassword(final char passwordChar) {
    pwChar = passwordChar;
  }

  @Override
  public CharSequence getDisplaySequence(final CharSequence original, final int start, final int end) {
    return new SingleCharSequence(pwChar, end - start);
  }
}
