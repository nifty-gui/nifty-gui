package de.lessvoid.nifty.api.controls;

import de.lessvoid.nifty.api.NiftyNode;
import de.lessvoid.nifty.api.UnitValue;

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
  public int getWidth() {
    return niftyNode.getWidth();
  }

  @Override
  public void setWidth(final UnitValue width) {
    niftyNode.setWidthConstraint(width);
  }

  @Override
  public int getHeight() {
    return niftyNode.getHeight();
  }

  @Override
  public void setHeight(final UnitValue height) {
    niftyNode.setHeightConstraint(height);
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
