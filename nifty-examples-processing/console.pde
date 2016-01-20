public static class ConsoleDemoStartScreen implements ScreenController, KeyInputHandler, NiftyExample {
  Nifty nifty;
  Screen screen;
  
  Element consolePopup;
  boolean consoleVisible = false;
  boolean allowConsoleToggle = true;
  boolean firstConsoleShow = true;
  
  void bind( Nifty newNifty,  Screen newScreen) {
    nifty = newNifty;
    screen = newScreen;
    screen.addKeyboardInputHandler(new DefaultInputMapping(), this);
    consolePopup = nifty.createPopup("consolePopup");
  }
  
  void onStartScreen() {
  }
  
  void onEndScreen() {
  }

  void back() {
    nifty.fromXml("all/intro.xml", "menu");
  }
  
  boolean keyEvent( NiftyInputEvent inputEvent) {
    if (inputEvent == NiftyStandardInputEvent.ConsoleToggle) {
      toggleConsole();
      return true;
    } else {
      return false;
    }
  }

  void toggleConsole() {
    if (allowConsoleToggle) {
      allowConsoleToggle = false;
      if (consoleVisible) {
        closeConsole();
      } else {
        openConsole();
      }
    }
  }

  void openConsole() {
    String id = consolePopup != null ? consolePopup.getId() : null;
    if (id == null) {
      return;
    }
    nifty.showPopup(screen, id, consolePopup.findElementById("console#textInput"));
    screen.processAddAndRemoveLayerElements();

    if (firstConsoleShow) {
      firstConsoleShow = false;
      Console console = screen.findNiftyControl("console", Console.class);
      if (console != null) {
        console.output("Nifty Console Demo\nVersion: 2.0");
      }
    }

    consoleVisible = true;
    allowConsoleToggle = true;
  }

  void closeConsole() {
    String id = consolePopup != null ? consolePopup.getId() : null;
    if (id == null) {
      return;
    }
    nifty.closePopup(id, new EndNotify() {
      
      public void perform() {
        consoleVisible = false;
        allowConsoleToggle = true;
      }
    });
  }

  @NiftyEventSubscriber(id = "console")
  void onConsoleCommand(String id,  ConsoleExecuteCommandEvent command) {
    Console console = screen.findNiftyControl("console", Console.class);
    if (console != null) {
      console.output("your input was: " + command.getCommandLine() + " [" + command.getArgumentCount() + " parameter" +
          "(s)]");
    }
    if ("exit".equals(command.getCommand())) {
      back();
    }
  }  
  
  String getStartScreen() {
    return "start";
  }  
  
  String getMainXML() {
    return "console/console.xml";
  }  
  
  String getTitle() {
    return "Nifty Console Demonstation";
  }
  
  void prepareStart(Nifty nifty) {
    // nothing to do
  }
}

public static class ConsoleSameScreenStartScreen implements ScreenController, KeyInputHandler, NiftyExample { 
  Nifty nifty;  
  Screen screen;
  boolean consoleVisible = false;
  boolean allowConsoleToggle = true;  
  Element oldFocusElement;  
  Element consoleElementFocus;  
  Element consoleLayer;
  
  void bind( Nifty newNifty,  Screen newScreen) {
    nifty = newNifty;
    screen = newScreen;
    screen.addKeyboardInputHandler(new DefaultInputMapping(), this);

    Element consoleElement = screen.findElementById("console");
    if (consoleElement != null) {
      consoleElementFocus = consoleElement.findElementById("#textInput");
    }
    consoleLayer = screen.findElementById("consoleLayer");
  }
  
  void onStartScreen() {
    removeConsoleElementFromFocusHandler();
  }
  
  void onEndScreen() {
  }

  void back() {
    if (nifty != null) {
      nifty.fromXml("all/intro.xml", "menu");
    }
  }
  
  boolean keyEvent( NiftyInputEvent inputEvent) {
    if (inputEvent == NiftyStandardInputEvent.ConsoleToggle) {
      toggleConsole();
      return true;
    } else {
      return false;
    }
  }

  @NiftyEventSubscriber(id = "console")
  void onConsoleCommand(String id,  ConsoleExecuteCommandEvent command) {
    if (screen == null) {
      return;
    }
    Console console = screen.findNiftyControl("console", Console.class);
    if (console != null) {
      console.output("your input was: " + command.getCommandLine() + " [" + command.getArgumentCount() + " parameter" +
          "(s)]");
    }
    if ("exit".equals(command.getCommand())) {
      back();
    }
  }

  void toggleConsole() {
    if (allowConsoleToggle) {
      allowConsoleToggle = false;
      if (consoleVisible) {
        closeConsole();
      } else {
        openConsole();
      }
    }
  }

  void openConsole() {
    if (consoleLayer != null && screen != null && consoleElementFocus != null) {
      consoleLayer.showWithoutEffects();
      consoleLayer.startEffect(EffectEventId.onStartScreen, new EndNotify() {
        
        public void perform() {
          oldFocusElement = screen.getFocusHandler().getKeyboardFocusElement();

          // add the consoleElement to the focushandler, when it's not yet added already
          addConsoleElementToFocusHandler();
          consoleElementFocus.setFocus();

          consoleVisible = true;
          allowConsoleToggle = true;
        }
      });
    }
  }

  void closeConsole() {
    if (consoleLayer != null) {
      consoleLayer.startEffect(EffectEventId.onEndScreen, new EndNotify() {
        
        public void perform() {
          consoleLayer.hideWithoutEffect();

          consoleVisible = false;
          allowConsoleToggle = true;

          if (oldFocusElement != null) {
            oldFocusElement.setFocus();
          }

          removeConsoleElementFromFocusHandler();
        }
      });
    }
  }

  void addConsoleElementToFocusHandler() {
    String id = consoleElementFocus == null ? null : consoleElementFocus.getId();
    if (id == null) {
      return;
    }
    if (screen != null && screen.getFocusHandler().findElement(id) == null) {
      screen.getFocusHandler().addElement(consoleElementFocus);
    }
  }

  void removeConsoleElementFromFocusHandler() {
    if (screen != null) {
      screen.getFocusHandler().remove(consoleElementFocus);
    }
  }  
  
  String getStartScreen() {
    return "start";
  }  
  
  String getMainXML() {
    return "console/console-samescreen.xml";
  }  
  
  String getTitle() {
    return "Nifty Console Same Screen Demonstation";
  }
  
  void prepareStart(Nifty nifty) {
    // nothing to do
  }
}