package de.lessvoid.nifty.elements;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyMethodInvoker;
import de.lessvoid.nifty.input.NiftyMouseInputEvent;

public abstract class MouseClickMethods {
  private NiftyMethodInvoker onClickMethod;
  private NiftyMethodInvoker onClickMouseMoveMethod;
  private NiftyMethodInvoker onReleaseMethod;
  protected Element element;

  public MouseClickMethods(final Element element) {
    this.element = element;
  }

  public void setOnClickMethod(NiftyMethodInvoker onClickMethod) {
    this.onClickMethod = onClickMethod;
  }

  public void setOnClickMouseMoveMethod(NiftyMethodInvoker onClickMouseMoveMethod) {
    this.onClickMouseMoveMethod = onClickMouseMoveMethod;
  }

  public void setOnReleaseMethod(NiftyMethodInvoker onReleaseMethod) {
    this.onReleaseMethod = onReleaseMethod;
  }

  public void onInitialClick() {
  }

  public boolean onClick(final Nifty nifty, final String onClickAlternateKey, final NiftyMouseInputEvent inputEvent) {
    if (onClickMethod != null) {
      if (nifty != null) {
        nifty.setAlternateKey(onClickAlternateKey);
      }
      return onClickMethod.invoke(inputEvent.getMouseX(), inputEvent.getMouseY());
      }
    return false;
  }

  public void onActivate(final Nifty nifty) {
    if (onClickMethod != null) {
      onClickMethod.invoke();
    }
  }

  public boolean onClickMouseMove(final Nifty nifty, final NiftyMouseInputEvent inputEvent) {
    if (onClickMouseMoveMethod != null) {
      return onClickMouseMoveMethod.invoke(inputEvent.getMouseX(), inputEvent.getMouseY());
    }
    return false;
  }

  public boolean onMouseRelease(final Nifty nifty, final NiftyMouseInputEvent mouseEvent) {
    if (onReleaseMethod != null) {
      return onReleaseMethod.invoke();
    }
    return false;
  }
}
