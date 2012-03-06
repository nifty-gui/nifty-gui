package de.lessvoid.nifty.slick2d.input;

import de.lessvoid.nifty.slick2d.input.events.InputEvent;
import org.newdawn.slick.InputListener;

/**
 * This is the input system that forwards all events to a slick listener. Also this input system is so slick, writing
 * slick once is not even enough.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public final class SlickSlickInputSystem extends AbstractSlickInputSystem implements ForwardingInputSystem {
  /**
   * The input listener that will receive any events the NiftyGUI does not use.
   */
  private final InputListener listener;

  /**
   * Create the input system and set the listener that will receive any unused input events.
   *
   * @param targetListener the listener
   * @throws IllegalArgumentException in case the targetListener parameter is {@code null}
   */
  public SlickSlickInputSystem(final InputListener targetListener) {
    if (targetListener == null) {
      throw new IllegalArgumentException("The target listener must not be NULL.");
    }
    listener = targetListener;
  }

  /**
   * Forward the input event to the slick listener.
   */
  @Override
  protected void handleInputEvent(final InputEvent event) {
    event.sendToSlick(listener);
  }

  @Override
  public void requestExclusiveMouse() {
    enableForwardingMode(ForwardingMode.mouse);
  }

  @Override
  public void requestExclusiveKeyboard() {
    enableForwardingMode(ForwardingMode.keyboard);
  }

  @Override
  public void requestExclusiveInput() {
    enableForwardingMode(ForwardingMode.all);
  }

  @Override
  public void releaseExclusiveMouse() {
    disableForwardingMode(ForwardingMode.mouse);
  }

  @Override
  public void releaseExclusiveKeyboard() {
    disableForwardingMode(ForwardingMode.keyboard);
  }

  @Override
  public void releaseExclusiveInput() {
    disableForwardingMode(ForwardingMode.all);
  }
}
