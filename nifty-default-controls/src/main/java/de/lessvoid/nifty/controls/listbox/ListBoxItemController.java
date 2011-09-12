package de.lessvoid.nifty.controls.listbox;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.AbstractController;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;

public class ListBoxItemController<T> extends AbstractController {
  private ListBoxImpl<T> listBox;
  private int visualItemIndex;

  public void bind(
      final Nifty niftyParam,
      final Screen screenParam,
      final Element newElement,
      final Properties properties,
      final Attributes controlDefinitionAttributes) {
  }

  public void onStartScreen() {
  }

  @Override
  public void onFocus(final boolean getFocus) {
    super.onFocus(getFocus);
  }

  public boolean inputEvent(final NiftyInputEvent inputEvent) {
    return false;
  }

  public void listBoxItemClicked() {
    T item = listBox.getItemByVisualIndex(visualItemIndex);
    listBox.setFocusItem(item);
    if (listBox.getSelection().contains(item)) {
      listBox.deselectItemByVisualIndex(visualItemIndex);
    } else {
      listBox.selectItemByVisualIndex(visualItemIndex);
    }
  }

  public void setListBox(final ListBoxImpl<T> listBox) {
    this.listBox = listBox;
  }

  public void setItemIndex(final int visualItemIndex) {
    this.visualItemIndex = visualItemIndex;
  }
}
