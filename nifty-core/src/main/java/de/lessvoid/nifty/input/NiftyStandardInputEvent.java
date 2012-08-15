package de.lessvoid.nifty.input;


/**
 * a nifty input event.
 * @author void
 */
public enum NiftyStandardInputEvent implements NiftyInputEvent {
  /**
   * goto next input element.
   */
  NextInputElement,

  /**
   * goto previous input element.
   */
  PrevInputElement,

  /**
   * submit text (suitable for text edit elements).
   */
  SubmitText,

  /**
   * activate the control.
   */
  Activate,

  /**
   * move cursor left.
   */
  MoveCursorLeft,

  /**
   * move cursor right.
   */
  MoveCursorRight,

  /**
   * move cursor up.
   */
  MoveCursorUp,

  /**
   * move cursor down.
   */
  MoveCursorDown,

  /**
   * move cursor page up.
   */
  MoveCursorPageUp,

  /**
   * move cursor page down.
   */
  MoveCursorPageDown,

  /**
   * delete.
   */
  Delete,

  /**
   * backspace.
   */
  Backspace,

  /**
   * move cursor to first position.
   */
  MoveCursorToFirstPosition,

  /**
   * move cursor to last position.
   */
  MoveCursorToLastPosition,

  /**
   * selection start.
   */
  SelectionStart,

  /**
   * selection end.
   */
  SelectionEnd,

  /**
   * select all.
   */
  SelectAll,

  /**
   * cut.
   */
  Cut,

  /**
   * copy.
   */
  Copy,

  /**
   * paste.
   */
  Paste,

  /**
   * escape/cancel.
   */
  Escape,

  /**
   * character.
   */
  Character,

  /**
   * ConsoleToggle.
   */
  ConsoleToggle;

  /**
   * additional character data.
   */
  private char character;

  /**
   * set additional character data.
   * @param characterParam character param
   */
  public void setCharacter(final char characterParam) {
    this.character = characterParam;
  }

  /**
   * get character.
   * @return character
   */
  public char getCharacter() {
    return character;
  }
}
