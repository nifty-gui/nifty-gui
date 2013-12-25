package de.lessvoid.nifty.controls.radiobutton;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.*;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.input.NiftyStandardInputEvent;
import de.lessvoid.nifty.screen.Screen;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.logging.Logger;

/**
 * RadioButtonControl implementation.
 *
 * @deprecated Please use {@link de.lessvoid.nifty.controls.RadioButton} when accessing NiftyControls.
 */
@Deprecated
public class RadioButtonControl extends AbstractController implements RadioButton {
  @Nonnull
  private static final Logger log = Logger.getLogger(RadioButtonControl.class.getName());
  private boolean active;
  @Nullable
  private RadioButtonGroupControl radioGroup;
  @Nullable
  private Nifty nifty;
  @Nullable
  private Screen screen;

  @Override
  public void bind(
      @Nonnull final Nifty nifty,
      @Nonnull final Screen screen,
      @Nonnull final Element element,
      @Nonnull final Parameters parameter) {
    bind(element);
    this.nifty = nifty;
    this.screen = screen;
    linkToRadioGroup(parameter.get("group"));
  }

  @Override
  public void onStartScreen() {
    if (active) {
      publishStateChangedEvent();
    }
  }

  @Override
  public boolean inputEvent(@Nonnull final NiftyInputEvent inputEvent) {
    if (inputEvent == NiftyStandardInputEvent.Activate) {
      onClick();
      return true;
    }

    if (screen == null) {
      return false;
    }
    Element element = getElement();
    if (element != null) {
      if (inputEvent == NiftyStandardInputEvent.NextInputElement) {
        screen.getFocusHandler().getNext(element).setFocus();
        return true;
      }
      if (inputEvent == NiftyStandardInputEvent.PrevInputElement) {
        screen.getFocusHandler().getPrev(element).setFocus();
        return true;
      }
    }
    return false;
  }

  @Override
  public void setGroup(@Nullable final String groupId) {
    linkToRadioGroup(groupId);
  }

  @Nullable
  @Override
  public RadioButtonGroup getGroup() {
    return radioGroup;
  }

  public void deactivate() {
    if (!active) {
      return;
    }
    Element element = getElement();
    if (element != null) {
      element.stopEffect(EffectEventId.onCustom);
      element.startEffect(EffectEventId.onCustom, null, "hide");
    }
    active = false;
    publishStateChangedEvent();
  }

  public void activate() {
    if (active) {
      return;
    }
    Element element = getElement();
    if (element != null) {
      element.stopEffect(EffectEventId.onCustom);
      element.startEffect(EffectEventId.onCustom, null, "show");
      element.setFocus();
    }
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

  private void linkToRadioGroup(@Nullable final String groupId) {
    if (screen == null) {
      log.warning("Linking to radio group failed. Is the binding not done yet?");
    }
    if (groupId == null) {
      radioGroup = null;
      return;
    }
    radioGroup = screen.findNiftyControl(groupId, RadioButtonGroupControl.class);
    if (radioGroup == null) {
      log.warning("No radio group with the id [" + groupId + "] found.");
    } else {
      radioGroup.registerRadioButton(this);
    }
  }

  private void publishStateChangedEvent() {
    if (nifty != null) {
      String id = getId();
      if (id != null) {
        nifty.publishEvent(id, new RadioButtonStateChangedEvent(this, active));
      }
    }
  }
}
