package de.lessvoid.nifty.effects.impl;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyMouse;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.EffectProperties;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ChangeMouseCursor implements EffectImpl {
  @Nullable
  private NiftyMouse niftyMouse;
  @Nullable
  private String oldMouseId;
  @Nullable
  private String newMouseId;

  @Override
  public void activate(
      @Nonnull final Nifty nifty,
      @Nonnull final Element element,
      @Nonnull final EffectProperties parameter) {
    niftyMouse = nifty.getNiftyMouse();
    oldMouseId = niftyMouse.getCurrentId();
    newMouseId = parameter.getProperty("id");
  }

  @Override
  public void execute(
      @Nonnull final Element element,
      final float effectTime,
      @Nullable final Falloff falloff,
      @Nonnull final NiftyRenderEngine r) {
    if (niftyMouse != null) {
      niftyMouse.enableMouseCursor(newMouseId);
    }
  }

  @Override
  public void deactivate() {
    if (niftyMouse != null) {
      niftyMouse.enableMouseCursor(oldMouseId);
    }
  }
}
