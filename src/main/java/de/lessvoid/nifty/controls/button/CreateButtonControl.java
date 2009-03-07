package de.lessvoid.nifty.controls.button;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyIdCreator;
import de.lessvoid.nifty.controls.button.controller.ButtonControl;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlAttributes;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;

public class CreateButtonControl extends ControlAttributes {
  private static final String NAME = "button";

  public CreateButtonControl() {
    setId(NiftyIdCreator.generate());
    setName(NAME);
  }

  public CreateButtonControl(final String id) {
    setId(id);
    setName(NAME);
  }

  public ButtonControl create(
      final Nifty nifty,
      final Screen screen,
      final Element parent) {
    nifty.addControl(screen, parent, getStandardControl());
    nifty.addControlsWithoutStartScreen();
    return parent.findControl(attributes.get("id"), ButtonControl.class);
  }
}
