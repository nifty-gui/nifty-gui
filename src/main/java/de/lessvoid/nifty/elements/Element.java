package de.lessvoid.nifty.elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.controls.FocusHandler;
import de.lessvoid.nifty.controls.NiftyInputControl;
import de.lessvoid.nifty.effects.Effect;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.effects.EffectManager;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.elements.render.ElementRenderer;
import de.lessvoid.nifty.input.keyboard.KeyboardInputEvent;
import de.lessvoid.nifty.input.mouse.MouseInputEvent;
import de.lessvoid.nifty.layout.LayoutPart;
import de.lessvoid.nifty.layout.align.HorizontalAlign;
import de.lessvoid.nifty.layout.align.VerticalAlign;
import de.lessvoid.nifty.layout.manager.LayoutManager;
import de.lessvoid.nifty.loaderv2.types.ElementType;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.render.RenderStateType;
import de.lessvoid.nifty.screen.KeyInputHandler;
import de.lessvoid.nifty.screen.MouseOverHandler;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.nifty.tools.TimeProvider;
import de.lessvoid.xml.tools.MethodInvoker;

/**
 * The Element.
 * @author void
 */
public class Element {

  /**
   * Time before we start an automated click when mouse button is holded.
   */
  private static final long REPEATED_CLICK_START_TIME = 100;

  /**
   * The time between automatic clicks.
   */
  private static final long REPEATED_CLICK_TIME = 100;

  /**
   * the logger.
   */
  private static Logger log = Logger.getLogger(Element.class.getName());

  /**
   * element type.
   */
  private ElementType elementType;

  /**
   * our identification.
   */
  private String id;

  /**
   * the parent element.
   */
  private Element parent;

  /**
   * the child elements.
   */
  private ArrayList < Element > elements = new ArrayList < Element >();

  /**
   * The LayoutManager we should use for all child elements.
   */
  private LayoutManager layoutManager;

  /**
   * The LayoutPart for layout this element.
   */
  private LayoutPart layoutPart;

  /**
   * The ElementRenderer we should use to render this element.
   */
  private ElementRenderer[] elementRenderer;

  /**
   * the effect manager for this element.
   */
  private EffectManager effectManager;

  /**
   * Element interaction.
   */
  private ElementInteraction interaction;

  /**
   * Nifty instance this element is attached to.
   */
  private Nifty nifty;

  /**
   * The focus handler this element is attached to.
   */
  private FocusHandler focusHandler;

  /**
   * enable element.
   */
  private boolean enabled;

  /**
   * visible element.
   */
  private boolean visible;

  /**
   * this is set to true, when there's no interaction with the element
   * possible. this happens when the onEndScreen effect starts.
   */
  private boolean done;

  /**
   * mouse down flag.
   */
  private boolean mouseDown;

  /**
   * visible to mouse events.
   */
  private boolean visibleToMouseEvents;

  /**
   * Last position of mouse X.
   */
  private int lastMouseX;

  /**
   * Last position of mouse Y.
   */
  private int lastMouseY;

  /**
   * mouse first click down time.
   */
  private long mouseDownTime;

  /**
   * Time the last repeat has been activated.
   */
  private long lastRepeatStartTime;

  /**
   * clip children.
   */
  private boolean clipChildren;

  /**
   * attached control when this element is an control.
   */
  private NiftyInputControl attachedInputControl = null;

  /**
   * remember if we've calculated this constraint ourself.
   */
  private boolean isCalcWidthConstraint;

  /**
   * remember if we've calculated this constraint ourself.
   */
  private boolean isCalcHeightConstraint;

  /**
   * focusable.
   */
  private boolean focusable = false;

  /**
   * screen we're connected to.
   */
  private Screen screen;

  /**
   * TimeProvider.
   */
  private TimeProvider time;

  private boolean parentClipArea = false;
  private int parentClipX;
  private int parentClipY;
  private int parentClipWidth;
  private int parentClipHeight;

  /**
   * construct new instance of Element.
   * @param newNifty Nifty
   * @param newElementType elementType
   * @param newId the id
   * @param newParent new parent
   * @param newFocusHandler the new focus handler
   * @param newVisibleToMouseEvents visible to mouse
   * @param newTimeProvider TimeProvider
   * @param newElementRenderer the element renderer
   */
  public Element(
      final Nifty newNifty,
      final ElementType newElementType,
      final String newId,
      final Element newParent,
      final FocusHandler newFocusHandler,
      final boolean newVisibleToMouseEvents,
      final TimeProvider newTimeProvider,
      final ElementRenderer ... newElementRenderer) {
    initialize(
        newNifty,
        newElementType,
        newId,
        newParent,
        newElementRenderer,
        new LayoutPart(),
        newFocusHandler,
        newVisibleToMouseEvents, newTimeProvider);
  }

