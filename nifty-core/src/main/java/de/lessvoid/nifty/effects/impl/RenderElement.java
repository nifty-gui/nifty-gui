package de.lessvoid.nifty.effects.impl;


import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.EffectProperties;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.ElementRenderer;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.render.NiftyRenderEngine;

/**
 * RenderElement.
 * @author void
 */
public class RenderElement implements EffectImpl {

  private String type;

  public void activate(final Nifty nifty, final Element element, final EffectProperties parameter) {
    type = parameter.getProperty("type", null);
  }

  public void execute(
      final Element element,
      final float normalizedTime,
      final Falloff falloff,
      final NiftyRenderEngine r) {
    ElementRenderer[] elementRenderer = element.getElementRenderer();
    if (elementRenderer != null) {
      for (ElementRenderer renderer : elementRenderer) {
        if (type == null) {
          renderer.render(element, r);
        } else {
          if ("text".equals(type)) {
            if (renderer instanceof TextRenderer) {
              renderer.render(element, r);
            }
          }
        }
      }
    }
  }

  public void deactivate() {
  }
}
