package de.lessvoid.nifty.controls.textfield;

import de.lessvoid.nifty.Clipboard;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.controls.textfield.filter.delete.FilterDeleteAll;
import de.lessvoid.nifty.controls.textfield.filter.delete.TextFieldDeleteFilter;
import de.lessvoid.nifty.controls.textfield.filter.input.FilterAcceptAll;
import de.lessvoid.nifty.controls.textfield.filter.input.TextFieldInputCharFilter;
import de.lessvoid.nifty.controls.textfield.filter.input.TextFieldInputCharSequenceFilter;
import de.lessvoid.nifty.controls.textfield.filter.input.TextFieldInputFilter;
import de.lessvoid.nifty.controls.textfield.format.FormatPassword;
import de.lessvoid.nifty.controls.textfield.format.FormatPlain;
import de.lessvoid.nifty.controls.textfield.format.TextFieldDisplayFormat;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * This class contains the control logic of a text field. Things like selection, the cursor position and operations like
 * copy and paste are handled here.
 *
 * @author void
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class TextFieldLogic {
  /**
   * The text that was typed into the input area by the user.
   */
  @Nonnull
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
  @Nonnull
  private final Clipboard clipboard;

  /**
   * The character limit that applies to the text field. {@code -1} means no limit applies.
   */
  private int maxLength;

  /**
   * The text field view instance that allows to notify the parent component about a update.
   */
  @Nonnull
  private final TextFieldView view;

  /**
   * The filter that is used on the input of single characters in the text.
   */
  @Nonnull
  private TextFieldInputCharFilter filterInputSingle;

  /**
   * The filter that is used on the input of a character sequence in the text.
   */
  @Nonnull
  private TextFieldInputCharSequenceFilter filterInputSequence;

  /**
   * The filter that is applied on delete operations.
   */
  @Nonnull
  private TextFieldDeleteFilter filterDelete;

  /**
   * The default input filter.
   */
  @Nonnull
  private static final TextFieldInputFilter DEFAULT_INPUT_FILTER = new FilterAcceptAll();

  /**
   * The default delete filter.
   */
  @Nonnull
  private static final TextFieldDeleteFilter DEFAULT_DELETE_FILTER = new FilterDeleteAll();

  /**
   * The default text filter format.
   */
  @Nonnull
  private static final TextFieldDisplayFormat DEFAULT_FORMAT = new FormatPlain();

  /**
   * The format that is applied to the text.
   */
  @Nonnull
  private TextFieldDisplayFormat format;

  /**
   * Create TextField with clipboard support.
   *
   * @param newClipboard  clipboard
   * @param textFieldView the viewer for the text field
   */
  public TextFieldLogic(@Nonnull final Clipboard newClipboard, @Nonnull final TextFieldView textFieldView) {
    view = textFieldView;
    clipboard = newClipboard;
    maxLength = TextField.UNLIMITED_LENGTH;

    filterInputSingle = DEFAULT_INPUT_FILTER;
    filterInputSequence = DEFAULT_INPUT_FILTER;
    filterDelete = DEFAULT_DELETE_FILTER;

    format = DEFAULT_FORMAT;

    text = new StringBuilder(100);
  }

  /**
   * Create TextField with clipboard support.
   *
   * @param newText       init text
   * @param newClipboard  clipboard
   * @param textFieldView the viewer for the text field
   */
  public TextFieldLogic(
      @Nullable final CharSequence newText,
      @Nonnull final Clipboard newClipboard,
      @Nonnull final TextFieldView textFieldView) {
    this(newClipboard, textFieldView);
    setText(newText);
  }

  public void setText(@Nullable final CharSequence newText) {
    text.setLength(0);
    if (newText != null) {
      text.append(newText);
    }
    cursorPosition = 0;
    resetSelection();
  }

  /**
   * Set the filter that is applied to single character inputs.
   *
   * @param filter the new filter or {@code null} to reset the filter
   */
  public void setInputFilterSingle(@Nullable final TextFieldInputCharFilter filter) {
    filterInputSingle = (filter == null) ? DEFAULT_INPUT_FILTER : filter;
  }

  /**
   * Set the filter that is applied to character sequence inputs.
   *
   * @param filter the new filter or {@code null} to reset the filter
   */
  public void setInputFilterSequence(@Nullable final TextFieldInputCharSequenceFilter filter) {
    filterInputSequence = (filter == null) ? DEFAULT_INPUT_FILTER : filter;
  }

  /**
   * Set the filter that is applied to delete operations.
   *
   * @param filter the new filter or {@code null} to reset the filter
   */
  public void setDeleteFilter(@Nullable final TextFieldDeleteFilter filter) {
    filterDelete = (filter == null) ? DEFAULT_DELETE_FILTER : filter;
  }

  /**
   * Set the format that is applied to the text field.
   *
   * @param newFormat the new format that is applied to the text field
   */
  public void setFormat(@Nullable final TextFieldDisplayFormat newFormat) {
    format = (newFormat == null) ? DEFAULT_FORMAT : newFormat;
  }

  /**
   * Get the format that currently applies to the displayed text of this text field.
   *
   * @return the format that is applied to the text displayed
   */
  @Nonnull
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
    setCursorPosition(cursorPosition + direction);
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
    if (hasSelection()) {
      deleteSelectedText();
    } else if ((cursorPosition < text.length()) && filterDelete.acceptDelete(text, cursorPosition,
        cursorPosition + 1)) {
      text.delete(cursorPosition, cursorPosition + 1);
    } else {
      return;
    }
    view.textChangeEvent(text.toString());
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

    if (filterDelete.acceptDelete(text, selectionStart, selectionEnd)) {
      text.delete(selectionStart, selectionEnd);
      cursorPosition = selectionStart;
      resetSelection();
    }
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

  @Nullable
  public CharSequence getDisplayedSelectedText() {
    if (!hasSelection()) {
      return null;
    }

    return format.getDisplaySequence(text, selectionStart, selectionEnd);
  }

  /**
   * Stop the active selecting operation.
   * <p/>
   * After calling this function moving the cursor will not increase or decrease the size of the selection, it rather
   * will reset the selection.
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
  @Nonnull
  public CharSequence getDisplayedText() {
    return format.getDisplaySequence(text, 0, text.length());
  }

  /**
   * Get the real text that is stored in this text field.
   *
   * @return the real text that was written into this text field
   */
  @Nonnull
  public CharSequence getRealText() {
    return text;
  }

  /**
   * Get the character that is used to mask the password.
   *
   * @return the character masking the password or {@code null} in case no character is used and the normal text is
   * displayed
   */
  @Nullable
  public Character getPasswordChar() {
    if (format instanceof FormatPassword) {
      return ((FormatPassword) format).getPasswordChar();
    }
    return null;
  }

  /**
   * Get the part of the text that is selected. The text returned is the one the user actually typed in. That is not by
   * all means the same text the user sees.
   *
   * @return the selected text as typed in by the user or {@code null} in case nothing is selected
   */
  @Nullable
  public CharSequence getRealSelectedText() {
    if (!hasSelection()) {
      return null;
    }

    return text.subSequence(selectionStart, selectionEnd);
  }

  /**
   * Get the amount of characters that are currently selected.
   *
   * @return the amount of selected characters
   */
  public int getSelectionLength() {
    if (hasSelection()) {
      return selectionEnd - selectionStart;
    }
    return 0;
  }

  /**
   * Overwrite the text that is stored in this text field entirely and notify the listeners about the changed text.
   *
   * @param newText the new text that should be applied
   */
  public void setTextAndNotify(@Nullable final CharSequence newText) {
    setText(newText);

    if ((newText != null) && (newText.length() > 0)) {
      view.textChangeEvent(newText.toString());
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
      if (filterInputSingle.acceptInput(cursorPosition, c)) {
        text.insert(cursorPosition, c);
        cursorPosition++;
        if (selecting) {
          startSelecting();
        }
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
  private boolean filterAndInsert(@Nonnull final CharSequence chars) {
    if (chars.length() == 0) {
      return false;
    }

    if ((maxLength == TextField.UNLIMITED_LENGTH) || (text.length() < maxLength)) {
      final int insertCnt;
      if (maxLength == TextField.UNLIMITED_LENGTH) {
        insertCnt = chars.length();
      } else {
        insertCnt = Math.min(maxLength - text.length(), chars.length());
      }
      final CharSequence insertSequence = chars.subSequence(0, insertCnt);
      if (filterInputSequence.acceptInput(cursorPosition, insertSequence)) {
        text.insert(cursorPosition, insertSequence);
        cursorPosition += insertCnt;
        if (selecting) {
          startSelecting();
        }
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
    if (hasSelection()) {
      deleteSelectedText();
    }

    if (filterAndInsert(c)) {
      view.textChangeEvent(text.toString());
    }
  }

  /**
   * Put data from clipboard into text field.
   */
  public void put() {
    final String clipboardText = clipboard.get();
    if (clipboardText != null) {
      insert(filterNewLines(clipboardText));
    }
  }

  /**
   * Insert a sequence of characters into the current text.
   *
   * @param chars the character sequence to insert
   */
  public void insert(@Nonnull final CharSequence chars) {
    if (hasSelection()) {
      deleteSelectedText();
    }

    if (filterAndInsert(chars)) {
      view.textChangeEvent(text.toString());
    }
  }

  /**
   * Expand the active selection over the entire text field.
   */
  public void selectAll() {
    selectionStartIndex = 0;
    selectionStart = 0;
    selectionEnd = text.length();
    cursorPosition = text.length();
  }

  /**
   * Set the maximal length that is allowed to be used. In case the current text is longer then the applied limit all
   * characters that exceed the limit will be deleted right away.
   *
   * @param maxLen the new maximal length of the text field
   */
  public void setMaxLength(final int maxLen) {
    maxLength = maxLen;

    if (maxLength != TextField.UNLIMITED_LENGTH) {
      if (text.length() > maxLen) {
        text.setLength(maxLen);
        setCursorPosition(Math.min(cursorPosition, text.length()));
        view.textChangeEvent(text.toString());
      }
    }
  }

  /**
   * Set new cursor position.
   *
   * @param newIndex index.
   */
  public void setCursorPosition(final int newIndex) {
    final int clampedIndex = Math.max(0, Math.min(newIndex, text.length()));

    if (cursorPosition != clampedIndex) {
      cursorPosition = clampedIndex;
      selectionFromCursorPosition();
    }
  }

  /**
   * Start a selecting operation at the current cursor position. Be sure to set the location of the cursor to the proper
   * stop before calling this function.
   */
  public void startSelecting() {
    selecting = true;
    selectionStartIndex = cursorPosition;
  }

  /**
   * Move the location of the cursor to the first position in the text.
   */
  public void toFirstPosition() {
    setCursorPosition(0);
  }

  /**
   * Move the cursor to the last position in the text field.
   */
  public void toLastPosition() {
    setCursorPosition(Integer.MAX_VALUE);
  }

  private CharSequence filterNewLines(@Nonnull final String input) {
    return input.replaceAll("\\r\\n|\\r|\\n", "");
  }
}
