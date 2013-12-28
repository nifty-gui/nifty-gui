package de.lessvoid.nifty.effects.impl;


import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.EffectProperties;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.spi.render.RenderFont;
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.nifty.tools.pulsate.Pulsator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * text size pulsate.
 *
 * @author void
 */
public class TextSizePulsate implements EffectImpl {

  /**
   * start size.
   */
  @Nonnull
  private SizeValue startSize = new SizeValue("0%");

  /**
   * end size.
   */
  @Nonnull
  private SizeValue endSize = new SizeValue("100%");

  /**
   * Pulsator to use.
   */
  private Pulsator pulsator;

  /**
   * flag to remember if this effect is activated or not. this is used
   * to reset the pulsator.
   */
  private boolean activated = false;

  @Override
  public void activate(
      @Nonnull final Nifty nifty,
      @Nonnull final Element element,
      @Nonnull final EffectProperties parameter) {
    String startSizeString = parameter.getProperty("startSize");
    if (startSizeString != null) {
      startSize = new SizeValue(startSizeString);
    }

    String endSizeString = parameter.getProperty("endSize");
    if (endSizeString != null) {
      endSize = new SizeValue(endSizeString);
    }
    pulsator = new Pulsator(parameter, nifty.getTimeProvider());
  }

  @Override
  public void execute(
      @Nonnull final Element element,
      final float normalizedTime,
      @Nullable final Falloff falloff,
      @Nonnull final NiftyRenderEngine r) {
    if (!activated && normalizedTime > 0.0f) {
      activated = true;
      pulsator.reset();
    }

    if (activated) {
      float value = pulsator.update();
      float size = startSize.getValue(1.0f) + value * (endSize.getValue(1.0f) - startSize.getValue(1.0f));

      TextRenderer textRenderer = element.getRenderer(TextRenderer.class);
      if (textRenderer != null) {
        String text = textRenderer.getWrappedText();
        RenderFont font = textRenderer.getFont();

        if (font != null) {
          float originalWidth = font.getWidth(text, 1.0f);
          float sizedWidth = font.getWidth(text, size);

          float originalHeight = font.getHeight();
          float sizedHeight = font.getHeight() * size;

          r.moveTo(-(sizedWidth - originalWidth) / 2, -(sizedHeight - originalHeight) / 2);
        }
      }

      r.setRenderTextSize(size);
    }
  }

  @Override
  public void deactivate() {
    activated = true;
  }
}
