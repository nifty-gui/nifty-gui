package de.lessvoid.nifty.controls.scrollbar;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyIdCreator;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlAttributes;
import de.lessvoid.nifty.controls.scrollbar.controller.HorizontalScrollbarControl;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.loaderv2.types.ControlType;
import de.lessvoid.nifty.loaderv2.types.ElementType;
import de.lessvoid.nifty.screen.Screen;

public class CreateHorizontalScrollbarControl extends ControlAttributes {
  public CreateHorizontalScrollbarControl() {
    setId(NiftyIdCreator.generate());
    setName("horizontalScrollbar");
  }

  public CreateHorizontalScrollbarControl(final String id) {
    setId(id);
    setName("horizontalScrollbar");
  }

  public HorizontalScrollbarControl create(
      final Nifty nifty,
      final Screen screen,
      final Element parent) {
    nifty.addControl(screen, parent, getStandardControl());
    nifty.addControlsWithoutStartScreen();
    return parent.findControl(attributes.get("id"), HorizontalScrollbarControl.class);
  }

  @Override
  public ElementType createType() {
    return new ControlType(attributes);
  }
}
