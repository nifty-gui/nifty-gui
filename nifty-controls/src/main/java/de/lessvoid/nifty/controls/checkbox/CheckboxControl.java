package de.lessvoid.nifty.controls.checkbox;

import org.bushe.swing.event.EventTopicSubscriber;

import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.AbstractController;
import de.lessvoid.nifty.controls.CheckBox;
import de.lessvoid.nifty.controls.CheckBoxStateChangedEvent;
import de.lessvoid.nifty.controls.FocusHandler;
import de.lessvoid.nifty.controls.Parameters;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.events.ElementShowEvent;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.input.NiftyStandardInputEvent;
import de.lessvoid.nifty.screen.Screen;

/**
 * A CheckboxControl.
 * @author void
 * @deprecated Please use {@link de.lessvoid.nifty.controls.CheckBox} when accessing NiftyControls.
 */
@Deprecated
public class CheckboxControl extends AbstractController
  implements CheckBox, CheckBoxView, EventTopicSubscriber<ElementShowEvent> {
  private CheckBoxImpl checkBoxImpl = new CheckBoxImpl(this);
  private Nifty nifty;
  private Screen screen;
  private FocusHandler focusHandler;

  @Override
  public void bind(
      final Nifty niftyParam,
      final Screen screenParam,
      final Element elementParam,
      final Parameters propertiesParam) {
    super.bind(elementParam);
    nifty = niftyParam;
    screen = screenParam;
    checkBoxImpl.bindToView(this);
    checkBoxImpl.setChecked(propertiesParam.getAsBoolean("checked", false));
  }

  @Override
  public void init(final Parameters parameter) {
    focusHandler = screen.getFocusHandler();
    nifty.subscribe(screen, getId(), ElementShowEvent.class, this);
    super.init(parameter);
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
      selectImage.setVisible(true);
      selectImage.stopEffect(EffectEventId.onCustom);
      selectImage.startEffect(EffectEventId.onCustom, null, "show");
    } else {
      selectImage.setVisible(true);
      selectImage.stopEffect(EffectEventId.onCustom);
      selectImage.startEffect(EffectEventId.onCustom, new EndNotify() {
        @Override
        public void perform() {
          selectImage.setVisible(false);
        }
      }, "hide");
    }
  }

  @Override
  public void publish(final CheckBoxStateChangedEvent event) {
    if (getElement().getId() != null) {
      nifty.publishEvent(getElement().getId(), event);
    }
  }

  @Override
  public void onEvent(final String topic, final ElementShowEvent data) {
    final Element selectImage = getElement().findElementByName("#select");
    selectImage.setVisible(checkBoxImpl.isChecked());
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
