package de.lessvoid.nifty.controls;


/**
 * The RadioButtonGroup interface is the Nifty control API view of a Nifty RadioButtonGroup control.
 *
 * @author void
 */
public interface RadioButtonGroup extends NiftyControl {
  /**
   * When set to true this allows the RadioButtonGroup to have no RadioButton
   * that is selected.
   *
   * @param allowDeselection true - allow deselection and false - forces at least one RadioButton
   *                         to be selected
   */
  void allowDeselection(final boolean allowDeselection);
}
