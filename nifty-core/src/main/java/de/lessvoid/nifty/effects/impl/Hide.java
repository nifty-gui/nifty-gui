package de.lessvoid.nifty.effects.impl;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.EffectProperties;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.tools.TargetElementResolver;

public class Hide implements EffectImpl {
  private Element targetElement;

  public void activate(final Nifty nifty, final Element element, final EffectProperties parameter) {
    TargetElementResolver resolver = new TargetElementResolver(nifty.getCurrentScreen(), element);
    targetElement = resolver.resolve(parameter.getProperty("targetElement"));
  }

  public void execute(
      final Element element,
      final float normalizedTime,
      final Falloff falloff,
      final NiftyRenderEngine r) {
    if (targetElement != null) {
      targetElement.hide();
    } else {
      element.hide();
    }
  }

  public void deactivate() {
  }
}