  /**
   * construct new instance of Element using the given layoutPart instance.
   * @param newNifty Nifty
   * @param newElementType element type
   * @param newId the id
   * @param newParent new parent
   * @param newLayoutPart the layout part
   * @param newFocusHandler the new focus handler
   * @param newVisibleToMouseEvents visible to  mouse
   * @param newTimeProvider TimeProvider
   * @param newElementRenderer the element renderer
   */
  public Element(
      final Nifty newNifty,
      final ElementType newElementType,
      final String newId,
      final Element newParent,
      final LayoutPart newLayoutPart,
      final FocusHandler newFocusHandler,
      final boolean newVisibleToMouseEvents,
      final TimeProvider newTimeProvider, final ElementRenderer ... newElementRenderer) {
    initialize(
        newNifty,
        newElementType,
        newId,
        newParent,
        newElementRenderer,
        newLayoutPart,
        newFocusHandler,
        newVisibleToMouseEvents, newTimeProvider);
  }

  /**
   * initialize this instance helper.
   * @param newNifty Nifty
   * @param newElementType element
   * @param newId the id
   * @param newParent parent
   * @param newElementRenderer the element renderer to use
   * @param newLayoutPart the layoutPart to use
   * @param newFocusHandler the focus handler that this element is attached to
   * @param newVisibleToMouseEvents visible to mouse
   * @param timeProvider TimeProvider to use
   */
  private void initialize(
      final Nifty newNifty,
      final ElementType newElementType,
      final String newId,
      final Element newParent,
      final ElementRenderer[] newElementRenderer,
      final LayoutPart newLayoutPart,
      final FocusHandler newFocusHandler,
      final boolean newVisibleToMouseEvents,
      final TimeProvider timeProvider) {
    this.nifty = newNifty;
    this.elementType = newElementType;
    this.id = newId;
    this.parent = newParent;
    this.elementRenderer = newElementRenderer;
    this.effectManager = new EffectManager();
    this.layoutPart = newLayoutPart;
    this.enabled = true;
    this.visible = true;
    this.done = false;
    this.focusHandler = newFocusHandler;
    this.visibleToMouseEvents = newVisibleToMouseEvents;
    this.time = timeProvider;
    this.setMouseDown(false, 0);
    this.interaction = new ElementInteraction(nifty);
  }

  /**
   * get the id of this element.
   * @return the id
   */
  public String getId() {
    return id;
  }

  /**
   * get parent.
   * @return parent
   */
  public Element getParent() {
    return parent;
  }

  /**
   * get element state as string.
   * @param offset offset string
   * @return the element state as string.
   */
  public String getElementStateString(final String offset) {
    String pos =
      " style [" + getElementType().getAttributes().get("style") + "], state [" + getState() + "],"
      + " position [" + getX() + ", " + getY() + ", " + getWidth() + ", " + getHeight() + "], constraint [" + outputSizeValue(layoutPart.getBoxConstraints().getX())
      + ", " + outputSizeValue(layoutPart.getBoxConstraints().getY()) + ", "
      + outputSizeValue(layoutPart.getBoxConstraints().getWidth()) + ", "
      + outputSizeValue(layoutPart.getBoxConstraints().getHeight()) + "]"
      + " focusable [" + focusable + "] mouseable [" + visibleToMouseEvents + "]";
    return pos;
  }

  private String getState() {
    if (isEffectActive(EffectEventId.onStartScreen)) {
      return "starting";
    }

    if (isEffectActive(EffectEventId.onEndScreen)) {
      return "ending";
    }

    if (!visible) {
      return "hidden";
    }

    return "normal";
  }

  /**
   * Output SizeValue.
   * @param value SizeValue
   * @return value string
   */
  private String outputSizeValue(final SizeValue value) {
    if (value == null) {
      return "null";
    } else {
      return value.toString();
    }
  }
  /**
   * get x.
   * @return x position of this element.
   */
  public int getX() {
    return layoutPart.getBox().getX();
  }

  /**
   * get y.
   * @return the y position of this element.
   */
  public int getY() {
    return layoutPart.getBox().getY();
  }

  /**
   * get height.
   * @return the height of this element.
   */
  public int getHeight() {
    return layoutPart.getBox().getHeight();
  }

  /**
   * get width.
   * @return the width of this element.
   */
  public int getWidth() {
    return layoutPart.getBox().getWidth();
  }

  /**
   * get all child elements of this element.
   * @return the list of child elements
   */
  public List < Element > getElements() {
    return elements;
  }

  /**
   * add a child element.
   * @param widget the child to add
   */
  public void add(final Element widget) {
    elements.add(widget);
  }

