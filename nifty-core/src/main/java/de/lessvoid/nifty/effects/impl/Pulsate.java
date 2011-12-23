package de.lessvoid.nifty.effects.impl;


import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.EffectProperties;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.nifty.tools.pulsate.Pulsator;

/**
 * Color - color overlay.
 * @author void
 */
public class Pulsate implements EffectImpl {
  private Color currentColor = new Color("#000f");
  private Color startColor;
  private Color endColor;
  private SizeValue width;
  private Pulsator pulsator;
  private boolean changeColorOnly = false;
  private boolean activated = false;

  public void activate(final Nifty nifty, final Element element, final EffectProperties parameter) {
    startColor = new Color(parameter.getProperty("startColor", "#00000000"));
    endColor = new Color(parameter.getProperty("endColor", "#ffffffff"));
    width = new SizeValue(parameter.getProperty("width"));
    changeColorOnly = new Boolean(parameter.getProperty("changeColorOnly", "false"));
    pulsator = new Pulsator(parameter, nifty.getTimeProvider());
  }

  public void execute(
      final Element element,
      final float normalizedTime,
      final Falloff falloff,
      final NiftyRenderEngine r) {
    if (!activated && normalizedTime > 0.0f) {
      activated = true;
      pulsator.reset();
    }

    if (activated) {
      if (!changeColorOnly) {
        r.saveState(null);
      }

      float value = pulsator.update();
      currentColor.linear(startColor, endColor, value);
      r.setColor(currentColor);
  
      if (!changeColorOnly) {
        int size = (int) width.getValue(element.getParent().getWidth());
        if (size == -1) {
          r.renderQuad(element.getX(), element.getY(), element.getWidth(), element.getHeight());
        } else {
          r.renderQuad((element.getX() + element.getWidth() / 2) - size / 2, element.getY(), size, element.getHeight());
        }
      }
  
      if (!changeColorOnly) {
        r.restoreState();
      }
    }
  }

  public void deactivate() {
    activated = true;
  }
}
