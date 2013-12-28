package de.lessvoid.nifty.effects.impl;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.EffectProperties;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.nifty.tools.TargetElementResolver;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Hint - show hint.
 *
 * @author void
 */
public class SimpleHint implements EffectImpl {

  /**
   * target element.
   */
  @Nullable
  private Element targetElement;

  /**
   * hint text.
   */
  @Nullable
  private String hintText;

  /**
   * initialize.
   *
   * @param nifty     Nifty
   * @param element   Element
   * @param parameter Parameter
   */
  @Override
  public void activate(
      @Nonnull final Nifty nifty,
      @Nonnull final Element element,
      @Nonnull final EffectProperties parameter) {
    Screen screen = nifty.getCurrentScreen();
    if (screen == null) {
      return;
    }
    TargetElementResolver resolver = new TargetElementResolver(nifty.getCurrentScreen(), element);
    targetElement = resolver.resolve(parameter.getProperty("targetElement"));

    String text = parameter.getProperty("hintText");
    if (text != null) {
      hintText = text;
    }
  }

  /**
   * execute the effect.
   *
   * @param element        the Element
   * @param normalizedTime TimeInterpolator to use
   * @param falloff        falloff value
   * @param r              RenderDevice to use
   */
  @Override
  public void execute(
      @Nonnull final Element element,
      final float normalizedTime,
      @Nullable final Falloff falloff,
      @Nonnull final NiftyRenderEngine r) {
    if (targetElement != null) {
      TextRenderer textRenderer = targetElement.getRenderer(TextRenderer.class);
      if (textRenderer != null) {
        textRenderer.setText(hintText == null ? "Missing Hint Text!" : hintText);
        targetElement.setConstraintWidth(SizeValue.px(textRenderer.getTextWidth()));
        element.getParent().layoutElements();
      }
    }
  }

  /**
   * deactivate the effect.
   */
  @Override
  public void deactivate() {
  }
}
