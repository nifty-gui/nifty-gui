package de.lessvoid.nifty.controls.treebox;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.TreeItem;
import de.lessvoid.nifty.controls.listbox.ListBoxItemController;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;

/**
 * This is the default controller for the items of the {@link de.lessvoid.nifty.controls.TreeBox}. It takes care for
 * forwarding the clicks to the expand button the {@link de.lessvoid.nifty.controls.TreeBox} in order to update the
 * display of the tree.
 *
 * @param <T> the type of the object displayed in the tree
 */
public class TreeBoxItemController<T> extends ListBoxItemController<TreeItem<T>> {
  /**
   * The control that is parent to this tree item.
   */
  @SuppressWarnings("deprecation")
  private TreeBoxControl<T> parentControl;

  @Override
  public void bind(
      final Nifty niftyParam,
      final Screen screenParam,
      final Element newElement,
      final Properties properties,
      final Attributes controlDefinitionAttributes) {
    super.bind(niftyParam, screenParam, newElement, properties, controlDefinitionAttributes);
  }

  @SuppressWarnings("deprecation")
  void setParentControl(final TreeBoxControl<T> control) {
    parentControl = control;
  }

  /**
   * This function is called in case the expand component of the tree entry is clicked. The event will be forwarded
   * to the parent control in order to update the tree.
   */
  public void expandButtonClicked() {
    final TreeItem<T> item = getItem();
    if (item.isLeaf()) {
      return;
    }
    item.setExpanded(!item.isExpanded());
    parentControl.updateList(item);
  }
}
