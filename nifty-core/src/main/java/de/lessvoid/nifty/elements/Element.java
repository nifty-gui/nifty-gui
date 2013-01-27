package de.lessvoid.nifty.elements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;

import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEvent;
import de.lessvoid.nifty.NiftyMethodInvoker;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.controls.FocusHandler;
import de.lessvoid.nifty.controls.NiftyControl;
import de.lessvoid.nifty.controls.NiftyInputControl;
import de.lessvoid.nifty.effects.Effect;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.EffectManager;
import de.lessvoid.nifty.effects.ElementEffectStateCache;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.elements.events.ElementDisableEvent;
import de.lessvoid.nifty.elements.events.ElementEnableEvent;
import de.lessvoid.nifty.elements.events.ElementHideEvent;
import de.lessvoid.nifty.elements.events.ElementShowEvent;
import de.lessvoid.nifty.elements.render.ElementRenderer;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.elements.render.PanelRenderer;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.elements.tools.ElementTreeTraverser;
import de.lessvoid.nifty.input.NiftyMouseInputEvent;
import de.lessvoid.nifty.input.keyboard.KeyboardInputEvent;
import de.lessvoid.nifty.layout.LayoutPart;
import de.lessvoid.nifty.layout.align.HorizontalAlign;
import de.lessvoid.nifty.layout.align.VerticalAlign;
import de.lessvoid.nifty.layout.manager.LayoutManager;
import de.lessvoid.nifty.loaderv2.types.ElementType;
import de.lessvoid.nifty.loaderv2.types.PopupType;
import de.lessvoid.nifty.loaderv2.types.apply.ApplyRenderText;
import de.lessvoid.nifty.loaderv2.types.apply.ApplyRenderer;
import de.lessvoid.nifty.loaderv2.types.apply.ApplyRendererImage;
import de.lessvoid.nifty.loaderv2.types.apply.ApplyRendererPanel;
import de.lessvoid.nifty.loaderv2.types.apply.Convert;
import de.lessvoid.nifty.loaderv2.types.helper.PaddingAttributeParser;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.screen.KeyInputHandler;
import de.lessvoid.nifty.screen.MouseOverHandler;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.spi.time.TimeProvider;
import de.lessvoid.nifty.tools.NullObjectFactory;
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.xml.xpp3.Attributes;

/**
 * The Element.
 * @author void
 */
public class Element implements NiftyEvent, EffectManager.Notify {

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
  private int renderOrder;

  /**
   * the parent element.
   */
  private Element parent;

  /**
   * the child elements.
   */
  private List < Element > elements = new ArrayList < Element >(0);

  /**
   * This set defines the render order of the child elements using a Comparator.
   */
  private Set < Element > elementsRenderOrderSet = new TreeSet < Element >(new Comparator<Element>() {

    /**
     * This uses the renderOrder attribute of the elements to compare them. If the renderOrder
     * attribute is not set (is 0) then the index of the element in the elements list is used
     * as the renderOrder value. This is done to keep the original sort order of the elements for
     * rendering. The value is not cached and is directly recalculated using the element index in
     * the list. The child count for an element is usually low (< 10) and the comparator is only
     * executed when the child elements change. So this lookup shouldn't hurt performance too much.
     *
     * If you change the default value of renderOrder then your value is being used. So if you set it
     * to some high value (> 1000 to be save) this element is rendered after all the other elements.
     * If you set it to some very low value (< -1000 to be save) then this element is rendered before
     * all the others.
     */
    @Override
    public int compare(final Element o1, final Element o2) {
      int o1RenderOrder = getRenderOrder(o1);
      int o2RenderOrder = getRenderOrder(o2);

      if (o1RenderOrder < o2RenderOrder) {
        return -1;
      } else if (o1RenderOrder > o2RenderOrder) {
        return 1;
      }
      // this means the renderOrder values are equal. since this is a set
      // we can't return 0 because this would mean (for the set) that the
      // elements are equal and one of the values will be removed. so here
      // we simply compare the String representation of the elements so that
      // we keep a fixed sort order.
      String o1Id = o1.id;
      String o2Id = o2.id;
      if (o1Id == null && o2Id != null){
        return -1;
      } else if (o1Id != null && o2Id == null) {
        return 1;
      } else if (o1Id != null && o2Id != null) {
        int idCompareResult = o1Id.compareTo(o2Id);
        if (idCompareResult != 0) {
          return idCompareResult;
        }
      }

      // ids equal or both null use super.toString()
      // hashCode() should return a value thats different for both elements since
      // adding the same element twice to the same parent element is not supported.
      String ref1 = Integer.toHexString(o1.hashCode());
      String ref2 = Integer.toHexString(o2.hashCode());
      return ref1.compareTo(ref2);
    }

    private int getRenderOrder(final Element element) {
      if (element.renderOrder != 0) {
        return element.renderOrder;
      }
      return elements.indexOf(element);
    }
  });

  /**
   * We keep a copy of the elementsRenderOrderSet in a simple array for being more GC friendly while rendering.
   */
  private Element[] elementsRenderOrder = new Element[0];

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
  private ElementRenderer[] elementRenderer = new ElementRenderer[0];

  /**
   * the effect manager for this element.
   */
  private EffectManager effectManager;

  /**
   * Element interaction.
   */
  private ElementInteraction interaction;

  /**
   * Effect state cache (this includes info about the child state) and is only
   * update when Effect states are changed.
   */
  private ElementEffectStateCache effectStateCache = new ElementEffectStateCache();

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
   * This helps us to keep track when you enable or disable this multiple times. We don't want
   * to start the onEnabled/onDisabled effects when the element is already enabled/disabled.
   */
  private int enabledCount;

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
   * this is set to true when there's no interaction with this element possibe.
   * (as long as onStartScreen and onEndScreen events are active even when this
   * element is not using the onStartScreen effect at all but a parent element did)
   */
  private boolean interactionBlocked;

  /**
   * visible to mouse events.
   */
  private boolean visibleToMouseEvents;

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
   * This attribute determines the element before (!) the new element will be inserted into the focusHandler.
   * You can set this to any element id on the screen and this element will be inserted right before the
   * given element. This is especially useful when you dynamically remove and add elements and you need to
   * enforce a specific focus order.
   *
   * The default value is null which will simply add the elements as they are created.
   */
  private String focusableInsertBeforeElementId;

  /**
   * screen we're connected to.
   */
  private Screen screen;

  /**
   * TimeProvider.
   */
  private TimeProvider time;

  private List<String> elementDebugOut = new ArrayList<String>();
  private StringBuilder elementDebug = new StringBuilder();

  private boolean parentClipArea = false;
  private int parentClipX;
  private int parentClipY;
  private int parentClipWidth;
  private int parentClipHeight;

  /*
   * when set to true this Element will ignore all mouse events.
   */
  private boolean ignoreMouseEvents;

  /*
   * when set to true this Element will ignore all keyboard events.
   */
  private boolean ignoreKeyboardEvents;

