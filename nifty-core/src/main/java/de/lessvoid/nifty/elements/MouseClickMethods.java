package de.lessvoid.nifty.elements;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyMethodInvoker;
import de.lessvoid.nifty.input.NiftyMouseInputEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class MouseClickMethods {
  @Nullable
  private NiftyMethodInvoker onClickMethod;
  @Nullable
  private NiftyMethodInvoker onClickMouseMoveMethod;
  @Nullable
  private NiftyMethodInvoker onReleaseMethod;
  @Nonnull
  protected final Element element;
  @Nullable
  private NiftyMethodInvoker onMultClickMethod;

  public MouseClickMethods(@Nonnull final Element element) {
    this.element = element;
  }

  public void setMultiClickMethod(@Nullable NiftyMethodInvoker onMultiClickMethod){
      this.onMultClickMethod = onMultiClickMethod;
  }
  public void setOnClickMethod(@Nullable NiftyMethodInvoker onClickMethod) {
    this.onClickMethod = onClickMethod;
  }

  public void setOnClickMouseMoveMethod(@Nullable NiftyMethodInvoker onClickMouseMoveMethod) {
    this.onClickMouseMoveMethod = onClickMouseMoveMethod;
  }

  public void setOnReleaseMethod(@Nullable NiftyMethodInvoker onReleaseMethod) {
    this.onReleaseMethod = onReleaseMethod;
  }

  public void onInitialClick() {
  }

  public boolean onClick(
      @Nonnull final Nifty nifty,
      @Nullable final String onClickAlternateKey,
      @Nonnull final NiftyMouseInputEvent inputEvent) {
    if (onClickMethod != null) {
      nifty.setAlternateKey(onClickAlternateKey);
      return onClickMethod.invoke(inputEvent.getMouseX(), inputEvent.getMouseY());
    }
    return false;
  }
  
  public boolean onMultiClick(
      @Nonnull final Nifty nifty,
      @Nullable final String onClickAlternateKey,
      @Nonnull final NiftyMouseInputEvent inputEvent,
      int clickCount) {
    if (onMultClickMethod != null) {
      nifty.setAlternateKey(onClickAlternateKey);
      return onMultClickMethod.invoke(inputEvent.getMouseX(), inputEvent.getMouseY(),clickCount);
    }
    return false;
  }
  
  public void onActivate(@Nonnull final Nifty nifty) {
    if (onClickMethod != null) {
      onClickMethod.invoke();
    }
  }

  public boolean onClickMouseMove(@Nonnull final Nifty nifty, @Nonnull final NiftyMouseInputEvent inputEvent) {
    if (onClickMouseMoveMethod != null) {
      return onClickMouseMoveMethod.invoke(inputEvent.getMouseX(), inputEvent.getMouseY());
    }
    return false;
  }

  public boolean onMouseRelease(@Nonnull final Nifty nifty, @Nonnull final NiftyMouseInputEvent mouseEvent) {
    if (onReleaseMethod != null) {
      return onReleaseMethod.invoke();
    }
    return false;
  }
}
