package de.lessvoid.nifty.controls.radiobutton;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.bushe.swing.event.EventTopicSubscriber;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.AbstractController;
import de.lessvoid.nifty.controls.RadioButton;
import de.lessvoid.nifty.controls.RadioButtonGroup;
import de.lessvoid.nifty.controls.RadioButtonGroupStateChangedEvent;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.events.ElementDisableEvent;
import de.lessvoid.nifty.elements.events.ElementEnableEvent;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;

/**
 * RadioButtonGroupControl implementation.
 * @deprecated Please use {@link de.lessvoid.nifty.controls.RadioButtonGroup} when accessing NiftyControls.
 */
@Deprecated
public class RadioButtonGroupControl extends AbstractController implements RadioButtonGroup {
  private Nifty nifty;
  private Screen screen;
  private RadioButton activeButton;
  private List<RadioButton> registeredRadioButtons = new ArrayList<RadioButton>();
  private boolean allowDeselection = false;  // by default we don't allow deselection

  @Override
  public void bind(final Nifty nifty, final Screen screen, final Element element, final Properties parameter, final Attributes controlDefinitionAttributes) {
    super.bind(element);
    this.nifty = nifty;
    this.screen = screen;
  }

  @Override
  public void onStartScreen() {
    nifty.subscribe(screen, getElement().getId(), ElementDisableEvent.class, new EventTopicSubscriber<ElementDisableEvent>() {
      @Override
      public void onEvent(final String topic, final ElementDisableEvent disableEvent) {
        enableAllRadioButtons(false);
      }
    });
    nifty.subscribe(screen, getElement().getId(), ElementEnableEvent.class, new EventTopicSubscriber<ElementEnableEvent>() {
      @Override
      public void onEvent(final String topic, final ElementEnableEvent disableEvent) {
        enableAllRadioButtons(true);
      }
    });
    nifty.publishEvent(getElement().getId(), new RadioButtonGroupStateChangedEvent(activeButton, null));
  }

  @Override
  public boolean inputEvent(final NiftyInputEvent inputEvent) {
    return false;
  }

  public void registerRadioButton(final RadioButton radioButtonControl) {
    registeredRadioButtons.add(radioButtonControl);
    if (activeButton == null) {
      onRadioButtonClick(registeredRadioButtons.get(0));
    }
  }

  public void onRadioButtonClick(final RadioButton clickedRadioButton) {
    if (activeButton == clickedRadioButton) {
      if (!allowDeselection) {
        return;
      }
      clickedRadioButton.deactivate();

      nifty.publishEvent(getElement().getId(), new RadioButtonGroupStateChangedEvent(null, activeButton));
      activeButton = null;
      return;
    }
    if (activeButton != null) {
      activeButton.deactivate();
    }
    clickedRadioButton.activate();
    nifty.publishEvent(getElement().getId(), new RadioButtonGroupStateChangedEvent(clickedRadioButton, activeButton));
    activeButton = clickedRadioButton;
  }

  @Override
  public void allowDeselection(final boolean allowDeselection) {
    this.allowDeselection = allowDeselection;
    if (!allowDeselection) {
      if (!registeredRadioButtons.isEmpty()) {
        onRadioButtonClick(registeredRadioButtons.get(0));
      }
    }
  }

  private void enableAllRadioButtons(final boolean enabled) {
    for (int i=0; i<registeredRadioButtons.size(); i++) {
      registeredRadioButtons.get(i).setEnabled(enabled);
    }
  }
}
