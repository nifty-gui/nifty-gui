package de.lessvoid.nifty.controls.textfield.controller;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.AbstractController;
import de.lessvoid.nifty.controls.FocusHandler;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.ControllerEventListener;
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
 */
public class TextFieldControl extends AbstractController {
  private static final int CURSOR_Y = 0;
  private Screen screen;
  private Element element;
  private Element textElement;
  private Element fieldElement;
  private Element cursorElement;
  private TextField textField;
  private int firstVisibleCharacterIndex;
  private int lastVisibleCharacterIndex;
  private int fieldWidth;
  private int fromClickCursorPos;
  private int toClickCursorPos;
  private FocusHandler focusHandler;
  private Character passwordChar;

  public TextFieldControl() {
  }

  public void bind(
      final Nifty niftyParam,
      final Screen screenParam,
      final Element newElement,
      final Properties properties,
      final ControllerEventListener newListener,
      final Attributes controlDefinitionAttributes) {
    this.element = newElement;
    this.screen = screenParam;
    this.fromClickCursorPos = -1;
    this.toClickCursorPos = -1;

    this.textField = new TextField("", new ClipboardAWT());
    this.textField.toFirstPosition();

    this.textElement = element.findElementByName("textfield-text");
    this.fieldElement = element.findElementByName("textfield-field");
    this.cursorElement = element.findElementByName("textfield-cursor");

    passwordChar = null;
    if (properties.containsKey("passwordChar")) {
      passwordChar = properties.get("passwordChar").toString().charAt(0);
    }
    if (properties.containsKey("maxLength")) {
      setMaxLength(new Integer(properties.getProperty("maxLength")));
    }
  }

  public void onStartScreen() {
    this.focusHandler = screen.getFocusHandler();

    this.textField.initWithText(textElement.getRenderer(TextRenderer.class).getOriginalText());
    this.fieldWidth = this.fieldElement.getWidth() - this.cursorElement.getWidth();

    TextRenderer textRenderer = textElement.getRenderer(TextRenderer.class);
    this.firstVisibleCharacterIndex = 0;
    this.lastVisibleCharacterIndex = FontHelper.getVisibleCharactersFromStart(textRenderer.getFont(),
        this.textField.getText(), fieldWidth, 1.0f);

    updateCursor();
  }

  public void onClick(final int mouseX, final int mouseY) {
    String visibleString = textField.getText().substring(firstVisibleCharacterIndex, lastVisibleCharacterIndex);
    int indexFromPixel = getCursorPosFromMouse(mouseX, visibleString);
    if (indexFromPixel != -1) {
      fromClickCursorPos = firstVisibleCharacterIndex + indexFromPixel;
    }
    textField.resetSelection();
    textField.setCursorPosition(fromClickCursorPos);
    updateCursor();
  }

  public void onClickMouseMove(final int mouseX, final int mouseY) {
    String visibleString = textField.getText().substring(firstVisibleCharacterIndex, lastVisibleCharacterIndex);
    int indexFromPixel = getCursorPosFromMouse(mouseX, visibleString);
    if (indexFromPixel != -1) {
      toClickCursorPos = firstVisibleCharacterIndex + indexFromPixel;
    }

    textField.setCursorPosition(fromClickCursorPos);
    textField.startSelecting();
    textField.setCursorPosition(toClickCursorPos);
    textField.endSelecting();
    updateCursor();
  }

  private int getCursorPosFromMouse(final int mouseX, final String visibleString) {
    TextRenderer textRenderer = textElement.getRenderer(TextRenderer.class);
    return FontHelper.getCharacterIndexFromPixelPosition(textRenderer.getFont(), visibleString,
        (mouseX - fieldElement.getX()), 1.0f);
  }

