package de.lessvoid.nifty.effects.impl;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.render.NiftyImageMode;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.render.RenderStateType;
import de.lessvoid.nifty.tools.TimeProvider;
import de.lessvoid.nifty.tools.pulsate.Pulsator;

/**
 * ImagePulsate - image color pulsate.
 * @author void
 */
public class ImageOverlayPulsate implements EffectImpl {

  private NiftyImage image;
  private Pulsator pulsater;

  public void activate(final Nifty nifty, final Element element, final Properties parameter) {
    image = nifty.getRenderEngine().createImage(parameter.getProperty("filename"), true);
    String subImageSizeMode = parameter.getProperty("imageMode", null);
    if (subImageSizeMode != null) {
      image.setImageMode(NiftyImageMode.valueOf(subImageSizeMode));
    }
    this.pulsater = new Pulsator(parameter, new TimeProvider());
  }

  public void execute(
      final Element element,
      final float normalizedTime,
      final Falloff falloff,
      final NiftyRenderEngine r) {
    r.saveState(RenderStateType.allStates());
    float value = pulsater.update();
    r.setColorAlpha(value);
    r.renderImage(image, element.getX(), element.getY(), element.getWidth(), element.getHeight());
    r.restoreState();
  }

  public void deactivate() {
  }
}
