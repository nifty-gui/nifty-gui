package de.lessvoid.nifty.slick2d.input.events;

import org.newdawn.slick.InputListener;

import de.lessvoid.nifty.NiftyInputConsumer;
import de.lessvoid.nifty.slick2d.input.InputState;

public interface InputEvent {
  /**
   * Check if the input event is supposed to be executed.
   * 
   * @param state
   *          the input event state
   * @return <code>true</code> in case this event is supposed to be executed
   */
  boolean executeEvent(InputState state);

  /**
   * Send the event to a nifty input consumer.
   * 
   * @param consumer
   *          the consumer the event needs to be send to
   * @return <code>true</code> in case the event was handled by the consumer and
   *         must not be forwarded to any other event handlers
   */
  boolean sendToNifty(final NiftyInputConsumer consumer);

  /**
   * Send the event to a slick input consumer.
   * 
   * @param listener
   *          the input listener to receive this event
   * @return <code>true</code> in case the event was handled by the consumer and
   *         must not be forwarded to any other event handlers
   */
  boolean sendToSlick(final InputListener listener);

  /**
   * Update the input event state.
   * 
   * @param state
   *          the input event state to update
   * @param handledByGUI
   *          <code>true</code> if this event was handled by the GUI
   */
  void updateState(InputState state, boolean handledByGUI);
}
