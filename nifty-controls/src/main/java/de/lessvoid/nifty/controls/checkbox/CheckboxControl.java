package de.lessvoid.nifty.controls.checkbox;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.AbstractController;
import de.lessvoid.nifty.controls.CheckBox;
import de.lessvoid.nifty.controls.CheckBoxStateChangedEvent;
import de.lessvoid.nifty.controls.FocusHandler;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.input.NiftyStandardInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;

/**
 * A CheckboxControl.
 * @author void
 * @deprecated Please use {@link de.lessvoid.nifty.controls.CheckBox} when accessing NiftyControls.
 */
@Deprecated
public class CheckboxControl extends AbstractController implements CheckBox, CheckBoxView {
  private CheckBoxImpl checkBoxImpl = new CheckBoxImpl(this);
  private Nifty nifty;
  private Screen screen;
  private FocusHandler focusHandler;

  @Override
  public void bind(
      final Nifty niftyParam,
      final Screen screenParam,
      final Element elementParam,
      final Properties propertiesParam,
      final Attributes controlDefinitionAttributes) {
    super.bind(elementParam);
    nifty = niftyParam;
    screen = screenParam;
    checkBoxImpl.bindToView(this);
    checkBoxImpl.setChecked(new Boolean(propertiesParam.getProperty("checked", "false")));
  }

  @Override
  public void init(final Properties parameter, final Attributes controlDefinitionAttributes) {
    focusHandler = screen.getFocusHandler();
    super.init(parameter, controlDefinitionAttributes);
  }

  @Override
  public void onStartScreen() {
  }

  @Override
  public void onFocus(final boolean getFocus) {
    super.onFocus(getFocus);
  }

  @Override
  public boolean inputEvent(final NiftyInputEvent inputEvent) {
    if (inputEvent == NiftyStandardInputEvent.NextInputElement) {
      focusHandler.getNext(getElement()).setFocus();
      return true;
    } else if (inputEvent == NiftyStandardInputEvent.PrevInputElement) {
      focusHandler.getPrev(getElement()).setFocus();
      return true;
    } else if (inputEvent == NiftyStandardInputEvent.Activate) {
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
    final Element selectImage = getElement().findElementByName("#select");
    if (checked) {
      selectImage.stopEffect(EffectEventId.onCustom);
      selectImage.startEffect(EffectEventId.onCustom, null, "show");
    } else {
      selectImage.stopEffect(EffectEventId.onCustom);
      selectImage.startEffect(EffectEventId.onCustom, null, "hide");
    }
  }

  @Override
  public void publish(final CheckBoxStateChangedEvent event) {
    if (getElement().getId() != null) {
      nifty.publishEvent(getElement().getId(), event);
    }
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
