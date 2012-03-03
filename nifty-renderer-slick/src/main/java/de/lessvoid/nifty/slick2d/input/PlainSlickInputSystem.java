package de.lessvoid.nifty.slick2d.input;

import de.lessvoid.nifty.slick2d.input.events.InputEvent;

/**
 * This version of the input system is only usable in case all input is meant to be used in the Nifty GUI. All input
 * events Nifty does not use will be discarded.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public final class PlainSlickInputSystem extends AbstractSlickInputSystem {
  /**
   * All input events not handled by the Nifty GUI end up here and get discarded.
   */
  @Override
  protected void handleInputEvent(final InputEvent event) {
    // discard the event
  }
}
