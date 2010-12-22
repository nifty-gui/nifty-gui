package de.lessvoid.nifty.renderer.lwjgl.input;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.lwjgl.BufferUtils;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import de.lessvoid.nifty.NiftyInputConsumer;
import de.lessvoid.nifty.input.keyboard.KeyboardInputEvent;
import de.lessvoid.nifty.input.mouse.MouseInputEvent;
import de.lessvoid.nifty.spi.input.InputSystem;

public class LwjglInputSystem implements InputSystem {
  private static Logger log = Logger.getLogger(LwjglInputSystem.class.getName());
  private boolean lastLeftMouseDown = false;
  private LwjglKeyboardInputEventCreator keyboardEventCreator = new LwjglKeyboardInputEventCreator();
  private List<KeyboardInputEvent> keyboardEvents = new ArrayList<KeyboardInputEvent>();
  private IntBuffer viewportBuffer = BufferUtils.createIntBuffer(4 * 4);

  public void startup() throws Exception {
    Mouse.create();
    Keyboard.create();
    Keyboard.enableRepeatEvents(true);

    logMouseCapabilities();
  }

  public void shutdown() {
    Mouse.destroy();
    Keyboard.destroy();
  }

  public void forwardEvents(final NiftyInputConsumer inputEventConsumer) {
    processMouseEvents(inputEventConsumer);
    processKeyboardEvents(inputEventConsumer);
  }  

  public void setMousePosition(final int x, final int y) {
    int viewportHeight = getViewportHeight();
    Mouse.setCursorPosition(x, viewportHeight - y);
  }

  private void processMouseEvents(final NiftyInputConsumer inputEventConsumer) {
    int viewportHeight = getViewportHeight();

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

  private int getViewportHeight() {
    GL11.glGetInteger(GL11.GL_VIEWPORT, viewportBuffer);
    int viewportHeight = viewportBuffer.get(3);
    return viewportHeight;
  }

  private void processKeyboardEvents(final NiftyInputConsumer inputEventConsumer) {
    keyboardEvents.clear();
    while (Keyboard.next()) {
      inputEventConsumer.processKeyboardEvent(keyboardEventCreator.createEvent(Keyboard.getEventKey(), Keyboard.getEventCharacter(), Keyboard.getEventKeyState()));
    }
  }

  private void logMouseCapabilities() {
    int caps = Cursor.getCapabilities();
    StringBuffer out = new StringBuffer();
    if ((caps & Cursor.CURSOR_ONE_BIT_TRANSPARENCY) != 0) {
      add(out, "CURSOR_ONE_BIT_TRANSPARENCY");
    }
    if ((caps & Cursor.CURSOR_8_BIT_ALPHA) != 0) {
      add(out, "CURSOR_8_BIT_ALPHA");
    }
    if ((caps & Cursor.CURSOR_ANIMATION) != 0) {
      add(out, "CURSOR_ANIMATION");
    }
    log.info("native cursor support (" + caps + ") -> [" + out.toString() + "]");
    log.info("native cursor min size: " + Cursor.getMinCursorSize());
    log.info("native cursor max size: " + Cursor.getMaxCursorSize());
  }

  private static void add(StringBuffer out, String text) {
    if (out.length() > 0) {
      out.append(", ");
    }
    out.append(text);
  }
}
