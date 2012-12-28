package de.lessvoid.nifty.renderer.lwjgl.input;

/**
 *
 * @author Joseph
 */
import java.nio.IntBuffer;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

import org.lwjgl.BufferUtils;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import de.lessvoid.nifty.NiftyInputConsumer;
import de.lessvoid.nifty.input.keyboard.KeyboardInputEvent;
import de.lessvoid.nifty.spi.input.InputSystem;
import de.lessvoid.nifty.tools.resourceloader.NiftyResourceLoader;

public class LwjglInputSystem implements InputSystem {
  private Logger log = Logger.getLogger(LwjglInputSystem.class.getName());
  private LwjglKeyboardInputEventCreator keyboardEventCreator = new LwjglKeyboardInputEventCreator();
  private IntBuffer viewportBuffer = BufferUtils.createIntBuffer(4 * 4);
  private ConcurrentLinkedQueue<MouseInputEvent> mouseEventsOut = new ConcurrentLinkedQueue<MouseInputEvent>();
  private ConcurrentLinkedQueue<KeyboardInputEvent> keyboardEventsOut = new ConcurrentLinkedQueue<KeyboardInputEvent>();
  public boolean niftyHasKeyboardFocus = true;
  public boolean niftyTakesKeyboardFocusOnClick = false;

  @Override
  public void setResourceLoader(final NiftyResourceLoader resourceLoader) {
  }

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

  // InputSystem Implementation

  public void forwardEvents(final NiftyInputConsumer inputEventConsumer) {
    mouseEventsOut.clear();
    keyboardEventsOut.clear();

    processMouseEvents(inputEventConsumer);
    processKeyboardEvents(inputEventConsumer);
  }

  public void setMousePosition(final int x, final int y) {
    int viewportHeight = getViewportHeight();
    Mouse.setCursorPosition(x, viewportHeight - y);
  }

  // Additional methods to access events in case they've got not handled by Nifty

  /**
   * This can be called the check if any mouse events have not been handled by Nifty.
   * @return true when mouse events are available and false if not
   */
  public boolean hasNextMouseEvent() {
    return mouseEventsOut.peek() != null;
  }

  /**
   * Retrieve a unhandled mouse event from the internal queue.
   * @return MouseInputEvent of the mouse event that was not handled by Nifty
   */
  public MouseInputEvent nextMouseEvent() {
    return mouseEventsOut.poll();
  }

  /**
   * This can be called the check if any keyboard events have not been handled by Nifty.
   * @return true when keyboard events are available and false if not
   */
  public boolean hasNextKeyboardEvent() {
    return keyboardEventsOut.peek() != null;
  }

  /**
   * Retrieve a unhandled keyboard event from the internal queue.
   * @return KeyboardInputEvent of the event that was not handled by Nifty
   */
  public KeyboardInputEvent nextKeyboardEvent() {
    return keyboardEventsOut.poll();
  }

  // Internals

  private void processMouseEvents(final NiftyInputConsumer inputEventConsumer) {
    int viewportHeight = getViewportHeight();
    while (Mouse.next()) {
      int mouseX = Mouse.getEventX();
      int mouseY = viewportHeight - Mouse.getEventY();
      int mouseWheel = Mouse.getEventDWheel() / 120; // not sure about that 120 here. works on my system and makes this return 1 if the wheel is moved the minimal amount.
      int button = Mouse.getEventButton();
      boolean buttonDown = Mouse.getEventButtonState();

      // now send the event to nifty
      boolean mouseEventProcessedByNifty = inputEventConsumer.processMouseEvent(mouseX, mouseY, mouseWheel, button, buttonDown);
      if (!mouseEventProcessedByNifty) {
        log.fine("Nifty did not processed this mouse event. You can handle it.");

        // nifty did not process this event, it did not hit any element
        mouseEventsOut.offer(new MouseInputEvent(mouseX, mouseY, mouseWheel, button, buttonDown));
        if (niftyTakesKeyboardFocusOnClick) {
          log.fine("Nifty gave up the keyboard focus");
          niftyHasKeyboardFocus = false; // give up focus if clicked outside nifty
        }
      } else {
        log.fine("Nifty has processed this mouse event");

        // nifty did handle that event. it hit an element and was processed by some GUI element
        if (niftyTakesKeyboardFocusOnClick) { // take focus if nifty element is clicked
          log.fine("Nifty takes the keyboard focus back");
          niftyHasKeyboardFocus = true;
        }
      }
    }
  }

  private void processKeyboardEvents(final NiftyInputConsumer inputEventConsumer) {
    while (Keyboard.next()) {
      KeyboardInputEvent event = keyboardEventCreator.createEvent(Keyboard.getEventKey(), Keyboard.getEventCharacter(), Keyboard.getEventKeyState());
      // due to or short-circuiting on true, the event will get forward to keyboardEventsOut if keyboardEventsOut=true
      if (!niftyHasKeyboardFocus || !inputEventConsumer.processKeyboardEvent(event))
        keyboardEventsOut.offer(event);
    }
  }

  private int getViewportHeight() {
    GL11.glGetInteger(GL11.GL_VIEWPORT, viewportBuffer);
    return viewportBuffer.get(3);
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
    log.fine("native cursor support (" + caps + ") -> [" + out.toString() + "]");
    log.fine("native cursor min size: " + Cursor.getMinCursorSize());
    log.fine("native cursor max size: " + Cursor.getMaxCursorSize());
  }

  private static void add(StringBuffer out, String text) {
    if (out.length() > 0) {
      out.append(", ");
    }
    out.append(text);
  }

  public class MouseInputEvent {
    public float mouseX;
    public float mouseY;
    public float pmouseX;
    public float pmouseY;
    public int button;
    public int scroll;
    public boolean buttonDown;

    MouseInputEvent(float mx, float my, int scroll, int button, boolean buttonDown) {
      this.mouseX = mx;
      this.mouseY = my;
      this.button = button;
      this.scroll = scroll;
      this.buttonDown = buttonDown;
    }

    public String toString() {
      return this.button + "=" + this.buttonDown + " at " + this.mouseX + "," + this.mouseY + " scroll:" + this.scroll;
    }
  }
}
