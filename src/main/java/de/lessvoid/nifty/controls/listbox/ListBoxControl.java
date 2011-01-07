package de.lessvoid.nifty.controls.listbox;

import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.AbstractController;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.ListBoxSelectionChangedEvent;
import de.lessvoid.nifty.controls.scrollbar.controller.HorizontalScrollbarControl;
import de.lessvoid.nifty.controls.scrollbar.controller.ScrollbarControlNotify;
import de.lessvoid.nifty.controls.scrollbar.controller.VerticalScrollbarControl;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.ControllerEventListener;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.layout.LayoutPart;
import de.lessvoid.nifty.loaderv2.types.ElementType;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.xml.xpp3.Attributes;

public class ListBoxControl<T> extends AbstractController implements ListBox<T>, ListBoxView<T> {
  private Logger log = Logger.getLogger(ListBoxControl.class.getName());
  private ListBoxImpl<T> listBoxImpl = new ListBoxImpl<T>();
  private Element[] labelElements;
  private Nifty nifty;
  private Screen screen;
  private boolean verticalScrollbar;
  private boolean horizontalScrollbar;
  private Element childRootElement;
  private Element labelTemplateElement;
  private Element listBoxPanelElement;
  private int labelTemplateHeight;
  private Properties parameter;
  private int displayItems;
  private ListBoxViewConverter<T> viewConverter = new ListBoxViewConverterSimple<T>();

