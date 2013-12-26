package de.lessvoid.nifty.controls.dropdown;

import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.DropDown;
import de.lessvoid.nifty.controls.DropDownSelectionChangedEvent;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.ListBoxSelectionChangedEvent;
import de.lessvoid.nifty.controls.listbox.ListBoxControl;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import org.bushe.swing.event.EventTopicSubscriber;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@SuppressWarnings("rawtypes")
public class DropDownListBoxSelectionChangedEventSubscriber implements
    EventTopicSubscriber<ListBoxSelectionChangedEvent> {
  @Nonnull
  private final Nifty nifty;
  @Nonnull
  private final Screen screen;
  @Nonnull
  private final ListBox listBox;
  @Nonnull
  private final DropDown dropDown;
  @Nonnull
  private final Element popupInstance;

  public DropDownListBoxSelectionChangedEventSubscriber(
      @Nonnull final Nifty nifty,
      @Nonnull final Screen screen,
      @Nonnull final ListBox listBox,
      @Nonnull final DropDown dropDown,
      @Nonnull final Element popupInstance) {
    this.nifty = nifty;
    this.screen = screen;
    this.listBox = listBox;
    this.dropDown = dropDown;
    this.popupInstance = popupInstance;
  }

  @SuppressWarnings({ "deprecation", "unchecked" })
  @Override
  public void onEvent(final String topic, @Nonnull final ListBoxSelectionChangedEvent data) {
    final Object selectedItem = getSelectedItem(data.getSelection());
    if (selectedItem == null) {
      return;
    }

    ListBoxControl listBoxControl = (ListBoxControl) listBox;
    Element dropDownElement = dropDown.getElement();
    if (dropDownElement != null) {
      ListBox.ListBoxViewConverter converter = listBoxControl.getViewConverter();
      if (converter != null) {
        Element dropDownTextDisplay = dropDownElement.findElementById("#text");
        if (dropDownTextDisplay != null) {
          converter.display(dropDownTextDisplay, selectedItem);
        }
      }

      final int selectedItemIndex = getSelectedIndex(data);
      final String id = dropDown.getId();
      if (id != null) {
        if (screen.isActivePopup(popupInstance)) {
          DropDownControl control = dropDownElement.getControl(DropDownControl.class);
          if (control != null) {
            control.close(new EndNotify() {
              @Override
              public void perform() {
                nifty.publishEvent(id, new DropDownSelectionChangedEvent(dropDown, selectedItem, selectedItemIndex));
              }
            });
          }
        } else {
          nifty.publishEvent(id, new DropDownSelectionChangedEvent(dropDown, selectedItem, selectedItemIndex));
        }
      }
    }
  }

  private int getSelectedIndex(@Nonnull final ListBoxSelectionChangedEvent<?> data) {
    int selectedItemIndex = -1;
    List<Integer> selectionIndices = data.getSelectionIndices();
    if (!selectionIndices.isEmpty()) {
      selectedItemIndex = selectionIndices.get(0);
    }
    return selectedItemIndex;
  }

  @Nullable
  private Object getSelectedItem(@Nonnull final List selection) {
    if (selection.isEmpty()) {
      return null;
    }
    return selection.get(0);
  }
}
