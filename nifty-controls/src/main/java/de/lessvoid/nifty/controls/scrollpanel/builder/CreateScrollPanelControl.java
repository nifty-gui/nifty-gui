package de.lessvoid.nifty.controls.scrollpanel.builder;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyIdCreator;
import de.lessvoid.nifty.controls.ScrollPanel;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlAttributes;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.loaderv2.types.ControlType;
import de.lessvoid.nifty.loaderv2.types.ElementType;
import de.lessvoid.nifty.screen.Screen;

public class CreateScrollPanelControl extends ControlAttributes {
  public CreateScrollPanelControl() {
    setAutoId(NiftyIdCreator.generate());
  }

  public CreateScrollPanelControl(final String id) {
    setId(id);
  }

  public ScrollPanel create(
      final Nifty nifty,
      final Screen screen,
      final Element parent) {
    nifty.addControl(screen, parent, getStandardControl());
    nifty.addControlsWithoutStartScreen();
    return parent.findNiftyControl(attributes.get("id"), ScrollPanel.class);
  }

  @Override
  public ElementType createType() {
    return new ControlType(attributes);
  }
}