  private static Convert convert = new Convert();
  private static Map < Class < ? extends ElementRenderer >, ApplyRenderer > rendererApplier = new HashMap < Class < ? extends ElementRenderer>, ApplyRenderer >();
  {
    rendererApplier.put(TextRenderer.class, new ApplyRenderText(convert));
    rendererApplier.put(ImageRenderer.class, new ApplyRendererImage(convert));
    rendererApplier.put(PanelRenderer.class, new ApplyRendererPanel(convert));
  }

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
   * This is used when the element is being created from an ElementType in the loading process.
   */
  public void initializeFromAttributes(final Attributes attributes, final NiftyRenderEngine renderEngine) {
    layoutPart.getBoxConstraints().setHeight(convert.sizeValue(attributes.get("height")));
    layoutPart.getBoxConstraints().setWidth(convert.sizeValue(attributes.get("width")));
    layoutPart.getBoxConstraints().setX(convert.sizeValue(attributes.get("x")));
    layoutPart.getBoxConstraints().setY(convert.sizeValue(attributes.get("y")));
    layoutPart.getBoxConstraints().setHorizontalAlign(convert.horizontalAlign(attributes.get("align")));
    layoutPart.getBoxConstraints().setVerticalAlign(convert.verticalAlign(attributes.get("valign")));

    String paddingLeft = Convert.DEFAULT_PADDING;
    String paddingRight = Convert.DEFAULT_PADDING;
    String paddingTop = Convert.DEFAULT_PADDING;
    String paddingBottom = Convert.DEFAULT_PADDING;
    if (attributes.isSet("padding")) {
      try {
        PaddingAttributeParser paddingParser = new PaddingAttributeParser(attributes.get("padding"));
        paddingLeft = paddingParser.getLeft();
        paddingRight = paddingParser.getRight();
        paddingTop = paddingParser.getTop();
        paddingBottom = paddingParser.getBottom();
      } catch (Exception e) {
        log.warning(e.getMessage());
      }
    }
    layoutPart.getBoxConstraints().setPaddingLeft(convert.paddingSizeValue(attributes.get("paddingLeft"), paddingLeft));
    layoutPart.getBoxConstraints().setPaddingRight(convert.paddingSizeValue(attributes.get("paddingRight"), paddingRight));
    layoutPart.getBoxConstraints().setPaddingTop(convert.paddingSizeValue(attributes.get("paddingTop"), paddingTop));
    layoutPart.getBoxConstraints().setPaddingBottom(convert.paddingSizeValue(attributes.get("paddingBottom"), paddingBottom));

    String marginLeft = Convert.DEFAULT_MARGIN;
    String marginRight = Convert.DEFAULT_MARGIN;
    String marginTop = Convert.DEFAULT_MARGIN;
    String marginBottom = Convert.DEFAULT_MARGIN;
    if (attributes.isSet("margin")) {
      try {
        PaddingAttributeParser marginParser = new PaddingAttributeParser(attributes.get("margin"));
        marginLeft = marginParser.getLeft();
        marginRight = marginParser.getRight();
        marginTop = marginParser.getTop();
        marginBottom = marginParser.getBottom();
      } catch (Exception e) {
        log.warning(e.getMessage());
      }
    }
    layoutPart.getBoxConstraints().setMarginLeft(convert.paddingSizeValue(attributes.get("marginLeft"), marginLeft));
    layoutPart.getBoxConstraints().setMarginRight(convert.paddingSizeValue(attributes.get("marginRight"), marginRight));
    layoutPart.getBoxConstraints().setMarginTop(convert.paddingSizeValue(attributes.get("marginTop"), marginTop));
    layoutPart.getBoxConstraints().setMarginBottom(convert.paddingSizeValue(attributes.get("marginBottom"), marginBottom));

    this.clipChildren = attributes.getAsBoolean("childClip", Convert.DEFAULT_CHILD_CLIP);
    this.renderOrder = attributes.getAsInteger("renderOrder", Convert.DEFAULT_RENDER_ORDER);
    boolean visible = attributes.getAsBoolean("visible", Convert.DEFAULT_VISIBLE);
    if (visible) {
      this.visible = true;
    }
    this.visibleToMouseEvents = attributes.getAsBoolean("visibleToMouse", Convert.DEFAULT_VISIBLE_TO_MOUSE);
    this.layoutManager = convert.layoutManager(attributes.get("childLayout"));

    this.focusable = attributes.getAsBoolean("focusable", Convert.DEFAULT_FOCUSABLE);
    this.focusableInsertBeforeElementId = attributes.get("focusableInsertBeforeElementId");
    for (int i=0; i<elementRenderer.length; i++) {
      ElementRenderer renderer = elementRenderer[i];
      ApplyRenderer rendererApply = rendererApplier.get(renderer.getClass());
      rendererApply.apply(this, attributes, renderEngine);
    }
  }

  public void initializeFromPostAttributes(final Attributes attributes) {
    boolean visible = attributes.getAsBoolean("visible", Convert.DEFAULT_VISIBLE);
    if (!visible) {
      hideWithChildren();
    }
  }

