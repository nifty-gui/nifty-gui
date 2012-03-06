package de.lessvoid.nifty.slick2d.input;

import de.lessvoid.nifty.NiftyInputConsumer;
import de.lessvoid.nifty.slick2d.input.events.InputEvent;

/**
 * This is the input system that forwards all events to a Nifty input consumer.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public final class NiftySlickInputSystem extends AbstractSlickInputSystem implements ForwardingInputSystem {
  /**
   * The consumer that is supposed to receive any input events that are not used by the Nifty GUI.
   */
  private final NiftyInputConsumer consumer;

  /**
   * Create a input system that forwards all events to a Nifty-style input consumer.
   *
   * @param targetConsumer the consumer that is supposed to receive any unused input events
   * @throws IllegalArgumentException in case the targetConsumer parameter is {@code null}
   */
  public NiftySlickInputSystem(final NiftyInputConsumer targetConsumer) {
    if (targetConsumer == null) {
      throw new IllegalArgumentException("The target consumer must not be NULL.");
    }
    consumer = targetConsumer;
  }

  /**
   * Send the event to the defined consumer.
   */
  @Override
  protected void handleInputEvent(final InputEvent event) {
    event.sendToNifty(consumer);
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
