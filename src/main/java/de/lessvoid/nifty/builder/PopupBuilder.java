package de.lessvoid.nifty.builder;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.dynamic.PopupCreator;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.loaderv2.types.PopupType;
import de.lessvoid.nifty.screen.Screen;

public class PopupBuilder extends ElementBuilder {
  private PopupCreator creator = new PopupCreator();
  
  public PopupBuilder() {
    initialize(creator);
  }
  
  public PopupBuilder(final String id) {
    this();
    this.id(id);
  }

  public Element build(final Nifty nifty, final Screen screen, final Element parent) {
    throw new RuntimeException("you can't build popups using the PopupBuilder. Please call register() instead to dynamically register popups with Nifty.");
  }

  public Element registerPopup(final Nifty nifty) {
    PopupType popupType = (PopupType) buildElementType();
    System.out.println(popupType.output(0));
    nifty.registerPopup(popupType);
    return nifty.createPopup(popupType.getAttributes().get("id"));
  }

  @Override
  protected Element buildInternal(final Nifty nifty, final Screen screen, final Element parent) {
    return null;
  }
}
