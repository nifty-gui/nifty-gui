package de.lessvoid.nifty.controls.listbox;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bushe.swing.event.EventTopicSubscriber;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyIdCreator;
import de.lessvoid.nifty.controls.AbstractController;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.ListBoxSelectionChangedEvent;
import de.lessvoid.nifty.controls.Scrollbar;
import de.lessvoid.nifty.controls.ScrollbarChangedEvent;
import de.lessvoid.nifty.controls.dynamic.CustomControlCreator;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.events.ElementShowEvent;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.input.NiftyMouseInputEvent;
import de.lessvoid.nifty.loaderv2.types.ControlType;
import de.lessvoid.nifty.loaderv2.types.ElementType;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.xml.xpp3.Attributes;

/**
 * @deprecated Please use {@link de.lessvoid.nifty.controls.ListBox} when accessing NiftyControls.
 */
@Deprecated
public class ListBoxControl<T> extends AbstractController implements ListBox<T>, ListBoxView<T> {
  private Logger log = Logger.getLogger(ListBoxControl.class.getName());
  private ListBoxImpl<T> listBoxImpl = new ListBoxImpl<T>(this);
  private Element[] labelElements;
  private Nifty nifty;
  private Screen screen;
  private ScrollbarMode verticalScrollbar;
  private Element verticalScrollbarTemplate;
  private Element scrollElement;
  private ScrollbarMode horizontalScrollbar;
  private Element horizontalScrollbarTemplate;
  private Element childRootElement;
  private ElementType labelTemplateElementType;
  private Element listBoxPanelElement;
  private Element bottomRightTemplate;
  private int labelTemplateHeight;
  private Properties parameter;
  private int displayItems;
  private ListBoxViewConverter<T> viewConverter;
  private EventTopicSubscriber<ScrollbarChangedEvent> verticalScrollbarSubscriber = new EventTopicSubscriber<ScrollbarChangedEvent>() {
    @Override
    public void onEvent(final String id, final ScrollbarChangedEvent event) {
      listBoxImpl.updateView((int) (event.getValue() / labelTemplateHeight));
    }
  };
  private EventTopicSubscriber<ScrollbarChangedEvent> horizontalScrollbarSubscriber = new EventTopicSubscriber<ScrollbarChangedEvent>() {
    @Override
    public void onEvent(final String id, final ScrollbarChangedEvent event) {
      if (childRootElement != null) {
        childRootElement.setConstraintX(new SizeValue(-(int) event.getValue() + "px"));
        childRootElement.getParent().layoutElements();
      }
    }
  };
  private EventTopicSubscriber<ElementShowEvent> listBoxControlShowEventSubscriber = new EventTopicSubscriber<ElementShowEvent>() {
    @Override
    public void onEvent(final String id, final ElementShowEvent event) {
      listBoxImpl.updateView();
    }
  };
  private int lastMaxWidth;
  private int applyWidthConstraintsLastWidth = -1;

  private final List<ListBoxItemProcessor> itemProcessors = new ArrayList<ListBoxItemProcessor>();

  public ListBoxControl() {
    itemProcessors.add(new ListBoxItemProcessor() {
      @Override
      public void processElement(final Element element) {
        final ListBoxItemController<T> listBoxItemController = element.getControl(ListBoxItemController.class);
        if (listBoxItemController != null) {
          listBoxItemController.setListBox(listBoxImpl);
        }
      }
    });
  }

  @Override
  public void bind(
      final Nifty niftyParam,
      final Screen screenParam,
      final Element elementParam,
      final Properties parameterParam,
      final Attributes controlDefinitionAttributes) {
    super.bind(elementParam);

    nifty = niftyParam;
    screen = screenParam;
    parameter = parameterParam;
    viewConverter = createViewConverter(parameter.getProperty("viewConverterClass", ListBoxViewConverterSimple.class.getName()));
    verticalScrollbar = getScrollbarMode("vertical");
    verticalScrollbarTemplate = getElement().findElementByName("#vertical-scrollbar");
    horizontalScrollbar = getScrollbarMode("horizontal");
    horizontalScrollbarTemplate = getElement().findElementByName("#horizontal-scrollbar-parent");
    bottomRightTemplate = getElement().findElementByName("#bottom-right");
    displayItems = new Integer(parameter.getProperty("displayItems", "2"));
    scrollElement = getElement().findElementByName("#scrollpanel");
    applyWidthConstraintsLastWidth = -1;

    childRootElement = getElement().findElementByName("#child-root");
    if (!childRootElement.getElements().isEmpty()) {
      final Element templateElement = childRootElement.getElements().get(0);
      templateElement.getParent().layoutElements();
      labelTemplateHeight = templateElement.getHeight();
      labelTemplateElementType = templateElement.getElementType().copy();
      nifty.removeElement(screen, templateElement);
    }
    labelElements = new Element[displayItems];
    listBoxPanelElement = getElement().findElementByName("#panel");

    initSelectionMode(listBoxImpl, parameter.getProperty("selectionMode", "Single"), parameter.getProperty("forceSelection", "false"));
    connectListBoxAndListBoxPanel();
    listBoxImpl.bindToView(this, displayItems);
    lastMaxWidth = childRootElement.getWidth();
    ensureVerticalScrollbar();
    createLabels();
    initializeScrollPanel(screen);
    initializeScrollElementHeight();
    listBoxImpl.updateView(0);
    initializeHorizontalScrollbar();
    initializeVerticalScrollbar(screen, labelTemplateHeight, 0);
  }

