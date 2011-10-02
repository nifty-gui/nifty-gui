package de.lessvoid.nifty.examples.helloworld;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.EffectProperties;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.tools.Color;

public class TextTyping implements EffectImpl {
  // length of the effect in ms
  private int effectLength;

  // start size of the text size effect
  private float startSize;

  // end size of the text size effect
  private float endSize;

  // time we have available for each step (char)
  private float stepTime;

  // the original text
  private String originalText;

  // the text renderer (for easy access)
  private TextRenderer textRenderer;

  @Override
  public void activate(Nifty nifty, Element element, EffectProperties parameter) {
    textRenderer = element.getRenderer(TextRenderer.class);
    originalText = textRenderer.getOriginalText();

    effectLength = Integer.valueOf(parameter.getProperty("length", "1000"));
    startSize = Float.valueOf(parameter.getProperty("startSize", "5.0"));
    endSize = Float.valueOf(parameter.getProperty("endSize", "1.0"));
    stepTime = effectLength / originalText.length();

    updateText("");
  }

  @Override
  public void execute(Element element, float effectTime, Falloff falloff, NiftyRenderEngine r) {
    int currentIndex = (int)(originalText.length() * effectTime);
    String currentText = originalText.substring(0, currentIndex);

    if (currentIndex < originalText.length()) {
      String nextChar = originalText.substring(currentIndex, currentIndex + 1);
      int textWidth = textRenderer.getFont().getWidth(currentText);

      // current time in ms (instead of being in the [0.0,1.0] interval what the effectTime is in)
      float currentTime = effectTime * effectLength;

      // normalizedStepTime is between 0.0f and 1.0f
      float normalizedStepTime = (currentTime % stepTime) / stepTime;

      // use normalizedStepTime to calculate the character size for the text size effect 
      float charSize = startSize + normalizedStepTime * (endSize - startSize);

      r.saveState(null);
      r.setFont(textRenderer.getFont());
      r.setRenderTextSize(charSize);
      r.renderText(nextChar, element.getX() + textWidth, element.getY(), -1, -1, Color.WHITE);
      r.restoreState();
    }

    updateText(currentText);
  }

  @Override
  public void deactivate() {
  }

  private void updateText(final String currentText) {
    textRenderer.setText(currentText);
  }
}
