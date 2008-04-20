package de.lessvoid.nifty.examples.helloworld;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 * ScreenController for Hello World Example.
 * @author void
 */
public class HelloWorldStartScreen implements ScreenController {

  /** nifty instance. */
  private Nifty nifty;

  /**
   * Bind this ScreenController to a screen.
   * @param newNifty nifty
   * @param newScreen screen
   */
  public final void bind(final Nifty newNifty, final Screen newScreen) {
    this.nifty = newNifty;
    
  }

  /**
   * on start screen.
   */
  public final void onStartScreen() {
  }

  /**
   * on start screen interactive.
   */
  public final void onStartInteractive() {
  }

  /**
   * on end screen.
   */
  public final void onEndScreen() {
  }

  /**
   * quit method called from the helloworld.xml.
   */
  public final void quit() {
    nifty.exit();
  }

}
