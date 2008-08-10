package de.lessvoid.nifty.controls.console;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.controls.textfield.TextFieldControl;
import de.lessvoid.nifty.elements.ControllerEventListener;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;

/**
 * A ConsoleControl.
 * @author void
 */
public class ConsoleControl implements Controller {

  /**
   * nifty.
   */
  private Nifty nifty;

  /**
   * the screen.
   */
  private Screen screen;

  /**
   * The element we're connected to.
   */
  private Element element;

  /**
   * lines.
   */
  private int lines;

  /**
   * line buffer for the console.
   */
  private List < String > buffer = new ArrayList < String >();

  /**
   * max buffer lines.
   */
  private static final int MAX_BUFFER_LINES = 100;

  /**
   * command handler.
   */
  private Collection < ConsoleCommandHandler > commandHandler = new ArrayList < ConsoleCommandHandler >();

  /**
   * default constructor.
   */
  public ConsoleControl() {
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
      final Properties properties, final ControllerEventListener newListener) {
    this.nifty = niftyParam;
    this.element = newElement;
    this.lines = Integer.valueOf((String) properties.get("lines"));
  }

  /**
   * On start screen event.
   * @param newScreen screen
   */
  public void onStartScreen(final Screen newScreen) {
    this.screen = newScreen;
    for (int i = 0; i < lines; i++) {
      this.nifty.addControl(screen, element, "console-line", "console-line-" + i, null, null);
    }
    this.nifty.addControl(screen, element, "textfield", "console-input", "nifty-console-textfield", false);
    this.screen.layoutLayers();
  }

  /**
   * click.
   * @param mouseX the mouse x position
   * @param mouseY the mouse y position
   */
  public void onClick(final int mouseX, final int mouseY) {
  }

  /**
   * on click mouse move.
   * @param mouseX mouse x
   * @param mouseY mouse y
   */
  public void onClickMouseMove(final int mouseX, final int mouseY) {
  }

  /**
   * input event.
   * @param inputEvent event
   */
  public void inputEvent(final NiftyInputEvent inputEvent) {
    TextFieldControl textControl = this.element.findElementByName("console-input").getControl(TextFieldControl.class);
    textControl.inputEvent(inputEvent);
    if (inputEvent == NiftyInputEvent.SubmitText) {
      String commandLine = textControl.getText();
      output(commandLine);
      textControl.setText("");
      this.element.setFocus();
      notifyCommandHandler(commandLine);
    }
  }

  /**
   * notify all commandHandler.
   * @param commandLine current line
   */
  private void notifyCommandHandler(final String commandLine) {
    for (ConsoleCommandHandler handler : commandHandler) {
      handler.execute(commandLine);
    }
  }

  /**
   * on focus event.
   * @param getFocus get or lose focus
   */
  public void onFocus(final boolean getFocus) {
    TextFieldControl control = this.element.findElementByName("console-input").getControl(TextFieldControl.class);
    control.onFocus(getFocus);
  }

  /**
   * output a string to the console.
   * @param line line to output
   */
  public void output(final String line) {
    buffer.add(line);
    if (buffer.size() > MAX_BUFFER_LINES) {
      buffer.remove(0);
    }

    showLines();
  }

  /**
   * show lines.
   */
  private void showLines() {
    if (buffer.isEmpty()) {
      return;
    }

    int lastLineIdx = buffer.size() - 1;
    for (int i = lines - 1; i >= 0; i--) {
      if (lastLineIdx >= 0) {
        String line = buffer.get(lastLineIdx);
        Element el = element.findElementByName("console-line-" + i);
        if (el != null) {
          el.getRenderer(TextRenderer.class).changeText(line);
        }
        lastLineIdx--;
      }
    }
  }

  /**
   * add a command handler.
   * @param consoleCommandHandler CommandHandler
   */
  public void addCommandHandler(final ConsoleCommandHandler consoleCommandHandler) {
    commandHandler.add(consoleCommandHandler);
  }
}
