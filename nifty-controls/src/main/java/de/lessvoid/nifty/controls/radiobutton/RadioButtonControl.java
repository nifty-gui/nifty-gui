package de.lessvoid.nifty.controls.radiobutton;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.AbstractController;
import de.lessvoid.nifty.controls.RadioButton;
import de.lessvoid.nifty.controls.RadioButtonGroup;
import de.lessvoid.nifty.controls.RadioButtonStateChangedEvent;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.input.NiftyStandardInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;

/**
 * RadioButtonControl implementation.
 * @deprecated Please use {@link de.lessvoid.nifty.controls.RadioButton} when accessing NiftyControls.
 */
@Deprecated
public class RadioButtonControl extends AbstractController implements RadioButton {
  private boolean active;
  private RadioButtonGroupControl radioGroup;
  private Nifty nifty;
  private Screen screen;

  @Override
  public void bind(final Nifty nifty, final Screen screen, final Element element, final Properties parameter, final Attributes controlDefinitionAttributes) {
    super.bind(element);
    this.nifty = nifty;
    this.screen = screen;
    linkToRadioGroup(parameter.getProperty("group"));
  }

  @Override
  public void onStartScreen() {
    if (active) {
      publishStateChangedEvent();
    }
  }

  @Override
  public boolean inputEvent(final NiftyInputEvent inputEvent) {
    if (inputEvent == NiftyStandardInputEvent.NextInputElement) {
      screen.getFocusHandler().getNext(getElement()).setFocus();
      return true;
    } else if (inputEvent == NiftyStandardInputEvent.PrevInputElement) {
      screen.getFocusHandler().getPrev(getElement()).setFocus();
      return true;
    } else if (inputEvent == NiftyStandardInputEvent.Activate) {
      onClick();
      return true;
    }
    return false;
  }

  @Override
  public void setGroup(final String groupId) {
    linkToRadioGroup(groupId);
  }

  @Override
  public RadioButtonGroup getGroup() {
    return radioGroup;
  }

  public void deactivate() {
    if (!active) {
      return;
    }
    getElement().stopEffect(EffectEventId.onCustom);
    getElement().startEffect(EffectEventId.onCustom, null, "hide");
    active = false;
    publishStateChangedEvent();
  }

  public void activate() {
    if (active) {
      return;
    }
    getElement().stopEffect(EffectEventId.onCustom);
    getElement().startEffect(EffectEventId.onCustom, null, "show");
    getElement().setFocus();
    active = true;
    publishStateChangedEvent();
  }

  @Override
  public boolean isActivated() {
    return active;
  }

  @Override
  public void select() {
    onClick();
  }

  public void onClick() {
    if (radioGroup == null) {
      return;
    }
    radioGroup.onRadioButtonClick(this);
  }

  private void linkToRadioGroup(final String groupId) {
    if (groupId == null) {
      radioGroup = null;
      return;
    }
    radioGroup = screen.findNiftyControl(groupId, RadioButtonGroupControl.class);
    radioGroup.registerRadioButton(this);
  }

  private void publishStateChangedEvent() {
    nifty.publishEvent(getElement().getId(), new RadioButtonStateChangedEvent(this, active));
  }
}
