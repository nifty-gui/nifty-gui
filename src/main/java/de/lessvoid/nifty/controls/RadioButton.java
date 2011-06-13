package de.lessvoid.nifty.controls;

/**
 * The RadioButton interface is the Nifty control API view of a Nifty RadioButton control.
 * @author void
 */
public interface RadioButton extends NiftyControl {
  /**
   * Make this RadioButton a part of the group with te given groupId.
   * @param groupId
   */
  void setGroup(String groupId);

  /**
   * Return the GroupId of the group this RadioButton is a member of.
   * @return
   */
  String getGroup();

  /**
   * Activate this RadioButton. This will make all other RadioButtons of the
   * same group inactive.
   */
  void activate();

  /**
   * Returns the activated state of this RadioButton.
   * @return true when this RadioButton is active and false when not.
   */
  boolean isActivated();

  /**
   * Returns the id of the currently activated RadioButton that is in the same
   * group as this RadioButton.
   * @return the id of the current active radiobutton that this RadioButton is a part of
   */
  String getGroupActive();
}
