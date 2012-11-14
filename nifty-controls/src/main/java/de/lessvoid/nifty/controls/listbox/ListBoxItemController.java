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
    private Nifty nifty;
    private Screen screen;

    @Override
    public void bind(
            final Nifty niftyParam,
            final Screen screenParam,
            final Element newElement,
            final Properties properties,
            final Attributes controlDefinitionAttributes) {
      bind(newElement);
        this.nifty = niftyParam;
        this.screen = screenParam;
    }

    @Override
    public void onStartScreen() {
    }

    @Override
    public void onFocus(final boolean getFocus) {
        super.onFocus(getFocus);
    }

    @Override
    public boolean inputEvent(final NiftyInputEvent inputEvent) {
        return false;
    }

    protected ListBoxImpl<T> getListBox() {
        return listBox;
    }

    protected int getVisualItemIndex() {
        return this.visualItemIndex;
    }

    protected Nifty getNifty() {
        return nifty;
    }

    protected Screen getScreen() {
        return screen;
    }

    public void listBoxItemClicked() {
        final T item = getItem();
        listBox.setFocusItem(item);
        if (listBox.getSelection().contains(item)) {
            listBox.deselectItemByVisualIndex(visualItemIndex);
        } else {
            listBox.selectItemByVisualIndex(visualItemIndex);
        }
    }

    protected T getItem() {
      return listBox.getItemByVisualIndex(visualItemIndex);
    }

    public void setListBox(final ListBoxImpl<T> listBox) {
        this.listBox = listBox;
    }

    public void setItemIndex(final int visualItemIndex) {
        this.visualItemIndex = visualItemIndex;
    }
}
