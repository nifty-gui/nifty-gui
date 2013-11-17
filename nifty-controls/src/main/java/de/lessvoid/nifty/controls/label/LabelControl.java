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

/**
 * @deprecated Please directly use {@link de.lessvoid.nifty.controls.Label} when accessing NiftyControls.
 */
@Deprecated
public class LabelControl extends AbstractController implements Label {

  @Override
  public void bind(
      final Nifty nifty,
      final Screen screen,
      final Element element,
      final Parameters parameter) {
    bind(element);

    final boolean wrap = parameter.getAsBoolean("wrap", false);
    getTextRenderer().setLineWrapping(wrap);
  }

  @Override
  public void onStartScreen() {
  }

  @Override
  public boolean inputEvent(final NiftyInputEvent inputEvent) {
    return false;
  }

  @Override
  public void setText(final String text) {
    getTextRenderer().setText(text);
  }

  @Override
  public String getText() {
    return getTextRenderer().getOriginalText();
  }

  @Override
  public void setColor(final Color color) {
    getTextRenderer().setColor(color);
  }

  @Override
  public Color getColor() {
    return getTextRenderer().getColor();
  }

  private TextRenderer getTextRenderer() {
    return getElement().getRenderer(TextRenderer.class);
  }
}
