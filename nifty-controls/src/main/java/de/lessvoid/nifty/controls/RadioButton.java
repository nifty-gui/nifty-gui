package de.lessvoid.nifty.controls;

import javax.annotation.Nullable;

/**
 * The RadioButton interface is the Nifty control API view of a Nifty RadioButton control.
 *
 * @author void
 */
public interface RadioButton extends NiftyControl {
  /**
   * Make this RadioButton a part of the group with the given groupId.
   */
  void setGroup(@Nullable String groupId);

  /**
   * Return the GroupId of the group this RadioButton is a member of.
   */
  @Nullable
  RadioButtonGroup getGroup();

  /**
   * Select this RadioButton. This will make all other RadioButtons of the
   * same group inactive.
   */
  void select();

  /**
   * Returns the activated state of this RadioButton.
   *
   * @return true when this RadioButton is active and false when not.
   */
  boolean isActivated();
}
