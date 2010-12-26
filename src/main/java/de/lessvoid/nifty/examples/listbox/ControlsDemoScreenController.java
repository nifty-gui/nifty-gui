package de.lessvoid.nifty.examples.listbox;

import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventTopicPatternSubscriber;
import org.bushe.swing.event.annotation.EventTopicSubscriber;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ControlBuilder;
import de.lessvoid.nifty.builder.EffectBuilder;
import de.lessvoid.nifty.builder.LabelBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.PopupBuilder;
import de.lessvoid.nifty.controls.CheckBox;
import de.lessvoid.nifty.controls.CheckBoxStateChangedEvent;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.ListBox.SelectionMode;
import de.lessvoid.nifty.controls.ListBoxSelectionChangedEvent;
import de.lessvoid.nifty.controls.textfield.controller.TextFieldControl;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class ControlsDemoScreenController implements ScreenController {
  private Nifty nifty;
  private Screen screen;
  private ListBox<JustAnExampleModelClass> listBox;
  private ListBox<JustAnExampleModelClass> selectionListBox;
  private CheckBox multiSelectionCheckBox;
  private CheckBox disableSelectionCheckBox;
  private CheckBox forceSelectionCheckBox;
  private TextFieldControl addTextField;

  @Override
  public void bind(final Nifty nifty, final Screen screen) {
    this.nifty = nifty;
    this.screen = screen;

    // get the controls right here and save them for later use
    listBox = getListBox("listBox");
    selectionListBox = getListBox("selectionListBox");
    addTextField = screen.findControl("addTextField", TextFieldControl.class);
    multiSelectionCheckBox = screen.findNiftyControl("multiSelectionCheckBox", CheckBox.class);
    disableSelectionCheckBox = screen.findNiftyControl("disableSelectionCheckBox", CheckBox.class);
    forceSelectionCheckBox  = screen.findNiftyControl("forceSelectionCheckBox", CheckBox.class);

    // just add some items to the listbox
    listBox.addItem(new JustAnExampleModelClass("You can add more lines to this ListBox."));
    listBox.addItem(new JustAnExampleModelClass("Use the append button to do this."));

    // important to enable the EvenBus annotations
    AnnotationProcessor.process(this);
  }

  @Override
  public void onStartScreen() {
  }

  @Override
  public void onEndScreen() {
  }

  public void changeStyle(final String styleName) {
    nifty.loadStyleFile(styleName);
    nifty.gotoScreen("start");
  }

  /**
   * Called from Nifty on the button press. Add a new Item to the ListBox. 
   */
  public void addListBoxItem() {
    String newItemContent = addTextField.getText();
    if (newItemContent.length() == 0) {
      showPopup("You'll need to enter some text in the Append field first!");
      return;
    }

    // add the item and make sure that the last item is shown
    listBox.addItem(new JustAnExampleModelClass(newItemContent));
    listBox.showItemByIndex(listBox.itemCount() - 1);
  }

  /**
   * Called from Nifty on the Remove Selection Button press. Remove all currently selected items from the
   * original ListBox.
   */
  public void removeSelectionAction() {
    if (listBox.getSelection().isEmpty()) {
      showPopup("Dude, there is no current selection to remove!");
      return;
    }
    listBox.removeAllItems(listBox.getSelection());
  }

  /**
   * This is an example how we could use a regular expression to select the topics we're interested in.
   * In this example all of our CheckBox Nifty Ids end with "CheckBox" and - in this example - all Checkboxes
   * influence the SelectionMode of the ListBox. All CheckBoxes really do the same here so we can take
   * this shortcut and on the other hand demonstrate the @EventTopicPatternSubscriber annotation :) 
   */
  @EventTopicPatternSubscriber(topicPattern=".*CheckBox", eventServiceName="NiftyEventBus")
  public void onAllCheckBoxChanged(final String id, final CheckBoxStateChangedEvent event) {
    changeSelectionMode();
  }

  @EventTopicSubscriber(topic="listBox", eventServiceName="NiftyEventBus")
  public void onListBoxSelectionChanged(final String id, final ListBoxSelectionChangedEvent<JustAnExampleModelClass> changed) {
    // Now take the new selection of the listBox and apply it to the selectionListBox to show the current selection
    selectionListBox.clear();
    selectionListBox.addAllItems(changed.getSelection());
  }

  public void closePopup() {
    nifty.closePopup("test");
  }

  private void showPopup(final String message) {
    new PopupBuilder("test") {{
      backgroundColor("#000a");
      childLayoutCenter();
      onStartScreenEffect(new EffectBuilder("fade").length(150).parameter("start", "#0").parameter("end", "#a"));
      onStartScreenEffect(new EffectBuilder("playSound").length(200).startDelay(100).parameter("sound", "popup"));
      onEndScreenEffect(new EffectBuilder("fade").length(150).startDelay(250).parameter("startColor", "#000a").parameter("endColor", "#0000"));
      panel(new PanelBuilder() {{
        childLayoutCenter();
        alignCenter();
        valignCenter();
        style("nifty-panel-red");
        width("45%");
        height("20%");
        onStartScreenEffect(new EffectBuilder("fade").length(100).startDelay(150).parameter("start", "#0").parameter("end", "#f").inherit());
        onStartScreenEffect(new EffectBuilder("imageSize").length(100).startDelay(150).timeType("exp").parameter("factor", "3.5").parameter("startSize", "1.5").parameter("endSize", "1.0").inherit());
        onEndScreenEffect(new EffectBuilder("fade").length(100).startDelay(50).parameter("start", "#f").parameter("end", "#0").inherit());
        onEndScreenEffect(new EffectBuilder("imageSize").length(100).startDelay(50).timeType("exp").parameter("factor", "3.5").parameter("startSize", "1.0").parameter("endSize", "1.5").inherit());
        panel(new PanelBuilder() {{
          childLayoutVertical();
          alignCenter();
          valignCenter();
          onStartScreenEffect(new EffectBuilder("fade").length(50).startDelay(350).parameter("start", "#0").parameter("end", "#f").inherit());
          onEndScreenEffect(new EffectBuilder("fade").length(50).startDelay(0).parameter("start", "#f").parameter("end", "#0").inherit());
          label(new LabelBuilder() {{
            alignCenter();
            text(message);
          }});
          panel(new PanelBuilder() {{
            childLayoutHorizontal();
            height("22px");
          }});
          control(new ControlBuilder("button") {{
            alignCenter();
            set("label", "OK, Sorry!");
            interactOnClick("closePopup()");
          }});
        }});
      }});
    }}.build(nifty, screen, null);
    nifty.showPopup(screen, "test", null);
  }

  @SuppressWarnings("unchecked")
  private ListBox<JustAnExampleModelClass> getListBox(final String name) {
    return (ListBox<JustAnExampleModelClass>) screen.findNiftyControl(name, ListBox.class);
  }

  private void changeSelectionMode() {
    listBox.changeSelectionMode(getSelectionMode(), forceSelectionCheckBox.isChecked());
  }

  private SelectionMode getSelectionMode() {
    if (disableSelectionCheckBox.isChecked()) {
      return SelectionMode.Disabled;
    }
    if (multiSelectionCheckBox.isChecked()) {
      return SelectionMode.Multiple;
    }
    return SelectionMode.Single;
  }

  /**
   * This is just an example. This should really be your own model. The ListBox does call toString() on this
   * to get the label it should display. But you can use your own ListBoxViewConverter if you want to use more
   * sophisticated mechanism.
   * @author void
   */
  private class JustAnExampleModelClass {
    private String label;

    public JustAnExampleModelClass(final String label) {
      this.label = label;
    }

    public String toString() {
      return label;
    }
  }
}
