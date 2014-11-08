package de.lessvoid.nifty.api.event;

import de.lessvoid.nifty.api.NiftyNode;

public class NiftyPointerExitNodeEvent implements NiftyEvent {
  private final NiftyNode niftyNode;

  public NiftyPointerExitNodeEvent(final NiftyNode niftyNode) {
    this.niftyNode = niftyNode;
  }

  public String toString() {
    return "pointer leave [" + niftyNode.toString() + "]";
  }
}
