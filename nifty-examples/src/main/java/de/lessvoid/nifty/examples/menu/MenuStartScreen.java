package de.lessvoid.nifty.examples.menu;

import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.controls.Menu;
import de.lessvoid.nifty.controls.MenuItemActivatedEvent;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.examples.NiftyExample;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.SizeValue;
import org.bushe.swing.event.EventTopicSubscriber;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author void
 */
public class MenuStartScreen implements ScreenController, NiftyExample {
  private Nifty nifty;
  private Screen screen;
  @Nullable
  private Element popup;

  @Override
  public void bind(@Nonnull final Nifty nifty, @Nonnull final Screen screen) {
    this.nifty = nifty;
    this.screen = screen;
    createPopup();
  }

  @Override
  public void onStartScreen() {
  }

  @Override
  public void onEndScreen() {
  }

  private void createPopup() {
    this.popup = nifty.createPopup("niftyPopupMenu");

    Menu<ThisReallyCouldBeAnyClassYouWant> popupMenu = popup.findNiftyControl("#menu", Menu.class);

    popupMenu.setWidth(new SizeValue("250px"));
    popupMenu.addMenuItem("MenuItem 1", "menu/listen.png", new ThisReallyCouldBeAnyClassYouWant("SomeId1",
        "You've clicked MenuItem 1"));
    popupMenu.addMenuItem("MenuItem 4000000000000000000", "menu/stop.png", new ThisReallyCouldBeAnyClassYouWant
        ("SomeId2", "You've clicked a very odd MenuItem"));
    popupMenu.addMenuItemSeparator();
    popupMenu.addMenuItem("MenuItem 5", new ThisReallyCouldBeAnyClassYouWant("SomeId5",
        "You've clicked MenuItem 5 (Where is 3?)"));
    popupMenu.addMenuItem("MenuItem 6", new ThisReallyCouldBeAnyClassYouWant("SomeId6", "You've clicked MenuItem 6"));
    popupMenu.addMenuItemSeparator();
    popupMenu.addMenuItem("Exit", new ThisReallyCouldBeAnyClassYouWant("exit", "Good Bye! :)"));
  }

  public void showMenu() {
    nifty.showPopup(screen, popup.getId(), null);
    nifty.subscribe(screen, popup.findNiftyControl("#menu", Menu.class).getId(), MenuItemActivatedEvent.class,
        new MenuItemActivatedEventSubscriber());
  }

  @Nonnull
  @Override
  public String getStartScreen() {
    return "start";
  }

  @Nonnull
  @Override
  public String getMainXML() {
    return "menu/menu.xml";
  }

  @Nonnull
  @Override
  public String getTitle() {
    return "Nifty Menu Example";
  }

  @Override
  public void prepareStart(Nifty nifty) {
    // nothing to do
  }

  private class MenuItemActivatedEventSubscriber implements EventTopicSubscriber<MenuItemActivatedEvent> {
    @Override
    public void onEvent(final String id, @Nonnull final MenuItemActivatedEvent event) {
      final ThisReallyCouldBeAnyClassYouWant item = (ThisReallyCouldBeAnyClassYouWant) event.getItem();

      Label label = screen.findNiftyControl("textOut", Label.class);
      label.setText(item.text + " [" + item.key + "]");

      nifty.closePopup(popup.getId(), new EndNotify() {

        @Override
        public void perform() {
          if ("exit".equals(item.key)) {
            nifty.setAlternateKeyForNextLoadXml("fade");
            nifty.fromXml("all/intro.xml", "menu");
          }
        }
      });
    }
  }

  // This is just an example and not an example of great class naming :)
  private static class ThisReallyCouldBeAnyClassYouWant {
    @Nonnull
    public String key;
    @Nonnull
    public String text;

    public ThisReallyCouldBeAnyClassYouWant(@Nonnull final String key, @Nonnull final String text) {
      this.key = key;
      this.text = text;
    }
  }
}
