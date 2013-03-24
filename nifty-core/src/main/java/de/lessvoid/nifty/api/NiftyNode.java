package de.lessvoid.nifty.api;

/**
 * The core element of the Nifty scene graph is a NiftyNode. It is created by the main Nifty instance and represents
 * a hierarchical structure of a Nifty GUI.
 *
 * @author void
 */
public interface NiftyNode {
  /**
   * Change the width constraint of this NiftyNode forcing it to a certain UnitValue.
   * @param value the UnitValue representing the new width
   */
  void setWidthConstraint(UnitValue value);

  /**
   * Change the height constraint of this NiftyNode forcing it to a certain UnitValue.
   * @param value the UnitValue representing the new height
   */
  void setHeightConstraint(UnitValue value);
}
