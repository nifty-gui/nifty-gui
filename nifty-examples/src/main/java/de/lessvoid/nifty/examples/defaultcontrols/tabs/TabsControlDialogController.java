package de.lessvoid.nifty.examples.defaultcontrols.tabs;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.CheckBox;
import de.lessvoid.nifty.controls.CheckBoxStateChangedEvent;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.controls.TabGroup;
import de.lessvoid.nifty.controls.checkbox.builder.CheckboxBuilder;
import de.lessvoid.nifty.controls.label.builder.LabelBuilder;
import de.lessvoid.nifty.controls.tabs.builder.TabBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.events.NiftyMousePrimaryClickedEvent;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;

/**
 * The TabsControlDialogController registers a new control with Nifty that represents the whole Dialog. This gives us
 * later an appropriate ControlBuilder to actual construct the Dialog (as a control).
 *
 * @author ractoc
 */
public class TabsControlDialogController implements Controller {

  private TabGroup tabs;
  private Screen screen;

  @Override
  public void bind(
      final Nifty nifty,
      final Screen screen,
      final Element element,
      final Properties parameter,
      final Attributes controlDefinitionAttributes) {
    this.screen = screen;
    tabs = screen.findNiftyControl("tabs", TabGroup.class);
    tabs.addTab(new TabBuilder("tab_1", "Tab 1") {{
      childLayoutVertical();
      height("100%");
      width("100%");
      control(new LabelBuilder("tab1_label", "Tab 1") {{
        height("50%");
        width("100%");
      }});
      control(new LabelBuilder("tab1_label_checkboxstate", "Checkbox state: ---") {{
        height("50%");
        width("100%");
        visibleToMouse();
      }});
    }});
    tabs.addTab(new TabBuilder("tab_2", "Tab 2") {{
      childLayoutCenter();
      control(new CheckboxBuilder("tab_2_checkbox") {{
      }});
      height("100%");
      width("100%");
    }});
  }

  @Override
  public void init(final Properties parameter, final Attributes controlDefinitionAttributes) {
  }

  @Override
  public void onStartScreen() {
    displayCheckBoxState(screen.findNiftyControl("tab_2_checkbox", CheckBox.class));
  }

  @Override
  public void onFocus(final boolean getFocus) {
  }

  @Override
  public boolean inputEvent(final NiftyInputEvent inputEvent) {
    return false;
  }

  @NiftyEventSubscriber(id="tab1_label_checkboxstate")
  public void onLabelClick(final String id, final NiftyMousePrimaryClickedEvent label) {
    CheckBox checkBox = screen.findNiftyControl("tab_2_checkbox", CheckBox.class);
    checkBox.toggle();
    displayCheckBoxState(checkBox);
  }

  @NiftyEventSubscriber(id="tab_2_checkbox")
  public void checkBoxStateChange(final String id, final CheckBoxStateChangedEvent stateChanged) {
    displayCheckBoxState(stateChanged.getCheckBox());
  }

  private void displayCheckBoxState(final CheckBox checkBox) {
    screen.findElementByName("tab1_label_checkboxstate")
      .getRenderer(TextRenderer.class)
      .setText("Checkbox state (on Tab 2)\nisChecked(): " + checkBox.isChecked() + "\n\n(click me!)");
  }
}
