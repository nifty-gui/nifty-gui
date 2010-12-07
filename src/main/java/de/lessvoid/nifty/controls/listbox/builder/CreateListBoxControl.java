package de.lessvoid.nifty.controls.listbox.builder;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyIdCreator;
import de.lessvoid.nifty.controls.dynamic.PanelCreator;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlAttributes;
import de.lessvoid.nifty.controls.listbox.controller.ListBoxControl;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;

public class CreateListBoxControl extends ControlAttributes {
  public CreateListBoxControl() {
    setId(NiftyIdCreator.generate());
    setName("listBox");
  }

  public CreateListBoxControl(final String id) {
    setId(id);
    setName("listBox");
  }

  public ListBoxControl create(
      final Nifty nifty,
      final Screen screen,
      final Element parent) {
    nifty.addControl(screen, parent, getStandardControl());
    nifty.addControlsWithoutStartScreen();

    Element controlElement = parent.findElementByName(attributes.get("id"));
    Element childRoot = controlElement.findElementByName("nifty-listbox-child-root");

    PanelCreator createPanel = new PanelCreator(attributes.get("id") + "Data");
    createPanel.setChildLayout("vertical");
    createPanel.setWidth("100%");
    createPanel.create(nifty, screen, childRoot);

    return parent.findControl(attributes.get("id"), ListBoxControl.class);
  }
}
