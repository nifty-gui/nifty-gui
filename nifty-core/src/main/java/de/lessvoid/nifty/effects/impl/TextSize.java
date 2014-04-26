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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * TextSize effect.
 *
 * @author void
 */
public class TextSize implements EffectImpl {

  private float startSize;
  private float endSize;
  @Nonnull
  private SizeValue textSize = SizeValue.percent(100);

  @Override
  public void activate(
      @Nonnull final Nifty nifty,
      @Nonnull final Element element,
      @Nonnull final EffectProperties parameter) {
    startSize = Float.parseFloat(parameter.getProperty("startSize", "1.0"));
    endSize = Float.parseFloat(parameter.getProperty("endSize", "2.0"));

    // hover mode only
    String maxSizeString = parameter.getProperty("maxSize");
    if (maxSizeString != null) {
      textSize = new SizeValue(maxSizeString);
    }
  }

  @Override
  public void execute(
      @Nonnull final Element element,
      final float normalizedTime,
      @Nullable final Falloff falloff,
      @Nonnull final NiftyRenderEngine r) {
    float scale;
    if (falloff == null) {
      scale = startSize + normalizedTime * (endSize - startSize);
    } else {
      scale = 1.0f + falloff.getFalloffValue() * textSize.getValue(1.0f);
    }
    r.setRenderTextSize(scale);

    TextRenderer textRenderer = element.getRenderer(TextRenderer.class);
    if (textRenderer != null) {
      RenderFont font = textRenderer.getFont();

      if (font != null) {
        String text = textRenderer.getWrappedText();
        float originalWidth = font.getWidth(text, 1.0f);
        float sizedWidth = font.getWidth(text, scale);

        float originalHeight = font.getHeight();
        float sizedHeight = font.getHeight() * scale;

        r.moveToRelative(-(sizedWidth - originalWidth) / 2, -(sizedHeight - originalHeight) / 2);
      }
    }
  }

  @Override
  public void deactivate() {
  }
}
