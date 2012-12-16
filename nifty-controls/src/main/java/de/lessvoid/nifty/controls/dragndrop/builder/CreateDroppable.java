package de.lessvoid.nifty.controls.dragndrop.builder;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyIdCreator;
import de.lessvoid.nifty.controls.Droppable;
import de.lessvoid.nifty.controls.DroppableDropFilter;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlAttributes;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.loaderv2.types.ControlType;
import de.lessvoid.nifty.loaderv2.types.ElementType;
import de.lessvoid.nifty.screen.Screen;

public class CreateDroppable extends ControlAttributes {
  private DroppableDropFilter[] filters;

  public CreateDroppable() {
    setAutoId(NiftyIdCreator.generate());
    setName("droppable");
  }

  public CreateDroppable(final String id) {
    setId(id);
    setName("droppable");
  }

  public void setFilter(final DroppableDropFilter ... filters) {
    this.filters = filters;
  }

  public Droppable create(
      final Nifty nifty,
      final Screen screen,
      final Element parent) {
    nifty.addControl(screen, parent, getStandardControl());
    nifty.addControlsWithoutStartScreen();

    Droppable result = parent.findNiftyControl(attributes.get("id"), Droppable.class);
    if (filters != null) {
      for (int i=0; i<filters.length; i++) {
        result.addFilter(filters[i]);
      }
    }
    return result;
  }

  @Override
  public ElementType createType() {
    return new ControlType(attributes);
  }
}
