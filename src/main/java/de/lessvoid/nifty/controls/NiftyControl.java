package de.lessvoid.nifty.controls;

import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.tools.SizeValue;

/**
 * Interface for all Nifty Controls.
 * @author void
 */
public interface NiftyControl {

  /**
   * Get the attached internal Nifty element for this Nifty control.
   * @return the element
   */
  Element getElement();

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

  /**
   * Set the focus to this control.
   */
  void setFocus();

  /**
   * Change if this control is focusable (if it can get the focus or not). Usually controls are set up
   * with focusable="true" but you can change this here if necessary. 
   * @param focusable true when this element can get the focus and false when not
   */
  void setFocusable(boolean focusable);

  /**
   * Returns true if this control has the focus.
   * @return true, when the control has the focus and false if not
   */
  boolean hasFocus();

  /**
   * This method is called after the element this control is attached too has been layouted. This
   * callback allows the control to update any layout related things if necessary. 
   */
  void layoutCallback();

  /**
   * Returns true when this NiftyControl is already bound (e.g. its Controllers bind() method has
   * been called). 
   * @return true if bound and false if not
   */
  boolean isBound();
}
