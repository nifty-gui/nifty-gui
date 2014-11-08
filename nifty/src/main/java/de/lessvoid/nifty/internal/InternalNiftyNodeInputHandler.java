/*
 * Copyright (c) 2014, Jens Hohmuth 
 * All rights reserved. 
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are 
 * met: 
 * 
 *  * Redistributions of source code must retain the above copyright 
 *    notice, this list of conditions and the following disclaimer. 
 *  * Redistributions in binary form must reproduce the above copyright 
 *    notice, this list of conditions and the following disclaimer in the 
 *    documentation and/or other materials provided with the distribution. 
 * 
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND 
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR 
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF 
 * THE POSSIBILITY OF SUCH DAMAGE.
 */
package de.lessvoid.nifty.internal;

import java.util.logging.Level;
import java.util.logging.Logger;

import de.lessvoid.nifty.api.event.NiftyPointerClickedEvent;
import de.lessvoid.nifty.api.event.NiftyPointerDraggedEvent;
import de.lessvoid.nifty.api.event.NiftyPointerEnterNodeEvent;
import de.lessvoid.nifty.api.event.NiftyPointerExitNodeEvent;
import de.lessvoid.nifty.api.event.NiftyPointerHoverEvent;
import de.lessvoid.nifty.api.event.NiftyPointerPressedEvent;
import de.lessvoid.nifty.api.event.NiftyPointerReleasedEvent;
import de.lessvoid.nifty.api.input.NiftyPointerEvent;
import de.lessvoid.nifty.internal.math.Vec4;

/**
 * This class handles all of the input events for a single node.
 * @author void
 */
public class InternalNiftyNodeInputHandler {
  private final Logger logger = Logger.getLogger(InternalNiftyNodeInputHandler.class.getName());
  private boolean mouseOverNode = false;
  private boolean buttonDown[] = new boolean[NiftyPointerEvent.BUTTON_COUNT];
  private int lastPosX;
  private int lastPosY;

  public boolean pointerEvent(
      final InternalNiftyEventBus eventBus,
      final InternalNiftyNode internalNiftyNode,
      final NiftyPointerEvent pointerEvent) {
    if (eventBus == null) {
      return false;
    }

    if (isInside(internalNiftyNode, pointerEvent.getX(), pointerEvent.getY())) {
      return inside(eventBus, internalNiftyNode, pointerEvent);
    } else {
      outside(eventBus, internalNiftyNode);
      return false;
    }
  }

  /**
   * This is called with pointer events when this node has captured pointer events.
   *
   * @param eventBus
   * @param internalNiftyNode
   * @param pointerEvent
   * @return true when this node will give up capturing and false if it still wants to receive captured events
   */
  public boolean capturedPointerEvent(
      final InternalNiftyEventBus eventBus,
      final InternalNiftyNode internalNiftyNode,
      final NiftyPointerEvent pointerEvent) {
    if (eventBus == null) {
      return false;
    }

    boolean wantsToGiveUpCapturing = false;
    eventBus.publish(new NiftyPointerHoverEvent(internalNiftyNode.getNiftyNode(), pointerEvent.getX(), pointerEvent.getY()));

    if (!pointerEvent.isButtonDown()) {
      if (pointerEvent.getButton() >= 0) {
        if (buttonDown[pointerEvent.getButton()]) {
          buttonDown[pointerEvent.getButton()] = false;
  
          eventBus.publish(new NiftyPointerReleasedEvent(internalNiftyNode.getNiftyNode(), pointerEvent.getButton(), pointerEvent.getX(), pointerEvent.getY()));
          if (isInside(internalNiftyNode, pointerEvent.getX(), pointerEvent.getY())) {
            eventBus.publish(new NiftyPointerClickedEvent(internalNiftyNode.getNiftyNode(), pointerEvent.getButton(), pointerEvent.getX(), pointerEvent.getY()));
          } else {
            if (mouseOverNode) {
              mouseOverNode = false;
              eventBus.publish(new NiftyPointerExitNodeEvent(internalNiftyNode.getNiftyNode()));
            }
          }
          wantsToGiveUpCapturing = true;
        }
      }
    }

    if (anyButtonDown() && (lastPosX != pointerEvent.getX() || lastPosY != pointerEvent.getY())) {
      eventBus.publish(new NiftyPointerDraggedEvent(internalNiftyNode.getNiftyNode(), pointerEvent.getButton(), pointerEvent.getX(), pointerEvent.getY()));
    }

    lastPosX = pointerEvent.getX();
    lastPosY = pointerEvent.getY();

    return wantsToGiveUpCapturing;
  }

  private boolean inside(
      final InternalNiftyEventBus eventBus,
      final InternalNiftyNode internalNiftyNode,
      final NiftyPointerEvent pointerEvent) {
    logInside(internalNiftyNode, pointerEvent);

    boolean wantsToCaptureEvents = false;
    if (!mouseOverNode) {
      mouseOverNode = true;
      eventBus.publish(new NiftyPointerEnterNodeEvent(internalNiftyNode.getNiftyNode()));
    }

    eventBus.publish(new NiftyPointerHoverEvent(internalNiftyNode.getNiftyNode(), pointerEvent.getX(), pointerEvent.getY()));

    if (pointerEvent.isButtonDown()) {
      if (!buttonDown[pointerEvent.getButton()]) {
        buttonDown[pointerEvent.getButton()] = true;
        eventBus.publish(new NiftyPointerPressedEvent(internalNiftyNode.getNiftyNode(), pointerEvent.getButton(), pointerEvent.getX(), pointerEvent.getY()));
        wantsToCaptureEvents = true;
      }
    } else {
      if (pointerEvent.getButton() >= 0) {
        if (buttonDown[pointerEvent.getButton()]) {
          buttonDown[pointerEvent.getButton()] = false;
  
          eventBus.publish(new NiftyPointerReleasedEvent(internalNiftyNode.getNiftyNode(), pointerEvent.getButton(), pointerEvent.getX(), pointerEvent.getY()));
          eventBus.publish(new NiftyPointerClickedEvent(internalNiftyNode.getNiftyNode(), pointerEvent.getButton(), pointerEvent.getX(), pointerEvent.getY()));
        }
      }
    }

    if (anyButtonDown() && (lastPosX != pointerEvent.getX() || lastPosY != pointerEvent.getY())) {
      eventBus.publish(new NiftyPointerDraggedEvent(internalNiftyNode.getNiftyNode(), pointerEvent.getButton(), pointerEvent.getX(), pointerEvent.getY()));
    }

    lastPosX = pointerEvent.getX();
    lastPosY = pointerEvent.getY();
    return wantsToCaptureEvents;
  }

  private boolean anyButtonDown() {
    for (int i=0; i<buttonDown.length; i++) {
      if (buttonDown[i]) {
        return true;
      }
    }
    return false;
  }

  private void outside(final InternalNiftyEventBus eventBus, final InternalNiftyNode internalNiftyNode) {
    if (!mouseOverNode) {
      return;
    }
    mouseOverNode = false;
    eventBus.publish(new NiftyPointerExitNodeEvent(internalNiftyNode.getNiftyNode()));
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
