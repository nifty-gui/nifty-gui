package de.lessvoid.nifty.controls.dynamic;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyIdCreator;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlAttributes;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlEffectsAttributes;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlInteractAttributes;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.loaderv2.types.ControlType;
import de.lessvoid.nifty.loaderv2.types.ElementType;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;

public class CustomControlCreator extends ControlAttributes {
  public CustomControlCreator(final ControlType source) {
    attributes = new Attributes(source.getAttributes());
    interact = new ControlInteractAttributes(source.getInteract());
    effects = new ControlEffectsAttributes(source.getEffects());
  }

  public CustomControlCreator(final String name) {
    setAutoId(NiftyIdCreator.generate());
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
