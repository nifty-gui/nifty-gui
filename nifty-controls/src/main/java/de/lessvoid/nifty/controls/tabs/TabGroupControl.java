/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lessvoid.nifty.controls.tabs;

import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.*;
import de.lessvoid.nifty.controls.tabs.builder.TabBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.events.ElementShowEvent;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.loaderv2.types.ElementType;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.SizeValue;
import org.bushe.swing.event.EventTopicSubscriber;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.logging.Logger;

/**
 * This is the controller for the tab group.
 *
 * @author ractoc
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 * @deprecated This class is not meant to be used in a final application. Rather use the {@link TabGroup} interface.
 */
@Deprecated
public class TabGroupControl extends AbstractController implements TabGroup {
  private static final int BUTTON_LEFT_MARGIN = -16;

  /**
   * This class is used for the event handlers that are needed for the tab group controls. It will force the tab group
   * to update the visibility settings of all tabs in case the element is shown.
   */
  private static final class TabGroupShowEventSubscriber implements EventTopicSubscriber<ElementShowEvent> {
    @Override
    public void onEvent(@Nonnull final String topic, @Nonnull final ElementShowEvent data) {
      final TabGroupControl control = data.getElement().getControl(TabGroupControl.class);
      if (control != null) {
        control.checkVisibility();
      }
    }
  }

  /**
   * This class is used for the event handlers that monitor the clicks on the buttons that are used to select the active
   * tab in a tab group.
   */
  private static final class ButtonClickEventSubscriber implements EventTopicSubscriber<ButtonClickedEvent> {
    /**
     * The tab group that is effected by this click.
     */
    private final TabGroupControl parentControl;

    /**
     * Create a instance of this event subscriber and set the tab group control that will be effected by this
     * subscriber.
     *
     * @param parent the tab group control that is the parent of this class
     */
    private ButtonClickEventSubscriber(final TabGroupControl parent) {
      parentControl = parent;
    }

    @Override
    public void onEvent(final String topic, @Nonnull final ButtonClickedEvent data) {
      Element element = data.getButton().getElement();
      if (element != null) {
        parentControl.processButtonClick(element);
      }
    }
  }

  /**
   * This end notification is supposed to used in case a check of the visibility values of the tab control is required
   * once a operation is done.
   */
  private static final class CheckVisibilityEndNotify implements EndNotify {
    /**
     * The tab group control that is the target for the visibility check.
     */
    @Nonnull
    private final TabGroupControl parentControl;

    /**
     * The next notification that is supposed to be called in chain.
     */
    @Nullable
    private final EndNotify next;

    /**
     * Constructor for the check visibility end notification.
     *
     * @param control    the tab group control that is the target
     * @param nextNotify the next notification to call or {@code null} in case there is none
     */
    private CheckVisibilityEndNotify(@Nonnull final TabGroupControl control, @Nullable final EndNotify nextNotify) {
      parentControl = control;
      next = nextNotify;
    }

    @Override
    public void perform() {
      parentControl.checkVisibility();
      if (next != null) {
        next.perform();
      }
    }
  }

  /**
   * This class is used to call the adding operation for a tab again, once the tab was moved to the proper location.
   * Hopefully people manage to add the tab at the right spot from the start, so using this class won't be needed that
   * often.
   */
  private static final class TabAddMoveEndNotify implements EndNotify {
    /**
     * The tab group control where the tab is supposed to be added to.
     */
    @Nonnull
    private final TabGroupControl parentControl;

    /**
     * The tab that is supposed to be added.
     */
    @Nonnull
    private final Tab tabToAdd;

    /**
     * Create a instance of this notification handler.
     *
     * @param control the tab group control where the tab is added to
     * @param tab     the tab that is added
     */
    private TabAddMoveEndNotify(@Nonnull final TabGroupControl control, @Nonnull final Tab tab) {
      parentControl = control;
      tabToAdd = tab;
    }

    @Override
    public void perform() {
      parentControl.addTab(tabToAdd);
    }
  }

  /**
   * The logger that takes care for the output of log messages in this class.
   */
  private static final Logger log = Logger.getLogger(TabGroupControl.class.getName());

  /**
   * The subscriber of the show events for this control. This is required to fix the visibility values one the tab
   * control is displayed.
   */
  @Nonnull
  private static final EventTopicSubscriber<ElementShowEvent> showEventSubscriber = new TabGroupShowEventSubscriber();

  /**
   * This subscriber is used to monitor the click events on the buttons.
   */
  @Nonnull
  private final EventTopicSubscriber<ButtonClickedEvent> buttonClickedSubscriber;

