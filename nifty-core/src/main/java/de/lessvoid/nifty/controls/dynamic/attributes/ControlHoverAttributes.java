package de.lessvoid.nifty.controls.dynamic.attributes;

import de.lessvoid.nifty.loaderv2.types.HoverType;
import de.lessvoid.xml.xpp3.Attributes;

import javax.annotation.Nonnull;

public class ControlHoverAttributes {
  @Nonnull
  private Attributes attributes = new Attributes();

  public ControlHoverAttributes() {
  }

  public ControlHoverAttributes(@Nonnull final HoverType hoverType) {
    this.attributes = new Attributes(hoverType.getAttributes());
  }

  public void set(@Nonnull final String key, @Nonnull final String value) {
    attributes.set(key, value);
  }

  public void setHoverFalloffType(@Nonnull final String hoverFalloffType) {
    attributes.set("hoverFalloffType", hoverFalloffType);
  }

  public void setHoverFalloffConstraint(@Nonnull final String hoverFalloffConstraint) {
    attributes.set("hoverFalloffConstraint", hoverFalloffConstraint);
  }

  public void setHoverWidth(@Nonnull final String hoverWidth) {
    attributes.set("hoverWidth", hoverWidth);
  }

  public void setHoverHeight(@Nonnull final String hoverHeight) {
    attributes.set("hoverHeight", hoverHeight);
  }

  @Nonnull
  public HoverType create() {
    return new HoverType(attributes);
  }
}
