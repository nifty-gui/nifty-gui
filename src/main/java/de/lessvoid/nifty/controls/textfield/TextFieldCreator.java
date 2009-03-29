package de.lessvoid.nifty.controls.textfield;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyIdCreator;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlAttributes;
import de.lessvoid.nifty.controls.textfield.controller.TextFieldControl;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;

public class TextFieldCreator extends ControlAttributes {
  private static final String NAME = "textfield";

  public TextFieldCreator() {
    setId(NiftyIdCreator.generate());
    setName(NAME);
  }

  public TextFieldCreator(final String id) {
    setId(id);
    setName(NAME);
  }

  public TextFieldControl create(
      final Nifty nifty,
      final Screen screen,
      final Element parent) {
    nifty.addControl(screen, parent, getStandardControl());
    nifty.addControlsWithoutStartScreen();
    return parent.findControl(attributes.get("id"), TextFieldControl.class);
  }
}
