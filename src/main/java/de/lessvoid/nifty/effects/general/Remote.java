package de.lessvoid.nifty.effects.general;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;

public class Remote implements EffectImpl {
  private Element targetElement;

  public void initialize(final Nifty nifty, final Element element, final Properties parameter) {
    String target = parameter.getProperty("targetElement");
    if (target != null) {
      targetElement = element.getParent().findElementByName(target);
      targetElement.startEffect((EffectEventId) parameter.get("effectEventId"), null);
    }
  }

  public void execute(final Element element, final float normalizedTime, final NiftyRenderEngine r) {
  }
}


