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

/**
 * TextSize effect.
 * @author void
 */
public class TextSize implements EffectImpl {

  private float startSize;
  private float endSize;
  private SizeValue textSize = new SizeValue("100%");

  public void activate(final Nifty nifty, final Element element, final EffectProperties parameter) {
    startSize = Float.parseFloat(parameter.getProperty("startSize", "1.0"));
    endSize = Float.parseFloat(parameter.getProperty("endSize", "2.0"));

    // hover mode only
    String maxSizeString = parameter.getProperty("maxSize");
    if (maxSizeString != null) {
      textSize = new SizeValue(maxSizeString);
    }
  }

  public void execute(
      final Element element,
      final float normalizedTime,
      final Falloff falloff,
      final NiftyRenderEngine r) {
    float scale;
    if (falloff == null) {
      float t = normalizedTime;
      scale = startSize + t * (endSize - startSize);
    } else {
      scale = 1.0f + falloff.getFalloffValue() * textSize.getValue(1.0f);
    }
    r.setRenderTextSize(scale);

    TextRenderer textRenderer = element.getRenderer(TextRenderer.class);
    if (textRenderer != null) {
      String text = textRenderer.getWrappedText();
      RenderFont font = textRenderer.getFont();

      float originalWidth = font.getWidth(text, 1.0f);
      float sizedWidth = font.getWidth(text, scale);

      float originalHeight = font.getHeight();
      float sizedHeight = font.getHeight() * scale;

      r.moveTo(- (sizedWidth - originalWidth) / 2, - (sizedHeight - originalHeight) / 2);
    }
  }

  public void deactivate() {
  }
}
