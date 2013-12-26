package de.lessvoid.nifty.controls;

import de.lessvoid.nifty.NiftyEvent;

public class FocusLostEvent implements NiftyEvent {
  private final Controller controller;
  private final NiftyControl niftyControl;

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
