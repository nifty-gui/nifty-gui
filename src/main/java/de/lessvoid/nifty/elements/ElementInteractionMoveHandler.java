package de.lessvoid.nifty.elements;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.events.NiftyMouseEvent;
import de.lessvoid.nifty.elements.events.NiftyMouseMovedEvent;
import de.lessvoid.nifty.elements.events.NiftyMouseWheelEvent;
import de.lessvoid.nifty.input.NiftyMouseInputEvent;

public class ElementInteractionMoveHandler {
  private Nifty nifty;
  private Element element;
  private int lastMouseX;
  private int lastMouseY;
  private boolean lastButton0Down;
  private boolean lastButton1Down;
  private boolean lastButton2Down;

  public ElementInteractionMoveHandler(final Nifty nifty, final Element element) {
    this.nifty = nifty;
    this.element = element;
    this.lastMouseX = 0;
    this.lastMouseY = 0;
    this.lastButton0Down = false;
    this.lastButton1Down = false;
    this.lastButton2Down = false;
  }

  public void process(final boolean canHandleInteraction, final boolean mouseInside, final boolean hasMouseAccess, final NiftyMouseInputEvent mouseEvent) {
    if (canHandleInteraction && mouseInside) {
      boolean moved = handleMoveEvent(mouseEvent);
      boolean wheel = handleWheelEvent(mouseEvent);
      if (moved || wheel) {
        handleGeneralEvent(mouseEvent);
      } else {
        boolean generateEvent = false;
        if (mouseEvent.isButton0Down() != lastButton0Down) {
          lastButton0Down = mouseEvent.isButton0Down();
          generateEvent = true;
        }
        if (mouseEvent.isButton1Down() != lastButton1Down) {
          lastButton1Down = mouseEvent.isButton1Down();
          generateEvent = true;
        }
        if (mouseEvent.isButton2Down() != lastButton2Down) {
          lastButton2Down = mouseEvent.isButton2Down();
          generateEvent = true;
        }
        if (generateEvent) {
          handleGeneralEvent(mouseEvent);
        }
      }
    }
  }

  private boolean handleMoveEvent(final NiftyMouseInputEvent mouseEvent) {
    if (mouseEvent.getMouseX() != lastMouseX ||
        mouseEvent.getMouseY() != lastMouseY) {
      lastMouseX = mouseEvent.getMouseX();
      lastMouseY = mouseEvent.getMouseY();
      nifty.publishEvent(element.getId(), new NiftyMouseMovedEvent(mouseEvent));
      return true;
    }
    return false;
  }

  private boolean handleWheelEvent(final NiftyMouseInputEvent mouseEvent) {
    if (mouseEvent.getMouseWheel() != 0) {
      nifty.publishEvent(element.getId(), new NiftyMouseWheelEvent(mouseEvent));
      return true;
    }
    return false;
  }

  private void handleGeneralEvent(final NiftyMouseInputEvent mouseEvent) {
    nifty.publishEvent(element.getId(), new NiftyMouseEvent(mouseEvent));
  }
}
