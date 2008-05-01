package de.lessvoid.nifty.examples.textfield;


/**
 * TextField logic.
 * @author void
 */
public class TextField {

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
  private int selectionStart;

  /**
   * Index of last selected character.
   */
  private int selectionEnd;

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

  /**
   * Create TextField from the given text string.
   * @param newText new text
   */
  public TextField(final String newText) {
    initializeInstance(newText);

    // init clipboard with null clipboard impl
    clipboard = new Clipboard() {

      public String get() {
        return null;
      }

      public void put(final String data) {
      }
    };
  }

  /**
   * Create TextField with clipboard support.
   * @param newText init text
   * @param newClipboard clipboard
   */
  public TextField(final String newText, final Clipboard newClipboard) {
    initializeInstance(newText);
    clipboard = newClipboard;
  }

  /**
   * @param newText
   */
  private void initializeInstance(final String newText) {
    this.text = new StringBuffer(newText);
    this.cursorPosition = 0;
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
    if (hasSelection()) {
      text.delete(selectionStart, selectionEnd);
      cursorPosition = selectionStart;
      resetSelection();
    } else {
      text.delete(cursorPosition, cursorPosition+1);
    }
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
    if (hasSelection()) {
      text.delete(selectionStart, selectionEnd);
      cursorPosition = selectionStart;
      resetSelection();
    } else {
      if (cursorPosition > 0) {
        // delete character left of cursor
        text.delete(cursorPosition-1, cursorPosition);
        cursorPosition--;
      }
    }
  }

  /**
   * reset the selection.
   */
  public void resetSelection() {
    selectionStart = -1;
    selectionEnd = -1;
  }

  /**
   * Insert character at cursor position.
   * @param c
   */
  public void insert(final char c) {
    if (hasSelection()) {
      text.delete(selectionStart, selectionEnd);
      cursorPosition = selectionStart;
      resetSelection();
    }
    text.insert(cursorPosition, c);
    cursorPosition++;
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
   * Cut the selected text into the clipboard.
   */
  public void cut() {
    clipboard.put(getSelectedText());
    delete();
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
   * Copy currently selected text to clipboard.
   */
  public void copy() {
    String selectedText = getSelectedText();
    if (selectedText != null) {
      clipboard.put(selectedText);
    }
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
}
