package de.lessvoid.nifty.controls.dynamic;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyIdCreator;
import de.lessvoid.nifty.controls.StandardControl;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlAttributes;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.loaderv2.types.ElementType;
import de.lessvoid.nifty.loaderv2.types.PanelType;
import de.lessvoid.nifty.screen.Screen;

public class PanelCreator extends ControlAttributes {
  public PanelCreator() {
    setAutoId(NiftyIdCreator.generate());
  }

  public PanelCreator(final String id) {
    setId(id);
  }

  public Element create(
      final Nifty nifty,
      final Screen screen,
      final Element parent) {
    nifty.addControl(screen, parent, new StandardControl() {
      public Element createControl(final Nifty nifty, final Screen screen, final Element parent) throws Exception {
        return createPanel(nifty, screen, parent);
      }
    });
    nifty.addControlsWithoutStartScreen();
    return parent.findElementByName(attributes.get("id"));
  }

  @Override
  public ElementType createType() {
    return new PanelType(attributes);
  }
}
