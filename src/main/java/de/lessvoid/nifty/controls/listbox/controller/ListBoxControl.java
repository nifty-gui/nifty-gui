package de.lessvoid.nifty.controls.listbox.controller;

import java.util.List;
import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.AbstractController;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.ListBoxSelectionChangedEvent;
import de.lessvoid.nifty.controls.ListBoxSelectionMode;
import de.lessvoid.nifty.controls.ListBoxSelectionModeDisabled;
import de.lessvoid.nifty.controls.ListBoxSelectionModeMulti;
import de.lessvoid.nifty.controls.ListBoxSelectionModeSingle;
import de.lessvoid.nifty.controls.dynamic.LabelCreator;
import de.lessvoid.nifty.controls.listbox.ListBoxImpl;
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
  private ListBoxImpl<T> listBoxImpl = new ListBoxImpl<T>();
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

  @SuppressWarnings("unchecked")
  public void bind(
      final Nifty niftyParam,
      final Screen screenParam,
      final Element elementParam,
      final Properties parameterParam,
      final ControllerEventListener listener,
      final Attributes controlDefinitionAttributes) {
    nifty = niftyParam;
    screen = screenParam;
    element = elementParam;
    parameter = parameterParam;
    verticalScrollbar = new Boolean(parameter.getProperty("vertical", "true"));
    horizontalScrollbar = new Boolean(parameter.getProperty("horizontal", "true"));
    displayItems = new Integer(parameter.getProperty("displayItems", "2"));
    childRootElement = element.findElementByName(controlDefinitionAttributes.get("childRootId"));
    labelTemplateElement = childRootElement.getElements().get(0);
    labelElements = new Element[displayItems];

    initSelectionMode(listBoxImpl, parameter.getProperty("selectionMode", "single"));

    ListBoxPanel<T> listBoxPanel = element.findControl("nifty-listbox-panel", ListBoxPanel.class);
    listBoxPanel.setListBox(listBoxImpl);

    int itemCount = listBoxImpl.bindToView(this, displayItems);
    if (displayItems == 1) {
      verticalScrollbar = false;
    }

    labelTemplateElement.getParent().layoutElements();
    labelTemplateHeight = labelTemplateElement.getHeight();
    labelTemplateStyle = labelTemplateElement.getElementType().getAttributes().get("style");
    labelTemplateController = labelTemplateElement.getElementType().getAttributes().get("controller");
    labelTemplateInputMapping = labelTemplateElement.getElementType().getAttributes().get("inputMapping");

    nifty.removeElement(screen, labelTemplateElement);

    initializeScrollPanel(screen);

    int horizontalScrollbarElementHeight = 0;
    Element horizontalScrollbarElement = element.findElementByName("nifty-internal-horizontal-scrollbar");
    if (horizontalScrollbarElement != null) {
      horizontalScrollbarElementHeight = horizontalScrollbarElement.getHeight();
    }
    element.setConstraintHeight(new SizeValue(displayItems * labelTemplateHeight + horizontalScrollbarElementHeight + "px"));

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
      ListBoxItemController<T> listBoxItemController = labelElements[i].getControl(ListBoxItemController.class);
      listBoxItemController.setListBox(listBoxImpl);
    }

    updateScrollPanel(screen, 1.0f, labelTemplateHeight, itemCount);
    listBoxImpl.updateView(0);
  }

  private void initSelectionMode(final ListBoxImpl<T> listBoxImpl, final String selectionMode) {
    if (selectionMode.equals("single")) {
      listBoxImpl.changeSelectionMode(new ListBoxSelectionModeSingle<T>());
    } else if (selectionMode.equals("multiple")) {
      listBoxImpl.changeSelectionMode(new ListBoxSelectionModeMulti<T>());
    } else {
      listBoxImpl.changeSelectionMode(new ListBoxSelectionModeDisabled<T>());
    }
  }

  public boolean inputEvent(final NiftyInputEvent inputEvent) {
    return false;
  }

  @Override
  public void onFocus(final boolean getFocus) {
    super.onFocus(getFocus);
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

  public void updateScrollPanel(final Screen screen, final float stepSizeX, final float labelTemplateHeight, final int itemCount) {
    if (childRootElement != null) {
      final Element scrollElement = element.findElementByName("nifty-listbox-child-root");

      HorizontalScrollbarControl horizontalS = element.findControl("nifty-internal-horizontal-scrollbar", HorizontalScrollbarControl.class);
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

      VerticalScrollbarControl verticalS = element.findControl("nifty-internal-vertical-scrollbar", VerticalScrollbarControl.class);
      if (verticalS != null) {
        verticalS.setWorldMaxValue(itemCount * labelTemplateHeight);
        verticalS.setViewMaxValue(childRootElement.getHeight());
        verticalS.setPerClickChange(labelTemplateHeight);
        verticalS.setScrollBarControlNotify(new ScrollbarControlNotify() {
          public void positionChanged(final float currentValue) {
            listBoxImpl.updateView((int) (currentValue / labelTemplateHeight));
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

  @SuppressWarnings("unchecked")
  @Override
  public void display(final List<T> visibleItems, final int focusElement, final List<Integer> selectedElements) {
    for (int i = 0; i < visibleItems.size(); i++) {
      T item = visibleItems.get(i);
      viewConverter.display(labelElements[i], item);

      ListBoxItemController<T> listBoxItemController = labelElements[i].getControl(ListBoxItemController.class);
      listBoxItemController.setItemIndex(i);

      ListBoxPanel<T> listBoxPanel = element.findControl("nifty-listbox-panel", ListBoxPanel.class);
      if (listBoxPanel.hasFocus()) {
        if (focusElement == i) {
          labelElements[i].startEffect(EffectEventId.onCustom, null, "focus");
        } else {
          labelElements[i].resetSingleEffect(EffectEventId.onCustom, "focus");
        }
      } else {
        labelElements[i].resetSingleEffect(EffectEventId.onCustom, "focus");
      }

      if (item != null && selectedElements.contains(i)) {
        labelElements[i].startEffect(EffectEventId.onCustom, null, "select");
      } else {
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
    VerticalScrollbarControl verticalS = element.findControl("nifty-internal-vertical-scrollbar", VerticalScrollbarControl.class);
    if (verticalS != null) {
      verticalS.setCurrentValue(newPosition * labelTemplateHeight);
    }
  }

  // ListBox Interface Implementation

  @Override
  public void changeSelectionMode(final ListBoxSelectionMode<T> listBoxSelectionMode) {
    listBoxImpl.changeSelectionMode(listBoxSelectionMode);
  }

  @Override
  public void addItem(final T newItem) {
    listBoxImpl.addItem(newItem);
  }

  @Override
  public void insertItem(final T item, final int index) {
    listBoxImpl.insertItem(item, index);
  }

  @Override
  public int itemCount() {
    return listBoxImpl.itemCount();
  }

  @Override
  public void clear() {
    listBoxImpl.clear();
  }

  @Override
  public void selectItemByIndex(final int selectionIndex) {
    listBoxImpl.selectItemByIndex(selectionIndex);
  }

  @Override
  public void selectItem(final T item) {
    listBoxImpl.selectItem(item);
  }

  @Override
  public void deselectItemByIndex(final int itemIndex) {
    listBoxImpl.deselectItemByIndex(itemIndex);
  }

  @Override
  public void deselectItem(final T item) {
    listBoxImpl.deselectItem(item);
  }

  @Override
  public List<T> getSelection() {
    return listBoxImpl.getSelection();
  }

  @Override
  public void removeItemByIndex(final int itemIndex) {
    listBoxImpl.removeItemByIndex(itemIndex);
  }

  @Override
  public void removeItem(final T item) {
    listBoxImpl.removeItem(item);
  }

  @Override
  public List<T> getItems() {
    return listBoxImpl.getItems();
  }

  @Override
  public void showItem(final T item) {
    listBoxImpl.showItem(item);
  }

  @Override
  public void showItemByIndex(final int itemIndex) {
    listBoxImpl.showItemByIndex(itemIndex);
  }

  @Override
  public void setFocusItem(final T item) {
    listBoxImpl.setFocusItem(item);
  }

  @Override
  public void setFocusItemByIndex(final int itemIndex) {
    listBoxImpl.setFocusItemByIndex(itemIndex);
  }

  @Override
  public T getFocusItem() {
    return listBoxImpl.getFocusItem();
  }

  @Override
  public int getFocusItemIndex() {
    return listBoxImpl.getFocusItemIndex();
  }

  @Override
  public void setListBoxViewConverter(final ListBoxViewConverter<T> viewConverter) {
    this.viewConverter = viewConverter;
  }

  @Override
  public void publish(final ListBoxSelectionChangedEvent<T> event) {
    nifty.publishEvent(element.getId(), event);
  }

  @Override
  public void addAllItems(final List<T> itemsToAdd) {
    listBoxImpl.addAllItems(itemsToAdd);
  }

  @Override
  public void removeAllItems(final List<T> itemsToRemove) {
    listBoxImpl.removeAllItems(itemsToRemove);
  }
}
