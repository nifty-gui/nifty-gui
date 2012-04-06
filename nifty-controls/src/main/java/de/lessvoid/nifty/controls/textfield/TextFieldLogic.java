package de.lessvoid.nifty.controls.textfield;

import de.lessvoid.nifty.Clipboard;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.controls.textfield.filter.FilterAcceptAll;
import de.lessvoid.nifty.controls.textfield.filter.TextFieldInputFilter;
import de.lessvoid.nifty.controls.textfield.format.FormatPassword;
import de.lessvoid.nifty.controls.textfield.format.FormatPlain;
import de.lessvoid.nifty.controls.textfield.format.TextFieldDisplayFormat;

/**
 * This class contains the control logic of a text field. Things like selection, the cursor position and operations like
 * copy and paste are handled here.
 *
 * @author void
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class TextFieldLogic {

  /**
   * the text.
   */
  private final StringBuilder text;

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

  /**
   * The character limit that applies to the text field. {@code -1} means no limit applies.
   */
  private int maxLength;

  /**
   * The text field view instance that allows to notify the parent component about a update.
   */
  private TextFieldView view;

  /**
   * The filter that is used on the input text.
   */
  private TextFieldInputFilter filter;

  /**
   * The format that is applied to the text.
   */
  private TextFieldDisplayFormat format;

  /**
   * Create TextField with clipboard support.
   *
   * @param newText init text
   * @param newClipboard clipboard
   */
  public TextFieldLogic(final CharSequence newText, final Clipboard newClipboard, final TextFieldView textFieldView) {
    view = textFieldView;
    clipboard = newClipboard;
    maxLength = TextField.UNLIMITED_LENGTH;

    setFilter(null);
    setFormat(null);

    text = new StringBuilder(100);
    setText(newText);
  }

  public void setText(final CharSequence newText) {
    text.setLength(0);
    if (newText != null) {
      text.append(newText);
    }
    cursorPosition = 0;
    resetSelection();
  }

  /**
   * Set the filter that is meant to be applied to the text that is typed into the text field.
   *
   * @param newFilter the input filter of the text field
   */
  public void setFilter(final TextFieldInputFilter newFilter) {
    if (newFilter == null) {
      filter = new FilterAcceptAll();
    } else {
      filter = newFilter;
    }
  }

  public TextFieldInputFilter getFilter() {
    return filter;
  }

  /**
   * Set the format that is applied to the text field.
   *
   * @param newFormat the new format that is applied to the text field
   */
  public void setFormat(final TextFieldDisplayFormat newFormat) {
    if (newFormat == null) {
      format = new FormatPlain();
    } else {
      format = newFormat;
    }
  }

  public TextFieldDisplayFormat getFormat() {
    return format;
  }

  /**
   * Reset the selection.
   */
  public void resetSelection() {
    selectionStart = -1;
    selectionEnd = -1;
    selecting = false;
  }

  /**
   * Get cursor position.
   *
   * @return current cursor position
   */
  public int getCursorPosition() {
    return cursorPosition;
  }

  /**
   * Get selection end.
   *
   * @return selection end index
   */
  public int getSelectionEnd() {
    return selectionEnd;
  }

  /**
   * Get selection start.
   *
   * @return selection start index
   */
  public int getSelectionStart() {
    return selectionStart;
  }

  /**
   * Backspace.
   */
  public void backspace() {
    if (hasSelection()) {
      delete();
    } else if (cursorPosition > 0) {
      cursorLeft();
      delete();
    }
  }

  /**
   * Move cursor left.
   */
  public void cursorLeft() {
    moveCursor(-1);
  }

  /**
   * Move the cursor.
   *
   * @param direction the amount of characters to move the cursor
   */
  private void moveCursor(final int direction) {
    if ((direction < 0) && (cursorPosition == 0)) {
      return;
    }
    if ((direction > 0) && (cursorPosition == text.length())) {
      return;
    }

    cursorPosition += direction;
    if (cursorPosition < 0) {
      cursorPosition = 0;
    } else if (cursorPosition > text.length()) {
      cursorPosition = text.length();
    }

    selectionFromCursorPosition();
  }

  /**
   * Update the selection from or to the cursor.
   */
  private void selectionFromCursorPosition() {
    if (!selecting || (cursorPosition == selectionStartIndex)) {
      resetSelection();
    } else if (cursorPosition > selectionStartIndex) {
      selectionStart = selectionStartIndex;
      selectionEnd = cursorPosition;
    } else {
      selectionStart = cursorPosition;
      selectionEnd = selectionStartIndex;
    }
  }

  /**
   * Delete the character at the cursor position.
   */
  public void delete() {
    final String old = text.toString();
    if (hasSelection()) {
      deleteSelectedText();
    } else if (filter.acceptDelete(old, cursorPosition, cursorPosition + 1)) {
      text.delete(cursorPosition, cursorPosition + 1);
    }

    notifyTextChange(old);
  }

  /**
   * Checks if we currently have a selection.
   *
   * @return true or false
   */
  public boolean hasSelection() {
    return (selectionStart != -1) && (selectionEnd != -1);
  }

  /**
   * Delete the text that is currently selected.
   *
   * @throws IllegalStateException in case there is no selection
   */
  private void deleteSelectedText() {
    if (!hasSelection()) {
      throw new IllegalStateException("Can't delete selected text without selection");
    }

    if (filter.acceptDelete(text, selectionStart, selectionEnd)) {
      text.delete(selectionStart, selectionEnd);
      cursorPosition = selectionStart;
      resetSelection();
    }
  }

  private void notifyTextChange(final CharSequence old) {
    final String current = text.toString();
    if (old.toString().equals(current)) {
      return;
    }
    view.textChangeEvent(current);
  }

  /**
   * Copy currently selected text to clipboard.
   */
  public void copy() {
    final CharSequence selectedText = getDisplayedSelectedText();
    if (selectedText != null) {
      clipboard.put(selectedText.toString());
    }
  }

  /**
   * Move cursor right.
   */
  public void cursorRight() {
    moveCursor(1);
  }

  /**
   * Cut the selected text into the clipboard.
   */
  public void cut() {
    final CharSequence selectedText = getDisplayedSelectedText();
    if (selectedText == null) {
      return;
    }

    clipboard.put(selectedText.toString());
    delete();
  }

  public CharSequence getDisplayedSelectedText() {
    if (!hasSelection()) {
      return null;
    }

    return format.getDisplaySequence(text, selectionStart, selectionEnd);
  }

  /**
   * end selecting.
   */
  public void endSelecting() {
    selecting = false;
  }

  /**
   * The text that is supposed to be displayed to the user. This text will be masked in case a password character is
   * set.
   *
   * @return the text that is to be displayed to the user
   */
  public CharSequence getDisplayedText() {
    return format.getDisplaySequence(text);
  }

  /**
   * Get the real text that is stored in this text field.
   *
   * @return the real text that was written into this text field
   */
  public CharSequence getRealText() {
    return text;
  }

  /**
   * Get the character that is used to mask the password.
   *
   * @return the character masking the password or {@code null} in case no character is used and the normal text is
   *         displayed
   */
  public Character getPasswordChar() {
    if (format instanceof FormatPassword) {
      return ((FormatPassword) format).getPasswordChar();
    }
    return null;
  }

  public CharSequence getRealSelectedText() {
    if (!hasSelection()) {
      return null;
    }

    return text.subSequence(selectionStart, selectionEnd);
  }

  public int getSelectionLength() {
    if (hasSelection()) {
      return selectionEnd - selectionStart;
    }
    return 0;
  }

  /**
   * init instance wit the given text.
   *
   * @param newText new text
   */
  public void initWithText(final CharSequence newText) {
    changeText(newText);

    if ((newText != null) && (newText.length() > 0)) {
      view.textChangeEvent(newText);
    }
  }

  private void changeText(final CharSequence newText) {
    final int oldCursorPos = cursorPosition;
    setText(newText);

    if (oldCursorPos <= text.length()) {
      cursorPosition = oldCursorPos;
    }
  }

  /**
   * Filter the new character and add it to the text in case its allowed.
   *
   * @param c the character to insert
   * @return {@code true} in case the text was changed
   */
  private boolean filterAndInsert(final char c) {
    if ((maxLength == TextField.UNLIMITED_LENGTH) || (text.length() < maxLength)) {
      if (filter.acceptInput(text, cursorPosition, c)) {
        text.insert(cursorPosition, c);
        cursorPosition++;
        return true;
      }
    }
    return false;
  }

  /**
   * Insert and filter a sequence of characters. In case the sequence is rejected as a whole the function tries to
   * insert the sequence character by character in order to add all allowed characters.
   *
   * @param chars the character sequence to add
   * @return {@code true} in case the text was changed
   */
  private boolean filterAndInsert(final CharSequence chars) {
    if (chars.length() == 0) {
      return false;
    }

    if ((maxLength == TextField.UNLIMITED_LENGTH) || (text.length() < maxLength)) {
      final int insertCnt = Math.min(maxLength - text.length(), chars.length());
      final CharSequence insertSequence = chars.subSequence(0, insertCnt);
      if (filter.acceptInput(text, cursorPosition, insertSequence)) {
        text.insert(cursorPosition, chars);
        cursorPosition += insertCnt;
        return true;
      } else {
        final int length = chars.length();
        boolean result = false;
        for (int i = 0; i < length; i++) {
          if (filterAndInsert(chars.charAt(i))) {
            result = true;
          }
        }
        return result;
      }
    }
    return false;
  }

  /**
   * Insert character at cursor position.
   *
   * @param c the character to insert
   */
  public void insert(final char c) {
    final String old = text.toString();

    if (hasSelection()) {
      deleteSelectedText();
    }

    if (filterAndInsert(c)) {
      notifyTextChange(old);
    }
  }

  /**
   * Put data from clipboard into textfield.
   */
  public void put() {
    final String clipboardText = clipboard.get();
    if (clipboardText != null) {
      insert(clipboardText);
    }
  }

  /**
   * Insert a sequence of characters into the current text.
   *
   * @param chars the character sequence to insert
   */
  public void insert(final CharSequence chars) {
    final String old = text.toString();

    if (hasSelection()) {
      deleteSelectedText();
    }

    if (filterAndInsert(chars)) {
      notifyTextChange(old);
    }
  }

  public void selectAll() {
    selectionStartIndex = 0;
    selectionStart = 0;
    selectionEnd = text.length();
    selecting = true;
    cursorPosition = text.length();
  }

  public void setMaxLength(final int maxLen) {
    maxLength = maxLen;

    if (maxLength != TextField.UNLIMITED_LENGTH) {
      if (text.length() > maxLen) {
        text.setLength(maxLen);
        setCursorPosition(text.length());
      }
    }
  }

  /**
   * Set new cursor position.
   *
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

    selectionFromCursorPosition();
  }

  /**
   * start selecting.
   */
  public void startSelecting() {
    selecting = true;
    selectionStartIndex = cursorPosition;
  }

  /**
   * Position cursor to first character.
   */
  public void toFirstPosition() {
    if (cursorPosition > 0) {
      cursorPosition = 0;

      selectionFromCursorPosition();
    }
  }

  /**
   * Position cursor to last character.
   */
  public void toLastPosition() {
    if (cursorPosition < text.length()) {
      cursorPosition = text.length();

      selectionFromCursorPosition();
    }
  }
}
