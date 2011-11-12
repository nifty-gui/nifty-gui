package de.lessvoid.nifty.controls.textfield;

import de.lessvoid.nifty.Clipboard;

/**
 * TextField logic.
 * @author void
 */
public class TextFieldLogic {

  /**
   * the text.
   */
  private StringBuffer text;

  /**
   * the current cursor position in the string.
   */
  private int cursorPosition;

  /**
   * Index of first selected character.
   */
  private int selectionStart = -1;

  /**
   * Index of last selected character.
   */
  private int selectionEnd = -1;

  /**
   * currently selecting stuff.
   */
  private boolean selecting;

  /**
   * cursor position we start the selection mode.
   */
  private int selectionStartIndex;

  /**
   * clipboard.
   */
  private Clipboard clipboard;

  private int maxLength = -1;

  private TextFieldView view;

  /**
   * Create TextField with clipboard support.
   * @param newText init text
   * @param newClipboard clipboard
   */
  public TextFieldLogic(final String newText, final Clipboard newClipboard, final TextFieldView textFieldView) {
    this.view = textFieldView;
    initText(newText);
    clipboard = newClipboard;
    maxLength = -1;
  }

  /**
   * init instance wit the given text.
   * @param newText new text
   */
  public void initWithText(final String newText) {
    changeText(newText);

    if (newText != null && newText.length() > 0) {
      view.textChangeEvent(newText);
    }
  }

  private void initText(final String newText) {
    this.text = new StringBuffer();
    if (newText != null) {
      this.text.append(newText);
    }
    this.cursorPosition = 0;
    this.selectionStart = -1;
    this.selectionEnd = -1;
    this.selecting = false;
    this.selectionStartIndex = -1;
  }

  private void changeText(final String newText) {
    this.text = new StringBuffer();
    if (newText != null) {
      this.text.append(newText);
    }
    // only reset the cursorposition if the old one is not valid anymore
    if (this.cursorPosition > this.text.length()) {
      this.cursorPosition = 0;
    }
    this.selectionStart = -1;
    this.selectionEnd = -1;
    this.selecting = false;
    this.selectionStartIndex = -1;
  }

  /**
   * Return the current text.
   * @return the current text
   */
  public String getText() {
    return text.toString();
  }

  /**
   * Get cursor position.
   * @return current cursor position
   */
  public int getCursorPosition() {
    return cursorPosition;
  }

  /**
   * Move cursor left.
   */
  public void cursorLeft() {
    cursorPosition--;
    if (cursorPosition < 0) {
      cursorPosition = 0;
    }

    if (selecting) {
      selectionFromCursorPosition();
    } else {
      resetSelection();
    }
  }

  /**
   * Move cursor right.
   */
  public void cursorRight() {
    cursorPosition++;
    if (cursorPosition > text.length()) {
      cursorPosition = text.length();
    }

    if (selecting) {
      selectionFromCursorPosition();
    } else {
      resetSelection();
    }
  }

  /**
   *
   */
  private void selectionFromCursorPosition() {
    if (cursorPosition > selectionStartIndex) {
      selectionStart = selectionStartIndex;
      selectionEnd = cursorPosition;
    } else if (cursorPosition == selectionStartIndex) {
      resetSelection();
    } else {
      selectionStart = cursorPosition;
      selectionEnd = selectionStartIndex;
    }
  }

  /**
   * Delete the character at the cursor position.
   */
  public void delete() {
    String old = text.toString();
    if (hasSelection()) {
      text.delete(selectionStart, selectionEnd);
      cursorPosition = selectionStart;
      resetSelection();
    } else {
      text.delete(cursorPosition, cursorPosition + 1);
    }
    notifyTextChange(old);
  }

  /**
   * checks if we currently have a selection.
   * @return true or false
   */
  public boolean hasSelection() {
    return (selectionStart != -1 && selectionEnd != -1);
  }

  /**
   * Position cursor to first character.
   */
  public void toFirstPosition() {
    cursorPosition = 0;
    if (selecting) {
      selectionFromCursorPosition();
    } else if (hasSelection()) {
        resetSelection();
    }
  }

