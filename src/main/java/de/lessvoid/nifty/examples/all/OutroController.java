package de.lessvoid.nifty.examples.all;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.scrollpanel.ScrollPanel;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.loader.xpp3.elements.LabelType;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.SizeValue;

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
      nifty.addControl(screen, myScrollStuff, "endscroller-page-1", "endscroller-page-1", null, false, null);
      
      myScrollStuff.startEffect(EffectEventId.onCustom);

//      ScrollPanel scrollPanel = screen.findControl("scrollbarPanel", ScrollPanel.class);
//      scrollPanel.initializeScrollPanel(screen);
    }

    // nifty.exit();
  }

  public void scrollEnd() {
    System.out.println("huh");
    Element myScrollStuff = screen.findElementByName("myScrollStuff");
    if (myScrollStuff != null) {
      myScrollStuff.startEffect(EffectEventId.onCustom);
    }
  }

  private void addLabel(final Element myScrollStuff, final String text) {
    LabelType labelType = nifty.createTypeContext().createLabelType(text);
    labelType.createElement(myScrollStuff, screen, null, this);
  }

  public void onEndScreen() {
  }
}
