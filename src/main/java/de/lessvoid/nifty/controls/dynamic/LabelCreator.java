package de.lessvoid.nifty.controls.dynamic;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyIdCreator;
import de.lessvoid.nifty.controls.StandardControl;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlAttributes;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.loaderv2.types.ElementType;
import de.lessvoid.nifty.loaderv2.types.LabelType;
import de.lessvoid.nifty.screen.Screen;

public class LabelCreator extends ControlAttributes {
  public LabelCreator(final String text) {
    setId(NiftyIdCreator.generate());
    setText(text);
  }

  public LabelCreator(final String id, final String text) {
    setId(id);
    setText(text);
  }

  public Element create(
      final Nifty nifty,
      final Screen screen,
      final Element parent) {
    nifty.addControl(screen, parent, new StandardControl() {
      public Element createControl(final Nifty nifty, final Screen screen, final Element parent) throws Exception {
        return createLabel(nifty, screen, parent);
      }
    });
    nifty.addControlsWithoutStartScreen();
    return parent.findElementByName(attributes.get("id"));
  }

  @Override
  public ElementType createType() {
    return new LabelType(attributes);
  }
}
