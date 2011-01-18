package de.lessvoid.nifty.elements;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyMethodInvoker;
import de.lessvoid.nifty.input.NiftyMouseClickedEvent;
import de.lessvoid.nifty.input.mouse.MouseInputEvent;

/**
 * All ElementInteraction is handled in here.
 * @author void
 */
public class ElementInteraction {
  private Nifty nifty;
  private String elementId;
  private boolean onClickRepeat;
  private String onClickAlternateKey;
  private NiftyMethodInvoker onClickMethod;
  private NiftyMethodInvoker onReleaseMethod;
  private NiftyMethodInvoker onClickMouseMoveMethod;
  private NiftyMethodInvoker onMouseOverMethod;

  /**
   * Create the ElemenInteraction.
   * @param niftyParam nifty
   * @param elementId 
   */
  public ElementInteraction(final Nifty niftyParam, final String elementId) {
    onClickMethod = new NiftyMethodInvoker(niftyParam);
    onReleaseMethod = new NiftyMethodInvoker(niftyParam);
    onClickMouseMoveMethod = new NiftyMethodInvoker(niftyParam);
    onMouseOverMethod = new NiftyMethodInvoker(niftyParam);

    nifty = niftyParam;
    onClickAlternateKey = null;
    this.elementId = elementId;
  }

  /**
   * Copy constructor.
   * @param source source to copy from
   */
  public ElementInteraction(final ElementInteraction source) {
    this.nifty = source.nifty;
    this.elementId = source.elementId;
    this.onClickMethod = source.onClickMethod;
    this.onReleaseMethod = source.onReleaseMethod;
    this.onClickMouseMoveMethod = source.onClickMouseMoveMethod;
    this.onClickRepeat = source.onClickRepeat;
    this.onClickAlternateKey = source.onClickAlternateKey;
    this.onMouseOverMethod = source.onMouseOverMethod;
  }

  /**
   * on click.
   * @param inputEvent mouse input
   */
  public boolean onClick(final MouseInputEvent inputEvent) {
    nifty.publishEvent(elementId, new NiftyMouseClickedEvent());

    if (onClickMethod != null) {
      if (nifty != null) {
        nifty.setAlternateKey(onClickAlternateKey);
      }
      return onClickMethod.invoke(inputEvent.getMouseX(), inputEvent.getMouseY());
    }

    return false;
  }

  public void onClick() {
    nifty.publishEvent(elementId, new NiftyMouseClickedEvent());

    if (onClickMethod != null) {
      onClickMethod.invoke();
    }
  }

  public void onRelease() {
    if (onReleaseMethod != null) {
      onReleaseMethod.invoke();
    }
  }

  /**
   * OnClick while mouse is moved.
   * @param inputEvent InputEvent
   */
  public void onClickMouseMoved(final MouseInputEvent inputEvent) {
    if (onClickMouseMoveMethod != null) {
      onClickMouseMoveMethod.invoke(inputEvent.getMouseX(), inputEvent.getMouseY());
    }
  }

  /**
   * Return onClickRepeat.
   * @return onClickRepeat
   */
  public boolean isOnClickRepeat() {
    return onClickRepeat;
  }

  /**
   * Mouse Over.
   * @param element Element
   * @param inputEvent mouse event
   * @return true (event eaten) false (forward event to children)
   */
  public boolean onMouseOver(final Element element, final MouseInputEvent inputEvent) {
    if (onMouseOverMethod != null) {
      onMouseOverMethod.invoke(element, inputEvent);
    }
    return false;
  }

  /**
   * Set onClickMethod.
   * @param methodInvokerParam MethodInvoker
   * @param useRepeatParam use repeat
   */
  public void setOnClickMethod(final NiftyMethodInvoker methodInvokerParam, final boolean useRepeatParam) {
    onClickMethod = methodInvokerParam;
    onClickRepeat = useRepeatParam;
  }

  public void setOnReleaseMethod(final NiftyMethodInvoker onRelease) {
    onReleaseMethod = onRelease;
  }

  /**
   * Set onClickMouseMoved.
   * @param methodInvoker MethodInvoker
   */
  public void setOnClickMouseMoved(final NiftyMethodInvoker methodInvoker) {
    onClickMouseMoveMethod = methodInvoker;
  }

  /**
   * Set onMouseOver.
   * @param methodInvoker MethodInvoker
   */
  public void setOnMouseOver(final NiftyMethodInvoker methodInvoker) {
    onMouseOverMethod = methodInvoker;
  }

  /**
   * Set alternate key.
   * @param newAlternateKey new alternate key
   */
  public void setAlternateKey(final String newAlternateKey) {
    onClickAlternateKey = newAlternateKey;
  }

  /**
   * Get onClickMethod.
   * @return on click method
   */
  public NiftyMethodInvoker getOnClickMethod() {
    return onClickMethod;
  }

  /**
   * Get onClickMouseMovedMethod.
   * @return on click mouse moved
   */
  public NiftyMethodInvoker getOnClickMouseMoveMethod() {
    return onClickMouseMoveMethod;
  }

  public NiftyMethodInvoker getOnReleaseMethod() {
    return onReleaseMethod;
  }
}
