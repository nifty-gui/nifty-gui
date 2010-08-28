package de.lessvoid.nifty.builder;

import de.lessvoid.nifty.controls.dynamic.attributes.ControlEffectOnHoverAttributes;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlHoverAttributes;
import de.lessvoid.nifty.effects.Falloff;

public class HoverEffectBuilder {
  private ControlEffectOnHoverAttributes attributes = new ControlEffectOnHoverAttributes();
  private ControlHoverAttributes hoverAttributes = new ControlHoverAttributes();

  public HoverEffectBuilder(final String effectName) {
    attributes.setName(effectName);
    attributes.setControlHoverAttributes(hoverAttributes);
  }

  public void inherit(final boolean inherit) {
    attributes.setInherit(String.valueOf(inherit));
  }

  public void post(final boolean post) {
    attributes.setPost(String.valueOf(post));
  }

  public void overlay(final boolean overlay) {
    attributes.setOverlay(String.valueOf(overlay));
  }

  public void alternateEnable(final String alternateEnable) {
    attributes.setAlternateEnable(alternateEnable);
  }

  public void alternateDisable(final String alternateDisable) {
    attributes.setAlternateDisable(alternateDisable);
  }

  public void customKey(final String customKey) {
    attributes.setCustomKey(customKey);
  }

  public void neverStopRendering(final boolean neverStopRendering) {
    attributes.setNeverStopRendering(String.valueOf(neverStopRendering));
  }

  public void parameter(final String key, final String value) {
    attributes.setAttribute(key, value);
  }

  public void hoverParameter(final String key, final String value) {
    hoverAttributes.set(key, value);
  }

  public void hoverFalloffType(final Falloff.HoverFalloffType hoverFalloffType) {
    hoverAttributes.set("hoverFalloffType", hoverFalloffType.toString());
  }
  
  public void hoverFalloffConstraint(final Falloff.HoverFalloffConstraint hoverFalloffConstraint) {
    hoverAttributes.set("hoverFalloffConstraint", hoverFalloffConstraint.toString());
  }
  
  public void hoverWidth(final String hoverWidth) {
    hoverAttributes.set("hoverWidth", hoverWidth);
  }
  
  public void hoverHeight(final String hoverHeight) {
    hoverAttributes.set("hoverHeight", hoverHeight);
  }

  public ControlEffectOnHoverAttributes getAttributes() {
    return attributes;
  }
}
