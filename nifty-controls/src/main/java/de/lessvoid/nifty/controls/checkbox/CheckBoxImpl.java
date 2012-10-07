package de.lessvoid.nifty.controls.checkbox;

import de.lessvoid.nifty.controls.CheckBox;
import de.lessvoid.nifty.controls.CheckBoxStateChangedEvent;
import de.lessvoid.nifty.controls.shared.EmptyNiftyControlImpl;

public class CheckBoxImpl extends EmptyNiftyControlImpl implements CheckBox {
  private CheckBox checkbox;
  private boolean checked;
  private CheckBoxView view = new CheckBoxViewNull();

  public CheckBoxImpl(final CheckBox checkbox) {
    this.checkbox = checkbox;
  }

  public void bindToView(final CheckBoxView checkBoxView) {
    this.view = checkBoxView;
  }

  @Override
  public void check() {
    internalSetChecked(true);
  }

  @Override
  public void uncheck() {
    internalSetChecked(false);
  }

  @Override
  public void setChecked(final boolean state) {
    internalSetChecked(state);
  }

  @Override
  public boolean isChecked() {
    return checked;
  }

  @Override
  public void toggle() {
    setChecked(!isChecked());
  }

  private void internalSetChecked(final boolean newState) {
    // when there is no state change then, well, don't change the state :)
    if (checked == newState) {
      return;
    }
    checked = newState;
    updateView();
  }

  private void updateView() {
    view.update(checked);
    view.publish(new CheckBoxStateChangedEvent(checkbox, checked));
  }
}
