/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lessvoid.nifty.controls.tabs;

import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import org.bushe.swing.event.EventTopicSubscriber;

import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.AbstractController;
import de.lessvoid.nifty.controls.Button;
import de.lessvoid.nifty.controls.ButtonClickedEvent;
import de.lessvoid.nifty.controls.Tab;
import de.lessvoid.nifty.controls.TabGroup;
import de.lessvoid.nifty.controls.TabSelectedEvent;
import de.lessvoid.nifty.controls.tabs.builder.TabBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.events.ElementShowEvent;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.loaderv2.types.ElementType;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.xml.xpp3.Attributes;

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
    public void onEvent(final String topic, final ElementShowEvent data) {
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
    public void onEvent(final String topic, final ButtonClickedEvent data) {
      parentControl.processButtonClick(data.getButton().getElement());
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
    private final TabGroupControl parentControl;

    /**
     * The next notification that is supposed to be called in chain.
     */
    private final EndNotify next;

    /**
     * Constructor for the check visibility end notification.
     *
     * @param control the tab group control that is the target
     * @param nextNotify the next notification to call or {@code null} in case there is none
     */
    private CheckVisibilityEndNotify(final TabGroupControl control, final EndNotify nextNotify) {
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
    private final TabGroupControl parentControl;

    /**
     * The tab that is supposed to be added.
     */
    private final Tab tabToAdd;

    /**
     * Create a instance of this notification handler.
     *
     * @param control the tab group control where the tab is added to
     * @param tab the tab that is added
     */
    private TabAddMoveEndNotify(final TabGroupControl control, final Tab tab) {
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
  private final Logger log;

  /**
   * The subscriber of the show events for this control. This is required to fix the visibility values one the tab
   * control is displayed.
   */
  private final EventTopicSubscriber<ElementShowEvent> showEventSubscriber;

  /**
   * This subscriber is used to monitor the click events on the buttons.
   */
  private final EventTopicSubscriber<ButtonClickedEvent> buttonClickedSubscriber;

  /**
   * This is the panel that is supposed to hold the buttons used to change the currently visible tab.
   */
  private Element tabButtonPanel;

  /**
   * This is the content panel that stores all tabs.
   */
  private Element contentPanel;

  /**
   * The instance of the Nifty-GUI that is parent to this tab group control.
   */
  private Nifty niftyGui;

  /**
   * The screen that is parent to the element this control is assigned to.
   */
  private Screen parentScreen;

  /**
   * The template of the button that is supposed to be used for each new tab.
   */
  private ElementType buttonTemplate;

  /**
   * The index of the tab that is currently selected or {@code -1} in case no tab is visible.
   */
  private int selectedIndex;

  public TabGroupControl() {
    log = Logger.getLogger(TabGroupControl.class.getName());
    showEventSubscriber = new TabGroupShowEventSubscriber();

    //noinspection ThisEscapedInObjectConstruction
    buttonClickedSubscriber = new ButtonClickEventSubscriber(this);
    selectedIndex = -1;
  }

  @Override
  public void addTab(final Element tab) {
    final Tab tabControl = tab.getNiftyControl(Tab.class);
    if (tabControl == null) {
      throw new IllegalArgumentException("Element to add is not a tab.");
    }
    addTab(tabControl);
  }

  @Override
  public void addTab(final TabBuilder tabBuilder) {
    final Element tab = tabBuilder.build(niftyGui, parentScreen, contentPanel);
    final Tab tabControl = tab.getNiftyControl(Tab.class);
    if (tabControl == null) {
      throw new IllegalStateException("Tab builder did not create a tab... WTF?!");
    }
    addTab(tabControl);
  }

  @Override
  public void bind(
      final Nifty nifty,
      final Screen screen,
      final Element element,
      final Properties parameter,
      final Attributes controlDefinitionAttributes) {
    bind(element);

    niftyGui = nifty;
    parentScreen = screen;

    tabButtonPanel = element.findElementByName("#tab-button-panel"); //NON-NLS
    contentPanel = element.findElementByName("#tab-content-panel"); //NON-NLS

    final Element buttonElement = tabButtonPanel.findElementByName("#button-template"); //NON-NLS
    if (buttonElement == null) {
      throw new IllegalStateException("Required button template missing.");
    }
    buttonTemplate = buttonElement.getElementType().copy();
    nifty.removeElement(screen, buttonElement);
  }

  @Override
  public Tab getSelectedTab() {
    return (selectedIndex == -1) ? null : contentPanel.getElements().get(selectedIndex).getNiftyControl(Tab.class);
  }

  @Override
  public int getSelectedTabIndex() {
    return selectedIndex;
  }

  @Override
  public void init(final Properties parameter, final Attributes controlDefinitionAttributes) {
    super.init(parameter, controlDefinitionAttributes);

    for (final Element element : contentPanel.getElements()) {
      final Tab tabControl = element.getNiftyControl(Tab.class);
      if (tabControl == null) {
        log.warning("Element without tab control detected. Removing: " + element.getId()); //NON-NLS
        element.markForRemoval();
      } else {
        initTab(tabControl);
        selectedIndex = 0;
      }
    }

    checkVisibility();
  }

  private void initTab(final Tab tab) {
    final int tabIndex = indexOf(tab);
    Element button = getButton(tabIndex);
    if (button == null) {
      final ElementType newButtonTemplate = buttonTemplate.copy();
      newButtonTemplate.getAttributes().set("id", buildTabButtonName(tabIndex)); //NON-NLS
      button = niftyGui.createElementFromType(parentScreen, tabButtonPanel, newButtonTemplate);
    }
    niftyGui.subscribe(parentScreen, button.getId(), ButtonClickedEvent.class, buttonClickedSubscriber);

    if (!button.isVisible()) {
      button.show();
    }

    final Button btnControl = button.getNiftyControl(Button.class);
    if (btnControl == null) {
      log.warning("Can't set label of tab selection element that is not a button."); //NON-NLS
    } else {
      btnControl.setText(tab.getCaption());
    }

    if (tab instanceof TabGroupMember) {
      ((TabGroupMember) tab).setParentTabGroup(this);
    }
  }

  private String buildTabButtonName(final int index) {
    return tabButtonPanel.getId() + "#tabButton-" + Integer.toString(index);
  }

  @Override
  public void addTab(final Tab tab) {
    if (!tab.getElement().getParent().equals(contentPanel)) {
      tab.getElement().markForMove(contentPanel, new TabAddMoveEndNotify(this, tab));
      return;
    }

    initTab(tab);

    checkVisibility();
  }

  @Override
  public int indexOf(final Tab tab) {
    if (tab == null) {
      throw new NullPointerException("The tab can't be null");
    }

    final int length = getTabCount();
    final List<Element> elementList = contentPanel.getElements();
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
    return contentPanel.getElements().size();
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
  private Element getButton(final int index) {
    int realIndex = index;

    final List<Element> buttonList = tabButtonPanel.getElements();
    if (buttonList.isEmpty()) {
      return null;
    }

    if (tabButtonPanel.getElements().get(0).getId().endsWith("#button-template")) { //NON-NLS
      realIndex++;
    }

    if (realIndex >= buttonList.size()) {
      return null;
    }
    return tabButtonPanel.getElements().get(realIndex);
  }

  /**
   * Get the tab control at a specified index.
   *
   * @param index the index of the tab control
   * @return the tab control
   */
  private Tab getTab(final int index) {
    return contentPanel.getElements().get(index).getNiftyControl(Tab.class);
  }

  /**
   * Check the visibility settings of all tabs and correct it as needed.
   */
  private void checkVisibility() {
    final int length = getTabCount();
    final List<Element> tabList = contentPanel.getElements();

    for (int i = 0; i < length; i++) {
      final Element tab = tabList.get(i);
      final Element button = getButton(i);

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

    getElement().layoutElements();
  }

  private void addMargin(final int i, final Element button) {
    if (i > 0) {
      button.setMarginLeft(SizeValue.px(BUTTON_LEFT_MARGIN));
    } else {
      button.setMarginLeft(SizeValue.px(0));
    }
  }

  @Override
  public boolean inputEvent(final NiftyInputEvent inputEvent) {
    return true;
  }

  @Override
  public boolean isTabInGroup(final Tab tab) {
    return indexOf(tab) > -1;
  }

  @Override
  public void onStartScreen() {
    //niftyGui.unsubscribe(getId(), showEventSubscriber);
    niftyGui.subscribe(parentScreen, getId(), ElementShowEvent.class, showEventSubscriber);
  }

  @Override
  public void removeTab(final int index) {
    removeTab(index, null);
  }

  @Override
  public void removeTab(final Tab tab) {
    removeTab(tab, null);
  }

  @Override
  public void removeTab(final Element tab) {
    removeTab(tab, null);
  }

  @Override
  public void removeTab(final int index, final EndNotify notify) {
    if ((index < 0) || (index >= getTabCount())) {
      throw new IndexOutOfBoundsException("Index out of bounds: " + index);
    }

    EndNotify triggeredNotification = notify;
    if (selectedIndex == index) {
      selectedIndex = Math.max(0, Math.min(selectedIndex, getTabCount() - 1));
      triggeredNotification = new CheckVisibilityEndNotify(this, triggeredNotification);
    }

    final Element button = tabButtonPanel.getElements().get(index);
    niftyGui.unsubscribe(button.getId(), buttonClickedSubscriber);
    button.markForRemoval();
    contentPanel.getElements().get(index).markForRemoval(triggeredNotification);
  }

  @Override
  public void removeTab(final Tab tab, final EndNotify notify) {
    final int index = indexOf(tab);
    if (index == -1) {
      throw new IllegalArgumentException("The tab to remove is not part of this tab group.");
    }
    removeTab(index, notify);
  }

  @Override
  public void removeTab(final Element tab, final EndNotify notify) {
    final Tab tabControl = tab.getNiftyControl(Tab.class);
    if (tabControl == null) {
      throw new IllegalArgumentException("Element to add is not a tab.");
    }
    removeTab(tabControl, notify);
  }

  @Override
  public void setSelectedTab(final Tab tab) {
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
    niftyGui.publishEvent(getId(), new TabSelectedEvent(this, getTab(index), index));
  }

  @Override
  public void setTabCaption(final int index, final String caption) {
    if ((index < 0) || (index >= getTabCount())) {
      throw new IndexOutOfBoundsException("Index out of bounds: " + index);
    }

    final Tab tabControl = contentPanel.getElements().get(index).getNiftyControl(Tab.class);
    if (tabControl == null) {
      throw new IllegalStateException("Tab control corrupted for index: " + index);
    }
    tabControl.setCaption(caption);

    final Button button = getButton(index).getNiftyControl(Button.class);
    if (button == null) {
      log.warning("Can't change caption in case template is not a button."); //NON-NLS
    } else {
      button.setText(caption);
    }
  }

  @Override
  public void setTabCaption(final Tab tab, final String caption) {
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
  private void processButtonClick(final Element clickedButton) {
    final List<Element> buttons = tabButtonPanel.getElements();
    if (buttons.isEmpty()) {
      return;
    }

    int indexOffset = 0;
    if (buttons.get(0).getId().endsWith("#button-template")) { //NON-NLS
      indexOffset = -1;
    }

    setSelectedTabIndex(buttons.indexOf(clickedButton) + indexOffset);
  }
}
