package de.lessvoid.nifty.controls.textfield;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.*;
import de.lessvoid.nifty.controls.textfield.filter.delete.TextFieldDeleteFilter;
import de.lessvoid.nifty.controls.textfield.filter.input.*;
import de.lessvoid.nifty.controls.textfield.format.FormatPassword;
import de.lessvoid.nifty.controls.textfield.format.TextFieldDisplayFormat;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.elements.tools.FontHelper;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.input.NiftyStandardInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.spi.render.RenderFont;
import de.lessvoid.nifty.tools.SizeValue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.logging.Logger;

/**
 * A TextFieldControl.
 *
 * @author void
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 * @deprecated Please use {@link TextField} when accessing NiftyControls.
 */
@Deprecated
public class TextFieldControl extends AbstractController implements TextField, TextFieldView {
  @Nonnull
  private static final Logger log = Logger.getLogger(TextFieldControl.class.getName());
  @Nullable
  private Nifty nifty;
  @Nullable
  private Screen screen;
  @Nullable
  private Element textElement;
  @Nullable
  private Element fieldElement;
  @Nullable
  private Element cursorElement;
  @Nullable
  private TextFieldLogic textField;
  private int firstVisibleCharacterIndex;
  private int lastVisibleCharacterIndex;
  private int fieldWidth;
  private int fromClickCursorPos;
  private int toClickCursorPos;
  @Nullable
  private FocusHandler focusHandler;

  @Override
  public void bind(
      @Nonnull final Nifty niftyParam,
      @Nonnull final Screen screenParam,
      @Nonnull final Element newElement,
      @Nonnull final Parameters properties) {
    bind(newElement);

    nifty = niftyParam;
    screen = screenParam;
    fromClickCursorPos = -1;
    toClickCursorPos = -1;

    final String initText = properties.get("text"); //NON-NLS
    if ((initText == null) || initText.isEmpty()) {
      textField = new TextFieldLogic(nifty.getClipboard(), this);
      textField.toFirstPosition();
    } else {
      textField = new TextFieldLogic(initText, nifty.getClipboard(), this);
    }

    textElement = newElement.findElementById("#text"); //NON-NLS
    fieldElement = newElement.findElementById("#field"); //NON-NLS
    cursorElement = newElement.findElementById("#cursor"); //NON-NLS

    if (textElement == null) {
      log.warning("Locating the text element of the text field failed. Looked for: #text");
    }
    if (fieldElement == null) {
      log.warning("Locating the field element of the text field failed. Looked for: #field");
    }
    if (cursorElement == null) {
      log.warning("Locating the cursor element of the text field failed. Looked for: #cursor");
    }

    if (properties.isSet("passwordChar")) { //NON-NLS
      //noinspection ConstantConditions
      textField.setFormat(new FormatPassword(properties.get("passwordChar").charAt(0))); //NON-NLS
    }
    setMaxLength(properties.getAsInteger("maxLength", UNLIMITED_LENGTH));
    activateFilter(properties.getWithDefault("filter", "all"));
  }

  /**
   * Apply a filter in regards to the filter property that was set for this text field control.
   *
   * @param filter the value of the filter property
   */
  private void activateFilter(@Nonnull final String filter) {
    if ("all".equals(filter)) { //NON-NLS
      disableInputFilter();
    } else if ("digits".equals(filter)) { //NON-NLS
      enableInputFilter(new FilterAcceptDigits());
    } else if ("negative digits".equals(filter)) { //NON-NLS
      enableInputFilter(new FilterAcceptNegativeDigits());
    } else if ("float".equals(filter)) { //NON-NLS
      enableInputFilter(new FilterAcceptFloat());
    } else if ("letters".equals(filter)) { //NON-NLS
      enableInputFilter(new FilterAcceptLetters());
    } else if ("upper case".equals(filter)) { //NON-NLS
      enableInputFilter(new FilterAcceptUpperCase());
    } else if ("lower case".equals(filter)) { //NON-NLS
      enableInputFilter(new FilterAcceptLowerCase());
    } else {
      enableInputFilter(new FilterAcceptRegex(filter));
    }
  }

