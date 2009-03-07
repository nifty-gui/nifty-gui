package de.lessvoid.nifty.examples.all;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.dynamic.CreateCustomControl;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 * Outro implementation for the nifty demo Outro screen.
 * @author void
 */
public class OutroController implements ScreenController {
  private Nifty nifty;
  private Screen screen;

  public void bind(final Nifty newNifty, final Screen newScreen) {
    this.nifty = newNifty;
    this.screen = newScreen;
  }

  public final void onStartScreen() {
    Element myScrollStuff = screen.findElementByName("myScrollStuff");
    if (myScrollStuff != null) {
      CreateCustomControl endScroller = new CreateCustomControl("endscroller-page-1");
      endScroller.create(nifty, screen, myScrollStuff);
      myScrollStuff.startEffect(EffectEventId.onCustom);
    }
  }

  public void scrollEnd() {
    Element myScrollStuff = screen.findElementByName("myScrollStuff");
    if (myScrollStuff != null) {
      myScrollStuff.startEffect(EffectEventId.onCustom);
    }
  }

  public void onEndScreen() {
  }
}
