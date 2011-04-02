package de.lessvoid.nifty.controls.listbox.builder;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyIdCreator;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlAttributes;
import de.lessvoid.nifty.controls.listbox.ListBoxControl;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.loaderv2.types.ControlType;
import de.lessvoid.nifty.loaderv2.types.ElementType;
import de.lessvoid.nifty.screen.Screen;

public class CreateListBoxControl extends ControlAttributes {
  public CreateListBoxControl() {
    setAutoId(NiftyIdCreator.generate());
    setName("listBox");
  }

  public CreateListBoxControl(final String id) {
    setId(id);
    setName("listBox");
  }

  @SuppressWarnings("rawtypes")
  public ListBox create(
      final Nifty nifty,
      final Screen screen,
      final Element parent) {
    nifty.addControl(screen, parent, getStandardControl());
    nifty.addControlsWithoutStartScreen();
    return parent.findNiftyControl(attributes.get("id"), ListBoxControl.class);
  }

  @Override
  public ElementType createType() {
    return new ControlType(attributes);
  }
}
