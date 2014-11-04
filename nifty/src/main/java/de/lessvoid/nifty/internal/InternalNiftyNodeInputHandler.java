package de.lessvoid.nifty.internal;

import java.util.logging.Level;
import java.util.logging.Logger;

import de.lessvoid.nifty.api.event.NiftyMouseEnterNodeEvent;
import de.lessvoid.nifty.api.event.NiftyMouseHoverEvent;
import de.lessvoid.nifty.api.event.NiftyMouseExitNodeEvent;
import de.lessvoid.nifty.api.input.NiftyPointerEvent;
import de.lessvoid.nifty.internal.math.Vec4;

/**
 * This class handles all of the input events for a single node.
 * @author void
 */
public class InternalNiftyNodeInputHandler {
  private final Logger logger = Logger.getLogger(InternalNiftyNodeInputHandler.class.getName());
  private boolean mouseOverNode = false;

  public void pointerEvent(
      final InternalNiftyEventBus eventBus,
      final InternalNiftyNode internalNiftyNode,
      final NiftyPointerEvent pointerEvent) {
    if (eventBus == null) {
      return;
    }

    if (isInside(internalNiftyNode, pointerEvent.getX(), pointerEvent.getY())) {
      inside(eventBus, internalNiftyNode, pointerEvent);
    } else {
      outside(eventBus, internalNiftyNode);
    }
  }

  private void inside(
      final InternalNiftyEventBus eventBus,
      final InternalNiftyNode internalNiftyNode,
      final NiftyPointerEvent pointerEvent) {
    logInside(internalNiftyNode, pointerEvent);

    if (!mouseOverNode) {
      mouseOverNode = true;
      eventBus.publish(new NiftyMouseEnterNodeEvent(internalNiftyNode.getNiftyNode()));
    }

    eventBus.publish(new NiftyMouseHoverEvent(internalNiftyNode.getNiftyNode()));
  }

  private void outside(final InternalNiftyEventBus eventBus, final InternalNiftyNode internalNiftyNode) {
    if (!mouseOverNode) {
      return;
    }
    mouseOverNode = false;
    eventBus.publish(new NiftyMouseExitNodeEvent(internalNiftyNode.getNiftyNode()));
  }

  private boolean isInside(final InternalNiftyNode internalNiftyNode, final int x, final int y) {
    Vec4 t = internalNiftyNode.screenToLocal(x, y);
    return (
        t.x >= 0 &&
        t.x <= internalNiftyNode.getWidth() &&
        t.y >= 0 &&
        t.y <= internalNiftyNode.getHeight());
  }

  private void logInside(final InternalNiftyNode internalNiftyNode, final NiftyPointerEvent pointerEvent) {
    if (logger.isLoggable(Level.FINE)) {
      logger.fine("HOVER [" + internalNiftyNode.getId() + "] (" + pointerEvent + ")");
    }
  }
}
