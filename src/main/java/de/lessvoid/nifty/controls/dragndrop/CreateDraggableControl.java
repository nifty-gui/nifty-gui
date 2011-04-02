package de.lessvoid.nifty.controls.dragndrop;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyIdCreator;
import de.lessvoid.nifty.controls.dragndrop.controller.DraggableControl;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlAttributes;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.loaderv2.types.ControlType;
import de.lessvoid.nifty.loaderv2.types.ElementType;
import de.lessvoid.nifty.screen.Screen; 

public class CreateDraggableControl extends ControlAttributes {
  public CreateDraggableControl() {
    setAutoId(NiftyIdCreator.generate());
    setName("draggable");
  }

  public CreateDraggableControl(final String id) {
    setId(id);
    setName("draggable");
  }

  public DraggableControl create(
      final Nifty nifty,
      final Screen screen,
      final Element parent) {
    nifty.addControl(screen, parent, getStandardControl());
    nifty.addControlsWithoutStartScreen();
    // FIXME
    // return parent.findControl(attributes.get("id"), DraggableControl.class);
    return null;
  }

  @Override
  public ElementType createType() {
    return new ControlType(attributes);
  }
}
