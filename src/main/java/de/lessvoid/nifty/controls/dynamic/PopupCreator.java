package de.lessvoid.nifty.controls.dynamic;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyIdCreator;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlAttributes;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;

public class PopupCreator extends ControlAttributes {
  public PopupCreator() {
    setId(NiftyIdCreator.generate());
  }

  public PopupCreator(final String id) {
    setId(id);
  }

  public Element create(final Nifty nifty, final Screen screen) {
    Element popup = createPopup(nifty, screen, screen.getRootElement());
    nifty.addPopupElement(attributes.get("id"), popup);
    return popup;
  }
}
