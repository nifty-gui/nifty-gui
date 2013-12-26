package de.lessvoid.nifty.controls.dynamic.attributes;

import de.lessvoid.nifty.loaderv2.types.EffectType;
import de.lessvoid.nifty.loaderv2.types.EffectValueType;
import de.lessvoid.xml.xpp3.Attributes;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ControlEffectAttributes {
  @Nonnull
  protected Attributes attributes = new Attributes();
  @Nonnull
  protected List<EffectValueType> effectValues = new ArrayList<EffectValueType>();

  public ControlEffectAttributes() {
  }

  public ControlEffectAttributes(
      @Nonnull final Attributes attributes,
      @Nonnull final List<EffectValueType> effectValues) {
    this.attributes = new Attributes(attributes);
    this.effectValues = new ArrayList<EffectValueType>(effectValues);
    Collections.copy(this.effectValues, effectValues);
  }

  public void setAttribute(@Nonnull final String name, @Nonnull final String value) {
    attributes.set(name, value);
  }

  public void setInherit(@Nonnull final String inherit) {
    attributes.set("inherit", inherit);
  }

  public void setPost(@Nonnull final String post) {
    attributes.set("post", post);
  }

  public void setOverlay(@Nonnull final String overlay) {
    attributes.set("overlay", overlay);
  }

  public void setAlternateEnable(@Nonnull final String alternateEnable) {
    attributes.set("alternateEnable", alternateEnable);
  }

  public void setAlternateDisable(@Nonnull final String alternateDisable) {
    attributes.set("alternateDisable", alternateDisable);
  }

  public void setCustomKey(@Nonnull final String customKey) {
    attributes.set("customKey", customKey);
  }

  public void setNeverStopRendering(@Nonnull final String neverStopRendering) {
    attributes.set("neverStopRendering", neverStopRendering);
  }

  public void setName(@Nonnull final String name) {
    attributes.set("name", name);
  }

  public void setStartDelay(@Nonnull final String value) {
    attributes.set("startDelay", value);
  }

  public void setLength(@Nonnull final String value) {
    attributes.set("length", value);
  }

  public void setOneShot(@Nonnull final String value) {
    attributes.set("oneShot", value);
  }

  public void setTimeType(@Nonnull final String value) {
    attributes.set("timeType", value);
  }

  public void setOnStartEffectCallback(@Nonnull final String value) {
    attributes.set("onStartEffect", value);
  }

  public void setOnEndEffectCallback(@Nonnull final String value) {
    attributes.set("onEndEffect", value);
  }

  public void addEffectValues(final EffectValueType value) {
    effectValues.add(value);
  }

  @Nonnull
  public EffectType create() {
    EffectType effectType = new EffectType(attributes);
    for (int i = 0; i < effectValues.size(); i++) {
      effectType.addValue(effectValues.get(i));
    }
    return effectType;
  }
}
