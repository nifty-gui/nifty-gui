package de.lessvoid.nifty.controls.listbox;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyIdCreator;
import de.lessvoid.nifty.controls.*;
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
import org.bushe.swing.event.EventTopicSubscriber;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @deprecated Please use {@link de.lessvoid.nifty.controls.ListBox} when accessing NiftyControls.
 */
@Deprecated
public class ListBoxControl<T> extends AbstractController implements ListBox<T>, ListBoxView<T> {
  @Nonnull
  private final Logger log = Logger.getLogger(ListBoxControl.class.getName());
  @Nonnull
  private final ListBoxImpl<T> listBoxImpl;
  @Nullable
  private Element[] labelElements;
  @Nullable
  private Nifty nifty;
  @Nullable
  private Screen screen;
  @Nonnull
  private ScrollbarMode verticalScrollbar;
  @Nullable
  private Element verticalScrollbarTemplate;
  @Nullable
  private Element scrollElement;
  @Nonnull
  private ScrollbarMode horizontalScrollbar;
  @Nullable
  private Element horizontalScrollbarTemplate;
  @Nullable
  private Element childRootElement;
  @Nullable
  private ElementType labelTemplateElementType;
  @Nullable
  private Element listBoxPanelElement;
  @Nullable
  private Element bottomRightTemplate;
  private int labelTemplateHeight;
  private int displayItems;
  @Nullable
  private ListBoxViewConverter<T> viewConverter;
  @Nonnull
  private final EventTopicSubscriber<ScrollbarChangedEvent> verticalScrollbarSubscriber = new
      EventTopicSubscriber<ScrollbarChangedEvent>() {
        @Override
        public void onEvent(final String id, @Nonnull final ScrollbarChangedEvent event) {
          listBoxImpl.updateView((int) (event.getValue() / labelTemplateHeight));
        }
      };
  @Nonnull
  private final EventTopicSubscriber<ScrollbarChangedEvent> horizontalScrollbarSubscriber = new
      EventTopicSubscriber<ScrollbarChangedEvent>() {
        @Override
        public void onEvent(final String id, @Nonnull final ScrollbarChangedEvent event) {
          if (childRootElement != null) {
            childRootElement.setConstraintX(SizeValue.px(-(int) event.getValue()));
            childRootElement.getParent().layoutElements();
          }
        }
      };
  @Nonnull
  private final EventTopicSubscriber<ElementShowEvent> listBoxControlShowEventSubscriber = new
      EventTopicSubscriber<ElementShowEvent>() {
        @Override
        public void onEvent(final String id, final ElementShowEvent event) {
          listBoxImpl.updateView();
        }
      };
  private int lastMaxWidth;
  private int applyWidthConstraintsLastWidth = -1;

  @Nonnull
  private final List<ListBoxItemProcessor> itemProcessors;

  public ListBoxControl() {
    listBoxImpl = new ListBoxImpl<T>(this);
    itemProcessors = new ArrayList<ListBoxItemProcessor>();
    itemProcessors.add(new ListBoxItemProcessor() {
      @Override
      public void processElement(@Nonnull final Element element) {
        @SuppressWarnings("unchecked")
        final ListBoxItemController<T> listBoxItemController = element.getControl(ListBoxItemController.class);
        if (listBoxItemController != null) {
          listBoxItemController.setListBox(listBoxImpl);
        }
      }
    });

    horizontalScrollbar = ScrollbarMode.on;
    verticalScrollbar = ScrollbarMode.on;
  }

