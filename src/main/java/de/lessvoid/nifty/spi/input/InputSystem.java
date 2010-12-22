package de.lessvoid.nifty.spi.input;

import de.lessvoid.nifty.NiftyInputConsumer;

/**
 * Interface for Niftys InputSystem.
 * @author void
 */
public interface InputSystem {

  /**
   * This method is called by Nifty when it's ready to process input events. The
   * InputSystem implementation should call the methods on the given NiftyInputConsumer
   * to forward events to Nifty.
   *
   * @param inputEventConsumer the NiftyInputConsumer to forward input events to
   */
  void forwardEvents(NiftyInputConsumer inputEventConsumer);

  /**
   * This allows Nifty to set the position of the mouse to the given coordinate with
   * (0,0) being the upper left corner of the screen.
   * @param x x coordinate of mouse
   * @param y y coordinate of mouse
   */
  void setMousePosition(final int x, final int y);
}
