package de.lessvoid.nifty.api.event;

import de.lessvoid.nifty.api.NiftyNode;

public class NiftyMouseExitNodeEvent implements NiftyEvent {
  private final NiftyNode niftyNode;

  public NiftyMouseExitNodeEvent(final NiftyNode niftyNode) {
    this.niftyNode = niftyNode;
  }

  public String toString() {
    return "mouse leave [" + niftyNode.toString() + "]";
  }
}
