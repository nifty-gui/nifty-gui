package de.lessvoid.nifty.api.event;

import de.lessvoid.nifty.api.NiftyNode;

public class NiftyPointerEnterNodeEvent implements NiftyEvent {
  private final NiftyNode niftyNode;

  public NiftyPointerEnterNodeEvent(final NiftyNode niftyNode) {
    this.niftyNode = niftyNode;
  }

  public String toString() {
    return "pointer enter [" + niftyNode.toString() + "]";
  }
}
