package de.lessvoid.nifty.effects.hover;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.RenderDevice;
import de.lessvoid.nifty.tools.SizeValue;

/**
 * text size pulsate.
 * @author void
 */
public class TextSizePulsate implements HoverEffectImpl {

  /**
   * start size.
   */
  private SizeValue startSize = new SizeValue("0%");

  /**
   * end size.
   */
  private SizeValue endSize = new SizeValue("100%");

  /**
   * initialize.
   * @param nifty Nifty
   * @param element Element
   * @param parameter Parameter
   */
  public void initialize(final Nifty nifty, final Element element, final Properties parameter) {
    String startSizeString = parameter.getProperty("startSize");
    if (startSizeString != null) {
      startSize = new SizeValue(startSizeString);
    }

    String endSizeString = parameter.getProperty("endSize");
    if (endSizeString != null) {
      endSize = new SizeValue(endSizeString);
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
    float value =
      startSize.getValue(1.0f)
      +
      (float) Math.pow(normalizedTime, 0.3) * (endSize.getValue(1.0f) - startSize.getValue(1.0f));
    r.setRenderTextSize(1.0f + value);
  }
}
