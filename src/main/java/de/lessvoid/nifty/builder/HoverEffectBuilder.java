package de.lessvoid.nifty.builder;

import java.util.logging.Logger;

import de.lessvoid.nifty.controls.dynamic.attributes.ControlEffectOnHoverAttributes;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlHoverAttributes;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.loaderv2.types.EffectValueType;

public class HoverEffectBuilder {
  private static Logger logger = Logger.getLogger(HoverEffectBuilder.class.getName());
  private ControlEffectOnHoverAttributes attributes = new ControlEffectOnHoverAttributes();
  private ControlHoverAttributes hoverAttributes = new ControlHoverAttributes();

  public HoverEffectBuilder(final String effectName) {
    attributes.setName(effectName);
    attributes.setControlHoverAttributes(hoverAttributes);
  }

  public HoverEffectBuilder inherit(final boolean inherit) {
    attributes.setInherit(String.valueOf(inherit));
    return this;
  }

  public HoverEffectBuilder post(final boolean post) {
    attributes.setPost(String.valueOf(post));
    return this;
  }

  public HoverEffectBuilder overlay(final boolean overlay) {
    attributes.setOverlay(String.valueOf(overlay));
    return this;
  }

  public HoverEffectBuilder alternateEnable(final String alternateEnable) {
    attributes.setAlternateEnable(alternateEnable);
    return this;
  }

  public HoverEffectBuilder alternateDisable(final String alternateDisable) {
    attributes.setAlternateDisable(alternateDisable);
    return this;
  }

  public HoverEffectBuilder customKey(final String customKey) {
    attributes.setCustomKey(customKey);
    return this;
  }

  public HoverEffectBuilder neverStopRendering(final boolean neverStopRendering) {
    attributes.setNeverStopRendering(String.valueOf(neverStopRendering));
    return this;
  }

  public HoverEffectBuilder parameter(final String key, final String value) {
    attributes.setAttribute(key, value);
    return this;
  }

  public HoverEffectBuilder hoverParameter(final String key, final String value) {
    hoverAttributes.set(key, value);
    return this;
  }

  public HoverEffectBuilder hoverFalloffType(final Falloff.HoverFalloffType hoverFalloffType) {
    hoverAttributes.set("hoverFalloffType", hoverFalloffType.toString());
    return this;
  }
  
  public HoverEffectBuilder hoverFalloffConstraint(final Falloff.HoverFalloffConstraint hoverFalloffConstraint) {
    hoverAttributes.set("hoverFalloffConstraint", hoverFalloffConstraint.toString());
    return this;
  }
  
  public HoverEffectBuilder hoverWidth(final String hoverWidth) {
    hoverAttributes.set("hoverWidth", hoverWidth);
    return this;
  }
  
  public HoverEffectBuilder hoverHeight(final String hoverHeight) {
    hoverAttributes.set("hoverHeight", hoverHeight);
    return this;
  }

  public ControlEffectOnHoverAttributes getAttributes() {
    return attributes;
  }

  public HoverEffectBuilder effectValue(final String ... values) {
    if (values == null || values.length % 2 != 0) {
      logger.warning("effect values must be given in pairs, example: effectValue(\"color\", \"#f00f\")");
      return this;
    }
    EffectValueType effectValue = new EffectValueType();
    for (int i=0; i<values.length/2; i++) {
      String key = values[i*2+0];
      String value = values[i*2+1];
      effectValue.getAttributes().set(key, value);
    }
    attributes.addEffectValues(effectValue);
    return this;
  }
}
