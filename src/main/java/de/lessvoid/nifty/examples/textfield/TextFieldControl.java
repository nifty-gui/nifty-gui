package de.lessvoid.nifty.examples.textfield;

import java.util.Properties;

import org.lwjgl.input.Keyboard;

import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.Controller;
import de.lessvoid.nifty.elements.ControllerEventListener;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.nifty.tools.TimeProvider;

/**
 * A TextFieldControl.
 * @author void
 */
public class TextFieldControl implements Controller {

  /**
   * the screen.
   */
  private Screen screen;

  /**
   * The element we're connected to.
   */
  private Element element;

  /**
   * The ControllerEventListener to use.
   */
  private ControllerEventListener listener;

  /**
   * 
   */
  private Element textElement;

  private Element fieldElement;

  private Element cursorElement;

  private TextField textField;

  private final TimeProvider timeProvider;

  private int firstVisibleCharacterIndex;
  private int lastVisibleCharacterIndex;

  private int fieldWidth;

  private int fromClickCursorPos;

  private int toClickCursorPos;

  public TextFieldControl() {
    timeProvider = new TimeProvider();
  }

  /**
   * Bind this controller to the given element.
   * @param newScreen the new nifty to set
   * @param newElement the new element to set
   * @param properties all attributes of the xml tag we're connected to
   * @param newListener listener
   */
  public void bind(
      final Screen newScreen,
      final Element newElement,
      final Properties properties,
      final ControllerEventListener newListener) {
    this.screen = newScreen;
    this.element = newElement;
    this.listener = newListener;
    this.fromClickCursorPos = -1;
    this.toClickCursorPos = -1;

    this.textField = new TextField("0123456789abcdefghijklmnopqrstuvwxyz", new ClipboardAWT());
    this.textField.toFirstPosition();
  }

  /**
   * On start screen event.
   */
  public void onStartScreen() {
    this.textElement = element.findElementByName("text");
    this.textElement.getRenderer(TextRenderer.class).changeText(textField.getText());

    this.fieldElement = element.findElementByName("field");
    this.cursorElement = element.findElementByName("cursor");

    this.fieldWidth = this.fieldElement.getWidth() - this.cursorElement.getWidth();

    TextRenderer textRenderer = textElement.getRenderer(TextRenderer.class);
    this.firstVisibleCharacterIndex = 0;
    this.lastVisibleCharacterIndex = textRenderer.getFont().getFittingOffset(this.textField.getText(), fieldWidth);

    cursorElement.hide();
    updateCursor();
  }

  /**
   * click.
   * @param mouseX the mouse x position
   * @param mouseY the mouse y position
   */
  public void onClick(final int mouseX, final int mouseY) {
    System.out.println("onClick: " + mouseX + ", " + mouseY);
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

    System.out.println("fromClickCursorPos: " + fromClickCursorPos + ", toClickCursorPos: " + toClickCursorPos);

    textField.setCursorPosition(fromClickCursorPos);
    textField.startSelecting();
    textField.setCursorPosition(toClickCursorPos);
    textField.endSelecting();
    updateCursor();
  }

  /**
   * @param mouseX
   * @param visibleString
   * @return
   */
  private int getCursorPosFromMouse(final int mouseX, String visibleString) {
    TextRenderer textRenderer = textElement.getRenderer(TextRenderer.class);
    return textRenderer.getFont().getIndexFromPixel(visibleString, (mouseX-fieldElement.getX()), 1.0f);
  }

  public void keyEvent(final int eventKey, final char eventCharacter, boolean keyDown) {
    if (keyDown && eventKey == Keyboard.KEY_LEFT) {
      textField.cursorLeft();
    } else if (keyDown && eventKey == Keyboard.KEY_RIGHT) {
      textField.cursorRight();
    } else if (keyDown && eventKey == Keyboard.KEY_DELETE) {
      textField.delete();
    } else if (keyDown && eventKey == Keyboard.KEY_BACK) {
      textField.backspace();
    } else if (keyDown && eventKey == Keyboard.KEY_END) {
      textField.toLastPosition();
    } else if (keyDown && eventKey == Keyboard.KEY_HOME) {
      textField.toFirstPosition();
    } else if (keyDown && (eventKey == Keyboard.KEY_LSHIFT || eventKey == Keyboard.KEY_RSHIFT)) {
      textField.startSelecting();
    } else if (!keyDown && (eventKey == Keyboard.KEY_LSHIFT || eventKey == Keyboard.KEY_RSHIFT)) {
      textField.endSelecting();
    } else if (keyDown && (eventKey == Keyboard.KEY_X)) {
      textField.cut();
    } else if (keyDown && (eventKey == Keyboard.KEY_C)) {
      textField.copy();
    } else if (keyDown && (eventKey == Keyboard.KEY_V)) {
      textField.put();
    } else {
      if (eventCharacter > 31 && eventCharacter < 127) {
        textField.insert(eventCharacter);
      }
    }

    updateCursor();
  }

  /**
   *
   */
  private void updateCursor() {
    TextRenderer textRenderer = textElement.getRenderer(TextRenderer.class);
    String text = textField.getText();
    checkBounds(text, textRenderer);
    calcLastVisibleIndex(textRenderer);

    // update text
    textRenderer.changeText(text);
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
    cursorElement.setConstraintY(new SizeValue((element.getHeight() - cursorElement.getHeight())/2 + 3 + "px"));
    cursorElement.startEffect(EffectEventId.onActive, timeProvider, null);
    screen.layoutLayers();

System.out.println(cursorPos + ": " + firstVisibleCharacterIndex + ", " + lastVisibleCharacterIndex + " (" + text.substring(firstVisibleCharacterIndex, lastVisibleCharacterIndex));    
  }

  /**
   * @param cursorPos
   */
  private void calcFirstVisibleIndex(final int cursorPos) {
    if (cursorPos > lastVisibleCharacterIndex) {
      int cursorPosDelta = cursorPos - lastVisibleCharacterIndex;
      firstVisibleCharacterIndex += cursorPosDelta;
    } else if (cursorPos < firstVisibleCharacterIndex) {
      int cursorPosDelta = firstVisibleCharacterIndex - cursorPos;
      firstVisibleCharacterIndex -= cursorPosDelta;
    }
  }

  /**
   * check bounds of first and last visible index according to text.
   * @param text
   * @param textRenderer TODO
   */
  private void checkBounds(final String text, final TextRenderer textRenderer) {
    int textLen = text.length();
    if (firstVisibleCharacterIndex > textLen) {
      // re position so that we show at much possible text
      lastVisibleCharacterIndex = textLen;
      firstVisibleCharacterIndex = textRenderer.getFont().getFittingOffsetBackward(text, fieldWidth);
    }
  }

  /**
   * @param textRenderer TextRenderer
   */
  private void calcLastVisibleIndex(final TextRenderer textRenderer) {
    String currentText = this.textField.getText();
    if (firstVisibleCharacterIndex < currentText.length()) {
      String textToCheck = currentText.substring(firstVisibleCharacterIndex);
      int lengthFitting = textRenderer.getFont().getFittingOffset(textToCheck, fieldWidth);
      lastVisibleCharacterIndex = lengthFitting + firstVisibleCharacterIndex;
    } else {
      lastVisibleCharacterIndex = firstVisibleCharacterIndex;
    }
  }

  public void onGetFocus() {
    cursorElement.show();
  }

  public void onLostFocus() {
    cursorElement.hide();
  }

  
}
