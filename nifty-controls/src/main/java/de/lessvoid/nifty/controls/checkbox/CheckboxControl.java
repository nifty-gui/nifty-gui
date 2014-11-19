package de.lessvoid.nifty.controls.checkbox;

import java.util.logging.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bushe.swing.event.EventTopicSubscriber;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.AbstractController;
import de.lessvoid.nifty.controls.CheckBox;
import de.lessvoid.nifty.controls.CheckBoxStateChangedEvent;
import de.lessvoid.nifty.controls.FocusHandler;
import de.lessvoid.nifty.controls.Parameters;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.events.ElementShowEvent;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.input.NiftyStandardInputEvent;
import de.lessvoid.nifty.screen.Screen;

/**
 * The controller for the checkbox element.
 *
 * @author void
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 * @deprecated Not really deprecated, but to report any uses its flagged as such. Please use
 * {@link de.lessvoid.nifty.controls.CheckBox} when accessing NiftyControls.
 */
@Deprecated
public class CheckboxControl extends AbstractController
    implements CheckBox, CheckBoxView, EventTopicSubscriber<ElementShowEvent> {
  @Nonnull
  private static final Logger log = Logger.getLogger(CheckboxControl.class.getName());
  @Nonnull
  private final CheckBoxImpl checkBoxImpl;
  @Nullable
  private Nifty nifty;
  @Nullable
  private Screen screen;
  @Nullable
  private FocusHandler focusHandler;

  public CheckboxControl() {
    checkBoxImpl = new CheckBoxImpl(this);
  }

  @Override
  public void bind(
      @Nonnull final Nifty niftyParam,
      @Nonnull final Screen screenParam,
      @Nonnull final Element elementParam,
      @Nonnull final Parameters propertiesParam) {
    super.bind(elementParam);
    nifty = niftyParam;
    screen = screenParam;
    checkBoxImpl.bindToView(this);
    checkBoxImpl.setInitialCheckedState(propertiesParam.getAsBoolean("checked", false));
  }

  @Override
  public void init(@Nonnull final Parameters parameter) {
    super.init(parameter);
    if (screen == null || nifty == null) {
      log.severe("Required instances not found, binding did not run or failed.");
    }
    String id = getId();
    if (id == null) {
      log.warning("Checkbox has not ID, can't register a event subscriber to monitor update events. Checkbox " +
          "functionality will be limited.");
    } else {
      focusHandler = screen.getFocusHandler();
      nifty.subscribe(screen, id, ElementShowEvent.class, this);
    }
  }

  @Override
  public void onStartScreen() {
  }

  @Override
  public boolean inputEvent(@Nonnull final NiftyInputEvent inputEvent) {
    if (inputEvent == NiftyStandardInputEvent.Activate) {
      onClick();
      return true;
    }
    if (focusHandler == null) {
      return false;
    }

    Element element = getElement();
    if (element == null) {
      return false;
    }
    if (inputEvent == NiftyStandardInputEvent.NextInputElement) {
      focusHandler.getNext(element).setFocus();
      return true;
    }
    if (inputEvent == NiftyStandardInputEvent.PrevInputElement) {
      focusHandler.getPrev(element).setFocus();
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
    final Element element = getElement();
    if (element == null) {
      return;
    }
    final Element selectImage = getElement().findElementById("#select");
    if (selectImage == null) {
      log.warning("Failed to find select image of checkbox. Can't display select state.");
      return;
    }
    if (checked) {
      selectImage.setVisible(true);
    } else {
      selectImage.setVisible(false);
    }
  }

  @Override
  public void publish(@Nonnull final CheckBoxStateChangedEvent event) {
    if (nifty != null) {
      String id = getId();
      if (id != null) {
        nifty.publishEvent(id, event);
      }
    }
  }

  @Override
  public void onEvent(final String topic, final ElementShowEvent data) {
    Element element = getElement();
    if (element != null) {
      // restore visiblity state
      final Element selectImage = element.findElementById("#select");
      if (selectImage != null) {
        selectImage.setVisible(checkBoxImpl.isChecked());
      }
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
