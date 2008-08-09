package de.lessvoid.nifty.examples.all;

import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 * Menu.
 * @author void
 */
public class MenuController implements ScreenController {

  /**
   * the nifty instance.
   */
  private Nifty nifty;

  /**
   * the screen this menu belongs to.
   */
  private Screen screen;

  /**
   * bind.
   * @param niftyParam niftyParam
   * @param screenParam screenParam
   */
  public void bind(final Nifty niftyParam, final Screen screenParam) {
    this.nifty = niftyParam;
    this.screen = screenParam;
  }

  /**
   * just goto the next screen.
   */
  public final void onStartScreen() {
    this.screen.setDefaultFocus();
  }

  /**
   * on end screen.
   */
  public final void onEndScreen() {
  }

  /**
   * helloWorld.
   */
  public void helloWorld() {
    nifty.getCurrentScreen().endScreen(new EndNotify() {
      public void perform() {
        nifty.fromXml("helloworld/helloworld.xml", "start");
      }
    });
  }

  /**
   * textfield.
   */
  public void textfield() {
    nifty.getCurrentScreen().endScreen(new EndNotify() {
      public void perform() {
        nifty.fromXml("textfield/textfield.xml", "start");
      }
    });
  }

  /**
   * textalign.
   */
  public void textalign() {
    nifty.getCurrentScreen().endScreen(new EndNotify() {
      public void perform() {
        nifty.fromXml("textalign/textalign.xml", "start");
      }
    });
  }
  /**
   * multiplayer.
   */
  public void multiplayer() {
    nifty.getCurrentScreen().endScreen(new EndNotify() {
      public void perform() {
        nifty.fromXml("multiplayer/multiplayer.xml", "start");
      }
    });
  }

  /**
   * exit.
   */
  public void exit() {
    nifty.showPopup(screen, "popupExit");
  }

  /**
   * popupExit.
   * @param exit exit string
   */
  public void popupExit(final String exit) {
    nifty.closePopup("popupExit");
    if ("yes".equals(exit)) {
      nifty.gotoScreen("outro");
    }
  }
}
