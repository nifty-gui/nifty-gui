package de.lessvoid.nifty.controls.checkbox;

import de.lessvoid.nifty.controls.CheckBox;
import de.lessvoid.nifty.controls.CheckBoxStateChangedEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.logging.Logger;

class CheckBoxImpl {
  private static final Logger log = Logger.getLogger(CheckBoxImpl.class.getName());
  @Nonnull
  private final CheckBox checkbox;
  private boolean checked;
  @Nullable
  private CheckBoxView view;

  public CheckBoxImpl(@Nonnull final CheckBox checkbox) {
    this.checkbox = checkbox;
  }

  public void bindToView(@Nonnull final CheckBoxView checkBoxView) {
    this.view = checkBoxView;
  }

  public void check() {
    internalSetChecked(true);
  }

  public void uncheck() {
    internalSetChecked(false);
  }

  public void setChecked(final boolean state) {
    internalSetChecked(state);
  }

  public boolean isChecked() {
    return checked;
  }

  public void toggle() {
    setChecked(!isChecked());
  }

  private void internalSetChecked(final boolean newState) {
    // when there is no state change then, well, don't change the state :)
    if (checked == newState) {
      return;
    }
    changeChecked(newState);
  }

  private void changeChecked(final boolean newState) {
    checked = newState;
    updateView();
  }

  void setInitialCheckedState(final boolean checked) {
    changeChecked(checked);
  }

  private void updateView() {
    if (view == null) {
      log.warning("Updating view is not possible before the view is bound. The bind call is likely missing.");
    } else {
      view.update(checked);
      view.publish(new CheckBoxStateChangedEvent(checkbox, checked));
    }
  }
}