  public boolean inputEvent(final NiftyInputEvent inputEvent) {
    if (inputEvent == NiftyInputEvent.MoveCursorLeft) {
      textField.cursorLeft();
      updateCursor();
      return true;
    } else if (inputEvent == NiftyInputEvent.MoveCursorRight) {
      textField.cursorRight();
      updateCursor();
      return true;
    } else if (inputEvent == NiftyInputEvent.Delete) {
      textField.delete();
      updateCursor();
      return true;
    } else if (inputEvent == NiftyInputEvent.Backspace) {
      textField.backspace();
      updateCursor();
      return true;
    } else if (inputEvent == NiftyInputEvent.MoveCursorToLastPosition) {
      textField.toLastPosition();
      updateCursor();
      return true;
    } else if (inputEvent == NiftyInputEvent.MoveCursorToFirstPosition) {
      textField.toFirstPosition();
      updateCursor();
      return true;
    } else if (inputEvent == NiftyInputEvent.SelectionStart) {
      textField.startSelecting();
      updateCursor();
      return true;
    } else if (inputEvent == NiftyInputEvent.SelectionEnd) {
      textField.endSelecting();
      updateCursor();
      return true;
    } else if (inputEvent == NiftyInputEvent.Cut) {
      textField.cut(passwordChar);
      updateCursor();
      return true;
    } else if (inputEvent == NiftyInputEvent.Copy) {
      textField.copy(passwordChar);
      updateCursor();
      return true;
    } else if (inputEvent == NiftyInputEvent.Paste) {
      textField.put();
      updateCursor();
      return true;
    } else if (inputEvent == NiftyInputEvent.Character) {
      textField.insert(inputEvent.getCharacter());
      updateCursor();
      return true;
    } else if (inputEvent == NiftyInputEvent.NextInputElement) {
      if (focusHandler != null) {
        focusHandler.getNext(fieldElement).setFocus();
        updateCursor();
        return true;
      }
    } else if (inputEvent == NiftyInputEvent.PrevInputElement) {
      textField.endSelecting();
      if (focusHandler != null) {
        focusHandler.getPrev(fieldElement).setFocus();
        updateCursor();
        return true;
      }
    }

    updateCursor();
    return false;
  }

  private void updateCursor() {
    TextRenderer textRenderer = textElement.getRenderer(TextRenderer.class);
    String text = textField.getText();
    checkBounds(text, textRenderer);
    calcLastVisibleIndex(textRenderer);

    // update text
    if (isPassword(passwordChar)) {
      int numChar = text.length();
      char[] chars = new char[numChar];
      for (int i = 0; i < numChar; ++i) {
        chars[i] = passwordChar;
      }
      text = new String(chars);
    }
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

    cursorElement.setConstraintX(new SizeValue(cursorPixelPos + "px"));
    cursorElement
        .setConstraintY(new SizeValue((element.getHeight() - cursorElement.getHeight()) / 2 + CURSOR_Y + "px"));
    cursorElement.startEffect(EffectEventId.onActive, null);
    if (screen != null) {
      screen.layoutLayers();
    }
  }

  private boolean isPassword(final Character currentPasswordChar) {
    return currentPasswordChar != null;
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

  private void checkBounds(final String text, final TextRenderer textRenderer) {
    int textLen = text.length();
    if (firstVisibleCharacterIndex > textLen) {
      // re position so that we show at much possible text
      lastVisibleCharacterIndex = textLen;
      firstVisibleCharacterIndex = FontHelper.getVisibleCharactersFromEnd(textRenderer.getFont(), text, fieldWidth,
          1.0f);
    }
  }

  private void calcLastVisibleIndex(final TextRenderer textRenderer) {
    String currentText = this.textField.getText();
    if (firstVisibleCharacterIndex < currentText.length()) {
      String textToCheck = currentText.substring(firstVisibleCharacterIndex);
      int lengthFitting = FontHelper.getVisibleCharactersFromStart(textRenderer.getFont(), textToCheck, fieldWidth,
          1.0f);
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

  public String getText() {
    return textField.getText();
  }

  public void setText(final String newText) {
    textField.initWithText(newText);
    updateCursor();
  }

  public void setMaxLength(final int maxLength) {
    textField.setMaxLength(maxLength);
  }

  public void setCursorPosition(final int position) {
    textField.setCursorPosition(position);
    updateCursor();
  }
}