  /**
   * render this element.
   * @param r the RenderDevice to use
   */
  public void render(final NiftyRenderEngine r) {
    // render element only when it is visible
    if (visible) {
      r.saveState(RenderStateType.allStates());

      // begin rendering / pre
      effectManager.begin(r, this);
      effectManager.renderPre(r, this);

      // render element
      if (elementRenderer != null) {
        for (ElementRenderer renderer : elementRenderer) {
          renderer.render(this, r);
        }
      }

      // finish rendering / post
      effectManager.renderPost(r, this);
      effectManager.end(r);

      if (clipChildren) {
        r.enableClip(getX(), getY(), getX() + getWidth(), getY() + getHeight());
      }

      // now render child elements
      for (int i = 0; i < elements.size(); i++) {
        Element p = elements.get(i);
        p.render(r);
      }

      if (clipChildren) {
        r.disableClip();
      }

      r.restoreState();

      r.saveState(RenderStateType.allStates());
      effectManager.renderOverlay(r, this);
      r.restoreState();
    }
  }

  /**
   * Set a new LayoutManager.
   * @param newLayout the new LayoutManager to use.
   */
  public void setLayoutManager(final LayoutManager newLayout) {
    this.layoutManager = newLayout;
  }

  /**
   * layout this element and all it's children.
   */
  public void layoutElements() {
    preProcessConstraintWidth();
    preProcessConstraintHeight();

    if (layoutManager != null) {
      // we need a list of LayoutPart and not of Element, so we'll build one on the fly here
      List < LayoutPart > layoutPartChild = new ArrayList < LayoutPart >();
      for (Element w : elements) {
        layoutPartChild.add(w.layoutPart);
      }

      // use out layoutManager to layout our children
      layoutManager.layoutElements(layoutPart, layoutPartChild);

      // repeat this step for all child elements
      for (Element w : elements) {
        w.layoutElements();
      }
    }

    if (clipChildren) {
      for (Element w : elements) {
        w.setParentClipArea(getX(), getY(), getWidth(), getHeight());
      }
    }
  }

  private void setParentClipArea(final int x, final int y, final int width, final int height) {
    parentClipArea = true;
    parentClipX = x;
    parentClipY = y;
    parentClipWidth = width;
    parentClipHeight = height;

    for (Element w : elements) {
      w.setParentClipArea(parentClipX, parentClipY, parentClipWidth, parentClipHeight);
    }
  }

  /**
   * pre-process constraint width.
   */
  private void preProcessConstraintWidth() {
    for (Element e : elements) {
      e.preProcessConstraintWidth();
    }

    // try the original width value first
    SizeValue myWidth = getConstraintWidth();

    // is it empty and we have an layoutManager there's still hope for a width constraint
    if (layoutManager != null && (myWidth == null || isCalcWidthConstraint)) {

      // collect all child layoutPart that have a fixed pixel size in a list
      List < LayoutPart > layoutPartChild = new ArrayList < LayoutPart >();
      for (Element e : elements) {
        SizeValue childWidth = e.getConstraintWidth();
        if (childWidth != null && childWidth.isPixel()) {
          layoutPartChild.add(e.layoutPart);
        }
      }

      // if all (!) child elements have a pixel fixed width we can calculate a new width constraint for this element!
      if (elements.size() == layoutPartChild.size()) {
        SizeValue newWidth = layoutManager.calculateConstraintWidth(layoutPartChild);
        if (newWidth != null) {
          // log.info("pre processed new width for element: " + getId() + ": " + newWidth.getValueAsInt(0));
          setConstraintWidth(newWidth);
          isCalcWidthConstraint = true;
        }
      }
    }
  }

  /**
   * pre process constraint height.
   */
  private void preProcessConstraintHeight() {
    for (Element e : elements) {
      e.preProcessConstraintHeight();
    }

    // try the original height value first
    SizeValue myHeight = getConstraintHeight();

    // is it empty and we have an layoutManager there's still hope for a height constraint
    if (layoutManager != null && (myHeight == null || isCalcHeightConstraint)) {

      // collect all child layoutPart that have a fixed px size in a list
      List < LayoutPart > layoutPartChild = new ArrayList < LayoutPart >();
      for (Element e : elements) {
        SizeValue childHeight = e.getConstraintHeight();
        if (childHeight != null && childHeight.isPixel()) {
          layoutPartChild.add(e.layoutPart);
        }
      }

      // if all (!) child elements have a px fixed height we can calculate a new height constraint for this element!
      if (elements.size() == layoutPartChild.size()) {
        SizeValue newHeight = layoutManager.calculateConstraintHeight(layoutPartChild);
        if (newHeight != null) {
          // log.info("pre processed new height for element: " + getId() + ": " + newHeight.getValueAsInt(0));
          setConstraintHeight(newHeight);
          isCalcHeightConstraint = true;
        }
      }
    }
  }

  /**
   * reset all effects.
   */
  public void resetEffects() {
    mouseDown = false;
    effectManager.reset();
    for (Element w : elements) {
      w.resetEffects();
    }
  }

  public void resetAllEffects() {
    mouseDown = false;
    effectManager.resetAll();
    for (Element w : elements) {
      w.resetAllEffects();
    }
  }

  public void resetSingleEffect(final EffectEventId effectEventId) {
    mouseDown = false;
    effectManager.resetSingleEffect(effectEventId);
    for (Element w : elements) {
      w.resetSingleEffect(effectEventId);
    }
  }

