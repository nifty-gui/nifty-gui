package de.lessvoid.nifty.controls.dropdown;

import java.util.List;

import org.bushe.swing.event.EventTopicSubscriber;

import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.DropDown;
import de.lessvoid.nifty.controls.DropDownSelectionChangedEvent;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.ListBoxSelectionChangedEvent;
import de.lessvoid.nifty.controls.listbox.ListBoxControl;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;

@SuppressWarnings("rawtypes")
public class DropDownListBoxSelectionChangedEventSubscriber implements EventTopicSubscriber<ListBoxSelectionChangedEvent> {
  private Nifty nifty;
  private Screen screen;
  private ListBox listBox;
  private DropDown dropDown;
  private Element popupInstance;

  public DropDownListBoxSelectionChangedEventSubscriber(final Nifty nifty, final Screen screen, final ListBox listBox, final DropDown dropDown, final Element popupInstance) {
    this.nifty = nifty;
    this.screen = screen;
    this.listBox = listBox;
    this.dropDown = dropDown;
    this.popupInstance = popupInstance;
  }

  @SuppressWarnings("unchecked")
  @Override
  public void onEvent(final String topic, final ListBoxSelectionChangedEvent data) {
    final Object selectedItem = getSelectedItem(data.getSelection());

    ListBoxControl listBoxControl = (ListBoxControl) listBox;
    listBoxControl.getViewConverter().display(dropDown.getElement().findElementByName("#text"), selectedItem);

    final int selectedItemIndex = getSelectedIndex(data);
    if (screen.isActivePopup(popupInstance)) {
      dropDown.getElement().getControl(DropDownControl.class).close(new EndNotify() {
        @Override
        public void perform() {
          nifty.publishEvent(dropDown.getId(), new DropDownSelectionChangedEvent(dropDown, selectedItem, selectedItemIndex));
        }
      });
    } else {
      nifty.publishEvent(dropDown.getId(), new DropDownSelectionChangedEvent(dropDown, selectedItem, selectedItemIndex));
    }
  }

  private int getSelectedIndex(final ListBoxSelectionChangedEvent data) {
    int selectedItemIndex = -1;
    List<Integer> selectionIndices = data.getSelectionIndices();
    if (!selectionIndices.isEmpty()) {
      selectedItemIndex = selectionIndices.get(0);
    }
    return selectedItemIndex;
  }

  private Object getSelectedItem(final List selection) {
    if (selection.isEmpty()) {
      return null;
    }
    return selection.get(0);
  }
}
