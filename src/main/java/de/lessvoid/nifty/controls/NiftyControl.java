package de.lessvoid.nifty.controls;


/**
 * Interface for all Nifty Controls.
 * @author void
 */
public interface NiftyControl {
  /**
   * Enable the control.
   */
  void enable();

  /**
   * Disable the control.
   */
  void disable();

  /**
   * Get the current enabled state of the control.
   * @return
   */
  boolean isEnabled();
}
