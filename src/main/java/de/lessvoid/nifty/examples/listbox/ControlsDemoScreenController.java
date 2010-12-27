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
import de.lessvoid.nifty.elements.Element;
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
  private TextField appendTextField;

  @Override
  public void bind(final Nifty nifty, final Screen screen) {
    this.nifty = nifty;
    this.screen = screen;

    // get the controls right here and save them for later use
    dialogPanel = screen.findElementByName("dialog");
    listBox = getListBox("listBox");
    selectionListBox = getListBox("selectionListBox");
    appendTextField = screen.findNiftyControl("addTextField", TextField.class);
    multiSelectionCheckBox = screen.findNiftyControl("multiSelectionCheckBox", CheckBox.class);
    disableSelectionCheckBox = screen.findNiftyControl("disableSelectionCheckBox", CheckBox.class);
    forceSelectionCheckBox  = screen.findNiftyControl("forceSelectionCheckBox", CheckBox.class);
    appendButton = screen.findNiftyControl("addAction", Button.class);

    appendFieldAndButtonUpdate();
  }

  @Override
  public void onStartScreen() {
    // We can have the event annotations on every class! it's not required to be a ScreenController or something
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
    System.out.println(screen.debugOutput());
  }

  /**
   * Called from Nifty on the button press. Add a new Item to the ListBox. 
   */
  public void addListBoxItem() {
    String newItemContent = appendTextField.getText();
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

  
//  @NiftyEventSubscriber(pattern=".*")
//  public void onNiftyInputControlEvent(final String id, final NiftyInputControlEvent event) {
//    System.out.println("[" + id + "] sent NiftyInputControlEvent " + event);
//  }
//
//  @NiftyEventSubscriber(id="addTextField")
//  public void onAppendTextFieldEvent(final String id, final NiftyInputControlEvent event) {
//    System.out.println("direct addTextField sent NiftyInputControlEvent " + event);
//  }
  
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

  private void appendFieldAndButtonUpdate() {
    appendButton.disable();
  }
}
