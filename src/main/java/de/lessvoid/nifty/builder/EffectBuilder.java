package de.lessvoid.nifty.builder;

import de.lessvoid.nifty.controls.dynamic.attributes.ControlEffectAttributes;

public class EffectBuilder {
  protected ControlEffectAttributes attributes = new ControlEffectAttributes();
  
  public EffectBuilder(final String effectName) {
    attributes.setName(effectName);
  }
  
  public EffectBuilder inherit(final boolean inherit) {
    attributes.setInherit(String.valueOf(inherit));
    return this;
  }

  public EffectBuilder inherit() {
    attributes.setInherit("true");
    return this;
  }

  public EffectBuilder post(final boolean post) {
    attributes.setPost(String.valueOf(post));
    return this;
  }
  
  public EffectBuilder overlay(final boolean overlay) {
    attributes.setOverlay(String.valueOf(overlay));
    return this;
  }
  
  public EffectBuilder alternateEnable(final String alternateEnable) {
    attributes.setAlternateEnable(alternateEnable);
    return this;
  }
  
  public EffectBuilder alternateDisable(final String alternateDisable) {
    attributes.setAlternateDisable(alternateDisable);
    return this;
  }
  
  public EffectBuilder customKey(final String customKey) {
    attributes.setCustomKey(customKey);
    return this;
  }
  
  public EffectBuilder neverStopRendering(final boolean neverStopRendering) {
    attributes.setNeverStopRendering(String.valueOf(neverStopRendering));
    return this;
  }

  public EffectBuilder parameter(final String key, final String value) {
    attributes.setAttribute(key, value);
    return this;
  }

  public EffectBuilder startDelay(final int ms) {
    attributes.setStartDelay(String.valueOf(ms));
    return this;
  }

  public EffectBuilder length(final int ms) {
    attributes.setLength(String.valueOf(ms));
    return this;
  }

  public EffectBuilder oneShot(final boolean oneShot) {
    attributes.setOneShot(String.valueOf(oneShot));
    return this;
  }

  public EffectBuilder timeType(final String timeType) {
    attributes.setTimeType(timeType);
    return this;
  }

  public EffectBuilder onStartEffectCallback(final String callback) {
    attributes.setOnStartEffectCallback(callback);
    return this;
  }

  public EffectBuilder onEndEffectCallback(final String callback) {
    attributes.setOnEndEffectCallback(callback);
    return this;
  }

  public ControlEffectAttributes getAttributes() {
    return attributes;
  }
}
