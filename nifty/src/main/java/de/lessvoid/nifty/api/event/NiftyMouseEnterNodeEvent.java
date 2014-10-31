package de.lessvoid.nifty.api.event;

import de.lessvoid.nifty.api.NiftyNode;

public class NiftyMouseEnterNodeEvent implements NiftyEvent {
  private final NiftyNode niftyNode;

  public NiftyMouseEnterNodeEvent(final NiftyNode niftyNode) {
    this.niftyNode = niftyNode;
  }

  public String toString() {
    return "mouse enter [" + niftyNode.toString() + "]";
  }
}
