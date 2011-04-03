package de.lessvoid.nifty.examples.controls.dropdown;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.Button;
import de.lessvoid.nifty.controls.ButtonClickedEvent;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.controls.DropDown;
import de.lessvoid.nifty.controls.DropDownSelectionChangedEvent;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.controls.TextFieldChangedEvent;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.examples.controls.common.JustAnExampleModelClass;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;

public class DropDownDialogController implements Controller {
  private TextField addDropDownItemText;
  private Button addDropDownItemButton;
  private DropDown<JustAnExampleModelClass> dropDown;
  private Label selectedItem;
  private Button removeDropDownItemButton;
  private Label selectedIndices;

  @SuppressWarnings("unchecked")
  @Override
  public void bind(
      final Nifty nifty,
      final Screen screen,
      final Element element,
      final Properties parameter,
      final Attributes controlDefinitionAttributes) {
    this.addDropDownItemText = screen.findNiftyControl("addDropDownItemText", TextField.class);
    this.addDropDownItemButton = screen.findNiftyControl("addDropDownItemButton", Button.class);
    this.dropDown = screen.findNiftyControl("dropDown", DropDown.class);
    this.selectedItem = screen.findNiftyControl("selectedItem", Label.class);
    this.removeDropDownItemButton = screen.findNiftyControl("removeDropDownItemButton", Button.class);
    this.selectedIndices = element.findNiftyControl("#selectedIndices", Label.class);
  }

  @Override
  public void init(final Properties parameter, final Attributes controlDefinitionAttributes) {
    setDropDownItemButtonState();
    setRemoveDropDownItemButtonState(null);
    updateSelectedIndexLabel(dropDown.getSelectedIndex());
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

  @NiftyEventSubscriber(id="addDropDownItemText")
  public void onAppendTextFieldChanged(final String id, final TextFieldChangedEvent event) {
    setDropDownItemButtonState();
  }

  @NiftyEventSubscriber(id="addDropDownItemText")
  public void onAddTextFieldInputEvent(final String id, final NiftyInputEvent event) {
    if (NiftyInputEvent.SubmitText.equals(event)) {
      addDropDownItemButton.activate();
    }
  }

  @NiftyEventSubscriber(id="addDropDownItemButton")
  public void onAppendButtonClicked(final String id, final ButtonClickedEvent event) {
    if (addDropDownItemText.getText().length() == 0) {
      return;
    }

    // add the item and make sure that the last item is shown
    JustAnExampleModelClass newItem = new JustAnExampleModelClass(addDropDownItemText.getText());
    dropDown.addItem(newItem);
    dropDown.selectItem(newItem);
  }

  @NiftyEventSubscriber(id="dropDown")
  public void onDropDownSelectionChanged(final String id, final DropDownSelectionChangedEvent<JustAnExampleModelClass> event) {
    if (event.getSelection() == null) {
      selectedItem.setText("");
    } else {
      selectedItem.setText(event.getSelection().toString());
    }
    setRemoveDropDownItemButtonState(event.getSelection());
    updateSelectedIndexLabel(event.getSelectionItemIndex());
  }

  @NiftyEventSubscriber(id="removeDropDownItemButton")
  public void onRemoveDropDownItemClicked(final String id, final ButtonClickedEvent event) {
    dropDown.removeItem(dropDown.getSelection());
  }

  private void setDropDownItemButtonState() {
    if (addDropDownItemText.getText().isEmpty()) {
      addDropDownItemButton.disable();
    } else {
      addDropDownItemButton.enable();
    }
  }

  private void setRemoveDropDownItemButtonState(final JustAnExampleModelClass item) {
    if (item == null) {
      removeDropDownItemButton.disable();
    } else {
      removeDropDownItemButton.enable();
    }
  }

  private void updateSelectedIndexLabel(final int selectedItemIndex) {
    if (selectedItemIndex == -1) {
      selectedIndices.setText("N/A");
      return;
    }
    selectedIndices.setText(String.valueOf(selectedItemIndex));
  }
}
