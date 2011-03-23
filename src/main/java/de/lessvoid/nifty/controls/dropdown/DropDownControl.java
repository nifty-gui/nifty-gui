package de.lessvoid.nifty.controls.dropdown;

import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import org.bushe.swing.event.EventTopicSubscriber;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.AbstractController;
import de.lessvoid.nifty.controls.DropDown;
import de.lessvoid.nifty.controls.DropDownSelectionChangedEvent;
import de.lessvoid.nifty.controls.FocusHandler;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.ListBox.ListBoxViewConverter;
import de.lessvoid.nifty.controls.ListBoxSelectionChangedEvent;
import de.lessvoid.nifty.controls.listbox.ListBoxControl;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;

/**
 * @deprecated Please use {@link de.lessvoid.nifty.controls.DropDown} when accessing NiftyControls.
 */
@Deprecated
public class DropDownControl<T> extends AbstractController implements DropDown<T> {
  private static Logger log = Logger.getLogger(DropDownControl.class.getName());

  private Nifty nifty;
  private boolean alreadyOpen = false;
  private FocusHandler focusHandler;
  private Screen screen;
  private Element popup;
  private ListBox<T> listBox;

  public void bind(
      final Nifty niftyParam,
      final Screen screenParam,
      final Element newElement,
      final Properties properties,
      final Attributes controlDefinitionAttributesParam) {
    super.bind(newElement);
    nifty = niftyParam;
    screen = screenParam;
    focusHandler = screen.getFocusHandler();

    final String elementId = getElement().getId();
    if (elementId == null) {
      log.warning("The DropDownControl requires an id but this one is missing it.");
      return;
    }
    popup = nifty.createPopupWithStyle(
        "dropDownBoxSelectPopup",
        getElement().getElementType().getAttributes().get("style"),
        new Attributes("dropDownControlId", elementId));

    popup.getControl(DropDownPopup.class).setDropDownElement(this);
    listBox = popup.findNiftyControl("#listBox", ListBox.class);
    nifty.subscribe(screen, listBox.getId(), ListBoxSelectionChangedEvent.class, new EventTopicSubscriber<ListBoxSelectionChangedEvent>() {
      @Override
      public void onEvent(final String topic, final ListBoxSelectionChangedEvent data) {
        Object selectedItem = getSelectedItem(data.getSelection());

        ListBoxControl listBoxControl = (ListBoxControl) listBox;
        listBoxControl.getViewConverter().display(getElement().findElementByName("#text"), selectedItem);

        nifty.publishEvent(elementId, new DropDownSelectionChangedEvent(selectedItem));
        close();
      }

      private Object getSelectedItem(final List selection) {
        if (selection.isEmpty()) {
          return null;
        }
        return selection.get(0);
      }
    });
  }

  @Override
  public void init(final Properties parameter, final Attributes controlDefinitionAttributes) {
  }

  public void onStartScreen() {
    updateEnabled();
  }

  public boolean inputEvent(final NiftyInputEvent inputEvent) {
    if (inputEvent == NiftyInputEvent.NextInputElement) {
      focusHandler.getNext(getElement()).setFocus();
      return true;
    } else if (inputEvent == NiftyInputEvent.PrevInputElement) {
      focusHandler.getPrev(getElement()).setFocus();
      return true;
    } else if (inputEvent == NiftyInputEvent.Activate) {
      dropDownClicked();
      return true;
    } else if (inputEvent == NiftyInputEvent.MoveCursorUp) {
      listBox.selectPrevious();
      return true;
    } else if (inputEvent == NiftyInputEvent.MoveCursorDown) {
      listBox.selectNext();
      return true;
    }
    return false;
  }

  public void dropDownClicked() {
    if (popup == null) {
      return;
    }
    if (alreadyOpen) {
      return;
    }

    alreadyOpen = true;
    nifty.showPopup(nifty.getCurrentScreen(), popup.getId(), null);
  }

  public void close() {
    alreadyOpen = false;
    nifty.closePopup(popup.getId());
  }

  public void refresh() {
  }

  private void updateEnabled() {
    setEnabled(!listBox.getItems().isEmpty());
  }

  // DropDown implementation that forwards to the internal ListBox

  @Override
  public void setViewConverter(final DropDownViewConverter<T> viewConverter) {
    listBox.setListBoxViewConverter(new ListBoxViewConverter<T>() {
      @Override
      public void display(final Element listBoxItem, final T item) {
        viewConverter.display(listBoxItem, item);
      }

      @Override
      public int getWidth(final Element element, final T item) {
        return viewConverter.getWidth(element, item);
      }
    });
  }

  @Override
  public void addItem(final T newItem) {
    listBox.addItem(newItem);
    updateEnabled();
  }

  @Override
  public void insertItem(final T item, final int index) {
    listBox.insertItem(item, index);
    updateEnabled();
  }

  @Override
  public int itemCount() {
    return listBox.itemCount();
  }

  @Override
  public void clear() {
    listBox.clear();
    updateEnabled();
  }

  @Override
  public void selectItemByIndex(final int selectionIndex) {
    listBox.selectItemByIndex(selectionIndex);
  }

  @Override
  public void selectItem(final T item) {
    listBox.selectItem(item);
  }

  @Override
  public T getSelection() {
    List<T> selection = listBox.getSelection();
    if (selection.isEmpty()) {
      return null;
    }
    return selection.get(0);
  }

  @Override
  public void removeItemByIndex(final int itemIndex) {
    listBox.removeItemByIndex(itemIndex);
    updateEnabled();
  }

  @Override
  public void removeItem(final T item) {
    listBox.removeItem(item);
    updateEnabled();
  }

  @Override
  public List<T> getItems() {
    return listBox.getItems();
  }

  @Override
  public void addAllItems(final List<T> itemsToAdd) {
    listBox.addAllItems(itemsToAdd);
    updateEnabled();
  }

  @Override
  public void removeAllItems(final List<T> itemsToRemove) {
    listBox.removeAllItems(itemsToRemove);
    updateEnabled();
  }
}
