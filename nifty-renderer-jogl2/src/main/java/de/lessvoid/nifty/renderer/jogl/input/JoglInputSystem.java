package de.lessvoid.nifty.renderer.jogl.input;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

import com.jogamp.newt.event.InputEvent;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;

import de.lessvoid.nifty.NiftyInputConsumer;
import de.lessvoid.nifty.input.keyboard.KeyboardInputEvent;
import de.lessvoid.nifty.spi.input.InputSystem;
import de.lessvoid.nifty.tools.resourceloader.NiftyResourceLoader;

/**
 * This is a direct port of the LwjglInputSystem to JOGL and the old existing InputSystem for JOGL.
 * @author void
 */
public class JoglInputSystem implements InputSystem, MouseListener, KeyListener {
  private Logger log = Logger.getLogger(JoglInputSystem.class.getName());
  private AwtToNiftyKeyCodeConverter converter = new AwtToNiftyKeyCodeConverter();

  // queues to store events received from JOGL in
  private ConcurrentLinkedQueue<MouseInputEvent> mouseEvents = new ConcurrentLinkedQueue<MouseInputEvent>();
  private ConcurrentLinkedQueue<KeyboardInputEvent> keyboardEvents = new ConcurrentLinkedQueue<KeyboardInputEvent>();

  // queues to store events not processed by Nifty in
  private ConcurrentLinkedQueue<MouseInputEvent> mouseEventsOut = new ConcurrentLinkedQueue<MouseInputEvent>();
  private ConcurrentLinkedQueue<KeyboardInputEvent> keyboardEventsOut = new ConcurrentLinkedQueue<KeyboardInputEvent>();

  // some booleans to remember if nifty currently has the focus
  private boolean niftyHasKeyboardFocus = true;
  private boolean niftyTakesKeyboardFocusOnClick = false;

  public boolean niftyHasKeyboardFocus() {
    return niftyHasKeyboardFocus;
  }

  public void niftyTakesKeyboardFocusOnClick(final boolean niftyTakesKeyboardFocusOnClick) {
    this.niftyTakesKeyboardFocusOnClick = niftyTakesKeyboardFocusOnClick;
  }

  // InputSystem Implementation

  @Override
  public void setResourceLoader(final NiftyResourceLoader resourceLoader) {
    // We currently don't need any resources so we don't have any use for this
    // method
  }

  @Override
  public void forwardEvents(final NiftyInputConsumer inputEventConsumer) {
    mouseEventsOut.clear();
    keyboardEventsOut.clear();

    processMouseEvents(inputEventConsumer);
    processKeyboardEvents(inputEventConsumer);
  }

  @Override
  public void setMousePosition(final int x, final int y) {
    // TODO implement this method later
  }

  // MouseMotionListener Implementation

  @Override
  public void mouseDragged(final MouseEvent mouseEvent) {
    if (mouseEvent.getButton() == MouseEvent.BUTTON1 || mouseEvent.getModifiers() == InputEvent.BUTTON1_MASK) {
      mouseEvents.add(new MouseInputEvent(mouseEvent.getX(), mouseEvent.getY(), 0, 0, true));
    }
    if (mouseEvent.getButton() == MouseEvent.BUTTON2 || mouseEvent.getModifiers() == InputEvent.BUTTON2_MASK) {
      mouseEvents.add(new MouseInputEvent(mouseEvent.getX(), mouseEvent.getY(), 0, 1, true));
    }
    if (mouseEvent.getButton() == MouseEvent.BUTTON3 || mouseEvent.getModifiers() == InputEvent.BUTTON3_MASK) {
      mouseEvents.add(new MouseInputEvent(mouseEvent.getX(), mouseEvent.getY(), 0, 2, true));
    }
  }

  @Override
  public void mouseMoved(final MouseEvent mouseEvent) {
    mouseEvents.add(new MouseInputEvent(mouseEvent.getX(), mouseEvent.getY(), 0, -1, false));
  }

  // MouseListener Implementation

  @Override
  public void mouseClicked(final MouseEvent mouseEvent) {
  }

  @Override
  public void mouseEntered(final MouseEvent mouseEvent) {
  }

  @Override
  public void mouseExited(final MouseEvent mouseEvent) {
  }

  @Override
  public void mousePressed(final MouseEvent mouseEvent) {
    if (mouseEvent.getButton() == MouseEvent.BUTTON1) {
      mouseEvents.add(new MouseInputEvent(mouseEvent.getX(), mouseEvent.getY(), 0, 0, true));
    }
    if (mouseEvent.getButton() == MouseEvent.BUTTON2) {
      mouseEvents.add(new MouseInputEvent(mouseEvent.getX(), mouseEvent.getY(), 0, 1, true));
    }
    if (mouseEvent.getButton() == MouseEvent.BUTTON3) {
      mouseEvents.add(new MouseInputEvent(mouseEvent.getX(), mouseEvent.getY(), 0, 2, true));
    }
  }

  @Override
  public void mouseReleased(final MouseEvent mouseEvent) {
    if (mouseEvent.getButton() == MouseEvent.BUTTON1) {
      mouseEvents.add(new MouseInputEvent(mouseEvent.getX(), mouseEvent.getY(), 0, 0, false));
    }
    if (mouseEvent.getButton() == MouseEvent.BUTTON2) {
      mouseEvents.add(new MouseInputEvent(mouseEvent.getX(), mouseEvent.getY(), 0, 1, false));
    }
    if (mouseEvent.getButton() == MouseEvent.BUTTON3) {
      mouseEvents.add(new MouseInputEvent(mouseEvent.getX(), mouseEvent.getY(), 0, 2, false));
    }
  }

