package de.lessvoid.nifty.controls;

import de.lessvoid.nifty.controls.textfield.filter.delete.TextFieldDeleteFilter;
import de.lessvoid.nifty.controls.textfield.filter.input.TextFieldInputCharFilter;
import de.lessvoid.nifty.controls.textfield.filter.input.TextFieldInputCharSequenceFilter;
import de.lessvoid.nifty.controls.textfield.filter.input.TextFieldInputFilter;
import de.lessvoid.nifty.controls.textfield.format.FormatPassword;
import de.lessvoid.nifty.controls.textfield.format.TextFieldDisplayFormat;

public interface TextField extends NiftyControl {
  /**
   * This is the constant the maximal length needs to be set to in order to set it to a unlimited.
   */
  int UNLIMITED_LENGTH = -1;

  /**
   * Disable the display of a password character and display the text as it was typed in.
   *
   * @deprecated Use {@link #setFormat(TextFieldDisplayFormat)} with the argument {@code null} to reset the display
   *             format
   */
  @Deprecated
  void disablePasswordChar();

  /**
   * Enable the password overlay for this text field.
   *
   * @param passwordChar the character all characters of the real text are replaced with
   * @deprecated Rather then using this function apply a new text format using {@link #setFormat
   *             (TextFieldDisplayFormat)} with the {@link FormatPassword}.
   */
  @Deprecated
  void enablePasswordChar(char passwordChar);

  /**
   * Get the text that is displayed in the text field. Depending on the format that is applied to this text field its
   * possible that this text is not equal to the text that was actually typed into the text field.
   *
   * @return the text visible to the user
   */
  String getDisplayedText();

  /**
   * Get the text that was typed in by the user into the text field. Depending on the filter applied by {@link
   * #enableInputFilter(TextFieldInputFilter)} its possible that this text does not equal the text that is actually
   * displayed in the text field.
   *
   * @return the text that was typed in by the user into the text field
   */
  String getRealText();

  /**
   * Get the current TextField text. This is the text that was typed into the text field. Its possible that this is not
   * equal to the text that is actually displayed in the text field.
   *
   * @return the text that was typed into the text field
   * @deprecated Better use {@link #getRealText()}
   */
  @Deprecated
  String getText();

  /**
   * Check if the displayed text is currently hidden by the password characters.
   *
   * @return {@code true} in case the actual input is hidden
   * @deprecated There is no replacement. Void did not like it! Complain to him!
   */
  @Deprecated
  boolean isPasswordCharEnabled();

  /**
   * Set the cursor to a new location within the text field. The applied value is automatically capped to valid values.
   *
   * @param position the new location of the cursor
   */
  void setCursorPosition(int position);

  /**
   * Enable a filter to the input to this text field. Once set only characters that are allowed by the filter are
   * usable.
   * <p/>
   * Be aware that calling this function will overwrite <b>all</b> filters applied to this text field before. No matter
   * what function was used to apply the filter.
   *
   * @param filter the new filter or {@code null} to reset to the default filter that allows all input
   */
  void enableInputFilter(TextFieldInputFilter filter);

  /**
   * Apply a filter to the input to this text field. Once set only characters that are allowed by the filter are
   * usable.
   * <p/>
   * Be aware that calling this function will overwrite <b>all</b> filters applied to this text field before. No matter
   * what function was used to apply the filter.
   *
   * @param filter the new filter or {@code null} to reset to the default filter that allows all input
   */
  void enableInputFilter(TextFieldInputCharFilter filter);

  /**
   * Apply a filter to the input to this text field. Once set only characters that are allowed by the filter are
   * usable.
   * <p/>
   * Be aware that calling this function will overwrite <b>all</b> filters applied to this text field before. No matter
   * what function was used to apply the filter.
   *
   * @param filter the new filter or {@code null} to reset to the default filter that allows all input
   */
  void enableInputFilter(TextFieldInputCharSequenceFilter filter);

  /**
   * Disable any active input filter and start using the default input filter again.
   */
  void disableInputFilter();

  /**
   * Apply a filter to the delete operation on this text field. Once set only delete operations permitted by this filter
   * are possible.
   *
   * @param filter the new filter or {@code null} to reset to the default filter that allows all delete operations
   */
  void enableDeleteFilter(TextFieldDeleteFilter filter);

  /**
   * Reset the delete filter to the default one that simply allows all input.
   */
  void disableDeleteFilter();

  /**
   * Apply a display format to this text field. This display formats allow changing the displayed text without effecting
   * the text that was typed in.
   *
   * @param format the new format or {@code null} to reset to the default format that simply displays the text that was
   * typed in
   */
  void setFormat(TextFieldDisplayFormat format);

  /**
   * Set the maximal length in characters that applies for this text field. Once set it will be impossible for the user
   * to type in more characters then set here.
   *
   * @param maxLength the maximal amount of characters allowed in this text field or {@link #UNLIMITED_LENGTH} in case
   * no limit is supposed to be set
   */
  void setMaxLength(int maxLength);

  /**
   * Set the text that is supposed to be displayed in the text field.
   *
   * @param text the text that is supposed to be displayed
   */
  void setText(CharSequence text);
}
