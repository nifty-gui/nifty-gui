package de.lessvoid.nifty.controls.label;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.AbstractController;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.controls.Parameters;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.Color;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @deprecated Please directly use {@link de.lessvoid.nifty.controls.Label} when accessing NiftyControls.
 */
@Deprecated
public class LabelControl extends AbstractController implements Label {

  @Override
  public void bind(
      @Nonnull final Nifty nifty,
      @Nonnull final Screen screen,
      @Nonnull final Element element,
      @Nonnull final Parameters parameter) {
    bind(element);

    final boolean wrap = parameter.getAsBoolean("wrap", false);
    TextRenderer textRenderer = getTextRenderer();
    if (textRenderer != null) {
      textRenderer.setLineWrapping(wrap);
    }
  }

  @Override
  public void onStartScreen() {
  }

  @Override
  public boolean inputEvent(@Nonnull final NiftyInputEvent inputEvent) {
    return false;
  }

  @Override
  public void setText(@Nullable final String text) {
    TextRenderer textRenderer = getTextRenderer();
    if (textRenderer != null) {
      textRenderer.setText(text);
    }
  }

  @Nullable
  @Override
  public String getText() {
    TextRenderer textRenderer = getTextRenderer();
    if (textRenderer == null) {
      return null;
    }
    return textRenderer.getOriginalText();
  }

  @Override
  public void setColor(@Nonnull final Color color) {
    TextRenderer textRenderer = getTextRenderer();
    if (textRenderer != null) {
      textRenderer.setColor(color);
    }
  }

  @Nullable
  @Override
  public Color getColor() {
    TextRenderer textRenderer = getTextRenderer();
    if (textRenderer == null) {
      return null;
    }
    return textRenderer.getColor();
  }

  @Nullable
  private TextRenderer getTextRenderer() {
    Element element = getElement();
    if (element == null) {
      return null;
    }
    return element.getRenderer(TextRenderer.class);
  }
}