  @Override
  public void init(final Properties parameter, final Attributes controlDefinitionAttributes) {
    listBoxImpl.updateViewTotalCount();
    listBoxImpl.updateViewScroll();

    subscribeHorizontalScrollbar();
    subscribeVerticalScrollbar();
    nifty.subscribe(screen, getId(), ElementShowEvent.class, listBoxControlShowEventSubscriber);

    super.init(parameter, controlDefinitionAttributes);
  }

  @Override
  public void onStartScreen() {
  }

  public void mouseWheel(final Element e, final NiftyMouseInputEvent inputEvent) {
    int mouseWheel = inputEvent.getMouseWheel();
    Scrollbar scrollbar = getVerticalScrollbar();
    if (scrollbar != null) {
      float currentValue = scrollbar.getValue();
      if (mouseWheel < 0) {
        scrollbar.setValue(currentValue - scrollbar.getButtonStepSize() * mouseWheel);
      } else if (mouseWheel > 0) {
        scrollbar.setValue(currentValue - scrollbar.getButtonStepSize() * mouseWheel);
      }
    }
  }

  private void subscribeVerticalScrollbar() {
    Element scrollbar = getElement().findElementByName("#vertical-scrollbar");
    if (scrollbar != null) {
      nifty.subscribe(screen, scrollbar.getId(), ScrollbarChangedEvent.class, verticalScrollbarSubscriber);
    }
  }

  private void subscribeHorizontalScrollbar() {
    Element scrollbar = getElement().findElementByName("#horizontal-scrollbar");
    if (scrollbar != null) {
      nifty.subscribe(screen, scrollbar.getId(), ScrollbarChangedEvent.class, horizontalScrollbarSubscriber);
    }
  }

  private void unsubscribeVerticalScrollbar() {
    Element scrollbar = getElement().findElementByName("#vertical-scrollbar");
    if (scrollbar != null) {
      nifty.unsubscribe(scrollbar.getId(), verticalScrollbarSubscriber);
    }
  }

  private void unsubscribeHorizontalScrollbar() {
    Element scrollbar = getElement().findElementByName("#horizontal-scrollbar");
    if (scrollbar != null) {
      nifty.unsubscribe(scrollbar.getId(), horizontalScrollbarSubscriber);
    }
  }

  @Override
  public boolean inputEvent(final NiftyInputEvent inputEvent) {
    return false;
  }

  @Override
  public void onFocus(final boolean getFocus) {
    super.onFocus(getFocus);
  }

