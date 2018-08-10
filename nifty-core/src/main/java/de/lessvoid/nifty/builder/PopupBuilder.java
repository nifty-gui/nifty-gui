package de.lessvoid.nifty.builder;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.dynamic.PopupCreator;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.loaderv2.types.PopupType;

import javax.annotation.Nonnull;

public class PopupBuilder extends ElementBuilder {

  public PopupBuilder() {
    super(new PopupCreator());
  }

  public PopupBuilder(@Nonnull final String id) {
    this();
    this.id(id);
  }

  @Override
  @Nonnull
  public Element build(@Nonnull final Element parent) {
    throw new RuntimeException("you can't build popups using the PopupBuilder. Please call register() instead to " +
        "dynamically register popups with Nifty.");
  }

  @Override
  public Element build(@Nonnull final Element parent, final int index) {
    throw new RuntimeException("you can't build popups using the PopupBuilder. Please call register() instead to " +
        "dynamically register popups with Nifty.");
  }

  public void registerPopup(@Nonnull final Nifty nifty) {
    PopupType popupType = (PopupType) buildElementType();
    if (popupType != null) {
      nifty.registerPopup(popupType);
    }
  }
}
