import de.lessvoid.nifty.controls.ScrollPanel.AutoScroll;

public static class ScrollDemoStartScreen implements ScreenController, NiftyExample {
  Nifty nifty;
  Screen screen;
  
  void bind( Nifty newNifty,  Screen newScreen) {
    screen = newScreen;
    nifty = newNifty;

    Element myScrollStuff = screen.findElementById("myScrollStuff");
    if (myScrollStuff != null) {
      for (int i = 0; i < 100; i++) {
        addLabel(myScrollStuff, "hello " + i);
      }
      screen.layoutLayers();

      ScrollPanelControl scrollPanel = screen.findControl("scrollbarPanel", ScrollPanelControl.class);
      scrollPanel.setUp(0, myScrollStuff.getHeight() / 100.f, 0, myScrollStuff.getHeight() / 10.f, AutoScroll.OFF);
      scrollPanel.setVerticalPos(0.0f);
    }
  }
  
  void onStartScreen() {
  }
  
  void onEndScreen() {
  }
  
  void back() {
    nifty.fromXml("all/intro.xml", "menu");
  }

  void addLabel( Element myScrollStuff,  String text) {
    new LabelBuilder(NiftyIdCreator.generate(), text).build(nifty, screen, myScrollStuff);
  }  
  
  String getStartScreen() {
    return "start";
  }  
  
  String getMainXML() {
    return "scroll/scroll.xml";
  }  
  
  String getTitle() {
    return "Nifty Scrolling Demonstation";
  }
  
  void prepareStart(Nifty nifty) {
    // nothing to do
  }
}