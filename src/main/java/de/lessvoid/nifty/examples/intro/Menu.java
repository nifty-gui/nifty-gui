package de.lessvoid.nifty.examples.intro;

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
   * 
   */
  public void bind(Nifty newNifty, Screen screen) {
    this.nifty = newNifty;
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
    nifty.getCurrentScreen().findElementByName("mainMenu").setFocus();
  }

  /**
   * on end screen.
   */
  public final void onEndScreen() {
  }
  
  /**
   * quit.
   */
  public void quit() {
    nifty.exit();
  }
  
  public void helloWorld() {
    nifty.fromXml("helloworld/helloworld.xml");
  }
}
