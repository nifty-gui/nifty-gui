package de.lessvoid.nifty.controls.dynamic;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyIdCreator;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlAttributes;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;

public class CustomControlCreator extends ControlAttributes {
  public CustomControlCreator(final String name) {
    setId(NiftyIdCreator.generate());
    setName(name);
  }

  public CustomControlCreator(final String id, final String name) {
    setId(id);
    setName(name);
  }

  public Element create(
      final Nifty nifty,
      final Screen screen,
      final Element parent) {
    nifty.addControl(screen, parent, getStandardControl());
    nifty.addControls();
    return parent.findElementByName(attributes.get("id"));
  }
}
