package de.lessvoid.nifty.controls.checkbox;

import java.util.Properties;

import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.AbstractController;
import de.lessvoid.nifty.controls.FocusHandler;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.ControllerEventListener;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;

/**
 * A CheckboxControl.
 * 
 * @author void
 */
public class CheckboxControl extends AbstractController {
  private Element element;
  private Screen screen;
  private boolean checked;
  private FocusHandler focusHandler;

  public void bind(
      final Nifty niftyParam,
      final Screen screenParam,
      final Element elementParam,
      final Properties propertiesParam,
      final ControllerEventListener listenerParam,
      final Attributes controlDefinitionAttributes) {
    element = elementParam;
    screen = screenParam;
    checked = new Boolean(propertiesParam.getProperty("checked", "true"));
    updateVisualState();
  }

  public void onStartScreen() {
    focusHandler = screen.getFocusHandler();
  }

  @Override
  public void onFocus(final boolean getFocus) {
    super.onFocus(getFocus);
  }

  public boolean inputEvent(final NiftyInputEvent inputEvent) {
    if (inputEvent == NiftyInputEvent.NextInputElement) {
      focusHandler.getNext(element).setFocus();
      return true;
    } else if (inputEvent == NiftyInputEvent.PrevInputElement) {
      focusHandler.getPrev(element).setFocus();
      return true;
    } else if (inputEvent == NiftyInputEvent.Activate) {
      onClick();
      return true;
    }
    return false;
  }

  public boolean onClick() {
    checked = !checked;
    updateVisualState();
    return true;
  }

  public boolean isChecked() {
    return checked;
  }

  public void check() {
    this.checked = true;
    updateVisualState();
  }

  public void uncheck() {
    this.checked = false;
    updateVisualState();
  }

  public void setChecked(final boolean state) {
    this.checked = state;
    updateVisualState();
  }

  private void updateVisualState() {
    final Element selectImage = element.findElementByName("select");
    if (checked) {
      selectImage.stopEffect(EffectEventId.onCustom);
      selectImage.startEffect(EffectEventId.onCustom, new EndNotify() { public void perform() { } }, "show");
    } else {
      selectImage.stopEffect(EffectEventId.onCustom);
      selectImage.startEffect(EffectEventId.onCustom, new EndNotify() { public void perform() { } }, "hide");
    }
  }
}
