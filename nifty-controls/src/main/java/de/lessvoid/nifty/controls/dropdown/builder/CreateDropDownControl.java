package de.lessvoid.nifty.controls.dropdown.builder;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyIdCreator;
import de.lessvoid.nifty.controls.DropDown;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlAttributes;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.loaderv2.types.ControlType;
import de.lessvoid.nifty.loaderv2.types.ElementType;
import de.lessvoid.nifty.screen.Screen;

public class CreateDropDownControl extends ControlAttributes {
  public CreateDropDownControl() {
    setAutoId(NiftyIdCreator.generate());
    setName("dropDown");
  }

  public CreateDropDownControl(final String id) {
    setId(id);
    setName("dropDown");
  }

  @SuppressWarnings("rawtypes")
  public DropDown create(
      final Nifty nifty,
      final Screen screen,
      final Element parent) {
    nifty.addControl(screen, parent, getStandardControl());
    nifty.addControlsWithoutStartScreen();
    return parent.findNiftyControl(attributes.get("id"), DropDown.class);
  }

  @Override
  public ElementType createType() {
    return new ControlType(attributes);
  }
}
