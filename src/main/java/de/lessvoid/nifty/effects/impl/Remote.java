package de.lessvoid.nifty.effects.impl;


import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.EffectProperties;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;

public class Remote implements EffectImpl {

  private Element targetElement;
  private EffectEventId effectEventId;

  public void activate(final Nifty nifty, final Element element, final EffectProperties parameter) {
    String target = parameter.getProperty("targetElement");
    if (target != null) {
      targetElement = element.getParent().findElementByName(target);
      effectEventId = (EffectEventId) parameter.get("effectEventId");
      targetElement.startEffect(effectEventId, null);
    }
  }

  public void execute(
      final Element element,
      final float normalizedTime,
      final Falloff falloff,
      final NiftyRenderEngine r) {
  }

  public void deactivate() {
    if (targetElement != null) {
      targetElement.stopEffect(effectEventId);
    }
  }
}


