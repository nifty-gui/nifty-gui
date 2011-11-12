package de.lessvoid.nifty.controls.dynamic;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyIdCreator;
import de.lessvoid.nifty.controls.StandardControl;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlAttributes;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.loaderv2.types.ElementType;
import de.lessvoid.nifty.loaderv2.types.TextType;
import de.lessvoid.nifty.screen.Screen;

public class TextCreator extends ControlAttributes {
  public TextCreator(final String text) {
    setAutoId(NiftyIdCreator.generate());
    setText(text);
  }

  public TextCreator(final String id, final String text) {
    setId(id);
    setText(text);
  }

  public Element create(
      final Nifty nifty,
      final Screen screen,
      final Element parent) {
    nifty.addControl(screen, parent, new StandardControl() {
      public Element createControl(final Nifty nifty, final Screen screen, final Element parent) throws Exception {
        return createText(nifty, screen, parent);
      }
    });
    nifty.addControlsWithoutStartScreen();
    return parent.findElementByName(attributes.get("id"));
  }

  @Override
  public ElementType createType() {
    return new TextType(attributes);
  }

  public void setWrap(final boolean wrap) {
    attributes.set("wrap", wrap ? "true" : "false");
  }
}
