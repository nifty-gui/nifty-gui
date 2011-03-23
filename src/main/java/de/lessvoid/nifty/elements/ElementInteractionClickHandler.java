package de.lessvoid.nifty.elements;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyMethodInvoker;
import de.lessvoid.nifty.input.NiftyMouseInputEvent;

public class ElementInteractionClickHandler {
  private static final long REPEATED_CLICK_START_TIME = 100;
  private static final long REPEATED_CLICK_TIME = 100;

  private Nifty nifty;
  private Element element;
  private MouseClickMethods mouseMethods;
  private boolean isMouseDown;
  private boolean onClickRepeatEnabled;
  private long mouseDownTime;
  private long lastRepeatStartTime;
  private int lastMouseX;
  private int lastMouseY;

  public ElementInteractionClickHandler(final Nifty nifty, final Element element, final MouseClickMethods mouseMethods) {
    this.nifty = nifty;
    this.element = element;
    this.mouseMethods = mouseMethods;
    setMouseDown(false, 0);
  }

  public MouseClickMethods getMouseMethods() {
    return mouseMethods;
  }

  public void setOnClickRepeatEnabled(final boolean onClickRepeatEnabled) {
    this.onClickRepeatEnabled = onClickRepeatEnabled;
  }

  public boolean isOnClickRepeatEnabled() {
    return onClickRepeatEnabled;
  }

  public boolean process(
      final NiftyMouseInputEvent mouseEvent,
      final boolean isButtonDown,
      final boolean isInitialButtonDown,
      final long eventTime,
      final boolean mouseInside,
      final boolean canHandleInteraction,
      final String onClickAlternateKey) {
    if (onClickRepeatEnabled) {
      if (mouseInside && isMouseDown && isButtonDown) {
        long deltaTime = eventTime - mouseDownTime;
        if (deltaTime > REPEATED_CLICK_START_TIME) {
          long pastTime = deltaTime - REPEATED_CLICK_START_TIME;
          long repeatTime = pastTime - lastRepeatStartTime;
          if (repeatTime > REPEATED_CLICK_TIME) {
            lastRepeatStartTime = pastTime;
            if (onClickMouse(element.getId(), mouseEvent, canHandleInteraction, onClickAlternateKey)) {
              return true;
            }
          }
        }
      }
    }
    if (mouseInside && !isMouseDown) {
      if (isButtonDown && isInitialButtonDown) {
        setMouseDown(true, eventTime);
        onInitialClick();
        return onClickMouse(element.getId(), mouseEvent, canHandleInteraction, onClickAlternateKey);
      }
    } else if (!isButtonDown && isMouseDown) {
      setMouseDown(false, eventTime);
      onMouseRelease(mouseEvent);
      if (mouseInside) {
        onMouseReleaseInside(mouseEvent);
      }
    }
    if (isMouseDown) {
      onClickMouseMove(mouseEvent);
    }
    return false;
  }

  private void setMouseDown(final boolean newMouseDown, final long eventTime) {
    this.mouseDownTime = eventTime;
    this.lastRepeatStartTime = 0;
    this.isMouseDown = newMouseDown;
  }

  private void onInitialClick() {
    mouseMethods.onInitialClick();
  }

  private boolean onClickMouse(final String elementId, final NiftyMouseInputEvent inputEvent, final boolean canHandleInteraction, final String onClickAlternateKey) {
    if (canHandleInteraction) {
      lastMouseX = inputEvent.getMouseX();
      lastMouseY = inputEvent.getMouseY();

      return mouseMethods.onClick(nifty, onClickAlternateKey, inputEvent);
    }
    return false;
  }

  private void onClickMouseMove(final NiftyMouseInputEvent inputEvent) {
    if (lastMouseX == inputEvent.getMouseX() &&
        lastMouseY == inputEvent.getMouseY()) {
      return;
    }

    lastMouseX = inputEvent.getMouseX();
    lastMouseY = inputEvent.getMouseY();

    mouseMethods.onClickMouseMove(nifty, inputEvent);
  }

  private void onMouseRelease(final NiftyMouseInputEvent mouseEvent) {
    mouseMethods.onMouseRelease(nifty, mouseEvent);
  }

  private void onMouseReleaseInside(final NiftyMouseInputEvent mouseEvent) {
    mouseMethods.onMouseReleaseInside(mouseEvent);
  }

  public void setFirst(final Object first) {
    mouseMethods.setFirst(first);
  }

  public void activate(final Nifty nifty) {
    mouseMethods.onActivate(nifty);
  }

  public void setOnClickMethod(final NiftyMethodInvoker onClickMethod) {
    mouseMethods.setOnClickMethod(onClickMethod);
  }

  public void setOnClickMouseMoveMethod(final NiftyMethodInvoker onClickMouseMoveMethod) {
    mouseMethods.setOnClickMouseMoveMethod(onClickMouseMoveMethod);
  }

  public void setOnReleaseMethod(final NiftyMethodInvoker onReleaseMethod) {
    mouseMethods.setOnReleaseMethod(onReleaseMethod);
  }
}
