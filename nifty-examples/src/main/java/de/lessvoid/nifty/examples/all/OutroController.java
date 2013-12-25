package de.lessvoid.nifty.examples.all;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.dynamic.CustomControlCreator;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.input.NiftyStandardInputEvent;
import de.lessvoid.nifty.screen.KeyInputHandler;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

import javax.annotation.Nonnull;

/**
 * Outro implementation for the nifty demo Outro screen.
 * @author void
 */
public class OutroController implements ScreenController, KeyInputHandler {
  private Nifty nifty;
  private Screen screen;
  private boolean escape;

  @Override
  public void bind(final Nifty newNifty, final Screen newScreen) {
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

  @Override
  public final void onStartScreen() {
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

  public void scrollEnd() {
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

  @Override
  public void onEndScreen() {
  }

  public void shizzleHide(final String id) {
    if (escape) return;
    screen.findElementById(id).hide();
  }

  public void shizzleShow(@Nonnull final String id) {
    if (escape) return;
    if (!id.equals("end")) {
      screen.findElementById(id).show();
    }
  }

  @Override
  public boolean keyEvent(@Nonnull final NiftyInputEvent inputEvent) {
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
