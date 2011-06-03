package de.lessvoid.nifty.examples.controls.listbox;

import java.util.List;
import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.Button;
import de.lessvoid.nifty.controls.ButtonClickedEvent;
import de.lessvoid.nifty.controls.CheckBox;
import de.lessvoid.nifty.controls.CheckBoxStateChangedEvent;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.ListBox.SelectionMode;
import de.lessvoid.nifty.controls.ListBoxSelectionChangedEvent;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.controls.TextFieldChangedEvent;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.examples.controls.common.JustAnExampleModelClass;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;

/**
 * The ListBoxDialog to show off the new ListBox and a couple of more new Nifty 1.3 things.
 * @author void
 */
public class ListBoxDialogController implements Controller {
  private Screen screen;
  private ListBox<JustAnExampleModelClass> listBox;
  private ListBox<JustAnExampleModelClass> selectionListBox;
  private CheckBox multiSelectionCheckBox;
  private CheckBox disableSelectionCheckBox;
  private CheckBox forceSelectionCheckBox;
  private Button appendButton;
  private Button removeSelectionButton;
  private TextField addTextField;
  private Label selectedIndices;

  @Override
  public void bind(
      final Nifty nifty,
      final Screen screen,
      final Element element,
      final Properties parameter,
      final Attributes controlDefinitionAttributes) {
    this.screen = screen;
    this.listBox = getListBox("listBox");
    this.selectionListBox = getListBox("selectionListBox");
    this.addTextField = screen.findNiftyControl("addTextField", TextField.class);
    this.multiSelectionCheckBox = screen.findNiftyControl("multiSelectionCheckBox", CheckBox.class);
    this.disableSelectionCheckBox = screen.findNiftyControl("disableSelectionCheckBox", CheckBox.class);
    this.forceSelectionCheckBox  = screen.findNiftyControl("forceSelectionCheckBox", CheckBox.class);
    this.appendButton = screen.findNiftyControl("appendButton", Button.class);
    this.removeSelectionButton = screen.findNiftyControl("removeSelectionButton", Button.class);
    this.selectedIndices = screen.findNiftyControl("selectedIndices", Label.class);

    // just add some items to the listbox
    listBox.addItem(new JustAnExampleModelClass("You can add more lines to this ListBox."));
    listBox.addItem(new JustAnExampleModelClass("Use the append button to do this."));
    multiSelectionCheckBox.uncheck();
    disableSelectionCheckBox.uncheck();
    forceSelectionCheckBox.uncheck();
    addTextField.setText("");
  }

  @Override
  public void init(final Properties parameter, final Attributes controlDefinitionAttributes) {
    updateSelectedIndexLabel(listBox.getSelectedIndices());
    setAppendButtonState();
    setRemoveSelectionButtonState();
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

  /**
   * This is an example how we could use a regular expression to select the elements we're interested in.
   * In this example all of our CheckBox Nifty Ids end with "CheckBox" and - in this example - all Checkboxes
   * influence the SelectionMode of the ListBox. All CheckBoxes really do the same here so we can take
   * this shortcut of handling all CheckBoxes equal.
   *
   * And we can demonstrate the @NiftyEventSubscriber annotation in pattern mode :) 
   */
  @NiftyEventSubscriber(pattern=".*CheckBox")
  public void onAllCheckBoxChanged(final String id, final CheckBoxStateChangedEvent event) {
    listBox.changeSelectionMode(getSelectionMode(), forceSelectionCheckBox.isChecked());
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

    updateSelectedIndexLabel(changed.getSelectionIndices());
    setRemoveSelectionButtonState();
  }

  @NiftyEventSubscriber(id="addTextField")
  public void onAppendTextFieldChanged(final String id, final TextFieldChangedEvent event) {
    setAppendButtonState();
  }

  @NiftyEventSubscriber(id="addTextField")
  public void onAddTextFieldInputEvent(final String id, final NiftyInputEvent event) {
    if (NiftyInputEvent.SubmitText.equals(event)) {
      appendButton.activate();
    }
  }

  @NiftyEventSubscriber(id="appendButton")
  public void onAppendButtonClicked(final String id, final ButtonClickedEvent event) {
    if (addTextField.getText().length() == 0) {
      return;
    }

    // add the item and make sure that the last item is shown
    listBox.addItem(new JustAnExampleModelClass(addTextField.getText()));
    listBox.showItemByIndex(listBox.itemCount() - 1);
  }

  @NiftyEventSubscriber(id="removeSelectionButton")
  public void onRemoveSelectionButtonClicked(final String id, final ButtonClickedEvent event) {
    if (!listBox.getSelection().isEmpty()) {
      listBox.removeAllItems(listBox.getSelection());
    }
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
  }

  private void setRemoveSelectionButtonState() {
    if (selectionListBox.itemCount() == 0) {
      removeSelectionButton.disable();
    } else {
      removeSelectionButton.enable();
    }
  }

  private void updateSelectedIndexLabel(final List<Integer> selectionIndices) {
    if (selectionIndices == null || selectionIndices.isEmpty()) {
      selectedIndices.setText("N/A");
      return;
    }
    StringBuffer text = new StringBuffer();
    boolean first = true;
    for (Integer i : selectionIndices) {
      if (first) {
        first = false;
        text.append(i);
        continue;
      }
      text.append(", ");
      text.append(i);
    }
    selectedIndices.setText(text.toString());
  }

  @SuppressWarnings("unchecked")
  private ListBox<JustAnExampleModelClass> getListBox(final String name) {
    return (ListBox<JustAnExampleModelClass>) screen.findNiftyControl(name, ListBox.class);
  }
}
