package de.lessvoid.nifty.effects.impl;


import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.EffectProperties;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;


public class BlendMode implements EffectImpl {
  de.lessvoid.nifty.render.BlendMode blendMode = null;

  public void activate(final Nifty nifty, final Element element, final EffectProperties parameter) {
    String blendMode = parameter.getProperty("blendMode");
    if (blendMode != null) {
      if (blendMode.toLowerCase().equals("blend")) {
        this.blendMode = de.lessvoid.nifty.render.BlendMode.BLEND;
      } else if (blendMode.toLowerCase().equals("multiply")) {
        this.blendMode = de.lessvoid.nifty.render.BlendMode.MULIPLY;
      }
    }
  }

  public void execute(
      final Element element,
      final float normalizedTime,
      final Falloff falloff,
      final NiftyRenderEngine r) {
    if (blendMode != null) {
      r.setBlendMode(blendMode);
    }
  }

  public void deactivate() {
  }
}


