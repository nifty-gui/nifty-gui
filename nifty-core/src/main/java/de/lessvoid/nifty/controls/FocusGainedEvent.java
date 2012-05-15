package de.lessvoid.nifty.controls;

import de.lessvoid.nifty.NiftyEvent;

public class FocusGainedEvent implements NiftyEvent {
  private Controller controller;
  private NiftyControl niftyControl;

  public FocusGainedEvent(final Controller controller, final NiftyControl niftyControl) {
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
