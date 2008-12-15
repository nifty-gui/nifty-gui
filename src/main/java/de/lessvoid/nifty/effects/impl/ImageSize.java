package de.lessvoid.nifty.effects.impl;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.tools.SizeValue;

/**
 * ImageSize effect.
 * @author void
 */
public class ImageSize implements EffectImpl {

  private float startSize;
  private float endSize;
  private SizeValue imageSize = new SizeValue("100%");

  public final void activate(final Nifty nifty, final Element element, final Properties parameter) {
    // for normal mode
    startSize = Float.parseFloat(parameter.getProperty("startSize", "1.0"));
    endSize = Float.parseFloat(parameter.getProperty("endSize", "2.0"));

    // for hover mode only
    String maxSizeString = parameter.getProperty("maxSize");
    if (maxSizeString != null) {
      imageSize = new SizeValue(maxSizeString);
    }
  }

  public void execute(
      final Element element,
      final float normalizedTime,
      final Falloff falloff,
      final NiftyRenderEngine r) {
    if (falloff == null) {
      float t = normalizedTime;
      r.setImageScale(startSize + t * (endSize - startSize));
    } else {
      float scale = 1.0f + falloff.getFalloffValue() * imageSize.getValue(1.0f);
      r.setImageScale(scale);
    }
  }

  public void deactivate() {
  }
}
