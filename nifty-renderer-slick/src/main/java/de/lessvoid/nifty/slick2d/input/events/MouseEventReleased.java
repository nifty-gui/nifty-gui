package de.lessvoid.nifty.slick2d.input.events;

import de.lessvoid.nifty.NiftyInputConsumer;
import org.newdawn.slick.InputListener;

/**
 * This mouse event is used to store the event generated in case a mouse button is released.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public final class MouseEventReleased extends AbstractMouseEventButton {
  /**
   * Create a new mouse button release event.
   *
   * @param x the x coordinate of the event location
   * @param y the y coordinate of the event location
   * @param mouseButton the mouse button that was used
   */
  public MouseEventReleased(final int x, final int y, final int mouseButton) {
    super(x, y, mouseButton);
  }

  /**
   * Send the event to a Nifty input event consumer.
   */
  @Override
  public boolean sendToNifty(final NiftyInputConsumer consumer) {
    return consumer.processMouseEvent(getX(), getY(), 0, getButton(), false);
  }

  /**
   * Send the event to a slick input event consumer.
   */
  @Override
  public boolean sendToSlick(final InputListener listener) {
    listener.mouseReleased(getButton(), getX(), getY());
    return true;
  }

}