  public void resetMouseDown() {
    mouseDown = false;
    for (Element w : elements) {
      w.resetMouseDown();
    }
  }

  /**
   * set new x position constraint.
   * @param newX new x constraint.
   */
  public void setConstraintX(final SizeValue newX) {
    layoutPart.getBoxConstraints().setX(newX);
  }

  /**
   * set new y position constraint.
   * @param newY new y constaint.
   */
  public void setConstraintY(final SizeValue newY) {
    layoutPart.getBoxConstraints().setY(newY);
  }

  /**
   * set new width constraint.
   * @param newWidth new width constraint.
   */
  public void setConstraintWidth(final SizeValue newWidth) {
    layoutPart.getBoxConstraints().setWidth(newWidth);
  }

  /**
   * set new height constraint.
   * @param newHeight new height constraint.
   */
  public void setConstraintHeight(final SizeValue newHeight) {
    layoutPart.getBoxConstraints().setHeight(newHeight);
  }

  /**
   * get current width constraint.
   * @return current width constraint
   */
  public SizeValue getConstraintWidth() {
    return layoutPart.getBoxConstraints().getWidth();
  }

  /**
   * get current height constraint.
   * @return current height constraint.
   */
  public SizeValue getConstraintHeight() {
    return layoutPart.getBoxConstraints().getHeight();
  }

  /**
   * set new horizontal align.
   * @param newHorizontalAlign new horizontal align.
   */
  public void setConstraintHorizontalAlign(final HorizontalAlign newHorizontalAlign) {
    layoutPart.getBoxConstraints().setHorizontalAlign(newHorizontalAlign);
  }

  /**
   * set new vertical align.
   * @param newVerticalAlign new vertical align.
   */
  public void setConstraintVerticalAlign(final VerticalAlign newVerticalAlign) {
    layoutPart.getBoxConstraints().setVerticalAlign(newVerticalAlign);
  }

  /**
   * get current horizontal align.
   * @return current horizontal align.
   */
  public HorizontalAlign getConstraintHorizontalAlign() {
    return layoutPart.getBoxConstraints().getHorizontalAlign();
  }

  /**
   * get current vertical align.
   * @return current vertical align.
   */
  public VerticalAlign getConstraintVerticalAlign() {
    return layoutPart.getBoxConstraints().getVerticalAlign();
  }

  /**
   * register an effect for this element.
   * @param theId the effect id
   * @param e the effect
   */
  public void registerEffect(
      final EffectEventId theId,
      final Effect e) {
    log.fine("[" + this.getId() + "] register: " + theId.toString() + "(" + e.getStateString() + ")");
    effectManager.registerEffect(theId, e);
  }

  /**
   * on start screen event.
   * @param effectEventId the effect event id to start
   * @param effectEndNotiy the EffectEndNotify event we should activate
   */
  public void startEffect(final EffectEventId effectEventId, final EndNotify effectEndNotiy) {
    if (effectEventId == EffectEventId.onStartScreen) {
      done = false;
    }
    if (effectEventId == EffectEventId.onEndScreen) {
      done = true;
    }
    // whenever the effect ends we forward to this event
    // that checks first, if all child elements are finished
    // and when yes forwards to the actual effectEndNotify event.
    //
    // this way we ensure that all child finished the effects
    // before forwarding this to the real event handler.
    //
    // little bit tricky though :/
    LocalEndNotify forwardToSelf = new LocalEndNotify(effectEventId, effectEndNotiy);

    // start the effect for ourself
    effectManager.startEffect(effectEventId, this, time, forwardToSelf);

    // notify all child elements of the start effect
    for (Element w : getElements()) {
      w.startEffect(effectEventId, forwardToSelf);
    }

    if (effectEventId == EffectEventId.onFocus) {
      if (attachedInputControl != null) {
        attachedInputControl.onFocus(true);
      }
    }

    // just in case there was no effect activated, we'll check here, if we're already done
    forwardToSelf.perform();
  }

  public void startEffect(final EffectEventId effectEventId) {
    startEffect(effectEventId, null);
  }

  /**
   * stop the given effect.
   * @param effectEventId effect event id to stop
   */
  public void stopEffect(final EffectEventId effectEventId) {
    effectManager.stopEffect(effectEventId);

    // notify all child elements of the start effect
    for (Element w : getElements()) {
      w.stopEffect(effectEventId);
    }

    if (effectEventId == EffectEventId.onFocus) {
      if (attachedInputControl != null) {
        attachedInputControl.onFocus(false);
      }
    }
  }

  /**
   * check if a certain effect is still active. travels down to child elements.
   * @param effectEventId the effect type id to check
   * @return true, if the effect has ended and false otherwise
   */
  public boolean isEffectActive(final EffectEventId effectEventId) {
    for (Element w : getElements()) {
      if (w.isEffectActive(effectEventId)) {
        return true;
      }
    }
    return effectManager.isActive(effectEventId);
  }

  /**
   * enable this element.
   */
  public void enable() {
    enabled = true;
  }

