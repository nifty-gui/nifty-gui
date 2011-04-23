package de.lessvoid.nifty.controls.dragndrop.builder;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyIdCreator;
import de.lessvoid.nifty.controls.Droppable;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlAttributes;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.loaderv2.types.ControlType;
import de.lessvoid.nifty.loaderv2.types.ElementType;
import de.lessvoid.nifty.screen.Screen;

public class CreateDroppable extends ControlAttributes {
  public CreateDroppable() {
    setAutoId(NiftyIdCreator.generate());
    setName("droppable");
  }

  public CreateDroppable(final String id) {
    setId(id);
    setName("droppable");
  }

  public Droppable create(
      final Nifty nifty,
      final Screen screen,
      final Element parent) {
    nifty.addControl(screen, parent, getStandardControl());
    nifty.addControlsWithoutStartScreen();
    return parent.findNiftyControl(attributes.get("id"), Droppable.class);
  }

  @Override
  public ElementType createType() {
    return new ControlType(attributes);
  }
}