  @Override
  public void init(@Nonnull final Parameters parameter) {
    super.init(parameter);
    if (screen == null) {
      log.severe("Screen instance not set. Binding failed or did not run yet.");
      return;
    }
    focusHandler = screen.getFocusHandler();

    if (textField == null) {
      log.severe("Field logic not available. Binding failed or did not run yet.");
      return;
    }

    layoutCallback();

    CharSequence displayedText = textField.getDisplayedText();
    firstVisibleCharacterIndex = 0;
    lastVisibleCharacterIndex = displayedText.length();
    if (textElement != null) {
      final TextRenderer textRenderer = textElement.getRenderer(TextRenderer.class);
      if (textRenderer == null) {
        log.warning("Text element does not contain a text renderer");
      } else {
        RenderFont font = textRenderer.getFont();
        if (font == null) {
          log.warning("No font applied to text element.");
        } else {
          lastVisibleCharacterIndex = FontHelper.getVisibleCharactersFromStart(font, displayedText, fieldWidth, 1.0f);
        }
      }
    }

    updateCursor();
  }

  @Override
  public void onStartScreen() {
  }

  @Override
  public void layoutCallback() {
    if (fieldElement != null && cursorElement != null) {
      fieldWidth = fieldElement.getWidth() - cursorElement.getWidth();
    } else {
      fieldWidth = 0;
    }
  }

  @Nonnull
  private CharSequence getVisibleText() {
    if (textField == null) {
      return "";
    }
    if (lastVisibleCharacterIndex == UNLIMITED_LENGTH) {
      final CharSequence text = textField.getDisplayedText();
      return text.subSequence(firstVisibleCharacterIndex, text.length());
    }
    return textField.getDisplayedText().subSequence(firstVisibleCharacterIndex, lastVisibleCharacterIndex);
  }

  public void onClick(final int mouseX, final int mouseY) {
    final CharSequence visibleString = getVisibleText();
    final int indexFromPixel = getCursorPosFromMouse(mouseX, visibleString);
    if (indexFromPixel != -1) {
      fromClickCursorPos = firstVisibleCharacterIndex + indexFromPixel;
    }
    if (textField != null) {
      textField.resetSelection();
      textField.setCursorPosition(fromClickCursorPos);
    }
    updateCursor();
  }

  public void onClickMouseMove(final int mouseX, final int mouseY) {
    final CharSequence visibleString = getVisibleText();
    final int indexFromPixel = getCursorPosFromMouse(mouseX, visibleString);
    if (indexFromPixel != -1) {
      toClickCursorPos = firstVisibleCharacterIndex + indexFromPixel;
    }

    if (textField != null) {
      textField.setCursorPosition(fromClickCursorPos);
      textField.startSelecting();
      textField.setCursorPosition(toClickCursorPos);
      textField.endSelecting();
    }
    updateCursor();
  }

  private int getCursorPosFromMouse(final int mouseX, @Nonnull final CharSequence visibleString) {
    if (textElement == null || fieldElement == null) {
      return 0;
    }
    final TextRenderer textRenderer = textElement.getRenderer(TextRenderer.class);
    if (textRenderer == null) {
      return 0;
    }
    final RenderFont font = textRenderer.getFont();
    if (font == null) {
      return 0;
    }
    return FontHelper.getCharacterIndexFromPixelPosition(font, visibleString, mouseX - fieldElement.getX(), 1.0f);
  }

