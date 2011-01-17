package de.lessvoid.nifty.examples.controls.textfield;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.CheckBox;
import de.lessvoid.nifty.controls.CheckBoxStateChangedEvent;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.controls.TextFieldChangedEvent;
import de.lessvoid.nifty.elements.ControllerEventListener;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;

/**
 * The ListBoxDialog to show off the new ListBox and a couple of more new Nifty 1.3 things.
 * @author void
 */
public class TextFieldDialogController implements Controller {
  private Nifty nifty;
  private Screen screen;
  private TextField mainTextField;
  private CheckBox passwordCharCheckBox;
  private TextField passwordCharTextField;
  private CheckBox maxLengthEnableCheckBox;
  private TextField maxLengthTextField;
  
  @Override
  public void bind(
      Nifty nifty,
      Screen screen,
      Element element,
      Properties parameter,
      ControllerEventListener listener,
      Attributes controlDefinitionAttributes) {
    this.nifty = nifty;
    this.screen = screen;
    this.mainTextField = screen.findNiftyControl("mainTextField", TextField.class);
    this.passwordCharCheckBox = screen.findNiftyControl("passwordCharCheckBox", CheckBox.class);
    this.passwordCharTextField = screen.findNiftyControl("passwordCharTextField", TextField.class);
    this.maxLengthEnableCheckBox = screen.findNiftyControl("maxLengthEnableCheckBox", CheckBox.class);
    this.maxLengthTextField = screen.findNiftyControl("maxLengthTextField", TextField.class);
//    this.textChangedLabel = screen.findNiftyControl("textChangedLabel", Label.class);
//    this.keyEventLabel = screen.findNiftyControl("keyEventLabel", Label.class);
    setPasswordCharTextFieldEnableState();
    setMaxLengthFieldEnableState();
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

  @NiftyEventSubscriber(id="passwordCharCheckBox")
  public void onPasswordCharCheckBoxChanged(final String id, final CheckBoxStateChangedEvent event) {
    setPasswordCharTextFieldEnableState();
  }

  @NiftyEventSubscriber(id="passwordCharTextField")
  public void onPasswordCharTextFieldChanged(final String id, final TextFieldChangedEvent event) {
    updatePasswordChar();
  }

  private void setPasswordCharTextFieldEnableState() {
    passwordCharTextField.setEnabled(passwordCharCheckBox.isChecked());
    updatePasswordChar();
  }

  private void updatePasswordChar() {
    if (passwordCharCheckBox.isChecked()) {
      if (passwordCharTextField.getText().isEmpty()) {
        mainTextField.disablePasswordChar();
      } else {
        mainTextField.enablePasswordChar(passwordCharTextField.getText().charAt(0));
      }
    } else {
      mainTextField.disablePasswordChar();
    }
  }

  @NiftyEventSubscriber(id="maxLengthEnableCheckBox")
  public void onMaxLengthEnableCheckBoxChanged(final String id, final CheckBoxStateChangedEvent event) {
    setMaxLengthFieldEnableState();
  }

  private void setMaxLengthFieldEnableState() {
    maxLengthTextField.setEnabled(maxLengthEnableCheckBox.isChecked());
  }
}
