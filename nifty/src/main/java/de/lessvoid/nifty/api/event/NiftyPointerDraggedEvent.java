package de.lessvoid.nifty.api.event;

import de.lessvoid.nifty.api.NiftyNode;

public class NiftyPointerDraggedEvent implements NiftyEvent {
  private final NiftyNode niftyNode;
  private final int button;
  private final int x;
  private final int y;

  public NiftyPointerDraggedEvent(final NiftyNode niftyNode, final int button, final int x, final int y) {
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
    return "pointer dragged [" + niftyNode.toString() + "]: " + button + " (" + x + ", " + y + ")";
  }
}
