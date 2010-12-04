package de.lessvoid.nifty.renderer.lwjgl.input;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import de.lessvoid.nifty.NiftyInputConsumer;
import de.lessvoid.nifty.input.keyboard.KeyboardInputEvent;
import de.lessvoid.nifty.input.mouse.MouseInputEvent;
import de.lessvoid.nifty.spi.input.InputSystem;

public class LwjglInputSystem implements InputSystem {
  private boolean lastLeftMouseDown = false;
  private LwjglKeyboardInputEventCreator keyboardEventCreator = new LwjglKeyboardInputEventCreator();
  private List<KeyboardInputEvent> keyboardEvents = new ArrayList<KeyboardInputEvent>();
  private IntBuffer viewportBuffer = BufferUtils.createIntBuffer(4 * 4);

  public void startup() throws Exception {
    Mouse.create();
    Keyboard.create();
    Keyboard.enableRepeatEvents(true);
  }

  public void shutdown() {
    Mouse.destroy();
    Keyboard.destroy();
  }

  public void forwardEvents(final NiftyInputConsumer inputEventConsumer) {
    processMouseEvents(inputEventConsumer);
    processKeyboardEvents(inputEventConsumer);
  }  

  private void processMouseEvents(final NiftyInputConsumer inputEventConsumer) {
    GL11.glGetInteger(GL11.GL_VIEWPORT, viewportBuffer);
    int viewportHeight = viewportBuffer.get(3);

    while (Mouse.next()) {
      int mouseX = Mouse.getEventX();
      int mouseY = viewportHeight - Mouse.getEventY();
      if (Mouse.getEventButton() == 0) {
        boolean leftMouseButton = Mouse.getEventButtonState();
        if (leftMouseButton != lastLeftMouseDown) {
          lastLeftMouseDown = leftMouseButton;
        }
      }
      MouseInputEvent inputEvent = new MouseInputEvent(mouseX, mouseY, lastLeftMouseDown);
      inputEventConsumer.processMouseEvent(inputEvent);
    }
  }

  private void processKeyboardEvents(final NiftyInputConsumer inputEventConsumer) {
    keyboardEvents.clear();
    while (Keyboard.next()) {
      inputEventConsumer.processKeyboardEvent(keyboardEventCreator.createEvent(Keyboard.getEventKey(), Keyboard.getEventCharacter(), Keyboard.getEventKeyState()));
    }
  }
}
