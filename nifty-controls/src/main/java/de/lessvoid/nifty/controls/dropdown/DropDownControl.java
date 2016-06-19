package de.lessvoid.nifty.controls.dropdown;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.AbstractController;
import de.lessvoid.nifty.controls.DropDown;
import de.lessvoid.nifty.controls.FocusHandler;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.ListBox.ListBoxViewConverter;
import de.lessvoid.nifty.controls.ListBoxSelectionChangedEvent;
import de.lessvoid.nifty.controls.Parameters;
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
  @Nonnull
  private static final Logger log = Logger.getLogger(DropDownControl.class.getName());

  @Nullable
  private Nifty nifty;
  private boolean alreadyOpen = false;
  @Nullable
  private FocusHandler focusHandler;
  @Nullable
  private Screen screen;
  @Nullable
  private Element popup;
  @Nullable
  private ListBox<T> listBox;

  @SuppressWarnings("unchecked")
  @Override
  public void bind(
      @Nonnull final Nifty nifty,
      @Nonnull final Screen screen,
      @Nonnull final Element element,
      @Nonnull final Parameters properties) {
    super.bind(element);
    this.nifty = nifty;
    this.screen = screen;
    focusHandler = screen.getFocusHandler();

    final String elementId = getId();
    if (elementId == null) {
      log.warning("The DropDownControl requires an id but this one is missing it.");
      return;
    }
    Attributes parameters = new Attributes("displayItems", properties.getWithDefault("displayItems", "4"));
    popup = nifty.createPopupWithStyle(screen, "dropDownBoxSelectPopup", properties.get("style"), parameters);
    DropDownPopup<T> popupControl = popup.getControl(DropDownPopup.class);
    if (popupControl == null) {
      log.severe("Popup of drop down does not contain the proper controller. Dropdown element will not work. " +
          "Expected controller class: " + DropDownPopup.class.getName());
    } else {
      popupControl.setDropDownElement(this, popup);
    }
    listBox = popup.findNiftyControl("#listBox", ListBox.class);

    if (listBox == null) {
      log.severe("Failed to locate list box of the drop down. Drop down element will not work. Looked for: #listBox");
    }

    final DropDownViewConverter<T> converter = createViewConverter(properties.getProperty("viewConverterClass"));
    if (converter != null) {
      setViewConverter(converter);
    }
  }

  @Nullable
  @SuppressWarnings("unchecked")
  private DropDownViewConverter<T> createViewConverter(@Nullable final String className) {
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
  public void onStartScreen() {
    updateEnabled();

    if (listBox instanceof ListBoxControl) {
      ListBoxControl<T> listBoxControl = (ListBoxControl<T>) listBox;
      Element element = getElement();
      if (element != null) {
        Element textElement = element.findElementById("#text");
        if (textElement != null) {
          T selectedItem = getSelection();
          if (selectedItem != null) {
            ListBoxViewConverter<T> converter = listBoxControl.getViewConverter();
            if (converter != null) {
              converter.display(textElement, selectedItem);
            }
          }
        }
      }
    }

    if (nifty != null && screen != null && listBox != null && popup != null) {
      String listBoxId = listBox.getId();
      if (listBoxId != null) {
        nifty.subscribe(screen, listBoxId, ListBoxSelectionChangedEvent.class,
            new DropDownListBoxSelectionChangedEventSubscriber(nifty, screen, listBox, this, popup));
      }
    }
  }

  @Override
  public boolean inputEvent(@Nonnull final NiftyInputEvent inputEvent) {
    if (inputEvent == NiftyStandardInputEvent.Activate) {
      dropDownClicked();
      return true;
    }
    if (listBox != null) {
      if (inputEvent == NiftyStandardInputEvent.MoveCursorUp) {
        listBox.selectPrevious();
        return true;
      } else if (inputEvent == NiftyStandardInputEvent.MoveCursorDown) {
        listBox.selectNext();
        return true;
      }
    }
    Element element = getElement();
    if (element != null && focusHandler != null) {
      if (inputEvent == NiftyStandardInputEvent.NextInputElement) {
        focusHandler.getNext(element).setFocus();
        return true;
      } else if (inputEvent == NiftyStandardInputEvent.PrevInputElement) {
        focusHandler.getPrev(element).setFocus();
        return true;
      }
    }

    return false;
  }

  public void dropDownClicked() {
    if (popup == null || nifty == null || screen == null || alreadyOpen) {
      return;
    }
    String popupId = popup.getId();
    if (popupId != null) {
      alreadyOpen = true;
      nifty.showPopup(screen, popup.getId(), null);
    }
  }

  public void close() {
    closeInternal(null);
  }

  public void close(final EndNotify endNotify) {
    closeInternal(endNotify);
  }

  private void closeInternal(@Nullable final EndNotify endNotify) {
    alreadyOpen = false;
    if (nifty == null || popup == null || screen == null || listBox == null) {
      return;
    }
    String popupId = popup.getId();
    final String listBoxId = listBox.getId();
    if (popupId == null || listBoxId == null) {
      return;
    }
    nifty.closePopup(popupId, new EndNotify() {
      @Override
      public void perform() {
        // this really feels like a hack but I don't have another idea right now:
        //
        // when the popup is closed Nifty will automatically remove all subscribers for all controls in the popup.
        // this is in general the right behaviour, since the controls are gone (the popup is closed). However in this
        // case here the listbox is still used by the DropDown. So we need to subscribe our listener again.
        nifty.subscribe(screen, listBoxId, ListBoxSelectionChangedEvent.class,
            new DropDownListBoxSelectionChangedEventSubscriber(nifty, screen, listBox, DropDownControl.this, popup));
        if (endNotify != null) {
          endNotify.perform();
        }
      }
    });
  }

  public void refresh() {
  }

  private void updateEnabled() {
    setEnabled(!(listBox != null && listBox.getItems().isEmpty()));
  }

  // DropDown implementation that forwards to the internal ListBox

  @Override
  public void setViewConverter(@Nonnull final DropDownViewConverter<T> viewConverter) {
    if (listBox == null) {
      log.warning("Can't apply view converter before the binding is done.");
    } else {
      listBox.setListBoxViewConverter(new ListBoxViewConverter<T>() {
        @Override
        public void display(@Nonnull final Element listBoxItem, @Nonnull final T item) {
          viewConverter.display(listBoxItem, item);
        }

        @Override
        public int getWidth(@Nonnull final Element element, @Nonnull final T item) {
          return viewConverter.getWidth(element, item);
        }
      });
    }
  }

  @Override
  public void addItem(@Nonnull final T newItem) {
    if (listBox == null) {
      if (!isBound()) {
        throw new IllegalStateException("Can't add item before the binding is done.");
      } else {
        log.severe("Binding seems to have failed, can't add item.");
        return;
      }
    }
    listBox.addItem(newItem);
    updateEnabled();
  }

  @Override
  public void insertItem(@Nonnull final T item, final int index) {
    if (listBox == null) {
      if (!isBound()) {
        throw new IllegalStateException("Can't insert item before the binding is done.");
      } else {
        log.severe("Binding seems to have failed, can't insert item.");
        return;
      }
    }
    listBox.insertItem(item, index);
    updateEnabled();
  }

  @Override
  public int itemCount() {
    if (listBox == null) {
      return 0;
    }
    return listBox.itemCount();
  }

  @Override
  public void clear() {
    if (listBox != null) {
      listBox.clear();
      updateEnabled();
    }
  }

  @Override
  public void selectItemByIndex(final int selectionIndex) {
    if (listBox == null) {
      if (!isBound()) {
        throw new IllegalStateException("Can't select item before the binding is done.");
      } else {
        log.severe("Binding seems to have failed, can't select item.");
        return;
      }
    }
    listBox.selectItemByIndex(selectionIndex);
  }

  @Override
  public void selectItem(@Nonnull final T item) {
    if (listBox == null) {
      if (!isBound()) {
        throw new IllegalStateException("Can't select item before the binding is done.");
      } else {
        log.severe("Binding seems to have failed, can't select item.");
        return;
      }
    }
    listBox.selectItem(item);
  }

  @Nullable
  @Override
  public T getSelection() {
    if (listBox == null) {
      return null;
    }
    List<T> selection = listBox.getSelection();
    if (selection.isEmpty()) {
      return null;
    }
    return selection.get(0);
  }

  @Override
  public int getSelectedIndex() {
    if (listBox == null) {
      return -1;
    }
    List<Integer> selection = listBox.getSelectedIndices();
    if (selection.isEmpty()) {
      return -1;
    }
    return selection.get(0);
  }

  @Override
  public void removeItemByIndex(final int itemIndex) {
    if (listBox == null) {
      if (!isBound()) {
        throw new IllegalStateException("Can't remove item before the binding is done.");
      } else {
        log.severe("Binding seems to have failed, can't remove item.");
        return;
      }
    }
    listBox.removeItemByIndex(itemIndex);
    updateEnabled();
  }

  @Override
  public void removeItem(@Nonnull final T item) {
    if (listBox == null) {
      if (!isBound()) {
        throw new IllegalStateException("Can't add item before the binding is done.");
      } else {
        log.severe("Binding seems to have failed, can't add item.");
        return;
      }
    }
    listBox.removeItem(item);
    updateEnabled();
  }

  @Nonnull
  @Override
  public List<T> getItems() {
    if (listBox == null) {
      return Collections.emptyList();
    }
    return listBox.getItems();
  }

  @Override
  public void addAllItems(@Nonnull final Collection<T> itemsToAdd) {
    if (listBox == null) {
      if (!isBound()) {
        throw new IllegalStateException("Can't add items before the binding is done.");
      } else {
        log.severe("Binding seems to have failed, can't add items.");
        return;
      }
    }
    listBox.addAllItems(itemsToAdd);
    updateEnabled();
  }

  @Override
  public void removeAllItems(@Nonnull final Collection<T> itemsToRemove) {
    if (listBox == null) {
      if (!isBound()) {
        throw new IllegalStateException("Can't remove items before the binding is done.");
      } else {
        log.severe("Binding seems to have failed, can't remove items.");
        return;
      }
    }
    listBox.removeAllItems(itemsToRemove);
    updateEnabled();
  }
}
