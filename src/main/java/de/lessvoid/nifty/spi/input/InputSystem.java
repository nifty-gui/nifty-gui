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
}
