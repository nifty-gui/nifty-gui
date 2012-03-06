package de.lessvoid.nifty.slick2d.input.events;

import de.lessvoid.nifty.NiftyInputConsumer;
import de.lessvoid.nifty.slick2d.input.InputState;
import org.newdawn.slick.InputListener;

/**
 * This mouse event is used to store the event generated in case a mouse button is pressed down.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public final class MouseEventPressed extends AbstractMouseEventButton {
  /**
   * Create a new mouse button pressed event.
   *
   * @param x the x coordinate of the event location
   * @param y the y coordinate of the event location
   * @param mouseButton the mouse button that was used
   */
  public MouseEventPressed(final int x, final int y, final int mouseButton) {
    super(x, y, mouseButton);
  }

  /**
   * Send the event to a Nifty input event consumer.
   */
  @Override
  public boolean sendToNifty(final NiftyInputConsumer consumer) {
    return consumer.processMouseEvent(getX(), getY(), 0, getButton(), true);
  }

  /**
   * Send the event to a slick input event consumer.
   */
  @Override
  public boolean sendToSlick(final InputListener listener) {
    listener.mousePressed(getButton(), getX(), getY());
    return true;
  }

  /**
   * Tell the state to consume the next click event in case the click happened upon the GUI.
   */
  @Override
  public void updateState(final InputState state, final boolean handledByGUI) {
    state.setConsumeNextClick(handledByGUI);
  }
}
