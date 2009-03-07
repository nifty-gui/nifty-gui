package de.lessvoid.nifty.controls.dropdown;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyIdCreator;
import de.lessvoid.nifty.controls.dropdown.controller.DropDownControlItem;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlAttributes;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;

public class CreateDropDownControlItem extends ControlAttributes {
  public CreateDropDownControlItem(final String text) {
    setId(NiftyIdCreator.generate());
    setText(text);
    setName("dropDownControlItem");
  }

  public CreateDropDownControlItem(final String id, final String text) {
    setId(id);
    setText(text);
    setName("dropDownControlItem");
  }

  public DropDownControlItem create(
      final Nifty nifty,
      final Screen screen,
      final Element parent) {
    nifty.addControl(screen, parent, getStandardControl());
    nifty.addControlsWithoutStartScreen();
    return parent.findControl(attributes.get("id"), DropDownControlItem.class);
  }
}