  /**
   * This is the panel that is supposed to hold the buttons used to change the currently visible tab.
   */
  @Nullable
  private Element tabButtonPanel;

  /**
   * This is the content panel that stores all tabs.
   */
  @Nullable
  private Element contentPanel;

  /**
   * The instance of the Nifty-GUI that is parent to this tab group control.
   */
  @Nullable
  private Nifty nifty;

  /**
   * The screen that is parent to the element this control is assigned to.
   */
  @Nullable
  private Screen screen;

  /**
   * The template of the button that is supposed to be used for each new tab.
   */
  @Nullable
  private ElementType buttonTemplate;

  /**
   * The index of the tab that is currently selected or {@code -1} in case no tab is visible.
   */
  private int selectedIndex;

  /**
   * This value is set true once the template is gone.
   */
  private boolean templateRemoved;

  public TabGroupControl() {
    //noinspection ThisEscapedInObjectConstruction
    buttonClickedSubscriber = new ButtonClickEventSubscriber(this);
    selectedIndex = -1;
  }

  @Override
  public void addTab(@Nonnull final Element tab) {
    final Tab tabControl = tab.getNiftyControl(Tab.class);
    if (tabControl == null) {
      throw new IllegalArgumentException("Element to add is not a tab.");
    }
    addTab(tabControl);
  }

  @Override
  public void addTab(@Nonnull final TabBuilder tabBuilder) {
    if (nifty == null || screen == null || contentPanel == null) {
      throw new IllegalStateException("Element is not bound yet. Can't add tabs.");
    }
    final Element tab = tabBuilder.build(nifty, screen, contentPanel);
    final Tab tabControl = tab.getNiftyControl(Tab.class);
    if (tabControl == null) {
      throw new IllegalStateException("Tab builder did not create a tab... WTF?!");
    }
    addTab(tabControl);
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

    tabButtonPanel = element.findElementById("#tab-button-panel");
    contentPanel = element.findElementById("#tab-content-panel");

    if (tabButtonPanel == null) {
      log.severe("Panel for the tabs not found. Tab group will not work properly. Looked for: #tab-button-panel");
    } else {
      final Element buttonElement = tabButtonPanel.findElementById("#button-template");
      if (buttonElement == null) {
        log.severe("No template for tab button found. Tab group will be unable to display tabs. Looked for: " +
            "#button-template");
      } else {
        buttonTemplate = buttonElement.getElementType().copy();
        buttonElement.markForRemoval(new EndNotify() {
          @Override
          public void perform() {
            templateRemoved = true;
          }
        });
      }
    }
    if (contentPanel == null) {
      log.severe("Content panel not found. Tab group will be unable to display tab content. Looked for: " +
          "#tab-content-pane");
    }
  }

  @Nullable
  @Override
  public Tab getSelectedTab() {
    if (selectedIndex == -1 || contentPanel == null) {
      return null;
    }
    return contentPanel.getChildren().get(selectedIndex).getNiftyControl(Tab.class);
  }

  @Override
  public int getSelectedTabIndex() {
    return selectedIndex;
  }

  @Override
  public void init(@Nonnull final Parameters parameter) {
    super.init(parameter);

    if (contentPanel != null) {
      for (final Element element : contentPanel.getChildren()) {
        final Tab tabControl = element.getNiftyControl(Tab.class);
        if (tabControl == null) {
          log.warning("Element without tab control detected. Removing: " + element.getId());
          element.markForRemoval();
        } else {
          initTab(tabControl);
          selectedIndex = 0;
        }
      }
    }

    checkVisibility();
  }

  private void initTab(@Nonnull final Tab tab) {
    final int tabIndex = indexOf(tab);
    Element button = getButton(tabIndex);
    if (button == null) {
      if (buttonTemplate == null || nifty == null || screen == null || tabButtonPanel == null) {
        log.severe("Tab can't be initialized. Binding not done yet or binding failed.");
        return;
      }
      final ElementType newButtonTemplate = buttonTemplate.copy();
      newButtonTemplate.getAttributes().set("id", buildTabButtonName(tabIndex));
      button = nifty.createElementFromType(screen, tabButtonPanel, newButtonTemplate);
    }
    String buttonId = button.getId();
    if (buttonId != null) {
      nifty.subscribe(screen, buttonId, ButtonClickedEvent.class, buttonClickedSubscriber);
    }

    if (!button.isVisible()) {
      button.show();
    }

    final Button btnControl = button.getNiftyControl(Button.class);
    if (btnControl == null) {
      log.warning("Can't set label of tab selection element that is not a button.");
    } else {
      btnControl.setText(tab.getCaption());
    }

    if (tab instanceof TabGroupMember) {
      ((TabGroupMember) tab).setParentTabGroup(this);
    }
  }

