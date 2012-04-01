package de.lessvoid.nifty.examples.menu;

import org.bushe.swing.event.EventTopicSubscriber;

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

/**
 * ScreenController for Hello World Example.
 * @author void
 */
public class MenuStartScreen implements ScreenController, NiftyExample {
  private Nifty nifty;
  private Screen screen;
  private Element popup;

  public void bind(final Nifty nifty, final Screen screen) {
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
    popupMenu.addMenuItem("MenuItem 1", "menu/listen.png", new ThisReallyCouldBeAnyClassYouWant("SomeId1", "You've clicked MenuItem 1"));
    popupMenu.addMenuItem("MenuItem 4000000000000000000", "menu/stop.png", new ThisReallyCouldBeAnyClassYouWant("SomeId2", "You've clicked a very odd MenuItem"));
    popupMenu.addMenuItemSeparator();
    popupMenu.addMenuItem("MenuItem 5", new ThisReallyCouldBeAnyClassYouWant("SomeId5", "You've clicked MenuItem 5 (Where is 3?)"));
    popupMenu.addMenuItem("MenuItem 6", new ThisReallyCouldBeAnyClassYouWant("SomeId6", "You've clicked MenuItem 6"));
    popupMenu.addMenuItemSeparator();
    popupMenu.addMenuItem("Exit", new ThisReallyCouldBeAnyClassYouWant("exit", "Good Bye! :)"));
  }

  public void showMenu() {
    nifty.showPopup(screen, popup.getId(), null);
    nifty.subscribe(screen, popup.findNiftyControl("#menu", Menu.class).getId(), MenuItemActivatedEvent.class, new MenuItemActivatedEventSubscriber());
  }

  @Override
  public String getStartScreen() {
    return "start";
  }

  @Override
  public String getMainXML() {
    return "menu/menu.xml";
  }

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
    public void onEvent(final String id, final MenuItemActivatedEvent event) {
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
  };

  // This is just an example and not an example of great class naming :)
  private class ThisReallyCouldBeAnyClassYouWant {
    public String key;
    public String text;

    public ThisReallyCouldBeAnyClassYouWant(final String key, final String text) {
      this.key = key;
      this.text = text;
    }
  }
}
