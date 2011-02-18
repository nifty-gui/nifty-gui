package de.lessvoid.nifty.controls;

import de.lessvoid.nifty.tools.SizeValue;

/**
 * Interface for all Nifty Controls.
 * @author void
 */
public interface NiftyControl {

  /**
   * Get the id.
   * @return id
   */
  String getId();

  /**
   * Set the id.
   * @param id new id
   */
  void setId(String id);

  /**
   * Get width as SizeValue.
   * @return width
   */
  int getWidth();

  /**
   * Set width as SizeValue.
   * @param width width
   */
  void setWidth(SizeValue width);

  /**
   * Get height as SizeValue.
   * @return height
   */
  int getHeight();

  /**
   * Set Height as SizeValue.
   * @param height new height
   */
  void setHeight(SizeValue height);

  /**
   * Get current style.
   * @return current style
   */
  String getStyle();

  /**
   * Set style. Please note that currently not all style attributes are processed.
   * @param style new style to apply
   */
  void setStyle(String style);

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
   * @param enabled the new enabled state
   */
  void setEnabled(boolean enabled);

  /**
   * Get the current enabled state of the control.
   * @return
   */
  boolean isEnabled();
}
