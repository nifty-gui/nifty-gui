package de.lessvoid.nifty.controls.dropdown;

import java.util.List;

import org.bushe.swing.event.EventTopicSubscriber;

import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.DropDown;
import de.lessvoid.nifty.controls.DropDownSelectionChangedEvent;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.ListBoxSelectionChangedEvent;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;

public class DropDownListBoxSelectionChangedEventSubscriber<T> implements EventTopicSubscriber<ListBoxSelectionChangedEvent<T>> {
  private Nifty nifty;
  private Screen screen;
  private ListBox<T> listBox;
  private DropDown<T> dropDown;
  private Element popupInstance;

  public DropDownListBoxSelectionChangedEventSubscriber(final Nifty nifty, final Screen screen, final ListBox<T> listBox, final DropDown<T> dropDown, final Element popupInstance) {
    this.nifty = nifty;
    this.screen = screen;
    this.listBox = listBox;
    this.dropDown = dropDown;
    this.popupInstance = popupInstance;
  }

  @Override
  public void onEvent(final String topic, final ListBoxSelectionChangedEvent<T> data) {
    final T selectedItem = getSelectedItem(data.getSelection());

    de.lessvoid.nifty.controls.listbox.ListBoxControl<T> listBoxControl = (de.lessvoid.nifty.controls.listbox.ListBoxControl<T>) listBox;
    listBoxControl.getViewConverter().display(dropDown.getElement().findElementByName("#text"), selectedItem);

    final int selectedItemIndex = getSelectedIndex(data);
    if (screen.isActivePopup(popupInstance)) {
      dropDown.getElement().getControl(DropDownControl.class).close(new EndNotify() {
        @Override
        public void perform() {
          nifty.publishEvent(dropDown.getId(), new DropDownSelectionChangedEvent<T>(dropDown, selectedItem, selectedItemIndex));
        }
      });
    } else {
      nifty.publishEvent(dropDown.getId(), new DropDownSelectionChangedEvent<T>(dropDown, selectedItem, selectedItemIndex));
    }
  }

  private int getSelectedIndex(final ListBoxSelectionChangedEvent<T> data) {
    int selectedItemIndex = -1;
    List<Integer> selectionIndices = data.getSelectionIndices();
    if (!selectionIndices.isEmpty()) {
      selectedItemIndex = selectionIndices.get(0);
    }
    return selectedItemIndex;
  }

  private T getSelectedItem(final List<T> selection) {
    if (selection.isEmpty()) {
      return null;
    }
    return selection.get(0);
  }
}
