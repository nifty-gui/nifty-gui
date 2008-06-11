package de.lessvoid.nifty.effects.hover;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.render.RenderEngine;
import de.lessvoid.nifty.tools.SizeValue;

/**
 * Hint - show hint.
 * @author void
 */
public class Hint implements HoverEffectImpl {

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
  public void initialize(final Nifty niftyParam, final Element element, final Properties parameter) {
    this.nifty = niftyParam;

    String target = parameter.getProperty("targetElement");
    if (target != null) {
      targetElement = nifty.getCurrentScreen().findElementByName(target);
    }

    String text = parameter.getProperty("text");
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
      final float normalizedFalloff,
      final RenderEngine r) {
    if (targetElement != null) {
      TextRenderer textRenderer = targetElement.getRenderer(TextRenderer.class);
      textRenderer.changeText(hintText);
      targetElement.setConstraintWidth(new SizeValue(textRenderer.getTextWidth()+"px"));
      nifty.getCurrentScreen().layoutLayers();
    }
  }

}
