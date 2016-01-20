public static class HelloWorldStartScreen implements ScreenController, NiftyExample {
  Nifty nifty;

  void bind(Nifty newNifty, Screen newScreen) {
    this.nifty = newNifty;
  }
  
  void onStartScreen() {
  }
  
  void onEndScreen() {
  }

  void quit() {
    nifty.setAlternateKeyForNextLoadXml("fade");
    nifty.fromXml("all/intro.xml", "menu");
  }  
  
  String getStartScreen() {
    return "start";
  }  
  
  String getMainXML() {
    return "helloworld/helloworld.xml";
  }  
  
  String getTitle() {
    return "Nifty Hello World";
  }
  
  void prepareStart(Nifty nifty) {
    // get the NiftyMouse interface that gives us access to all mouse cursor related stuff
    NiftyMouse niftyMouse = nifty.getNiftyMouse();

    try {
      // register/load a mouse cursor (this would be done somewhere at the beginning)
      niftyMouse.registerMouseCursor("mouseId", "nifty-cursor.png", 0, 0);

      // change the cursor to the one we've loaded before
      niftyMouse.enableMouseCursor("mouseId");
    } catch (IOException e) {
      System.err.println("Loading the mouse cursor failed.");
    }

    // we could set the position like so
    niftyMouse.setMousePosition(20, 20);
  }
}