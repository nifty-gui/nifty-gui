package de.lessvoid.nifty.builder;

import de.lessvoid.nifty.controls.dynamic.attributes.ControlEffectAttributes;
import de.lessvoid.nifty.loaderv2.types.EffectValueType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.logging.Logger;

public class EffectBuilder {
  private static final Logger logger = Logger.getLogger(EffectBuilder.class.getName());
  @Nonnull
  protected final ControlEffectAttributes attributes;

  public EffectBuilder(@Nonnull final ControlEffectAttributes attributes, @Nonnull final String effectName) {
    this.attributes = attributes;
    attributes.setName(effectName);
  }

  public EffectBuilder(@Nonnull final String effectName) {
    this(new ControlEffectAttributes(), effectName);
  }

  @Nonnull
  public EffectBuilder inherit(final boolean inherit) {
    attributes.setInherit(String.valueOf(inherit));
    return this;
  }

  @Nonnull
  public EffectBuilder inherit() {
    attributes.setInherit("true");
    return this;
  }

  @Nonnull
  public EffectBuilder post(final boolean post) {
    attributes.setPost(String.valueOf(post));
    return this;
  }

  @Nonnull
  public EffectBuilder overlay(final boolean overlay) {
    attributes.setOverlay(String.valueOf(overlay));
    return this;
  }

  @Nonnull
  public EffectBuilder alternateEnable(@Nonnull final String alternateEnable) {
    attributes.setAlternateEnable(alternateEnable);
    return this;
  }

  @Nonnull
  public EffectBuilder alternateDisable(@Nonnull final String alternateDisable) {
    attributes.setAlternateDisable(alternateDisable);
    return this;
  }

  @Nonnull
  public EffectBuilder customKey(@Nonnull final String customKey) {
    attributes.setCustomKey(customKey);
    return this;
  }

  @Nonnull
  public EffectBuilder neverStopRendering(final boolean neverStopRendering) {
    attributes.setNeverStopRendering(String.valueOf(neverStopRendering));
    return this;
  }

  @Nonnull
  public EffectBuilder effectParameter(@Nonnull final String key, @Nonnull final String value) {
    attributes.setAttribute(key, value);
    return this;
  }

  @Nonnull
  public EffectBuilder startDelay(final int ms) {
    attributes.setStartDelay(String.valueOf(ms));
    return this;
  }

  @Nonnull
  public EffectBuilder length(final int ms) {
    attributes.setLength(String.valueOf(ms));
    return this;
  }

  @Nonnull
  public EffectBuilder oneShot(final boolean oneShot) {
    attributes.setOneShot(String.valueOf(oneShot));
    return this;
  }

  @Nonnull
  public EffectBuilder timeType(@Nonnull final String timeType) {
    attributes.setTimeType(timeType);
    return this;
  }

  @Nonnull
  public EffectBuilder onStartEffectCallback(@Nonnull final String callback) {
    attributes.setOnStartEffectCallback(callback);
    return this;
  }

  @Nonnull
  public EffectBuilder onEndEffectCallback(@Nonnull final String callback) {
    attributes.setOnEndEffectCallback(callback);
    return this;
  }

  @Nonnull
  public ControlEffectAttributes getAttributes() {
    return attributes;
  }

  @Nonnull
  public EffectBuilder effectValue(@Nullable final String... values) {
    if (values == null || values.length % 2 != 0) {
      logger.warning("effect values must be given in pairs, example: effectValue(\"color\", \"#f00f\")");
      return this;
    }
    EffectValueType effectValue = new EffectValueType();
    for (int i = 0; i < values.length / 2; i++) {
      String key = values[(i * 2)];
      String value = values[i * 2 + 1];
      effectValue.getAttributes().set(key, value);
    }
    attributes.addEffectValues(effectValue);
    return this;
  }
}
