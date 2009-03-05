package de.lessvoid.nifty.controls.dropdown;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.StandardAttributes;
import de.lessvoid.nifty.controls.StandardControl;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;

public class DropDownControl implements StandardControl {
  private StandardAttributes attributes = new StandardAttributes();

  public DropDownControl(final String id, final String text) {
    attributes.setId(id);
    attributes.setText(text);
    attributes.setName("dropDownControl");
  }

  public StandardAttributes getStandardAttributes() {
    return attributes;
  }

  public Element createControl(
      final Nifty nifty,
      final Screen screen,
      final Element parent) throws Exception {
    return attributes.createControl(nifty, screen, parent);
  }
}
