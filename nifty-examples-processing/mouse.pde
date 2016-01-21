public static class MouseStartScreen implements ScreenController, NiftyExample {
  Nifty nifty;
  Screen screen;  
  Label mousePrimaryText;  
  Label mouseSecondaryText;  
  Label mouseTertiaryText;  
  Label mouseMoveText;  
  Label mouseWheelText;  
  Label mouseEventsPrimaryText;  
  Label mouseEventsSecondaryText;  
  Label mouseEventsTertiaryText;  
  Label mouseEventsMoveText;  
  Label mouseEventsWheelText;  
  Label mouseEventsText;
  
  void bind( Nifty newNifty,  Screen newScreen) {
    this.nifty = newNifty;
    this.screen = newScreen;
    this.mousePrimaryText = screen.findNiftyControl("mousePrimaryText", Label.class);
    this.mouseSecondaryText = screen.findNiftyControl("mouseSecondaryText", Label.class);
    this.mouseTertiaryText = screen.findNiftyControl("mouseTertiaryText", Label.class);
    this.mouseMoveText = screen.findNiftyControl("mouseMoveText", Label.class);
    this.mouseWheelText = screen.findNiftyControl("mouseWheelText", Label.class);

    this.mouseEventsPrimaryText = screen.findNiftyControl("mouseEventsPrimaryText", Label.class);
    this.mouseEventsSecondaryText = screen.findNiftyControl("mouseEventsSecondaryText", Label.class);
    this.mouseEventsTertiaryText = screen.findNiftyControl("mouseEventsTertiaryText", Label.class);
    this.mouseEventsMoveText = screen.findNiftyControl("mouseEventsMoveText", Label.class);
    this.mouseEventsWheelText = screen.findNiftyControl("mouseEventsWheelText", Label.class);
    this.mouseEventsText = screen.findNiftyControl("mouseEventsText", Label.class);
  }
  
  void onStartScreen() {
    System.out.println(screen.debugOutput());
  }
  
  void onEndScreen() {
  }
  
  void quit() {
    nifty.setAlternateKeyForNextLoadXml("fade");
    nifty.fromXml("all/intro.xml", "menu");
  }
  
  // Primary

  void primaryClick() {
    mousePrimaryText.setText("clicked");
  }

  void primaryRelease() {
    mousePrimaryText.setText("");
  }

  void primaryClickMouseMoved() {
    mousePrimaryText.setText("clicked mouse moved");
  }

  // Secondary

  void secondaryClick() {
    mouseSecondaryText.setText("clicked");
  }

  void secondaryClickMouseMove() {
    mouseSecondaryText.setText("clicked mouse moved");
  }

  void secondaryRelease() {
    mouseSecondaryText.setText("");
  }

  // Tertiary

  void tertiaryClick() {
    mouseTertiaryText.setText("clicked");
  }

  void tertiaryClickMouseMove() {
    mouseTertiaryText.setText("clicked mouse moved");
  }

  void tertiaryRelease() {
    mouseTertiaryText.setText("");
  }

  // Mouse over

  void mouseOver(Element element,  NiftyMouseInputEvent event) {
    mouseMoveText.setText(event.getMouseX() + ", " + event.getMouseY());
  }

  // Mouse wheel

  void mouseWheel(Element element,  NiftyMouseInputEvent event) {
    mouseWheelText.setText(String.valueOf(event.getMouseWheel()));
  }

  // The new event subscriber method. please note that these methods really could be anywhere as long as
  // you call nifty.processAnnotations() on the class that implements them.

  // Primary

  @NiftyEventSubscriber(id = "mouseEvents")
  void onElementPrimaryClick(String id, NiftyMousePrimaryClickedEvent event) {
    mouseEventsPrimaryText.setText("clicked");
  }

  @NiftyEventSubscriber(id = "mouseEvents")
  void onElementPrimaryClickMove(String id, NiftyMousePrimaryClickedMovedEvent event) {
    mouseEventsPrimaryText.setText("clicked mouse move");
  }

  @NiftyEventSubscriber(id = "mouseEvents")
  void onElementPrimaryRelease(String id, NiftyMousePrimaryReleaseEvent event) {
    mouseEventsPrimaryText.setText("");
  }

  // Secondary

  @NiftyEventSubscriber(id = "mouseEvents")
  void onElementSecondaryClick(String id, NiftyMouseSecondaryClickedEvent event) {
    mouseEventsSecondaryText.setText("clicked");
  }

  @NiftyEventSubscriber(id = "mouseEvents")
  void onElementSecondaryClickMove(String id, NiftyMouseSecondaryClickedMovedEvent event) {
    mouseEventsSecondaryText.setText("clicked mouse moved");
  }

  @NiftyEventSubscriber(id = "mouseEvents")
  void onElementSecondaryRelease(String id, NiftyMouseSecondaryReleaseEvent event) {
    mouseEventsSecondaryText.setText("");
  }

  // Tertiary

  @NiftyEventSubscriber(id = "mouseEvents")
  void onElementTertiaryClick(String id, NiftyMouseTertiaryClickedEvent event) {
    mouseEventsTertiaryText.setText("clicked");
  }

  @NiftyEventSubscriber(id = "mouseEvents")
  void onElementTertiaryClickMove(String id, NiftyMouseTertiaryClickedMovedEvent event) {
    mouseEventsTertiaryText.setText("clicked mouse moved");
  }

  @NiftyEventSubscriber(id = "mouseEvents")
  void onElementTertiaryRelease(String id, NiftyMouseTertiaryReleaseEvent event) {
    mouseEventsTertiaryText.setText("");
  }

  // Mouse move

  @NiftyEventSubscriber(id = "mouseEvents")
  void onElementMouseMove(String id,  NiftyMouseMovedEvent event) {
    mouseEventsMoveText.setText(event.getMouseX() + ", " + event.getMouseY());
  }

  // Mouse wheel

  @NiftyEventSubscriber(id = "mouseEvents")
  void onElementMouseWheel(String id,  NiftyMouseWheelEvent event) {
    mouseEventsWheelText.setText(String.valueOf(event.getMouseWheel()));
  }

  // General

  @NiftyEventSubscriber(id = "mouseEvents")
  void onElementMouse(String id,  NiftyMouseEvent event) {
    mouseEventsText.setText(
        id + " -> " + event.getMouseX() + ", " + event.getMouseY() + ", " + event.getMouseWheel() + "\n" +
            event.isButton0Down() + ", " + event.isButton1Down() + ", " + event.isButton2Down() + "\n" +
            event.isButton0Release() + ", " + event.isButton1Release() + ", " + event.isButton2Release());
  }  
  
  String getStartScreen() {
    return "start";
  }  
  
  String getMainXML() {
    return "mouse/mouse.xml";
  }  
  
  String getTitle() {
    return "Nifty Mouse Control Example";
  }
  
  void prepareStart( Nifty nifty) {
    // get the NiftyMouse interface that gives us access to all mouse cursor related stuff
    NiftyMouse niftyMouse = nifty.getNiftyMouse();

    try {
      // register/load a mouse cursor (this would be done somewhere at the beginning)
      niftyMouse.registerMouseCursor("mouseId", "nifty-cursor.png", 0, 0);

      // change the cursor to the one we've loaded before
      niftyMouse.enableMouseCursor("mouseId");
    } catch (IOException e) {
      System.err.println("Failed to load mouse cursor!");
    }

    // we could set the position like so
    niftyMouse.setMousePosition(20, 20);
  }
}