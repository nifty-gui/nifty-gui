package de.lessvoid.nifty.controls.dynamic.attributes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.lessvoid.nifty.loaderv2.types.EffectType;
import de.lessvoid.nifty.loaderv2.types.EffectValueType;
import de.lessvoid.xml.xpp3.Attributes;

public class ControlEffectAttributes {
  protected Attributes attributes = new Attributes();
  protected List<EffectValueType> effectValues = new ArrayList<EffectValueType>();

  public ControlEffectAttributes() {
  }

  public ControlEffectAttributes(final Attributes attributes, final ArrayList<EffectValueType> effectValues) {
    this.attributes = new Attributes(attributes);
    this.effectValues = new ArrayList<EffectValueType>(effectValues);
    Collections.copy(this.effectValues, effectValues);
  }

  public void setAttribute(final String name, final String value) {
    attributes.set(name, value);
  }

  public void setInherit(final String inherit) {
    attributes.set("inherit", inherit);
  }

  public void setPost(final String post) {
    attributes.set("post", post);
  }

  public void setOverlay(final String overlay) {
    attributes.set("overlay", overlay);
  }

  public void setAlternateEnable(final String alternateEnable) {
    attributes.set("alternateEnable", alternateEnable);
  }

  public void setAlternateDisable(final String alternateDisable) {
    attributes.set("alternateDisable", alternateDisable);
  }

  public void setCustomKey(final String customKey) {
    attributes.set("customKey", customKey);
  }

  public void setNeverStopRendering(final String neverStopRendering) {
    attributes.set("neverStopRendering", neverStopRendering);
  }

  public void setName(final String name) {
    attributes.set("name", name);
  }

  public void refreshEffectType(final EffectType effectsType) {
  }

  public void setStartDelay(final String value) {
    attributes.set("startDelay", value);
  }

  public void setLength(final String value) {
    attributes.set("length", value);
  }

  public void setOneShot(final String value) {
    attributes.set("oneShot", value);
  }

  public void setTimeType(final String value) {
    attributes.set("timeType", value);
  }

  public void setOnStartEffectCallback(final String value) {
    attributes.set("onStartEffect", value);
  }

  public void setOnEndEffectCallback(final String value) {
    attributes.set("onEndEffect", value);
  }

  public void addEffectValues(final EffectValueType value) {
    effectValues.add(value);
  }

  public EffectType create() {
    EffectType effectType = new EffectType();
    effectType.initFromAttributes(attributes);
    for (int i=0; i<effectValues.size(); i++) {
      effectType.addValue(effectValues.get(i));
    }
    return effectType;
  }
}
