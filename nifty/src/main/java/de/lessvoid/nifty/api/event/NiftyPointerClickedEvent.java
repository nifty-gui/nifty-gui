package de.lessvoid.nifty.api.event;

import de.lessvoid.nifty.api.NiftyNode;

public class NiftyPointerClickedEvent implements NiftyEvent {
  private final NiftyNode niftyNode;
  private final int button;
  private final int x;
  private final int y;

  public NiftyPointerClickedEvent(final NiftyNode niftyNode, final int button, final int x, final int y) {
    this.niftyNode = niftyNode;
    this.button = button;
    this.x = x;
    this.y = y;
  }

  public int getButton() {
    return button;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public String toString() {
    return "pointer clicked [" + niftyNode.toString() + "] (" + x + ", " + y + ")";
  }
}
