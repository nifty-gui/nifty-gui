package de.lessvoid.nifty.examples.helloniftybuilder;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 * ScreenController for Hello World Example.
 * @author void
 */
public class HelloNiftyBuilderExampleScreenController implements ScreenController {

  public void bind(final Nifty newNifty, final Screen newScreen) {
    System.out.println("bind()");
  }

  public void onStartScreen() {
    System.out.println("onStartScreen()");
  }

  public void onEndScreen() {
    System.out.println("onEndScreen()");
  }
}