  @Override
  public void setFocus() {
    childRootElement.setFocus();
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  private ListBoxViewConverter<T> createViewConverter(final String className) {
    try {
      return (ListBoxViewConverter<T>) Class.forName(className).newInstance();
    } catch (Exception e) {
      log.log(Level.WARNING, "Unable to instantiate given class [" + className + "] with error: " + e.getMessage(), e);
      return new ListBoxViewConverterSimple();
    }
  }

  public ListBoxViewConverter<T> getViewConverter() {
    return viewConverter;
  }

  // ListBoxView Interface implementation

  @Override
  public void display(final List<T> visibleItems, final int focusElement, final List<Integer> selectedElements) {
    ensureWidthConstraints();
    for (int i = 0; i < visibleItems.size(); i++) {
      T item = visibleItems.get(i);
      if (labelElements[i] != null) {
        labelElements[i].setVisible(item != null && getElement().isVisible());
        if (item != null) {
          displayElement(i, item);
          setListBoxItemIndex(i);
          handleElementFocus(i, focusElement);
          handleElementSelection(i, item, selectedElements);
        }
      }
    }
  }

  @Override
  public void updateTotalCount(final int newCount) {
    if (verticalScrollbar == ScrollbarMode.optional) {
      Element vertical = getElement().findElementByName("#vertical-scrollbar");
      if (newCount > displayItems) {
        if (vertical == null) {
          ElementType templateType = verticalScrollbarTemplate.getElementType().copy();
          CustomControlCreator create = new CustomControlCreator((ControlType) templateType);
          Element e = create.create(nifty, screen, scrollElement);
          if (e.getHeight() < 23*2) { // ugly
            nifty.removeElement(screen, e);
            return;
          }
          subscribeVerticalScrollbar();
          ensureWidthConstraints();

          updateBottomRightElement();
          nifty.executeEndOfFrameElementActions();
          getElement().getParent().layoutElements();
        }
      } else if (newCount <= displayItems) {
        if (vertical != null) {
          unsubscribeVerticalScrollbar();
          nifty.removeElement(screen, vertical);
          nifty.executeEndOfFrameElementActions();
          ensureWidthConstraints();

          updateBottomRightElement();
          nifty.executeEndOfFrameElementActions();
          getElement().getParent().layoutElements();
        }
      }
    }
    initializeVerticalScrollbar(screen, labelTemplateHeight, newCount);
  }

  private void applyIdPrefixToElementType(final String prefix, final ElementType type) {
    type.getAttributes().set("id", prefix + type.getAttributes().get("id"));

    for (final ElementType child : type.getElements()) {
      applyIdPrefixToElementType(prefix, child);
    }
  }

  @Override
  public void updateTotalWidth(final int newWidth) {
    this.lastMaxWidth = newWidth;
    if (horizontalScrollbar == ScrollbarMode.optional) {
      Element horizontal = getElement().findElementByName("#horizontal-scrollbar-parent");
      if (newWidth > listBoxPanelElement.getWidth()) {
        if (horizontal == null) {
          final ElementType type = horizontalScrollbarTemplate.getElementType().copy();
          applyIdPrefixToElementType(getElement().getId(), type);
          nifty.createElementFromType(screen, getElement(), type);

          updateBottomRightElement();
          nifty.executeEndOfFrameElementActions();
          subscribeHorizontalScrollbar();
        }
      } else if (newWidth <= listBoxPanelElement.getWidth()) {
        if (horizontal != null) {
          unsubscribeHorizontalScrollbar();
          nifty.removeElement(screen, horizontal);
          nifty.executeEndOfFrameElementActions();
        }
      }
    }    
    initializeHorizontalScrollbar();
    ensureWidthConstraints();
    getElement().getParent().layoutElements();
  }

  public void ensureWidthConstraints() {
    applyWidthConstraints(Math.max(lastMaxWidth, listBoxPanelElement.getWidth()));
  }

  @Override
  public void layoutCallback() {
    if (listBoxPanelElement == null) {
      return;
    }
    ensureWidthConstraints();
    initializeHorizontalScrollbar();
  }

  private void applyWidthConstraints(final int width) {
    if (applyWidthConstraintsLastWidth  == width) {
      return;
    }

    applyWidthConstraintsLastWidth = width;
    SizeValue newWidthSizeValue = new SizeValue(width + "px");
    for (Element element : labelElements) {
      if (element != null) {
        element.setConstraintWidth(newWidthSizeValue);
      }
    }
    childRootElement.setConstraintWidth(newWidthSizeValue);
    getElement().layoutElements();
  }

  @Override
  public void scrollTo(final int newPosition) {
    Scrollbar verticalS = getVerticalScrollbar();
    if (verticalS != null) {
      verticalS.setValue(newPosition * labelTemplateHeight);
    }
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
  public void selectNext() {
    listBoxImpl.selectNext();
  }

  @Override
  public void selectPrevious() {
    listBoxImpl.selectPrevious();
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
  public List<Integer> getSelectedIndices() {
    return listBoxImpl.getSelectedIndices();
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

  @Override
  public void sortAllItems() {
    listBoxImpl.sortItems(null);
  }

  @Override
  public void sortAllItems(final Comparator<T> comperator) {
    listBoxImpl.sortItems(comperator);
  }

  @Override
  public int getDisplayItemCount() {
    return displayItems;
  }

  @Override
  public void refresh() {
    listBoxImpl.updateView();
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
    if (horizontalScrollbar == ScrollbarMode.off || horizontalScrollbar == ScrollbarMode.optional) {
      Element horizontal = getElement().findElementByName("#horizontal-scrollbar-parent");
      if (horizontal != null) {
        nifty.removeElement(screen, horizontal);
      }
    }
    if (verticalScrollbar == ScrollbarMode.off || verticalScrollbar == ScrollbarMode.optional) {
      Element vertical = getElement().findElementByName("#vertical-scrollbar");
      if (vertical != null) {
        nifty.removeElement(screen, vertical);
      }
    }

    childRootElement.setConstraintX(new SizeValue("0px"));
    childRootElement.setConstraintY(new SizeValue("0px"));

    updateBottomRightElement();

    nifty.executeEndOfFrameElementActions();
    getElement().getParent().layoutElements();
  }

  private void updateBottomRightElement() {
    Element horizontal = getElement().findElementByName("#horizontal-scrollbar-parent");
    Element vertical = getElement().findElementByName("#vertical-scrollbar");
    Element bottomRight = getElement().findElementByName("#bottom-right");
    if (horizontal != null) {
      if (vertical == null) {
        if (bottomRight != null) {
          nifty.removeElement(screen, bottomRight);
          nifty.executeEndOfFrameElementActions();
          initializeHorizontalScrollbar();
        }
      } else {
        if (bottomRight == null) {
          nifty.createElementFromType(screen, horizontal, bottomRightTemplate.getElementType());
          initializeHorizontalScrollbar();
        }
      }
    }
  }

  private void initializeHorizontalScrollbar() {
    Scrollbar horizontalS = getHorizontalScrollbar();
    if (horizontalS != null && horizontalS.isBound()) {
      horizontalS.setWorldMax(lastMaxWidth);
      horizontalS.setWorldPageSize(listBoxPanelElement.getWidth());
    }
  }

  private void initializeVerticalScrollbar(final Screen screen, final float labelTemplateHeight, final int itemCount) {
    Scrollbar verticalS = getVerticalScrollbar();
    if (verticalS != null && verticalS.isBound()) {
      verticalS.setWorldMax(itemCount * labelTemplateHeight);
      verticalS.setWorldPageSize(displayItems * labelTemplateHeight);
      verticalS.setButtonStepSize(labelTemplateHeight);
    }
  }

  @Override
  public void addItemProcessor(final ListBoxItemProcessor processor) {
    itemProcessors.add(processor);
  }

  private void createLabels() {
    if (labelTemplateElementType == null) {
      return;
    }
    for (final Element e : childRootElement.getElements()) {
      nifty.removeElement(screen, e);
    }    
    for (int i = 0; i < displayItems; i++) {
      ElementType templateType = labelTemplateElementType.copy();

      String oldId = templateType.getAttributes().get("id");
      if (oldId == null) {
        oldId = getElement().findElementByName("#child-root").getId();
      }
      final String newId = oldId + "#" + NiftyIdCreator.generate();

      templateType.getAttributes().set("id", newId);
      replaceAllIds(templateType, oldId, newId);

      labelElements[i] = nifty.createElementFromType(screen, childRootElement, templateType);

      for (final ListBoxItemProcessor processor : itemProcessors) {
        processor.processElement(labelElements[i]);
      }
    }
  }

  private void replaceAllIds(final ElementType type, final String oldId, final String newId) {
    final Collection<ElementType> children = type.getElements();
    for (final ElementType child : children) {
      final String id = child.getAttributes().get("id");
      if (id != null) {
        child.getAttributes().set("id", id.replace(oldId, newId));
      }
      replaceAllIds(child, oldId, newId);
    }
  }

  private void initializeScrollElementHeight() {
    scrollElement.setConstraintHeight(new SizeValue(displayItems * labelTemplateHeight + "px"));
  }

  private void ensureVerticalScrollbar() {
    if (displayItems == 1) {
      verticalScrollbar = ScrollbarMode.off;
    }
  }

  private int findHorizontalScrollbarHeight() {
    int horizontalScrollbarElementHeight = 0;
    Element horizontalScrollbarElement = getElement().findElementByName("#horizontal-scrollbar");
    if (horizontalScrollbarElement != null) {
      horizontalScrollbarElementHeight = horizontalScrollbarElement.getHeight();
    }
    return horizontalScrollbarElementHeight;
  }

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

  private void handleElementFocus(final int index, final int focusElement) {
    ListBoxPanel<T> listBoxPanel = listBoxPanelElement.getControl(ListBoxPanel.class);
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

  private void setListBoxItemIndex(final int itemIndex) {
    ListBoxItemController<T> listBoxItemController = labelElements[itemIndex].getControl(ListBoxItemController.class);
    if (listBoxItemController != null) {
      listBoxItemController.setItemIndex(itemIndex);
    }
  }

  private Scrollbar getVerticalScrollbar() {
    return getScrollbar("#vertical-scrollbar");
  }

  private Scrollbar getHorizontalScrollbar() {
    return getScrollbar("#horizontal-scrollbar");
  }

  private Scrollbar getScrollbar(final String id) {
    return getElement().findNiftyControl(id, Scrollbar.class);
  }

  private ScrollbarMode getScrollbarMode(final String key) {
    try {
      return ScrollbarMode.valueOf(parameter.getProperty(key, ScrollbarMode.on.toString()));
    } catch (Exception e) {
      log.warning("unknown scrollbar mode [" + key + "] falling back to 'on'");
      return ScrollbarMode.on;
    }
  }

  private enum ScrollbarMode {
    off,
    on,
    optional
  }
}
