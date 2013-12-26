package de.lessvoid.nifty.examples.defaultcontrols.eventconsume;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.NiftyInputConsumerNotify;
import de.lessvoid.nifty.controls.*;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.input.keyboard.KeyboardInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.Color;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * The Controller for the EventConsumeDialog.
 */
public class EventConsumeDialogController implements Controller {
  private Screen screen;
  @Nullable
  private Label mouseXText;
  @Nullable
  private Label mouseYText;
  @Nullable
  private Label mouseWheelText;
  @Nullable
  private Label mouseButtonText;
  @Nullable
  private Label mouseDownText;
  @Nullable
  private Label mouseProcessedText;
  @Nullable
  private DropDown<ElementInfo> eventConsumeElementDropDown;
  @Nullable
  private CheckBox eventConsumeIgnoreMouseEventsCheckBox;
  @Nullable
  private CheckBox eventConsumeIgnoreKeyboardEventsCheckBox;

  @Override
  public void bind(
      @Nonnull final Nifty nifty,
      @Nonnull final Screen screen,
      @Nonnull final Element element,
      @Nonnull final Parameters parameter) {
    this.screen = screen;
    this.mouseXText = screen.findNiftyControl("mouseXText", Label.class);
    this.mouseYText = screen.findNiftyControl("mouseYText", Label.class);
    this.mouseWheelText = screen.findNiftyControl("mouseWheelText", Label.class);
    this.mouseButtonText = screen.findNiftyControl("mouseButtonText", Label.class);
    this.mouseDownText = screen.findNiftyControl("mouseDownText", Label.class);
    this.mouseProcessedText = screen.findNiftyControl("mouseProcessedText", Label.class);
    this.eventConsumeElementDropDown = screen.findNiftyControl("eventConsumeElementDropDown", DropDown.class);
    this.eventConsumeIgnoreMouseEventsCheckBox = screen.findNiftyControl("eventConsumeIgnoreMouseEventsCheckBox", CheckBox.class);
    this.eventConsumeIgnoreKeyboardEventsCheckBox = screen.findNiftyControl("eventConsumeIgnoreKeyboardEventsCheckBox", CheckBox.class);

    nifty.setNiftyInputConsumerNotify(new NiftyInputConsumerNotify() {
      @Override
      public void processedMouseEvent(
          final int mouseX,
          final int mouseY,
          final int mouseWheel,
          final int button,
          final boolean buttonDown,
          final boolean processed) {
        if (!element.isVisible()) {
          return;
        }
        mouseXText.setText(String.valueOf(mouseX));
        mouseYText.setText(String.valueOf(mouseY));
        mouseWheelText.setText(String.valueOf(mouseWheel));
        mouseButtonText.setText(String.valueOf(button));
        mouseDownText.setText(String.valueOf(buttonDown));
        mouseProcessedText.setText(String.valueOf(processed));

        if (processed) {
          mouseProcessedText.setColor(new Color("#5f5f"));
        } else {
          mouseProcessedText.setColor(new Color("#f55f"));
        }
      }
      
      @Override
      public void processKeyboardEvent(final KeyboardInputEvent keyEvent, final boolean processed) {
      }
    });
  }

  @Override
  public void init(@Nonnull final Parameters parameter) {
    eventConsumeElementDropDown.addItem(new ElementInfo(screen.findElementById("eventConsumeLeftPanel"), "Green Panel"));
    eventConsumeElementDropDown.addItem(new ElementInfo(screen.findElementById("eventConsumeLeftButton"), "Test Left Button"));
    eventConsumeElementDropDown.addItem(new ElementInfo(screen.findElementById("eventConsumeRightPanel"), "Red Panel"));
    eventConsumeElementDropDown.addItem(new ElementInfo(screen.findElementById("eventConsumeRightButton"), "Test Right Button"));
  }

  @Override
  public void onStartScreen() {
  }

  @Override
  public void onFocus(final boolean getFocus) {
  }

  @Override
  public boolean inputEvent(@Nonnull final NiftyInputEvent inputEvent) {
    return false;
  }

  @NiftyEventSubscriber(id="eventConsumeElementDropDown")
  public void eventConsumeElementDropDownChanged(final String id, @Nonnull final DropDownSelectionChangedEvent<ElementInfo> e) {
    eventConsumeIgnoreMouseEventsCheckBox.setChecked(e.getSelection().getElement().isIgnoreMouseEvents());
    eventConsumeIgnoreKeyboardEventsCheckBox.setChecked(e.getSelection().getElement().isIgnoreKeyboardEvents());
  }

  @NiftyEventSubscriber(id="eventConsumeIgnoreMouseEventsCheckBox")
  public void eventConsumeIgnoreMouseEventsCheckBoxToggle(final String id, @Nonnull final CheckBoxStateChangedEvent e) {
    eventConsumeElementDropDown.getSelection().getElement().setIgnoreMouseEvents(e.isChecked());
  }

  @NiftyEventSubscriber(id="eventConsumeIgnoreKeyboardEventsCheckBox")
  public void eventConsumeIgnoreKeyboardEventsCheckBoxToggle(final String id, @Nonnull final CheckBoxStateChangedEvent e) {
    eventConsumeElementDropDown.getSelection().getElement().setIgnoreKeyboardEvents(e.isChecked());
  }

  @NiftyEventSubscriber(pattern="eventConsume.*Button")
  public void onButtonClick(final String id, @Nonnull final ButtonClickedEvent e) {
    screen.findNiftyControl("eventConsumeButtonOut", Label.class).setText(e.getButton().getText() + " pressed");
  }

  private static class ElementInfo {
    private final Element element;
    private final String caption;

    public ElementInfo(final Element element, final String caption) {
      this.element = element;
      this.caption = caption;
    }

    public Element getElement() {
      return element;
    }

    @Override
    public String toString() {
      return caption;
    }
  }
}
