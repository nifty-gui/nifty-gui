package de.lessvoid.nifty.effects.impl;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.EffectProperties;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.nifty.tools.TargetElementResolver;

/**
 * Hint - show hint.
 * @author void
 */
public class SimpleHint implements EffectImpl {

  /**
   * nifty.
   */
  private Nifty nifty;

  /**
   * target element.
   */
  private Element targetElement;

  /**
   * hint text.
   */
  private String hintText;

  /**
   * initialize.
   * @param niftyParam Nifty
   * @param element Element
   * @param parameter Parameter
   */
  public void activate(final Nifty niftyParam, final Element element, final EffectProperties parameter) {
    this.nifty = niftyParam;

    TargetElementResolver resolver = new TargetElementResolver(nifty.getCurrentScreen(), element);
    targetElement = resolver.resolve(parameter.getProperty("targetElement"));

    String text = parameter.getProperty("hintText");
    if (text != null) {
      hintText = text;
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
      final Falloff falloff,
      final NiftyRenderEngine r) {
    if (targetElement != null) {
      TextRenderer textRenderer = targetElement.getRenderer(TextRenderer.class);
      textRenderer.setText(hintText);
      targetElement.setConstraintWidth(new SizeValue(textRenderer.getTextWidth() + "px"));
      element.getParent().layoutElements();
    }
  }

  /**
   * deactivate the effect.
   */
  public void deactivate() {
  }
}
