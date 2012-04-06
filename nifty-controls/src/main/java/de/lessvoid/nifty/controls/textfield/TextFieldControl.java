package de.lessvoid.nifty.controls.textfield;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.AbstractController;
import de.lessvoid.nifty.controls.FocusHandler;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.controls.TextFieldChangedEvent;
import de.lessvoid.nifty.controls.textfield.filter.*;
import de.lessvoid.nifty.controls.textfield.format.FormatPassword;
import de.lessvoid.nifty.controls.textfield.format.TextFieldDisplayFormat;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.elements.tools.FontHelper;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.xml.xpp3.Attributes;

/**
 * A TextFieldControl.
 *
 * @author void
 * @deprecated Please use {@link de.lessvoid.nifty.controls.TextField} when accessing NiftyControls.
 */
@Deprecated
public class TextFieldControl extends AbstractController implements TextField, TextFieldView {
  private static final int CURSOR_Y = 0;
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

    textField = new TextFieldLogic(properties.getProperty("text", ""), nifty.getClipboard(), this);
    textField.toFirstPosition();

    textElement = getElement().findElementByName("#text");
    fieldElement = getElement().findElementByName("#field");
    cursorElement = getElement().findElementByName("#cursor");

    if (properties.containsKey("passwordChar")) {
      textField.setFormat(new FormatPassword(properties.getProperty("passwordChar").charAt(0)));
    }
    if (properties.containsKey("maxLength")) {
      setMaxLength(Integer.parseInt(properties.getProperty("maxLength")));
    }
    if (properties.containsKey("filter")) {
      activateFilter(properties.getProperty("filter"));
    }
  }

  /**
   * Apply a filter in regards to the filter property that was set for this text field control.
   *
   * @param filter the value of the filter property
   */
  private void activateFilter(final String filter) {
    if ("all".equals(filter)) {
      setFilter(null);
    } else if ("digits".equals(filter)) {
      setFilter(new FilterAcceptDigits());
    } else if ("negative digits".equals(filter)) {
      setFilter(new FilterAcceptNegativeDigits());
    } else if ("float".equals(filter)) {
      setFilter(new FilterAcceptFloat());
    } else if ("letters".equals(filter)) {
      setFilter(new FilterAcceptLetters());
    } else if ("upper case".equals(filter)) {
      setFilter(new FilterAcceptUpperCase());
    } else if ("lower case".equals(filter)) {
      setFilter(new FilterAcceptLowerCase());
    } else {
      setFilter(new FilterAcceptRegex(filter));
    }
  }

  @Override
  public void init(final Properties parameter, final Attributes controlDefinitionAttributes) {
    focusHandler = screen.getFocusHandler();

    textField.initWithText(textElement.getRenderer(TextRenderer.class).getOriginalText());
    fieldWidth = this.fieldElement.getWidth() - this.cursorElement.getWidth();

    TextRenderer textRenderer = textElement.getRenderer(TextRenderer.class);
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
    this.fieldWidth = this.fieldElement.getWidth() - this.cursorElement.getWidth();
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
    TextRenderer textRenderer = textElement.getRenderer(TextRenderer.class);
    return FontHelper.getCharacterIndexFromPixelPosition(textRenderer.getFont(), visibleString,
        (mouseX - fieldElement.getX()), 1.0f);
  }

  @Override
  public boolean inputEvent(final NiftyInputEvent inputEvent) {
    if (inputEvent == null) {
      return false;
    }

    switch (inputEvent) {
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
        textField.insert(inputEvent.getCharacter());
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
    TextRenderer textRenderer = textElement.getRenderer(TextRenderer.class);
    String text = textField.getDisplayedText().toString();
    checkBounds(text, textRenderer);
    calcLastVisibleIndex(textRenderer);

    textRenderer.setText(text);
    textRenderer.setSelection(textField.getSelectionStart(), textField.getSelectionEnd());

    // calc cursor position
    int cursorPos = textField.getCursorPosition();

    // outside, move window to fit cursorPos inside [first,last]
    calcFirstVisibleIndex(cursorPos);
    calcLastVisibleIndex(textRenderer);

    String substring2 = text.substring(0, firstVisibleCharacterIndex);
    int d = textRenderer.getFont().getWidth(substring2);
    textRenderer.setXoffsetHack(-d);

    String substring = text.substring(0, cursorPos);
    int textWidth = textRenderer.getFont().getWidth(substring);
    int cursorPixelPos = textWidth - d;

    final StringBuilder tempBuilder = new StringBuilder(5);
    tempBuilder.append(cursorPixelPos);
    tempBuilder.append("px");
    cursorElement.setConstraintX(new SizeValue(tempBuilder.toString()));

    tempBuilder.setLength(0);
    tempBuilder.append(((getElement().getHeight() - cursorElement.getHeight()) / 2) + CURSOR_Y);
    tempBuilder.append("px");
    cursorElement.setConstraintY(new SizeValue(tempBuilder.toString()));
    cursorElement.startEffect(EffectEventId.onActive, null);
    if (screen != null) {
      screen.layoutLayers();
    }
  }

  private void calcFirstVisibleIndex(final int cursorPos) {
    if (cursorPos > lastVisibleCharacterIndex) {
      int cursorPosDelta = cursorPos - lastVisibleCharacterIndex;
      firstVisibleCharacterIndex += cursorPosDelta;
    } else if (cursorPos < firstVisibleCharacterIndex) {
      int cursorPosDelta = firstVisibleCharacterIndex - cursorPos;
      firstVisibleCharacterIndex -= cursorPosDelta;
    }
  }

  private void checkBounds(final CharSequence text, final TextRenderer textRenderer) {
    int textLen = text.length();
    if (firstVisibleCharacterIndex > textLen) {
      // re position so that we show at much possible text
      lastVisibleCharacterIndex = textLen;
      firstVisibleCharacterIndex = FontHelper.getVisibleCharactersFromEnd(textRenderer.getFont(), text, fieldWidth,
          1.0f);
    }
  }

  private void calcLastVisibleIndex(final TextRenderer textRenderer) {
    final CharSequence currentText = this.textField.getDisplayedText();
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
    return getRealText().toString();
  }

  @Override
  public CharSequence getRealText() {
    return textField.getRealText();
  }

  @Override
  public CharSequence getDisplayedText() {
    return textField.getDisplayedText();
  }

  @Override
  public void setText(CharSequence text) {
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
  public void setFilter(final TextFieldInputFilter filter) {
    textField.setFilter(filter);
  }

  @Override
  public TextFieldInputFilter getFilter() {
    return textField.getFilter();
  }

  @Override
  public void setFormat(final TextFieldDisplayFormat format) {
    textField.setFormat(format);
  }

  @Override
  public TextFieldDisplayFormat getFormat() {
    return textField.getFormat();
  }

  @Override
  public void textChangeEvent(final CharSequence newText) {
    nifty.publishEvent(getElement().getId(), new TextFieldChangedEvent(this, newText.toString()));
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
