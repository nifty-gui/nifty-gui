package de.lessvoid.nifty.controls;

/**
 * The CheckBox interface is the Nifty control API view of a Nifty CheckBox control.
 *
 * @author void
 */
public interface CheckBox extends NiftyControl {
  /**
   * Check this CheckBox.
   */
  void check();

  /**
   * Uncheck this CheckBox.
   */
  void uncheck();

  /**
   * Set the state of this CheckBox to the given state.
   *
   * @param state true = checked, false = unchecked
   */
  void setChecked(boolean state);

  /**
   * Returns the current State of this CheckBox.
   */
  boolean isChecked();

  /**
   * Toggle this CheckBox state.
   */
  void toggle();
}
