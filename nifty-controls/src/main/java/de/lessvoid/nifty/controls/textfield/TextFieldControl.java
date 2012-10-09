package de.lessvoid.nifty.controls.textfield;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.AbstractController;
import de.lessvoid.nifty.controls.FocusHandler;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.controls.TextFieldChangedEvent;
import de.lessvoid.nifty.controls.textfield.filter.delete.TextFieldDeleteFilter;
import de.lessvoid.nifty.controls.textfield.filter.input.FilterAcceptDigits;
import de.lessvoid.nifty.controls.textfield.filter.input.FilterAcceptFloat;
import de.lessvoid.nifty.controls.textfield.filter.input.FilterAcceptLetters;
import de.lessvoid.nifty.controls.textfield.filter.input.FilterAcceptLowerCase;
import de.lessvoid.nifty.controls.textfield.filter.input.FilterAcceptNegativeDigits;
import de.lessvoid.nifty.controls.textfield.filter.input.FilterAcceptRegex;
import de.lessvoid.nifty.controls.textfield.filter.input.FilterAcceptUpperCase;
import de.lessvoid.nifty.controls.textfield.filter.input.TextFieldInputCharFilter;
import de.lessvoid.nifty.controls.textfield.filter.input.TextFieldInputCharSequenceFilter;
import de.lessvoid.nifty.controls.textfield.filter.input.TextFieldInputFilter;
import de.lessvoid.nifty.controls.textfield.format.FormatPassword;
import de.lessvoid.nifty.controls.textfield.format.TextFieldDisplayFormat;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.elements.tools.FontHelper;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.input.NiftyStandardInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.xml.xpp3.Attributes;

/**
 * A TextFieldControl.
 *
 * @author void
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 * @deprecated Please use {@link TextField} when accessing NiftyControls.
 */
@Deprecated
public class TextFieldControl extends AbstractController implements TextField, TextFieldView {
  private Nifty nifty;
  private Screen screen;
  private Element textElement;
  private Element fieldElement;
  private Element cursorElement;
  private TextFieldLogic textField;
  private int firstVisibleCharacterIndex;
  private int lastVisibleCharacterIndex;
  private int fieldWidth;
  private int fromClickCursorPos;
  private int toClickCursorPos;
  private FocusHandler focusHandler;

  @Override
  public void bind(
      final Nifty niftyParam,
      final Screen screenParam,
      final Element newElement,
      final Properties properties,
      final Attributes controlDefinitionAttributes) {
    bind(newElement);

    nifty = niftyParam;
    screen = screenParam;
    fromClickCursorPos = -1;
    toClickCursorPos = -1;

    final String initText = properties.getProperty("text"); //NON-NLS
    if ((initText == null) || initText.isEmpty()) {
      textField = new TextFieldLogic(nifty.getClipboard(), this);
      textField.toFirstPosition();
    } else {
      textField = new TextFieldLogic(initText, nifty.getClipboard(), this);
    }

    textElement = getElement().findElementByName("#text"); //NON-NLS
    fieldElement = getElement().findElementByName("#field"); //NON-NLS
    cursorElement = getElement().findElementByName("#cursor"); //NON-NLS

    if (properties.containsKey("passwordChar")) { //NON-NLS
      textField.setFormat(new FormatPassword(properties.getProperty("passwordChar").charAt(0))); //NON-NLS
    }
    if (properties.containsKey("maxLength")) { //NON-NLS
      setMaxLength(Integer.parseInt(properties.getProperty("maxLength"))); //NON-NLS
    }
    if (properties.containsKey("filter")) { //NON-NLS
      activateFilter(properties.getProperty("filter")); //NON-NLS
    }
  }

  /**
   * Apply a filter in regards to the filter property that was set for this text field control.
   *
   * @param filter the value of the filter property
   */
  private void activateFilter(final String filter) {
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
  public void init(final Properties parameter, final Attributes controlDefinitionAttributes) {
    focusHandler = screen.getFocusHandler();

    fieldWidth = fieldElement.getWidth() - cursorElement.getWidth();

    final TextRenderer textRenderer = textElement.getRenderer(TextRenderer.class);
    firstVisibleCharacterIndex = 0;
    lastVisibleCharacterIndex = FontHelper.getVisibleCharactersFromStart(textRenderer.getFont(),
        textField.getDisplayedText(), fieldWidth, 1.0f);

    updateCursor();
    super.init(parameter, controlDefinitionAttributes);
  }

  @Override
  public void onStartScreen() {
  }

  @Override
  public void layoutCallback() {
    fieldWidth = fieldElement.getWidth() - cursorElement.getWidth();
  }

  private CharSequence getVisibleText() {
    return textField.getDisplayedText().subSequence(firstVisibleCharacterIndex, lastVisibleCharacterIndex);
  }

  public void onClick(final int mouseX, final int mouseY) {
    final CharSequence visibleString = getVisibleText();
    final int indexFromPixel = getCursorPosFromMouse(mouseX, visibleString);
    if (indexFromPixel != -1) {
      fromClickCursorPos = firstVisibleCharacterIndex + indexFromPixel;
    }
    textField.resetSelection();
    textField.setCursorPosition(fromClickCursorPos);
    updateCursor();
  }

  public void onClickMouseMove(final int mouseX, final int mouseY) {
    final CharSequence visibleString = getVisibleText();
    final int indexFromPixel = getCursorPosFromMouse(mouseX, visibleString);
    if (indexFromPixel != -1) {
      toClickCursorPos = firstVisibleCharacterIndex + indexFromPixel;
    }

    textField.setCursorPosition(fromClickCursorPos);
    textField.startSelecting();
    textField.setCursorPosition(toClickCursorPos);
    textField.endSelecting();
    updateCursor();
  }

  private int getCursorPosFromMouse(final int mouseX, final CharSequence visibleString) {
    final TextRenderer textRenderer = textElement.getRenderer(TextRenderer.class);
    return FontHelper.getCharacterIndexFromPixelPosition(textRenderer.getFont(), visibleString,
        mouseX - fieldElement.getX(), 1.0f);
  }

  @Override
  public boolean inputEvent(final NiftyInputEvent inputEvent) {
    if (inputEvent == null) {
      return false;
    }

    final NiftyStandardInputEvent standardInputEvent = (NiftyStandardInputEvent) inputEvent;
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
        focusHandler.getNext(fieldElement).setFocus();
        break;
      case PrevInputElement:
        focusHandler.getPrev(fieldElement).setFocus();
        break;
      default:
        updateCursor();
        return false;
    }

    updateCursor();
    return true;
  }

