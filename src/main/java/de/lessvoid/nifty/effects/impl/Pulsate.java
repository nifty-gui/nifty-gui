package de.lessvoid.nifty.effects.impl;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.render.RenderStateType;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.nifty.tools.TimeProvider;
import de.lessvoid.nifty.tools.pulsate.Pulsator;

/**
 * Color - color overlay.
 * @author void
 */
public class Pulsate implements EffectImpl {

  private Color startColor;
  private Color endColor;
  private SizeValue width;
  private Pulsator pulsater;

  public void activate(final Nifty nifty, final Element element, final Properties parameter) {
    startColor = new Color(parameter.getProperty("startColor", "#00000000"));
    endColor = new Color(parameter.getProperty("endColor", "#ffffffff"));
    width = new SizeValue(parameter.getProperty("width"));
    this.pulsater = new Pulsator(parameter, new TimeProvider());
  }

  public void execute(
      final Element element,
      final float normalizedTime,
      final Falloff falloff,
      final NiftyRenderEngine r) {
    r.saveState(RenderStateType.allStates());

    float value = pulsater.update();
    Color c = startColor.linear(endColor, value);
    r.setColor(c);
    int size = (int) width.getValue(element.getParent().getWidth());
    if (size == -1) {
      r.renderQuad(element.getX(), element.getY(), element.getWidth(), element.getHeight());
    } else {
      r.renderQuad((element.getX() + element.getWidth() / 2) - size / 2, element.getY(), size, element.getHeight());
    }
    r.restoreState();
  }

  public void deactivate() {
  }
}
