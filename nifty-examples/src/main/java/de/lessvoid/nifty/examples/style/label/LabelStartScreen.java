package de.lessvoid.nifty.examples.style.label;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.examples.NiftyExample;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

import javax.annotation.Nonnull;

/**
 * ScreenController for Hello World Example.
 *
 * @author void
 */
public class LabelStartScreen implements ScreenController, NiftyExample {

  /**
   * nifty instance.
   */
  private Nifty nifty;

  /**
   * Bind this ScreenController to a screen.
   *
   * @param newNifty  nifty
   * @param newScreen screen
   */
  @Override
  public final void bind(final Nifty newNifty, final Screen newScreen) {
    this.nifty = newNifty;
  }

  /**
   * on start screen interactive.
   */
  @Override
  public final void onStartScreen() {
  }

  /**
   * on end screen.
   */
  @Override
  public final void onEndScreen() {
  }

  /**
   * quit method called from the helloworld.xml.
   */
  public final void quit() {
    nifty.fromXml("all/intro.xml", "menu");
  }

  @Nonnull
  @Override
  public String getStartScreen() {
    return "start";
  }

  @Nonnull
  @Override
  public String getMainXML() {
    return "style/label/label.xml";
  }

  @Nonnull
  @Override
  public String getTitle() {
    return "Nifty Style Label Example";
  }

  @Override
  public void prepareStart(Nifty nifty) {
    // nothing
  }
}
