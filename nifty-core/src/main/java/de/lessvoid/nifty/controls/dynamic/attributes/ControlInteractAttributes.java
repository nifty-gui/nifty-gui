package de.lessvoid.nifty.controls.dynamic.attributes;

import de.lessvoid.nifty.loaderv2.types.InteractType;
import de.lessvoid.xml.xpp3.Attributes;

public class ControlInteractAttributes {
  protected Attributes attributes = new Attributes();

  public ControlInteractAttributes() {
  }

  /**
   * Support for CustomControlCreator
   * @param interact
   */
  public ControlInteractAttributes(final InteractType interact) {
    this.attributes = new Attributes(interact.getAttributes());
  }

  public void setAttribute(final String name, final String value) {
    attributes.set(name, value);
  }

  public void setOnClick(final String onClick) {
    attributes.set("onClick", onClick);
  }

  public void setOnRelease(final String onRelease) {
    attributes.set("onRelease", onRelease);
  }

  public void setOnMouseOver(final String onMouseOver) {
    attributes.set("onMouseOver", onMouseOver);
  }

  public void setOnClickRepeat(final String onClickRepeat) {
    attributes.set("onClickRepeat", onClickRepeat);
  }

  public void setOnClickMouseMove(final String onClickMouseMove) {
    attributes.set("onClickMouseMove", onClickMouseMove);
  }

  public void setOnClickAlternateKey(final String onClickAlternateKey) {
    attributes.set("onClickAlternateKey", onClickAlternateKey);
  }

  public InteractType create() {
    InteractType interact = new InteractType();
    interact.initFromAttributes(attributes);
    return interact;
  }
}
