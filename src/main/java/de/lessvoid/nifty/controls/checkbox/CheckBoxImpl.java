package de.lessvoid.nifty.controls.checkbox;

import de.lessvoid.nifty.controls.CheckBox;
import de.lessvoid.nifty.controls.CheckBoxStateChangedEvent;

public class CheckBoxImpl implements CheckBox {
  private boolean checked;
  private CheckBoxView view = new CheckBoxViewNull();

  public void bindToView(final CheckBoxView checkBoxView) {
    this.view = checkBoxView;
  }

  @Override
  public void check() {
    checked = true;
    updateView();
  }

  @Override
  public void uncheck() {
    checked = false;
    updateView();
  }

  @Override
  public void setChecked(final boolean state) {
    checked = state;
    updateView();
  }

  @Override
  public boolean isChecked() {
    return checked;
  }

  @Override
  public void toggle() {
    setChecked(!isChecked());
  }

  private void updateView() {
    view.update(checked);
    view.publish(new CheckBoxStateChangedEvent(checked));
  }
}
