package de.lessvoid.nifty.examples.progressbar;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.controls.Parameters;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.examples.NiftyExample;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.SizeValue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ProgressbarControl implements Controller, NiftyExample {
  @Nullable
  private Element progressBarElement;
  @Nullable
  private Element progressTextElement;

  @Override
  public void bind(
      @Nonnull final Nifty nifty,
      @Nonnull final Screen screenParam,
      @Nonnull final Element element,
      @Nonnull final Parameters parameter) {
    progressBarElement = element.findElementById("#progress");
    progressTextElement = element.findElementById("#progress-text");
  }

  @Override
  public void init(@Nonnull final Parameters parameter) {
  }

  @Override
  public void onStartScreen() {
  }

  @Override
  public void onFocus(final boolean getFocus) {
  }

  @Override
  public boolean inputEvent(@Nonnull final NiftyInputEvent inputEvent) {
    return false;
  }

  public void setProgress(final float progressValue) {
    float progress = progressValue;
    if (progress < 0.0f) {
      progress = 0.0f;
    } else if (progress > 1.0f) {
      progress = 1.0f;
    }
    final int MIN_WIDTH = 32;
    int pixelWidth = (int) (MIN_WIDTH + (progressBarElement.getParent().getWidth() - MIN_WIDTH) * progress);
    progressBarElement.setConstraintWidth(new SizeValue(pixelWidth + "px"));
    progressBarElement.getParent().layoutElements();

    String progressText = String.format("%3.0f%%", progress * 100);
    progressTextElement.getRenderer(TextRenderer.class).setText(progressText);
  }

  @Nonnull
  @Override
  public String getStartScreen() {
    return "start";
  }

  @Nonnull
  @Override
  public String getMainXML() {
    return "progressbar/progressbar.xml";
  }

  @Nonnull
  @Override
  public String getTitle() {
    return "Nifty Progressbar Example";
  }

  @Override
  public void prepareStart(Nifty nifty) {
    // nothing
  }
}