  private void hideWithChildren() {
    visible = false;
    for (int i=0; i<elements.size(); i++) {
      Element element = elements.get(i);
      element.hideWithChildren();
    }
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
    if (newElementRenderer == null) {
      this.elementRenderer = new ElementRenderer[0];
    } else {
      this.elementRenderer = newElementRenderer;
    }
    this.effectManager = new EffectManager(this);
    this.effectManager.setAlternateKey(nifty.getAlternateKey());
    this.layoutPart = newLayoutPart;
    this.enabled = true;
    this.enabledCount = 0;
    this.visible = true;
    this.done = false;
    this.interactionBlocked = false;
    this.focusHandler = newFocusHandler;
    this.visibleToMouseEvents = newVisibleToMouseEvents;
    this.time = timeProvider;
    this.interaction = new ElementInteraction(nifty, this);
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

  public void setParent(final Element element) {
    parent = element;

    // this element has a new parent. check the parentClipArea and update this element accordingly.
    if (parentHasClipArea()) {
      setParentClipArea(parentClipX, parentClipY, parentClipWidth, parentClipHeight);
      notifyListeners();
    } else {
      parentClipArea = false;
    }
  }

  private boolean parentHasClipArea() {
    return parent.parentClipArea;
  }

  /**
   * get element state as string.
   * @param offset offset string
   * @param regex
   * @return the element state as string.
   */
  public String getElementStateString(final String offset) {
    return getElementStateString(offset, ".*");
  }

  public String getElementStateString(final String offset, final String regex) {
    elementDebugOut.clear();
    elementDebugOut.add(" type: [" + elementType.output(offset.length()) + "]");
    elementDebugOut.add(" style [" + getElementType().getAttributes().get("style") + "]");
    elementDebugOut.add(" state [" + getState() + "]");
    elementDebugOut.add(" position [x=" + getX() + ", y=" + getY() + ", w=" + getWidth() + ", h=" + getHeight() + "]");
    elementDebugOut.add(" constraint [" + outputSizeValue(layoutPart.getBoxConstraints().getX()) + ", " + outputSizeValue(layoutPart.getBoxConstraints().getY()) + ", " + outputSizeValue(layoutPart.getBoxConstraints().getWidth()) + ", " + outputSizeValue(layoutPart.getBoxConstraints().getHeight()) + "]");
    elementDebugOut.add(" padding [" + outputSizeValue(layoutPart.getBoxConstraints().getPaddingLeft()) + ", " + outputSizeValue(layoutPart.getBoxConstraints().getPaddingRight()) + ", " + outputSizeValue(layoutPart.getBoxConstraints().getPaddingTop()) + ", " + outputSizeValue(layoutPart.getBoxConstraints().getPaddingBottom()) + "]");
    elementDebugOut.add(" margin [" + outputSizeValue(layoutPart.getBoxConstraints().getMarginLeft()) + ", " + outputSizeValue(layoutPart.getBoxConstraints().getMarginRight()) + ", " + outputSizeValue(layoutPart.getBoxConstraints().getMarginTop()) + ", " + outputSizeValue(layoutPart.getBoxConstraints().getMarginBottom()) + "]");
    StringBuffer state = new StringBuffer();
    if (focusable) {
      state.append(" focusable");
    }
    if (enabled) {
      if (state.length() > 0) {
        state.append(",");
      }
      state.append(" enabled(" + enabledCount + ")");
    }
    if (visible) {
      if (state.length() > 0) {
        state.append(",");
      }
      state.append(" visible");
    }
    if (visibleToMouseEvents) {
      if (state.length() > 0) {
        state.append(",");
      }
      state.append(" mouseable");
    }
    if (clipChildren) {
      if (state.length() > 0) {
        state.append(",");
      }
      state.append(" clipChildren");
    }
    elementDebugOut.add(" flags [" + state + "]");
    elementDebugOut.add(" effects [" + effectManager.getStateString(offset) + "]");
    elementDebugOut.add(" renderOrder [" + renderOrder + "]");

    if (parentClipArea) {
      elementDebugOut.add(" parent clip [x=" + parentClipX + ", y=" + parentClipY + ", w=" + parentClipWidth + ", h=" + parentClipHeight + "]");
    }
    StringBuffer renderOrder = new StringBuffer();
    renderOrder.append(" render order: ");
    for (Element e : elementsRenderOrderSet) {
      renderOrder.append("[" + e.getId() + " (" + ((e.renderOrder == 0) ? elements.indexOf(e) : e.renderOrder) + ")]");
    }
    elementDebugOut.add(renderOrder.toString());

    elementDebug.delete(0, elementDebug.length());
    for (int i=0; i<elementDebugOut.size(); i++) {
      String line = elementDebugOut.get(i);
      if (line.matches(regex)) {
        if (elementDebug.length() > 0) {
          elementDebug.append("\n" + offset);
        }
        elementDebug.append(line);
      }
    }
    return elementDebug.toString();
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

    if (interactionBlocked) {
      return "interactionBlocked";
    }

    if (!enabled) {
      return "disabled";
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
   * Sets the height of this element
   * @param height the new height in pixels
   */
  public void setHeight(int height) {
    layoutPart.getBox().setHeight(height);
  }

  /**
   * Sets the width of this element
   * @param width the new width in pixels
   */
  public void setWidth(int width) {
    layoutPart.getBox().setWidth(width);
  }

  /**
   * get all child elements of this element.
   * @return the list of child elements
   */
  public List < Element > getElements() {
    return Collections.unmodifiableList(elements);
  }

  /**
   * get all children and all childrens' children.
   * @return an iterator that will traverse the entire Element tree.
   */
  public Iterator<Element> getDescendants(){
    return new ElementTreeTraverser(this);
  }
  /**
   * add a child element.
   * @param widget the child to add
   */
  public void add(final Element widget) {
    elements.add(widget);
    elementsRenderOrderSet.add(widget);
    elementsRenderOrder = elementsRenderOrderSet.toArray(new Element[0]);
  }

  /**
   * render this element.
   * @param r the RenderDevice to use
   */
  public void render(final NiftyRenderEngine r) {
    if (visible) {
      if (effectManager.isEmpty()) {
        r.saveState(null);
        renderElement(r);
        renderChildren(r);
        r.restoreState();
      } else {
        r.saveState(null);
        effectManager.begin(r, this);
        effectManager.renderPre(r, this);
        renderElement(r);
        effectManager.renderPost(r, this);
        effectManager.end(r);
        renderChildren(r);
        r.restoreState();
        r.saveState(null);
        effectManager.renderOverlay(r, this);
        r.restoreState();
      }
    }
  }

  private void renderElement(final NiftyRenderEngine r) {
    for (int i=0; i<elementRenderer.length; i++) {
      ElementRenderer renderer = elementRenderer[i];
      renderer.render(this, r);
    }
  }

  private void renderChildren(final NiftyRenderEngine r) {
    if (clipChildren) {
      r.enableClip(getX(), getY(), getX() + getWidth(), getY() + getHeight());
      renderInternalChildElements(r);
      r.disableClip();
    } else {
      renderInternalChildElements(r);
    }
  }

  private void renderInternalChildElements(final NiftyRenderEngine r) {
    for (int i=0; i<elementsRenderOrder.length; i++) {
      Element p = elementsRenderOrder[i];
      p.render(r);
    }
  }

  /**
   * Set a new LayoutManager.
   * @param newLayout the new LayoutManager to use.
   */
  public void setLayoutManager(final LayoutManager newLayout) {
    this.layoutManager = newLayout;
  }

  public void resetLayout() {
    isCalcWidthConstraint = false;
    isCalcHeightConstraint = false;

    TextRenderer textRenderer = getRenderer(TextRenderer.class);
    if (textRenderer != null) {
      textRenderer.resetLayout(this);
    }

    for (int i=0; i<elements.size(); i++) {
      Element e = elements.get(i);
      e.resetLayout();
    }
  }

  private void preProcessConstraintWidth() {
    for (int i=0; i<elements.size(); i++) {
      Element e = elements.get(i);
      e.preProcessConstraintWidth();
    }

    preProcessConstraintWidthThisLevel();
  }

  private void preProcessConstraintWidthThisLevel() {
    // try the original width value first
    SizeValue myWidth = getConstraintWidth();

    // is it empty and we have an layoutManager there's still hope for a width constraint
    if (layoutManager != null && (myWidth == null || isCalcWidthConstraint)) {

      // collect all child layoutPart that have a fixed pixel size in a list
      List < LayoutPart > layoutPartChild = new ArrayList < LayoutPart >();
      for (int i=0; i<elements.size(); i++) {
        Element e = elements.get(i);
        SizeValue childWidth = e.getConstraintWidth();
        if (childWidth != null && childWidth.isPixel()) {
          layoutPartChild.add(e.layoutPart);
        }
      }

      // if all (!) child elements have a pixel fixed width we can calculate a new width constraint for this element!
      if (elements.size() == layoutPartChild.size()) {
        // we don't have anything to calculate values from so we quit
        if (layoutPartChild.size() == 0) {
          // but before we check if we eventually need to reset the constrain
          checkAndResetCalculatedWidthConstraint(myWidth);
          return;
        }
        SizeValue newWidth = layoutManager.calculateConstraintWidth(this.layoutPart, layoutPartChild);
        if (newWidth != null) {
          int newWidthPx = newWidth.getValueAsInt(0);
          newWidthPx += this.layoutPart.getBoxConstraints().getPaddingLeft().getValueAsInt(newWidth.getValueAsInt(newWidthPx));
          newWidthPx += this.layoutPart.getBoxConstraints().getPaddingRight().getValueAsInt(newWidth.getValueAsInt(newWidthPx));
          setConstraintWidth(SizeValue.px(newWidthPx));
          isCalcWidthConstraint = true;
        }
      } else {
        checkAndResetCalculatedWidthConstraint(myWidth);
      }
    }
  }

  private void checkAndResetCalculatedWidthConstraint(final SizeValue currentWidthConstraint) {
    if (!isCalcWidthConstraint) {
      return;
    }
    // this now means we had a calculatedWidthConstrained before but for whatever reason
    // that is not valid anymore so we need to reset it here.
    if (currentWidthConstraint != null) {
      setConstraintWidth(null);
    }
  }

  private void preProcessConstraintHeight() {
    for (int i=0; i<elements.size(); i++) {
      Element e = elements.get(i);
      e.preProcessConstraintHeight();
    }

    preProcessConstraintHeightThisLevel();
  }

  private void preProcessConstraintHeightThisLevel() {
    // try the original height value first
    SizeValue myHeight = getConstraintHeight();

    // is it empty and we have an layoutManager there's still hope for a height constraint
    if (layoutManager != null && (myHeight == null || isCalcHeightConstraint)) {
      // collect all child layoutPart that have a fixed px size in a list
      List < LayoutPart > layoutPartChild = new ArrayList < LayoutPart >();
      for (int i=0; i<elements.size(); i++) {
        Element e = elements.get(i);
        SizeValue childHeight = e.getConstraintHeight();
        if (childHeight != null && childHeight.isPixel()) {
          layoutPartChild.add(e.layoutPart);
        }
      }

      // if all (!) child elements have a px fixed height we can calculate a new height constraint for this element!
      if (elements.size() == layoutPartChild.size()) {
        // we don't have anything to calculate values from so we quit
        if (layoutPartChild.size() == 0) {
          // but before we check if we eventually need to reset the constrain
          checkAndResetCalculatedHeightConstraint(myHeight);
          return;
        }
        SizeValue newHeight = layoutManager.calculateConstraintHeight(this.layoutPart, layoutPartChild);
        if (newHeight != null) {
          int newHeightPx = newHeight.getValueAsInt(0);
          newHeightPx += this.layoutPart.getBoxConstraints().getPaddingTop().getValueAsInt(newHeight.getValueAsInt(newHeightPx));
          newHeightPx += this.layoutPart.getBoxConstraints().getPaddingBottom().getValueAsInt(newHeight.getValueAsInt(newHeightPx));
          setConstraintHeight(SizeValue.px(newHeightPx));
          isCalcHeightConstraint = true;
        }
      } else {
        checkAndResetCalculatedHeightConstraint(myHeight);
      }
    }
  }

  private void checkAndResetCalculatedHeightConstraint(final SizeValue myHeightConstraint) {
    if (!isCalcHeightConstraint) {
      return;
    }
    // this now means we had a calculatedWidthConstrained before but for whatever reason
    // that is not valid anymore so we need to reset it here.
    if (myHeightConstraint != null) {
      setConstraintHeight(null);
    }
  }

  private void processLayoutInternal() {
    for (int i=0; i<elements.size(); i++) {
      Element w = elements.get(i);
      TextRenderer textRenderer = w.getRenderer(TextRenderer.class);
      if (textRenderer != null) {
        textRenderer.setWidthConstraint(w, w.getConstraintWidth(), getWidth(), nifty.getRenderEngine());
      }
    }
  }

  private void processLayout() {
    processLayoutInternal();

    if (layoutManager != null) {
      // we need a list of LayoutPart and not of Element, so we'll build one on the fly here
      List < LayoutPart > layoutPartChild = new ArrayList < LayoutPart >();
      for (int i=0; i<elements.size(); i++) {
        Element w = elements.get(i);
        layoutPartChild.add(w.layoutPart);
      }

      // use out layoutManager to layout our children
      layoutManager.layoutElements(layoutPart, layoutPartChild);

      if (attachedInputControl != null) {
        NiftyControl niftyControl = attachedInputControl.getNiftyControl(NiftyControl.class);
        if (niftyControl != null) {
          if (niftyControl.isBound()) {
            niftyControl.layoutCallback();
          }
        }
      }

      // repeat this step for all child elements
      for (int i=0; i<elements.size(); i++) {
        Element w = elements.get(i);
        w.processLayout();
      }
    }

    if (clipChildren) {
      for (int i=0; i<elements.size(); i++) {
        Element w = elements.get(i);
        w.setParentClipArea(getX(), getY(), getWidth(), getHeight());
      }
    }
  }

  public void layoutElements() {
    prepareLayout();
    processLayout();

    prepareLayout();
    processLayout();
  }

  private void prepareLayout() {
    preProcessConstraintWidth();
    preProcessConstraintHeight();
  }

  private void setParentClipArea(final int x, final int y, final int width, final int height) {
    parentClipArea = true;
    parentClipX = x;
    parentClipY = y;
    parentClipWidth = width;
    parentClipHeight = height;

    for (int i=0; i<elements.size(); i++) {
      Element w = elements.get(i);
      w.setParentClipArea(parentClipX, parentClipY, parentClipWidth, parentClipHeight);
    }
  }

  /**
   * reset all effects.
   */
  public void resetEffects() {
    effectManager.reset();
    for (int i=0; i<elements.size(); i++) {
      Element w = elements.get(i);
      w.resetEffects();
    }
  }

  public void resetAllEffects() {
    effectManager.resetAll();
    for (int i=0; i<elements.size(); i++) {
      Element w = elements.get(i);
      w.resetAllEffects();
    }
  }

  public void resetForHide() {
    effectManager.resetForHide();
    for (int i=0; i<elements.size(); i++) {
      Element w = elements.get(i);
      w.resetForHide();
    }
  }

  public void resetSingleEffect(final EffectEventId effectEventId) {
    effectManager.resetSingleEffect(effectEventId);
    for (int i=0; i<elements.size(); i++) {
      Element w = elements.get(i);
      w.resetSingleEffect(effectEventId);
    }
  }

  public void resetSingleEffect(final EffectEventId effectEventId, final String customKey) {
    effectManager.resetSingleEffect(effectEventId, customKey);
    for (int i=0; i<elements.size(); i++) {
      Element w = elements.get(i);
      w.resetSingleEffect(effectEventId, customKey);
    }
  }

  public void resetMouseDown() {
    interaction.resetMouseDown();
    for (int i=0; i<elements.size(); i++) {
      Element w = elements.get(i);
      w.resetMouseDown();
    }
  }

  /**
   * set new x position constraint.
   * @param newX new x constraint.
   */
  public void setConstraintX(final SizeValue newX) {
    layoutPart.getBoxConstraints().setX(newX);
    notifyListeners();
  }

  /**
   * set new y position constraint.
   * @param newY new y constaint.
   */
  public void setConstraintY(final SizeValue newY) {
    layoutPart.getBoxConstraints().setY(newY);
    notifyListeners();
  }

  /**
   * set new width constraint.
   * @param newWidth new width constraint.
   */
  public void setConstraintWidth(final SizeValue newWidth) {
    layoutPart.getBoxConstraints().setWidth(newWidth);
    notifyListeners();
  }

  /**
   * set new height constraint.
   * @param newHeight new height constraint.
   */
  public void setConstraintHeight(final SizeValue newHeight) {
    layoutPart.getBoxConstraints().setHeight(newHeight);
    notifyListeners();
  }

  public SizeValue getConstraintX() {
    return layoutPart.getBoxConstraints().getX();
  }

  public SizeValue getConstraintY() {
    return layoutPart.getBoxConstraints().getY();
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

  public void startEffect(final EffectEventId effectEventId) {
    startEffect(effectEventId, null);
  }

  public void startEffect(final EffectEventId effectEventId, final EndNotify effectEndNotiy) {
    startEffect(effectEventId, effectEndNotiy, null);
  }

  public void startEffect(final EffectEventId effectEventId, final EndNotify effectEndNotiy, final String customKey) {
    startEffectDoIt(effectEventId, effectEndNotiy, customKey, true);
  }

  public void startEffectWithoutChildren(final EffectEventId effectEventId) {
    startEffectWithoutChildren(effectEventId, null);
  }

  public void startEffectWithoutChildren(final EffectEventId effectEventId, final EndNotify effectEndNotiy) {
    startEffectWithoutChildren(effectEventId, effectEndNotiy, null);
  }

  public void startEffectWithoutChildren(final EffectEventId effectEventId, final EndNotify effectEndNotiy, final String customKey) {
    startEffectDoIt(effectEventId, effectEndNotiy, customKey, false);
  }

  private void startEffectDoIt(final EffectEventId effectEventId, final EndNotify effectEndNotiy, final String customKey, final boolean withChildren) {
    if (effectEventId == EffectEventId.onStartScreen) {
      if (!visible) {
        return;
      }
      done = false;
      interactionBlocked = true;
    }
    if (effectEventId == EffectEventId.onEndScreen) {
      if (!visible) {
        // it doesn't make sense to start the onEndScreen effect when the element is hidden
        // just call the effectEndNotify directly and quit
        effectEndNotiy.perform();
        return;
      }
      done = true;
      interactionBlocked = true;
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
    effectManager.startEffect(effectEventId, this, time, forwardToSelf, customKey);

    // notify all child elements of the start effect
    if (withChildren) {
      for (int i=0; i<elements.size(); i++) {
        Element w = elements.get(i);
        w.startEffectInternal(effectEventId, forwardToSelf, customKey);
      }
    }

    if (effectEventId == EffectEventId.onFocus) {
      if (attachedInputControl != null) {
        attachedInputControl.onFocus(true);
      }
    }

    // just in case there was no effect activated, we'll check here, if we're already done
    forwardToSelf.perform();
  }

  private void startEffectInternal(final EffectEventId effectEventId, final EndNotify effectEndNotiy, final String customKey) {
    if (effectEventId == EffectEventId.onStartScreen) {
      if (!visible) {
        return;
      }
      done = false;
      interactionBlocked = true;
    }
    if (effectEventId == EffectEventId.onEndScreen) {
      if (!visible) {
        return;
      }
      done = true;
      interactionBlocked = true;
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
    effectManager.startEffect(effectEventId, this, time, forwardToSelf, customKey);

    // notify all child elements of the start effect
    for (int i=0; i<elements.size(); i++) {
      Element w = elements.get(i);
      w.startEffectInternal(effectEventId, forwardToSelf, customKey);
    }

    if (effectEventId == EffectEventId.onFocus) {
      if (attachedInputControl != null) {
        attachedInputControl.onFocus(true);
      }
    }
  }

  /**
   * stop the given effect.
   * @param effectEventId effect event id to stop
   */
  public void stopEffect(final EffectEventId effectEventId) {
    stopEffectInternal(effectEventId, true);
  }

  public void stopEffectWithoutChildren(final EffectEventId effectEventId) {
    stopEffectInternal(effectEventId, false);
  }

  private void stopEffectInternal(final EffectEventId effectEventId, final boolean withChildren) {
    if (EffectEventId.onStartScreen == effectEventId ||
        EffectEventId.onEndScreen == effectEventId) {
      interactionBlocked = false;
      if (!visible) {
        return;
      }
    }
    effectManager.stopEffect(effectEventId);

    // notify all child elements of the start effect
    if (withChildren) {
      for (int i=0; i<elements.size(); i++) {
        Element w = elements.get(i);
        w.stopEffect(effectEventId);
      }
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
    return effectStateCache.get(effectEventId);
  }

  /**
   * enable this element.
   */
  public void enable() {
    if (enabled) {
      return;
    }
    enableInternal();
  }

  private void enableInternal() {
    enabledCount++;
    if (enabledCount == 0) {
      enabled = true;
      enableEffect();
      for (int i=0; i<elements.size(); i++) {
        elements.get(i).enableInternal();
      }
    } else {
      for (int i=0; i<elements.size(); i++) {
        elements.get(i).enableInternal();
      }
    }
  }

  void enableEffect() {
    stopEffectWithoutChildren(EffectEventId.onDisabled);
    startEffectWithoutChildren(EffectEventId.onEnabled);
    nifty.publishEvent(getId(), new ElementEnableEvent(this));
  }

  /**
   * disable this element.
   */
  public void disable() {
    if (!enabled) {
      return;
    }
    disableInternal();
  }

  private void disableInternal() {
    enabledCount--;
    if (enabledCount == -1) {
      enabled = false;
      disableFocus();
      disableEffect();
      for (int i=0; i<elements.size(); i++) {
        elements.get(i).disableInternal();
      }
    } else {
      for (int i=0; i<elements.size(); i++) {
        elements.get(i).disableInternal();
      }
    }
  }

  public void disableFocus() {
    if (focusHandler.getKeyboardFocusElement() == this) {
      Element prevElement = focusHandler.getNext(this);
      prevElement.setFocus();
    }

    focusHandler.lostKeyboardFocus(Element.this);
    focusHandler.lostMouseFocus(Element.this);
  }

  void disableEffect() {
    stopEffectWithoutChildren(EffectEventId.onHover);
    stopEffectWithoutChildren(EffectEventId.onStartHover);
    stopEffectWithoutChildren(EffectEventId.onEndHover);
    stopEffectWithoutChildren(EffectEventId.onEnabled);
    startEffectWithoutChildren(EffectEventId.onDisabled);
    nifty.publishEvent(getId(), new ElementDisableEvent(this));
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
    show(null);
  }

  public void show(final EndNotify perform) {
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
    startEffect(EffectEventId.onShow, perform);
  }

  private void internalShow() {
    visible = true;
    effectManager.restoreForShow();

    for (int i=0; i<elements.size(); i++) {
      Element element = elements.get(i);
      element.internalShow();
    }

    nifty.publishEvent(getId(), new ElementShowEvent(this));
  }

  public void setVisible(final boolean visibleParam) {
    if (visibleParam) {
      show();
    } else {
      hide();
    }
  }

  /**
   * hide this element.
   */
  public void hide() {
    hide(null);
  }

  public void hide(final EndNotify perform) {
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
        resetForHide();
        internalHide();
        if (perform != null) {
          perform.perform();
        }
      }
    });
  }

  public void showWithoutEffects() {
    internalShow();
  }

  public void hideWithoutEffect() {
    // don't hide if not visible
    if (!isVisible()) {
      return;
    }

    resetEffects();
    internalHide();
  }

  private void internalHide() {
    visible = false;
    disableFocus();

    for (int i=0; i<elements.size(); i++) {
      Element element = elements.get(i);
      element.internalHide();
    }

    nifty.publishEvent(getId(), new ElementHideEvent(this));
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
  public boolean canHandleMouseEvents() {
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
    if (interactionBlocked) {
      return false;
    }
    if (!enabled) {
      return false;
    }
    if (isIgnoreMouseEvents()) {
      return false;
    }
    return true;
  }

  /**
   * This is a special version of canHandleMouseEvents() that will return true if this element is able to process
   * mouse events in general. This method will return true even when the element is temporarily not able to handle
   * events because onStartScreen or onEndScreen effects are active.
   * @return true can handle mouse events, false can't handle them
   */
  boolean canTheoreticallyHandleMouseEvents() {
//    if (isEffectActive(EffectEventId.onStartScreen)) {
//      return false;
//    }
//    if (isEffectActive(EffectEventId.onEndScreen)) {
//      return false;
//    }
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
//    if (interactionBlocked) {
//      return false;
//    }
    if (!enabled) {
      return false;
    }
    if (isIgnoreMouseEvents()) {
      return false;
    }
    return true;
  }

  /**
   * This should check if the mouse event is inside the current element and if it is
   * forward the event to it's child. The purpose of this is to build a list of all
   * elements from front to back that are available for a certain mouse position.
   */
  public void buildMouseOverElements(
      final NiftyMouseInputEvent mouseEvent,
      final long eventTime,
      final MouseOverHandler mouseOverHandler) {
    boolean isInside = isInside(mouseEvent);
    if (canHandleMouseEvents()) {
      if (isInside) {
        mouseOverHandler.addMouseOverElement(this);
      } else {
        mouseOverHandler.addMouseElement(this);
      }
    } else if (canTheoreticallyHandleMouseEvents()) {
      if (isInside) {
        mouseOverHandler.canTheoreticallyHandleMouse(this);
      }
    }
    if (visible) {
      for (int i=0; i<elements.size(); i++) {
        Element w = elements.get(i);
        w.buildMouseOverElements(mouseEvent, eventTime, mouseOverHandler);
      }
    }
  }

  public void mouseEventHoverPreprocess(final NiftyMouseInputEvent mouseEvent, final long eventTime) {
    effectManager.handleHoverDeactivate(this, mouseEvent.getMouseX(), mouseEvent.getMouseY());
  }

  /**
   * MouseEvent.
   * @param mouseEvent mouse event
   * @param eventTime event time
   */
  public boolean mouseEvent(final NiftyMouseInputEvent mouseEvent, final long eventTime) {
    mouseEventHover(mouseEvent);
    return interaction.process(mouseEvent, eventTime, isInside(mouseEvent), canHandleInteraction(), focusHandler.hasExclusiveMouseFocus(this));
  }

  private void mouseEventHover(final NiftyMouseInputEvent mouseEvent) {
    effectManager.handleHover(this, mouseEvent.getMouseX(), mouseEvent.getMouseY());
    effectManager.handleHoverStartAndEnd(this, mouseEvent.getMouseX(), mouseEvent.getMouseY());
  }

  /**
   * Handle the MouseOverEvent. Must not call child elements. This is handled by caller.
   * @param mouseEvent mouse event
   * @param eventTime event time
   * @return true the mouse event has been eated and false when the mouse event can be processed further down
   */
  public boolean mouseOverEvent(final NiftyMouseInputEvent mouseEvent, final long eventTime) {
    boolean eatMouseEvent = false;
    if (interaction.onMouseOver(this, mouseEvent)) {
      eatMouseEvent = true;
    }
    if ((mouseEvent.getMouseWheel() != 0) && interaction.onMouseWheel(this, mouseEvent)) {
      eatMouseEvent = true;
    }
    return eatMouseEvent;
  }

  /**
   * checks to see if the given mouse position is inside of this element.
   * @param inputEvent MouseInputEvent
   * @return true when inside, false otherwise
   */
  private boolean isInside(final NiftyMouseInputEvent inputEvent) {
    return isMouseInsideElement(inputEvent.getMouseX(), inputEvent.getMouseY());
  }

  public boolean isMouseInsideElement(final int mouseX, final int mouseY) {
    if (parentClipArea) {
      // must be inside the parent to continue
      if (isInsideParentClipArea(mouseX, mouseY)) {
          return
            mouseX >= getX() &&
            mouseX <= (getX() + getWidth()) &&
            mouseY > (getY()) &&
            mouseY < (getY() + getHeight());
        } else {
          return false;
        }
    } else {
      return
        mouseX >= getX() &&
        mouseX <= (getX() + getWidth()) &&
        mouseY > (getY()) &&
        mouseY < (getY() + getHeight());
    }
  }

  private boolean isInsideParentClipArea(final int mouseX, final int mouseY) {
    return mouseX >= parentClipX &&
        mouseX <= (parentClipX + parentClipWidth) &&
        mouseY > (parentClipY) &&
        mouseY < (parentClipY + parentClipHeight);
  }

  public void onClick() {
    if (canHandleInteraction()) {
      interaction.activate(nifty);
    }
  }

  private boolean canHandleInteraction() {
    return enabled && !screen.isEffectActive(EffectEventId.onStartScreen) && !screen.isEffectActive(EffectEventId.onEndScreen);
  }

  /**
   * find an element by name.
   * this method is deprecated, use findElementById() instead
   *
   * @param name the name of the element (id)
   * @return the element or null
   *
   * @see Element#findElementById(java.lang.String)
   */
  @Deprecated
  public Element findElementByName(final String id) {
      return findElementById(id);
  }

  /**
   * find an element by id.
   *
   * @param id the name of the element (id)
   * @return the element or null
   */
  public Element findElementById(final String findId) {
    if (findId == null) {
      return null;
    }

    if (id != null && id.equals(findId)) {
      return this;
    }

    if (childIdMatch(findId, id)) {
      return this;
    }

    for (int i=0; i<elements.size(); i++) {
      Element e = elements.get(i);
      Element found = e.findElementById(findId);
      if (found != null) {
        return found;
      }
    }

    return null;
  }

  private boolean childIdMatch(final String name, final String id) {
    if (name.startsWith("#")) {
      if (id != null && id.endsWith(name)) {
        return true;
      }
    }
    return false;
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

    for (int i=0; i<elements.size(); i++) {
      Element e = elements.get(i);
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

  private void bindToScreen(final Screen newScreen) {
    screen = newScreen;
    screen.registerElementId(id);
  }

  private void bindToFocusHandler(final boolean isPopup) {
    // not focusable means we won't add the element to the focus handler
    if (!focusable) {
      return;
    }

    // when this element is part of a popup (and this popup is not currently active) we don't add the element
    Element popupParent = resolvePopupParentElement();
    if (popupParent != null) {
      if (!isPopup) {
        return;
      }
    }

    focusHandler.addElement(this, screen.findElementById(focusableInsertBeforeElementId));
  }

  private Element resolvePopupParentElement() {
    if (elementType instanceof PopupType) {
      return this;
    }

    if (parent == null) {
      return null;
    }

    return parent.resolvePopupParentElement();
  }

  /**
   * On start screen event.
   */
  public void onStartScreen() {
    onStartScreenSubscribeControllerAnnotations();
    onStartScreenInternal();
  }

  private void onStartScreenSubscribeControllerAnnotations() {
    for (int i=0; i<elements.size(); i++) {
      Element e = elements.get(i);
      e.onStartScreenSubscribeControllerAnnotations();
    }
    if (attachedInputControl != null) {
      nifty.subscribeAnnotations(attachedInputControl.getController());
    }
  }

  private void onStartScreenInternal() {
    for (int i=0; i<elements.size(); i++) {
      Element e = elements.get(i);
      e.onStartScreenInternal();
    }
    if (attachedInputControl != null) {
      attachedInputControl.onStartScreen(nifty, screen);
    }
  }

  /**
   *
   * @param <T> the ElementRenderer type
   * @param requestedRendererClass the special ElementRenderer type to check for
   * @return the ElementRenderer that matches the class
   */
  public < T extends ElementRenderer > T getRenderer(final Class < T > requestedRendererClass) {
    for (int i=0; i<elementRenderer.length; i++) {
      ElementRenderer renderer = elementRenderer[i];
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
  public boolean keyEvent(final KeyboardInputEvent inputEvent) {
    if (attachedInputControl != null) {
      return attachedInputControl.keyEvent(nifty, inputEvent, id);
    }
    return false;
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

  public void setRenderOrder(final int renderOrder) {
    this.renderOrder = renderOrder;
    parent.renderOrderChanged(this);
  }

  private void renderOrderChanged(final Element element) {
    elementsRenderOrderSet.remove(element);
    elementsRenderOrderSet.add(element);
    elementsRenderOrder = elementsRenderOrderSet.toArray(new Element[0]);
  }

  public int getRenderOrder() {
    return renderOrder;
  }

  /**
   * Set the focus to this element.
   */
  public void setFocus() {
    if (nifty != null && nifty.getCurrentScreen() != null) {
      if (isFocusable()) {
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

  private boolean hasParentActiveOnStartOrOnEndScreenEffect() {
    if (parent != null) {
      return
        parent.effectManager.isActive(EffectEventId.onStartScreen) ||
        parent.effectManager.isActive(EffectEventId.onEndScreen) ||
        parent.hasParentActiveOnStartOrOnEndScreenEffect();
    }
    return false;
  }

  private void resetInteractionBlocked() {
    interactionBlocked = false;
    for (Element e : elements) {
      e.resetInteractionBlocked();
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
      if (effectEventId.equals(EffectEventId.onStartScreen) || effectEventId.equals(EffectEventId.onEndScreen)) {
        if (interactionBlocked &&
            !hasParentActiveOnStartOrOnEndScreenEffect() &&
            !isEffectActive(effectEventId)) {
          resetInteractionBlocked();
        }
      }

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

  public void bindControls(final Screen target) {
    if (target == null) {
      throw new IllegalArgumentException();
    }
    if (screen == target) {
      return;
    }
    bindToScreen(target);
    for (Element element : elements) {
      element.bindControls(target);
    }
    if (attachedInputControl != null) {
      attachedInputControl.bindControl(nifty, target, this, elementType.getAttributes());
    }
  }

  public void initControls(final boolean isPopup) {
    for (Element element : elements) {
      element.initControls(isPopup);
    }
    if (attachedInputControl != null) {
      attachedInputControl.initControl(elementType.getAttributes());
    }
    bindToFocusHandler(isPopup);
  }

  /**
   * remove this and all children from the focushandler.
   */
  public void removeFromFocusHandler() {
    focusHandler.remove(this);
    for (Element element : elements) {
      element.removeFromFocusHandler();
    }
  }

  public FocusHandler getFocusHandler() {
    return focusHandler;
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

    log.fine("after setStyle [" + newStyle + "]\n" + elementType.output(0));
    notifyListeners();
  }

  public String getStyle() {
    return elementType.getAttributes().get("style");
  }

  void removeStyle(final String style) {
    log.fine("before removeStyle [" + style + "]\n" + elementType.output(0));

    elementType.removeWithTag(style);
    effectManager.removeAllEffects();

    log.fine("after removeStyle [" + style + "]\n" + elementType.output(0));
    notifyListeners();
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

  /**
   * add additional input handler to this element or childs of the elements.
   * @param handler additiona handler
   */
  public void addPreInputHandler(final KeyInputHandler handler) {
    if (attachedInputControl != null) {
      attachedInputControl.addPreInputHandler(handler);
    }
    for (Element element : elements) {
      element.addPreInputHandler(handler);
    }
  }

  public < T extends Controller > T findControl(final String elementName, final Class < T > requestedControlClass) {
    Element element = findElementById(elementName);
    if (element == null) {
      return null;
    }
    return element.getControl(requestedControlClass);
  }

  public < T extends NiftyControl > T findNiftyControl(final String elementName, final Class < T > requestedControlClass) {
    Element element = findElementById(elementName);
    if (element == null) {
      return null;
    }
    return element.getNiftyControl(requestedControlClass);
  }

  public < T extends Controller > T getControl(final Class < T > requestedControlClass) {
    if (attachedInputControl != null) {
      T t = attachedInputControl.getControl(requestedControlClass);
      if (t != null) {
        return t;
      }
    }
    for (Element element : elements) {
      T t = element.getControl(requestedControlClass);
      if (t != null) {
        return t;
      }
    }
    return null;
  }

  public < T extends NiftyControl > T getNiftyControl(final Class < T > requestedControlClass) {
    if (attachedInputControl != null) {
      T t = attachedInputControl.getNiftyControl(requestedControlClass);
      if (t != null) {
        return t;
      }
    }
    for (Element element : elements) {
      T t = element.getNiftyControl(requestedControlClass);
      if (t != null) {
        return t;
      }
    }
    log.warning("missing element/control with id [" + getId() + "] for requested control class [" + requestedControlClass.getName() + "]");
    return NullObjectFactory.createNull(requestedControlClass);
  }

  /**
   * is focusable?
   * @return focusable
   */
  public boolean isFocusable() {
    return
        focusable &&
        enabled &&
        visible &&
        hasVisibleParent() &&
        !isIgnoreKeyboardEvents();
  }

  private boolean hasVisibleParent() {
    if (parent != null) {
      return parent.visible && parent.hasVisibleParent();
    }
    return true;
  }

  /**
   * Set onMouseOverMethod.
   * @param onMouseOverMethod new on mouse over method
   */
  public void setOnMouseOverMethod(final NiftyMethodInvoker onMouseOverMethod) {
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
   * Is this element visible to mouse events.
   * @return true visible and false not visible
   */
  public boolean isVisibleToMouseEvents() {
    return visibleToMouseEvents;
  }

  /**
   * get current left padding.
   * @return current left padding
   */
  public SizeValue getPaddingLeft() {
    return layoutPart.getBoxConstraints().getPaddingLeft();
  }

  /**
   * get current right padding.
   * @return current right padding
   */
  public SizeValue getPaddingRight() {
    return layoutPart.getBoxConstraints().getPaddingRight();
  }

  /**
   * get current top padding.
   * @return current top padding
   */
  public SizeValue getPaddingTop() {
    return layoutPart.getBoxConstraints().getPaddingTop();
  }

  /**
   * get current bottom padding.
   * @return current bottom padding
   */
  public SizeValue getPaddingBottom() {
    return layoutPart.getBoxConstraints().getPaddingBottom();
  }

  /**
   * get current left margin.
   * @return current left margin
   */
  public SizeValue getMarginLeft() {
    return layoutPart.getBoxConstraints().getMarginLeft();
  }

  /**
   * get current right margin.
   * @return current right margin
   */
  public SizeValue getMarginRight() {
    return layoutPart.getBoxConstraints().getMarginRight();
  }

  /**
   * get current top margin.
   * @return current top margin
   */
  public SizeValue getMarginTop() {
    return layoutPart.getBoxConstraints().getMarginTop();
  }

  /**
   * get current bottom margin.
   * @return current bottom margin
   */
  public SizeValue getMarginBottom() {
    return layoutPart.getBoxConstraints().getMarginBottom();
  }

  public void setPaddingLeft(final SizeValue paddingValue) {
    layoutPart.getBoxConstraints().setPaddingLeft(paddingValue);
    notifyListeners();
  }

  public void setPaddingRight(final SizeValue paddingValue) {
    layoutPart.getBoxConstraints().setPaddingRight(paddingValue);
    notifyListeners();
  }

  public void setPaddingTop(final SizeValue paddingValue) {
    layoutPart.getBoxConstraints().setPaddingTop(paddingValue);
    notifyListeners();
  }

  public void setPaddingBottom(final SizeValue paddingValue) {
    layoutPart.getBoxConstraints().setPaddingBottom(paddingValue);
    notifyListeners();
  }

  public void setMarginLeft(final SizeValue value) {
    layoutPart.getBoxConstraints().setMarginLeft(value);
    notifyListeners();
  }

  public void setMarginRight(final SizeValue value) {
    layoutPart.getBoxConstraints().setMarginRight(value);
    notifyListeners();
  }

  public void setMarginTop(final SizeValue value) {
    layoutPart.getBoxConstraints().setMarginTop(value);
    notifyListeners();
  }

  public void setMarginBottom(final SizeValue value) {
    layoutPart.getBoxConstraints().setMarginBottom(value);
    notifyListeners();
  }

  public String toString() {
    return id + " (" + super.toString() + ")";
  }

  public boolean isStarted() {
    return isEffectActive(EffectEventId.onStartScreen);
  }

  public void markForRemoval() {
    markForRemoval(null);
  }

  public void markForRemoval(final EndNotify endNotify) {
    nifty.removeElement(screen, this, endNotify);
  }

  public void markForMove(final Element destination) {
    markForMove(destination, null);
  }

  public void markForMove(final Element destination, final EndNotify endNotify) {
    nifty.moveElement(screen, this, destination, endNotify);
  }

  public void reactivate() {
    done = false;
    for (Element element : elements) {
      element.reactivate();
    }
  }

  private void notifyListeners() {
    nifty.publishEvent(id, this);
  }

  public Nifty getNifty() {
    return nifty;
  }

  public <T extends EffectImpl> List<Effect> getEffects(final EffectEventId effectEventId, final Class<T> requestedClass) {
    return effectManager.getEffects(effectEventId, requestedClass);
  }

  public void onEndScreen(final Screen screen) {
    screen.unregisterElementId(id);

    if (attachedInputControl != null) {
      attachedInputControl.onEndScreen(nifty, screen, id);
    }
    for (Element element : elements) {
      element.onEndScreen(screen);
    }
  }

  public ElementInteraction getElementInteraction() {
    return interaction;
  }

  public void setIgnoreMouseEvents(final boolean newValue) {
    ignoreMouseEvents = newValue;
    if (newValue) {
      focusHandler.lostMouseFocus(this);
    }
  }

  public boolean isIgnoreMouseEvents() {
    return ignoreMouseEvents;
  }

  public void setIgnoreKeyboardEvents(final boolean newValue) {
    ignoreKeyboardEvents = newValue;
    if (newValue) {
      focusHandler.lostKeyboardFocus(this);
    }
  }

  public boolean isIgnoreKeyboardEvents() {
    return ignoreKeyboardEvents;
  }

  // EffectManager.Notify implementation

  @Override
  public void effectStateChanged(final EffectEventId eventId, final boolean active) {
    // Get the oldState first.
    boolean oldState = effectStateCache.get(eventId);

    // The given EffectEventId changed its state. This means we now must update
    // the ElementEffectStatetCache for this element. We do this by recalculating
    // our state taking the state of all child elements into account.
    boolean newState = isEffectActiveRecalc(eventId);

    // When our state has been changed due to the update we will update the cache
    // and tell our parent element to update as well.
    if (newState != oldState) {
      effectStateCache.set(eventId, newState);

      if (parent != null) {
        parent.effectStateChanged(eventId, newState);
      }
    }
  }

  private boolean isEffectActiveRecalc(final EffectEventId eventId) {
    for (int i=0; i<elements.size(); i++) {
      Element w = elements.get(i);
      if (w.isEffectActiveRecalc(eventId)) {
        return true;
      }
    }
    return effectManager.isActive(eventId);
  }

  // package private to prevent public access
  void internalRemoveElement(final Element element) {
    // so now that's odd: we need to remove the element first from the
    // elementsRenderOrder and THEN from the elements list. this is because
    // the elementsRenderOrder comparator uses the index of the element in
    // the elements list >_<
    //
    // the main issue here is of course the splitted data structure. something
    // we need to adress in 1.4 or 2.0.
    elementsRenderOrderSet.remove(element);

    // now that the element has been removed from the elementsRenderOrder set
    // we can remove it from the elements list as well.
    elements.remove(element);

    elementsRenderOrder = elementsRenderOrderSet.toArray(new Element[0]);
  }

  // package private to prevent public access
  void internalRemoveElementWithChilds() {
    Iterator < Element > elementIt = elements.iterator();
    while (elementIt.hasNext()) {
      Element el = elementIt.next();
      el.internalRemoveElementWithChilds();
    }

    elementsRenderOrderSet.clear();
    elements.clear();
    elementsRenderOrder = elementsRenderOrderSet.toArray(new Element[0]);
  }
}
