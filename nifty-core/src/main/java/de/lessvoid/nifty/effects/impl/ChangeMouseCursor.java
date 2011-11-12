package de.lessvoid.nifty.effects.impl;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyMouse;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.EffectProperties;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;

public class ChangeMouseCursor implements EffectImpl {
  private NiftyMouse niftyMouse;
  private String oldMouseId;
  private String newMouseId;

  @Override
  public void activate(final Nifty nifty, final Element element, final EffectProperties parameter) {
    niftyMouse = nifty.getNiftyMouse();
    oldMouseId = niftyMouse.getCurrentId();
    newMouseId = parameter.getProperty("id");
  }

  @Override
  public void execute(final Element element, final float effectTime, final Falloff falloff, final NiftyRenderEngine r) {
    niftyMouse.enableMouseCursor(newMouseId);
  }

  @Override
  public void deactivate() {
    niftyMouse.enableMouseCursor(oldMouseId);
  }
}
