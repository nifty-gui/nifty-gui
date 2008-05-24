package de.lessvoid.nifty.examples.intro;

import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 * Menu.
 * @author void
 */
public class Menu implements ScreenController {

  /**
   * the nifty instance.
   */
  private Nifty nifty;
  
  /**
   * the screen this menu belongs to.
   */
  private Screen screen;

  /**
   * 
   */
  public void bind(Nifty niftyParam, Screen screenParam) {
    this.nifty = niftyParam;
    this.screen = screenParam;
  }

  /**
   * on start screen.
   */
  public final void onStartScreen() {
  }

  /**
   * just goto the next screen.
   */
  public final void onStartInteractive() {
    screen.findElementByName("helloWorld").setFocus();
  }

  /**
   * on end screen.
   */
  public final void onEndScreen() {
  }
  
  public void helloWorld() {
    nifty.getCurrentScreen().endScreen(new EndNotify() {
      public void perform() {
        nifty.fromXml("helloworld/helloworld.xml");
      }
    });
  }

  public void textfield() {
    nifty.getCurrentScreen().endScreen(new EndNotify() {
      public void perform() {
        nifty.fromXml("textfield/textfield.xml");
      }
    });
  }

  /**
   * exit.
   */
  public void exit() {
    nifty.showPopup(screen, "popupExit");
  }
  
  public void popupExit(final String exit) {
    nifty.closePopup("popupExit");
    if ("yes".equals(exit)) {
      nifty.gotoScreen("outro");    
    }
  }
}
