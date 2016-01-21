public static class TextAlignStartScreen implements ScreenController, NiftyExample {
  Nifty nifty;
  
  void bind( Nifty newNifty,  Screen newScreen) {
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
    return "textalign/textalign.xml";
  }  
  
  String getTitle() {
    return "Nifty Textalign Example";
  }
  
  void prepareStart(Nifty nifty) {
    // nothing to do
  }
}