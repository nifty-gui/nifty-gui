package de.lessvoid.nifty.examples.progressbar;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.examples.NiftyExample;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.xml.xpp3.Attributes;

public class ProgressbarControl implements Controller, NiftyExample {
  private Element progressBarElement;
  private Element progressTextElement;

  public void bind(
      final Nifty nifty,
      final Screen screenParam,
      final Element element,
      final Properties parameter,
      final Attributes controlDefinitionAttributes) {
    progressBarElement = element.findElementByName("#progress");
    progressTextElement = element.findElementByName("#progress-text");
  }

  @Override
  public void init(final Properties parameter, final Attributes controlDefinitionAttributes) {
  }

  public void onStartScreen() {
  }

  public void onFocus(final boolean getFocus) {
  }

  public boolean inputEvent(final NiftyInputEvent inputEvent) {
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
    int pixelWidth = (int)(MIN_WIDTH + (progressBarElement.getParent().getWidth() - MIN_WIDTH) * progress);
    progressBarElement.setConstraintWidth(new SizeValue(pixelWidth + "px"));
    progressBarElement.getParent().layoutElements();

    String progressText = String.format("%3.0f%%", progress * 100);
    progressTextElement.getRenderer(TextRenderer.class).setText(progressText);
  }

  @Override
  public String getStartScreen() {
    return "start";
  }

  @Override
  public String getMainXML() {
    return "progressbar/progressbar.xml";
  }

  @Override
  public String getTitle() {
    return "Nifty Progressbar Example";
  }

  @Override
  public void prepareStart(Nifty nifty) {
    // nothing
  }
}