  private void updateCursor() {
    final TextRenderer textRenderer = textElement.getRenderer(TextRenderer.class);
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

    final String substring2 = text.substring(0, firstVisibleCharacterIndex);
    final int d = textRenderer.getFont().getWidth(substring2);
    textRenderer.setXoffsetHack(-d);

    final String substring = text.substring(0, cursorPos);
    final int textWidth = textRenderer.getFont().getWidth(substring);
    final int cursorPixelPos = textWidth - d;

    final StringBuilder tempBuilder = new StringBuilder(5);
    tempBuilder.append(cursorPixelPos);
    tempBuilder.append("px"); //NON-NLS
    cursorElement.setConstraintX(new SizeValue(tempBuilder.toString()));

    tempBuilder.setLength(0);
    tempBuilder.append((getElement().getHeight() - cursorElement.getHeight()) / 2);
    tempBuilder.append("px"); //NON-NLS
    cursorElement.setConstraintY(new SizeValue(tempBuilder.toString()));
    cursorElement.startEffect(EffectEventId.onActive, null);
    cursorElement.getParent().layoutElements();
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

  private void checkBounds(final CharSequence text, final TextRenderer textRenderer) {
    final int textLen = text.length();
    if (firstVisibleCharacterIndex > textLen) {
      // re position so that we show at much possible text
      lastVisibleCharacterIndex = textLen;
      firstVisibleCharacterIndex = FontHelper.getVisibleCharactersFromEnd(textRenderer.getFont(), text, fieldWidth,
          1.0f);
    }
  }

  private void calcLastVisibleIndex(final TextRenderer textRenderer) {
    final CharSequence currentText = textField.getDisplayedText();
    if (firstVisibleCharacterIndex < currentText.length()) {
      final CharSequence textToCheck = currentText.subSequence(firstVisibleCharacterIndex, currentText.length());
      final int lengthFitting = FontHelper.getVisibleCharactersFromStart(textRenderer.getFont(), textToCheck,
          fieldWidth, 1.0f);
      lastVisibleCharacterIndex = lengthFitting + firstVisibleCharacterIndex;
    } else {
      lastVisibleCharacterIndex = firstVisibleCharacterIndex;
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

  @Override
  public String getText() {
    return getRealText();
  }

  @Override
  public String getRealText() {
    return textField.getRealText().toString();
  }

  @Override
  public String getDisplayedText() {
    return textField.getDisplayedText().toString();
  }

  @Override
  public void setText(final CharSequence text) {
    textField.setText(nifty.specialValuesReplace(text.toString()));
    updateCursor();
  }

  @Override
  public void setMaxLength(final int maxLength) {
    textField.setMaxLength(maxLength);
    updateCursor();
  }

  @Override
  public void setCursorPosition(final int position) {
    textField.setCursorPosition(position);
    updateCursor();
  }

  @Override
  public void enableInputFilter(final TextFieldInputFilter filter) {
    textField.setInputFilterSingle(filter);
    textField.setInputFilterSequence(filter);
  }

  @Override
  public void enableInputFilter(final TextFieldInputCharFilter filter) {
    textField.setInputFilterSingle(filter);
    textField.setInputFilterSequence(new InputCharFilterWrapper(filter));
  }

  @Override
  public void enableInputFilter(final TextFieldInputCharSequenceFilter filter) {
    textField.setInputFilterSingle(new InputCharSequenceFilterWrapper(filter));
    textField.setInputFilterSequence(filter);
  }

  @Override
  public void disableInputFilter() {
    textField.setInputFilterSingle(null);
    textField.setInputFilterSequence(null);
  }

  @Override
  public void enableDeleteFilter(final TextFieldDeleteFilter filter) {
    textField.setDeleteFilter(filter);
  }

  @Override
  public void disableDeleteFilter() {
    textField.setDeleteFilter(null);
  }

  @Override
  public void setFormat(final TextFieldDisplayFormat format) {
    textField.setFormat(format);
  }

  @Override
  public void textChangeEvent(final String newText) {
    nifty.publishEvent(getElement().getId(), new TextFieldChangedEvent(this, newText));
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
    return textField.getFormat() instanceof FormatPassword;
  }
}
