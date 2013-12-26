package de.lessvoid.nifty.controls.radiobutton;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.AbstractController;
import de.lessvoid.nifty.controls.Parameters;
import de.lessvoid.nifty.controls.RadioButtonGroup;
import de.lessvoid.nifty.controls.RadioButtonGroupStateChangedEvent;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.events.ElementDisableEvent;
import de.lessvoid.nifty.elements.events.ElementEnableEvent;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import org.bushe.swing.event.EventTopicSubscriber;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * RadioButtonGroupControl implementation.
 *
 * @deprecated Please use {@link de.lessvoid.nifty.controls.RadioButtonGroup} when accessing NiftyControls.
 */
@Deprecated
public class RadioButtonGroupControl extends AbstractController implements RadioButtonGroup {
  @Nonnull
  private static final Logger log = Logger.getLogger(RadioButtonGroupControl.class.getName());
  @Nullable
  private Nifty nifty;
  @Nullable
  private Screen screen;
  @Nullable
  private RadioButtonControl activeButton;
  @Nonnull
  private final List<RadioButtonControl> registeredRadioButtons;
  private boolean allowDeselection = false;  // by default we don't allow deselection

  public RadioButtonGroupControl() {
    registeredRadioButtons = new ArrayList<RadioButtonControl>();
  }

  @Override
  public void bind(
      @Nonnull final Nifty nifty,
      @Nonnull final Screen screen,
      @Nonnull final Element element,
      @Nonnull final Parameters parameter) {
    bind(element);
    this.nifty = nifty;
    this.screen = screen;
    this.allowDeselection = parameter.getAsBoolean("allowDeselection", false);
  }

  @Override
  public void onStartScreen() {
    if (nifty == null || screen == null) {
      log.severe("Can't subscribe to the required events while the control is not bound.");
      return;
    }
    String id = getId();
    if (id == null) {
      log.warning("Radio group has no id, functionality will be severely limited.");
      return;
    }
    nifty.subscribe(screen, id, ElementDisableEvent.class,
        new EventTopicSubscriber<ElementDisableEvent>() {
          @Override
          public void onEvent(final String topic, final ElementDisableEvent disableEvent) {
            enableAllRadioButtons(false);
          }
        });
    nifty.subscribe(screen, id, ElementEnableEvent.class,
        new EventTopicSubscriber<ElementEnableEvent>() {
          @Override
          public void onEvent(final String topic, final ElementEnableEvent disableEvent) {
            enableAllRadioButtons(true);
          }
        });
    nifty.publishEvent(id, new RadioButtonGroupStateChangedEvent(activeButton, null));
  }

  @Override
  public boolean inputEvent(@Nonnull final NiftyInputEvent inputEvent) {
    return false;
  }

  public void registerRadioButton(@Nonnull final RadioButtonControl radioButtonControl) {
    registeredRadioButtons.add(radioButtonControl);
    if (activeButton == null) {
      onRadioButtonClick(registeredRadioButtons.get(0));
    }
  }

  public void onRadioButtonClick(@Nonnull final RadioButtonControl clickedRadioButton) {
    if (activeButton == clickedRadioButton) {
      if (!allowDeselection) {
        return;
      }
      clickedRadioButton.deactivate();

      String id = getId();
      if (id != null && nifty != null) {
        nifty.publishEvent(id, new RadioButtonGroupStateChangedEvent(null, activeButton));
      }
      activeButton = null;
      return;
    }
    if (activeButton != null) {
      activeButton.deactivate();
    }
    clickedRadioButton.activate();
    String id = getId();
    if (id != null && nifty != null) {
      nifty.publishEvent(id, new RadioButtonGroupStateChangedEvent(clickedRadioButton, activeButton));
    }
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
    for (int i = 0; i < registeredRadioButtons.size(); i++) {
      registeredRadioButtons.get(i).setEnabled(enabled);
    }
  }
}
