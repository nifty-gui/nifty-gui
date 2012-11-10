package de.lessvoid.nifty.controls.treebox;

import org.bushe.swing.event.EventTopicSubscriber;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.TreeItem;
import de.lessvoid.nifty.controls.listbox.ListBoxItemController;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.events.NiftyMousePrimaryClickedEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;

public class TreeBoxItemController<T> extends ListBoxItemController<TreeItem<T>> {
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

  public void expandButtonClicked() {
    final TreeItem<T> item = getItem();
    if (item.isLeaf()) {
      return;
    }
    item.setExpanded(!item.isExpanded());
    parentControl.updateList(item);
  }
}
