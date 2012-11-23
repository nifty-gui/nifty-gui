package de.lessvoid.nifty.controls.dropdown;

import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.AbstractController;
import de.lessvoid.nifty.controls.DropDown;
import de.lessvoid.nifty.controls.FocusHandler;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.ListBox.ListBoxViewConverter;
import de.lessvoid.nifty.controls.ListBoxSelectionChangedEvent;
import de.lessvoid.nifty.controls.listbox.ListBoxControl;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.input.NiftyStandardInputEvent;
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

  @Override
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
    Attributes parameters = new Attributes("displayItems", properties.getProperty("displayItems", "4"));
    popup = nifty.createPopupWithStyle("dropDownBoxSelectPopup", getElement().getElementType().getAttributes().get("style"), parameters);
    popup.getControl(DropDownPopup.class).setDropDownElement(this, popup);
    listBox = popup.findNiftyControl("#listBox", ListBox.class);

    final DropDownViewConverter<T> converter = createViewConverter(properties.getProperty("viewConverterClass"));
    if (converter != null) {
      setViewConverter(converter);
    }
  }

  @SuppressWarnings("unchecked")
  private DropDownViewConverter<T> createViewConverter(final String className) {
    if (className == null) {
      return null;
    }
    try {
      return (DropDownViewConverter<T>) Class.forName(className).newInstance();
    } catch (Exception e) {
      log.log(Level.WARNING, "Unable to instantiate given class [" + className + "] with error: " + e.getMessage(), e);
      return null;
    }
  }

  @Override
  @SuppressWarnings("rawtypes")
  public void onStartScreen() {
    updateEnabled();

    ListBoxControl listBoxControl = (ListBoxControl) listBox;
    listBoxControl.getViewConverter().display(getElement().findElementByName("#text"), getSelection());

    nifty.subscribe(screen, listBox.getId(), ListBoxSelectionChangedEvent.class,
        new DropDownListBoxSelectionChangedEventSubscriber(nifty, screen, listBox, this, popup));
  }

  @Override
  public boolean inputEvent(final NiftyInputEvent inputEvent) {
    if (inputEvent == NiftyStandardInputEvent.NextInputElement) {
      focusHandler.getNext(getElement()).setFocus();
      return true;
    } else if (inputEvent == NiftyStandardInputEvent.PrevInputElement) {
      focusHandler.getPrev(getElement()).setFocus();
      return true;
    } else if (inputEvent == NiftyStandardInputEvent.Activate) {
      dropDownClicked();
      return true;
    } else if (inputEvent == NiftyStandardInputEvent.MoveCursorUp) {
      listBox.selectPrevious();
      return true;
    } else if (inputEvent == NiftyStandardInputEvent.MoveCursorDown) {
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
    closeInternal(null);
  }

  public void close(final EndNotify endNotify) {
    closeInternal(endNotify);
  }

  private void closeInternal(final EndNotify endNotify) {
    alreadyOpen = false;
    nifty.closePopup(popup.getId(), new EndNotify() {
      @Override
      public void perform() {
        // this really feels like a hack but I don't have another idea right now:
        //
        // when the popup is closed Nifty will automatically remove all subscribers for all controls in the popup.
        // this is in general the right behaviour, since the controls are gone (the popup is closed). However in this
        // case here the listbox is still used by the DropDown. So we need to subscribe our listener again.
        nifty.subscribe(screen, listBox.getId(), ListBoxSelectionChangedEvent.class, new DropDownListBoxSelectionChangedEventSubscriber(nifty, screen, listBox, DropDownControl.this, popup));
        if (endNotify != null) {
          endNotify.perform();
        }
      }
    });
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
  public int getSelectedIndex() {
    List<Integer> selection = listBox.getSelectedIndices();
    if (selection.isEmpty()) {
      return -1;
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