  @Nonnull
  private String buildTabButtonName(final int index) {
    String tabButtonId = "#tabButton-" + index;
    if (tabButtonPanel != null) {
      tabButtonId = tabButtonPanel.getId() + tabButtonId;
    }
    return tabButtonId;
  }

  @Override
  public void addTab(@Nonnull final Tab tab) {
    Element tabElement = tab.getElement();
    if (tabElement != null) {
      Element tabParentElement = tabElement.getParent();
      if (contentPanel != null && !tabParentElement.equals(contentPanel)) {
        tabElement.markForMove(contentPanel, new TabAddMoveEndNotify(this, tab));
        return;
      }
    }

    initTab(tab);
    checkVisibility();
  }

  @Override
  public int indexOf(@Nonnull final Tab tab) {
    if (contentPanel == null) {
      return -1;
    }
    final int length = getTabCount();
    final List<Element> elementList = contentPanel.getChildren();
    int result = -1;
    for (int i = 0; i < length; i++) {
      if (tab.equals(elementList.get(i).getNiftyControl(Tab.class))) {
        result = i;
        break;
      }
    }
    return result;
  }

  @Override
  public int getTabCount() {
    return contentPanel != null ? contentPanel.getChildren().size() : 0;
  }

  /**
   * Get the button element at a specified index.
   * <p/>
   * This function ignores the template-button as needed. This is required because Nifty does not provide a blocking way
   * to remove a element from the GUI.
   *
   * @param index the index of the button
   * @return the element of the button or {@code null} in case there is no button assigned to this index
   */
  @Nullable
  private Element getButton(final int index) {
    if (tabButtonPanel == null) {
      return null;
    }
    int realIndex = index;

    final List<Element> buttonList = tabButtonPanel.getChildren();
    if (buttonList.isEmpty()) {
      return null;
    }

    if (!templateRemoved) {
      realIndex++;
    }

    if (realIndex >= buttonList.size()) {
      return null;
    }
    return tabButtonPanel.getChildren().get(realIndex);
  }

  /**
   * Get the tab control at a specified index.
   *
   * @param index the index of the tab control
   * @return the tab control
   */
  @Nullable
  public Tab getTabAtIndex(final int index) {
    if (contentPanel == null) {
      return null;
    }
    return contentPanel.getChildren().get(index).getNiftyControl(Tab.class);
  }

  /**
   * Check the visibility settings of all tabs and correct it as needed.
   */
  private void checkVisibility() {
    if (contentPanel == null) {
      return;
    }
    final int length = getTabCount();
    final List<Element> tabList = contentPanel.getChildren();

    for (int i = 0; i < length; i++) {
      final Element tab = tabList.get(i);
      final Element button = getButton(i);

      if (button == null) {
        log.warning("Something is wrong with the tabs. Tab button not there anymore.");
        continue;
      }

      if (i == selectedIndex) {
        if (!tab.isVisible()) {
          tab.show();
        }
        if (!"nifty-tab-button-active".equals(button.getStyle())) {
          button.setStyle("nifty-tab-button-active");
          addMargin(i, button);
        }
        button.setRenderOrder(100000);
      } else {
        if (tab.isVisible()) {
          tab.hide();
        }
        if (!"nifty-tab-button".equals(button.getStyle())) {
          button.setStyle("nifty-tab-button");
          addMargin(i, button);
        }
        button.setRenderOrder(0);
      }
    }

    Element element = getElement();
    if (element != null) {
      element.layoutElements();
    }
  }

  private void addMargin(final int i, @Nonnull final Element button) {
    if (i > 0) {
      button.setMarginLeft(SizeValue.px(BUTTON_LEFT_MARGIN));
    } else {
      button.setMarginLeft(SizeValue.px(0));
    }
  }

  @Override
  public boolean inputEvent(@Nonnull final NiftyInputEvent inputEvent) {
    return true;
  }

  @Override
  public boolean isTabInGroup(@Nonnull final Tab tab) {
    return indexOf(tab) > -1;
  }

  @Override
  public void onStartScreen() {
    if (nifty == null || screen == null) {
      log.severe("Starting screen failed. Seems the binding is not done yet.");
    }
    String id = getId();
    if (id != null) {
      nifty.subscribe(screen, id, ElementShowEvent.class, showEventSubscriber);
    }
  }

