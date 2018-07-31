package de.lessvoid.nifty.elements;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyMethodInvoker;
import de.lessvoid.nifty.input.NiftyMouseInputEvent;

import javax.annotation.Nonnull;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ElementInteractionClickHandler {
  private static final Logger logger = Logger.getLogger(ElementInteractionClickHandler.class.getName());

  private static final long REPEATED_CLICK_START_TIME_MS = 100;
  private static final long REPEATED_CLICK_TIME_MS = 100;
  private static final int CLICK_COUNT_RECORD_TIME_MS = 500;

  private final Nifty nifty;
  private final Element element;
  private final MouseClickMethods mouseMethods;
  private boolean isMouseDown;
  private boolean onClickRepeatEnabled;
  private int lastMouseX;
  private int lastMouseY;
  private long deltaTimeMs;
  private long mouseDownTimeMs;
  private long lastClickTimeMs;
  private long lastRepeatStartTimeMs;
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
      final long eventTimeMs,
      final boolean mouseInside,
      final boolean canHandleInteraction,
      final boolean hasMouseAccess,
      final String onClickAlternateKey) {
    if (onClickRepeatEnabled) {
      if (mouseInside && isMouseDown && isButtonDown) {
        long deltaTimeMs = eventTimeMs - mouseDownTimeMs;
        if (deltaTimeMs > REPEATED_CLICK_START_TIME_MS) {
          long pastTimeMs = deltaTimeMs - REPEATED_CLICK_START_TIME_MS;
          long repeatTimeMs = pastTimeMs - lastRepeatStartTimeMs;
          if (repeatTimeMs > REPEATED_CLICK_TIME_MS) {
            lastRepeatStartTimeMs = pastTimeMs;
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
            deltaTimeMs += eventTimeMs - lastClickTimeMs;
            setMouseDown(true, eventTimeMs);
            if (deltaTimeMs > calculateMulticlickThresholdTimeMs()) {
                if (logger.isLoggable(Level.FINE)) {
                  logger.fine("eventTimeMs: " + eventTimeMs + ", "
                            + "lastClickTimeMs: " + lastClickTimeMs + ", "
                            + "deltaTimeMs: " + deltaTimeMs + " => INITIAL CLICK");
                }
                lastClickTimeMs = eventTimeMs;
                deltaTimeMs = 0;
                clickCounter = 1;
                onInitialClick();
                onClickMouse(element.getId(), mouseEvent, canHandleInteraction, onClickAlternateKey);
            } else {
                clickCounter++;
                if (logger.isLoggable(Level.FINE)) {
                  logger.fine("eventTimeMs: " + eventTimeMs + ", "
                            + "lastClickTimeMs: " + lastClickTimeMs + ", "
                            + "deltaTimeMs: " + deltaTimeMs + " => MULTI CLICK: " + clickCounter);
                }
                onMultiClickMouse(element.getId(), mouseEvent, canHandleInteraction, onClickAlternateKey);
            }
            processed = true;
        }
    } else if (!isButtonDown && isMouseDown) {
      setMouseDown(false, eventTimeMs);
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

  private void setMouseDown(final boolean newMouseDown, final long eventTimeMs) {
    this.mouseDownTimeMs = eventTimeMs;
    this.lastRepeatStartTimeMs = 0;
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
    if (!canHandleInteraction) {
      return false;
    }

    lastMouseX = inputEvent.getMouseX();
    lastMouseY = inputEvent.getMouseY();

    return mouseMethods.onClick(nifty, onClickAlternateKey, inputEvent);
  }

  private boolean onMultiClickMouse(
          final String elementId,
          @Nonnull final NiftyMouseInputEvent inputEvent,
          final boolean canHandleInteraction,
          final String onClickAlternateKey) {
    if (!canHandleInteraction) {
      return false;
    }

    lastMouseX = inputEvent.getMouseX();
    lastMouseY = inputEvent.getMouseY();

    return mouseMethods.onMultiClick(nifty, onClickAlternateKey, inputEvent, clickCounter);
  }

  private boolean onClickMouseMove(@Nonnull final NiftyMouseInputEvent inputEvent) {
    if (lastMouseX == inputEvent.getMouseX() && lastMouseY == inputEvent.getMouseY()) {
      return false;
    }

    lastMouseX = inputEvent.getMouseX();
    lastMouseY = inputEvent.getMouseY();

    return mouseMethods.onClickMouseMove(nifty, inputEvent);
  }

  private boolean onMouseRelease(@Nonnull final NiftyMouseInputEvent mouseEvent) {
    return mouseMethods.onRelease(nifty, mouseEvent);
  }

  public void clickAndReleaseMouse(@Nonnull final Nifty nifty) {
    element.startEffectWithoutChildren(EffectEventId.onClick); 
    mouseMethods.clickAndRelease(nifty);
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
   * Calculates the multiclick threshold, which is the amount of time in milliseconds between two consecutive mouse
   * clicks that determines whether or not they comprise a multiclick. Two consecutive clicks must be less than or
   * equal to the multiclick threshold to be considered a multiclick.
   *
   * @return Either: 1) the value of the global property MULTI_CLICK_TIME,
   * if it is set & the property is a valid {@link Integer},
   * otherwise 2) a default value specified by {@link #CLICK_COUNT_RECORD_TIME_MS}.
   */
  private int calculateMulticlickThresholdTimeMs() {
    Properties globalProperties = nifty.getGlobalProperties();

    if (globalProperties == null) {
      return ElementInteractionClickHandler.CLICK_COUNT_RECORD_TIME_MS;
    }

    String threshold = globalProperties.getProperty("MULTI_CLICK_TIME");

    try {
      return Integer.parseInt(threshold);
    } catch (NumberFormatException e) {
      logger.warning ("Invalid value for global property \"MULTI_CLICK_TIME\": " + threshold +
              " (ms). Falling back to default value of " + ElementInteractionClickHandler.CLICK_COUNT_RECORD_TIME_MS +
              " (ms).");
      return ElementInteractionClickHandler.CLICK_COUNT_RECORD_TIME_MS;
    }
  }
}
