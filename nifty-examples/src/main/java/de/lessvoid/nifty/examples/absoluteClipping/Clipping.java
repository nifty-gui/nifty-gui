package de.lessvoid.nifty.examples.absoluteClipping;

import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.ButtonClickedEvent;
import de.lessvoid.nifty.screen.DefaultScreenController;

public class Clipping extends DefaultScreenController {
  private static int width;
  private static int height;
  private int amount = 0;

  @NiftyEventSubscriber(id="decrease")
  public void redPanelVisible(final String id, final ButtonClickedEvent ev) {
    update(false);
  }

  @NiftyEventSubscriber(id="increase")
  public void greenPanelVisible(final String id, final ButtonClickedEvent ev) {
     update(true);
  }

  private void update(boolean up) {
    amount += up ? 10 : -10;
    nifty.setAbsoluteClip(-amount/2, -amount/2, width+amount/2, height+amount/2);
  }
}
