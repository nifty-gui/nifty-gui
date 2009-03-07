package de.lessvoid.nifty.controls.dynamic;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyIdCreator;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlAttributes;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;

public class CreateImage extends ControlAttributes {
  public CreateImage() {
    setId(NiftyIdCreator.generate());
  }

  public CreateImage(final String id) {
    setId(id);
  }

  public Element create(
      final Nifty nifty,
      final Screen screen,
      final Element parent) {
    return createImage(nifty, screen, parent);
  }
}
