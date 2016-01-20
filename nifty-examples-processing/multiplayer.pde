import de.lessvoid.nifty.controls.Controller;

public static class MultiplayerPanelControl implements Controller {
  Nifty nifty;
  Screen screen;
  Element element;
  
  void bind(
       Nifty niftyParam,
       Screen screenParam,
       Element newElement,
       Parameters properties) {
    nifty = niftyParam;
    screen = screenParam;
    element = newElement;
  }
  
  void init( Parameters parameter) {
  }
  
  void onStartScreen() {
    setDifficulty("easy");
  }
  
  void onFocus(boolean getFocus) {
  }
  
  boolean inputEvent( NiftyInputEvent inputEvent) {
    return false;
  }
  
  void onEndScreen() {
  }

  void removePanel() {
    nifty.removeElement(screen, element);
  }

  void setDifficulty(String mode) {
    element.findElementById("#easy").setStyle("unselected");
    element.findElementById("#medium").setStyle("unselected");
    element.findElementById("#hard").setStyle("unselected");
    element.findElementById("#expert").setStyle("unselected");

    if ("easy".equals(mode)) {
      element.findElementById("#easy").setStyle("selected");
    } else if ("medium".equals(mode)) {
      element.findElementById("#medium").setStyle("selected");
    } else if ("hard".equals(mode)) {
      element.findElementById("#hard").setStyle("selected");
    } else if ("expert".equals(mode)) {
      element.findElementById("#expert").setStyle("selected");
    }
  }
}

public static class StartScreenController implements ScreenController, NiftyExample {
  Nifty nifty;
  Screen screen;
  int id = 10000;
  
  void bind( Nifty newNifty,  Screen newScreen) {
    this.nifty = newNifty;
    this.screen = newScreen;
    addPanel();
  }
  
  void onStartScreen() {
  }
  
  void onEndScreen() {
  }

  void quit() {
    nifty.fromXml("all/intro.xml", "menu");
  }

  void addPanel() {
    CustomControlCreator createMultiplayerPanel = new CustomControlCreator(String.valueOf(id++), "multiplayerPanel");
    createMultiplayerPanel.create(nifty, screen, screen.findElementById("box-parent"));
  }

  @NiftyEventSubscriber(pattern = ".*#imageSelect")
  void onImageSelectSelectionChanged(String id,  ImageSelectSelectionChangedEvent event) {
    System.out.println("ImageSelect [" + id + "] changed selection to [" + event.getSelectedIndex() + "]");
  }  
  
  String getStartScreen() {
    return "start";
  }  
  
  String getMainXML() {
    return "multiplayer/multiplayer.xml";
  }  
  
  String getTitle() {
    return "Nifty Multiplayer Example";
  }
  
  void prepareStart(Nifty nifty) {
    // nothing to do
  }
}