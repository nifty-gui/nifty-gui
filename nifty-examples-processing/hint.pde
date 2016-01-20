public static class HintScreen implements ScreenController, NiftyExample {
  Nifty nifty;
  
  void bind( final Nifty newNifty,  final Screen newScreen) {
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
    return "hint/hint.xml";
  }  
  
  String getTitle() {
    return "Nifty Hint Example";
  }
  
  void prepareStart(Nifty nifty) {
    // nothing
  }
}