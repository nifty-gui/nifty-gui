package de.lessvoid.nifty.effects.impl;

import java.util.Properties;
import java.util.logging.Logger;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;

public class Hide implements EffectImpl {
  private Element targetElement;

  public void activate(final Nifty nifty, final Element element, final Properties parameter) {
    String targetElementName = parameter.getProperty("targetElement");
    if (targetElementName != null) {
      targetElement = nifty.getCurrentScreen().findElementByName(targetElementName);
    }
  }

  public void execute(
      final Element element,
      final float normalizedTime,
      final Falloff falloff,
      final NiftyRenderEngine r) {
    Logger.getAnonymousLogger().info("hide [" + targetElement + "]");
    if (targetElement != null) {
      targetElement.hide();
    } else {
      element.hide();
    }
  }

  public void deactivate() {
  }
}