  /**
   * disable this element.
   */
  public void disable() {
    enabled = false;
  }

  /**
   * is this element enabled?
   * @return true, if enabled and false otherwise.
   */
  public boolean isEnabled() {
    return enabled;
  }

  /**
   * show this element.
   */
  public void show() {
    // don't show if show is still in progress
    if (isEffectActive(EffectEventId.onShow)) {
      return;
    }

    // stop any onHide effects when a new onShow effect is about to be started
    if (isEffectActive(EffectEventId.onHide)) {
      resetSingleEffect(EffectEventId.onHide);
    }

    // show
    internalShow();
    startEffect(EffectEventId.onShow, new EndNotify() {
      public void perform() {
      }
    });
  }

  public void setVisible(final boolean visibleParam) {
    if (visibleParam) {
      show();
    } else {
      hide();
    }
  }

  private void internalShow() {
    visible = true;
    for (Element element : elements) {
      element.internalShow();
    }
  }

  /**
   * hide this element.
   */
  public void hide() {
    // don't hide if not visible
    if (!isVisible()) {
      return;
    }

    // don't hide if hide is still in progress
    if (isEffectActive(EffectEventId.onHide)) {
      return;
    }

    // stop any onShow effects when a new onHide effect is about to be started
    if (isEffectActive(EffectEventId.onShow)) {
      resetSingleEffect(EffectEventId.onShow);
    }

    // start effect and shizzle
    startEffect(EffectEventId.onHide, new EndNotify() {
      public void perform() {
        focusHandler.lostKeyboardFocus(Element.this);
        focusHandler.lostMouseFocus(Element.this);

        resetEffects();
        internalHide();
      }
    });
  }

  public void hideWithoutEffect() {
    // don't hide if not visible
    if (!isVisible()) {
      return;
    }

    focusHandler.lostKeyboardFocus(Element.this);
    focusHandler.lostMouseFocus(Element.this);

    resetEffects();
    internalHide();
  }

  private void internalHide() {
    visible = false;
    for (Element element : elements) {
      element.internalHide();
    }
  }

  /**
   * check if this element is visible.
   * @return true, if this element is visible and false otherwise.
   */
  public boolean isVisible() {
    return visible;
  }

  /**
   * set a new Falloff.
   * @param newFalloff new Falloff
   */
  public void setHotSpotFalloff(final Falloff newFalloff) {
    effectManager.setFalloff(newFalloff);
  }

  public Falloff getFalloff() {
    return effectManager.getFalloff();
  }

  /**
   * Checks if this element can handle mouse events.
   * @return true can handle mouse events, false can't handle them
   */
  boolean canHandleMouseEvents() {
    if (isEffectActive(EffectEventId.onStartScreen)) {
      return false;
    }
    if (isEffectActive(EffectEventId.onEndScreen)) {
      return false;
    }
    if (!visible) {
      return false;
    }
    if (done) {
      return false;
    }
    if (!visibleToMouseEvents) {
      return false;
    }
    if (!focusHandler.canProcessMouseEvents(this)) {
      return false;
    }
    return true;
  }

  /**
   * This should check of the mouse event is inside the current element and if it is
   * forward the event to it's child. The purpose of this is to build a list of all
   * elements from front to back that are available for a certain mouse position.
   * @param mouseEvent MouseInputEvent
   * @param eventTime time this event occured in ms
   * @param mouseOverHandler MouseOverHandler to fill
   */
  public void buildMouseOverElements(
      final MouseInputEvent mouseEvent,
      final long eventTime,
      final MouseOverHandler mouseOverHandler) {
    if (!canHandleMouseEvents()) {
      return;
    }

    if (isInside(mouseEvent)) {
      mouseOverHandler.addElement(this);
      for (Element w : getElements()) {
        w.buildMouseOverElements(mouseEvent, eventTime, mouseOverHandler);
      }
    }
  }

  /**
   * MouseEvent.
   * @param mouseEvent mouse event
   * @param eventTime event time
   */
  public boolean mouseEvent(final MouseInputEvent mouseEvent, final long eventTime) {
    for (Element w : getElements()) {
      if (w.mouseEvent(mouseEvent, eventTime)) {
        return true;
      }
    }
    if (!canHandleMouseEvents()) {
      return false;
    }
    effectManager.handleHover(this, mouseEvent.getMouseX(), mouseEvent.getMouseY());
    boolean mouseInside = isInside(mouseEvent);
    if (interaction.isOnClickRepeat()) {
      if (mouseInside && isMouseDown() && mouseEvent.isLeftButton()) {
        long deltaTime = eventTime - mouseDownTime;
        if (deltaTime > REPEATED_CLICK_START_TIME) {
          long pastTime = deltaTime - REPEATED_CLICK_START_TIME;
          long repeatTime = pastTime - lastRepeatStartTime;
          if (repeatTime > REPEATED_CLICK_TIME) {
            lastRepeatStartTime = pastTime;
            if (onClick(mouseEvent)) {
              return true;
            }
          }
        }
      }
    }
    if (mouseInside && !isMouseDown()) {
      if (mouseEvent.isLeftButton()) {
        setMouseDown(true, eventTime);
        if (focusable) {
          focusHandler.requestExclusiveMouseFocus(this);
          focusHandler.setKeyFocus(this);
        }
        return onClick(mouseEvent);
      }
    } else if (!mouseEvent.isLeftButton() && isMouseDown()) {
      setMouseDown(false, eventTime);
      effectManager.stopEffect(EffectEventId.onClick);
      focusHandler.lostMouseFocus(this);
      if (mouseInside) {
        onRelease();
      }
    }
    if (isMouseDown()) {
      onClickMouseMove(mouseEvent);
    }
    return false;
  }

