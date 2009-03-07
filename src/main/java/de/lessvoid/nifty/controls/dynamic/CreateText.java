package de.lessvoid.nifty.controls.dynamic;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyIdCreator;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlAttributes;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;

public class CreateText extends ControlAttributes {
  public CreateText(final String text) {
    setId(NiftyIdCreator.generate());
    setText(text);
  }

  public CreateText(final String id, final String text) {
    setId(id);
    setText(text);
  }

  public Element create(
      final Nifty nifty,
      final Screen screen,
      final Element parent) {
    return createText(nifty, screen, parent);
  }
}
