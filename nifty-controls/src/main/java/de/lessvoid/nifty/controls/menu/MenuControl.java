package de.lessvoid.nifty.controls.menu;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyIdCreator;
import de.lessvoid.nifty.builder.ControlBuilder;
import de.lessvoid.nifty.controls.AbstractController;
import de.lessvoid.nifty.controls.Menu;
import de.lessvoid.nifty.controls.MenuItemActivatedEvent;
import de.lessvoid.nifty.controls.Parameters;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.SizeValue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @deprecated Please use {@link de.lessvoid.nifty.controls.Menu} when accessing NiftyControls.
 */
@Deprecated
public class MenuControl<T> extends AbstractController implements Menu<T> {
  @Nonnull
  private static final Logger log = Logger.getLogger(MenuControl.class.getName());
  @Nullable
  private Nifty nifty;
  @Nullable
  private Screen screen;

  // This will keep a map of all items (T) added to this menu with the elementId
  // of the Nifty element as the key. We'll use this map to find the added item
  // that we'll need to return when the item with an elementId has been activated.
  @Nonnull
  private final Map<String, T> items = new HashMap<String, T>();

  @Override
  public void bind(
      @Nonnull final Nifty nifty,
      @Nonnull final Screen screen,
      @Nonnull final Element element,
      @Nonnull final Parameters properties) {
    bind(element);
    this.nifty = nifty;
    this.screen = screen;
  }

  @Override
  public void onStartScreen() {
    movePopup();
  }

  @Override
  public boolean inputEvent(@Nonnull final NiftyInputEvent inputEvent) {
    return false;
  }

  @Override
  public void addMenuItem(@Nonnull final String menuText, @Nonnull final T item) {
    addMenuItem(menuText, null, item);
  }

  @Override
  public void addMenuItem(@Nonnull final String menuText, @Nullable final String menuIcon, @Nonnull final T item) {
    if (nifty == null || screen == null) {
      log.warning("Adding item is not possible as long as the control is not properly bound.");
      return;
    }
    Element element = getElement();
    if (element == null) {
      return;
    }

    final String id = NiftyIdCreator.generate();

    ControlBuilder builder = new ControlBuilder(id, "niftyMenuItem");
    builder.set("menuText", nifty.specialValuesReplace(menuText));
    builder.set("menuOnClick", "activateItem(" + id + ")");
    if (menuIcon == null) {
      builder.set("menuIconVisible", "false");
    } else {
      builder.set("menuIcon", menuIcon);
      builder.set("menuIconVisible", "true");
    }
    builder.build(nifty, screen, element);
    items.put(id, item);
  }

  @Override
  public void addMenuItemSeparator() {
    if (nifty == null || screen == null) {
      log.warning("Adding item is not possible as long as the control is not properly bound.");
      return;
    }
    Element element = getElement();
    if (element == null) {
      return;
    }
    new ControlBuilder("niftyMenuItemSeparator").build(nifty, screen, element);
  }

  // interact callbacks

  public boolean activateItem(@Nonnull final String menuItemId) {
    if (nifty != null) {
      String id = getId();
      if (id != null) {
        T item = items.get(menuItemId);
        if (item != null) {
          nifty.publishEvent(id, new MenuItemActivatedEvent<T>(this, item));
        }
      }
    }

    return true;
  }

  // Internals

  private void movePopup() {
    if (nifty == null) {
      return;
    }
    Element element = getElement();
    if (element != null) {
      element.setConstraintX(SizeValue.px(nifty.getNiftyMouse().getX()));
      element.setConstraintY(SizeValue.px(nifty.getNiftyMouse().getY()));
      element.getParent().layoutElements();
    }
  }
}
