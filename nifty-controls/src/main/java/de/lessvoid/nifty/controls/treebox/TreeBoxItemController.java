package de.lessvoid.nifty.controls.treebox;

import de.lessvoid.nifty.controls.TreeItem;
import de.lessvoid.nifty.controls.listbox.ListBoxItemController;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.logging.Logger;

/**
 * This is the default controller for the items of the {@link de.lessvoid.nifty.controls.TreeBox}. It takes care for
 * forwarding the clicks to the expand button the {@link de.lessvoid.nifty.controls.TreeBox} in order to update the
 * display of the tree.
 *
 * @param <T> the type of the object displayed in the tree
 */
public class TreeBoxItemController<T> extends ListBoxItemController<TreeItem<T>> {
  @Nonnull
  private static final Logger log = Logger.getLogger(TreeBoxItemController.class.getName());
  /**
   * The control that is parent to this tree item.
   */
  @SuppressWarnings("deprecation")
  @Nullable
  private TreeBoxControl<T> parentControl;

  @SuppressWarnings({ "deprecation", "NullableProblems" })
  void setParentControl(@Nonnull final TreeBoxControl<T> control) {
    parentControl = control;
  }

  /**
   * This function is called in case the expand component of the tree entry is clicked. The event will be forwarded
   * to the parent control in order to update the tree.
   */
  public void expandButtonClicked() {
    if (parentControl == null) {
      log.warning("Can't handle click to expend button as long as the parent control is not applied.");
      return;
    }
    final TreeItem<T> item = getItem();
    if (item == null || item.isLeaf()) {
      return;
    }
    item.setExpanded(!item.isExpanded());
    parentControl.updateList(item);
  }
}
