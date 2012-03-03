package de.lessvoid.nifty.slick2d;

import de.lessvoid.nifty.slick2d.input.ForwardingInputSystem;

/**
 * This interface defines the methods available in the game implementations to control how input events are send to the
 * Nifty-GUI or to the underlying game.
 */
public interface NiftyInputForwarding {
  /**
   * Get the control class that enables changing the way how the input events are send to the Nifty-GUI and to the
   * underlying game.
   *
   * @return the forwarding control instance
   */
  ForwardingInputSystem getInputForwardingControl();
  /**
   * Check if input forwarding is supported.
   *
   * @return {@code true} in case input forwarding is supported
   */
  boolean isInputForwardingSupported();
}
