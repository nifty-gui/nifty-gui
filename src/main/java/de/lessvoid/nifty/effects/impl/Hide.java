package de.lessvoid.nifty.effects.impl;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.tools.Color;

public class Hide implements EffectImpl {

  public void activate(final Nifty nifty, final Element element, final Properties parameter) {
  }

  public void execute(
      final Element element,
      final float normalizedTime,
      final Falloff falloff,
      final NiftyRenderEngine r) {
    if (normalizedTime >= 1.0f) {
      r.setColorAlpha(Color.WHITE.getAlpha());
    } else {
      r.setColorAlpha(Color.NONE.getAlpha());
    }
  }

  public void deactivate() {
  }
}


