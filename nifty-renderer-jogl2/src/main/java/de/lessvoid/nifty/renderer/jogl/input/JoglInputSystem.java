package de.lessvoid.nifty.renderer.jogl.input;

import com.jogamp.newt.event.*;

import de.lessvoid.nifty.NiftyInputConsumer;
import de.lessvoid.nifty.input.keyboard.KeyboardInputEvent;
import de.lessvoid.nifty.spi.input.InputSystem;
import de.lessvoid.nifty.tools.resourceloader.NiftyResourceLoader;

import javax.annotation.Nonnull;

import java.awt.Window;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

/**
 * This is a direct port of the LwjglInputSystem to JOGL and the old existing InputSystem for JOGL.
 *
 * @author void
 */
public class JoglInputSystem implements InputSystem, MouseListener, KeyListener {
	
  private final Logger log = Logger.getLogger(JoglInputSystem.class.getName());
  
  @Nonnull
  private final AwtToNiftyKeyCodeConverter converter = new AwtToNiftyKeyCodeConverter();

  // queues to store events received from JOGL in
  @Nonnull
  private final ConcurrentLinkedQueue<MouseInputEvent> mouseEvents = new ConcurrentLinkedQueue<MouseInputEvent>();
  
  @Nonnull
  private final ConcurrentLinkedQueue<KeyboardInputEvent> keyboardEvents = new
      ConcurrentLinkedQueue<KeyboardInputEvent>();

  // queues to store events not processed by Nifty in
  @Nonnull
  private final ConcurrentLinkedQueue<MouseInputEvent> mouseEventsOut = new ConcurrentLinkedQueue<MouseInputEvent>();
  
  @Nonnull
  private final ConcurrentLinkedQueue<KeyboardInputEvent> keyboardEventsOut = new
      ConcurrentLinkedQueue<KeyboardInputEvent>();
      
  public JoglInputSystem(@Nonnull final Window newtWindow) {
	  this.niftyNewtWindow = newtWindow;
  }
  
  private Window niftyNewtWindow;

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
  public void setResourceLoader(@Nonnull final NiftyResourceLoader resourceLoader) {
    // We currently don't need any resources so we don't have any use for this
    // method
  }

  @Override
  public void forwardEvents(@Nonnull final NiftyInputConsumer inputEventConsumer) {
    mouseEventsOut.clear();
    keyboardEventsOut.clear();

    processMouseEvents(inputEventConsumer);
    processKeyboardEvents(inputEventConsumer);
  }

  @Override
  public void setMousePosition(final int x, final int y) {
    int windowX = niftyNewtWindow.getX(), windowY = niftyNewtWindow.getY();
    niftyNewtWindow.warpPointer(x - windowX, y - windowY);
  }

  // MouseMotionListener Implementation

  @Override
  public void mouseDragged(@Nonnull final MouseEvent mouseEvent) {
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
  public void mouseMoved(@Nonnull final MouseEvent mouseEvent) {
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
  public void mousePressed(@Nonnull final MouseEvent mouseEvent) {
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
  public void mouseReleased(@Nonnull final MouseEvent mouseEvent) {
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
  public void mouseWheelMoved(@Nonnull final MouseEvent mouseEvent) {
    // void: I have no idea if this would be the correct way to translate mouse wheel events
    mouseEvents.add(new MouseInputEvent(mouseEvent.getX(), mouseEvent.getY(), (int) mouseEvent.getRotation()[0], -1,
        false));
  }

  // KeyListener implementation

  @Override
  public void keyPressed(@Nonnull final KeyEvent e) {
    handleKeyEvent(e, true);
  }

  @Override
  public void keyReleased(@Nonnull final KeyEvent e) {
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

  private void processMouseEvents(@Nonnull final NiftyInputConsumer inputEventConsumer) {
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

  private void processKeyboardEvents(@Nonnull final NiftyInputConsumer inputEventConsumer) {
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

  private void handleKeyEvent(@Nonnull final KeyEvent e, final boolean isKeyDown) {
    keyboardEvents.add(convert(e, isKeyDown, converter.convertToNiftyKeyCode(e.getKeyCode(), e.getKeySymbol())));
  }

  @Nonnull
  private KeyboardInputEvent convert(@Nonnull final KeyEvent e, final boolean isKeyDown, final int keyCode) {
    return new KeyboardInputEvent(keyCode, e.getKeyChar(), isKeyDown, e.isShiftDown(), e.isControlDown());
  }

  public static class MouseInputEvent {
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

    MouseInputEvent(@Nonnull final MouseInputEvent mouseEvent) {
      this.mouseX = mouseEvent.mouseX;
      this.mouseY = mouseEvent.mouseY;
      this.button = mouseEvent.button;
      this.mouseWheel = mouseEvent.mouseWheel;
      this.buttonDown = mouseEvent.buttonDown;
    }

    boolean sendToNifty(@Nonnull final NiftyInputConsumer inputEventConsumer) {
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

    @Override
    @Nonnull
    public String toString() {
      return this.button + "=" + this.buttonDown + " at " + this.mouseX + "," + this.mouseY + " scroll:" + this
          .mouseWheel;
    }
  }
}
