package de.lessvoid.nifty.examples.progressbar;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.elements.ControllerEventListener;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.xml.xpp3.Attributes;

public class ProgressbarControl implements Controller {
  private Element progressElement;
  private Element progressBarElement;
  private float progressValue;
  private int width;

  public void bind(
      final Nifty nifty,
      final Screen screenParam,
      final Element element,
      final Properties parameter,
      final ControllerEventListener listener,
      final Attributes controlDefinitionAttributes) {
    this.progressElement = element;
    this.progressBarElement = element.findElementByName("progress");
    this.progressValue = 0.0f;
  }

  private void updateDisplay() {
    progressBarElement.setConstraintWidth(new SizeValue(String.valueOf(((int)((float)(width - 32) * progressValue) + 32))));
    progressBarElement.getParent().layoutElements();
  }

  public void onStartScreen() {
    width = progressElement.getWidth();
    updateDisplay();
  }

  public void onFocus(final boolean getFocus) {
  }

  public boolean inputEvent(final NiftyInputEvent inputEvent) {
    return false;
  }

  public void setProgress(float value) {
    progressValue = value;
    if (progressValue > 1.0f) {
      progressValue = 1.0f;
    }
    updateDisplay();
  }
}
