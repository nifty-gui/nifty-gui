package de.lessvoid.nifty.slick2d.input.events;

import de.lessvoid.nifty.NiftyInputConsumer;
import org.newdawn.slick.InputListener;

import javax.annotation.Nonnull;

/**
 * This mouse event is used to store the event generated in case the mouse cursor is moved.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public final class MouseEventMoved extends AbstractMouseEvent {
  /**
   * The X coordinate where the movement stopped.
   */
  private final int targetX;

  /**
   * The Y coordinate where the movement stopped.
   */
  private final int targetY;

  /**
   * Create a mouse moved event.
   *
   * @param startX the X coordinate of the location where the movement started
   * @param startY the Y coordinate of the location where the movement started
   * @param endX   the X coordinate of the location where the movement stopped
   * @param endY   the Y coordinate of the location where the movement stopped
   */
  public MouseEventMoved(final int startX, final int startY, final int endX, final int endY) {
    super(startX, startY);
    targetX = endX;
    targetY = endY;
  }

  /**
   * Send the event to the Nifty input consumer.
   */
  @Override
  public boolean sendToNifty(@Nonnull final NiftyInputConsumer consumer) {
    return consumer.processMouseEvent(targetX, targetY, 0, -1, false);
  }

  /**
   * Send the event to the Slick input listener.
   */
  @Override
  public boolean sendToSlick(@Nonnull final InputListener listener) {
    listener.mouseMoved(getX(), getY(), targetX, targetY);
    return false;
  }

  /**
   * Get the X coordinate where the movement stopped.
   *
   * @return the final mouse X coordinate
   */
  public final int getTargetX() {
    return targetX;
  }

  /**
   * Get the Y coordinate where the movement stopped.
   *
   * @return the final mouse Y coordinate
   */
  public final int getTargetY() {
    return targetY;
  }

}
