public static class TextFieldDemoStartScreen implements ScreenController, NiftyExample {
  Nifty nifty;
  Screen screen;
  
  void bind(Nifty newNifty, Screen newScreen) {
    screen = newScreen;
    nifty = newNifty;

    // dynamically add another Textfield
    Element dynamicParent = screen.findElementById("dynamic-parent");

    PanelCreator panelCreator = new PanelCreator();
    panelCreator.setChildLayout("horizontal");
    panelCreator.setHeight("8px");
    panelCreator.create(newNifty, screen, dynamicParent);

    panelCreator = new PanelCreator();
    panelCreator.setId("bla");
    panelCreator.setChildLayout("horizontal");
    Element row = panelCreator.create(newNifty, screen, dynamicParent);

    LabelBuilder labelBuilder = new LabelBuilder();
    labelBuilder.text("Dynamic:");
    labelBuilder.width("150px");
    labelBuilder.align(ElementBuilder.Align.Left);
    labelBuilder.textVAlign(ElementBuilder.VAlign.Center);
    labelBuilder.textHAlign(ElementBuilder.Align.Left);
    labelBuilder.build(newNifty, screen, row);

    TextFieldBuilder textFieldBuilder = new TextFieldBuilder();
    textFieldBuilder.text("Dynamically created TextField");
    textFieldBuilder.build(nifty, screen, row);

    Element element = screen.findElementById("labelName");
    if (element != null) {
      element.getRenderer(TextRenderer.class).setText("Name:");
    }
  }
  
  void onStartScreen() {
    screen.findNiftyControl("maxLengthTest", TextField.class).setMaxLength(5);
    screen.findElementById("name").setFocus();
    screen.findNiftyControl("name", TextField.class).setCursorPosition(3);
    System.out.println(screen.getFocusHandler().toString());
  }
  
  void onEndScreen() {
    System.out.println("ip: " + screen.findControl("ip", TextFieldControl.class).getText());
  }

  void back() {
    System.out.println(screen.findElementById("password").getControl(TextFieldControl.class).getText());
    nifty.fromXml("all/intro.xml", "menu");
  }  
  
  String getStartScreen() {
    return "start";
  }  
  
  String getMainXML() {
    return "textfield/textfield.xml";
  }  
  
  String getTitle() {
    return "Nifty Textfield Demonstation";
  }
  
  void prepareStart(Nifty nifty) {
    // nothing to do
  }
}