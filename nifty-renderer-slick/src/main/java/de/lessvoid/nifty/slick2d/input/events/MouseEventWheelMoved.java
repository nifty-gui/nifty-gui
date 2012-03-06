package de.lessvoid.nifty.slick2d.input.events;

import de.lessvoid.nifty.NiftyInputConsumer;
import org.newdawn.slick.InputListener;

/**
 * This mouse event is used to store the event generated in case the mouse wheel got moved.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public final class MouseEventWheelMoved extends AbstractMouseEvent {
  /**
   * The delta value that defines how much and into what direction the mouse wheel got moved.
   */
  private final int wheelDelta;

  /**
   * Create a new mouse wheel event and store the delta value.
   *
   * @param x the x coordinate
   * @param y the y coordinate
   * @param delta the delta of the mouse wheel movement
   */
  public MouseEventWheelMoved(final int x, final int y, final int delta) {
    super(x, y);
    wheelDelta = delta;
  }

  /**
   * Send the event to a Nifty input consumer.
   */
  @Override
  public boolean sendToNifty(final NiftyInputConsumer consumer) {
    return consumer.processMouseEvent(getX(), getY(), wheelDelta, -1, false);
  }

  /**
   * Send the event to a Slick input listener.
   */
  @Override
  public boolean sendToSlick(final InputListener listener) {
    listener.mouseWheelMoved(wheelDelta);
    return true;
  }

}
