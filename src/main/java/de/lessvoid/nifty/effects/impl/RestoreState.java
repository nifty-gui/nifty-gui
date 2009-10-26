package de.lessvoid.nifty.effects.impl;


import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.EffectProperties;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;

/**
 * RestoreState.
 * @author void
 */
public class RestoreState implements EffectImpl {

  public void activate(final Nifty nifty, final Element element, final EffectProperties parameter) {
  }

  public void execute(
      final Element element,
      final float normalizedTime,
      final Falloff falloff,
      final NiftyRenderEngine r) {
    r.restoreState();
  }

  public void deactivate() {
  }
}
