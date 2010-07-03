package de.lessvoid.nifty.examples.dragndrop;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyIdCreator;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlAttributes;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen; 

public class CreateDraggableControl extends ControlAttributes {
  public CreateDraggableControl() {
    setId(NiftyIdCreator.generate());
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
    return parent.findControl(attributes.get("id"), DraggableControl.class);
  }
}
