package de.lessvoid.nifty.examples.defaultcontrols.eventconsume;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.NiftyInputConsumerNotify;
import de.lessvoid.nifty.controls.ButtonClickedEvent;
import de.lessvoid.nifty.controls.CheckBox;
import de.lessvoid.nifty.controls.CheckBoxStateChangedEvent;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.controls.DropDown;
import de.lessvoid.nifty.controls.DropDownSelectionChangedEvent;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.input.keyboard.KeyboardInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.xml.xpp3.Attributes;

/**
 * The Controller for the EventConsumeDialog.
 */
public class EventConsumeDialogController implements Controller {
  private Screen screen;
  private Label mouseXText;
  private Label mouseYText;
  private Label mouseWheelText;
  private Label mouseButtonText;
  private Label mouseDownText;
  private Label mouseProcessedText;
  private DropDown<ElementInfo> eventConsumeElementDropDown;
  private CheckBox eventConsumeIgnoreMouseEventsCheckBox;
  private CheckBox eventConsumeIgnoreKeyboardEventsCheckBox;

  @Override
  public void bind(
      final Nifty nifty,
      final Screen screen,
      final Element element,
      final Properties parameter,
      final Attributes controlDefinitionAttributes) {
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
  public void init(final Properties parameter, final Attributes controlDefinitionAttributes) {
    eventConsumeElementDropDown.addItem(new ElementInfo(screen.findElementByName("eventConsumeLeftPanel"), "Green Panel"));
    eventConsumeElementDropDown.addItem(new ElementInfo(screen.findElementByName("eventConsumeLeftButton"), "Test Left Button"));
    eventConsumeElementDropDown.addItem(new ElementInfo(screen.findElementByName("eventConsumeRightPanel"), "Red Panel"));
    eventConsumeElementDropDown.addItem(new ElementInfo(screen.findElementByName("eventConsumeRightButton"), "Test Right Button"));
  }

  @Override
  public void onStartScreen() {
  }

  @Override
  public void onFocus(final boolean getFocus) {
  }

  @Override
  public boolean inputEvent(final NiftyInputEvent inputEvent) {
    return false;
  }

  @NiftyEventSubscriber(id="eventConsumeElementDropDown")
  public void eventConsumeElementDropDownChanged(final String id, final DropDownSelectionChangedEvent<ElementInfo> e) {
    eventConsumeIgnoreMouseEventsCheckBox.setChecked(e.getSelection().getElement().isIgnoreMouseEvents());
    eventConsumeIgnoreKeyboardEventsCheckBox.setChecked(e.getSelection().getElement().isIgnoreKeyboardEvents());
  }

  @NiftyEventSubscriber(id="eventConsumeIgnoreMouseEventsCheckBox")
  public void eventConsumeIgnoreMouseEventsCheckBoxToggle(final String id, final CheckBoxStateChangedEvent e) {
    eventConsumeElementDropDown.getSelection().getElement().setIgnoreMouseEvents(e.isChecked());
  }

  @NiftyEventSubscriber(id="eventConsumeIgnoreKeyboardEventsCheckBox")
  public void eventConsumeIgnoreKeyboardEventsCheckBoxToggle(final String id, final CheckBoxStateChangedEvent e) {
    eventConsumeElementDropDown.getSelection().getElement().setIgnoreKeyboardEvents(e.isChecked());
  }

  @NiftyEventSubscriber(pattern="eventConsume.*Button")
  public void onButtonClick(final String id, final ButtonClickedEvent e) {
    screen.findNiftyControl("eventConsumeButtonOut", Label.class).setText(e.getButton().getText() + " pressed");
  }

  private static class ElementInfo {
    private Element element;
    private String caption;

    public ElementInfo(final Element element, final String caption) {
      this.element = element;
      this.caption = caption;
    }

    public Element getElement() {
      return element;
    }

    public String toString() {
      return caption;
    }
  }
}
