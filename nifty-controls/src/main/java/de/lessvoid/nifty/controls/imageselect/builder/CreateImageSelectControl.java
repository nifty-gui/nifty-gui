package de.lessvoid.nifty.controls.imageselect.builder;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyIdCreator;
import de.lessvoid.nifty.controls.ImageSelect;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlAttributes;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.loaderv2.types.ControlType;
import de.lessvoid.nifty.loaderv2.types.ElementType;
import de.lessvoid.nifty.screen.Screen;

public class CreateImageSelectControl extends ControlAttributes {
  public CreateImageSelectControl() {
    setAutoId(NiftyIdCreator.generate());
    setName("imageSelect");
  }

  public CreateImageSelectControl(final String id) {
    setId(id);
    setName("imageSelect");
  }

  public ImageSelect create(
      final Nifty nifty,
      final Screen screen,
      final Element parent) {
    nifty.addControl(screen, parent, getStandardControl());
    nifty.addControlsWithoutStartScreen();
    return parent.findNiftyControl(attributes.get("id"), ImageSelect.class);
  }

  @Override
  public ElementType createType() {
    return new ControlType(attributes);
  }
}
