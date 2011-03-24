package de.lessvoid.nifty.slick2d.input;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import de.lessvoid.nifty.NiftyInputConsumer;
import de.lessvoid.nifty.input.keyboard.KeyboardInputEvent;
import de.lessvoid.nifty.spi.input.InputSystem;

public class LwjglInputSystem implements InputSystem {
  private boolean lastLeftMouseDown = false;
  private LwjglKeyboardInputEventCreator keyboardEventCreator = new LwjglKeyboardInputEventCreator();
  private List<KeyboardInputEvent> keyboardEvents = new ArrayList<KeyboardInputEvent>();

  public void startup() throws Exception {
    Mouse.create();
  }

  public void shutdown() {
    Mouse.destroy();
  }

  public void forwardEvents(final NiftyInputConsumer inputEventConsumer) {
    processMouseEvents(inputEventConsumer);
    processKeyboardEvents(inputEventConsumer);
  }  

  private void processMouseEvents(final NiftyInputConsumer inputEventConsumer) {
    while (Mouse.next()) {
      int mouseX = Mouse.getEventX();
      int mouseY = Display.getDisplayMode().getHeight() - Mouse.getEventY();
      int mouseWheel = Mouse.getEventDWheel() / 120; // not sure about that 120 here. works on my system and makes this return 1 if the wheel is moved the minimal amount.
      int button = Mouse.getEventButton();
      boolean buttonDown = Mouse.getEventButtonState();
      inputEventConsumer.processMouseEvent(mouseX, mouseY, mouseWheel, button, buttonDown);
    }
  }

  private void processKeyboardEvents(final NiftyInputConsumer inputEventConsumer) {
    keyboardEvents.clear();
    while (Keyboard.next()) {
      inputEventConsumer.processKeyboardEvent(keyboardEventCreator.createEvent(Keyboard.getEventKey(), Keyboard.getEventCharacter(), Keyboard.getEventKeyState()));
    }
  }

  @Override
  public void setMousePosition(int x, int y) {
  }
}