  @Override
  public boolean inputEvent(@Nonnull final NiftyInputEvent inputEvent) {
    if (inputEvent instanceof NiftyStandardInputEvent) {
      final NiftyStandardInputEvent standardInputEvent = (NiftyStandardInputEvent) inputEvent;
      if (textField != null) {
        switch (standardInputEvent) {
          case MoveCursorLeft:
            textField.cursorLeft();
            break;
          case MoveCursorRight:
            textField.cursorRight();
            break;
          case Delete:
            textField.delete();
            break;
          case Backspace:
            textField.backspace();
            break;
          case MoveCursorToLastPosition:
            textField.toLastPosition();
            break;
          case MoveCursorToFirstPosition:
            textField.toFirstPosition();
            break;
          case SelectionStart:
            textField.startSelecting();
            break;
          case SelectionEnd:
            textField.endSelecting();
            break;
          case Cut:
            textField.cut();
            break;
          case Copy:
            textField.copy();
            break;
          case Paste:
            textField.put();
            break;
          case SelectAll:
            textField.selectAll();
            break;
          case Character:
            textField.insert(standardInputEvent.getCharacter());
            break;
          case NextInputElement:
            if (focusHandler != null && fieldElement != null) {
              focusHandler.getNext(fieldElement).setFocus();
            }
            break;
          case PrevInputElement:
            if (focusHandler != null && fieldElement != null) {
              focusHandler.getPrev(fieldElement).setFocus();
            }
            break;
          default:
            updateCursor();
            return false;
        }
      }
    }

    updateCursor();
    return true;
  }

  private void updateCursor() {
    if (cursorElement == null || textElement == null || textField == null) {
      return;
    }
    final TextRenderer textRenderer = textElement.getRenderer(TextRenderer.class);

    if (textRenderer == null) {
      return;
    }

    final String text = textField.getDisplayedText().toString();
    checkBounds(text, textRenderer);
    calcLastVisibleIndex(textRenderer);

    textRenderer.setText(text);
    textRenderer.setSelection(textField.getSelectionStart(), textField.getSelectionEnd());

    // calc cursor position
    final int cursorPos = textField.getCursorPosition();

    // outside, move window to fit cursorPos inside [first,last]
    calcFirstVisibleIndex(cursorPos);
    calcLastVisibleIndex(textRenderer);

    RenderFont font = textRenderer.getFont();
    final int d;
    if (font != null) {
      final String substring2 = text.substring(0, firstVisibleCharacterIndex);
      d = font.getWidth(substring2);
    } else {
      d = 0;
    }
    textRenderer.setxOffsetHack(-d);

    final String substring = text.substring(0, cursorPos);
    final int textWidth = textRenderer.getFont().getWidth(substring);
    final int cursorPixelPos = textWidth - d;
    final Element element = getElement();
    if (element == null) {
      return;
    }
    cursorElement.setConstraintX(SizeValue.px(cursorPixelPos));
    cursorElement.setConstraintY(SizeValue.px((getElement().getHeight() - cursorElement.getHeight()) / 2));
    cursorElement.startEffect(EffectEventId.onActive, null);

    element.getParent().layoutElements();
  }

  private void calcFirstVisibleIndex(final int cursorPos) {
    if (cursorPos > lastVisibleCharacterIndex) {
      final int cursorPosDelta = cursorPos - lastVisibleCharacterIndex;
      firstVisibleCharacterIndex += cursorPosDelta;
    } else if (cursorPos < firstVisibleCharacterIndex) {
      final int cursorPosDelta = firstVisibleCharacterIndex - cursorPos;
      firstVisibleCharacterIndex -= cursorPosDelta;
    }
  }

  private void checkBounds(@Nonnull final CharSequence text, @Nonnull final TextRenderer textRenderer) {
    final int textLen = text.length();
    if (firstVisibleCharacterIndex > textLen) {
      // re position so that we show at much possible text
      lastVisibleCharacterIndex = textLen;

      RenderFont font = textRenderer.getFont();
      if (font == null) {
        firstVisibleCharacterIndex = 0;
      } else {
        firstVisibleCharacterIndex = FontHelper.getVisibleCharactersFromEnd(font, text, fieldWidth, 1.0f);
      }
    }
  }

  private void calcLastVisibleIndex(@Nonnull final TextRenderer textRenderer) {
    if (textField == null) {
      return;
    }
    final CharSequence currentText = textField.getDisplayedText();
    final RenderFont font = textRenderer.getFont();
    final int textLength = currentText.length();
    if (font == null) {
      lastVisibleCharacterIndex = textLength;
    } else {
      if (firstVisibleCharacterIndex < textLength) {
        final CharSequence textToCheck = currentText.subSequence(firstVisibleCharacterIndex, textLength);
        final int lengthFitting = FontHelper.getVisibleCharactersFromStart(font, textToCheck, fieldWidth, 1.0f);
        lastVisibleCharacterIndex = lengthFitting + firstVisibleCharacterIndex;
      } else {
        lastVisibleCharacterIndex = firstVisibleCharacterIndex;
      }
    }
  }

