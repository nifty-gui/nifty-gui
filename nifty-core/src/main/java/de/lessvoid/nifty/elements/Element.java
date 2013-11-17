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
 * @author void
 */
public class Element implements NiftyEvent, EffectManager.Notify {
  private static Logger log = Logger.getLogger(Element.class.getName());
  private ElementType elementType;
  private String id;
  private int renderOrder;
  private Element parent;
  private List<Element> children = new ArrayList<Element>(0);

  /**
   * This set defines the render order of the child elements using a Comparator.
   */
  private Set<Element> elementsRenderOrderSet = new TreeSet<Element>(new Comparator<Element>() {

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
      return children.indexOf(element);
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
   * The LayoutPart for laying out this element.
   */
  private LayoutPart layoutPart;

  /**
   * The ElementRenderer we should use to render this element.
   */
  private ElementRenderer[] elementRenderer = new ElementRenderer[0];

  private EffectManager effectManager;
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
   * Whether the element is enabled or not.
   */
  private boolean enabled;

  /**
   * This helps us to keep track when you enable or disable this multiple times. We don't want
   * to start the onEnabled/onDisabled effects when the element is already enabled/disabled.
   */
  private int enabledCount;

  /**
   * Whether the element is visible or not.
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
   * Whether the element is visible to mouse events.
   */
  private boolean visibleToMouseEvents;

  private boolean clipChildren;

  /**
   * The attached control when this element is a control.
   */
  private NiftyInputControl attachedInputControl = null;

  /**
   * Whether this element can be focused or not.
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
   * The screen this element is part of.
   */
  private Screen screen;

  private TimeProvider time;
  private List<String> elementDebugOut = new ArrayList<String>();
  private StringBuilder elementDebug = new StringBuilder();
  private boolean parentClipArea = false;
  private int parentClipX;
  private int parentClipY;
  private int parentClipWidth;
  private int parentClipHeight;

  /*
   * Whether or not this element should ignore all mouse events.
   */
  private boolean ignoreMouseEvents;

  /*
   * Whether or not this element should ignore all keyboard events.
   */
  private boolean ignoreKeyboardEvents;

  private static Convert convert = new Convert();
  private static Map < Class < ? extends ElementRenderer >, ApplyRenderer > rendererApplier = new HashMap < Class < ? extends ElementRenderer>, ApplyRenderer >();
  {
    rendererApplier.put(TextRenderer.class, new ApplyRenderText(convert));
    rendererApplier.put(ImageRenderer.class, new ApplyRendererImage(convert));
    rendererApplier.put(PanelRenderer.class, new ApplyRendererPanel(convert));
  }

  private Map<String, Object> userData;

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
   * Use this constructor to specify a LayoutPart
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
  public void initializeFromAttributes(final Screen targetScreen, final Attributes attributes, final NiftyRenderEngine renderEngine) {
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
      rendererApply.apply(targetScreen, this, attributes, renderEngine);
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
    for (int i=0; i< children.size(); i++) {
      Element element = children.get(i);
      element.hideWithChildren();
    }
  }

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

  public String getId() {
    return id;
  }

  public Element getParent() {
    return parent;
  }

  public void setParent(final Element element) {
    parent = element;

    // This element has a new parent. Check the parent's clip area and update this element accordingly.
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
      renderOrder.append("[" + e.getId() + " (" + ((e.renderOrder == 0) ? children.indexOf(e) : e.renderOrder) + ")]");
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

  private String outputSizeValue(final SizeValue value) {
    if (value == null) {
      return "null";
    } else {
      return value.toString();
    }
  }

  /**
   * Gets the x location of the top left corner of this element.
   */
  public int getX() {
    return layoutPart.getBox().getX();
  }

  /**
   * Gets the y location of the top left corner of this element.
   */
  public int getY() {
    return layoutPart.getBox().getY();
  }

  public int getHeight() {
    return layoutPart.getBox().getHeight();
  }

  public int getWidth() {
    return layoutPart.getBox().getWidth();
  }

  public void setHeight(int height) {
    layoutPart.getBox().setHeight(height);
  }

  public void setWidth(int width) {
    layoutPart.getBox().setWidth(width);
  }

  /**
   * @deprecated Use {@link #getChildren()}
   */
  public List<Element> getElements() {
    return Collections.unmodifiableList(children);
  }

  public List<Element> getChildren() {
    return Collections.unmodifiableList(children);
  }

  /**
   * Get all children, children of children, etc, recursively.
   *
   * @return an iterator that will traverse the element's entire tree downward.
   */
  public Iterator<Element> getDescendants() {
    return new ElementTreeTraverser(this);
  }

  /**
   * @deprecated Use {@link #addChild(Element)} instead.
   *
   * Adds a child element to the end of the list of this element's children.
   */
  public void add(final Element child) {
    addChild(child);
  }

  /**
   * @deprecated Use {@link #insertChild(Element, int)} instead.
   *
   * Inserts a child element at the specified index in this element's list of children.
   */
  public void add(final Element child, final int index) {
    insertChild(child, index);
  }

  /**
   * Adds a child element to the end of the list of this element's children.
   */
  public void addChild(final Element child) {
    insertChild(child, children.size());
	}

  /**
   * Inserts a child element at the specified index in this element's list of children.
   */
   public void insertChild(final Element child, final int index) {
	    children.add (index, child);
	    elementsRenderOrderSet.add(child);
	    elementsRenderOrder = elementsRenderOrderSet.toArray(new Element[0]);
   }

   /**
    * Set the index of this element in it's parent's list of children.
    */
   public void setIndex(final int index) {
	  int curInd = this.parent.children.indexOf(this);
	  if(curInd>=0 && index !=curInd){
		  this.parent.children.remove(curInd);
		  this.parent.children.add (index, this);
		  this.parent.layoutElements();
	  }
   }

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

  public void setLayoutManager(final LayoutManager newLayout) {
    this.layoutManager = newLayout;
  }

  public void resetLayout() {

    TextRenderer textRenderer = getRenderer(TextRenderer.class);
    if (textRenderer != null) {
      textRenderer.resetLayout(this);
    }

    for (int i=0; i< children.size(); i++) {
      Element e = children.get(i);
      e.resetLayout();
    }
  }

  private void preProcessConstraintWidth() {
    for (int i=0; i< children.size(); i++) {
      Element e = children.get(i);
      e.preProcessConstraintWidth();
    }

    preProcessConstraintWidthThisLevel();
  }

  private void preProcessConstraintWidthThisLevel() {
    // This is either the original width value, or a value we set here.
    SizeValue myWidth = getConstraintWidth();
    
    if (layoutManager != null) {

      // unset width, or width="sum" or width="max"
      // try to calculate the width constraint using the children
      // but only if all child elements have a fixed pixel width.
      
      // The difference between an unset width and width="sum" is that the latter does not fail if
      // there are values that are not pixel, it simply considers them to be "0".
      // width="sum" also simply sums the width values, it does not care about the layout manager.
      
      if (myWidth == null || myWidth.hasDefault()) {
        List<LayoutPart> layoutPartChild = getLayoutChildrenWithIndependentWidth();

        // if all (!) child elements have a pixel fixed width we can calculate a new width constraint for this element!
        if (!layoutPartChild.isEmpty() && children.size() == layoutPartChild.size()) {
          
          SizeValue newWidth = layoutManager.calculateConstraintWidth(this.layoutPart, layoutPartChild);
          if (newWidth != null && newWidth.hasValue()) {
            int newWidthPx = newWidth.getValueAsInt(0);
            newWidthPx += this.layoutPart.getBoxConstraints().getPaddingTop().getValueAsInt(newWidth.getValueAsInt(newWidthPx));
            newWidthPx += this.layoutPart.getBoxConstraints().getPaddingBottom().getValueAsInt(newWidth.getValueAsInt(newWidthPx));
            setConstraintWidth(SizeValue.def(newWidthPx));
          }
        } else {
            setConstraintWidth(SizeValue.def());
        }
        
      } else if (myWidth.hasSum()) {
        List<LayoutPart> layoutPartChild = getLayoutChildrenWithIndependentWidth();
        SizeValue newWidth = layoutPart.getSumWidth(layoutPartChild);
        if (newWidth != null && newWidth.hasValue()) {
          int newWidthPx = newWidth.getValueAsInt(0);
          newWidthPx += this.layoutPart.getBoxConstraints().getPaddingTop().getValueAsInt(newWidth.getValueAsInt(newWidthPx));
          newWidthPx += this.layoutPart.getBoxConstraints().getPaddingBottom().getValueAsInt(newWidth.getValueAsInt(newWidthPx));
          setConstraintWidth(SizeValue.sum(newWidthPx));
        } else {
          setConstraintWidth(SizeValue.sum(0));
        }
        
      } else if (myWidth.hasMax()) {
        List<LayoutPart> layoutPartChild = getLayoutChildrenWithIndependentWidth();
        SizeValue newWidth = layoutPart.getMaxWidth(layoutPartChild);
        if (newWidth != null && newWidth.hasValue()) {
          int newWidthPx = newWidth.getValueAsInt(0);
          newWidthPx += this.layoutPart.getBoxConstraints().getPaddingTop().getValueAsInt(newWidth.getValueAsInt(newWidthPx));
          newWidthPx += this.layoutPart.getBoxConstraints().getPaddingBottom().getValueAsInt(newWidth.getValueAsInt(newWidthPx));
          setConstraintWidth(SizeValue.max(newWidthPx));
        } else {
          setConstraintWidth(SizeValue.max(0));
        }
        
      }
    }
  }
  
  private List<LayoutPart> getLayoutChildrenWithIndependentWidth() {
    List < LayoutPart > layoutPartChild = new ArrayList<LayoutPart>(children.size());
    for (int i=0; i< children.size(); i++) {
      Element e = children.get(i);
      SizeValue childWidth = e.getConstraintWidth();
      if (childWidth.isPixel() && childWidth.isIndependentFromParent()) {
        layoutPartChild.add(e.layoutPart);
      }
    }
    return layoutPartChild;
  }

  private void preProcessConstraintHeight() {
    for (int i=0; i< children.size(); i++) {
      Element e = children.get(i);
      e.preProcessConstraintHeight();
    }

    preProcessConstraintHeightThisLevel();
  }

  private void preProcessConstraintHeightThisLevel() {
    // This is either the original height value, or a value we set here.
    SizeValue myHeight = getConstraintHeight();
    
    if (layoutManager != null) {

      // unset height, or height="sum" or height="max"
      // try to calculate the height constraint using the children
      // but only if all child elements have a fixed pixel height.
      
      // The difference between an unset height and height="sum" is that the latter does not fail if
      // there are values that are not pixel, it simply considers them to be "0".
      // height="sum" also simply sums the height values, it does not care about the layout manager.
      
      if (myHeight == null || myHeight.hasDefault()) {
        List<LayoutPart> layoutPartChild = getLayoutChildrenWithIndependentHeight();

        // if all (!) child elements have a pixel fixed height we can calculate a new height constraint for this element!
        if (!layoutPartChild.isEmpty() && children.size() == layoutPartChild.size()) {
          
          SizeValue newHeight = layoutManager.calculateConstraintHeight(this.layoutPart, layoutPartChild);
          if (newHeight != null && newHeight.hasValue()) {
            int newHeightPx = newHeight.getValueAsInt(0);
            newHeightPx += this.layoutPart.getBoxConstraints().getPaddingTop().getValueAsInt(newHeight.getValueAsInt(newHeightPx));
            newHeightPx += this.layoutPart.getBoxConstraints().getPaddingBottom().getValueAsInt(newHeight.getValueAsInt(newHeightPx));
            setConstraintHeight(SizeValue.def(newHeightPx));
          }
        } else {
            setConstraintHeight(SizeValue.def());
        }
        
      } else if (myHeight.hasSum()) {
        List<LayoutPart> layoutPartChild = getLayoutChildrenWithIndependentHeight();
        SizeValue newHeight = layoutPart.getSumHeight(layoutPartChild);
        if (newHeight != null && newHeight.hasValue()) {
          int newHeightPx = newHeight.getValueAsInt(0);
          newHeightPx += this.layoutPart.getBoxConstraints().getPaddingTop().getValueAsInt(newHeight.getValueAsInt(newHeightPx));
          newHeightPx += this.layoutPart.getBoxConstraints().getPaddingBottom().getValueAsInt(newHeight.getValueAsInt(newHeightPx));
          setConstraintHeight(SizeValue.sum(newHeightPx));
        } else {
          setConstraintHeight(SizeValue.sum(0));
        }
        
      } else if (myHeight.hasMax()) {
        List<LayoutPart> layoutPartChild = getLayoutChildrenWithIndependentHeight();
        SizeValue newHeight = layoutPart.getMaxHeight(layoutPartChild);
        if (newHeight != null && newHeight.hasValue()) {
          int newHeightPx = newHeight.getValueAsInt(0);
          newHeightPx += this.layoutPart.getBoxConstraints().getPaddingTop().getValueAsInt(newHeight.getValueAsInt(newHeightPx));
          newHeightPx += this.layoutPart.getBoxConstraints().getPaddingBottom().getValueAsInt(newHeight.getValueAsInt(newHeightPx));
          setConstraintHeight(SizeValue.max(newHeightPx));
        } else {
          setConstraintHeight(SizeValue.max(0));
        }
        
      }
    }
  }
  
  private List<LayoutPart> getLayoutChildrenWithIndependentHeight() {
    List < LayoutPart > layoutPartChild = new ArrayList<LayoutPart>(children.size());
    for (int i=0; i< children.size(); i++) {
      Element e = children.get(i);
      SizeValue childHeight = e.getConstraintHeight();
      if (childHeight.isPixel() && childHeight.isIndependentFromParent()) {
        layoutPartChild.add(e.layoutPart);
      }
    }
    return layoutPartChild;
  }

  private void processLayoutInternal() {
    for (int i=0; i< children.size(); i++) {
      Element w = children.get(i);
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
      for (int i=0; i< children.size(); i++) {
        Element w = children.get(i);
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
      for (int i=0; i< children.size(); i++) {
        Element w = children.get(i);
        w.processLayout();
      }
    }

    if (clipChildren) {
      for (int i=0; i< children.size(); i++) {
        Element w = children.get(i);
        w.setParentClipArea(getX(), getY(), getWidth(), getHeight());
      }
    }
  }

  public void layoutElements() {
    prepareLayout();
    processLayout();
    
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

    for (int i=0; i< children.size(); i++) {
      Element w = children.get(i);
      w.setParentClipArea(parentClipX, parentClipY, parentClipWidth, parentClipHeight);
    }
  }

  public void resetEffects() {
    effectManager.reset();
    for (int i=0; i< children.size(); i++) {
      Element w = children.get(i);
      w.resetEffects();
    }
  }

  public void resetAllEffects() {
    effectManager.resetAll();
    for (int i=0; i< children.size(); i++) {
      Element w = children.get(i);
      w.resetAllEffects();
    }
  }

  public void resetForHide() {
    effectManager.resetForHide();
    for (int i=0; i< children.size(); i++) {
      Element w = children.get(i);
      w.resetForHide();
    }
  }

  public void resetSingleEffect(final EffectEventId effectEventId) {
    effectManager.resetSingleEffect(effectEventId);
    for (int i=0; i< children.size(); i++) {
      Element w = children.get(i);
      w.resetSingleEffect(effectEventId);
    }
  }

  public void resetSingleEffect(final EffectEventId effectEventId, final String customKey) {
    effectManager.resetSingleEffect(effectEventId, customKey);
    for (int i=0; i< children.size(); i++) {
      Element w = children.get(i);
      w.resetSingleEffect(effectEventId, customKey);
    }
  }

  public void resetMouseDown() {
    interaction.resetMouseDown();
    for (int i=0; i< children.size(); i++) {
      Element w = children.get(i);
      w.resetMouseDown();
    }
  }

  public void setConstraintX(final SizeValue newX) {
    layoutPart.getBoxConstraints().setX(newX);
    notifyListeners();
  }

  public void setConstraintY(final SizeValue newY) {
    layoutPart.getBoxConstraints().setY(newY);
    notifyListeners();
  }

  public void setConstraintWidth(final SizeValue newWidth) {
    layoutPart.getBoxConstraints().setWidth(newWidth);
    notifyListeners();
  }

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

  public SizeValue getConstraintWidth() {
    return layoutPart.getBoxConstraints().getWidth();
  }

  public SizeValue getConstraintHeight() {
    return layoutPart.getBoxConstraints().getHeight();
  }

  public void setConstraintHorizontalAlign(final HorizontalAlign newHorizontalAlign) {
    layoutPart.getBoxConstraints().setHorizontalAlign(newHorizontalAlign);
  }

  public void setConstraintVerticalAlign(final VerticalAlign newVerticalAlign) {
    layoutPart.getBoxConstraints().setVerticalAlign(newVerticalAlign);
  }

  public HorizontalAlign getConstraintHorizontalAlign() {
    return layoutPart.getBoxConstraints().getHorizontalAlign();
  }

  public VerticalAlign getConstraintVerticalAlign() {
    return layoutPart.getBoxConstraints().getVerticalAlign();
  }

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
      for (int i=0; i< children.size(); i++) {
        Element w = children.get(i);
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
    for (int i=0; i< children.size(); i++) {
      Element w = children.get(i);
      w.startEffectInternal(effectEventId, forwardToSelf, customKey);
    }

    if (effectEventId == EffectEventId.onFocus) {
      if (attachedInputControl != null) {
        attachedInputControl.onFocus(true);
      }
    }
  }

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
      for (int i=0; i< children.size(); i++) {
        Element w = children.get(i);
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
   * Checks if a certain effect is still active in this element or any of its children.
   *
   * @return true if the effect has ended and false otherwise
   */
  public boolean isEffectActive(final EffectEventId effectEventId) {
    return effectStateCache.get(effectEventId);
  }

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
      for (int i=0; i< children.size(); i++) {
        children.get(i).enableInternal();
      }
    } else {
      for (int i=0; i< children.size(); i++) {
        children.get(i).enableInternal();
      }
    }
  }

  void enableEffect() {
    stopEffectWithoutChildren(EffectEventId.onDisabled);
    startEffectWithoutChildren(EffectEventId.onEnabled);
    nifty.publishEvent(getId(), new ElementEnableEvent(this));
  }

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
      for (int i=0; i< children.size(); i++) {
        children.get(i).disableInternal();
      }
    } else {
      for (int i=0; i< children.size(); i++) {
        children.get(i).disableInternal();
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

  public boolean isEnabled() {
    return enabled;
  }

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

    for (int i=0; i< children.size(); i++) {
      Element element = children.get(i);
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
      @Override
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

    for (int i=0; i< children.size(); i++) {
      Element element = children.get(i);
      element.internalHide();
    }

    nifty.publishEvent(getId(), new ElementHideEvent(this));
  }

  public boolean isVisible() {
    return visible;
  }

  public void setHotSpotFalloff(final Falloff newFalloff) {
    effectManager.setFalloff(newFalloff);
  }

  public Falloff getFalloff() {
    return effectManager.getFalloff();
  }

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
      for (int i=0; i< children.size(); i++) {
        Element w = children.get(i);
        w.buildMouseOverElements(mouseEvent, eventTime, mouseOverHandler);
      }
    }
  }

  public void mouseEventHoverPreprocess(final NiftyMouseInputEvent mouseEvent, final long eventTime) {
    effectManager.handleHoverDeactivate(this, mouseEvent.getMouseX(), mouseEvent.getMouseY());
  }

  public boolean mouseEvent(final NiftyMouseInputEvent mouseEvent, final long eventTime) {
    mouseEventHover(mouseEvent);
    return interaction.process(mouseEvent, eventTime, isInside(mouseEvent), canHandleInteraction(), focusHandler.hasExclusiveMouseFocus(this));
  }

  private void mouseEventHover(final NiftyMouseInputEvent mouseEvent) {
    effectManager.handleHover(this, mouseEvent.getMouseX(), mouseEvent.getMouseY());
    effectManager.handleHoverStartAndEnd(this, mouseEvent.getMouseX(), mouseEvent.getMouseY());
  }

  /**
   * Handles the MouseOverEvent. Must not call child elements. This is handled by the caller.
   *
   * @return true the mouse event has been consumed and false when the mouse event can be processed further down
   */
  public boolean mouseOverEvent(final NiftyMouseInputEvent mouseEvent, final long eventTime) {
    boolean isMouseEventConsumed = false;
    if (interaction.onMouseOver(this, mouseEvent)) {
      isMouseEventConsumed = true;
    }
    if ((mouseEvent.getMouseWheel() != 0) && interaction.onMouseWheel(this, mouseEvent)) {
      isMouseEventConsumed = true;
    }
    return isMouseEventConsumed;
  }

  /**
   * Checks to see if the given mouse position is inside of this element.
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
   * @deprecated Please use {@link #findElementById(String)}.
   *
   * @param name the name (id) of the element
   * @return the element or null if an element with the specified name cannot be found
   */
  @Deprecated
  public Element findElementByName(final String name) {
      return findElementById(name);
  }

  /**
   * @return the element or null if an element with the specified id cannot be found
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

    for (int i=0; i< children.size(); i++) {
      Element e = children.get(i);
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

  public void setOnClickAlternateKey(final String newAlternateKey) {
    interaction.setAlternateKey(newAlternateKey);
  }

  public void setAlternateKey(final String alternateKey) {
    effectManager.setAlternateKey(alternateKey);

    for (int i=0; i< children.size(); i++) {
      Element e = children.get(i);
      e.setAlternateKey(alternateKey);
    }
  }

  public EffectManager getEffectManager() {
    return effectManager;
  }

  public void setEffectManager(final EffectManager effectManagerParam) {
    effectManager = effectManagerParam;
  }

  private void bindToScreen(final Screen newScreen) {
    screen = newScreen;
    screen.registerElementId(id);
  }

  private void bindToFocusHandler(final boolean isPopup) {
    if (!focusable) {
      return;
    }

    if (hasAncestorPopup() && !isPopup)
    {
      return;
    }

    focusHandler.addElement(this, screen.findElementById(focusableInsertBeforeElementId));
  }

  private boolean hasAncestorPopup() {
    return findAncestorPopupElement() != null;
  }

  private Element findAncestorPopupElement() {
    if (elementType instanceof PopupType) {
      return this;
    }

    if (parent == null) {
      return null;
    }

    return parent.findAncestorPopupElement();
  }

  public void onStartScreen() {
    onStartScreenSubscribeControllerAnnotations();
    onStartScreenInternal();
  }

  private void onStartScreenSubscribeControllerAnnotations() {
    for (int i=0; i< children.size(); i++) {
      Element e = children.get(i);
      e.onStartScreenSubscribeControllerAnnotations();
    }
    if (attachedInputControl != null) {
      nifty.subscribeAnnotations(attachedInputControl.getController());
    }
  }

  private void onStartScreenInternal() {
    for (int i=0; i< children.size(); i++) {
      Element e = children.get(i);
      e.onStartScreenInternal();
    }
    if (attachedInputControl != null) {
      attachedInputControl.onStartScreen(nifty, screen);
    }
  }

  /**
   * Gets this element's renderer matching the specified class.
   *
   * @param <T> the {@link de.lessvoid.nifty.elements.render.ElementRenderer} type to check for
   * @param requestedRendererClass the {@link de.lessvoid.nifty.elements.render.ElementRenderer} class to check for
   * @return the {@link de.lessvoid.nifty.elements.render.ElementRenderer} that matches the specified class, or null if
   * there is no matching renderer
   */
  public <T extends ElementRenderer> T getRenderer(final Class <T> requestedRendererClass) {
    for (int i=0; i<elementRenderer.length; i++) {
      ElementRenderer renderer = elementRenderer[i];
      if (requestedRendererClass.isInstance(renderer)) {
        return requestedRendererClass.cast(renderer);
      }
    }
    return null;
  }

  public void setVisibleToMouseEvents(final boolean newVisibleToMouseEvents) {
    this.visibleToMouseEvents = newVisibleToMouseEvents;
  }

  public boolean keyEvent(final KeyboardInputEvent inputEvent) {
    if (attachedInputControl != null) {
      return attachedInputControl.keyEvent(nifty, inputEvent, id);
    }
    return false;
  }

  public void setClipChildren(final boolean clipChildrenParam) {
    this.clipChildren = clipChildrenParam;
  }

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
   * Attempts to set the focus to this element.
   */
  public void setFocus() {
    if (nifty != null && nifty.getCurrentScreen() != null) {
      if (isFocusable()) {
        focusHandler.setKeyFocus(this);
      }
    }
  }

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
    for (Element e : children) {
      e.resetInteractionBlocked();
    }
  }

  /**
   * @author void
   */
  public class LocalEndNotify implements EndNotify {
    private EffectEventId effectEventId;
    private EndNotify effectEndNotiy;

    public LocalEndNotify(final EffectEventId effectEventIdParam, final EndNotify effectEndNotiyParam) {
      effectEventId = effectEventIdParam;
      effectEndNotiy = effectEndNotiyParam;
    }

    @Override
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

  public void setId(final String newId) {
    this.id = newId;
  }

  public ElementType getElementType() {
    return elementType;
  }

  public ElementRenderer[] getElementRenderer() {
    return elementRenderer;
  }

  public void setFocusable(final boolean isFocusable) {
    this.focusable = isFocusable;
    for (Element e : children) {
      e.setFocusable(isFocusable);
    }
  }

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
    for (Element element : children) {
      element.bindControls(target);
    }
    if (attachedInputControl != null) {
      attachedInputControl.bindControl(nifty, target, this, elementType.getAttributes());
    }
  }

  public void initControls(final boolean isPopup) {
    for (Element element : children) {
      element.initControls(isPopup);
    }
    if (attachedInputControl != null) {
      attachedInputControl.initControl(elementType.getAttributes());
    }
    bindToFocusHandler(isPopup);
  }

  /**
   * Removes this element and all of its children from the focusHandler.
   */
  public void removeFromFocusHandler() {
    focusHandler.remove(this);
    for (Element element : children) {
      element.removeFromFocusHandler();
    }
  }

  public FocusHandler getFocusHandler() {
    return focusHandler;
  }

  public void setStyle(final String newStyle) {
    removeStyle(elementType.getAttributes().get("style"));

    elementType.getAttributes().set("style", newStyle);
    elementType.applyStyles(nifty.getDefaultStyleResolver());
    elementType.applyAttributes(this.screen, this, elementType.getAttributes(), nifty.getRenderEngine());
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
   * Adds an additional input handler to this element and its children.
   *
   * @param handler additional handler
   */
  public void addInputHandler(final KeyInputHandler handler) {
    if (attachedInputControl != null) {
      attachedInputControl.addInputHandler(handler);
    }
    for (Element element : children) {
      element.addInputHandler(handler);
    }
  }

  /**
   * Adds an additional input handler to this element and its children.
   *
   * @param handler additional handler
   */
  public void addPreInputHandler(final KeyInputHandler handler) {
    if (attachedInputControl != null) {
      attachedInputControl.addPreInputHandler(handler);
    }
    for (Element element : children) {
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
    for (Element element : children) {
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
    for (Element element : children) {
      T t = element.getNiftyControl(requestedControlClass);
      if (t != null) {
        return t;
      }
    }
    log.warning("missing element/control with id [" + getId() + "] for requested control class [" + requestedControlClass.getName() + "]");
    return NullObjectFactory.createNull(requestedControlClass);
  }

  public boolean isFocusable() {
    return focusable &&
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

  public void setOnMouseOverMethod(final NiftyMethodInvoker onMouseOverMethod) {
    this.interaction.setOnMouseOver(onMouseOverMethod);
  }

  public LayoutPart getLayoutPart() {
    return layoutPart;
  }

  public boolean isVisibleToMouseEvents() {
    return visibleToMouseEvents;
  }

  public SizeValue getPaddingLeft() {
    return layoutPart.getBoxConstraints().getPaddingLeft();
  }

  public SizeValue getPaddingRight() {
    return layoutPart.getBoxConstraints().getPaddingRight();
  }

  public SizeValue getPaddingTop() {
    return layoutPart.getBoxConstraints().getPaddingTop();
  }

  public SizeValue getPaddingBottom() {
    return layoutPart.getBoxConstraints().getPaddingBottom();
  }

  public SizeValue getMarginLeft() {
    return layoutPart.getBoxConstraints().getMarginLeft();
  }

  public SizeValue getMarginRight() {
    return layoutPart.getBoxConstraints().getMarginRight();
  }

  public SizeValue getMarginTop() {
    return layoutPart.getBoxConstraints().getMarginTop();
  }

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
    for (Element element : children) {
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
    nifty.unsubscribeElement(screen, id);

    if (attachedInputControl != null) {
      attachedInputControl.onEndScreen(nifty, screen, id);
    }
    for (Element element : children) {
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
    for (int i=0; i< children.size(); i++) {
      Element w = children.get(i);
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
    children.remove (element);

    elementsRenderOrder = elementsRenderOrderSet.toArray(new Element[0]);
  }

  // package private to prevent public access
  void internalRemoveElementWithChilds() {
    Iterator < Element > elementIt = children.iterator();
    while (elementIt.hasNext()) {
      Element el = elementIt.next();
      el.internalRemoveElementWithChilds();
    }

    elementsRenderOrderSet.clear();
    children.clear();
    elementsRenderOrder = elementsRenderOrderSet.toArray(new Element[0]);
  }

  /**
   * Sets custom user data for this element.
   *
   * @param key the key for the object to set
   */
  public void setUserData(String key, Object data) {
    if (userData == null) {
      userData = new HashMap<String, Object>();
    }
    userData.put(key, data);
  }

  /**
   * Gets custom user data set with {@link #setUserData(String, Object)} by key
   */
  @SuppressWarnings("unchecked")
  public <T> T getUserData(String key) {
    if (userData == null) {
      return null;
    }
    return (T) userData.get(key);
  }

  /**
   * Gets all custom user data keys set with {@link #setUserData(String, Object)}
   */
  public Set<String> getUserDataKeys() {
    if (userData != null) {
      return userData.keySet();
    }
    return Collections.emptySet();
  }

}
