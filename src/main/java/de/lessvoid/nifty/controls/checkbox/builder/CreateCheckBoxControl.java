package de.lessvoid.nifty.controls.checkbox.builder;


import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyIdCreator;
import de.lessvoid.nifty.controls.CheckBox;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlAttributes;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.loaderv2.types.ControlType;
import de.lessvoid.nifty.loaderv2.types.ElementType;
import de.lessvoid.nifty.screen.Screen;

public class CreateCheckBoxControl extends ControlAttributes {
  private static final String NAME = "checkbox";

  public CreateCheckBoxControl() {
    setAutoId(NiftyIdCreator.generate());
    setName(NAME);
  }

  public CreateCheckBoxControl(final String id) {
    setId(id);
    setName(NAME);
  }

  public CheckBox create(
      final Nifty nifty,
      final Screen screen,
      final Element parent) {
    nifty.addControl(screen, parent, getStandardControl());
    nifty.addControlsWithoutStartScreen();
    return parent.findNiftyControl(attributes.get("id"), CheckBox.class);
  }

  @Override
  public ElementType createType() {
    return new ControlType(attributes);
  }
}
