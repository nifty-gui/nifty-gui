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
  
  public void setOnClickRepeat(@Nonnull final String onClickRepeat) {
    setAttribute("onClickRepeat", onClickRepeat);
  }
  
  public void setOnRelease(@Nonnull final String onRelease) {
    setAttribute("onRelease", onRelease);
  }
  
  public void setOnClickMouseMove(@Nonnull final String onClickMouseMove) {
    setAttribute("onClickMouseMove", onClickMouseMove);
  }
  
  public void setOnMultiClick(@Nonnull final String onOnMultiClick) {
    setAttribute("onMultiClick", onOnMultiClick);
  }
  
  public void setOnPrimaryClick(@Nonnull final String onPrimaryClick) {
    setAttribute("onPrimaryClick", onPrimaryClick);
  }
  
  public void setOnPrimaryClickRepeat(@Nonnull final String onPrimaryClickRepeat) {
    setAttribute("onPrimaryClickRepeat", onPrimaryClickRepeat);
  }
  
  public void setOnPrimaryRelease(@Nonnull final String onPrimaryRelease) {
    setAttribute("onPrimaryRelease", onPrimaryRelease);
  }
  
  public void setOnPrimaryClickMouseMove(@Nonnull final String onPrimaryClickMouseMove) {
    setAttribute("onPrimaryClickMouseMove", onPrimaryClickMouseMove);
  }
  
  public void setOnPrimaryMultiClick(@Nonnull final String onPrimaryMultiClick) {
    setAttribute("onPrimaryMultiClick", onPrimaryMultiClick);
  }
  
  public void setOnSecondaryClick(@Nonnull final String onSecondaryClick) {
    setAttribute("onSecondaryClick", onSecondaryClick);
  }
  
  public void setOnSecondaryClickRepeat(@Nonnull final String onSecondaryClickRepeat) {
    setAttribute("onSecondaryClickRepeat", onSecondaryClickRepeat);
  }
  
  public void setOnSecondaryRelease(@Nonnull final String onSecondaryRelease) {
    setAttribute("onSecondaryRelease", onSecondaryRelease);
  }
  
  public void setOnSecondaryClickMouseMove(@Nonnull final String onSecondaryClickMouseMove) {
    setAttribute("onSecondaryClickMouseMove", onSecondaryClickMouseMove);
  }
  
  public void setOnSecondaryMultiClick(@Nonnull final String onSecondaryMultiClick) {
    setAttribute("onSecondaryMultiClick", onSecondaryMultiClick);
  }
  
  public void setOnTertiaryClick(@Nonnull final String onTertiaryClick) {
    setAttribute("onTertiaryClick", onTertiaryClick);
  }
  
  public void setOnTertiaryClickRepeat(@Nonnull final String onTertiaryClickRepeat) {
    setAttribute("onTertiaryClickRepeat", onTertiaryClickRepeat);
  }
  
  public void setOnTertiaryRelease(@Nonnull final String onTertiaryRelease) {
    setAttribute("onTertiaryRelease", onTertiaryRelease);
  }
  
  public void setOnTertiaryClickMouseMove(@Nonnull final String onTertiaryClickMouseMove) {
    setAttribute("onTertiaryClickMouseMove", onTertiaryClickMouseMove);
  }
  
  public void setOnTertiaryMultiClick(@Nonnull final String onTertiaryMultiClick) {
    setAttribute("onTertiaryMultiClick", onTertiaryMultiClick);
  }
  
  public void setOnMouseOver(@Nonnull final String onMouseOver) {
    setAttribute("onMouseOver", onMouseOver);
  }
  
  public void setOnMouseWheel(@Nonnull final String onMouseWheel) {
    setAttribute("onMouseWheel", onMouseWheel);
  }

  public void setOnClickAlternateKey(@Nonnull final String onClickAlternateKey) {
    setAttribute("onClickAlternateKey", onClickAlternateKey);
  }

  @Nonnull
  public InteractType create() {
    return new InteractType(attributes);
  }
}
