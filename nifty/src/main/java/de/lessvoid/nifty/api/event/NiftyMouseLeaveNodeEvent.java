package de.lessvoid.nifty.api.event;

import de.lessvoid.nifty.api.NiftyNode;

public class NiftyMouseLeaveNodeEvent implements NiftyEvent {
  private final NiftyNode niftyNode;

  public NiftyMouseLeaveNodeEvent(final NiftyNode niftyNode) {
    this.niftyNode = niftyNode;
  }

  public String toString() {
    return "mouse leave [" + niftyNode.toString() + "]";
  }
}
