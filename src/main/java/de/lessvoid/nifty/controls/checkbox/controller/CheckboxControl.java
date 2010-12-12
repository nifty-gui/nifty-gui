package de.lessvoid.nifty.controls.checkbox.controller;

import java.util.Properties;

import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.AbstractController;
import de.lessvoid.nifty.controls.CheckBox;
import de.lessvoid.nifty.controls.CheckBoxStateChangedEvent;
import de.lessvoid.nifty.controls.FocusHandler;
import de.lessvoid.nifty.controls.checkbox.CheckBoxImpl;
import de.lessvoid.nifty.controls.checkbox.CheckBoxView;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.ControllerEventListener;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;

/**
 * A CheckboxControl.
 * @author void
 */
public class CheckboxControl extends AbstractController implements CheckBox, CheckBoxView {
  private CheckBoxImpl checkBoxImpl = new CheckBoxImpl();
  private Nifty nifty;
  private Element element;
  private Screen screen;
  private FocusHandler focusHandler;

  public void bind(
      final Nifty niftyParam,
      final Screen screenParam,
      final Element elementParam,
      final Properties propertiesParam,
      final ControllerEventListener listenerParam,
      final Attributes controlDefinitionAttributes) {
    nifty = niftyParam;
    element = elementParam;
    screen = screenParam;
    checkBoxImpl.bindToView(this);
    checkBoxImpl.setChecked(new Boolean(propertiesParam.getProperty("checked", "true")));
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
    checkBoxImpl.toggle();
    return true;
  }

  // CheckBoxView Implementation

  @Override
  public void update(final boolean checked) {
    final Element selectImage = element.findElementByName("select");
    if (checked) {
      selectImage.stopEffect(EffectEventId.onCustom);
      selectImage.startEffect(EffectEventId.onCustom, new EndNotify() { public void perform() { } }, "show");
    } else {
      selectImage.stopEffect(EffectEventId.onCustom);
      selectImage.startEffect(EffectEventId.onCustom, new EndNotify() { public void perform() { } }, "hide");
    }
  }

  @Override
  public void publish(final CheckBoxStateChangedEvent event) {
    nifty.publishEvent(element.getId(), event);
  }

  // CheckBox Implementation

  @Override
  public void check() {
    checkBoxImpl.check();
  }

  @Override
  public void uncheck() {
    checkBoxImpl.uncheck();
  }

  @Override
  public void setChecked(final boolean state) {
    checkBoxImpl.setChecked(state);
  }

  @Override
  public boolean isChecked() {
    return checkBoxImpl.isChecked();
  }

  @Override
  public void toggle() {
    checkBoxImpl.toggle();
  }
}
