package de.lessvoid.nifty.slick2d.input.events;

import de.lessvoid.nifty.NiftyInputConsumer;
import de.lessvoid.nifty.slick2d.input.InputState;
import org.newdawn.slick.InputListener;

/**
 * This mouse event is used to store the event generated in case a mouse button is clicked.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public final class MouseEventClicked extends AbstractMouseEventButton {
  /**
   * The count how often the button got clicked.
   */
  private final int count;

  /**
   * Create a new mouse button clicked event.
   *
   * @param x the x coordinate of the event location
   * @param y the y coordinate of the event location
   * @param mouseButton the mouse button that was used
   * @param clickCount the count of times how often the button got clicked
   */
  public MouseEventClicked(final int x, final int y, final int mouseButton, final int clickCount) {
    super(x, y, mouseButton);
    count = clickCount;
  }

  /**
   * Reject this event in case the input state says so.
   */
  @Override
  public boolean executeEvent(final InputState state) {
    final boolean result = !state.isConsumeNextClick();
    state.setConsumeNextClick(false);
    return result;
  }

  /**
   * This would send the event to Nifty, how ever Nifty does not use such high-level events and so its never send to
   * Nifty.
   */
  @Override
  public boolean sendToNifty(final NiftyInputConsumer consumer) {
    return false;
  }

  /**
   * Send the event to a slick listener.
   */
  @Override
  public boolean sendToSlick(final InputListener listener) {
    listener.mouseClicked(getButton(), getX(), getY(), count);
    return true;
  }
}
