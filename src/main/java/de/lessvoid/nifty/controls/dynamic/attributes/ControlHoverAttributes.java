package de.lessvoid.nifty.controls.dynamic.attributes;

import de.lessvoid.nifty.loaderv2.types.HoverType;
import de.lessvoid.xml.xpp3.Attributes;

public class ControlHoverAttributes {
  private Attributes attributes = new Attributes();

  public ControlHoverAttributes() {
  }

  public ControlHoverAttributes(final HoverType hoverType) {
    this.attributes = new Attributes(hoverType.getAttributes());
  }

  public void set(final String key, final String value) {
    attributes.set(key, value);
  }

  public void setHoverFalloffType(final String hoverFalloffType) {
    attributes.set("hoverFalloffType", hoverFalloffType);
  }

  public void setHoverFalloffConstraint(final String hoverFalloffConstraint) {
    attributes.set("hoverFalloffConstraint", hoverFalloffConstraint);
  }

  public void setHoverWidth(final String hoverWidth) {
    attributes.set("hoverWidth", hoverWidth);
  }

  public void setHoverHeight(final String hoverHeight) {
    attributes.set("hoverHeight", hoverHeight);
  }

  public HoverType create() {
    HoverType effectType = new HoverType();
    effectType.initFromAttributes(attributes);
    return effectType;
  }
}
