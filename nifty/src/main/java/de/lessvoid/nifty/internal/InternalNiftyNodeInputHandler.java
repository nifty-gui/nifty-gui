package de.lessvoid.nifty.internal;

import java.util.logging.Level;
import java.util.logging.Logger;

import de.lessvoid.nifty.api.event.NiftyMouseEnterNodeEvent;
import de.lessvoid.nifty.api.event.NiftyMouseHoverEvent;
import de.lessvoid.nifty.api.event.NiftyMouseLeaveNodeEvent;
import de.lessvoid.nifty.api.input.NiftyPointerEvent;
import de.lessvoid.nifty.internal.math.Vec4;

/**
 * This class handles all of the input events for a single node.
 * @author void
 */
public class InternalNiftyNodeInputHandler {
  private final Logger logger = Logger.getLogger(InternalNiftyNodeInputHandler.class.getName());
  private final InternalNiftyNode internalNiftyNode;

  private boolean mouseOverNode = false;

  public InternalNiftyNodeInputHandler(final InternalNiftyNode niftyNode) {
    this.internalNiftyNode = niftyNode;
  }

  public void pointerEvent(final InternalNiftyEventBus eventBus, final NiftyPointerEvent pointerEvent) {
    if (eventBus == null) {
      return;
    }
    if (isInside(pointerEvent.getX(), pointerEvent.getY())) {
      // inside
      if (logger.isLoggable(Level.FINE)) {
        logger.fine("HOVER [" + internalNiftyNode.getId() + "] (" + pointerEvent + ")");
      }

      if (!mouseOverNode) {
        mouseOverNode = true;
        eventBus.publish(new NiftyMouseEnterNodeEvent(internalNiftyNode.getNiftyNode()));
      }

      eventBus.publish(new NiftyMouseHoverEvent(internalNiftyNode.getNiftyNode()));
    } else {
      // outside
      if (mouseOverNode) {
        mouseOverNode = false;
        eventBus.publish(new NiftyMouseLeaveNodeEvent(internalNiftyNode.getNiftyNode()));
      }
    }
  }

  private boolean isInside(final int x, final int y) {
    Vec4 t = internalNiftyNode.screenToLocal(x, y);
    return (t.x >= 0 && t.x <= internalNiftyNode.getWidth() && t.y >= 0 && t.y <= internalNiftyNode.getHeight());
  }
}
