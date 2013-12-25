package de.lessvoid.nifty.controls.listbox;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.AbstractController;
import de.lessvoid.nifty.controls.FocusHandler;
import de.lessvoid.nifty.controls.Parameters;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.input.NiftyStandardInputEvent;
import de.lessvoid.nifty.screen.Screen;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ListBoxPanel<T> extends AbstractController {
  @Nullable
  private FocusHandler focusHandler;
  @Nullable
  private ListBoxImpl<T> listBox;
  private boolean hasFocus = false;

  @Override
  public void bind(
      @Nonnull final Nifty nifty,
      @Nonnull final Screen screen,
      @Nonnull final Element element,
      @Nonnull final Parameters parameter) {
    bind(element);
    focusHandler = screen.getFocusHandler();
  }

  @Override
  public void onStartScreen() {
  }

  @Override
  public boolean inputEvent(@Nonnull final NiftyInputEvent inputEvent) {
    Element element = getElement();
    if (inputEvent == NiftyStandardInputEvent.NextInputElement) {
      if (focusHandler != null && element != null) {
        Element nextElement = focusHandler.getNext(element);
        nextElement.setFocus();
        return true;
      }
    } else if (inputEvent == NiftyStandardInputEvent.PrevInputElement) {
      if (focusHandler != null && element != null) {
        Element prevElement = focusHandler.getPrev(element);
        prevElement.setFocus();
        return true;
      }
    } else if (inputEvent == NiftyStandardInputEvent.MoveCursorDown) {
      if (hasElements()) {
        assert listBox != null; // has elements checks this
        int focusItemIndex = listBox.getFocusItemIndex();
        if (focusItemIndex < listBox.itemCount() - 1) {
          listBox.setFocusItemByIndex(focusItemIndex + 1);
          return true;
        }
      }
    } else if (inputEvent == NiftyStandardInputEvent.MoveCursorUp) {
      if (hasElements()) {
        assert listBox != null; // has elements checks this
        int focusItemIndex = listBox.getFocusItemIndex();
        if (focusItemIndex > 0) {
          listBox.setFocusItemByIndex(focusItemIndex - 1);
          return true;
        }
      }
    } else if (inputEvent == NiftyStandardInputEvent.Activate) {
      if (hasElements()) {
        assert listBox != null; // has elements checks this
        int focusItemIndex = listBox.getFocusItemIndex();
        if (focusItemIndex >= 0) {
          if (listBox.getSelection().contains(listBox.getFocusItem())) {
            listBox.deselectItemByIndex(focusItemIndex);
          } else {
            listBox.selectItemByIndex(focusItemIndex);
          }
        }
      }
    }
    return false;
  }

  private boolean hasElements() {
    return listBox != null && listBox.itemCount() > 0;
  }

  @Override
  public void onFocus(final boolean getFocus) {
    super.onFocus(getFocus);

    hasFocus = getFocus;
    if (listBox != null) {
      listBox.updateView();
    }
  }

  public void setListBox(@Nullable final ListBoxImpl<T> listBox) {
    this.listBox = listBox;
  }

  @Override
  public boolean hasFocus() {
    return hasFocus;
  }
}
