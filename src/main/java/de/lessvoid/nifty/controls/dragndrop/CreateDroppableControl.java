package de.lessvoid.nifty.controls.dragndrop;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyIdCreator;
import de.lessvoid.nifty.controls.dragndrop.controller.DroppableControl;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlAttributes;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.loaderv2.types.ControlType;
import de.lessvoid.nifty.loaderv2.types.ElementType;
import de.lessvoid.nifty.screen.Screen; 

public class CreateDroppableControl extends ControlAttributes {
  public CreateDroppableControl() {
    setId(NiftyIdCreator.generate());
    setName("droppable");
  }

  public CreateDroppableControl(final String id) {
    setId(id);
    setName("droppable");
  }

  public DroppableControl create(
      final Nifty nifty,
      final Screen screen,
      final Element parent) {
    nifty.addControl(screen, parent, getStandardControl());
    nifty.addControlsWithoutStartScreen();
    return parent.findControl(attributes.get("id"), DroppableControl.class);
  }

  @Override
  public ElementType createType() {
    return new ControlType(attributes);
  }
}
