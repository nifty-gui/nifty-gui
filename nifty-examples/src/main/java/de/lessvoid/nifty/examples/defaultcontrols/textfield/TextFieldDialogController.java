package de.lessvoid.nifty.examples.defaultcontrols.textfield;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.*;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.input.NiftyStandardInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.Color;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * The ListBoxDialog to show off the new ListBox and a couple of more new Nifty 1.3 things.
 * @author void
 */
public class TextFieldDialogController implements Controller {
  @Nullable
  private TextField mainTextField;
  @Nullable
  private CheckBox passwordCharCheckBox;
  @Nullable
  private TextField passwordCharTextField;
  @Nullable
  private CheckBox maxLengthEnableCheckBox;
  @Nullable
  private TextField maxLengthTextField;
  @Nullable
  private Label textChangedLabel;
  @Nullable
  private Label keyEventLabel;
  
  @Override
  public void bind(
      @Nonnull final Nifty nifty,
      @Nonnull final Screen screen,
      @Nonnull final Element element,
      @Nonnull final Parameters parameter) {
    this.mainTextField = screen.findNiftyControl("mainTextField", TextField.class);
    this.passwordCharCheckBox = screen.findNiftyControl("passwordCharCheckBox", CheckBox.class);
    this.passwordCharTextField = screen.findNiftyControl("passwordCharTextField", TextField.class);
    this.maxLengthEnableCheckBox = screen.findNiftyControl("maxLengthEnableCheckBox", CheckBox.class);
    this.maxLengthTextField = screen.findNiftyControl("maxLengthTextField", TextField.class);
    this.textChangedLabel = screen.findNiftyControl("textChangedLabel", Label.class);
    this.keyEventLabel = screen.findNiftyControl("keyEventLabel", Label.class);
  }

  @Override
  public void init(@Nonnull final Parameters parameter) {
    passwordCharTextField.setText("*");
    maxLengthTextField.setText("5");
    textChangedLabel.setText("---");
    keyEventLabel.setText("---");
    keyEventLabel.setColor(new Color("#ff0f"));
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
  public boolean inputEvent(@Nonnull final NiftyInputEvent inputEvent) {
    return false;
  }

  @Override
  public void onEndScreen() {

  }

  @NiftyEventSubscriber(id="passwordCharCheckBox")
  public void onPasswordCharCheckBoxChanged(final String id, final CheckBoxStateChangedEvent event) {
    setPasswordCharTextFieldEnableState();
  }

  @NiftyEventSubscriber(id="maxLengthEnableCheckBox")
  public void onMaxLengthEnableCheckBoxChanged(final String id, final CheckBoxStateChangedEvent event) {
    setMaxLengthFieldEnableState();
  }

  @NiftyEventSubscriber(id="passwordCharTextField")
  public void onPasswordCharTextFieldChanged(final String id, final TextFieldChangedEvent event) {
    updatePasswordChar();
  }

  @NiftyEventSubscriber(id="mainTextField")
  public void onTextChanged(final String id, @Nonnull final TextFieldChangedEvent event) {
    textChangedLabel.setText(event.getText());
  }

  @NiftyEventSubscriber(id="maxLengthTextField")
  public void onMaxLengthTextChanged(final String id, @Nonnull final TextFieldChangedEvent event) {
    setMaxLength(event.getText());
  }

  private void setMaxLength(final String text) {
    try {
      mainTextField.setMaxLength(Integer.valueOf(text));
    } catch (Exception e) {
    }
  }

  @NiftyEventSubscriber(id="mainTextField")
  public void onTextChanged(final String id, @Nonnull final NiftyStandardInputEvent event) {
    keyEventLabel.setText(event.toString() + " [" + event.getCharacter() + "]");
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

  private void setMaxLengthFieldEnableState() {
    maxLengthTextField.setEnabled(maxLengthEnableCheckBox.isChecked());
    if (maxLengthEnableCheckBox.isChecked()) {
      setMaxLength(maxLengthTextField.getText());
    } else {
      mainTextField.setMaxLength(-1);
    }
  }
}
