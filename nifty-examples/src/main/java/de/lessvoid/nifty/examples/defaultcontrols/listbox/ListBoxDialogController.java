package de.lessvoid.nifty.examples.defaultcontrols.listbox;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.*;
import de.lessvoid.nifty.controls.ListBox.SelectionMode;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.examples.defaultcontrols.common.JustAnExampleModelClass;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.input.NiftyStandardInputEvent;
import de.lessvoid.nifty.screen.Screen;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * The ListBoxDialog to show off the new ListBox and a couple of more new Nifty 1.3 things.
 * @author void
 */
public class ListBoxDialogController implements Controller {
  private Screen screen;
  @Nullable
  private ListBox<JustAnExampleModelClass> listBox;
  @Nullable
  private ListBox<JustAnExampleModelClass> selectionListBox;
  @Nullable
  private CheckBox multiSelectionCheckBox;
  @Nullable
  private CheckBox disableSelectionCheckBox;
  @Nullable
  private CheckBox forceSelectionCheckBox;
  @Nullable
  private Button appendButton;
  @Nullable
  private Button removeSelectionButton;
  @Nullable
  private TextField addTextField;
  @Nullable
  private Label selectedIndices;

  @Override
  public void bind(
      @Nonnull final Nifty nifty,
      @Nonnull final Screen screen,
      @Nonnull final Element element,
      @Nonnull final Parameters parameter) {
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
  public void init(@Nonnull final Parameters parameter) {
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
  public boolean inputEvent(@Nonnull final NiftyInputEvent inputEvent) {
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
  public void onListBoxSelectionChanged(final String id, @Nonnull final ListBoxSelectionChangedEvent<JustAnExampleModelClass> changed) {
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
  public void onAddTextFieldInputEvent(final String id, final NiftyStandardInputEvent event) {
    if (NiftyStandardInputEvent.SubmitText.equals(event)) {
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

  @Nonnull
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

  private void updateSelectedIndexLabel(@Nullable final List<Integer> selectionIndices) {
    if (selectionIndices == null || selectionIndices.isEmpty()) {
      selectedIndices.setText("N/A");
      return;
    }
    StringBuilder text = new StringBuilder();
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

  @Nullable
  @SuppressWarnings("unchecked")
  private ListBox<JustAnExampleModelClass> getListBox(final String name) {
    return (ListBox<JustAnExampleModelClass>) screen.findNiftyControl(name, ListBox.class);
  }
}
