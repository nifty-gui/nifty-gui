import org.bushe.swing.event.EventTopicSubscriber;

public static class MenuStartScreen implements ScreenController, NiftyExample {
  Nifty nifty;
  Screen screen;  
  Element popup;
  
  void bind( Nifty nifty,  Screen screen) {
    this.nifty = nifty;
    this.screen = screen;
    createPopup();
  }
  
  void onStartScreen() {
  }
  
  void onEndScreen() {
  }

  void createPopup() {
    this.popup = nifty.createPopup("niftyPopupMenu");

    Menu<ThisReallyCouldBeAnyClassYouWant> popupMenu = popup.findNiftyControl("#menu", Menu.class);

    popupMenu.setWidth(new SizeValue("250px"));
    popupMenu.addMenuItem("MenuItem 1", "menu/listen.png", new ThisReallyCouldBeAnyClassYouWant("SomeId1", "You've clicked MenuItem 1"));
    popupMenu.addMenuItem("MenuItem 4000000000000000000", "menu/stop.png", new ThisReallyCouldBeAnyClassYouWant("SomeId2", "You've clicked a very odd MenuItem"));
    popupMenu.addMenuItemSeparator();
    popupMenu.addMenuItem("MenuItem 5", new ThisReallyCouldBeAnyClassYouWant("SomeId5","You've clicked MenuItem 5 (Where is 3?)"));
    popupMenu.addMenuItem("MenuItem 6", new ThisReallyCouldBeAnyClassYouWant("SomeId6", "You've clicked MenuItem 6"));
    popupMenu.addMenuItemSeparator();
    popupMenu.addMenuItem("Exit", new ThisReallyCouldBeAnyClassYouWant("exit", "Good Bye! :)"));
  }

  void showMenu() {
    nifty.showPopup(screen, popup.getId(), null);
    nifty.subscribe(screen, popup.findNiftyControl("#menu", Menu.class).getId(), MenuItemActivatedEvent.class,
        new MenuItemActivatedEventSubscriber());
  }  
  
  String getStartScreen() {
    return "start";
  }  
  
  String getMainXML() {
    return "menu/menu.xml";
  }  
  
  String getTitle() {
    return "Nifty Menu Example";
  }
  
  void prepareStart(Nifty nifty) {
    // nothing to do
  }

  class MenuItemActivatedEventSubscriber implements EventTopicSubscriber<MenuItemActivatedEvent> {    
    void onEvent(String id,  MenuItemActivatedEvent event) {
      
      final ThisReallyCouldBeAnyClassYouWant item = (ThisReallyCouldBeAnyClassYouWant) event.getItem();

      Label label = screen.findNiftyControl("textOut", Label.class);
      label.setText(item.text + " [" + item.key + "]");

      nifty.closePopup(popup.getId(), new EndNotify() {
        
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
  class ThisReallyCouldBeAnyClassYouWant {
    
    String key;    
    String text;

    ThisReallyCouldBeAnyClassYouWant( String key,  String text) {
      this.key = key;
      this.text = text;
    }
  }
}