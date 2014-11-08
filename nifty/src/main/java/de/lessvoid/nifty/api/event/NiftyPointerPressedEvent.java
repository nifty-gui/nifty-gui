package de.lessvoid.nifty.api.event;

import de.lessvoid.nifty.api.NiftyNode;

public class NiftyPointerPressedEvent implements NiftyEvent {
  private final NiftyNode niftyNode;
  private int button;
  private final int x;
  private final int y;

  public NiftyPointerPressedEvent(final NiftyNode niftyNode, final int button, final int x, final int y) {
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
    return "pointer pressed [" + niftyNode.toString() + "]: " + button + " (" + x + ", " + y + ")";
  }
}
