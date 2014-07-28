package de.lessvoid.nifty.controls.dynamic.attributes;

import de.lessvoid.nifty.loaderv2.types.InteractType;
import de.lessvoid.xml.xpp3.Attributes;

import javax.annotation.Nonnull;

public class ControlInteractAttributes {
  @Nonnull
  protected Attributes attributes = new Attributes();

  public ControlInteractAttributes() {
  }

  /**
   * Support for CustomControlCreator
   *
   * @param interact
   */
  public ControlInteractAttributes(@Nonnull final InteractType interact) {
    this.attributes = new Attributes(interact.getAttributes());
  }

  public void setAttribute(@Nonnull final String name, @Nonnull final String value) {
    attributes.set(name, value);
  }

  public void setOnClick(@Nonnull final String onClick) {
    setAttribute("onClick", onClick);
  }
  public void setOnMultiClick(@Nonnull final String onOnMultiClick) {
    setAttribute("onMultiClick", onOnMultiClick);
  }
  public void setOnRelease(@Nonnull final String onRelease) {
    setAttribute("onRelease", onRelease);
  }
  
  public void setOnMouseOver(@Nonnull final String onMouseOver) {
    setAttribute("onMouseOver", onMouseOver);
  }

  public void setOnClickRepeat(@Nonnull final String onClickRepeat) {
    setAttribute("onClickRepeat", onClickRepeat);
  }

  public void setOnClickMouseMove(@Nonnull final String onClickMouseMove) {
    setAttribute("onClickMouseMove", onClickMouseMove);
  }

  public void setOnClickAlternateKey(@Nonnull final String onClickAlternateKey) {
    setAttribute("onClickAlternateKey", onClickAlternateKey);
  }

  @Nonnull
  public InteractType create() {
    return new InteractType(attributes);
  }
}
