package de.lessvoid.nifty.api.event;

import de.lessvoid.nifty.api.NiftyNode;

public class NiftyPointerHoverEvent implements NiftyEvent {
  private final NiftyNode niftyNode;
  private final int x;
  private final int y;

  public NiftyPointerHoverEvent(final NiftyNode niftyNode, final int x, final int y) {
    this.niftyNode = niftyNode;
    this.x = x;
    this.y = y;
  }

  public String toString() {
    return "pointer hover [" + niftyNode.toString() + "] (" + x + ", " + y + ")";
  }
}
