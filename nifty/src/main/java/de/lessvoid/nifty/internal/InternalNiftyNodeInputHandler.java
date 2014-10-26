package de.lessvoid.nifty.internal;

import java.util.logging.Logger;

import de.lessvoid.nifty.api.input.NiftyPointerEvent;
import de.lessvoid.nifty.internal.math.Vec4;

/**
 * This class handles all of the input events for a single node.
 * @author void
 */
public class InternalNiftyNodeInputHandler {
  private final Logger logger = Logger.getLogger(InternalNiftyNodeInputHandler.class.getName());
  private final InternalNiftyNode internalNiftyNode;

  public InternalNiftyNodeInputHandler(final InternalNiftyNode niftyNode) {
    this.internalNiftyNode = niftyNode;
  }

  public void pointerEvent(final NiftyPointerEvent pointerEvent) {
    if (isInside(pointerEvent.getX(), pointerEvent.getY())) {
      logger.fine("HOVER [" + internalNiftyNode.getId() + "] (" + pointerEvent + ")");
    }
  }

  private boolean isInside(final int x, final int y) {
    Vec4 t = internalNiftyNode.screenToLocal(x, y);
    return (t.x >= 0 && t.x <= internalNiftyNode.getWidth() && t.y >= 0 && t.y <= internalNiftyNode.getHeight());
  }
}
