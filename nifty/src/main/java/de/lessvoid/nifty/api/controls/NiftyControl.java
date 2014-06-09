package de.lessvoid.nifty.api.controls;

import de.lessvoid.nifty.api.NiftyNode;
import de.lessvoid.nifty.api.UnitValue;

/**
 * Interface for all Nifty Controls.
 *
 * @author void
 */
public interface NiftyControl {

  /**
   * Initialize this NiftyControl with the given NiftyNode.
   * @param node NiftyNode
   */
  void init(NiftyNode node);

  /**
   * Get the NiftyNode for this Nifty control.
   *
   * @return the NiftyNode
   */
  NiftyNode getNode();

  /**
   * Get width as px value.
   *
   * @return width
   */
  int getWidth();

  /**
   * Set width as UnitValue.
   *
   * @param width width
   */
  void setWidth(UnitValue width);

  /**
   * Get height as px value.
   *
   * @return height
   */
  int getHeight();

  /**
   * Set Height as UnitValue.
   *
   * @param height new height
   */
  void setHeight(UnitValue height);

  /**
   * Enable the control.
   */
  void enable();

  /**
   * Disable the control.
   */
  void disable();

  /**
   * Set the enabled state from the given boolean.
   *
   * @param enabled the new enabled state
   */
  void setEnabled(boolean enabled);

  /**
   * Get the current enabled state of the control.
   *
   * @return {@code true} in case the element is enabled
   */
  boolean isEnabled();

  /**
   * Set the focus to this control.
   */
  void setFocus();

  /**
   * Change if this control is focusable (if it can get the focus or not). Usually controls are set up
   * with focusable="true" but you can change this here if necessary.
   *
   * @param focusable true when this element can get the focus and false when not
   */
  void setFocusable(boolean focusable);

  /**
   * Returns true if this control has the focus.
   *
   * @return true, when the control has the focus and false if not
   */
  boolean hasFocus();
}
