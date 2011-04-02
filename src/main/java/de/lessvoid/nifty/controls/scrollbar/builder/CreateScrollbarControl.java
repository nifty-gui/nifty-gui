package de.lessvoid.nifty.controls.scrollbar.builder;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyIdCreator;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlAttributes;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.loaderv2.types.ControlType;
import de.lessvoid.nifty.loaderv2.types.ElementType;
import de.lessvoid.nifty.screen.Screen;

public class CreateScrollbarControl extends ControlAttributes {
  public CreateScrollbarControl(final boolean vertical) {
    setAutoId(NiftyIdCreator.generate());
    initName(vertical);
  }

  public CreateScrollbarControl(final boolean vertical, final String id) {
    setId(id);
    initName(vertical);
  }

  private void initName(final boolean vertical) {
    if (vertical) {
      setName("verticalScrollbar");
    } else {
      setName("horizontalScrollbar");
    }
  }

  @SuppressWarnings("rawtypes")
  public ListBox create(
      final Nifty nifty,
      final Screen screen,
      final Element parent) {
    nifty.addControl(screen, parent, getStandardControl());
    nifty.addControlsWithoutStartScreen();
    return parent.findNiftyControl(attributes.get("id"), ListBox.class);
  }

  @Override
  public ElementType createType() {
    return new ControlType(attributes);
  }
}
