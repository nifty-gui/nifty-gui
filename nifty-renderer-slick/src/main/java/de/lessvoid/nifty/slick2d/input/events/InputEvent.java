package de.lessvoid.nifty.slick2d.input.events;

import de.lessvoid.nifty.NiftyInputConsumer;
import de.lessvoid.nifty.slick2d.input.ForwardingMode;
import de.lessvoid.nifty.slick2d.input.InputState;
import org.newdawn.slick.InputListener;

public interface InputEvent {
  /**
   * Check if the input event is supposed to be executed.
   *
   * @param state the input event state
   * @return {@code true} in case this event is supposed to be executed
   */
  boolean executeEvent(InputState state);

  /**
   * Check if this input event is forwarded.
   *
   * @param mode the current forwarding mode
   * @return {@code true} in case the event should be forwarded
   */
  boolean isForwarded(ForwardingMode mode);

  /**
   * Send the event to a nifty input consumer.
   *
   * @param consumer the consumer the event needs to be send to
   * @return {@code true} in case the event was handled by the consumer and must not be forwarded to any other event
   *         handlers
   */
  boolean sendToNifty(NiftyInputConsumer consumer);

  /**
   * Send the event to a slick input consumer.
   *
   * @param listener the input listener to receive this event
   * @return {@code true} in case the event was handled by the consumer and must not be forwarded to any other event
   *         handlers
   */
  boolean sendToSlick(InputListener listener);

  /**
   * Update the input event state.
   *
   * @param state the input event state to update
   * @param handledByGUI {@code true} if this event was handled by the GUI
   */
  void updateState(InputState state, boolean handledByGUI);
}
