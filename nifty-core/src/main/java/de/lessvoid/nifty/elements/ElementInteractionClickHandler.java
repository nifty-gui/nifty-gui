package de.lessvoid.nifty.elements;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyMethodInvoker;
import de.lessvoid.nifty.input.NiftyMouseInputEvent;
import java.util.Properties;

import javax.annotation.Nonnull;

public class ElementInteractionClickHandler {
  private static final long REPEATED_CLICK_START_TIME = 100;
  private static final long REPEATED_CLICK_TIME = 100;
  private static final int CLICK_COUNT_RECORD_TIME = 500;

  private final Nifty nifty;
  private final Element element;
  private final MouseClickMethods mouseMethods;
  private boolean isMouseDown;
  private boolean onClickRepeatEnabled;
  private long mouseDownTime;
  private long lastRepeatStartTime;
  private int lastMouseX;
  private int lastMouseY;
  private int deltaTime;
  private long lastClickTime;
  private int clickCounter;

  public ElementInteractionClickHandler(
      final Nifty nifty,
      final Element element,
      final MouseClickMethods mouseMethods) {
    this.nifty = nifty;
    this.element = element;
    this.mouseMethods = mouseMethods;
    this.clickCounter = 1;
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
      @Nonnull final NiftyMouseInputEvent mouseEvent,
      final boolean isButtonDown,
      final boolean isInitialButtonDown,
      final boolean isButtonRelease,
      final long eventTime,
      final boolean mouseInside,
      final boolean canHandleInteraction,
      final boolean hasMouseAccess,
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
    boolean processed = false;
    if (mouseInside && !isMouseDown) {
        if (isButtonDown && isInitialButtonDown) {
            this.deltaTime += eventTime - this.lastClickTime;
            setMouseDown(true, eventTime);
            if ( this.deltaTime > this.calculateThreshold()) {
                this.lastClickTime = eventTime;
                this.deltaTime = 0;
                this.clickCounter = 1;
                onInitialClick();
                onClickMouse(element.getId(), mouseEvent, canHandleInteraction, onClickAlternateKey);
            } else {
                this.clickCounter++;
                onMultiClickMouse(element.getId(), mouseEvent, canHandleInteraction, onClickAlternateKey);
            }
            processed = true;
        }
    } else if (!isButtonDown && isMouseDown) {
      setMouseDown(false, eventTime);
    }
    if (isButtonRelease) {
      if (mouseInside || hasMouseAccess) {
        onMouseRelease(mouseEvent);
        processed = true;
      }
    }
    if (isMouseDown) {
      onClickMouseMove(mouseEvent);
      processed = true;
    }
    return processed;
  }

  private void setMouseDown(final boolean newMouseDown, final long eventTime) {
    this.mouseDownTime = eventTime;
    this.lastRepeatStartTime = 0;
    this.isMouseDown = newMouseDown;
  }

  public void resetMouseDown() {
    this.isMouseDown = false;
  }

  private void onInitialClick() {
    mouseMethods.onInitialClick();
  }

  private boolean onClickMouse(
      final String elementId,
      @Nonnull final NiftyMouseInputEvent inputEvent,
      final boolean canHandleInteraction,
      final String onClickAlternateKey) {
    if (canHandleInteraction) {
      lastMouseX = inputEvent.getMouseX();
      lastMouseY = inputEvent.getMouseY();

      return mouseMethods.onClick(nifty, onClickAlternateKey, inputEvent);
    }
    return false;
  }

  private boolean onClickMouseMove(@Nonnull final NiftyMouseInputEvent inputEvent) {
    if (lastMouseX == inputEvent.getMouseX() &&
        lastMouseY == inputEvent.getMouseY()) {
      return false;
    }

    lastMouseX = inputEvent.getMouseX();
    lastMouseY = inputEvent.getMouseY();

    return mouseMethods.onClickMouseMove(nifty, inputEvent);
  }
  
   private boolean onMultiClickMouse(String id, NiftyMouseInputEvent inputEvent, boolean canHandleInteraction, String onClickAlternateKey) {
       if (canHandleInteraction) {
      lastMouseX = inputEvent.getMouseX();
      lastMouseY = inputEvent.getMouseY();

      return mouseMethods.onMultiClick(nifty, onClickAlternateKey, inputEvent, clickCounter);
    }
    return false;
   }
   
  private boolean onMouseRelease(@Nonnull final NiftyMouseInputEvent mouseEvent) {
    return mouseMethods.onMouseRelease(nifty, mouseEvent);
  }

  public void activate(@Nonnull final Nifty nifty) {
    mouseMethods.onActivate(nifty);
  }

  public void setOnClickMethod(final NiftyMethodInvoker onClickMethod) {
    mouseMethods.setOnClickMethod(onClickMethod);
  }
  
   public void setOnMultiClickMethod(final NiftyMethodInvoker onMultiClickMethod) {
    mouseMethods.setMultiClickMethod(onMultiClickMethod);
   }

  public void setOnClickMouseMoveMethod(final NiftyMethodInvoker onClickMouseMoveMethod) {
    mouseMethods.setOnClickMouseMoveMethod(onClickMouseMoveMethod);
  }

  public void setOnReleaseMethod(final NiftyMethodInvoker onReleaseMethod) {
    mouseMethods.setOnReleaseMethod(onReleaseMethod);
  }
  /**
   * Take the threshold for multiclick calculation . This is useful if user
   * change the value runtime.
   * @return amount of milliseconds if MULTI_CLICK_TIME is set.
   */
  private int calculateThreshold(){
      int result = ElementInteractionClickHandler.CLICK_COUNT_RECORD_TIME;
      Properties globalProperties = this.nifty.getGlobalProperties();
      if (globalProperties != null) {
          String threshold = globalProperties.getProperty("MULTI_CLICK_TIME");
          try {
              result = Integer.parseInt(threshold);
          } catch (NumberFormatException e) {
          }
      }
      return result;
  }

}
