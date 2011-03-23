package de.lessvoid.nifty.elements;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyMethodInvoker;
import de.lessvoid.nifty.input.NiftyMouseInputEvent;

/**
 * All ElementInteraction is handled in here.
 * @author void
 */
public class ElementInteraction {
  private String onClickAlternateKey;
  private NiftyMethodInvoker onMouseOverMethod;
  private NiftyMethodInvoker onMouseWheelMethod;

  private ElementInteractionClickHandler primary;
  private ElementInteractionClickHandler secondary;
  private ElementInteractionClickHandler tertiary;
  private ElementInteractionMoveHandler move;

  public ElementInteraction(final Nifty niftyParam, final Element element) {
    primary = new ElementInteractionClickHandler(niftyParam, element, new PrimaryClickMouseMethods(element));
    secondary = new ElementInteractionClickHandler(niftyParam, element, new SecondaryClickMouseMethods(element));
    tertiary = new ElementInteractionClickHandler(niftyParam, element, new TertiaryClickMouseMethods(element));
    move = new ElementInteractionMoveHandler(niftyParam, element);
  }

  public ElementInteractionClickHandler getPrimary() {
    return primary;
  }

  public ElementInteractionClickHandler getSecondary() {
    return secondary;
  }

  public ElementInteractionClickHandler getTertiary() {
    return tertiary;
  }

  public void setOnMouseOver(final NiftyMethodInvoker method) {
    onMouseOverMethod = method;
  }

  public void setOnMouseWheelMethod(final NiftyMethodInvoker method) {
    onMouseWheelMethod = method;
  }

  public void setFirstMethod(Object first) {
    primary.setFirst(first);
  }

  public boolean onMouseOver(final Element element, final NiftyMouseInputEvent inputEvent) {
    if (onMouseOverMethod != null) {
      onMouseOverMethod.invoke(element, inputEvent);
    }
    return false;
  }

  public boolean onMouseWheel(final Element element, final NiftyMouseInputEvent inputEvent) {
    if (onMouseWheelMethod != null) {
      onMouseWheelMethod.invoke(element, inputEvent);
    }
    return false;
  }

  public void setAlternateKey(final String newAlternateKey) {
    onClickAlternateKey = newAlternateKey;
  }

  public boolean process(final NiftyMouseInputEvent mouseEvent, final long eventTime, final boolean mouseInside, final boolean canHandleInteraction) {
    move.process(canHandleInteraction, mouseInside, mouseEvent);
    return
      primary.process(mouseEvent, mouseEvent.isButton0Down(), mouseEvent.isButton0InitialDown(), eventTime, mouseInside, canHandleInteraction, onClickAlternateKey) ||
      secondary.process(mouseEvent, mouseEvent.isButton1Down(), mouseEvent.isButton1InitialDown(), eventTime, mouseInside, canHandleInteraction, onClickAlternateKey) ||
      tertiary.process(mouseEvent, mouseEvent.isButton2Down(), mouseEvent.isButton2InitialDown(), eventTime, mouseInside, canHandleInteraction, onClickAlternateKey);
  }

  public void activate(final Nifty nifty) {
    primary.activate(nifty);
  }
}
