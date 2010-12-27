package de.lessvoid.nifty.controls;

public interface TextField extends NiftyControl {

  /**
   * Get the current TextField text.
   * @return text
   */
  String getText();

  /**
   * Set the Text of the TextField.
   * @param text new text
   */
  void setText(String text);

  /**
   * Change the max. input length to a new length.
   * @param maxLength max length
   */
  void setMaxLength(int maxLength);

  /**
   * Set the cursorposition to the given index.
   * @param position new cursor position
   */
  void setCursorPosition(int position);
}
