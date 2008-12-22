package de.lessvoid.nifty.effects.impl;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;

/**
 * focus on hover.
 * @author void
 */
public class Focus implements EffectImpl {
  private Element targetElement;

  public void activate(final Nifty nifty, final Element element, final Properties parameter) {
    String target = parameter.getProperty("targetElement");
    if (target != null) {
      targetElement = element.getParent().findElementByName(target);
    }
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
