package de.lessvoid.nifty.effects.impl;


import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.EffectProperties;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.tools.TargetElementResolver;

/**
 * focus on hover.
 * @author void
 */
public class Focus implements EffectImpl {
  private Element targetElement;

  public void activate(final Nifty nifty, final Element element, final EffectProperties parameter) {
    TargetElementResolver resolver = new TargetElementResolver(nifty.getCurrentScreen(), element);
    targetElement = resolver.resolve(parameter.getProperty("targetElement"));

    if (targetElement != null) {
      targetElement.setFocus();
    } else {
      element.setFocus();
    }
  }

  public void execute(
      final Element element,
      final float normalizedTime,
      final Falloff falloff,
      final NiftyRenderEngine r) {
  }

  public void deactivate() {
  }
}
