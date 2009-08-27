package de.lessvoid.nifty.controls.dynamic;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyIdCreator;
import de.lessvoid.nifty.controls.StandardControl;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlAttributes;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;

public class LayerCreator extends ControlAttributes {
  public LayerCreator() {
    setId(NiftyIdCreator.generate());
  }

  public LayerCreator(final String id) {
    setId(id);
  }

  public Element create(
      final Nifty nifty,
      final Screen screen,
      final Element parent) {
    Element layerElement = createLayer(nifty, screen, parent);
    screen.addLayerElement(layerElement);
    screen.processAddAndRemoveLayerElements();
    /*
    nifty.addControl(screen, parent, new StandardControl() {
      public Element createControl(final Nifty nifty, final Screen screen, final Element parent) throws Exception {
        return createLayer(nifty, screen, parent);
      }
    });
    nifty.addControlsWithoutStartScreen();*/
    return layerElement;
  }
}
