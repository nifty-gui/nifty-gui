package de.lessvoid.nifty.examples.listbox;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventAnnotationProcessor;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.builder.ControlBuilder;
import de.lessvoid.nifty.builder.EffectBuilder;
import de.lessvoid.nifty.builder.LabelBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.PopupBuilder;
import de.lessvoid.nifty.controls.Button;
import de.lessvoid.nifty.controls.CheckBox;
import de.lessvoid.nifty.controls.CheckBoxStateChangedEvent;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.ListBox.SelectionMode;
import de.lessvoid.nifty.controls.ListBoxSelectionChangedEvent;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.controls.TextFieldChangedEvent;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class ControlsDemoScreenController implements ScreenController {
  private Nifty nifty;
  private Screen screen;
  private ListBox<JustAnExampleModelClass> listBox;
  private ListBox<JustAnExampleModelClass> selectionListBox;
  private Element dialogPanel;
  private CheckBox multiSelectionCheckBox;
  private CheckBox disableSelectionCheckBox;
  private CheckBox forceSelectionCheckBox;
  private Button appendButton;
  private Button removeSelectionButton;
  private TextField addTextField;
  private Element popup;

  @Override
  public void bind(final Nifty nifty, final Screen screen) {
    this.nifty = nifty;
    this.screen = screen;

    // get the controls right here and save them for later use
    dialogPanel = screen.findElementByName("dialog");
    listBox = getListBox("listBox");
    selectionListBox = getListBox("selectionListBox");
    addTextField = screen.findNiftyControl("addTextField", TextField.class);
    multiSelectionCheckBox = screen.findNiftyControl("multiSelectionCheckBox", CheckBox.class);
    disableSelectionCheckBox = screen.findNiftyControl("disableSelectionCheckBox", CheckBox.class);
    forceSelectionCheckBox  = screen.findNiftyControl("forceSelectionCheckBox", CheckBox.class);
    appendButton = screen.findNiftyControl("appendButton", Button.class);
    removeSelectionButton = screen.findNiftyControl("removeSelectionButton", Button.class);

    popup = registerPopup();

    setAppendButtonState();
    setRemoveSelectionButtonState();
  }

  @Override
  public void onStartScreen() {
    // We can have the event annotations on every class! It's not required to be a ScreenController or something
    // even related to Nifty. We only need to make sure to call this method to enable the annotations.
    NiftyEventAnnotationProcessor.process(this);    
  }

  @Override
  public void onEndScreen() {
    NiftyEventAnnotationProcessor.unprocess(this);    
  }

  public void changeStyle(final String styleName) {
    if (dialogPanel.isEnabled()) {
      dialogPanel.disable();
    } else {
      dialogPanel.enable();
    }
    System.out.println(screen.debugOutput(".*enabled.*"));
  }

  /**
   * Called from Nifty on the button press. Add a new Item to the ListBox. 
   */
  public void addListBoxItem() {
    // add the item and make sure that the last item is shown
    listBox.addItem(new JustAnExampleModelClass(addTextField.getText()));
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
   * This is an example how we could use a regular expression to select the elements we're interested in.
   * In this example all of our CheckBox Nifty Ids end with "CheckBox" and - in this example - all Checkboxes
   * influence the SelectionMode of the ListBox. All CheckBoxes really do the same here so we can take
   * this shortcut of handling all checkboxes equal.
   *
   * And we can demonstrate the @NiftyEventSubscriber annotation in pattern mode :) 
   */
  @NiftyEventSubscriber(pattern=".*CheckBox")
  public void onAllCheckBoxChanged(final String id, final CheckBoxStateChangedEvent event) {
    changeSelectionMode();
  }

  /**
   * This event handler is directly listening to ListBoxSelectionChangedEvent of a single Control
   * (the one with the Nifty id "listBox").
   */
  @NiftyEventSubscriber(id="listBox")
  public void onListBoxSelectionChanged(final String id, final ListBoxSelectionChangedEvent<JustAnExampleModelClass> changed) {
    // Now take the new selection of the listBox and apply it to the selectionListBox to show the current selection
    selectionListBox.clear();
    selectionListBox.addAllItems(changed.getSelection());
    setRemoveSelectionButtonState();
  }

  @NiftyEventSubscriber(id="addTextField")
  public void onAppendTextFieldChanged(final String id, final TextFieldChangedEvent event) {
    setAppendButtonState();
  }

  @NiftyEventSubscriber(id="addTextField")
  public void onAddTextFieldInputEvent(final String id, final NiftyInputEvent event) {
    if (NiftyInputEvent.SubmitText.equals(event)) {
      if (addTextField.getText().length() == 0) {
        showPopup("Yeah, nice idea! This will work when you've entered some text first! :)");
        return;
      }

      appendButton.activate();
    }
  }

  public void closePopup() {
    nifty.closePopup("test");
  }

  private void showPopup(final String message) {
    popup.findElementByName("message").getRenderer(TextRenderer.class).setText(message);
    nifty.showPopup(screen, "test", null);
  }

  private Element registerPopup() {
    return new PopupBuilder("test") {{
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
        width("55%");
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
          label(new LabelBuilder("message") {{
            alignCenter();
            text("");
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
    }}.registerPopup(nifty);
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

  private void setAppendButtonState() {
    if (addTextField.getText().isEmpty()) {
      appendButton.disable();
    } else {
      appendButton.enable();
    }
    System.out.println(screen.debugOutput(".*enabled.*"));
  }

  private void setRemoveSelectionButtonState() {
    if (selectionListBox.itemCount() == 0) {
      removeSelectionButton.disable();
    } else {
      removeSelectionButton.enable();
    }
    System.out.println(screen.debugOutput(".*enabled.*"));
  }
}
