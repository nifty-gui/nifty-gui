package de.lessvoid.nifty.controls.label.builder;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyIdCreator;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlAttributes;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.loaderv2.types.ControlType;
import de.lessvoid.nifty.loaderv2.types.ElementType;
import de.lessvoid.nifty.screen.Screen;

public class CreateLabelControl extends ControlAttributes {
  private static final String NAME = "label";

  public CreateLabelControl(final String text) {
    setAutoId(NiftyIdCreator.generate());
    setName(NAME);
    setText(text);
  }

  public CreateLabelControl(final String id, final String text) {
    setId(id);
    setText(text);
  }

  public Label create(
      final Nifty nifty,
      final Screen screen,
      final Element parent) {
    nifty.addControl(screen, parent, getStandardControl());
    nifty.addControlsWithoutStartScreen();
    return parent.findNiftyControl(attributes.get("id"), Label.class);
  }

  @Override
  public ElementType createType() {
    return new ControlType(attributes);
  }
}

