package de.lessvoid.nifty.controls.listbox.controller;

import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.AbstractController;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.dynamic.LabelCreator;
import de.lessvoid.nifty.controls.listbox.ListBoxImpl;
import de.lessvoid.nifty.controls.listbox.ListBoxSelectionMode;
import de.lessvoid.nifty.controls.listbox.ListBoxView;
import de.lessvoid.nifty.controls.listbox.ListBoxViewConverter;
import de.lessvoid.nifty.controls.listbox.SimpleListBoxViewConverter;
import de.lessvoid.nifty.controls.scrollbar.controller.HorizontalScrollbarControl;
import de.lessvoid.nifty.controls.scrollbar.controller.ScrollbarControlNotify;
import de.lessvoid.nifty.controls.scrollbar.controller.VerticalScrollbarControl;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.ControllerEventListener;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.xml.xpp3.Attributes;

public class ListBoxControl<T> extends AbstractController implements ListBox<T>, ListBoxView<T> {
    private Logger log = Logger.getLogger(ListBoxControl.class.getName());

    private ListBoxImpl<T> listBox = new ListBoxImpl<T>();

    private Element[] labelElements;

    private Nifty nifty;

    private Screen screen;

    private Element element;

    private boolean verticalScrollbar;

    private boolean horizontalScrollbar;

    private Element childRootElement;

    private Element labelTemplateElement;

    private int labelTemplateHeight;

    private String labelTemplateStyle;

    private String labelTemplateController;

    private String labelTemplateInputMapping;

    private Properties parameter;

    private int displayItems;

    private ListBoxViewConverter<T> viewConverter = new SimpleListBoxViewConverter<T>();

    public void bind(final Nifty niftyParam, final Screen screenParam, final Element elementParam,
            final Properties parameterParam, final ControllerEventListener listener,
            final Attributes controlDefinitionAttributes) {
        nifty = niftyParam;
        screen = screenParam;
        element = elementParam;
        parameter = parameterParam;
        verticalScrollbar = new Boolean(parameter.getProperty("vertical", "true"));
        horizontalScrollbar = new Boolean(parameter.getProperty("horizontal", "true"));
        displayItems = new Integer(parameter.getProperty("displayItems", "1"));
        childRootElement = element
                .findElementByName(controlDefinitionAttributes.get("childRootId"));
        labelTemplateElement = childRootElement.getElements().get(0);
        labelElements = new Element[displayItems];

        ListBoxPanel<T> listBoxPanel = element.findControl("nifty-listbox-panel",
                ListBoxPanel.class);
        listBoxPanel.setListBox(listBox);

        int itemCount = listBox.bindToView(this, displayItems);
        if (displayItems == 1) {
            verticalScrollbar = false;
        }

        labelTemplateElement.getParent().layoutElements();
        labelTemplateHeight = labelTemplateElement.getHeight();
        labelTemplateStyle = labelTemplateElement.getElementType().getAttributes().get("style");
        labelTemplateController = labelTemplateElement.getElementType().getAttributes()
                .get("controller");
        labelTemplateInputMapping = labelTemplateElement.getElementType().getAttributes()
                .get("inputMapping");

        nifty.removeElement(screen, labelTemplateElement);

        initializeScrollPanel(screen);

        int horizontalScrollbarElementHeight = 0;
        Element horizontalScrollbarElement = element
                .findElementByName("nifty-internal-horizontal-scrollbar");
        if (horizontalScrollbarElement != null) {
            horizontalScrollbarElementHeight = horizontalScrollbarElement.getHeight();
        }
        element.setConstraintHeight(new SizeValue(displayItems * labelTemplateHeight
                + horizontalScrollbarElementHeight + "px"));

        Element scrollElement = element.findElementByName("nifty-listbox-scrollpanel");
        scrollElement.setConstraintHeight(new SizeValue(displayItems * labelTemplateHeight + "px"));

        for (int i = 0; i < displayItems; i++) {
            // create the label
            LabelCreator createLabel = new LabelCreator("label: " + i);
            createLabel.setStyle(labelTemplateStyle);
            createLabel.setController(labelTemplateController);
            createLabel.setInputMapping(labelTemplateInputMapping);
            labelElements[i] = createLabel.create(nifty, screen, childRootElement);

            // connect it to this listbox
            ListBoxItemController<T> listBoxItemController = labelElements[i]
                    .getControl(ListBoxItemController.class);
            listBoxItemController.setListBox(listBox);
        }

        updateScrollPanel(screen, 1.0f, labelTemplateHeight, itemCount);
        listBox.updateView(0);
    }

    public boolean inputEvent(NiftyInputEvent inputEvent) {
        return false;
    }

    @Override
    public void onFocus(boolean getFocus) {
    }

    public void onStartScreen() {
    }

    public void initializeScrollPanel(final Screen screen) {
        if (!horizontalScrollbar) {
            Element horizontal = element.findElementByName("nifty-internal-horizontal-scrollbar");
            if (horizontal != null) {
                nifty.removeElement(screen, horizontal);
            }
        }
        if (!verticalScrollbar) {
            Element vertical = element.findElementByName("nifty-internal-vertical-scrollbar");
            if (vertical != null) {
                nifty.removeElement(screen, vertical);
            }
        }

        Element scrollElement = element.findElementByName("nifty-listbox-child-root");
        scrollElement.setConstraintX(new SizeValue("0px"));
        scrollElement.setConstraintY(new SizeValue("0px"));

        nifty.executeEndOfFrameElementActions();
        screen.layoutLayers();
    }

