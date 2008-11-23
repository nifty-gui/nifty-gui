package de.lessvoid.nifty.effects.hover;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.ElementRenderer;
import de.lessvoid.nifty.render.NiftyRenderEngine;

/**
 * RenderElement.
 * @author void
 */
public class RenderElement implements HoverEffectImpl {

  /**
   * initialize.
   * @param nifty Nifty
   * @param element Element
   * @param parameter Parameter
   */
  public void initialize(final Nifty nifty, final Element element, final Properties parameter) {
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
      final NiftyRenderEngine r) {
    ElementRenderer[] elementRenderer = element.getElementRenderer();
    if (elementRenderer != null) {
      for (ElementRenderer renderer : elementRenderer) {
        renderer.render(element, r);
      }
    }
  }

  /**
   * deactivate the effect.
   */
  public void deactivate() {
  }
}
