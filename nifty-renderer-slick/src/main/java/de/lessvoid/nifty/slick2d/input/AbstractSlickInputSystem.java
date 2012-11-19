package de.lessvoid.nifty.slick2d.input;

import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.lessvoid.nifty.NiftyInputConsumer;
import de.lessvoid.nifty.slick2d.input.events.*;
import de.lessvoid.nifty.tools.resourceloader.NiftyResourceLoader;
import org.newdawn.slick.Input;
import org.newdawn.slick.util.InputAdapter;

/**
 * This is the abstract Input System implementation to connect the Input of Slick and Nifty.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
@SuppressWarnings("AbstractClassExtendsConcreteClass")
public abstract class AbstractSlickInputSystem extends InputAdapter implements SlickInputSystem {
  /**
   * This constants holds the initial size of the storage list that holds the received input events.
   */
  private static final int INPUT_EVENT_BUFFER_INIT_SIZE = 20;

  /**
   * The delta value is divided by this value in order to give Nifty nice values for the mouse wheel movement. Reducing
   * this value will increase the speed the mouse wheel moves for Nifty.
   */
  private static final int WHEEL_DELTA_CORRECTION = 120;

  /**
   * The list of buttons that got pressed and are still pressed.
   */
  private final List<Integer> buttonPressedStack;

  /**
   * The input system that feeds this input system with data.
   */
  private Input input = null;

  /**
   * The list of input events that was registered but yet not processed.
   */
  private final List<InputEvent> inputEventList;

  /**
   * The input state used for the communication between the input events.
   */
  private final InputState inputState;

  /**
   * The currently active forwarding mode.
   */
  private ForwardingMode forwardMode;

  /**
   * Prepare all required instances to work with this class.
   */
  protected AbstractSlickInputSystem() {
    inputEventList = new ArrayList<InputEvent>(INPUT_EVENT_BUFFER_INIT_SIZE);
    buttonPressedStack = new LinkedList<Integer>();
    inputState = new InputState();
    forwardMode = ForwardingMode.none;
  }

  /**
   * This is called by Nifty and used to poll the input events. In case the event is not handled by the NiftyGUI is send
   * to the additional input event handler that is implemented by the {@link #handleInputEvent(InputEvent)} function.
   *
   * @param inputEventConsumer the input event consumer that is provided by Nifty, it will receive all events first
   */
  @Override
  public final void forwardEvents(final NiftyInputConsumer inputEventConsumer) {
    while (!inputEventList.isEmpty()) {
      final InputEvent currentEvent = inputEventList.remove(0);
      if (!currentEvent.executeEvent(inputState)) {
        continue;
      }

      final boolean isForwarded = currentEvent.isForwarded(forwardMode);
      if (!isForwarded && currentEvent.sendToNifty(inputEventConsumer)) {
        currentEvent.updateState(inputState, true);
      } else {
        handleInputEvent(currentEvent);

        // Forwarding might just have been enabled again, if that is the case Nifty needs to know about the event
        if (isForwarded && !currentEvent.isForwarded(forwardMode)) {
          currentEvent.sendToNifty(inputEventConsumer);
          currentEvent.updateState(inputState, true);
        } else {
          currentEvent.updateState(inputState, false);
        }
      }
    }
  }

  /**
   * This function is called in case a input event was not handled by the Nifty event consumer of the Nifty GUI.
   *
   * @param event the event that needs to be handled
   */
  protected abstract void handleInputEvent(InputEvent event);

  /**
   * Check if the control key is pressed down. This function requires the {@link Input} instance that is used by Slick
   * to be sent in order to work properly. Setting this instance is done by calling {@link #setInput(Input)}.
   *
   * @return {@code true} in case one of the control keys is done and the {@link Input} instance was set properly, else
   *         {@code false}
   */
  private boolean isControlDown() {
    return (input != null) && (input.isKeyDown(Input.KEY_LCONTROL) || input.isKeyDown(Input.KEY_RCONTROL));
  }

  /**
   * Check if the shift key is pressed down. This function requires the {@link Input} instance that is used by Slick to
   * be sent in order to work properly. Setting this instance is done by calling {@link #setInput(Input)}.
   *
   * @return {@code true} in case one of the shift keys is done and the {@link Input} instance was set properly, else
   *         {@code false}
   */
  private boolean isShiftDown() {
    return (input != null) && (input.isKeyDown(Input.KEY_LSHIFT) || input.isKeyDown(Input.KEY_RSHIFT));
  }

  @Override
  public final void keyPressed(final int key, final char c) {
    inputEventList.add(new KeyboardEventPressed(key, c, isShiftDown(), isControlDown()));
  }

  @Override
  public final void keyReleased(final int key, final char c) {
    inputEventList.add(new KeyboardEventReleased(key, c, isShiftDown(), isControlDown()));
  }

  @Override
  public final void mouseClicked(final int button, final int x, final int y, final int clickCount) {
    inputEventList.add(new MouseEventClicked(x, y, button, clickCount));
  }

  @Override
  public final void mouseDragged(final int oldX, final int oldY, final int newX, final int newY) {
    if (buttonPressedStack.isEmpty()) {
      // drag started outside of the screen. Can't handle this one.
      return;
    }
    final int lastButton = buttonPressedStack.get(buttonPressedStack.size() - 1);
    inputEventList.add(new MouseEventDragged(lastButton, oldX, oldY, newX, newY));
  }

  @Override
  public final void mouseMoved(final int oldX, final int oldY, final int newX, final int newY) {
    inputEventList.add(new MouseEventMoved(oldX, oldY, newX, newY));
  }

  @Override
  public final void mousePressed(final int button, final int x, final int y) {
    inputEventList.add(new MouseEventPressed(x, y, button));
    buttonPressedStack.add(button);
  }

  @Override
  public final void mouseReleased(final int button, final int x, final int y) {
    inputEventList.add(new MouseEventReleased(x, y, button));
    buttonPressedStack.remove(Integer.valueOf(button));
  }

  /**
   * {@inheritDoc}. This function requires the {@link Input} instance that is used by Slick to be sent in order to work
   * properly. Setting this instance is done by calling {@link #setInput(Input)}.
   *
   * @throws IllegalStateException in case the {@link Input} was not set
   */
  @Override
  public final void mouseWheelMoved(final int change) {
    if (input == null) {
      throw new IllegalStateException("Can't generate mouse wheel events without a reference to the Input");
    }
    inputEventList.add(new MouseEventWheelMoved(input.getMouseX(), input.getMouseY(), change / WHEEL_DELTA_CORRECTION));
  }

  /**
   * Enable the forwarding modes. This causes that some event are not send to the Nifty-GUI. Instead they are always
   * forwarded to the second listener.
   *
   * @param mode the forwarding mode to enable
   */
  protected final void enableForwardingMode(final ForwardingMode mode) {
    if ((forwardMode == ForwardingMode.all) || (mode == ForwardingMode.none) || (mode == forwardMode)) {
      return;
    }

    if (mode == ForwardingMode.all) {
      forwardMode = ForwardingMode.all;
    }

    forwardMode = (forwardMode == ForwardingMode.none) ? mode : ForwardingMode.all;
  }

  /**
   * Disable the forwarding mode. Once called the specified events are not send to the Nifty-GUI anymore.
   *
   * @param mode the forwarding mode that is supposed to be disabled
   */
  protected final void disableForwardingMode(final ForwardingMode mode) {
    if ((forwardMode == ForwardingMode.none) || (mode == ForwardingMode.none)) {
      return;
    }

    if ((forwardMode == mode) || (mode == ForwardingMode.all)) {
      forwardMode = ForwardingMode.none;
      return;
    }

    if (forwardMode == ForwardingMode.all) {
      forwardMode = (mode == ForwardingMode.mouse) ? ForwardingMode.keyboard : ForwardingMode.mouse;
    }
  }

  /**
   * Set the {@link Input} instance that is used by Slick to handle the user input. Some functions of this class require
   * this instance to work properly. Calling this function will also make sure that the {@link Input} instance is
   * configured properly so the GUI works as expected.
   *
   * @param newInput the {@link Input} instance
   * @see #mouseWheelMoved(int)
   * @see #isControlDown()
   * @see #isShiftDown()
   */
  @Override
  public final void setInput(final Input newInput) {
    input = newInput;
    input.enableKeyRepeat();
  }

  @Override
  public final void setMousePosition(final int x, final int y) {
    Mouse.setCursorPosition(x, y);
  }

  @Override
  public void setResourceLoader(final NiftyResourceLoader resourceLoader) {
    // no use for the resource loader
  }
}