  @Override
  public void onFocus(final boolean getFocus) {
    if (cursorElement != null) {
      super.onFocus(getFocus);
      if (getFocus) {
        cursorElement.startEffect(EffectEventId.onCustom);
      } else {
        cursorElement.stopEffect(EffectEventId.onCustom);
      }
      updateCursor();
    }
  }

  @Nonnull
  @Override
  public String getText() {
    return getRealText();
  }

  @Nonnull
  @Override
  public String getRealText() {
    if (textField == null) {
      return "";
    } else {
      return textField.getRealText().toString();
    }
  }

  @Nonnull
  @Override
  public String getDisplayedText() {
    if (textField == null) {
      return "";
    } else {
      return textField.getDisplayedText().toString();
    }
  }

  @Override
  public void setText(@Nonnull final CharSequence text) {
    final CharSequence realText;
    if (nifty == null) {
      log.warning("Nifty instance is not set, binding did not run yet. Special value replacing skipped.");
      realText = text;
    } else {
      realText = nifty.specialValuesReplace(text.toString());
    }
    if (textField != null) {
      textField.setText(realText);
    }
    updateCursor();
  }

  @Override
  public void setMaxLength(final int maxLength) {
    if (textField != null) {
      textField.setMaxLength(maxLength);
    }
    updateCursor();
  }

  @Override
  public void setCursorPosition(final int position) {
    if (textField != null) {
      textField.setCursorPosition(position);
    }
    updateCursor();
  }

  @Override
  public void enableInputFilter(@Nullable final TextFieldInputFilter filter) {
    if (textField != null) {
      textField.setInputFilterSingle(filter);
      textField.setInputFilterSequence(filter);
    }
  }

  @Override
  public void enableInputFilter(@Nullable final TextFieldInputCharFilter filter) {
    if (filter == null) {
      enableInputFilter(null);
    } else {
      if (textField != null) {
        textField.setInputFilterSingle(filter);
        textField.setInputFilterSequence(new InputCharFilterWrapper(filter));
      }
    }
  }

  @Override
  public void enableInputFilter(@Nullable final TextFieldInputCharSequenceFilter filter) {
    if (filter == null) {
      enableInputFilter(null);
    } else {
      if (textField != null) {
        textField.setInputFilterSingle(new InputCharSequenceFilterWrapper(filter));
        textField.setInputFilterSequence(filter);
      }
    }
  }

  @Override
  public void disableInputFilter() {
    enableInputFilter(null);
  }

  @Override
  public void enableDeleteFilter(@Nullable final TextFieldDeleteFilter filter) {
    if (textField != null) {
      textField.setDeleteFilter(filter);
    }
  }

  @Override
  public void disableDeleteFilter() {
    enableDeleteFilter(null);
  }

  @Override
  public void setFormat(@Nullable final TextFieldDisplayFormat format) {
    if (textField != null) {
      textField.setFormat(format);
    }
  }

  @Override
  public void textChangeEvent(@Nonnull final String newText) {
    if (nifty == null) {
      log.warning("Binding not done yet. Can't publish events without reference to Nifty.");
    } else {
      final Element element = getElement();
      if (element != null) {
        String id = getElement().getId();
        if (id != null) {
          nifty.publishEvent(id, new TextFieldChangedEvent(this, newText));
        }
      }
    }
  }

  @Override
  public void enablePasswordChar(final char passwordChar) {
    setFormat(new FormatPassword(passwordChar));
    updateCursor();
  }

  @Override
  public void disablePasswordChar() {
    setFormat(null);
    updateCursor();
  }

  @Override
  public boolean isPasswordCharEnabled() {
    return (textField != null ? textField.getFormat() : null) instanceof FormatPassword;
  }
}
