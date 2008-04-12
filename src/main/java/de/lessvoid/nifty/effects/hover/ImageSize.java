package de.lessvoid.nifty.effects.hover;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.RenderDevice;
import de.lessvoid.nifty.tools.SizeValue;

/**
 * Change ImageSize on hover.
 * @author void
 */
public class ImageSize implements HoverEffectImpl {

  /**
   * imageSize.
   */
  private SizeValue imageSize = new SizeValue("100%");

  /**
   * initialize.
   * @param nifty Nifty
   * @param element Element
   * @param parameter Parameter
   */
  public void initialize(final Nifty nifty, final Element element, final Properties parameter) {
    String maxSizeString = parameter.getProperty("maxSize");
    if (maxSizeString != null) {
      imageSize = new SizeValue(maxSizeString);
    }
  }

  /**
   * execute the effect.
   * @param element the Element
   * @param normalizedTime TimeInterpolator to use
   * @param normalizedFalloff falloff value
   * @param r RenderDevice to use
   */
  public void execute(
      final Element element,
      final float normalizedTime,
      final float normalizedFalloff,
      final RenderDevice r) {
    float scale = 1.0f + normalizedFalloff * imageSize.getValue(1.0f);
    r.setImageScale(scale);
  }
}
