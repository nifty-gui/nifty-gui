package de.lessvoid.nifty.controls.scrollbar;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyIdCreator;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlAttributes;
import de.lessvoid.nifty.controls.scrollbar.controller.VerticalScrollbarControl;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.loaderv2.types.ControlType;
import de.lessvoid.nifty.loaderv2.types.ElementType;
import de.lessvoid.nifty.screen.Screen;

public class CreateVerticalScrollbarControl extends ControlAttributes {
  public CreateVerticalScrollbarControl() {
    setId(NiftyIdCreator.generate());
    setName("verticalScrollbar");
  }

  public CreateVerticalScrollbarControl(final String id) {
    setId(id);
    setName("verticalScrollbar");
  }

  public VerticalScrollbarControl create(
      final Nifty nifty,
      final Screen screen,
      final Element parent) {
    nifty.addControl(screen, parent, getStandardControl());
    nifty.addControlsWithoutStartScreen();
    return parent.findControl(attributes.get("id"), VerticalScrollbarControl.class);
  }

  @Override
  public ElementType createType() {
    return new ControlType(attributes);
  }
}