  /**
   * Handle the MouseOverEvent. Must not call child elements. This is handled by caller.
   * @param mouseEvent mouse event
   * @param eventTime event time
   * @return true the mouse event has been eated and false when the mouse event can be processed further down
   */
  public boolean mouseOverEvent(final MouseInputEvent mouseEvent, final long eventTime) {
    boolean eatMouseEvent = false;

    if (interaction.onMouseOver(this, mouseEvent)) {
      eatMouseEvent = true;
    }

    return eatMouseEvent;
  }

  /**
   * checks to see if the given mouse position is inside of this element.
   * @param inputEvent MouseInputEvent
   * @return true when inside, false otherwise
   */
  private boolean isInside(final MouseInputEvent inputEvent) {
    return isMouseInsideElement(inputEvent.getMouseX(), inputEvent.getMouseY());
  }

  public boolean isMouseInsideElement(final int mouseX, final int mouseY) {
    if (parentClipArea) {
//      System.out.println("x: " + mouseX + ", y: " + mouseY + " [" + this.id + "] " + getX() + ", " + getY() + ", " + (getX() + getWidth()) + ", " + (getY() + getHeight()) + " [" + parentWithClipChildred.getId() + "] " + parentWithClipChildred.getX() + ", " + parentWithClipChildred.getY() + ", " + (parentWithClipChildred.getX() + parentWithClipChildred.getWidth()) + ", " + (parentWithClipChildred.getY() + parentWithClipChildred.getHeight()));
      // must be inside the parent to continue
      if (mouseX >= parentClipX
        &&
        mouseX <= (parentClipX + parentClipWidth)
        &&
        mouseY > (parentClipY)
        &&
        mouseY < (parentClipY + parentClipHeight)) {
          return
          mouseX >= getX()
          &&
          mouseX <= (getX() + getWidth())
          &&
          mouseY > (getY())
          &&
          mouseY < (getY() + getHeight());
        } else {
          return false;
        }
    } else {
      return
        mouseX >= getX()
        &&
        mouseX <= (getX() + getWidth())
        &&
        mouseY > (getY())
        &&
        mouseY < (getY() + getHeight());
    }
  }

  /**
   * on click method.
   * @param inputEvent event
   */
  public boolean onClick(final MouseInputEvent inputEvent) {
    effectManager.startEffect(EffectEventId.onClick, this, time, null);
    lastMouseX = inputEvent.getMouseX();
    lastMouseY = inputEvent.getMouseY();

    return interaction.onClick(inputEvent);
  }

  public void onClick() {
    effectManager.startEffect(EffectEventId.onClick, this, time, null);
    interaction.onClick();
  }

  public void onRelease() {
    interaction.onRelease();
  }

  /**
   * on click mouse move method.
   * @param inputEvent MouseInputEvent
   */
  private void onClickMouseMove(final MouseInputEvent inputEvent) {
    if (lastMouseX == inputEvent.getMouseX() && lastMouseY == inputEvent.getMouseY()) {
      return;
    }

    lastMouseX = inputEvent.getMouseX();
    lastMouseY = inputEvent.getMouseY();

    interaction.onClickMouseMoved(inputEvent);
  }

  /**
   * set on click method for the given screen.
   * @param methodInvoker the method to invoke
   * @param useRepeat repeat on click (true) or single event (false)
   */
  public void setOnClickMethod(final MethodInvoker methodInvoker, final boolean useRepeat) {
    interaction.setOnClickMethod(methodInvoker, useRepeat);
  }

  public void setOnReleaseMethod(final MethodInvoker onReleaseMethod) {
    interaction.setOnReleaseMethod(onReleaseMethod);
  }

  /**
   * Set on click mouse move method.
   * @param methodInvoker the method to invoke
   */
  public void setOnClickMouseMoveMethod(final MethodInvoker methodInvoker) {
    interaction.setOnClickMouseMoved(methodInvoker);
  }

  /**
   * set mouse down.
   * @param newMouseDown new state of mouse button.
   * @param eventTime the time in ms the event occured
   */
  private void setMouseDown(final boolean newMouseDown, final long eventTime) {
    this.mouseDownTime = eventTime;
    this.lastRepeatStartTime = 0;
    this.mouseDown = newMouseDown;
  }

