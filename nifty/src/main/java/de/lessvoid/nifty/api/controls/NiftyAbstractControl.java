package de.lessvoid.nifty.api.controls;

import de.lessvoid.nifty.api.NiftyNode;

/**
 * This abstract class already implements a couple of NiftyControl method. Use this class as the base class for
 * custom controls.
 *
 * @author void
 */
public abstract class NiftyAbstractControl implements NiftyControl {
  protected NiftyNode niftyNode;

  public void init(final NiftyNode niftyNode) {
    this.niftyNode = niftyNode;
  }

  @Override
  public NiftyNode getNode() {
    return niftyNode;
  }

  @Override
  public void enable() {
    // TODO
  }

  @Override
  public void disable() {
    // TODO
  }

  @Override
  public void setEnabled(final boolean enabled) {
    // TODO
  }

  @Override
  public boolean isEnabled() {
    // TODO
    return false;
  }

  @Override
  public void setFocus() {
    // TODO
  }

  @Override
  public void setFocusable(boolean focusable) {
    // TODO
  }

  @Override
  public boolean hasFocus() {
    // TODO
    return false;
  }
}
