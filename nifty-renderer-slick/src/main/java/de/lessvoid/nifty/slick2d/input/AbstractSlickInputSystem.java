package de.lessvoid.nifty.slick2d.input;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.Input;
import org.newdawn.slick.util.InputAdapter;

import de.lessvoid.nifty.NiftyInputConsumer;
import de.lessvoid.nifty.slick2d.input.events.InputEvent;
import de.lessvoid.nifty.slick2d.input.events.KeyboardEventPressed;
import de.lessvoid.nifty.slick2d.input.events.KeyboardEventReleased;
import de.lessvoid.nifty.slick2d.input.events.MouseEventClicked;
import de.lessvoid.nifty.slick2d.input.events.MouseEventDragged;
import de.lessvoid.nifty.slick2d.input.events.MouseEventMoved;
import de.lessvoid.nifty.slick2d.input.events.MouseEventPressed;
import de.lessvoid.nifty.slick2d.input.events.MouseEventReleased;
import de.lessvoid.nifty.slick2d.input.events.MouseEventWheelMoved;
import de.lessvoid.nifty.tools.resourceloader.NiftyResourceLoader;

/**
 * This is the abstract Input System implementation to connect the Input of
 * Slick and Nifty.
 * 
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public abstract class AbstractSlickInputSystem extends InputAdapter implements SlickInputSystem {
  /**
   * The list of buttons that got pressed and are still pressed.
   */
  private final List<Integer> buttonPressedStack;

  /**
   * The input system that feeds this input system with data.
   */
  private Input input;

  /**
   * The list of input events that was registered but yet not processed.
   */
  private final List<InputEvent> inputEventList;

  /**
   * The input state used for the communication between the input events.
   */
  private final InputState inputState;

  /**
   * Prepare all required instances to work with this class.
   */
  protected AbstractSlickInputSystem() {
    super();
    inputEventList = new ArrayList<InputEvent>();
    buttonPressedStack = new LinkedList<Integer>();
    inputState = new InputState();
  }

  /**
   * This is called by Nifty and used to poll the input events. In case the
   * event is not handled by the NiftyGUI is send to the additional input event
   * handler that is implemented by the {@link #handleInputEvent(InputEvent)}
   * function.
   * 
   * @param inputEventConsumer
   *          the input event consumer that is provided by Nifty, it will
   *          receive all events first
   */
  @Override
  public final void forwardEvents(final NiftyInputConsumer inputEventConsumer) {
    InputEvent currentEvent;
    while (!inputEventList.isEmpty()) {
      currentEvent = inputEventList.remove(0);
      if (!currentEvent.executeEvent(inputState)) {
        continue;
      }
      
      if (currentEvent.sendToNifty(inputEventConsumer)) {
        currentEvent.updateState(inputState, true);
      } else {
        handleInputEvent(currentEvent);
        currentEvent.updateState(inputState, false);
      }
    }
  }

  /**
   * This function is called in case a input event was not handled by the Nifty
   * event consumer of the Nifty GUI.
   * 
   * @param event
   *          the event that needs to be handled
   */
  protected abstract void handleInputEvent(InputEvent event);

  /**
   * {@inheritDoc}
   */
  @Override
  public final void inputEnded() {
    // nothing to do once all input events were send
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void inputStarted() {
    // nothing to do when the input starts
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final boolean isAcceptingInput() {
    return true;
  }

  /**
   * Check if the control key is pressed down. This function requires the
   * {@link org.newdawn.slick.Input} instance that is used by Slick to be sent
   * in order to work properly. Setting this instance is done by calling
   * {@link #setInput(Input)}.
   * 
   * @return <code>true</code> in case one of the control keys is done and the
   *         {@link org.newdawn.slick.Input} instance was set properly, else
   *         <code>false</code>
   */
  private boolean isControlDown() {
      return input != null && (input.isKeyDown(Input.KEY_LCONTROL) || input.isKeyDown(Input.KEY_RCONTROL));
  }

  /**
   * Check if the shift key is pressed down. This function requires the
   * {@link org.newdawn.slick.Input} instance that is used by Slick to be sent
   * in order to work properly. Setting this instance is done by calling
   * {@link #setInput(Input)}.
   * 
   * @return <code>true</code> in case one of the shift keys is done and the
   *         {@link org.newdawn.slick.Input} instance was set properly, else
   *         <code>false</code>
   */
  private boolean isShiftDown() {
      return input != null && (input.isKeyDown(Input.KEY_LSHIFT) || input.isKeyDown(Input.KEY_RSHIFT));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void keyPressed(final int key, final char c) {
    inputEventList.add(new KeyboardEventPressed(key, c, isShiftDown(), isControlDown()));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void keyReleased(final int key, final char c) {
    inputEventList.add(new KeyboardEventReleased(key, c, isShiftDown(), isControlDown()));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void mouseClicked(final int button, final int x, final int y, final int clickCount) {
    inputEventList.add(new MouseEventClicked(x, y, button, clickCount));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void mouseDragged(final int oldX, final int oldY, final int newX, final int newY) {
    final int lastButton = buttonPressedStack.get(buttonPressedStack.size() - 1);
    inputEventList.add(new MouseEventDragged(lastButton, oldX, oldY, newX, newY));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void mouseMoved(final int oldX, final int oldY, final int newX, final int newY) {
    inputEventList.add(new MouseEventMoved(oldX, oldY, newX, newY));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void mousePressed(final int button, final int x, final int y) {
    inputEventList.add(new MouseEventPressed(x, y, button));
    buttonPressedStack.add(button);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void mouseReleased(final int button, final int x, final int y) {
    inputEventList.add(new MouseEventReleased(x, y, button));
    buttonPressedStack.remove(Integer.valueOf(button));
  }

  /**
   * {@inheritDoc}. This function requires the {@link org.newdawn.slick.Input}
   * instance that is used by Slick to be sent in order to work properly.
   * Setting this instance is done by calling {@link #setInput(Input)}.
   * 
   * @throws IllegalStateException
   *           in case the {@link org.newdawn.slick.Input} was not set
   */
  @Override
  public final void mouseWheelMoved(final int change) {
    if (input == null) {
      throw new IllegalStateException("Can't generate mouse wheel events without a reference to the Input");
    }
    inputEventList.add(new MouseEventWheelMoved(input.getMouseX(), input.getMouseY(), change / 120));
  }

  /**
   * Set the {@link org.newdawn.slick.Input} instance that is used by Slick to
   * handle the user input. Some functions of this class require this instance
   * to work properly. Calling this function will also make sure that the
   * {@link org.newdawn.slick.Input} instance is configured properly so the GUI
   * works as expected.
   * 
   * @param input
   *          the {@link org.newdawn.slick.Input} instance
   * @see #mouseWheelMoved(int)
   * @see #isControlDown()
   * @see #isShiftDown()
   * 
   */
  @Override
  public final void setInput(final Input input) {
    this.input = input;
    input.enableKeyRepeat();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void setMousePosition(final int x, final int y) {
    Mouse.setCursorPosition(x, y);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setResourceLoader(final NiftyResourceLoader resourceLoader) {
    // no use for the resource loader
  }
}
