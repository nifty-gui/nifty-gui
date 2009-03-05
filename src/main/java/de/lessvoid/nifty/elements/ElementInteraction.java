package de.lessvoid.nifty.elements;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.input.mouse.MouseInputEvent;
import de.lessvoid.xml.tools.MethodInvoker;

/**
 * All ElementInteraction is handled in here.
 * @author void
 */
public class ElementInteraction {

  private Nifty nifty;

  private MethodInvoker onClickMethod = new MethodInvoker();
  private boolean onClickRepeat;
  private String onClickAlternateKey;
  private MethodInvoker onReleaseMethod = new MethodInvoker();
  private MethodInvoker onClickMouseMoveMethod = new MethodInvoker();
  private MethodInvoker onMouseOverMethod = new MethodInvoker();

  /**
   * Create the ElemenInteraction.
   * @param niftyParam nifty
   */
  public ElementInteraction(final Nifty niftyParam) {
    nifty = niftyParam;
    onClickAlternateKey = null;
  }

  /**
   * Copy constructor.
   * @param source source to copy from
   */
  public ElementInteraction(final ElementInteraction source) {
    this.nifty = source.nifty;
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
    if (onClickMethod != null) {
      if (nifty != null) {
        nifty.setAlternateKey(onClickAlternateKey);
      }
      if (onClickMethod.invoke(inputEvent.getMouseX(), inputEvent.getMouseY()) != null) {
        return true;
      }
    }
    return false;
  }

  public void onClick() {
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
      Object result = onMouseOverMethod.invoke(element, inputEvent);
      if (result == null) {
        return false;
      } else {
        if (result instanceof Boolean) {
          return (Boolean) result;
        } else {
          return false;
        }
      }
    } else {
      return false;
    }
  }

  /**
   * Set onClickMethod.
   * @param methodInvokerParam MethodInvoker
   * @param useRepeatParam use repeat
   */
  public void setOnClickMethod(final MethodInvoker methodInvokerParam, final boolean useRepeatParam) {
    onClickMethod = methodInvokerParam;
    onClickRepeat = useRepeatParam;
  }

  public void setOnReleaseMethod(final MethodInvoker onRelease) {
    onReleaseMethod = onRelease;
  }

  /**
   * Set onClickMouseMoved.
   * @param methodInvoker MethodInvoker
   */
  public void setOnClickMouseMoved(final MethodInvoker methodInvoker) {
    onClickMouseMoveMethod = methodInvoker;
  }

  /**
   * Set onMouseOver.
   * @param methodInvoker MethodInvoker
   */
  public void setOnMouseOver(final MethodInvoker methodInvoker) {
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
  public MethodInvoker getOnClickMethod() {
    return onClickMethod;
  }

  /**
   * Get onClickMouseMovedMethod.
   * @return on click mouse moved
   */
  public MethodInvoker getOnClickMouseMoveMethod() {
    return onClickMouseMoveMethod;
  }

  public MethodInvoker getOnReleaseMethod() {
    return onReleaseMethod;
  }
}
