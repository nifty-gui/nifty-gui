package de.lessvoid.nifty.api.event;

import de.lessvoid.nifty.api.NiftyNode;

public class NiftyMouseHoverEvent implements NiftyEvent {
  private final NiftyNode niftyNode;

  public NiftyMouseHoverEvent(final NiftyNode niftyNode) {
    this.niftyNode = niftyNode;
  }

  public String toString() {
    return "mouse hover [" + niftyNode.toString() + "]";
  }
}