  @Override
  public void bind(
      @Nonnull final Nifty nifty,
      @Nonnull final Screen screen,
      @Nonnull final Element element,
      @Nonnull final Parameters parameter) {
    bind(element);

    this.nifty = nifty;
    this.screen = screen;
    String viewConverterClass = parameter.get("viewConverterClass");
    if (viewConverterClass == null) {
      viewConverter = new ListBoxViewConverterSimple<T>();
    } else {
      viewConverter = createViewConverter(viewConverterClass);
    }
    verticalScrollbarTemplate = element.findElementById("#vertical-scrollbar");
    if (verticalScrollbarTemplate == null) {
      log.severe("Failed to locate vertical scrollbar. Scrollbar disabled. Looked for: #vertical-scrollbar");
      verticalScrollbar = ScrollbarMode.off;
    } else {
      verticalScrollbar = parameter.getAsEnum("vertical", ScrollbarMode.class, ScrollbarMode.on);
    }

    horizontalScrollbarTemplate = element.findElementById("#horizontal-scrollbar-parent");
    if (horizontalScrollbarTemplate == null) {
      log.severe("Failed to locate horizontal scrollbar. Scrollbar disabled. Looked for: #horizontal-scrollbar-parent");
      horizontalScrollbar = ScrollbarMode.off;
    } else {
      horizontalScrollbar = parameter.getAsEnum("horizontal", ScrollbarMode.class, ScrollbarMode.on);
    }

    bottomRightTemplate = element.findElementById("#bottom-right");
    scrollElement = element.findElementById("#scrollpanel");

    if (bottomRightTemplate == null) {
      log.severe("Failed to locate bottom right spacer. Scrollbars will not display properly. Looked for: " +
          "#bottom-right");
    }
    if (scrollElement == null) {
      log.severe("Failed to locate scroll panel. Scrolling will not work properly. Looked for: #scrollpanel");
    }

    displayItems = parameter.getAsInteger("displayItems", 2);
    if (displayItems < 1) {
      log.warning(displayItems + " items to display?! Really? Falling back to 2.");
      displayItems = 2;
    }
    applyWidthConstraintsLastWidth = -1;

    childRootElement = element.findElementById("#child-root");
    if (childRootElement == null) {
      log.severe("Failed to locate child root element. Displaying will not work properly. Looked for: #child-root");
    } else {
      if (!childRootElement.getChildren().isEmpty()) {
        final Element templateElement = childRootElement.getChildren().get(0);
        childRootElement.layoutElements();
        labelTemplateHeight = templateElement.getHeight();
        labelTemplateElementType = templateElement.getElementType().copy();
        nifty.removeElement(screen, templateElement);
      }
    }
    listBoxPanelElement = element.findElementById("#panel");

    if (listBoxPanelElement == null) {
      log.severe("Failed to locate list box panel element. List box will not work properly. Looked for: #panel");
    }

    initSelectionMode(listBoxImpl, parameter.getWithDefault("selectionMode", "Single"),
        parameter.getWithDefault("forceSelection", "false"));
    connectListBoxAndListBoxPanel();
    listBoxImpl.bindToView(this, displayItems);
    lastMaxWidth = childRootElement.getWidth();
    ensureVerticalScrollbar();
    createLabels();
    initializeScrollPanel(screen);
    initializeScrollElementHeight();
    listBoxImpl.updateView(0);
    initializeHorizontalScrollbar();
    initializeVerticalScrollbar(labelTemplateHeight, 0);
  }

  @Override
  public void init(@Nonnull final Parameters parameter) {
    super.init(parameter);

    if (nifty == null || screen == null) {
      log.severe("Init of controller called before binding was done.");
      return;
    }

    listBoxImpl.updateViewTotalCount();
    listBoxImpl.updateViewScroll();

    subscribeHorizontalScrollbar();
    subscribeVerticalScrollbar();

    String id = getId();
    if (id == null) {
      log.warning("ListBox has no ID. Functionality will be limited.");
    } else {
      nifty.subscribe(screen, getId(), ElementShowEvent.class, listBoxControlShowEventSubscriber);
    }
  }

  @Override
  public void onStartScreen() {
  }

