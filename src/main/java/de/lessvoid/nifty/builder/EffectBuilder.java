package de.lessvoid.nifty.builder;

import de.lessvoid.nifty.controls.dynamic.attributes.ControlEffectAttributes;

public class EffectBuilder {
  protected ControlEffectAttributes attributes = new ControlEffectAttributes();
  
  public EffectBuilder(final String effectName) {
    attributes.setName(effectName);
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

  public void startDelay(final int ms) {
    attributes.setStartDelay(String.valueOf(ms));
  }

  public void length(final int ms) {
    attributes.setLength(String.valueOf(ms));
  }

  public void oneShot(final boolean oneShot) {
    attributes.setOneShot(String.valueOf(oneShot));
  }

  public void timeType(final String timeType) {
    attributes.setTimeType(timeType);
  }

  public void onStartEffectCallback(final String callback) {
    attributes.setOnStartEffectCallback(callback);
  }

  public void onEndEffectCallback(final String callback) {
    attributes.setOnEndEffectCallback(callback);
  }

  public ControlEffectAttributes getAttributes() {
    return attributes;
  }
}
