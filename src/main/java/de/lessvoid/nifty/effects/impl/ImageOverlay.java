package de.lessvoid.nifty.effects.impl;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.render.NiftyImageMode;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.tools.Color;

public class ImageOverlay implements EffectImpl {
  private NiftyImage image;

  public void activate(final Nifty nifty, final Element element, final Properties parameter) {
    image = nifty.getRenderDevice().createImage(parameter.getProperty("filename"), true);
    String imageMode = parameter.getProperty("imageMode", null);
    if (imageMode != null) {
      image.setImageMode(NiftyImageMode.valueOf(imageMode));
    }
  }

  public void execute(
      final Element element,
      final float normalizedTime,
      final Falloff falloff,
      final NiftyRenderEngine r) {
    if (falloff != null) {
      r.setColor(new Color(1.0f, 1.0f, 1.0f, falloff.getFalloffValue()));
    }
    r.renderImage(image, element.getX(), element.getY(), element.getWidth(), element.getHeight());
  }

  public void deactivate() {
  }
}