  @Override
  public void removeTab(final int index) {
    removeTab(index, null);
  }

  @Override
  public void removeTab(@Nonnull final Tab tab) {
    removeTab(tab, null);
  }

  @Override
  public void removeTab(@Nonnull final Element tab) {
    removeTab(tab, null);
  }

  @Override
  public void removeTab(final int index, @Nullable final EndNotify notify) {
    if (nifty == null) {
      throw new IllegalStateException("Can't remove tab as long as binding is not done.");
    }
    if ((index < 0) || (index >= getTabCount())) {
      throw new IndexOutOfBoundsException("Index out of bounds: " + index);
    }

    EndNotify triggeredNotification = notify;
    if (selectedIndex == index) {
      selectedIndex = Math.max(0, Math.min(selectedIndex, getTabCount() - 1));
      triggeredNotification = new CheckVisibilityEndNotify(this, triggeredNotification);
    }

    if (tabButtonPanel != null) {
      final Element button;
      if (templateRemoved) {
        button = tabButtonPanel.getChildren().get(index);
      } else {
        button = tabButtonPanel.getChildren().get(index + 1);
      }
      nifty.unsubscribe(button.getId(), buttonClickedSubscriber);
      button.markForRemoval();
    }
    if (contentPanel != null) {
      contentPanel.getChildren().get(index).markForRemoval(triggeredNotification);
    }
  }

  @Override
  public void removeTab(@Nonnull final Tab tab, @Nullable final EndNotify notify) {
    final int index = indexOf(tab);
    if (index == -1) {
      throw new IllegalArgumentException("The tab to remove is not part of this tab group.");
    }
    removeTab(index, notify);
  }

  @Override
  public void removeTab(@Nonnull final Element tab, @Nullable final EndNotify notify) {
    final Tab tabControl = tab.getNiftyControl(Tab.class);
    if (tabControl == null) {
      throw new IllegalArgumentException("Element to add is not a tab.");
    }
    removeTab(tabControl, notify);
  }

  @Override
  public void setSelectedTab(@Nonnull final Tab tab) {
    final int index = indexOf(tab);
    if (index == -1) {
      throw new IllegalArgumentException("The tab to remove is not part of this tab group.");
    }
    setSelectedTabIndex(index);
  }

  @Override
  public void setSelectedTabIndex(final int index) {
    if ((index < 0) || (index >= getTabCount())) {
      throw new IndexOutOfBoundsException("Index out of bounds: " + index);
    }

    selectedIndex = index;
    checkVisibility();
    if (nifty != null) {
      String id = getId();
      if (id != null) {
        Tab tab = getTabAtIndex(index);
        if (tab == null) {
          log.severe("Tab with valid index returned null. This looks like a internal error.");
        } else {
          nifty.publishEvent(id, new TabSelectedEvent(this, tab, index));
        }
      }
    }
  }

  @Override
  public void setTabCaption(final int index, @Nonnull final String caption) {
    if ((index < 0) || (index >= getTabCount())) {
      throw new IndexOutOfBoundsException("Index out of bounds: " + index);
    }

    if (contentPanel != null) {
      final Tab tabControl = contentPanel.getChildren().get(index).getNiftyControl(Tab.class);
      if (tabControl == null) {
        log.severe("Tab control seems to be corrupted. Expected tab control not located.");
      } else {
        tabControl.setCaption(caption);
      }
    }

    Element buttonElement = getButton(index);
    if (buttonElement == null) {
      log.severe("Tab control seems corrupted. Expected button element not located.");
    } else {
      final Button button = buttonElement.getNiftyControl(Button.class);
      if (button == null) {
        log.severe("Tab button does not seem to contain a button control.");
      } else {
        button.setText(caption);
      }
    }
  }

  @Override
  public void setTabCaption(@Nonnull final Tab tab, @Nonnull final String caption) {
    final int index = indexOf(tab);
    if (index == -1) {
      throw new IllegalArgumentException("The tab to remove is not part of this tab group.");
    }
    setTabCaption(index, caption);
  }

  /**
   * Handle a click on a button and switch the tab.
   *
   * @param clickedButton the button that was clicked
   */
  private void processButtonClick(@Nonnull final Element clickedButton) {
    if (tabButtonPanel == null) {
      return;
    }
    final List<Element> buttons = tabButtonPanel.getChildren();
    if (buttons.isEmpty()) {
      return;
    }

    int indexOffset = 0;
    if (!templateRemoved) {
      indexOffset = -1;
    }

    setSelectedTabIndex(buttons.indexOf(clickedButton) + indexOffset);
  }
}
