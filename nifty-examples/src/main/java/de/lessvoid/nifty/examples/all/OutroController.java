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

/**
 * Outro implementation for the nifty demo Outro screen.
 * @author void
 */
public class OutroController implements ScreenController, KeyInputHandler {
  private Nifty nifty;
  private Screen screen;
  private boolean escape;

  public void bind(final Nifty newNifty, final Screen newScreen) {
    this.nifty = newNifty;
    this.screen = newScreen;

    screen.findElementByName("1").hideWithoutEffect();
    screen.findElementByName("2").hideWithoutEffect();
    screen.findElementByName("3").hideWithoutEffect();
    screen.findElementByName("4").hideWithoutEffect();
    screen.findElementByName("5").hideWithoutEffect();
    screen.findElementByName("6").hideWithoutEffect();
    screen.findElementByName("7").hideWithoutEffect();
    screen.findElementByName("8").hideWithoutEffect();
  }

  public final void onStartScreen() {
    Element theEndLabel = screen.findElementByName("theEndLabel");
    if (theEndLabel != null) {
      theEndLabel.startEffect(EffectEventId.onCustom);
      theEndLabel.show();
    }

    Element myScrollStuff = screen.findElementByName("myScrollStuff");
    if (myScrollStuff != null) {
      CustomControlCreator endScroller = new CustomControlCreator("endscroller-page-1");
      endScroller.create(nifty, screen, myScrollStuff);
      myScrollStuff.startEffect(EffectEventId.onCustom);
      screen.findElementByName("1").show();
    }
  }

  public void scrollEnd() {
    if (escape) {
      return;
    }
    Element theEndLabel = screen.findElementByName("theEndLabel");
    if (theEndLabel != null) {
      theEndLabel.stopEffect(EffectEventId.onCustom);
    }

    Element myScrollStuff = screen.findElementByName("myScrollStuff");
    if (myScrollStuff != null) {
      nifty.setAlternateKeyForNextLoadXml("fade");
      nifty.gotoScreen("menu");
    }
  }

  public void onEndScreen() {
  }

  public void shizzleHide(final String id) {
    if (escape) return;
    screen.findElementByName(id).hide();
  }

  public void shizzleShow(final String id) {
    if (escape) return;
    if (!id.equals("end")) {
      screen.findElementByName(id).show();
    }
  }

  public boolean keyEvent(final NiftyInputEvent inputEvent) {
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