  /**
   * Position cursor to last character.
   */
  public void toLastPosition() {
    cursorPosition = text.length();
    if (selecting) {
      selectionFromCursorPosition();
    } else if (hasSelection()) {
      resetSelection();
    }
  }

  /**
   * Backspace.
   */
  public void backspace() {
    String old = text.toString();
    if (hasSelection()) {
      text.delete(selectionStart, selectionEnd);
      cursorPosition = selectionStart;
      resetSelection();
    } else {
      if (cursorPosition > 0) {
        // delete character left of cursor
        text.delete(cursorPosition - 1, cursorPosition);
        cursorPosition--;
      }
    }
    notifyTextChange(old);
  }

  /**
   * reset the selection.
   */
  public void resetSelection() {
    selectionStart = -1;
    selectionEnd = -1;
    selecting = false;
  }

  /**
   * Insert character at cursor position.
   * @param c
   */
  public void insert(final char c) {
    String old = text.toString();
    if (hasSelection()) {
      text.delete(selectionStart, selectionEnd);
      cursorPosition = selectionStart;
      resetSelection();
    }
    if (maxLength == -1 || text.length() < maxLength) {
      text.insert(cursorPosition, c);
      cursorPosition++;
    }
    notifyTextChange(old);
  }

  /**
   * Set new cursor position.
   * @param newIndex index.
   */
  public void setCursorPosition(final int newIndex) {
    if (newIndex < 0) {
      cursorPosition = 0;
    } else if (newIndex > text.length()) {
      cursorPosition = text.length();
    } else {
      cursorPosition = newIndex;
    }

    if (selecting) {
      selectionFromCursorPosition();
    }
  }

  /**
   * Get selection start.
   * @return selection start index
   */
  public int getSelectionStart() {
    return selectionStart;
  }

  /**
   * Get selection end.
   * @return selection end index
   */
  public int getSelectionEnd() {
    return selectionEnd;
  }

  /**
   * start selecting.
   */
  public void startSelecting() {
    selecting = true;
    selectionStartIndex = cursorPosition;
  }

  /**
   * end selecting.
   */
  public void endSelecting() {
    selecting = false;
  }

  /**
   * Return the selected text or null when there is no selection.
   * @return selected text or null
   */
  public String getSelectedText() {
    if (!hasSelection()) {
      return null;
    }
    return text.substring(selectionStart, selectionEnd);
  }

  /**
   * Cut the selected text into the clipboard.
   * @param passwordChar password character might be null
   */
  public void cut(final Character passwordChar) {
    String selectedText = getSelectedText();
    if (selectedText == null) {
      return;
    }
    clipboard.put(modifyWithPasswordChar(selectedText, passwordChar));
    delete();
  }

  /**
   * Copy currently selected text to clipboard.
   * @param passwordChar password character might be null
   */
  public void copy(final Character passwordChar) {
    String selectedText = modifyWithPasswordChar(getSelectedText(), passwordChar);
    if (selectedText != null) {
      clipboard.put(selectedText);
    }
  }

  String modifyWithPasswordChar(final String selectedText, final Character passwordChar) {
    if (passwordChar == null) {
      return selectedText;
    }
    if (selectedText == null) {
      return null;
    }
    String result = selectedText;
    return result.replaceAll(".", new String(new char[]{passwordChar}));
  }

  /**
   * Put data from clipboard into textfield.
   */
  public void put() {
    String clipboardText = clipboard.get();
    if (clipboardText != null) {
      for (int i = 0; i < clipboardText.length(); i++) {
        insert(clipboardText.charAt(i));
      }
    }
  }

  public void setMaxLength(final int maxLen) {
    maxLength = maxLen;

    if (maxLength != -1) {
      if (text.length() > maxLen) {
        setCursorPosition(maxLen);
        startSelecting();
        setCursorPosition(text.length());
        delete();
      }
    }
  }

  private void notifyTextChange(final String old) {
    String current = text.toString();
    if (old.equals(current)) {
      return;
    }
    view.textChangeEvent(current);
  }
}
