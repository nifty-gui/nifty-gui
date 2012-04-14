package de.lessvoid.nifty.controls.textfield;

import java.util.Arrays;

/**
 * This is a support class that is used to create a character sequence that contains a single character at a set
 * length.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class SingleCharSequence implements CharSequence {
  /**
   * The character the sequence is build of.
   */
  private char singleChar;

  /**
   * The length of the sequence.
   */
  private int length;

  /**
   * Create a new password character sequence.
   *
   * @param pwChar the password character
   * @param pwLength the length of the sequence
   */
  public SingleCharSequence(final char pwChar, final int pwLength) {
    setSingleChar(pwChar);
    setLength(pwLength);
  }

  /**
   * Set the new single character.
   *
   * @param newChar the new single character
   */
  public void setSingleChar(final char newChar) {
    singleChar = newChar;
  }

  /**
   * Get the character that is used to hide the password.
   *
   * @return the character that is used to mask the password
   */
  public char getSingleChar() {
    return singleChar;
  }

  /**
   * Set the new length of the single character sequence.
   *
   * @param newLength the new length
   */
  public void setLength(final int newLength) {
    length = newLength;
  }

  @Override
  public String toString() {
    final char[] tempArray = new char[length];
    Arrays.fill(tempArray, singleChar);
    return new String(tempArray);
  }

  /**
   * @return the character at the location, in this implementation it will be always the single character
   */
  @Override
  public char charAt(final int index) {
    if ((index < 0) || (index >= length)) {
      throw new IndexOutOfBoundsException("Index out of range: " + Integer.toString(index));
    }
    return singleChar;
  }

  @Override
  public int length() {
    return length;
  }

  @Override
  public CharSequence subSequence(final int start, final int end) {
    if (start < 0) {
      throw new IndexOutOfBoundsException("Index out of range: " + Integer.toString(start));
    }
    if (end > length) {
      throw new IndexOutOfBoundsException("Index out of range: " + Integer.toString(end));
    }
    if (start > end) {
      throw new IndexOutOfBoundsException("Index out of range: " + Integer.toString(end - start));
    }

    if ((start == 0) && (end == length)) {
      return this;
    }
    return new SingleCharSequence(singleChar, end - start);
  }
}
