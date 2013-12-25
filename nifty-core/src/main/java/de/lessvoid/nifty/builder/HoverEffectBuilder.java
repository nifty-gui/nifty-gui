package de.lessvoid.nifty.builder;

import de.lessvoid.nifty.controls.dynamic.attributes.ControlEffectOnHoverAttributes;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlHoverAttributes;
import de.lessvoid.nifty.effects.Falloff;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class HoverEffectBuilder extends EffectBuilder {
  @Nonnull
  private final ControlHoverAttributes hoverAttributes = new ControlHoverAttributes();

  public HoverEffectBuilder(@Nonnull final String effectName) {
    super(new ControlEffectOnHoverAttributes(), effectName);
    getAttributes().setControlHoverAttributes(hoverAttributes);
  }

  @Override
  @Nonnull
  public HoverEffectBuilder inherit(final boolean inherit) {
    super.inherit(inherit);
    return this;
  }

  @Override
  @Nonnull
  public HoverEffectBuilder inherit() {
    super.inherit();
    return this;
  }

  @Override
  @Nonnull
  public HoverEffectBuilder post(final boolean post) {
    super.post(post);
    return this;
  }

  @Override
  @Nonnull
  public HoverEffectBuilder overlay(final boolean overlay) {
    super.overlay(overlay);
    return this;
  }

  @Override
  @Nonnull
  public HoverEffectBuilder alternateEnable(@Nonnull final String alternateEnable) {
    super.alternateEnable(alternateEnable);
    return this;
  }

  @Override
  @Nonnull
  public HoverEffectBuilder alternateDisable(@Nonnull final String alternateDisable) {
    super.alternateDisable(alternateDisable);
    return this;
  }

  @Override
  @Nonnull
  public HoverEffectBuilder customKey(@Nonnull final String customKey) {
    super.customKey(customKey);
    return this;
  }

  @Override
  @Nonnull
  public HoverEffectBuilder neverStopRendering(final boolean neverStopRendering) {
    super.neverStopRendering(neverStopRendering);
    return this;
  }

  @Override
  @Nonnull
  public HoverEffectBuilder effectParameter(@Nonnull final String key, @Nonnull final String value) {
    super.effectParameter(key, value);
    return this;
  }

  @Nonnull
  public HoverEffectBuilder hoverParameter(@Nonnull final String key, @Nonnull final String value) {
    hoverAttributes.set(key, value);
    return this;
  }

  @Nonnull
  public HoverEffectBuilder hoverFalloffType(@Nonnull final Falloff.HoverFalloffType hoverFalloffType) {
    hoverAttributes.set("hoverFalloffType", hoverFalloffType.toString());
    return this;
  }

  @Nonnull
  public HoverEffectBuilder hoverFalloffConstraint(@Nonnull final Falloff.HoverFalloffConstraint
      hoverFalloffConstraint) {
    hoverAttributes.set("hoverFalloffConstraint", hoverFalloffConstraint.toString());
    return this;
  }

  @Nonnull
  public HoverEffectBuilder hoverWidth(@Nonnull final String hoverWidth) {
    hoverAttributes.set("hoverWidth", hoverWidth);
    return this;
  }

  @Nonnull
  public HoverEffectBuilder hoverHeight(@Nonnull final String hoverHeight) {
    hoverAttributes.set("hoverHeight", hoverHeight);
    return this;
  }

  @Override
  @Nonnull
  public ControlEffectOnHoverAttributes getAttributes() {
    return (ControlEffectOnHoverAttributes) attributes;
  }

  @Override
  @Nonnull
  public HoverEffectBuilder effectValue(@Nullable final String... values) {
    super.effectValue(values);
    return this;
  }
}