  public void bind(
      final Nifty niftyParam,
      final Screen screenParam,
      final Element elementParam,
      final Properties parameterParam,
      final ControllerEventListener listener,
      final Attributes controlDefinitionAttributes) {
    super.bind(elementParam);
    nifty = niftyParam;
    screen = screenParam;
    parameter = parameterParam;
    verticalScrollbar = new Boolean(parameter.getProperty("vertical", "true"));
    horizontalScrollbar = new Boolean(parameter.getProperty("horizontal", "true"));
    displayItems = new Integer(parameter.getProperty("displayItems", "2"));
    childRootElement = getElement().findElementByName(controlDefinitionAttributes.get("childRootId"));
    labelTemplateElement = childRootElement.getElements().get(0);
    labelElements = new Element[displayItems];
    listBoxPanelElement = getElement().findElementByName("nifty-listbox-panel");

    initSelectionMode(listBoxImpl, parameter.getProperty("selectionMode", "Single"), parameter.getProperty("forceSelection", "false"));
    connectListBoxAndListBoxPanel();
    int itemCount = listBoxImpl.bindToView(this, displayItems);
    ensureVerticalScrollbar();
    initLabelTemplateData();
    createLabels();
    initializeScrollPanel(screen);
    calculateElementHeight(findHorizontalScrollbarHeight());
    initializeScrollElementHeight();
    initializeHorizontalScrollbar();
    updateScrollPanel(screen, labelTemplateHeight, itemCount);
    listBoxImpl.updateView(0);
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

  public void setFocus() {
    childRootElement.setFocus();
  }

  // ListBoxView Interface implementation

  @Override
  public void display(final List<T> visibleItems, final int focusElement, final List<Integer> selectedElements) {
    for (int i = 0; i < visibleItems.size(); i++) {
      T item = visibleItems.get(i);
      labelElements[i].setVisible(item != null);
      displayElement(i, item);
      setListBoxItemIndex(i);
      handleElementFocus(i, focusElement);
      handleElementSelection(i, item, selectedElements);
    }
  }

  @Override
  public void updateTotalCount(final int newCount) {
    updateScrollPanel(screen, labelTemplateHeight, newCount);
  }

  @Override
  public void scrollTo(final int newPosition) {
    VerticalScrollbarControl verticalS = getElement().findControl("nifty-internal-vertical-scrollbar", VerticalScrollbarControl.class);
    if (verticalS != null) {
      verticalS.setCurrentValueWithoutNotify(newPosition * labelTemplateHeight);
    }
  }

  @Override
  public void updateTotalWidth(final int newWidth) {
    HorizontalScrollbarControl horizontalS = getElement().findControl("nifty-internal-horizontal-scrollbar", HorizontalScrollbarControl.class);
    if (horizontalS != null) {
      horizontalS.setWorldMaxValue(newWidth);
      horizontalS.setCurrentValue(0);
    }

    // resize labels and panel to the new width or the minimal width
    int width = listBoxPanelElement.getWidth();
    SizeValue newWidthSizeValue = new SizeValue(Math.max(width, newWidth) + "px");
    for (Element element : labelElements) {
      element.setConstraintWidth(newWidthSizeValue);
    }
    childRootElement.setConstraintWidth(newWidthSizeValue);
  }

  @Override
  public int getWidth(final T item) {
    return viewConverter.getWidth(labelElements[0], item);
  }

  // ListBox Interface Implementation

  @Override
  public void changeSelectionMode(final SelectionMode listBoxSelectionMode, final boolean forceSelection) {
    listBoxImpl.changeSelectionMode(listBoxSelectionMode, forceSelection);
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
    if (getElement().getId() != null) {
      nifty.publishEvent(getElement().getId(), event);
    }
  }

  @Override
  public void addAllItems(final List<T> itemsToAdd) {
    listBoxImpl.addAllItems(itemsToAdd);
  }

  @Override
  public void removeAllItems(final List<T> itemsToRemove) {
    listBoxImpl.removeAllItems(itemsToRemove);
  }

  // internals 

  private void initSelectionMode(final ListBoxImpl<T> listBoxImpl, final String selectionMode, final String forceSelection) {
    SelectionMode listBoxSelectionMode = SelectionMode.Single;
    try {
      listBoxSelectionMode = SelectionMode.valueOf(selectionMode);
    } catch (RuntimeException e) {
      log.warning("Unsupported value for selectionMode [" + selectionMode + "]. Fall back to using single selection mode.");
    }

    listBoxImpl.changeSelectionMode(listBoxSelectionMode, "true".equalsIgnoreCase(forceSelection));
  }

  private void initializeScrollPanel(final Screen screen) {
    if (!horizontalScrollbar) {
      Element horizontal = getElement().findElementByName("nifty-internal-horizontal-scrollbar");
      if (horizontal != null) {
        nifty.removeElement(screen, horizontal);
      }
    }
    if (!verticalScrollbar) {
      Element vertical = getElement().findElementByName("nifty-internal-vertical-scrollbar");
      if (vertical != null) {
        nifty.removeElement(screen, vertical);
      }
    }

    Element scrollElement = getElement().findElementByName("nifty-listbox-child-root");
    scrollElement.setConstraintX(new SizeValue("0px"));
    scrollElement.setConstraintY(new SizeValue("0px"));

    nifty.executeEndOfFrameElementActions();
    screen.layoutLayers();
  }

  private void updateScrollPanel(final Screen screen, final float labelTemplateHeight, final int itemCount) {
    if (childRootElement != null) {
      VerticalScrollbarControl verticalS = getElement().findControl("nifty-internal-vertical-scrollbar", VerticalScrollbarControl.class);
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

  private void initializeHorizontalScrollbar() {
    HorizontalScrollbarControl horizontalS = getElement().findControl("nifty-internal-horizontal-scrollbar", HorizontalScrollbarControl.class);
    if (horizontalS != null) {
      horizontalS.setViewMaxValue(childRootElement.getWidth());
      horizontalS.setPerClickChange(1.0f);
      horizontalS.setScrollBarControlNotify(new ScrollbarControlNotify() {
        public void positionChanged(final float currentValue) {
          if (childRootElement != null) {
            childRootElement.setConstraintX(new SizeValue(-(int) currentValue + "px"));
            childRootElement.getParent().layoutElements();
          }
        }
      });
    }
  }

  @SuppressWarnings("unchecked")
  private void createLabels() {
    for (int i = 0; i < displayItems; i++) {
      ElementType templateType = labelTemplateElement.getElementType().copy();
      templateType.prepare(nifty, screen, screen.getRootElement().getElementType());
      labelElements[i] = templateType.create(childRootElement, nifty, screen, new LayoutPart());

      // connect it to this listbox
      ListBoxItemController<T> listBoxItemController = labelElements[i].getControl(ListBoxItemController.class);
      listBoxItemController.setListBox(listBoxImpl);
    }
  }

  private void initializeScrollElementHeight() {
    Element scrollElement = getElement().findElementByName("nifty-listbox-scrollpanel");
    scrollElement.setConstraintHeight(new SizeValue(displayItems * labelTemplateHeight + "px"));
  }

  private void ensureVerticalScrollbar() {
    if (displayItems == 1) {
      verticalScrollbar = false;
    }
  }

  private void calculateElementHeight(final int horizontalScrollbarElementHeight) {
    getElement().setConstraintHeight(new SizeValue(displayItems * labelTemplateHeight + horizontalScrollbarElementHeight + "px"));
  }

  private int findHorizontalScrollbarHeight() {
    int horizontalScrollbarElementHeight = 0;
    Element horizontalScrollbarElement = getElement().findElementByName("nifty-internal-horizontal-scrollbar");
    if (horizontalScrollbarElement != null) {
      horizontalScrollbarElementHeight = horizontalScrollbarElement.getHeight();
    }
    return horizontalScrollbarElementHeight;
  }

  private void initLabelTemplateData() {
    labelTemplateElement.getParent().layoutElements();
    labelTemplateHeight = labelTemplateElement.getHeight();
    nifty.removeElement(screen, labelTemplateElement);
  }

  @SuppressWarnings("unchecked")
  private void connectListBoxAndListBoxPanel() {
    ListBoxPanel<T> listBoxPanel = listBoxPanelElement.getControl(ListBoxPanel.class);
    listBoxPanel.setListBox(listBoxImpl);
  }

  private void displayElement(final int index, final T item) {
    viewConverter.display(labelElements[index], item);
  }

  private void handleElementSelection(final int index, final T item, final List<Integer> selectedElements) {
    if (item != null && selectedElements.contains(index)) {
      labelElements[index].startEffect(EffectEventId.onCustom, null, "select");
    } else {
      labelElements[index].resetSingleEffect(EffectEventId.onCustom, "select");
    }
  }

  @SuppressWarnings("unchecked")
  private void handleElementFocus(final int index, final int focusElement) {
    ListBoxPanel<T> listBoxPanel = getElement().findControl("nifty-listbox-panel", ListBoxPanel.class);
    if (listBoxPanel.hasFocus()) {
      if (focusElement == index) {
        labelElements[index].startEffect(EffectEventId.onCustom, null, "focus");
      } else {
        labelElements[index].resetSingleEffect(EffectEventId.onCustom, "focus");
      }
    } else {
      labelElements[index].resetSingleEffect(EffectEventId.onCustom, "focus");
    }
  }

  @SuppressWarnings("unchecked")
  private void setListBoxItemIndex(final int itemIndex) {
    ListBoxItemController<T> listBoxItemController = labelElements[itemIndex].getControl(ListBoxItemController.class);
    listBoxItemController.setItemIndex(itemIndex);
  }
}
