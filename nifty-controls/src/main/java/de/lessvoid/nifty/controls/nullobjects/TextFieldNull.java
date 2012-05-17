package de.lessvoid.nifty.controls.nullobjects;

import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.controls.textfield.filter.delete.TextFieldDeleteFilter;
import de.lessvoid.nifty.controls.textfield.filter.input.TextFieldInputCharFilter;
import de.lessvoid.nifty.controls.textfield.filter.input.TextFieldInputCharSequenceFilter;
import de.lessvoid.nifty.controls.textfield.filter.input.TextFieldInputFilter;
import de.lessvoid.nifty.controls.textfield.format.TextFieldDisplayFormat;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.tools.SizeValue;

/**
 * The NullObject in case you're requesting this control and it could not be found. You'll get a warning in the log and
 * an instance of this class back. This reduces NPE.
 *
 * @author void
 */
public class TextFieldNull implements TextField {

  @Override
  public Element getElement() {
    return null;
  }

  @Override
  public String getId() {
    return "TextFieldNull"; //NON-NLS
  }

  @Override
  public void setId(final String id) {
  }

  @Override
  public int getWidth() {
    return 0;
  }

  @Override
  public void setWidth(final SizeValue width) {
  }

  @Override
  public int getHeight() {
    return 0;
  }

  @Override
  public void setHeight(final SizeValue height) {
  }

  @Override
  public String getStyle() {
    return null;
  }

  @Override
  public void setStyle(final String style) {
  }

  @Override
  public void enable() {
  }

  @Override
  public void disable() {
  }

  @Override
  public void setEnabled(final boolean enabled) {
  }

  @Override
  public boolean isEnabled() {
    return false;
  }

  @Override
  public String getText() {
    return null;
  }

  @Override
  public String getRealText() {
    return null;
  }

  @Override
  public String getDisplayedText() {
    return null;
  }

  @Override
  public void setText(final CharSequence text) {
  }

  @Override
  public void setMaxLength(final int maxLength) {
  }

  @Override
  public void setCursorPosition(final int position) {
  }

  @Override
  public void enableInputFilter(final TextFieldInputFilter filter) {
  }

  @Override
  public void enableInputFilter(final TextFieldInputCharFilter filter) {
  }

  @Override
  public void enableInputFilter(final TextFieldInputCharSequenceFilter filter) {
  }

  @Override
  public void disableInputFilter() {
  }

  @Override
  public void enableDeleteFilter(final TextFieldDeleteFilter filter) {
  }

  @Override
  public void disableDeleteFilter() {
  }

  @Override
  public void setFormat(final TextFieldDisplayFormat format) {
  }

  @Override
  public void enablePasswordChar(final char passwordChar) {
  }

  @Override
  public void disablePasswordChar() {
  }

  @Override
  public boolean isPasswordCharEnabled() {
    return false;
  }

  @Override
  public void setFocus() {
  }

  @Override
  public void setFocusable(final boolean focusable) {
  }

  @Override
  public boolean hasFocus() {
    return false;
  }

  @Override
  public void layoutCallback() {
  }

  @Override
  public boolean isBound() {
    return false;
  }
}
