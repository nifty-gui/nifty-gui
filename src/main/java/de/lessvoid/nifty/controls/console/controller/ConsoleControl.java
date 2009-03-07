package de.lessvoid.nifty.controls.console.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.controls.dynamic.CreateCustomControl;
import de.lessvoid.nifty.controls.textfield.CreateTextFieldControl;
import de.lessvoid.nifty.controls.textfield.controller.TextFieldControl;
import de.lessvoid.nifty.elements.ControllerEventListener;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;

/**
 * A ConsoleControl.
 * @author void
 */
public class ConsoleControl implements Controller {
  private Nifty nifty;
  private Screen screen;
  private Element element;
  private int lines;
  private List < String > buffer = new ArrayList < String >();
  private static final int MAX_BUFFER_LINES = 100;
  private Collection < ConsoleCommandHandler > commandHandler = new ArrayList < ConsoleCommandHandler >();

  public ConsoleControl() {
  }

  public void bind(
      final Nifty niftyParam,
      final Screen screenParam,
      final Element newElement,
      final Properties properties,
      final ControllerEventListener newListener,
      final Attributes controlDefinitionAttributes) {
    this.nifty = niftyParam;
    this.screen = screenParam;
    this.element = newElement;
    this.lines = Integer.valueOf((String) properties.get("lines"));
    for (int i = 0; i < lines; i++) {
      CreateCustomControl createConsoleLine = new CreateCustomControl("console-line-" + i, "console-line");
      createConsoleLine.create(nifty, screen, element);
    }
    CreateTextFieldControl createTextField = new CreateTextFieldControl("console-input");
    createTextField.setStyle("nifty-console-textfield");
    createTextField.create(nifty, screen, element);
    screen.layoutLayers();
  }

  /**
   * On start screen event.
   * @param newScreen screen
   */
  public void onStartScreen() {
  }

  public void onClick(final int mouseX, final int mouseY) {
  }

  public void onClickMouseMove(final int mouseX, final int mouseY) {
  }

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

  private void notifyCommandHandler(final String commandLine) {
    for (ConsoleCommandHandler handler : commandHandler) {
      handler.execute(commandLine);
    }
  }

  public void onFocus(final boolean getFocus) {
    TextFieldControl control = this.element.findControl("console-input", TextFieldControl.class);
    if (control != null) {
      control.onFocus(getFocus);
    }
  }

  public void output(final String line) {
    if (line != null && line.length() > 0) {
      if (line.contains("\n")) {
        String[] splitLines = line.split("\n");
        for (String l : splitLines) {
          outputLine(l);
        }
      } else {
        outputLine(line);
      }
      showLines();
    }
  }

  private void outputLine(final String line) {
    buffer.add(line);
    if (buffer.size() > MAX_BUFFER_LINES) {
      buffer.remove(0);
    }
  }

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

  public void addCommandHandler(final ConsoleCommandHandler consoleCommandHandler) {
    commandHandler.add(consoleCommandHandler);
  }
}
