package de.lessvoid.nifty.controls.listbox.builder;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyIdCreator;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlAttributes;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.loaderv2.types.ControlType;
import de.lessvoid.nifty.loaderv2.types.ElementType;
import de.lessvoid.nifty.screen.Screen;

public class CreateListBoxControl<T> extends ControlAttributes {
  public CreateListBoxControl() {
    setAutoId(NiftyIdCreator.generate());
    setName("listBox");
  }

  public CreateListBoxControl(final String id) {
    setId(id);
    setName("listBox");
  }

  public ListBox<T> create(
      final Nifty nifty,
      final Screen screen,
      final Element parent) {
    nifty.addControl(screen, parent, getStandardControl());
    nifty.addControlsWithoutStartScreen();
    @SuppressWarnings ("unchecked")
    ListBox<T> result = parent.findNiftyControl(attributes.get("id"), ListBox.class);
    return result;
  }

  @Override
  public ElementType createType() {
    return new ControlType(attributes);
  }
}