  @Override
  public void mouseWheelMoved(final MouseEvent mouseEvent) {
    // void: I have no idea if this would be the correct way to translate mouse wheel events
    mouseEvents.add(new MouseInputEvent(mouseEvent.getX(), mouseEvent.getY(), (int) mouseEvent.getWheelRotation(), -1, false));
  }

  // KeyListener implementation

  @Override
  public void keyPressed(final KeyEvent e) {
    handleKeyEvent(e, true);
  }

  @Override
  public void keyReleased(final KeyEvent e) {
    handleKeyEvent(e, false);
  }

  // Additional methods to access events in case they've got not handled by
  // Nifty

  /**
   * This can be called the check if any mouse events have not been handled by
   * Nifty.
   * 
   * @return true when mouse events are available and false if not
   */
  public boolean hasNextMouseEvent() {
    return mouseEventsOut.peek() != null;
  }

  /**
   * Retrieve a unhandled mouse event from the internal queue.
   * 
   * @return MouseInputEvent of the mouse event that was not handled by Nifty
   */
  public MouseInputEvent nextMouseEvent() {
    return mouseEventsOut.poll();
  }

  /**
   * This can be called the check if any keyboard events have not been handled
   * by Nifty.
   * 
   * @return true when keyboard events are available and false if not
   */
  public boolean hasNextKeyboardEvent() {
    return keyboardEventsOut.peek() != null;
  }

  /**
   * Retrieve a unhandled keyboard event from the internal queue.
   * 
   * @return KeyboardInputEvent of the event that was not handled by Nifty
   */
  public KeyboardInputEvent nextKeyboardEvent() {
    return keyboardEventsOut.poll();
  }

  // Internals

  private void processMouseEvents(final NiftyInputConsumer inputEventConsumer) {
    MouseInputEvent mouseEvent = mouseEvents.poll();
    while (mouseEvent != null) {
      // now send the event to nifty
      boolean mouseEventProcessedByNifty = mouseEvent.sendToNifty(inputEventConsumer);
      if (!mouseEventProcessedByNifty) {
        log.fine("Nifty did not processed this mouse event. You can handle it.");

        // nifty did not process this event, it did not hit any element
        mouseEventsOut.offer(new MouseInputEvent(mouseEvent));

        if (niftyTakesKeyboardFocusOnClick) {
          log.fine("Nifty gave up the keyboard focus");
          niftyHasKeyboardFocus = false; // give up focus if clicked outside nifty
        }
      } else {
        log.fine("Nifty has processed this mouse event");
        // nifty did handle that event. it hit an element and was processed by some GUI element
        if (niftyTakesKeyboardFocusOnClick) {
          // take focus if nifty element is  clicked
          log.fine("Nifty takes the keyboard focus back");
          niftyHasKeyboardFocus = true;
        }
      }

      // get next event
      mouseEvent = mouseEvents.poll();
    }
  }

  private void processKeyboardEvents(final NiftyInputConsumer inputEventConsumer) {
    KeyboardInputEvent keyEvent = keyboardEvents.poll();
    while (keyEvent != null) {
      // due to or short-circuiting on true, the event will get forward to
      // keyboardEventsOut if keyboardEventsOut=true
      if (!niftyHasKeyboardFocus || !inputEventConsumer.processKeyboardEvent(keyEvent)) {
        keyboardEventsOut.offer(keyEvent);
      }

      // get next event
      keyEvent = keyboardEvents.poll();
    }
  }

  private void handleKeyEvent(final KeyEvent e, final boolean isKeyDown) {
    keyboardEvents.add(convert(e, isKeyDown, converter.convertToNiftyKeyCode(e.getKeyCode(), e.getKeySymbol())));
  }

  private KeyboardInputEvent convert(final KeyEvent e, final boolean isKeyDown, final int keyCode) {
    return new KeyboardInputEvent(keyCode, e.getKeyChar(), isKeyDown, e.isShiftDown(), e.isControlDown());
  }

  public class MouseInputEvent {
    private final int mouseX;
    private final int mouseY;
    // the button that has been pressed with -1 = no button, 0 = first button, 1
    // = second button and so on
    private final int button;
    private final int mouseWheel;
    // the button was pressed down (true) or has been released (false)
    private final boolean buttonDown;

    MouseInputEvent(final int mx, final int my, final int scroll, final int button, final boolean buttonDown) {
      this.mouseX = mx;
      this.mouseY = my;
      this.button = button;
      this.mouseWheel = scroll;
      this.buttonDown = buttonDown;
    }

    MouseInputEvent(final MouseInputEvent mouseEvent) {
      this.mouseX = mouseEvent.mouseX;
      this.mouseY = mouseEvent.mouseY;
      this.button = mouseEvent.button;
      this.mouseWheel = mouseEvent.mouseWheel;
      this.buttonDown = mouseEvent.buttonDown;
    }

    boolean sendToNifty(final NiftyInputConsumer inputEventConsumer) {
      return inputEventConsumer.processMouseEvent(
          mouseX,
          mouseY,
          mouseWheel,
          button,
          buttonDown);
    }

    public int getMouseX() {
      return mouseX;
    }

    public int getMouseY() {
      return mouseY;
    }

    public int getButton() {
      return button;
    }

    public int getMouseWheel() {
      return mouseWheel;
    }

    public boolean isButtonDown() {
      return buttonDown;
    }

    public String toString() {
      return this.button + "=" + this.buttonDown + " at " + this.mouseX + "," + this.mouseY + " scroll:" + this.mouseWheel;
    }
  }
}
