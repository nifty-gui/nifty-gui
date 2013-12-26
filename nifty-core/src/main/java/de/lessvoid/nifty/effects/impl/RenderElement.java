package de.lessvoid.nifty.effects.impl;


import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.EffectProperties;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.ElementRenderer;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.render.NiftyRenderEngine;

import javax.annotation.Nonnull;

/**
 * RenderElement.
 *
 * @author void
 */
public class RenderElement implements EffectImpl {

  private String type;

  @Override
  public void activate(
      @Nonnull final Nifty nifty,
      @Nonnull final Element element,
      @Nonnull final EffectProperties parameter) {
    type = parameter.getProperty("type", null);
  }

  @Override
  public void execute(
      @Nonnull final Element element,
      final float normalizedTime,
      final Falloff falloff,
      @Nonnull final NiftyRenderEngine r) {
    ElementRenderer[] elementRenderer = element.getElementRenderer();
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

  @Override
  public void deactivate() {
  }
}
