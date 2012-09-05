package de.lessvoid.nifty.controls.menu;

import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyIdCreator;
import de.lessvoid.nifty.builder.ControlBuilder;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.controls.Menu;
import de.lessvoid.nifty.controls.MenuItemActivatedEvent;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.xml.xpp3.Attributes;

/**
 * @deprecated Please use {@link de.lessvoid.nifty.controls.PopupMenu} when accessing NiftyControls.
 */
@Deprecated
public class MenuControl<T> implements Controller, Menu<T> {
  private Nifty nifty;
  private Screen screen;
  private Element element;
  private boolean bound;

  // This will keep a map of all items (T) added to this menu with the elementId
  // of the Nifty element as the key. We'll use this map to find the added item
  // that we'll need to return when the item with an elementId has been activated.
  private Map<String, T> items = new Hashtable<String, T>();

  @Override
  public void bind(
      final Nifty niftyParam,
      final Screen screenParam,
      final Element newElement,
      final Properties properties,
      final Attributes controlDefinitionAttributesParam) {
    nifty = niftyParam;
    screen = screenParam;
    element = newElement;
  }

  @Override
  public void init(final Properties parameter, final Attributes controlDefinitionAttributes) {
    bound = true;
  }

  @Override
  public void onStartScreen() {
    movePopup();
  }

  @Override
  public boolean inputEvent(final NiftyInputEvent inputEvent) {
    return false;
  }

  @Override
  public void onFocus(final boolean getFocus) {
  }

  @Override
  public void addMenuItem(final String menuText, final T item) {
    final String id = NiftyIdCreator.generate();
    new ControlBuilder(id, "niftyMenuItem") { {
      set("menuText", nifty.specialValuesReplace(menuText));
      set("menuOnClick", "activateItem(" + id + ")");
      set("menuIconVisible", "false");
    } } .build(nifty, screen, element);
    items.put(id, item);
  }

  @Override
  public void addMenuItem(final String menuText, final String menuIcon, final T item) {
    final String id = NiftyIdCreator.generate();
    new ControlBuilder(id, "niftyMenuItem") { {
      set("menuText", nifty.specialValuesReplace(menuText));
      set("menuOnClick", "activateItem(" + id + ")");
      if (menuIcon != null) {
        set("menuIcon", menuIcon);
        set("menuIconVisible", "true");
      } else {
        set("menuIconVisible", "false");
      }
    } } .build(nifty, screen, element);
    items.put(id, item);
  }

  @Override
  public void addMenuItemSeparator() {
    new ControlBuilder("niftyMenuItemSeparator").build(nifty, screen, element);
  }

  // NiftyControl implementation

  @Override
  public Element getElement() {
    return element;
  }

  @Override
  public void enable() {
    element.enable();
  }

  @Override
  public void disable() {
    element.disable();
  }

  @Override
  public void setEnabled(final boolean enabled) {
    if (enabled) {
      element.enable();
    } else {
      element.disable();
    }
  }

  @Override
  public boolean isEnabled() {
    return element.isEnabled();
  }

  @Override
  public String getId() {
    return element.getId();
  }

  @Override
  public void setId(final String id) {
    element.setId(id);
  }

  @Override
  public int getWidth() {
    return element.getWidth();
  }

  @Override
  public void setWidth(final SizeValue width) {
    element.setConstraintWidth(width);
  }

  @Override
  public int getHeight() {
    return element.getHeight();
  }

  @Override
  public void setHeight(final SizeValue height) {
    element.setConstraintHeight(height);
  }

  @Override
  public String getStyle() {
    return element.getStyle();
  }

  @Override
  public void setStyle(final String style) {
   element.setStyle(style); 
  }

  @Override
  public void setFocus() {
  }

  @Override
  public void setFocusable(final boolean focusable) {
  }

  @Override
  public boolean hasFocus() {
    return false;
  }

  @Override
  public void layoutCallback() {
  }

  @Override
  public boolean isBound() {
    return bound;
  }

  @Override
  public String toString() {
    return super.toString() + " {" + (element == null ? "" : element.getId()) + "}";
  }

  // interact callbacks

  public boolean activateItem(final String menuItemId) {
    nifty.publishEvent(element.getId(), new MenuItemActivatedEvent<T>(MenuControl.this, items.get(menuItemId)));
    return true;
  }

  // Internals

  private void movePopup() {
    element.setConstraintX(new SizeValue(nifty.getNiftyMouse().getX() + "px"));
    element.setConstraintY(new SizeValue(nifty.getNiftyMouse().getY() + "px"));
    element.getParent().layoutElements();
  }


}
