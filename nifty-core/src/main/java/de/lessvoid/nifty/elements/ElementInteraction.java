package de.lessvoid.nifty.elements;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyMethodInvoker;
import de.lessvoid.nifty.input.NiftyMouseInputEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * All ElementInteraction is handled in here.
 *
 * @author void
 */
public class ElementInteraction {
  private String onClickAlternateKey;
  @Nullable
  private NiftyMethodInvoker onMouseOverMethod;
  private NiftyMethodInvoker onMouseWheelMethod;

  @Nonnull
  private final ElementInteractionClickHandler primary;
  @Nonnull
  private final ElementInteractionClickHandler secondary;
  @Nonnull
  private final ElementInteractionClickHandler tertiary;
  @Nonnull
  private final ElementInteractionMoveHandler move;

  public ElementInteraction(@Nonnull final Nifty niftyParam, @Nonnull final Element element) {
    primary = new ElementInteractionClickHandler(niftyParam, element, new PrimaryClickMouseMethods(element));
    secondary = new ElementInteractionClickHandler(niftyParam, element, new SecondaryClickMouseMethods(element));
    tertiary = new ElementInteractionClickHandler(niftyParam, element, new TertiaryClickMouseMethods(element));
    move = new ElementInteractionMoveHandler(niftyParam, element);
  }

  public void resetMouseDown() {
    primary.resetMouseDown();
    secondary.resetMouseDown();
    tertiary.resetMouseDown();
  }

  @Nonnull
  public ElementInteractionClickHandler getPrimary() {
    return primary;
  }

  @Nonnull
  public ElementInteractionClickHandler getSecondary() {
    return secondary;
  }

  @Nonnull
  public ElementInteractionClickHandler getTertiary() {
    return tertiary;
  }

  public void setOnMouseOver(@Nullable final NiftyMethodInvoker method) {
    onMouseOverMethod = method;
  }

  public void setOnMouseWheelMethod(final NiftyMethodInvoker method) {
    onMouseWheelMethod = method;
  }

  public boolean onMouseOver(final Element element, final NiftyMouseInputEvent inputEvent) {
    if (onMouseOverMethod != null) {
      return onMouseOverMethod.invoke(element, inputEvent);
    }
    return false;
  }

  public boolean onMouseWheel(final Element element, final NiftyMouseInputEvent inputEvent) {
    if (onMouseWheelMethod != null) {
      return onMouseWheelMethod.invoke(element, inputEvent);
    }
    return false;
  }

  public void setAlternateKey(final String newAlternateKey) {
    onClickAlternateKey = newAlternateKey;
  }

  public boolean process(
      @Nonnull final NiftyMouseInputEvent mouseEvent,
      final long eventTime,
      final boolean mouseInside,
      final boolean canHandleInteraction,
      final boolean hasMouseAccess) {
    final boolean moveResult = move.process(canHandleInteraction, mouseInside, hasMouseAccess, mouseEvent);
    final boolean clickResult =
        primary.process(mouseEvent, mouseEvent.isButton0Down(), mouseEvent.isButton0InitialDown(),
            mouseEvent.isButton0Release(), eventTime, mouseInside, canHandleInteraction, hasMouseAccess,
            onClickAlternateKey) ||
            secondary.process(mouseEvent, mouseEvent.isButton1Down(), mouseEvent.isButton1InitialDown(),
                mouseEvent.isButton1Release(), eventTime, mouseInside, canHandleInteraction, hasMouseAccess,
                onClickAlternateKey) ||
            tertiary.process(mouseEvent, mouseEvent.isButton2Down(), mouseEvent.isButton2InitialDown(),
                mouseEvent.isButton2Release(), eventTime, mouseInside, canHandleInteraction, hasMouseAccess,
                onClickAlternateKey);
    return moveResult || clickResult;
  }

  public void activate(@Nonnull final Nifty nifty) {
    primary.activate(nifty);
  }
}
