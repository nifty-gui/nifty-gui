package de.lessvoid.nifty.controls.checkbox;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyIdCreator;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlAttributes;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;

public class CreateCheckBoxControl extends ControlAttributes {
  private static final String NAME = "checkbox";

  public CreateCheckBoxControl() {
    setId(NiftyIdCreator.generate());
    setName(NAME);
  }

  public CreateCheckBoxControl(final String id) {
    setId(id);
    setName(NAME);
  }

  public CheckboxControl create(
      final Nifty nifty,
      final Screen screen,
      final Element parent) {
    nifty.addControl(screen, parent, getStandardControl());
    nifty.addControlsWithoutStartScreen();
    return parent.findControl(attributes.get("id"), CheckboxControl.class);
  }
}
