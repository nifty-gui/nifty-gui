package de.lessvoid.nifty.controls.checkbox;

import de.lessvoid.nifty.controls.NiftyControl;

/**
 * The methods that NiftyControl provides are implemented in the Control class but also
 * in the *Impl classes where we would only add empty methods.
 * @author void
 */
public abstract class EmptyNiftyControlImpl implements NiftyControl {

  public void enable() {
  }

  public void disable() {
  }

  public boolean isEnabled() {
    return false;
  }
}
