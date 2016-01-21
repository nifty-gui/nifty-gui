import de.lessvoid.nifty.elements.Element;

public interface NiftyExample {
  String getStartScreen();
  String getMainXML();
  String getTitle();
  void prepareStart(Nifty nifty);
}

public static class AllExamples implements NiftyExample {
  String ALL_INTRO_XML = "all/intro.xml";
  String startScreen;

  AllExamples() {
    this("start");
  }

  AllExamples(String screen) {
    startScreen = screen;
  }
  
  String getStartScreen() {
    return startScreen;
  }

  String getMainXML() {
    return ALL_INTRO_XML;
  }

  String getTitle() {
    return "Nifty Examples";
  }

  void prepareStart(Nifty nifty) {
    // nothing to do
  }
}

public static class MenuController implements ScreenController {
  Nifty nifty;
  Screen screen;

  void bind(Nifty niftyParam, Screen screenParam) {
    this.nifty = niftyParam;
    this.screen = screenParam;
    hideIfThere("thumbHelloWorld");
    hideIfThere("thumbHint");
    hideIfThere("thumbMouse");
    hideIfThere("thumbMenu");
    hideIfThere("thumbDragAndDrop");
    hideIfThere("thumbTextAlign");
    hideIfThere("thumbTextField");
    hideIfThere("thumbDropDownList");
    hideIfThere("thumbScrollpanel");
    hideIfThere("thumbMultiplayer");
    hideIfThere("thumbConsole");
    hideIfThere("thumbCredits");
    hideIfThere("thumbExit");
  }

  void hideIfThere(String elementName) {
    Element element = screen.findElementById(elementName);
    if (element != null) {
      element.hide();
    }
  }

  void onStartScreen() {
  }

  void onEndScreen() {
  }

  void helloWorld() {
    nifty.fromXml("helloworld/helloworld.xml", "start");
  }

  void hint() {
    nifty.fromXml("hint/hint.xml", "start");
  }

  void mouse() {
    nifty.fromXml("mouse/mouse.xml", "start");
  }

  void menu() {
    nifty.fromXml("menu/menu.xml", "start");
  }

  void dragAndDrop() {
    nifty.fromXml("dragndrop/dragndrop.xml", "start");
  }

  void textfield() {
    nifty.fromXml("textfield/textfield.xml", "start");
  }

  void textalign() {
    nifty.fromXml("textalign/textalign.xml", "start");
  }

  void multiplayer() {
    nifty.fromXml("multiplayer/multiplayer.xml", "start");
  }

  void console() {
    nifty.fromXml("console/console.xml", "start");
  }

  void dropDown() {
    nifty.fromXml("controls/controls.xml", "start");
  }

  void scrollpanel() {
    nifty.fromXml("scroll/scroll.xml", "start");
  }

  void credits() {
    nifty.gotoScreen("outro");
  }

  void exit() {
    nifty.createPopupWithId("popupExit", "popupExit");
    nifty.showPopup(screen, "popupExit", null);
  }

  void popupExit(final String exit) {
    nifty.closePopup("popupExit", new EndNotify() {
      public void perform() {
        if ("yes".equals(exit)) {
          nifty.setAlternateKey("fade");
          nifty.exit();
        }
      }
    }
    );
  }
}

public static class OutroController implements ScreenController, KeyInputHandler {
  Nifty nifty;
  Screen screen;
  boolean escape;

  void bind(Nifty newNifty, Screen newScreen) {
    this.nifty = newNifty;
    this.screen = newScreen;

    screen.findElementById("1").hideWithoutEffect();
    screen.findElementById("2").hideWithoutEffect();
    screen.findElementById("3").hideWithoutEffect();
    screen.findElementById("4").hideWithoutEffect();
    screen.findElementById("5").hideWithoutEffect();
    screen.findElementById("6").hideWithoutEffect();
    screen.findElementById("7").hideWithoutEffect();
    screen.findElementById("8").hideWithoutEffect();
  }

  void onStartScreen() {
    Element theEndLabel = screen.findElementById("theEndLabel");
    if (theEndLabel != null) {
      theEndLabel.startEffect(EffectEventId.onCustom);
      theEndLabel.show();
    }

    Element myScrollStuff = screen.findElementById("myScrollStuff");
    if (myScrollStuff != null) {
      CustomControlCreator endScroller = new CustomControlCreator("endscroller-page-1");
      endScroller.create(nifty, screen, myScrollStuff);
      myScrollStuff.startEffect(EffectEventId.onCustom);
      screen.findElementById("1").show();
    }
  }

  void scrollEnd() {
    if (escape) {
      return;
    }
    Element theEndLabel = screen.findElementById("theEndLabel");
    if (theEndLabel != null) {
      theEndLabel.stopEffect(EffectEventId.onCustom);
    }

    Element myScrollStuff = screen.findElementById("myScrollStuff");
    if (myScrollStuff != null) {
      nifty.setAlternateKeyForNextLoadXml("fade");
      nifty.gotoScreen("menu");
    }
  }

  void onEndScreen() {
  }

  void shizzleHide(String id) {
    if (escape) {
      return;
    }
    screen.findElementById(id).hide();
  }

  void shizzleShow(String id) {
    if (escape) {
      return;
    }
    if (!id.equals("end")) {
      screen.findElementById(id).show();
    }
  }

  boolean keyEvent(NiftyInputEvent inputEvent) {
    if (inputEvent == NiftyStandardInputEvent.Escape) {
      escape = true;
      nifty.setAlternateKey("exit");
      nifty.setAlternateKeyForNextLoadXml("fade");
      nifty.fromXml("all/intro.xml", "menu");
      return true;
    }
    return false;
  }
}

public static class SplashController implements ScreenController, KeyInputHandler {
  Nifty nifty;

  void bind(Nifty newNifty, Screen screen) {
    this.nifty = newNifty;
  }

  void onStartScreen() {
    nifty.gotoScreen("start2");
  }

  void onEndScreen() {
  }

  boolean keyEvent(NiftyInputEvent inputEvent) {
    if (inputEvent == NiftyStandardInputEvent.Escape) {
      nifty.setAlternateKey("exit");
      nifty.gotoScreen("menu");
      return true;
    }
    return false;
  }
}

public static class SplashController2 implements ScreenController, KeyInputHandler {
  Nifty nifty;

  void bind(Nifty newNifty, Screen screen) {
    this.nifty = newNifty;
  }

  void onStartScreen() {
    nifty.setAlternateKeyForNextLoadXml("fade");
    nifty.gotoScreen("menu");
  }

  void onEndScreen() {
  }

  boolean keyEvent(NiftyInputEvent inputEvent) {
    if (inputEvent == NiftyStandardInputEvent.Escape) {
      nifty.setAlternateKey("exit");
      nifty.gotoScreen("menu");
      return true;
    }
    return false;
  }
}