  /**
   * is mouse down.
   * @return mouse down state.
   */
  private boolean isMouseDown() {
    return mouseDown;
  }

  /**
   * find an element by name.
   *
   * @param name the name of the element (id)
   * @return the element or null
   */
  public Element findElementByName(final String name) {
    if (id != null && id.equals(name)) {
      return this;
    }

    for (Element e : elements) {
      Element found = e.findElementByName(name);
      if (found != null) {
        return found;
      }
    }

    return null;
  }

  /**
   * set a new alternate key.
   * @param newAlternateKey new alternate key to use
   */
  public void setOnClickAlternateKey(final String newAlternateKey) {
    interaction.setAlternateKey(newAlternateKey);
  }

  /**
   * set alternate key.
   * @param alternateKey new alternate key
   */
  public void setAlternateKey(final String alternateKey) {
    effectManager.setAlternateKey(alternateKey);

    for (Element e : elements) {
      e.setAlternateKey(alternateKey);
    }
  }

  /**
   * get the effect manager.
   * @return the EffectManager
   */
  public EffectManager getEffectManager() {
    return effectManager;
  }

  /**
   * Set a New EffectManager.
   * @param effectManagerParam new Effectmanager
   */
  public void setEffectManager(final EffectManager effectManagerParam) {
    effectManager = effectManagerParam;
  }

  /**
   * On start screen event.
   * @param newScreen screen
   */
  public void onStartScreen(final Screen newScreen) {
    screen = newScreen;

    for (Element e : elements) {
      e.onStartScreen(newScreen);
    }

    if (focusable) {
      focusHandler.addElement(this);
    }

    if (attachedInputControl != null) {
      attachedInputControl.onStartScreen(screen);
    }
  }

  /**
   *
   * @param <T> the ElementRenderer type
   * @param requestedRendererClass the special ElementRenderer type to check for
   * @return the ElementRenderer that matches the class
   */
  public < T extends ElementRenderer > T getRenderer(final Class < T > requestedRendererClass) {
    for (ElementRenderer renderer : elementRenderer) {
      if (requestedRendererClass.isInstance(renderer)) {
        return requestedRendererClass.cast(renderer);
      }
    }
    return null;
  }

  /**
   * Set visible to mouse flag.
   * @param newVisibleToMouseEvents true or false
   */
  public void setVisibleToMouseEvents(final boolean newVisibleToMouseEvents) {
    this.visibleToMouseEvents = newVisibleToMouseEvents;
  }

  /**
   * keyboard event.
   * @param inputEvent keyboard event
   */
  public void keyEvent(final KeyboardInputEvent inputEvent) {
    if (attachedInputControl != null) {
      attachedInputControl.keyEvent(inputEvent);
    }
  }

  /**
   * Set clip children.
   * @param clipChildrenParam clip children flag
   */
  public void setClipChildren(final boolean clipChildrenParam) {
    this.clipChildren = clipChildrenParam;
  }

  /**
   * Is clip children enabled?
   * @return clip children
   */
  public boolean isClipChildren() {
    return this.clipChildren;
  }

  /**
   * Set the focus to this element.
   */
  public void setFocus() {
    if (nifty != null && nifty.getCurrentScreen() != null) {
      if (focusable) {
        focusHandler.setKeyFocus(this);
      }
    }
  }

  /**
   * attach an input control to this element.
   * @param newInputControl input control
   */
  public void attachInputControl(final NiftyInputControl newInputControl) {
    attachedInputControl = newInputControl;
  }

  /**
   * attach popup.
   * @param screenController screencontroller
   */
  public void attachPopup(final ScreenController screenController) {
    log.fine("attachPopup(" + screenController + ") to element [" + id + "]");
    attach(interaction.getOnClickMethod(), screenController);
    attach(interaction.getOnClickMouseMoveMethod(), screenController);
    attach(interaction.getOnReleaseMethod(), screenController);
  }

  /**
   * attach method.
   * @param method method
   * @param screenController method controller
   */
  private void attach(final MethodInvoker method, final ScreenController screenController) {
    method.setFirst(screenController);
    for (Element e : elements) {
      e.attachPopup(screenController);
    }
  }

  /**
   * LocalEndNotify helper class.
   * @author void
   */
  public class LocalEndNotify implements EndNotify {
    /**
     * event id.
     */
    private EffectEventId effectEventId;

    /**
     * end notify.
     */
    private EndNotify effectEndNotiy;

    /**
     * create it.
     * @param effectEventIdParam event id
     * @param effectEndNotiyParam end notify
     */
    public LocalEndNotify(final EffectEventId effectEventIdParam, final EndNotify effectEndNotiyParam) {
      effectEventId = effectEventIdParam;
      effectEndNotiy = effectEndNotiyParam;
    }

