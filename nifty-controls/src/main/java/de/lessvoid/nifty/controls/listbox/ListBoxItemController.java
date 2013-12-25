package de.lessvoid.nifty.controls.listbox;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.AbstractController;
import de.lessvoid.nifty.controls.Parameters;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ListBoxItemController<T> extends AbstractController {
  @Nullable
  private ListBoxImpl<T> listBox;
  private int visualItemIndex;
  @Nullable
  private Nifty nifty;
  @Nullable
  private Screen screen;

  @Override
  public void bind(
      @Nonnull final Nifty nifty,
      @Nonnull final Screen screen,
      @Nonnull final Element element,
      @Nonnull final Parameters properties) {
    bind(element);
    this.nifty = nifty;
    this.screen = screen;
  }

  @Override
  public void onStartScreen() {
  }

  @Override
  public void onFocus(final boolean getFocus) {
    super.onFocus(getFocus);
  }

  @Override
  public boolean inputEvent(@Nonnull final NiftyInputEvent inputEvent) {
    return false;
  }

  @Nullable
  protected ListBoxImpl<T> getListBox() {
    return listBox;
  }

  protected int getVisualItemIndex() {
    return this.visualItemIndex;
  }

  @Nullable
  protected Nifty getNifty() {
    return nifty;
  }

  @Nullable
  protected Screen getScreen() {
    return screen;
  }

  public void listBoxItemClicked() {
    if (listBox != null) {
      final T item = getItem();
      listBox.setFocusItem(item);
      if (listBox.getSelection().contains(item)) {
        listBox.deselectItemByVisualIndex(visualItemIndex);
      } else {
        listBox.selectItemByVisualIndex(visualItemIndex);
      }
    }
  }

  @Nullable
  protected T getItem() {
    if (listBox == null) {
      return null;
    }
    return listBox.getItemByVisualIndex(visualItemIndex);
  }

  public void setListBox(@Nullable final ListBoxImpl<T> listBox) {
    this.listBox = listBox;
  }

  public void setItemIndex(final int visualItemIndex) {
    this.visualItemIndex = visualItemIndex;
  }
}
