package de.lessvoid.nifty.controls.window.builder;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyIdCreator;
import de.lessvoid.nifty.controls.Window;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlAttributes;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.loaderv2.types.ControlType;
import de.lessvoid.nifty.loaderv2.types.ElementType;
import de.lessvoid.nifty.screen.Screen;

public class CreateWindow extends ControlAttributes {
  public CreateWindow() {
    setAutoId(NiftyIdCreator.generate());
    setName("window");
  }

  public CreateWindow(final String id) {
    setId(id);
    setName("window");
  }

  public CreateWindow(final String id, final String title) {
    setId(id);
    setName("window");
    set("title", title);
  }

  public Window create(
      final Nifty nifty,
      final Screen screen,
      final Element parent) {
    nifty.addControl(screen, parent, getStandardControl());
    nifty.addControlsWithoutStartScreen();
    return parent.findNiftyControl(attributes.get("id"), Window.class);
  }

  @Override
  public ElementType createType() {
    return new ControlType(attributes);
  }
}
