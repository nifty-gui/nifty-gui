package de.lessvoid.nifty.examples.style.label;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.examples.NiftyExample;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

import javax.annotation.Nonnull;

/**
 * @author void
 */
public class LabelStartScreen implements ScreenController, NiftyExample {
  private Nifty nifty;
  @Override
  public final void bind(@Nonnull final Nifty newNifty, @Nonnull final Screen newScreen) {
    this.nifty = newNifty;
  }

  @Override
  public final void onStartScreen() {
  }

  @Override
  public final void onEndScreen() {
  }

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