    /**
     * perform.
     */
    public void perform() {
      // notify parent if:
      // a) the effect is done for ourself
      // b) the effect is done for all of our children
      if (!isEffectActive(effectEventId)) {
        // all fine. we can notify the actual event handler
        if (effectEndNotiy != null) {
          effectEndNotiy.perform();
        }
      }
    }
  }

  /**
   * set id.
   * @param newId new id
   */
  public void setId(final String newId) {
    this.id = newId;
  }

  /**
   * get element type.
   * @return element type
   */
  public ElementType getElementType() {
    return elementType;
  }

  /**
   * get element renderer.
   * @return element renderer array
   */
  public ElementRenderer[] getElementRenderer() {
    return elementRenderer;
  }

  /**
   * set focusable flag.
   * @param newFocusable focusable flag
   */
  public void setFocusable(final boolean newFocusable) {
    this.focusable = newFocusable;
    for (Element e : elements) {
      e.setFocusable(newFocusable);
    }
  }

  /**
   * @return the attachedInputControl
   */
  public NiftyInputControl getAttachedInputControl() {
    return attachedInputControl;
  }

  /**
   * remove this and all children from the focushandler.
   */
  public void removeFromFocusHandler() {
    if (screen != null) {
      if (screen.getFocusHandler() != null) {
        screen.getFocusHandler().remove(this);
        for (Element element : elements) {
          element.removeFromFocusHandler();
        }
      }
    }
  }

  /**
   * set a new style.
   * @param newStyle new style to set
   */
  public void setStyle(final String newStyle) {
    removeStyle(elementType.getAttributes().get("style"));
    elementType.getAttributes().set("style", newStyle);
    elementType.applyStyles(nifty.getDefaultStyleResolver());
    elementType.applyAttributes(this, elementType.getAttributes(), nifty.getRenderEngine());
    elementType.applyEffects(nifty, screen, this);
    elementType.applyInteract(nifty, screen, this);
//    log.info("after setStyle [" + newStyle + "]\n" + elementType.output(0));
  }

  void removeStyle(final String style) {
    elementType.getAttributes().removeWithTag(style);
//    log.info("after removeStyle [" + style + "]\n" + elementType.output(0));
  }

  /**
   * add additional input handler to this element or childs of the elements.
   * @param handler additiona handler
   */
  public void addInputHandler(final KeyInputHandler handler) {
    if (attachedInputControl != null) {
      attachedInputControl.addInputHandler(handler);
    }
    for (Element element : elements) {
      element.addInputHandler(handler);
    }
  }

  public < T extends Controller > T findControl(final String elementName, final Class < T > requestedControlClass) {
    Element element = findElementByName(elementName);
    if (element == null) {
      return null;
    }
    return element.getControl(requestedControlClass);
  }

  /**
   * Get Control from element.
   * @param <T> Type
   * @param requestedControlClass requested class
   * @return controller or null
   */
  public < T extends Controller > T getControl(final Class < T > requestedControlClass) {
    if (attachedInputControl != null) {
      T t = attachedInputControl.getControl(requestedControlClass);
      if (t != null) {
        return t;
      }
    } else {
      for (Element element : elements) {
        T t = element.getControl(requestedControlClass);
        if (t != null) {
          return t;
        }
      }
    }
    return null;
  }

  /**
   * is focusable?
   * @return focusable
   */
  public boolean isFocusable() {
    return focusable;
  }

  /**
   * Set onMouseOverMethod.
   * @param onMouseOverMethod new on mouse over method
   */
  public void setOnMouseOverMethod(final MethodInvoker onMouseOverMethod) {
    this.interaction.setOnMouseOver(onMouseOverMethod);
  }

  /**
   * Get LayoutPart.
   * @return LayoutPart
   */
  public LayoutPart getLayoutPart() {
    return layoutPart;
  }

  /**
   * Get Element Interaction.
   * @return current ElementInteraction
   */
  public ElementInteraction getInteraction() {
    return interaction;
  }

  /**
   * Set Element Interaction.
   * @param elementInteractionParam ElementInteraction
   */
  public void setInteraction(final ElementInteraction elementInteractionParam) {
    interaction = elementInteractionParam;
  }

  /**
   * Is this element visible to mouse events.
   * @return true visible and false not visible
   */
  public boolean isVisibleToMouseEvents() {
    return visibleToMouseEvents;
  }

  public void setPaddingLeft(final SizeValue paddingValue) {
    layoutPart.getBoxConstraints().setPaddingLeft(paddingValue);
  }

  public void setPaddingRight(final SizeValue paddingValue) {
    layoutPart.getBoxConstraints().setPaddingRight(paddingValue);
  }

  public void setPaddingTop(final SizeValue paddingValue) {
    layoutPart.getBoxConstraints().setPaddingTop(paddingValue);
  }

  public void setPaddingBottom(final SizeValue paddingValue) {
    layoutPart.getBoxConstraints().setPaddingBottom(paddingValue);
  }

  public String toString() {
    return id + " (" + super.toString() + ")";
  }

  public boolean isStarted() {
    return isEffectActive(EffectEventId.onStartScreen);
  }
}
