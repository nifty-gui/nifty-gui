package de.lessvoid.nifty.controls;

import de.lessvoid.nifty.NiftyEvent;

public class FocusLostEvent implements NiftyEvent<Void> {
  private Controller controller;
  private NiftyControl niftyControl;

  public FocusLostEvent(final Controller controller, final NiftyControl niftyControl) {
    this.controller = controller;
    this.niftyControl = niftyControl;
  }

  public Controller getController() {
    return controller;
  }

  public NiftyControl getNiftyControl() {
    return niftyControl;
  }
}