    public void updateScrollPanel(final Screen screen, final float stepSizeX,
            final float labelTemplateHeight, final int itemCount) {
        if (childRootElement != null) {
            final Element scrollElement = element.findElementByName("nifty-listbox-child-root");

            HorizontalScrollbarControl horizontalS = element.findControl(
                    "nifty-internal-horizontal-scrollbar", HorizontalScrollbarControl.class);
            if (horizontalS != null) {
                horizontalS.setWorldMaxValue(500); // FIXME
                horizontalS.setViewMaxValue(childRootElement.getWidth());
                horizontalS.setPerClickChange(stepSizeX);
                horizontalS.setScrollBarControlNotify(new ScrollbarControlNotify() {
                    public void positionChanged(final float currentValue) {
                        if (scrollElement != null) {
                            scrollElement.setConstraintX(new SizeValue(-(int) currentValue + "px"));
                            scrollElement.getParent().layoutElements();
                        }
                    }
                });
            }

            VerticalScrollbarControl verticalS = element.findControl(
                    "nifty-internal-vertical-scrollbar", VerticalScrollbarControl.class);
            if (verticalS != null) {
                verticalS.setWorldMaxValue(itemCount * labelTemplateHeight);
                verticalS.setViewMaxValue(childRootElement.getHeight());
                verticalS.setPerClickChange(labelTemplateHeight);
                verticalS.setScrollBarControlNotify(new ScrollbarControlNotify() {
                    public void positionChanged(final float currentValue) {
                        listBox.updateView((int) (currentValue / labelTemplateHeight));
                    }
                });
            }
        }
        screen.layoutLayers();
    }

    public void setFocus() {
        childRootElement.setFocus();
    }

    // ListBoxView Interface implementation

    @Override
    public void display(final List<T> visibleItems, final int focusElement,
            final List<Integer> selectedElements) {
        for (int i = 0; i < visibleItems.size(); i++) {
            T item = visibleItems.get(i);
            viewConverter.display(labelElements[i], item);

            ListBoxItemController<T> listBoxItemController = labelElements[i]
                    .getControl(ListBoxItemController.class);
            listBoxItemController.setItemIndex(i);

            if (focusElement == i) {
                labelElements[i].startEffect(EffectEventId.onCustom, null, "focus");
            }
            else {
                labelElements[i].resetSingleEffect(EffectEventId.onCustom, "focus");
            }

            if (item != null && selectedElements.contains(i)) {
                labelElements[i].startEffect(EffectEventId.onCustom, null, "select");
            }
            else {
                labelElements[i].resetSingleEffect(EffectEventId.onCustom, "select");
            }
        }
    }

    @Override
    public void updateTotalCount(final int newCount) {
        updateScrollPanel(screen, 1.0f, labelTemplateHeight, newCount);
    }

    @Override
    public void scrollTo(final int newPosition) {
        VerticalScrollbarControl verticalS = element.findControl(
                "nifty-internal-vertical-scrollbar", VerticalScrollbarControl.class);
        if (verticalS != null) {
            verticalS.setCurrentValue(newPosition * labelTemplateHeight);
        }
    }

    // ListBox Interface Implementation

    @Override
    public void setSelectionMode(final ListBoxSelectionMode<T> listBoxSelectionMode) {
        listBox.setSelectionMode(listBoxSelectionMode);
    }

    @Override
    public void addItem(final T newItem) {
        listBox.addItem(newItem);
    }

    @Override
    public void insertItem(final T item, final int index) {
        listBox.insertItem(item, index);
    }

    @Override
    public int itemCount() {
        return listBox.itemCount();
    }

    @Override
    public void clear() {
        listBox.clear();
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
    public void deselectItemByIndex(final int itemIndex) {
        listBox.deselectItemByIndex(itemIndex);
    }

    @Override
    public void deselectItem(final T item) {
        listBox.deselectItem(item);
    }

    @Override
    public List<T> getSelection() {
        return listBox.getSelection();
    }

    @Override
    public void removeItemByIndex(final int itemIndex) {
        listBox.removeItemByIndex(itemIndex);
    }

    @Override
    public void removeItem(final T item) {
        listBox.removeItem(item);
    }

    @Override
    public List<T> getItems() {
        return listBox.getItems();
    }

    @Override
    public void showItem(final T item) {
        listBox.showItem(item);
    }

    @Override
    public void showItemByIndex(final int itemIndex) {
        listBox.showItemByIndex(itemIndex);
    }

    @Override
    public void setFocusItem(final T item) {
        listBox.setFocusItem(item);
    }

    @Override
    public void setFocusItemByIndex(final int itemIndex) {
        listBox.setFocusItemByIndex(itemIndex);
    }

    @Override
    public T getFocusItem() {
        return listBox.getFocusItem();
    }

    @Override
    public int getFocusItemIndex() {
        return listBox.getFocusItemIndex();
    }

    @Override
    public void setListBoxViewConverter(final ListBoxViewConverter<T> viewConverter) {
        this.viewConverter = viewConverter;
    }
}
