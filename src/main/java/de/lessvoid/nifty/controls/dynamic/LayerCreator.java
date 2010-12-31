package de.lessvoid.nifty.controls.dynamic;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyIdCreator;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlAttributes;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.loaderv2.types.ElementType;
import de.lessvoid.nifty.loaderv2.types.LayerType;
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
    return layerElement;
  }

  @Override
  public ElementType createType() {
    return new LayerType(attributes);
  }
}
