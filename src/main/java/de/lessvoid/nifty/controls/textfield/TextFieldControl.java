package de.lessvoid.nifty.controls.textfield;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.controls.FocusHandler;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.ControllerEventListener;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.elements.tools.FontHelper;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.nifty.tools.TimeProvider;

/**
 * A TextFieldControl.
 * @author void
 */
public class TextFieldControl implements Controller {

  /**
   * cursor y position offset.
   */
  private static final int CURSOR_Y = 3;

  /**
   * the screen.
   */
  private Screen screen;

  /**
   * The element we're connected to.
   */
  private Element element;

  /**
   * text element.
   */
  private Element textElement;

  /**
   * field element.
   */
  private Element fieldElement;

  /**
   * cursor element.
   */
  private Element cursorElement;

  /**
   * text field.
   */
  private TextField textField;

  /**
   * time provider.
   */
  private final TimeProvider timeProvider;

  /**
   * fist visible character index.
   */
  private int firstVisibleCharacterIndex;

  /**
   * last visible character index.
   */
  private int lastVisibleCharacterIndex;

  /**
   * field width.
   */
  private int fieldWidth;

  /**
   * from click cursor position.
   */
  private int fromClickCursorPos;

  /**
   * to click cursor position.
   */
  private int toClickCursorPos;

  /**
   * the focus handler this control belongs to.
   */
  private FocusHandler focusHandler;

  /**
   * default constructor.
   */
  public TextFieldControl() {
    timeProvider = new TimeProvider();
  }

  /**
   * Bind this controller to the given element.
   * @param niftyParam niftyParam
   * @param newElement the new element to set
   * @param properties all attributes of the xml tag we're connected to
   * @param newListener listener
   */
  public void bind(
      final Nifty niftyParam,
      final Element newElement,
      final Properties properties,
      final ControllerEventListener newListener) {
    this.element = newElement;
    this.fromClickCursorPos = -1;
    this.toClickCursorPos = -1;

    this.textField = new TextField("", new ClipboardAWT());
    this.textField.toFirstPosition();
  }

  /**
   * On start screen event.
   * @param newScreen screen
   */
  public void onStartScreen(final Screen newScreen) {
    this.screen = newScreen;
    this.focusHandler = screen.getFocusHandler();

    this.textElement = element.findElementByName("textfield-text");
    this.textField.initWithText(textElement.getRenderer(TextRenderer.class).getOriginalText());

    this.fieldElement = element.findElementByName("textfield-field");
    this.cursorElement = element.findElementByName("textfield-cursor");

    this.fieldWidth = this.fieldElement.getWidth() - this.cursorElement.getWidth();

    TextRenderer textRenderer = textElement.getRenderer(TextRenderer.class);
    this.firstVisibleCharacterIndex = 0;
    this.lastVisibleCharacterIndex =
      FontHelper.getVisibleCharactersFromStart(textRenderer.getFont(), this.textField.getText(), fieldWidth, 1.0f);

    cursorElement.hide();
    updateCursor();
  }

  /**
   * click.
   * @param mouseX the mouse x position
   * @param mouseY the mouse y position
   */
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

  /**
   * on click mouse move.
   * @param mouseX mouse x
   * @param mouseY mouse y
   */
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

  /**
   * getCursorPosFromMouse.
   * @param mouseX mouse x
   * @param visibleString visible string
   * @return cursor from mouse
   */
  private int getCursorPosFromMouse(final int mouseX, final String visibleString) {
    TextRenderer textRenderer = textElement.getRenderer(TextRenderer.class);
    return FontHelper.getCharacterIndexFromPixelPosition(
        textRenderer.getFont(), visibleString, (mouseX - fieldElement.getX()), 1.0f);
  }

  /**
   * handle input event.
   * @param inputEvent input event
   */
  public void inputEvent(final NiftyInputEvent inputEvent) {
    if (inputEvent == NiftyInputEvent.MoveCursorLeft) {
      textField.cursorLeft();
    } else if (inputEvent == NiftyInputEvent.MoveCursorRight) {
      textField.cursorRight();
    } else if (inputEvent == NiftyInputEvent.Delete) {
      textField.delete();
    } else if (inputEvent == NiftyInputEvent.Backspace) {
      textField.backspace();
    } else if (inputEvent == NiftyInputEvent.MoveCursorToLastPosition) {
      textField.toLastPosition();
    } else if (inputEvent == NiftyInputEvent.MoveCursorToFirstPosition) {
      textField.toFirstPosition();
    } else if (inputEvent == NiftyInputEvent.SelectionStart) {
      textField.startSelecting();
    } else if (inputEvent == NiftyInputEvent.SelectionEnd) {
      textField.endSelecting();
    } else if (inputEvent == NiftyInputEvent.Cut) {
      textField.cut();
    } else if (inputEvent == NiftyInputEvent.Copy) {
      textField.copy();
    } else if (inputEvent == NiftyInputEvent.Paste) {
      textField.put();
    } else if (inputEvent == NiftyInputEvent.SubmitText) {
      this.screen.setFocus(null);
    } else if (inputEvent == NiftyInputEvent.Character) {
      textField.insert(inputEvent.getCharacter());
    } else if (inputEvent == NiftyInputEvent.NextInputElement) {
      if (focusHandler != null) {
        focusHandler.getNext(fieldElement).setFocus();
      }
    } else if (inputEvent == NiftyInputEvent.PrevInputElement) {
      if (focusHandler != null) {
        focusHandler.getPrev(fieldElement).setFocus();
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
    cursorElement.setConstraintY(
        new SizeValue((element.getHeight() - cursorElement.getHeight()) / 2 + CURSOR_Y + "px"));
    cursorElement.startEffect(EffectEventId.onActive, timeProvider, null);
    screen.layoutLayers();
  }

  /**
   * calcFirstVisibleIndex.
   * @param cursorPos cursor pos
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
   * @param text text
   * @param textRenderer text renderer
   */
  private void checkBounds(final String text, final TextRenderer textRenderer) {
    int textLen = text.length();
    if (firstVisibleCharacterIndex > textLen) {
      // re position so that we show at much possible text
      lastVisibleCharacterIndex = textLen;
      firstVisibleCharacterIndex =
        FontHelper.getVisibleCharactersFromEnd(textRenderer.getFont(), text, fieldWidth, 1.0f);
    }
  }

  /**
   * @param textRenderer TextRenderer
   */
  private void calcLastVisibleIndex(final TextRenderer textRenderer) {
    String currentText = this.textField.getText();
    if (firstVisibleCharacterIndex < currentText.length()) {
      String textToCheck = currentText.substring(firstVisibleCharacterIndex);
      int lengthFitting =
        FontHelper.getVisibleCharactersFromStart(textRenderer.getFont(), textToCheck, fieldWidth, 1.0f);
      lastVisibleCharacterIndex = lengthFitting + firstVisibleCharacterIndex;
    } else {
      lastVisibleCharacterIndex = firstVisibleCharacterIndex;
    }
  }

  /**
   * on focus event.
   * @param getFocus get (true) or lose (false) focus
   */
  public void onFocus(final boolean getFocus) {
    if (getFocus) {
      cursorElement.show();
    } else {
      cursorElement.hide();
    }
  }
}
