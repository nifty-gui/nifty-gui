package de.lessvoid.nifty.controls.dynamic;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlAttributes;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.loaderv2.types.ControlType;
import de.lessvoid.nifty.loaderv2.types.ElementType;
import de.lessvoid.nifty.screen.Screen;

public class CustomControlCreator extends ControlAttributes {
  public CustomControlCreator(final String name) {
    setName(name);
  }

  public CustomControlCreator(final String id, final String name) {
    setId(id);
    setName(name);
  }

  public void parameter(final String name, final String value) {
    set(name, value);
  }

  public Element create(
      final Nifty nifty,
      final Screen screen,
      final Element parent) {
    nifty.addControl(screen, parent, getStandardControl());
    nifty.addControls();
    return parent.findElementByName(attributes.get("id"));
  }

  @Override
  public ElementType createType() {
    return new ControlType(attributes);
  }
}