  public void mouseWheel(@Nonnull final Element e, @Nonnull final NiftyMouseInputEvent inputEvent) {
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

  @Nullable
  private String getChildId(@Nonnull final String id) {
    Element element = getElement();
    if (element == null) {
      return null;
    }
    Element child = element.findElementById(id);
    if (child == null) {
      return null;
    }
    return child.getId();
  }

  private void subscribeVerticalScrollbar() {
    if (nifty == null || screen == null) {
      log.severe("Subscribing scrollbar before binding is done.");
      return;
    }
    String id = getChildId("#vertical-scrollbar");
    if (id != null) {
      nifty.subscribe(screen, id, ScrollbarChangedEvent.class, verticalScrollbarSubscriber);
    }
  }

  private void subscribeHorizontalScrollbar() {
    if (nifty == null || screen == null) {
      log.severe("Subscribing scrollbar before binding is done.");
      return;
    }

    String id = getChildId("#horizontal-scrollbar");
    if (id != null) {
      nifty.subscribe(screen, id, ScrollbarChangedEvent.class, horizontalScrollbarSubscriber);
    }
  }

  private void unsubscribeVerticalScrollbar() {
    if (nifty == null) {
      log.severe("Unsubscribing scrollbar before binding is done.");
      return;
    }
    String id = getChildId("#vertical-scrollbar");
    if (id != null) {
      nifty.unsubscribe(id, verticalScrollbarSubscriber);
    }
  }

  private void unsubscribeHorizontalScrollbar() {
    if (nifty == null) {
      log.severe("Unsubscribing scrollbar before binding is done.");
      return;
    }
    String id = getChildId("#horizontal-scrollbar");
    if (id != null) {
      nifty.unsubscribe(id, horizontalScrollbarSubscriber);
    }
  }

  @Override
  public boolean inputEvent(@Nonnull final NiftyInputEvent inputEvent) {
    return false;
  }

  @Override
  public void onFocus(final boolean getFocus) {
    super.onFocus(getFocus);
  }

  @Override
  public void setFocus() {
    if (childRootElement != null) {
      childRootElement.setFocus();
    }
  }

  @Nonnull
  @SuppressWarnings({ "unchecked", "rawtypes" })
  private ListBoxViewConverter<T> createViewConverter(@Nonnull final String className) {
    try {
      return (ListBoxViewConverter<T>) Class.forName(className).newInstance();
    } catch (Exception e) {
      log.log(Level.WARNING, "Unable to instantiate given class [" + className + "] with error: " + e.getMessage(), e);
      return new ListBoxViewConverterSimple();
    }
  }

  @Nullable
  public ListBoxViewConverter<T> getViewConverter() {
    return viewConverter;
  }

  // ListBoxView Interface implementation

  @Override
  public void display(
      @Nonnull final List<T> visibleItems,
      final int focusElement,
      @Nonnull final List<Integer> selectedElements) {
    ensureWidthConstraints();
    if (labelElements == null) {
      log.warning("Can't display anything. Control binding is not done yet.");
    } else {
      int count = Math.min(visibleItems.size(), labelElements.length);
      if (visibleItems.size() > count) {
        log.warning("Trying to show more elements in list then there are display labels.");
      }
      Element element = getElement();
      for (int i = 0; i < count; i++) {
        @Nonnull T item = visibleItems.get(i);
        if (labelElements[i] != null) {
          labelElements[i].setVisible(element != null && element.isVisible());
          displayElement(i, item);
          setListBoxItemIndex(i);
          handleElementFocus(i, focusElement);
          handleElementSelection(i, item, selectedElements);
        }
      }
      if (count < labelElements.length) {
        for (int i = count; i < labelElements.length; i++) {
          if (labelElements[i] != null) {
            labelElements[i].setVisible(false);
          }
        }
      }
    }
  }

  @Override
  public void updateTotalCount(final int newCount) {
    if (nifty == null || screen == null || scrollElement == null || verticalScrollbarTemplate == null) {
      log.severe("Can't update the total count as long as the control is not bound.");
      return;
    }
    if (verticalScrollbar == ScrollbarMode.optional) {
      Element element = getElement();
      if (element == null) {
        return;
      }
      Element vertical = element.findElementById("#vertical-scrollbar");
      if (newCount > displayItems) {
        if (vertical == null) {
          ElementType templateType = verticalScrollbarTemplate.getElementType().copy();
          CustomControlCreator create = new CustomControlCreator((ControlType) templateType);
          Element e = create.create(nifty, screen, scrollElement);
          if (e.getHeight() < 23 * 2) { // ugly
            nifty.removeElement(screen, e);
            return;
          }
          subscribeVerticalScrollbar();
          ensureWidthConstraints();

          updateBottomRightElement();
          nifty.executeEndOfFrameElementActions();
          layoutSilently(element.getParent());
        }
      } else if (newCount <= displayItems) {
        if (vertical != null) {
          unsubscribeVerticalScrollbar();
          nifty.removeElement(screen, vertical);
          nifty.executeEndOfFrameElementActions();
          ensureWidthConstraints();

          updateBottomRightElement();
          nifty.executeEndOfFrameElementActions();
          layoutSilently(element.getParent());
        }
      }
    }
    initializeVerticalScrollbar(labelTemplateHeight, newCount);
  }

  private static void layoutSilently(@Nullable Element element) {
    if (element != null) {
      element.layoutElements();
    }
  }

  private void applyIdPrefixToElementType(@Nonnull final String prefix, @Nonnull final ElementType type) {
    type.getAttributes().set("id", prefix + type.getAttributes().get("id"));

    for (final ElementType child : type.getElements()) {
      applyIdPrefixToElementType(prefix, child);
    }
  }

  @Override
  public void updateTotalWidth(final int newWidth) {
    this.lastMaxWidth = newWidth;
    if (nifty == null || screen == null || listBoxPanelElement == null || horizontalScrollbarTemplate == null) {
      log.severe("Can't update the total count as long as the control is not bound.");
      return;
    }
    Element element = getElement();
    if (element == null) {
      return;
    }
    if (horizontalScrollbar == ScrollbarMode.optional) {
      Element horizontal = element.findElementById("#horizontal-scrollbar-parent");
      if (newWidth > listBoxPanelElement.getWidth()) {
        if (horizontal == null) {
          final ElementType type = horizontalScrollbarTemplate.getElementType().copy();
          String id = getId();
          if (id != null) {
            applyIdPrefixToElementType(id, type);
          }
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
    layoutSilently(element.getParent());
  }

  public void ensureWidthConstraints() {
    if (listBoxPanelElement != null) {
      applyWidthConstraints(Math.max(lastMaxWidth, listBoxPanelElement.getWidth()));
    }
  }

  @Override
  public void layoutCallback() {
    ensureWidthConstraints();
    initializeHorizontalScrollbar();
  }

  private void applyWidthConstraints(final int width) {
    if (applyWidthConstraintsLastWidth == width) {
      return;
    }

    applyWidthConstraintsLastWidth = width;
    SizeValue newWidthSizeValue = SizeValue.px(width);
    if (labelElements != null) {
      for (int i = 0; i < labelElements.length; i++) {
        Element element = labelElements[i];
        if (element != null) {
          element.setConstraintWidth(newWidthSizeValue);
        }
      }
    }
    if (childRootElement != null) {
      childRootElement.setConstraintWidth(newWidthSizeValue);
    }
    layoutSilently(getElement());
  }

  @Override
  public void scrollTo(final int newPosition) {
    Scrollbar verticalS = getVerticalScrollbar();
    if (verticalS != null) {
      verticalS.setValue(newPosition * labelTemplateHeight);
    }
  }

  @Override
  public int getWidth(@Nonnull final T item) {
    if (viewConverter == null | labelElements == null || labelElements[0] == null) {
      return 0;
    }
    return viewConverter.getWidth(labelElements[0], item);
  }

  // ListBox Interface Implementation

  @Override
  public void changeSelectionMode(@Nonnull final SelectionMode listBoxSelectionMode, final boolean forceSelection) {
    listBoxImpl.changeSelectionMode(listBoxSelectionMode, forceSelection);
  }

  @Override
  public void addItem(@Nonnull final T newItem) {
    listBoxImpl.addItem(newItem);
  }

  @Override
  public void insertItem(@Nonnull final T item, final int index) {
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
  public void selectItem(@Nonnull final T item) {
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
  public void deselectItem(@Nonnull final T item) {
    listBoxImpl.deselectItem(item);
  }

  @Nonnull
  @Override
  public List<T> getSelection() {
    return listBoxImpl.getSelection();
  }

  @Nonnull
  @Override
  public List<Integer> getSelectedIndices() {
    return listBoxImpl.getSelectedIndices();
  }

  @Override
  public void removeItemByIndex(final int itemIndex) {
    listBoxImpl.removeItemByIndex(itemIndex);
  }

  @Override
  public void removeItem(@Nonnull final T item) {
    listBoxImpl.removeItem(item);
  }

  @Nonnull
  @Override
  public List<T> getItems() {
    return listBoxImpl.getItems();
  }

  @Override
  public void showItem(@Nonnull final T item) {
    listBoxImpl.showItem(item);
  }

  @Override
  public void showItemByIndex(final int itemIndex) {
    listBoxImpl.showItemByIndex(itemIndex);
  }

  @Override
  public void setFocusItem(@Nullable final T item) {
    listBoxImpl.setFocusItem(item);
  }

  @Override
  public void setFocusItemByIndex(final int itemIndex) {
    listBoxImpl.setFocusItemByIndex(itemIndex);
  }

  @Nullable
  @Override
  public T getFocusItem() {
    return listBoxImpl.getFocusItem();
  }

  @Override
  public int getFocusItemIndex() {
    return listBoxImpl.getFocusItemIndex();
  }

  @Override
  public void setListBoxViewConverter(@Nonnull final ListBoxViewConverter<T> viewConverter) {
    this.viewConverter = viewConverter;
  }

  @Override
  public void publish(@Nonnull final ListBoxSelectionChangedEvent<T> event) {
    if (nifty != null) {
      String id = getId();
      if (id != null) {
        nifty.publishEvent(id, event);
      }
    }
  }

  @Override
  public void addAllItems(@Nonnull final List<T> itemsToAdd) {
    listBoxImpl.addAllItems(itemsToAdd);
  }

  @Override
  public void removeAllItems(@Nonnull final List<T> itemsToRemove) {
    listBoxImpl.removeAllItems(itemsToRemove);
  }

  @Override
  public void sortAllItems() {
    listBoxImpl.sortItems(null);
  }

  @Override
  public void sortAllItems(@Nullable final Comparator<T> comparator) {
    listBoxImpl.sortItems(comparator);
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

  private void initSelectionMode(
      @Nonnull final ListBoxImpl<T> listBoxImpl,
      @Nonnull final String selectionMode,
      @Nonnull final String forceSelection) {
    SelectionMode listBoxSelectionMode = SelectionMode.Single;
    try {
      listBoxSelectionMode = SelectionMode.valueOf(selectionMode);
    } catch (RuntimeException e) {
      log.warning("Unsupported value for selectionMode [" + selectionMode + "]. Fall back to using single selection " +
          "mode.");
    }

    listBoxImpl.changeSelectionMode(listBoxSelectionMode, "true".equalsIgnoreCase(forceSelection));
  }

  private void initializeScrollPanel(@Nonnull final Screen screen) {
    if (nifty == null) {
      log.severe("Can't init the scroll panel as long as the controller is not properly bound.");
      return;
    }
    Element element = getElement();
    if (element == null) {
      return;
    }
    if (horizontalScrollbar == ScrollbarMode.off || horizontalScrollbar == ScrollbarMode.optional) {
      Element horizontal = element.findElementById("#horizontal-scrollbar-parent");
      if (horizontal != null) {
        nifty.removeElement(screen, horizontal);
      }
    }
    if (verticalScrollbar == ScrollbarMode.off || verticalScrollbar == ScrollbarMode.optional) {
      Element vertical = element.findElementById("#vertical-scrollbar");
      if (vertical != null) {
        nifty.removeElement(screen, vertical);
      }
    }

    if (childRootElement != null) {
      childRootElement.setConstraintX(SizeValue.px(0));
      childRootElement.setConstraintY(SizeValue.px(0));
    }

    updateBottomRightElement();

    nifty.executeEndOfFrameElementActions();
    layoutSilently(element.getParent());
  }

  private void updateBottomRightElement() {
    if (nifty == null || screen == null) {
      log.severe("Can't apply the bottom right spacer as long as the controller is not properly bound.");
      return;
    }
    Element element = getElement();
    if (element == null) {
      return;
    }
    Element horizontal = element.findElementById("#horizontal-scrollbar-parent");
    Element vertical = element.findElementById("#vertical-scrollbar");
    Element bottomRight = element.findElementById("#bottom-right");
    if (horizontal != null) {
      if (vertical == null) {
        if (bottomRight != null) {
          nifty.removeElement(screen, bottomRight);
          nifty.executeEndOfFrameElementActions();
          initializeHorizontalScrollbar();
        }
      } else {
        if (bottomRight == null) {
          if (bottomRightTemplate == null) {
            log.severe("Need to create bottom right element to apply a proper spacing. But there is no template. " +
                "List box is expected to look crappy.");
          } else {
            nifty.createElementFromType(screen, horizontal, bottomRightTemplate.getElementType());
            initializeHorizontalScrollbar();
          }
        }
      }
    }
  }

  private void initializeHorizontalScrollbar() {
    Scrollbar horizontalS = getHorizontalScrollbar();
    if (horizontalS != null && horizontalS.isBound()) {
      horizontalS.setWorldMax(lastMaxWidth);
      horizontalS.setWorldPageSize(listBoxPanelElement != null ? listBoxPanelElement.getWidth() : 0);
    }
  }

  private void initializeVerticalScrollbar(final float labelTemplateHeight, final int itemCount) {
    Scrollbar verticalS = getVerticalScrollbar();
    if (verticalS != null && verticalS.isBound()) {
      verticalS.setWorldMax(itemCount * labelTemplateHeight);
      verticalS.setWorldPageSize(displayItems * labelTemplateHeight);
      verticalS.setButtonStepSize(labelTemplateHeight);
    }
  }

  @Override
  public void addItemProcessor(@Nonnull final ListBoxItemProcessor processor) {
    itemProcessors.add(processor);
  }

  private void createLabels() {
    if (nifty == null || screen == null || childRootElement == null) {
      log.severe("Label creation failed. Binding not done properly");
      return;
    }
    if (labelTemplateElementType == null) {
      log.severe("Label creation failed. Template element set.");
      return;
    }
    String templateId = labelTemplateElementType.getAttributes().get("id");
    for (final Element e : childRootElement.getChildren()) {
      nifty.removeElement(screen, e);
    }
    labelElements = new Element[displayItems];

    for (int i = 0; i < displayItems; i++) {
      ElementType templateType = labelTemplateElementType.copy();

      String oldId = templateId;
      if (oldId == null) {
        oldId = getChildId("#child-root");
      }
      final String newId;
      if (oldId == null) {
        log.severe("Failed to locate proper ID, label element will be created with global id.");
        newId = NiftyIdCreator.generate();
      } else {
        newId = oldId + "#" + NiftyIdCreator.generate();
      }

      templateType.getAttributes().set("id", newId);
      if (oldId != null) {
        replaceAllIds(templateType, oldId, newId);
      }

      labelElements[i] = nifty.createElementFromType(screen, childRootElement, templateType);

      for (final ListBoxItemProcessor processor : itemProcessors) {
        processor.processElement(labelElements[i]);
      }
    }
  }

  private void replaceAllIds(
      @Nonnull final ElementType type,
      @Nonnull final String oldId,
      @Nonnull final String newId) {
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
    if (scrollElement != null) {
      scrollElement.setConstraintHeight(SizeValue.px(displayItems * labelTemplateHeight));
    }
  }

  private void ensureVerticalScrollbar() {
    if (displayItems == 1) {
      verticalScrollbar = ScrollbarMode.off;
    }
  }

  private void connectListBoxAndListBoxPanel() {
    if (listBoxPanelElement == null) {
      log.severe("Can't connect list box and panel while panel is not set. Binding not done?");
      return;
    }
    @SuppressWarnings("unchecked")
    ListBoxPanel<T> listBoxPanel = listBoxPanelElement.getControl(ListBoxPanel.class);
    if (listBoxPanel == null) {
      log.severe("List box panel element does not contain proper control. Corrupted control.");
    } else {
      listBoxPanel.setListBox(listBoxImpl);
    }
  }

  private void displayElement(final int index, @Nonnull final T item) {
    if (viewConverter != null && labelElements != null) {
      viewConverter.display(labelElements[index], item);
    }
  }

  private void handleElementSelection(
      final int index,
      @Nullable final T item,
      @Nonnull final List<Integer> selectedElements) {
    if (labelElements != null) {
      if (item != null && selectedElements.contains(index)) {
        labelElements[index].startEffect(EffectEventId.onCustom, null, "select");
      } else {
        labelElements[index].resetSingleEffect(EffectEventId.onCustom, "select");
      }
    }
  }

  private void handleElementFocus(final int index, final int focusElement) {
    if (listBoxPanelElement != null && labelElements != null) {
      if (index < 0 || index >= labelElements.length) {
        throw new ArrayIndexOutOfBoundsException(index);
      }
      @SuppressWarnings("unchecked")
      ListBoxPanel<T> listBoxPanel = listBoxPanelElement.getControl(ListBoxPanel.class);
      if (listBoxPanel != null && listBoxPanel.hasFocus()) {
        if (focusElement == index) {
          labelElements[index].startEffect(EffectEventId.onCustom, null, "focus");
        } else {
          labelElements[index].resetSingleEffect(EffectEventId.onCustom, "focus");
        }
      } else {
        labelElements[index].resetSingleEffect(EffectEventId.onCustom, "focus");
      }
    }
  }

  private void setListBoxItemIndex(final int itemIndex) {
    if (labelElements != null) {
      if (itemIndex < 0 || itemIndex >= labelElements.length) {
        throw new ArrayIndexOutOfBoundsException(itemIndex);
      }
      @SuppressWarnings("unchecked")
      ListBoxItemController<T> listBoxItemController = labelElements[itemIndex].getControl(ListBoxItemController.class);
      if (listBoxItemController != null) {
        listBoxItemController.setItemIndex(itemIndex);
      }
    }
  }

  @Nullable
  private Scrollbar getVerticalScrollbar() {
    return getScrollbar("#vertical-scrollbar");
  }

  @Nullable
  private Scrollbar getHorizontalScrollbar() {
    return getScrollbar("#horizontal-scrollbar");
  }

  @Nullable
  private Scrollbar getScrollbar(@Nonnull final String id) {
    Element element = getElement();
    if (element == null) {
      return null;
    }
    return element.findNiftyControl(id, Scrollbar.class);
  }

  private enum ScrollbarMode {
    off,
    on,
    optional
  }
}
