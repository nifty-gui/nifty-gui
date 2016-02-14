package de.lessvoid.nifty.elements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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
import de.lessvoid.nifty.layout.BoxConstraints;
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
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.xml.xpp3.Attributes;
import java.util.regex.Pattern;
import org.bushe.swing.event.EventTopicSubscriber;

/**
 * @author void
 */
public class Element implements NiftyEvent, EffectManager.Notify {
  @Nonnull
  private static final Logger log = Logger.getLogger(Element.class.getName());
  @Nonnull
  private final ElementType elementType;
  @Nullable
  private String id;
  private int renderOrder;
  @Nullable
  private Element parent;
  @Nullable
  private List<Element> children;

  /**
   * This set defines the render order of the child elements using a Comparator.
   */
  @Nullable
  private Set<Element> elementsRenderOrderSet;

  /**
   * This is the shared instance of the comparator used to get the rendering oder for the child elements of another
   * element.
   */
  @Nonnull
  private static final Comparator<Element> RENDER_ORDER_COMPARATOR = new RenderOrderComparator();

  /**
   * We keep a copy of the elementsRenderOrderSet in a simple array for being more GC friendly while rendering.
   */
  @Nullable
  private Element[] elementsRenderOrder;

  /**
   * The LayoutManager we should use for all child elements.
   */
  @Nullable
  private LayoutManager layoutManager;

  /**
   * The LayoutPart for laying out this element.
   */
  @Nonnull
  private final LayoutPart layoutPart;

  /**
   * The ElementRenderer we should use to render this element.
   */
  @Nonnull
  private final ElementRenderer[] elementRenderer;

  @Nonnull
  private EffectManager effectManager;
  @Nonnull
  private ElementInteraction interaction;

  /**
   * Effect state cache (this includes info about the child state) and is only
   * update when Effect states are changed.
   */
  @Nonnull
  private final ElementEffectStateCache effectStateCache = new ElementEffectStateCache();

  /**
   * Nifty instance this element is attached to.
   */
  @Nonnull
  private final Nifty nifty;

  /**
   * The focus handler this element is attached to.
   */
  @Nonnull
  private final FocusHandler focusHandler;

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
  @Nullable
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
   * <p/>
   * The default value is null which will simply add the elements as they are created.
   */
  @Nullable
  private String focusableInsertBeforeElementId;

  /**
   * The screen this element is part of.
   */
  @Nullable
  private Screen screen;

  @Nonnull
  private TimeProvider time;
  @Nonnull
  private final List<String> elementDebugOut = new ArrayList<String>();
  @Nonnull
  private final StringBuilder elementDebug = new StringBuilder();
  private boolean parentClipArea = false;
  private int parentClipX;
  private int parentClipY;
  private int parentClipWidth;
  private int parentClipHeight;

  // this will be set to true when constraints, padding, margin and so on have been changed and this change should
  // publish an event on the event bus later
  private boolean constraintsChanged;

  /*
   * Whether or not this element should ignore all mouse events.
   */
  private boolean ignoreMouseEvents;

  /*
   * Whether or not this element should ignore all keyboard events.
   */
  private boolean ignoreKeyboardEvents;

  @Nonnull
  private static final Convert convert;
  @Nonnull
  private static final Map<Class<? extends ElementRenderer>, ApplyRenderer> rendererApplier;

  static {
    convert = new Convert();
    rendererApplier = new HashMap<Class<? extends ElementRenderer>, ApplyRenderer>();
    rendererApplier.put(TextRenderer.class, new ApplyRenderText(convert));
    rendererApplier.put(ImageRenderer.class, new ApplyRendererImage(convert));
    rendererApplier.put(PanelRenderer.class, new ApplyRendererPanel(convert));
  }

  @Nullable
  private Map<String, Object> userData;
  private final StyleRefresh styleListener = new StyleRefresh();

  public Element(
      @Nonnull final Nifty nifty,
      @Nonnull final ElementType elementType,
      @Nullable final String id,
      @Nullable final Element parent,
      @Nonnull final FocusHandler focusHandler,
      final boolean visibleToMouseEvents,
      @Nonnull final TimeProvider timeProvider,
      @Nonnull final ElementRenderer... elementRenderer) {
    this(
        nifty,
        elementType,
        id,
        parent,
        new LayoutPart(),
        focusHandler,
        visibleToMouseEvents,
        timeProvider,
        elementRenderer);
  }

  /**
   * Use this constructor to specify a LayoutPart
   */
  public Element(
      @Nonnull final Nifty nifty,
      @Nonnull final ElementType elementType,
      @Nullable final String id,
      @Nullable final Element parent,
      @Nonnull final LayoutPart layoutPart,
      @Nonnull final FocusHandler focusHandler,
      final boolean visibleToMouseEvents,
      @Nonnull final TimeProvider timeProvider,
      @Nonnull final ElementRenderer... elementRenderer) {
    this.nifty = nifty;
    this.elementType = elementType;
    this.id = id;
    this.parent = parent;
    this.elementRenderer = elementRenderer;
    this.effectManager = new EffectManager(this);
    this.effectManager.setAlternateKey(this.nifty.getAlternateKey());
    this.layoutPart = layoutPart;
    this.enabled = true;
    this.enabledCount = 0;
    this.visible = true;
    this.done = false;
    this.interactionBlocked = false;
    this.focusHandler = focusHandler;
    this.visibleToMouseEvents = visibleToMouseEvents;
    this.time = timeProvider;
    this.interaction = new ElementInteraction(this.nifty, this);
  }

  /**
   * This is used when the element is being created from an ElementType in the loading process.
   */
  public void initializeFromAttributes(
      @Nonnull final Screen targetScreen,
      @Nonnull final Attributes attributes,
      @Nonnull final NiftyRenderEngine renderEngine) {
    BoxConstraints boxConstraints = layoutPart.getBoxConstraints();
    boxConstraints.setHeight(convert.sizeValue(attributes.get("height")));
    boxConstraints.setWidth(convert.sizeValue(attributes.get("width")));
    boxConstraints.setX(convert.sizeValue(attributes.get("x")));
    boxConstraints.setY(convert.sizeValue(attributes.get("y")));
    boxConstraints.setHorizontalAlign(convert.horizontalAlign(attributes.get("align")));
    boxConstraints.setVerticalAlign(convert.verticalAlign(attributes.get("valign")));

    String paddingLeft = Convert.DEFAULT_PADDING;
    String paddingRight = Convert.DEFAULT_PADDING;
    String paddingTop = Convert.DEFAULT_PADDING;
    String paddingBottom = Convert.DEFAULT_PADDING;
    if (attributes.isSet("padding")) {
      try {
        String padding = attributes.get("padding");
        assert padding != null; // checked by isSet
        PaddingAttributeParser paddingParser = new PaddingAttributeParser(padding);
        paddingLeft = paddingParser.getLeft();
        paddingRight = paddingParser.getRight();
        paddingTop = paddingParser.getTop();
        paddingBottom = paddingParser.getBottom();
      } catch (Exception e) {
        log.warning(e.getMessage());
      }
    }
    boxConstraints.setPaddingLeft(convert.paddingSizeValue(attributes.get("paddingLeft"), paddingLeft));
    boxConstraints.setPaddingRight(convert.paddingSizeValue(attributes.get("paddingRight"), paddingRight));
    boxConstraints.setPaddingTop(convert.paddingSizeValue(attributes.get("paddingTop"), paddingTop));
    boxConstraints.setPaddingBottom(convert.paddingSizeValue(attributes.get("paddingBottom"), paddingBottom));

    String marginLeft = Convert.DEFAULT_MARGIN;
    String marginRight = Convert.DEFAULT_MARGIN;
    String marginTop = Convert.DEFAULT_MARGIN;
    String marginBottom = Convert.DEFAULT_MARGIN;
    if (attributes.isSet("margin")) {
      try {
        String margin = attributes.get("margin");
        assert margin != null; // checked by isSet
        PaddingAttributeParser marginParser = new PaddingAttributeParser(margin);
        marginLeft = marginParser.getLeft();
        marginRight = marginParser.getRight();
        marginTop = marginParser.getTop();
        marginBottom = marginParser.getBottom();
      } catch (Exception e) {
        log.warning(e.getMessage());
      }
    }
    boxConstraints.setMarginLeft(convert.paddingSizeValue(attributes.get("marginLeft"), marginLeft));
    boxConstraints.setMarginRight(convert.paddingSizeValue(attributes.get("marginRight"), marginRight));
    boxConstraints.setMarginTop(convert.paddingSizeValue(attributes.get("marginTop"), marginTop));
    boxConstraints.setMarginBottom(convert.paddingSizeValue(attributes.get("marginBottom"), marginBottom));

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
    for (int i = 0; i < elementRenderer.length; i++) {
      ElementRenderer renderer = elementRenderer[i];
      ApplyRenderer rendererApply = rendererApplier.get(renderer.getClass());
      rendererApply.apply(targetScreen, this, attributes, renderEngine);
    }
    //Subscribe for a style refresh
    nifty.getEventService().subscribeStrongly("style-refresh:" + getStyle(), styleListener);
  }

  public void initializeFromPostAttributes(@Nonnull final Attributes attributes) {
    visible = attributes.getAsBoolean("visible", Convert.DEFAULT_VISIBLE);
  }

  @Nullable
  public String getId() {
    return id;
  }

  /**
   * Get the parent of this element.
   *
   * @return either the parent of the element or {@code this} in case the element is the root element
   */
  @Nonnull
  public Element getParent() {
    return parent == null ? this : parent;
  }

  /**
   * Check if this element has a parent. If this is not the case the element is a root element.
   *
   * @return {@code true} in case the element has a parent
   */
  public boolean hasParent() {
    return parent != null;
  }

  public void setParent(@Nullable final Element element) {
    parent = element;

    // This element has a new parent. Check the parent's clip area and update this element accordingly.
    if (parentHasClipArea()) {
      setParentClipArea(parentClipX, parentClipY, parentClipWidth, parentClipHeight);
      publishEvent();
    } else {
      parentClipArea = false;
    }
  }

  private boolean parentHasClipArea() {
    if (parent == null) {
      return false;
    }
    return parent.parentClipArea;
  }

  @Nonnull
  public String getElementStateString(@Nonnull final String offset) {
    return getElementStateString(offset, ".*");
  }

  @Nonnull
  public String getElementStateString(@Nonnull final String offset, @Nonnull final String regex) {
    elementDebugOut.clear();
    elementDebugOut.add(" type: [" + elementType.output(offset.length()) + "]");
    elementDebugOut.add(" style [" + getElementType().getAttributes().get("style") + "]");
    elementDebugOut.add(" state [" + getState() + "]");
    elementDebugOut.add(" position [x=" + getX() + ", y=" + getY() + ", w=" + getWidth() + ", h=" + getHeight() + "]");
    elementDebugOut.add(" constraint [" + outputSizeValue(layoutPart.getBoxConstraints().getX()) + ", " +
        "" + outputSizeValue(layoutPart.getBoxConstraints().getY()) + ", " + outputSizeValue(layoutPart
        .getBoxConstraints().getWidth()) + ", " + outputSizeValue(layoutPart.getBoxConstraints().getHeight()) + "]");
    elementDebugOut.add(" padding [" + outputSizeValue(layoutPart.getBoxConstraints().getPaddingLeft()) + ", " +
        "" + outputSizeValue(layoutPart.getBoxConstraints().getPaddingRight()) + ", " +
        "" + outputSizeValue(layoutPart.getBoxConstraints().getPaddingTop()) + ", " +
        "" + outputSizeValue(layoutPart.getBoxConstraints().getPaddingBottom()) + "]");
    elementDebugOut.add(" margin [" + outputSizeValue(layoutPart.getBoxConstraints().getMarginLeft()) + ", " +
        "" + outputSizeValue(layoutPart.getBoxConstraints().getMarginRight()) + ", " +
        "" + outputSizeValue(layoutPart.getBoxConstraints().getMarginTop()) + ", " +
        "" + outputSizeValue(layoutPart.getBoxConstraints().getMarginBottom()) + "]");
    StringBuilder state = new StringBuilder();
    if (focusable) {
      state.append(" focusable");
    }
    if (enabled) {
      if (state.length() > 0) {
        state.append(",");
      }
      state.append(" enabled(").append(enabledCount).append(")");
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
      elementDebugOut.add(" parent clip [x=" + parentClipX + ", y=" + parentClipY + ", w=" + parentClipWidth + ", " +
          "h=" + parentClipHeight + "]");
    }
    StringBuilder renderOrder = new StringBuilder();
    renderOrder.append(" render order: ");
    if (children != null && elementsRenderOrderSet != null) {
      for (Element e : elementsRenderOrderSet) {
        renderOrder.append("[").append(e.id).append(" (")
            .append((e.renderOrder == 0) ? children.indexOf(e) : e.renderOrder).append(")]");
      }
    }
    elementDebugOut.add(renderOrder.toString());

    elementDebug.delete(0, elementDebug.length());
    for (int i = 0; i < elementDebugOut.size(); i++) {
      String line = elementDebugOut.get(i);
      if (line.matches(regex)) {
        if (elementDebug.length() > 0) {
          elementDebug.append("\n").append(offset);
        }
        elementDebug.append(line);
      }
    }
    return elementDebug.toString();
  }

  @Nonnull
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

  @Nullable
  private String outputSizeValue(@Nullable final SizeValue value) {
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

  @Nonnull
  public List<Element> getChildren() {
    if (children == null) {
      return Collections.emptyList();
    }
    return Collections.unmodifiableList(children);
  }

  /**
   * Get the amount of children assigned to this element.
   *
   * @return the amaount of children
   */
  public int getChildrenCount() {
    return elementsRenderOrder != null ? elementsRenderOrder.length : 0;
  }

  /**
   * Get all children, children of children, etc, recursively.
   *
   * @return an iterator that will traverse the element's entire tree downward.
   */
  @Nonnull
  public Iterator<Element> getDescendants() {
    if (children == null) {
      // We don't want to give up Java 1.6 compatibility right now. Since Collections.emptyIterator() is Java 1.7 API
      // for now we've made our own replacement (see end of class). If we finally switch over to 1.7 we can use the
      // original method.
      //
      // return Collections.emptyIterator();
      return emptyIterator();
    }
    return new ElementTreeTraverser(this);
  }

  /**
   * Adds a child element to the end of the list of this element's children.
   */
  public void addChild(@Nonnull final Element child) {
    insertChild(child, getChildrenCount());
  }

  /**
   * Inserts a child element at the specified index in this element's list of children.
   */
  public void insertChild(@Nonnull final Element child, final int index) {
    final int lastValidIndex = getChildrenCount();
    int usedIndex = index;
    if (index < 0 || index > lastValidIndex) {
      log.severe("Index is out of range. Index: " + index + " Last valid: " + lastValidIndex);
      usedIndex = Math.min(lastValidIndex, Math.max(0, index));
    }
    if (children == null) {
      children = new ArrayList<Element>();
    }

    children.add(usedIndex, child);

    if (elementsRenderOrderSet == null) {
      elementsRenderOrderSet = new TreeSet<Element>(RENDER_ORDER_COMPARATOR);
    }
    if (!elementsRenderOrderSet.add(child)) {
      log.severe("Adding the element failed as it seems this element is already part of the children list. This is " +
          "bad. Rebuilding the children list is required now.");
      final int childCount = children.size();
      boolean foundProblem = false;
      for (int i = 0; i < childCount; i++) {
        if (i == usedIndex) {
          continue;
        }
        Element testChild = children.get(i);
        if (testChild.equals(child)) {
          foundProblem = true;
          children.remove(i);
          break;
        }
      }
      if (!foundProblem) {
        /* Can't locate the issue, recovery failed -> undoing insert and throwing exception */
        children.remove(usedIndex);
        throw new IllegalStateException("Insert item failed, render list refused the item, " +
            "but duplicate couldn't be located in the children list. Element is corrupted.");
      }
    } else {
      elementsRenderOrder = elementsRenderOrderSet.toArray(new Element[elementsRenderOrderSet.size()]);
    }
  }

  /**
   * Set the index of this element in it's parent's list of children.
   */
  public void setIndex(final int index) {
    if (parent == null) {
      return;
    }
    List<Element> parentChildren = parent.children;
    if (parentChildren == null) {
      log.severe("Element tree corrupted. Parent element has not children.");
    } else {
      final int curInd = parentChildren.indexOf(this);
      if (curInd >= 0 && index != curInd) {
        Element shouldBeThis = parentChildren.remove(curInd);
        if (shouldBeThis.equals(this)) {
          parentChildren.add(index, this);
        } else {
          log.severe("Setting index failed, detected index did not return correct element. Undoing operation");
          parentChildren.add(curInd, shouldBeThis);
        }
      }
    }
  }

  public void render(@Nonnull final NiftyRenderEngine r) {
    if (visible) {
      if (effectManager.isEmpty()) {
        r.saveStates();
        renderElement(r);
        renderChildren(r);
        r.restoreStates();
      } else {
        r.saveStates();
        effectManager.renderPre(r, this);
        renderElement(r);
        effectManager.renderPost(r, this);
        renderChildren(r);
        r.restoreStates();
        r.saveStates();
        effectManager.renderOverlay(r, this);
        r.restoreStates();
      }
    }
  }

  private void renderElement(@Nonnull final NiftyRenderEngine r) {
    for (int i = 0; i < elementRenderer.length; i++) {
      ElementRenderer renderer = elementRenderer[i];
      renderer.render(this, r);
    }
  }

  private void renderChildren(@Nonnull final NiftyRenderEngine r) {
    if (clipChildren) {
      r.enableClip(getX(), getY(), getX() + getWidth(), getY() + getHeight());
      renderInternalChildElements(r);
      r.disableClip();
    } else {
      renderInternalChildElements(r);
    }
  }

  private void renderInternalChildElements(@Nonnull final NiftyRenderEngine r) {
    if (elementsRenderOrder != null) {
      for (int i = 0; i < elementsRenderOrder.length; i++) {
        Element p = elementsRenderOrder[i];
        p.render(r);
      }
    }
  }

  public void setLayoutManager(@Nullable final LayoutManager newLayout) {
    this.layoutManager = newLayout;
  }

  public void resetLayout() {

    TextRenderer textRenderer = getRenderer(TextRenderer.class);
    if (textRenderer != null) {
      textRenderer.resetLayout(this);
    }

    if (children != null) {
      final int childrenCount = children.size();
      for (int i = 0; i < childrenCount; i++) {
        Element e = children.get(i);
        e.resetLayout();
      }
    }
  }

  private void preProcessConstraintWidth() {
    if (children != null) {
      final int childrenCount = children.size();
      for (int i = 0; i < childrenCount; i++) {
        Element e = children.get(i);
        e.preProcessConstraintWidth();
      }
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

      if (myWidth.hasDefault()) {
        List<LayoutPart> layoutPartChild = getLayoutChildrenWithIndependentWidth();

        // if all (!) child elements have a pixel fixed width we can calculate a new width constraint for this element!
        if (!layoutPartChild.isEmpty() && getChildrenCount() == layoutPartChild.size()) {

          SizeValue newWidth = layoutManager.calculateConstraintWidth(this.layoutPart, layoutPartChild);
          if (newWidth.hasValue()) {
            int newWidthPx = newWidth.getValueAsInt(0);
            newWidthPx += this.layoutPart.getBoxConstraints().getPaddingLeft().getValueAsInt(newWidth.getValueAsInt
                (newWidthPx));
            newWidthPx += this.layoutPart.getBoxConstraints().getPaddingRight().getValueAsInt(newWidth.getValueAsInt
                (newWidthPx));
            setConstraintWidth(SizeValue.def(newWidthPx));
          }
        } else {
          setConstraintWidth(SizeValue.def());
        }

      } else if (myWidth.hasSum()) {
        List<LayoutPart> layoutPartChild = getLayoutChildrenWithIndependentWidth();
        SizeValue newWidth = layoutPart.getSumWidth(layoutPartChild);
        if (newWidth.hasValue()) {
          int newWidthPx = newWidth.getValueAsInt(0);
          newWidthPx += this.layoutPart.getBoxConstraints().getPaddingLeft().getValueAsInt(newWidth.getValueAsInt
              (newWidthPx));
          newWidthPx += this.layoutPart.getBoxConstraints().getPaddingRight().getValueAsInt(newWidth.getValueAsInt
              (newWidthPx));
          setConstraintWidth(SizeValue.sum(newWidthPx));
        } else {
          setConstraintWidth(SizeValue.sum(0));
        }

      } else if (myWidth.hasMax()) {
        List<LayoutPart> layoutPartChild = getLayoutChildrenWithIndependentWidth();
        SizeValue newWidth = layoutPart.getMaxWidth(layoutPartChild);
        if (newWidth.hasValue()) {
          int newWidthPx = newWidth.getValueAsInt(0);
          newWidthPx += this.layoutPart.getBoxConstraints().getPaddingLeft().getValueAsInt(newWidth.getValueAsInt
              (newWidthPx));
          newWidthPx += this.layoutPart.getBoxConstraints().getPaddingRight().getValueAsInt(newWidth.getValueAsInt
              (newWidthPx));
          setConstraintWidth(SizeValue.max(newWidthPx));
        } else {
          setConstraintWidth(SizeValue.max(0));
        }

      }
    }
  }

  @Nonnull
  private List<LayoutPart> getLayoutChildrenWithIndependentWidth() {
    if (children == null) {
      return Collections.emptyList();
    }
    final int childrenCount = children.size();
    List<LayoutPart> layoutPartChild = new ArrayList<LayoutPart>(childrenCount);
    for (int i = 0; i < childrenCount; i++) {
      Element e = children.get(i);
      SizeValue childWidth = e.getConstraintWidth();
      if (childWidth.isPixel() && childWidth.isIndependentFromParent()) {
        layoutPartChild.add(e.layoutPart);
      }
    }
    return layoutPartChild;
  }

  private void preProcessConstraintHeight() {
    if (children != null) {
      final int childrenCount = children.size();
      for (int i = 0; i < childrenCount; i++) {
        Element e = children.get(i);
        e.preProcessConstraintHeight();
      }
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

      if (myHeight.hasDefault()) {
        List<LayoutPart> layoutPartChild = getLayoutChildrenWithIndependentHeight();

        // if all (!) child elements have a pixel fixed height we can calculate a new height constraint for this
        // element!
        if (!layoutPartChild.isEmpty() && getChildrenCount() == layoutPartChild.size()) {

          SizeValue newHeight = layoutManager.calculateConstraintHeight(this.layoutPart, layoutPartChild);
          if (newHeight.hasValue()) {
            int newHeightPx = newHeight.getValueAsInt(0);
            newHeightPx += this.layoutPart.getBoxConstraints().getPaddingTop().getValueAsInt(newHeight.getValueAsInt
                (newHeightPx));
            newHeightPx += this.layoutPart.getBoxConstraints().getPaddingBottom().getValueAsInt(newHeight
                .getValueAsInt(newHeightPx));
            setConstraintHeight(SizeValue.def(newHeightPx));
          }
        } else {
          setConstraintHeight(SizeValue.def());
        }

      } else if (myHeight.hasSum()) {
        List<LayoutPart> layoutPartChild = getLayoutChildrenWithIndependentHeight();
        SizeValue newHeight = layoutPart.getSumHeight(layoutPartChild);
        if (newHeight.hasValue()) {
          int newHeightPx = newHeight.getValueAsInt(0);
          newHeightPx += this.layoutPart.getBoxConstraints().getPaddingTop().getValueAsInt(newHeight.getValueAsInt
              (newHeightPx));
          newHeightPx += this.layoutPart.getBoxConstraints().getPaddingBottom().getValueAsInt(newHeight.getValueAsInt
              (newHeightPx));
          setConstraintHeight(SizeValue.sum(newHeightPx));
        } else {
          setConstraintHeight(SizeValue.sum(0));
        }

      } else if (myHeight.hasMax()) {
        List<LayoutPart> layoutPartChild = getLayoutChildrenWithIndependentHeight();
        SizeValue newHeight = layoutPart.getMaxHeight(layoutPartChild);
        if (newHeight.hasValue()) {
          int newHeightPx = newHeight.getValueAsInt(0);
          newHeightPx += this.layoutPart.getBoxConstraints().getPaddingTop().getValueAsInt(newHeight.getValueAsInt
              (newHeightPx));
          newHeightPx += this.layoutPart.getBoxConstraints().getPaddingBottom().getValueAsInt(newHeight.getValueAsInt
              (newHeightPx));
          setConstraintHeight(SizeValue.max(newHeightPx));
        } else {
          setConstraintHeight(SizeValue.max(0));
        }

      }
    }
  }

  @Nonnull
  private List<LayoutPart> getLayoutChildrenWithIndependentHeight() {
    if (children == null) {
      return Collections.emptyList();
    }
    final int childrenCount = children.size();
    List<LayoutPart> layoutPartChild = new ArrayList<LayoutPart>(childrenCount);
    for (int i = 0; i < childrenCount; i++) {
      Element e = children.get(i);
      SizeValue childHeight = e.getConstraintHeight();
      if (childHeight.isPixel() && childHeight.isIndependentFromParent()) {
        layoutPartChild.add(e.layoutPart);
      }
    }
    return layoutPartChild;
  }

  private void processLayoutInternal() {
    if (children != null) {
      final int childrenCount = children.size();
      for (int i = 0; i < childrenCount; i++) {
        Element w = children.get(i);
        TextRenderer textRenderer = w.getRenderer(TextRenderer.class);
        if (textRenderer != null) {
          textRenderer.setWidthConstraint(w, w.getConstraintWidth(), getWidth(), nifty.getRenderEngine());
        }
      }
    }
  }

  private void processLayout() {
    processLayoutInternal();

    if (layoutManager != null) {
      if (children != null) {
        final int childrenCount = children.size();
        // we need a list of LayoutPart and not of Element, so we'll build one on the fly here
        List<LayoutPart> layoutPartChild = new ArrayList<LayoutPart>(childrenCount);
        for (int i = 0; i < childrenCount; i++) {
          Element w = children.get(i);
          layoutPartChild.add(w.layoutPart);
        }

        // use out layoutManager to layout our children
        layoutManager.layoutElements(layoutPart, layoutPartChild);
      }

      if (attachedInputControl != null) {
        NiftyControl niftyControl = attachedInputControl.getNiftyControl(NiftyControl.class);
        if (niftyControl != null) {
          if (niftyControl.isBound()) {
            niftyControl.layoutCallback();
          }
        }
      }

      if (children != null) {
        // repeat this step for all child elements
        final int childrenCount = children.size();
        for (int i = 0; i < childrenCount; i++) {
          Element w = children.get(i);
          w.processLayout();
        }
      }
    }

    if (clipChildren && children != null) {
      final int childrenCount = children.size();
      for (int i = 0; i < childrenCount; i++) {
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

    publishConstraintsChangedEvent();
  }

  private void publishConstraintsChangedEvent() {
    if (constraintsChanged) {
      publishEvent();
      constraintsChanged = false;
    }
    if (children != null) {
      for (int i = 0; i < children.size(); i++) {
        children.get(i).publishConstraintsChangedEvent();
      }
    }
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

    if (children != null) {
      final int childrenCount = children.size();
      for (int i = 0; i < childrenCount; i++) {
        Element w = children.get(i);
        w.setParentClipArea(parentClipX, parentClipY, parentClipWidth, parentClipHeight);
      }
    }
  }

  public void resetEffects() {
    effectManager.reset();
    if (children != null) {
      final int childrenCount = children.size();
      for (int i = 0; i < childrenCount; i++) {
        Element w = children.get(i);
        w.resetEffects();
      }
    }
  }

  public void resetAllEffects() {
    effectManager.resetAll();
    if (children != null) {
      final int childrenCount = children.size();
      for (int i = 0; i < childrenCount; i++) {
        Element w = children.get(i);
        w.resetAllEffects();
      }
    }
  }

  public void restoreForShow() {
    effectManager.restoreForShow();
    if (children != null) {
      final int childrenCount = children.size();
      for (int i = 0; i < childrenCount; i++) {
        Element w = children.get(i);
        w.restoreForShow();
      }
    }
  }

  public void resetForHide() {
    effectManager.resetForHide();
    if (children != null) {
      final int childrenCount = children.size();
      for (int i = 0; i < childrenCount; i++) {
        Element w = children.get(i);
        w.resetForHide();
      }
    }
  }

  public void resetSingleEffect(@Nonnull final EffectEventId effectEventId) {
    effectManager.resetSingleEffect(effectEventId);
    if (children != null) {
      final int childrenCount = children.size();
      for (int i = 0; i < childrenCount; i++) {
        Element w = children.get(i);
        w.resetSingleEffect(effectEventId);
      }
    }
  }

  public void resetSingleEffect(@Nonnull final EffectEventId effectEventId, @Nonnull final String customKey) {
    effectManager.resetSingleEffect(effectEventId, customKey);
    if (children != null) {
      final int childrenCount = children.size();
      for (int i = 0; i < childrenCount; i++) {
        Element w = children.get(i);
        w.resetSingleEffect(effectEventId, customKey);
      }
    }
  }

  public void resetMouseDown() {
    interaction.resetMouseDown();
    if (children != null) {
      final int childrenCount = children.size();
      for (int i = 0; i < childrenCount; i++) {
        Element w = children.get(i);
        w.resetMouseDown();
      }
    }
  }

  public void setConstraintX(@Nonnull final SizeValue newX) {
    layoutPart.getBoxConstraints().setX(newX);
    notifyListeners();
  }

  public void setConstraintY(@Nonnull final SizeValue newY) {
    layoutPart.getBoxConstraints().setY(newY);
    notifyListeners();
  }

  public void setConstraintWidth(@Nonnull final SizeValue newWidth) {
    layoutPart.getBoxConstraints().setWidth(newWidth);
    notifyListeners();
  }

  public void setConstraintHeight(@Nonnull final SizeValue newHeight) {
    layoutPart.getBoxConstraints().setHeight(newHeight);
    notifyListeners();
  }

  @Nonnull
  public SizeValue getConstraintX() {
    return layoutPart.getBoxConstraints().getX();
  }

  @Nonnull
  public SizeValue getConstraintY() {
    return layoutPart.getBoxConstraints().getY();
  }

  @Nonnull
  public SizeValue getConstraintWidth() {
    return layoutPart.getBoxConstraints().getWidth();
  }

  @Nonnull
  public SizeValue getConstraintHeight() {
    return layoutPart.getBoxConstraints().getHeight();
  }

  public void setConstraintHorizontalAlign(@Nonnull final HorizontalAlign newHorizontalAlign) {
    layoutPart.getBoxConstraints().setHorizontalAlign(newHorizontalAlign);
  }

  public void setConstraintVerticalAlign(@Nonnull final VerticalAlign newVerticalAlign) {
    layoutPart.getBoxConstraints().setVerticalAlign(newVerticalAlign);
  }

  @Nonnull
  public HorizontalAlign getConstraintHorizontalAlign() {
    return layoutPart.getBoxConstraints().getHorizontalAlign();
  }

  @Nonnull
  public VerticalAlign getConstraintVerticalAlign() {
    return layoutPart.getBoxConstraints().getVerticalAlign();
  }

  public void registerEffect(
      @Nonnull final EffectEventId theId,
      @Nonnull final Effect e) {
    log.fine("[" + id + "] register: " + theId.toString() + "(" + e.getStateString() + ")");
    effectManager.registerEffect(theId, e);
  }

  public void startEffect(@Nonnull final EffectEventId effectEventId) {
    startEffect(effectEventId, null);
  }

  public void startEffect(@Nonnull final EffectEventId effectEventId, @Nullable final EndNotify effectEndNotify) {
    startEffect(effectEventId, effectEndNotify, null);
  }

  public void startEffect(
      @Nonnull final EffectEventId effectEventId,
      @Nullable final EndNotify effectEndNotify,
      @Nullable final String customKey) {
    startEffectDoIt(effectEventId, effectEndNotify, customKey, true);
  }

  public void startEffectWithoutChildren(@Nonnull final EffectEventId effectEventId) {
    startEffectWithoutChildren(effectEventId, null);
  }

  public void startEffectWithoutChildren(
      @Nonnull final EffectEventId effectEventId,
      @Nullable final EndNotify effectEndNotify) {
    startEffectWithoutChildren(effectEventId, effectEndNotify, null);
  }

  public void startEffectWithoutChildren(
      @Nonnull final EffectEventId effectEventId,
      @Nullable final EndNotify effectEndNotify,
      @Nullable final String customKey) {
    startEffectDoIt(effectEventId, effectEndNotify, customKey, false);
  }

  private void startEffectDoIt(
      @Nonnull final EffectEventId effectEventId,
      @Nullable final EndNotify effectEndNotify,
      @Nullable final String customKey,
      final boolean withChildren) {
    if (effectEventId == EffectEventId.onStartScreen) {
      if (!visible) {
        return;
      }
      done = false;
      interactionBlocked = true;
    }
    if (effectEventId == EffectEventId.onEndScreen) {
      if (!visible && (effectEndNotify != null)) {
        // it doesn't make sense to start the onEndScreen effect when the element is hidden
        // just call the effectEndNotify directly and quit
        effectEndNotify.perform();
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
    LocalEndNotify forwardToSelf = new LocalEndNotify(effectEventId, effectEndNotify);

    // start the effect for our self

    // The commit 77059d2544d3825aaa680491683b377aa1b1fb41 introduces an issue with the controls example. All the panels
    // (and the hints too) didn't fade in properly and it is because of this commit. Here is the original commit comment
    // for the commit:
    //
    // ---------------------------------
    // element.show() not propagates EffectEventId.onShow to children anymore
    //
    // When you show() an element in the past the EffectEventId.onShow was
    // triggered for all children, even for invisible ones. So we'd end up with
    // invisible child elements with an active onShow effect which would
    // prevent an actual show() call on that child elements.
    //
    // This commit will now not propagate EffectEventId.onShow to the children
    // anymore.
    // ---------------------------------
    //
    // So, somehow this didn't work for the controls example demonstration. The show() method has already been changed
    // back to the original code that will use startEffect() instead of startEffectWithoutChildren(). Doing that means
    // we'll end up here for all children - even the invisible ones. Which seems to be needed for some use cases.
    // So instead of removing the traversing of the children for the onShow event completely we'll now only actually
    // start the onShow effect for elements that are visible.
    //
    // For now we'll leave the other EffectEventIds untouched so they will still get executed for invisible elements -
    // which was the behavior before this commit and the one that introduced the controls example demo bug too.
    //
    // With that change the default controls example is working again AND the intent of the original commit is preserved
    // as well.
    if (effectEventId != EffectEventId.onShow
        ||
        (effectEventId == EffectEventId.onShow && visible)) {
      effectManager.startEffect(effectEventId, this, time, forwardToSelf, customKey);
    }

    // notify all child elements of the start effect
    if (withChildren && children != null) {
      final int childrenCount = children.size();
      for (int i = 0; i < childrenCount; i++) {
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

  private void startEffectInternal(
      @Nonnull final EffectEventId effectEventId,
      @Nullable final EndNotify effectEndNotify,
      @Nullable final String customKey) {
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
    LocalEndNotify forwardToSelf = new LocalEndNotify(effectEventId, effectEndNotify);

    // start the effect for our self
    effectManager.startEffect(effectEventId, this, time, forwardToSelf, customKey);

    // notify all child elements of the start effect
    if (children != null) {
      final int childrenCount = children.size();
      for (int i = 0; i < childrenCount; i++) {
        Element w = children.get(i);
        w.startEffectInternal(effectEventId, forwardToSelf, customKey);
      }
    }

    if (effectEventId == EffectEventId.onFocus) {
      if (attachedInputControl != null) {
        attachedInputControl.onFocus(true);
      }
    }
  }

  public void stopEffect(@Nonnull final EffectEventId effectEventId) {
    stopEffectInternal(effectEventId, true);
  }

  public void stopEffectWithoutChildren(@Nonnull final EffectEventId effectEventId) {
    stopEffectInternal(effectEventId, false);
  }

  private void stopEffectInternal(@Nonnull final EffectEventId effectEventId, final boolean withChildren) {
    if (EffectEventId.onStartScreen == effectEventId ||
        EffectEventId.onEndScreen == effectEventId) {
      interactionBlocked = false;
      if (!visible) {
        return;
      }
    }
    effectManager.stopEffect(effectEventId);

    // notify all child elements of the start effect
    if (withChildren && children != null) {
      final int childrenCount = children.size();
      for (int i = 0; i < childrenCount; i++) {
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
  public boolean isEffectActive(@Nonnull final EffectEventId effectEventId) {
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
      if (children != null) {
        final int childrenCount = children.size();
        for (int i = 0; i < childrenCount; i++) {
          children.get(i).enableInternal();
        }
      }
    } else {
      if (children != null) {
        final int childrenCount = children.size();
        for (int i = 0; i < childrenCount; i++) {
          children.get(i).enableInternal();
        }
      }
    }
  }

  void enableEffect() {
    stopEffectWithoutChildren(EffectEventId.onDisabled);
    startEffectWithoutChildren(EffectEventId.onEnabled);
    if (id != null) {
      nifty.publishEvent(id, new ElementEnableEvent(this));
    }
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
      if (children != null) {
        final int childrenCount = children.size();
        for (int i = 0; i < childrenCount; i++) {
          children.get(i).disableInternal();
        }
      }
    } else {
      if (children != null) {
        final int childrenCount = children.size();
        for (int i = 0; i < childrenCount; i++) {
          children.get(i).disableInternal();
        }
      }
    }
  }

  public void disableFocus() {
    if (focusHandler.getKeyboardFocusElement() == this) {
      Element prevElement = focusHandler.getNext(this);
      prevElement.setFocus();
    }

    focusHandler.lostKeyboardFocus(this);
    focusHandler.lostMouseFocus(this);

    // make sure we don't have child elements that still have the focus
    if (children != null) {
      final int childrenCount = children.size();
      for (int i = 0; i < childrenCount; i++) {
        children.get(i).disableFocus();
      }
    }
  }

  void disableEffect() {
    stopEffectWithoutChildren(EffectEventId.onHover);
    stopEffectWithoutChildren(EffectEventId.onStartHover);
    stopEffectWithoutChildren(EffectEventId.onEndHover);
    stopEffectWithoutChildren(EffectEventId.onEnabled);
    startEffectWithoutChildren(EffectEventId.onDisabled);

    if (id != null) {
      nifty.publishEvent(id, new ElementDisableEvent(this));
    }
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void show() {
    show(null);
  }

  public void show(@Nullable final EndNotify perform) {
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
    restoreForShow();

    if (id != null) {
      nifty.publishEvent(id, new ElementShowEvent(this));
    }
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

  public void hide(@Nullable final EndNotify perform) {
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

    if (id != null) {
      nifty.publishEvent(id, new ElementHideEvent(this));
    }
  }

  /**
   * Returns true if this element is visible. Please note that this is with regards to that element only. It's possible
   * that this element is invisible (because of any of its parent elements is invisible) and still this method will
   * return true;
   *
   * @return if this element is visible returns true. when the element is invisible returns false.
   */
  public boolean isVisible() {
    return visible;
  }

  /**
   * Returns true if this element is visible and all of its parent hierarchy is visible too.
   * @return if this element is really visible and false if it is invisible.
   */
  public boolean isVisibleWithParent() {
    if (parent == null) {
        return visible;
      }
    return visible && parent.isVisibleWithParent();
  }

  public void setHotSpotFalloff(@Nullable final Falloff newFalloff) {
    effectManager.setFalloff(newFalloff);
  }

  @Nullable
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
   *
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
      @Nonnull final NiftyMouseInputEvent mouseEvent,
      final long eventTime,
      @Nonnull final MouseOverHandler mouseOverHandler) {
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
      if (children != null) {
        final int childrenCount = children.size();
        for (int i = 0; i < childrenCount; i++) {
          Element w = children.get(i);
          w.buildMouseOverElements(mouseEvent, eventTime, mouseOverHandler);
        }
      }
    }
  }

  public void mouseEventHoverPreprocess(@Nonnull final NiftyMouseInputEvent mouseEvent, final long eventTime) {
    effectManager.handleHoverDeactivate(this, mouseEvent.getMouseX(), mouseEvent.getMouseY());
  }
  /**
   * Process mouse event for this element
   * @param mouseEvent
   * @param eventTime is the current time in milliseconds
   * @return True if the event has been processed 
   */
  public boolean mouseEvent(@Nonnull final NiftyMouseInputEvent mouseEvent, final long eventTime) {
    mouseEventHover(mouseEvent);
    return interaction.process(mouseEvent, eventTime, isInside(mouseEvent), canHandleInteraction(),
        focusHandler.hasExclusiveMouseFocus(this));
  }

  private void mouseEventHover(@Nonnull final NiftyMouseInputEvent mouseEvent) {
    effectManager.handleHover(this, mouseEvent.getMouseX(), mouseEvent.getMouseY());
    effectManager.handleHoverStartAndEnd(this, mouseEvent.getMouseX(), mouseEvent.getMouseY());
  }

  /**
   * Handles the MouseOverEvent. Must not call child elements. This is handled by the caller.
   *
   * @return true the mouse event has been consumed and false when the mouse event can be processed further down
   */
  public boolean mouseOverEvent(@Nonnull final NiftyMouseInputEvent mouseEvent, final long eventTime) {
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
  private boolean isInside(@Nonnull final NiftyMouseInputEvent inputEvent) {
    return isMouseInsideElement(inputEvent.getMouseX(), inputEvent.getMouseY());
  }

  public boolean isMouseInsideElement(final int mouseX, final int mouseY) {
    if (parentClipArea) {
      // must be inside the parent to continue
      if (isInsideParentClipArea(mouseX, mouseY)) {
        return
            mouseX >= getX() &&
            mouseX < (getX() + getWidth()) &&
            mouseY >= (getY()) &&
            mouseY < (getY() + getHeight());
      } else {
        return false;
      }
    } else {
      return
          mouseX >= getX() &&
          mouseX < (getX() + getWidth()) &&
          mouseY >= (getY()) &&
          mouseY < (getY() + getHeight());
    }
  }

  private boolean isInsideParentClipArea(final int mouseX, final int mouseY) {
    return mouseX >= parentClipX &&
        mouseX <= (parentClipX + parentClipWidth) &&
        mouseY > (parentClipY) &&
        mouseY < (parentClipY + parentClipHeight);
  }

  /**
   * @deprecated Use {@link #onClickAndReleasePrimaryMouseButton()} instead.
   *
   * Simulates a click-release of the primary mouse button on the element.
   *
   * @see #onClickAndReleasePrimaryMouseButton()
   * @see #onClickAndReleaseSecondaryMouseButton()
   * @see #onClickAndReleaseSecondaryMouseButton()
   */
  @Deprecated
  public void onClick() {
    onClickAndReleasePrimaryMouseButton();
  }

  /**
   * Simulates a click-release of the primary mouse button on the element.
   *
   * This method is called automatically in many cases as a response to receiving a
   * {@link de.lessvoid.nifty.input.NiftyStandardInputEvent#Activate} event.
   *
   * An element will not respond to a click-release of the primary mouse button in the following situations:
   *
   * 1) When the element is disabled.
   *
   * 2) When a {@link de.lessvoid.nifty.screen.Screen} is starting or ending, or more specifically, when:
   * {@link de.lessvoid.nifty.effects.EffectEventId#onStartScreen} or
   * {@link de.lessvoid.nifty.effects.EffectEventId#onEndScreen} effects are active on the current
   * {@link de.lessvoid.nifty.screen.Screen}.
   *
   * 3) If there is no current {@link de.lessvoid.nifty.screen.Screen}, i.e., the current
   * {@link de.lessvoid.nifty.screen.Screen} is null.
   *
   * @see de.lessvoid.nifty.elements.ElementInteraction#clickAndReleasePrimaryMouseButton(de.lessvoid.nifty.Nifty)
   * @see #onClickAndReleaseSecondaryMouseButton()
   * @see #onClickAndReleaseTertiaryMouseButton()
   * @see de.lessvoid.nifty.input.NiftyStandardInputEvent#Activate
   * @see #isEnabled()
   * @see #enable()
   * @see #disable()
   * @see Screen#isEffectActive(de.lessvoid.nifty.effects.EffectEventId)
   * @see de.lessvoid.nifty.effects.EffectEventId#onStartScreen
   * @see de.lessvoid.nifty.effects.EffectEventId#onEndScreen
   * @see de.lessvoid.nifty.Nifty#getCurrentScreen()
   */
  public void onClickAndReleasePrimaryMouseButton() {
    if (!canHandleInteraction()) {
      return;
    }

    interaction.clickAndReleasePrimaryMouseButton(nifty);
  }

  /**
   * Simulates a click-release of the secondary mouse button on the element.
   *
   * An element will not respond to a click-release of the secondary mouse button in the following situations:
   *
   * 1) When the element is disabled.
   *
   * 2) When a {@link de.lessvoid.nifty.screen.Screen} is starting or ending, or more specifically, when:
   * {@link de.lessvoid.nifty.effects.EffectEventId#onStartScreen} or
   * {@link de.lessvoid.nifty.effects.EffectEventId#onEndScreen} effects are active on the current
   * {@link de.lessvoid.nifty.screen.Screen}.
   *
   * 3) If there is no current {@link de.lessvoid.nifty.screen.Screen}, i.e., the current
   * {@link de.lessvoid.nifty.screen.Screen} is null.
   *
   * @see de.lessvoid.nifty.elements.ElementInteraction#clickAndReleaseSecondaryMouseButton(de.lessvoid.nifty.Nifty)
   * @see #onClickAndReleasePrimaryMouseButton()
   * @see #onClickAndReleaseTertiaryMouseButton()
   * @see #isEnabled()
   * @see #enable()
   * @see #disable()
   * @see Screen#isEffectActive(de.lessvoid.nifty.effects.EffectEventId)
   * @see de.lessvoid.nifty.effects.EffectEventId#onStartScreen
   * @see de.lessvoid.nifty.effects.EffectEventId#onEndScreen
   * @see de.lessvoid.nifty.Nifty#getCurrentScreen()
   */
  public void onClickAndReleaseSecondaryMouseButton() {
    if (!canHandleInteraction()) {
      return;
    }

    interaction.clickAndReleaseSecondaryMouseButton(nifty);
  }

  /**
   * Simulates a click-release of the tertiary mouse button on the element.
   *
   * An element will not respond to a click-release of the tertiary mouse button in the following situations:
   *
   * 1) When the element is disabled.
   *
   * 2) When a {@link de.lessvoid.nifty.screen.Screen} is starting or ending, or more specifically, when:
   * {@link de.lessvoid.nifty.effects.EffectEventId#onStartScreen} or
   * {@link de.lessvoid.nifty.effects.EffectEventId#onEndScreen} effects are active on the current
   * {@link de.lessvoid.nifty.screen.Screen}.
   *
   * 3) If there is no current {@link de.lessvoid.nifty.screen.Screen}, i.e., the current
   * {@link de.lessvoid.nifty.screen.Screen} is null.
   *
   * @see de.lessvoid.nifty.elements.ElementInteraction#clickAndReleaseTertiaryMouseButton(de.lessvoid.nifty.Nifty)
   * @see #onClickAndReleasePrimaryMouseButton()
   * @see #onClickAndReleaseSecondaryMouseButton()
   * @see #isEnabled()
   * @see #enable()
   * @see #disable()
   * @see Screen#isEffectActive(de.lessvoid.nifty.effects.EffectEventId)
   * @see de.lessvoid.nifty.effects.EffectEventId#onStartScreen
   * @see de.lessvoid.nifty.effects.EffectEventId#onEndScreen
   * @see de.lessvoid.nifty.Nifty#getCurrentScreen()
   */
  public void onClickAndReleaseTertiaryMouseButton() {
    if (!canHandleInteraction()) {
      return;
    }

    interaction.clickAndReleaseTertiaryMouseButton(nifty);
  }

  private boolean canHandleInteraction() {
    if (screen == null || !enabled) {
      return false;
    }

    return !screen.isEffectActive(EffectEventId.onStartScreen) && !screen.isEffectActive(EffectEventId.onEndScreen);
  }

  /**
   * @return the element or null if an element with the specified id cannot be found
   */
  @Nullable
  public Element findElementById(@Nullable final String findId) {
    if (findId == null) {
      return null;
    }

    if (id != null && id.equals(findId)) {
      return this;
    }

    if (childIdMatch(findId, id)) {
      return this;
    }

    if (children != null) {
      final int childrenCount = children.size();
      for (int i = 0; i < childrenCount; i++) {
        Element e = children.get(i);
        Element found = e.findElementById(findId);
        if (found != null) {
          return found;
        }
      }
    }

    return null;
  }

  private boolean childIdMatch(@Nonnull final String name, @Nullable final String id) {
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

  public void setAlternateKey(@Nullable final String alternateKey) {
    effectManager.setAlternateKey(alternateKey);

    if (children != null) {
      final int childrenCount = children.size();
      for (int i = 0; i < childrenCount; i++) {
        Element e = children.get(i);
        e.setAlternateKey(alternateKey);
      }
    }
  }

  @Nonnull
  public EffectManager getEffectManager() {
    return effectManager;
  }

  public void setEffectManager(@Nonnull final EffectManager effectManagerParam) {
    effectManager = effectManagerParam;
  }

  private void bindToScreen(@Nonnull final Screen newScreen) {
    screen = newScreen;
    if (id != null) {
      screen.registerElementId(id);
    }
  }

  private void bindToFocusHandler(final boolean isPopup) {
    if (!focusable) {
      return;
    }

    if (hasAncestorPopup() && !isPopup) {
      return;
    }

    if (screen == null) {
      log.severe("Trying to bind element [" + String.valueOf(getId()) + "] to focus handler while screen is not " +
          "bound.");
    } else {
      focusHandler.addElement(this, screen.findElementById(focusableInsertBeforeElementId));
    }
  }

  private boolean hasAncestorPopup() {
    return findAncestorPopupElement() != null;
  }

  @Nullable
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
    if (children != null) {
      final int childrenCount = children.size();
      for (int i = 0; i < childrenCount; i++) {
        Element e = children.get(i);
        e.onStartScreenSubscribeControllerAnnotations();
      }
    }
    if (attachedInputControl != null) {
      nifty.subscribeAnnotations(attachedInputControl.getController());
    }
  }

  private void onStartScreenInternal() {
    if (children != null) {
      final int childrenCount = children.size();
      for (int i = 0; i < childrenCount; i++) {
        Element e = children.get(i);
        e.onStartScreenInternal();
      }
    }
    if (screen == null) {
      log.severe("Internal start of screen called, but no screen is bound to the element [" + String.valueOf(getId())
          + "]");
    } else {
      if (attachedInputControl != null) {
        attachedInputControl.onStartScreen(nifty, screen);
      }
    }
  }

  /**
   * Gets this element's renderer matching the specified class.
   *
   * @param <T>                    the {@link de.lessvoid.nifty.elements.render.ElementRenderer} type to check for
   * @param requestedRendererClass the {@link de.lessvoid.nifty.elements.render.ElementRenderer} class to check for
   * @return the {@link de.lessvoid.nifty.elements.render.ElementRenderer} that matches the specified class, or null if
   * there is no matching renderer
   */
  @Nullable
  public <T extends ElementRenderer> T getRenderer(@Nonnull final Class<T> requestedRendererClass) {
    for (int i = 0; i < elementRenderer.length; i++) {
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

  public boolean keyEvent(@Nonnull final KeyboardInputEvent inputEvent) {
    if (attachedInputControl != null && id != null) {
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
    if (parent != null) {
      parent.renderOrderChanged(this);
    }
  }

  private void renderOrderChanged(@Nonnull final Element element) {
    if (elementsRenderOrderSet == null) {
      log.warning("Can't report a changed order, parent doesn't seem to have children?! O.o");
      return;
    }
    Iterator<Element> childItr = elementsRenderOrderSet.iterator();
    boolean foundOldEntry = false;
    while (childItr.hasNext()) {
      Element checkElement = childItr.next();
      if (checkElement.equals(element)) {
        childItr.remove();
        foundOldEntry = true;
        break;
      }
    }
    if (foundOldEntry) {
      elementsRenderOrderSet.add(element);
      if (elementsRenderOrder == null || elementsRenderOrder.length != elementsRenderOrderSet.size()) {
        elementsRenderOrder = new Element[elementsRenderOrderSet.size()];
      }
      elementsRenderOrder = elementsRenderOrderSet.toArray(elementsRenderOrder);
    } else {
      log.warning("Failed to locate the element with changed id in the render set.");
    }
  }

  public int getRenderOrder() {
    return renderOrder;
  }

  /**
   * Attempts to set the focus to this element.
   */
  public void setFocus() {
    if (nifty.getCurrentScreen() != null) {
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
    if (children != null) {
      final int childrenCount = children.size();
      for (int i = 0; i < childrenCount; i++) {
        Element child = children.get(i);
        child.resetInteractionBlocked();
      }
    }
  }
  
  /**
  * An internal class that handles a possible style changing.
  * @see Nifty#registerStyle(de.lessvoid.nifty.loaderv2.types.StyleType)
  */
  private class StyleRefresh implements EventTopicSubscriber {
    @Override
    public void onEvent(final String string, final Object style) {
      log.fine("Rereshing style of " + Element.this.getId());
      String styleid = style.toString();
      String simplyStyle = styleid.split("#")[0];
      Element.this.setStyle(simplyStyle);
    }
  }

  /**
   * @author void
   */
  public class LocalEndNotify implements EndNotify {
    @Nonnull
    private final EffectEventId effectEventId;
    @Nullable
    private final EndNotify effectEndNotify;

    public LocalEndNotify(
        @Nonnull final EffectEventId effectEventIdParam,
        @Nullable final EndNotify effectEndNotifyParam) {
      effectEventId = effectEventIdParam;
      effectEndNotify = effectEndNotifyParam;
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
      // a) the effect is done for our self
      // b) the effect is done for all of our children
      if (!isEffectActive(effectEventId)) {
        // all fine. we can notify the actual event handler
        if (effectEndNotify != null) {
          effectEndNotify.perform();
        }
      }
    }
  }

  public void setId(@Nullable final String id) {
    @Nullable String oldId = this.id;
    this.id = id;

    if (parent == null) {
      return;
    }

    if (oldId == null && id == null) {
      return;
    }
    if ((oldId != null && oldId.equals(id)) || (id != null && id.equals(oldId))) {
      return;
    }
    /*
      So the ID changed and we got a parent. This means the render order set is likely to be corrupted now. We need
      to update it to ensure that everything is still working properly.
     */
    parent.renderOrderChanged(this);
  }

  @Nonnull
  public ElementType getElementType() {
    return elementType;
  }

  @Nonnull
  public ElementRenderer[] getElementRenderer() {
    return elementRenderer;
  }

  public void setFocusable(final boolean isFocusable) {
    this.focusable = isFocusable;
    if (children != null) {
      final int childrenCount = children.size();
      for (int i = 0; i < childrenCount; i++) {
        Element child = children.get(i);
        child.setFocusable(isFocusable);
      }
    }
  }

  @Nullable
  public NiftyInputControl getAttachedInputControl() {
    return attachedInputControl;
  }

  public void bindControls(@Nonnull final Screen target) {
    if (screen == target) {
      return;
    }
    bindToScreen(target);
    if (children != null) {
      final int childrenCount = children.size();
      for (int i = 0; i < childrenCount; i++) {
        Element child = children.get(i);
        child.bindControls(target);
      }
    }
    if (attachedInputControl != null) {
      attachedInputControl.bindControl(nifty, target, this, elementType.getAttributes());
    }
  }

  public void initControls(final boolean isPopup) {
    if (children != null) {
      final int childrenCount = children.size();
      for (int i = 0; i < childrenCount; i++) {
        Element child = children.get(i);
        child.initControls(isPopup);
      }
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
    if (children != null) {
      final int childrenCount = children.size();
      for (int i = 0; i < childrenCount; i++) {
        Element child = children.get(i);
        child.removeFromFocusHandler();
      }
    }
  }

  @Nonnull
  public FocusHandler getFocusHandler() {
    return focusHandler;
  }

  public void setStyle(@Nonnull final String newStyle) {
    final String oldStyle = getStyle();
    if (oldStyle != null) {
      removeStyle(oldStyle);
      nifty.getEventService().unsubscribe("style-refresh:" + oldStyle, styleListener);
    }
    elementType.getAttributes().set("style", newStyle);
    elementType.applyStyles(nifty.getDefaultStyleResolver());
    if (screen == null) {
      log.warning("Can't properly apply style as long as the element is not bound to a screen.");
    } else {
      elementType.applyAttributes(screen, this, elementType.getAttributes(), nifty.getRenderEngine());
      elementType.applyEffects(nifty, screen, this);
      elementType.applyInteract(nifty, screen, this);
    }
    layoutElements();
    log.fine("after setStyle [" + newStyle + "]\n" + elementType.output(0));
    publishEvent();
  }

  @Nullable
  public String getStyle() {
    return elementType.getAttributes().get("style");
  }

  void removeStyle(@Nonnull final String style) {
    log.fine("before removeStyle [" + style + "]\n" + elementType.output(0));

    elementType.removeWithTag(style);
    effectManager.removeAllEffects();
     
    // If the new style has no image the renderer will still display it 
    // workaround :
    ImageRenderer renderer = getRenderer(ImageRenderer.class);
    if (renderer!= null) {
      renderer.setImage(null);
    }
    log.fine("after removeStyle [" + style + "]\n" + elementType.output(0));
    publishEvent();
  }

  /**
   * Adds an additional input handler to this element and its children.
   *
   * @param handler additional handler
   */
  public void addInputHandler(@Nonnull final KeyInputHandler handler) {
    if (attachedInputControl != null) {
      attachedInputControl.addInputHandler(handler);
    }
    if (children != null) {
      final int childrenCount = children.size();
      for (int i = 0; i < childrenCount; i++) {
        Element child = children.get(i);
        child.addInputHandler(handler);
      }
    }
  }

  /**
   * Adds an additional input handler to this element and its children.
   *
   * @param handler additional handler
   */
  public void addPreInputHandler(@Nonnull final KeyInputHandler handler) {
    if (attachedInputControl != null) {
      attachedInputControl.addPreInputHandler(handler);
    }
    if (children != null) {
      final int childrenCount = children.size();
      for (int i = 0; i < childrenCount; i++) {
        Element child = children.get(i);
        child.addPreInputHandler(handler);
      }
    }
  }

  @Nullable
  public <T extends Controller> T findControl(
      @Nonnull final String elementName,
      @Nonnull final Class<T> requestedControlClass) {
    Element element = findElementById(elementName);
    if (element == null) {
      return null;
    }
    return element.getControl(requestedControlClass);
  }

  @Nullable
  public <T extends NiftyControl> T findNiftyControl(
      @Nonnull final String elementName,
      @Nonnull final Class<T> requestedControlClass) {
    Element element = findElementById(elementName);
    if (element == null) {
      return null;
    }
    return element.getNiftyControl(requestedControlClass);
  }

  @Nullable
  public <T extends Controller> T getControl(@Nonnull final Class<T> requestedControlClass) {
    if (attachedInputControl != null) {
      T t = attachedInputControl.getControl(requestedControlClass);
      if (t != null) {
        return t;
      }
    }
    if (children != null) {
      final int childrenCount = children.size();
      for (int i = 0; i < childrenCount; i++) {
        Element child = children.get(i);
        T t = child.getControl(requestedControlClass);
        if (t != null) {
          return t;
        }
      }
    }
    return null;
  }

  @Nullable
  public <T extends NiftyControl> T getNiftyControl(@Nonnull final Class<T> requestedControlClass) {
    if (attachedInputControl != null) {
      T t = attachedInputControl.getNiftyControl(requestedControlClass);
      if (t != null) {
        return t;
      }
    }
    if (children != null) {
      final int childrenCount = children.size();
      for (int i = 0; i < childrenCount; i++) {
        Element child = children.get(i);
        T t = child.getNiftyControl(requestedControlClass);
        if (t != null) {
          return t;
        }
      }
    }
    log.warning("missing element/control with id [" + id + "] for requested control class [" +
        requestedControlClass.getName() + "]");
    return null;
  }

  public boolean isFocusable() {
    return focusable && enabled && visible && hasVisibleParent() && !isIgnoreKeyboardEvents();
  }

  private boolean hasVisibleParent() {
    if (parent != null) {
      return parent.visible && parent.hasVisibleParent();
    }
    return true;
  }

  public void setOnMouseOverMethod(@Nullable final NiftyMethodInvoker onMouseOverMethod) {
    interaction.setOnMouseOver(onMouseOverMethod);
  }

  @Nonnull
  public LayoutPart getLayoutPart() {
    return layoutPart;
  }

  public boolean isVisibleToMouseEvents() {
    return visibleToMouseEvents;
  }

  @Nonnull
  public SizeValue getPaddingLeft() {
    return layoutPart.getBoxConstraints().getPaddingLeft();
  }

  @Nonnull
  public SizeValue getPaddingRight() {
    return layoutPart.getBoxConstraints().getPaddingRight();
  }

  @Nonnull
  public SizeValue getPaddingTop() {
    return layoutPart.getBoxConstraints().getPaddingTop();
  }

  @Nonnull
  public SizeValue getPaddingBottom() {
    return layoutPart.getBoxConstraints().getPaddingBottom();
  }

  @Nonnull
  public SizeValue getMarginLeft() {
    return layoutPart.getBoxConstraints().getMarginLeft();
  }

  @Nonnull
  public SizeValue getMarginRight() {
    return layoutPart.getBoxConstraints().getMarginRight();
  }

  @Nonnull
  public SizeValue getMarginTop() {
    return layoutPart.getBoxConstraints().getMarginTop();
  }

  @Nonnull
  public SizeValue getMarginBottom() {
    return layoutPart.getBoxConstraints().getMarginBottom();
  }

  public void setPaddingLeft(@Nonnull final SizeValue paddingValue) {
    layoutPart.getBoxConstraints().setPaddingLeft(paddingValue);
    notifyListeners();
  }

  public void setPaddingRight(@Nonnull final SizeValue paddingValue) {
    layoutPart.getBoxConstraints().setPaddingRight(paddingValue);
    notifyListeners();
  }

  public void setPaddingTop(@Nonnull final SizeValue paddingValue) {
    layoutPart.getBoxConstraints().setPaddingTop(paddingValue);
    notifyListeners();
  }

  public void setPaddingBottom(@Nonnull final SizeValue paddingValue) {
    layoutPart.getBoxConstraints().setPaddingBottom(paddingValue);
    notifyListeners();
  }

  public void setMarginLeft(@Nonnull final SizeValue value) {
    layoutPart.getBoxConstraints().setMarginLeft(value);
    notifyListeners();
  }

  public void setMarginRight(@Nonnull final SizeValue value) {
    layoutPart.getBoxConstraints().setMarginRight(value);
    notifyListeners();
  }

  public void setMarginTop(@Nonnull final SizeValue value) {
    layoutPart.getBoxConstraints().setMarginTop(value);
    notifyListeners();
  }

  public void setMarginBottom(@Nonnull final SizeValue value) {
    layoutPart.getBoxConstraints().setMarginBottom(value);
    notifyListeners();
  }

  @Override
  @Nonnull
  public String toString() {
    return id + " (" + super.toString() + ")";
  }

  public boolean isStarted() {
    return isEffectActive(EffectEventId.onStartScreen);
  }

  public void markForRemoval() {
    markForRemoval(null);
  }

  public void markForRemoval(@Nullable final EndNotify endNotify) {
    if (screen == null) {
      log.warning("Marking the element [" + String.valueOf(getId()) + "] for removal is not possible when there is " +
          "not screen bound.");
    } else {
      nifty.removeElement(screen, this, endNotify);
    }
  }

  public void markForMove(@Nonnull final Element destination) {
    markForMove(destination, null);
  }

  public void markForMove(@Nonnull final Element destination, @Nullable final EndNotify endNotify) {
    if (screen == null) {
      log.warning("Marking the element [" + String.valueOf(getId()) + "] for moving is not possible when there is not" +
          " screen bound.");
    } else {
      nifty.moveElement(screen, this, destination, endNotify);
    }
  }

  public void reactivate() {
    done = false;
    if (children != null) {
      final int childrenCount = children.size();
      for (int i = 0; i < childrenCount; i++) {
        children.get(i).reactivate();
      }
    }
  }

  private void notifyListeners() {
    constraintsChanged = true;
  }

  private void publishEvent() {
    if (id != null) {
      nifty.publishEvent(id, this);
    }
  }

  @Nonnull
  public Nifty getNifty() {
    return nifty;
  }

  @Nonnull
  public <T extends EffectImpl> List<Effect> getEffects(
      @Nonnull final EffectEventId effectEventId,
      @Nonnull final Class<T> requestedClass) {
    return effectManager.getEffects(effectEventId, requestedClass);
  }

  public void onEndScreen(@Nonnull final Screen screen) {
    if (id != null) {
      screen.unregisterElementId(id);
      nifty.unsubscribeElement(screen, id);
    }

    if (attachedInputControl != null) {
      attachedInputControl.onEndScreen(nifty, screen, id);
    }
    if (children != null) {
      final int childrenCount = children.size();
      for (int i = 0; i < childrenCount; i++) {
        children.get(i).onEndScreen(screen);
      }
    }
  }

  @Nonnull
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
  public void effectStateChanged(@Nonnull final EffectEventId eventId, final boolean active) {
    // Get the oldState first.
    boolean oldState = effectStateCache.get(eventId);

    // The given EffectEventId changed its state. This means we now must update
    // the ElementEffectStartedCache for this element. We do this by recalculating
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

  private boolean isEffectActiveRecalc(@Nonnull final EffectEventId eventId) {
    if (children != null) {
      final int childrenCount = children.size();
      for (int i = 0; i < childrenCount; i++) {
        Element w = children.get(i);
        if (w.isEffectActiveRecalc(eventId)) {
          return true;
        }
      }
    }
    return effectManager.isActive(eventId);
  }

  // package private to prevent public access
  void internalRemoveElement(@Nonnull final Element element) {
    if (elementsRenderOrderSet != null && children != null) {
      // so now that's odd: we need to remove the element first from the
      // elementsRenderOrder and THEN from the elements list. this is because
      // the elementsRenderOrder comparator uses the index of the element in
      // the elements list >_<
      //
      // the main issue here is of course the splitted data structure. something
      // we need to address in 1.4 or 2.0.
      elementsRenderOrderSet.remove(element);

      // now that the element has been removed from the elementsRenderOrder set
      // we can remove it from the elements list as well.
      children.remove(element);

      if (children.isEmpty()) {
        elementsRenderOrderSet = null;
        children = null;
      } else if (children.size() != elementsRenderOrderSet.size()) {
        log.severe("Problem at removing a element. RenderOrderSet and children list don't have the same size " +
            "anymore. Rebuilding the render order set.");
        elementsRenderOrderSet.clear();
        elementsRenderOrderSet.addAll(children);
      }
    }

    if (elementsRenderOrderSet != null) {
      elementsRenderOrder = elementsRenderOrderSet.toArray(new Element[elementsRenderOrderSet.size()]);
    } else {
      elementsRenderOrder = null;
    }
  }

  // package private to prevent public access
  void internalRemoveElementWithChildren() {
    if (children != null) {
      final int childrenCount = children.size();
      for (int i = 0; i < childrenCount; i++) {
        children.get(i).internalRemoveElementWithChildren();
      }
    }

    elementsRenderOrderSet = null;
    children = null;
    elementsRenderOrder = null;
  }

  /**
   * Sets custom user data for this element.
   *
   * @param key the key for the object to set
   */
  public void setUserData(@Nonnull final String key, @Nullable final Object data) {
    if (data == null) {
      if (userData != null) {
        userData.remove(key);
        if (userData.isEmpty()) {
          userData = null;
        }
      }
    } else {
      if (userData == null) {
        userData = new HashMap<String, Object>();
      }
      userData.put(key, data);
    }
  }

  /**
   * Gets custom user data set with {@link #setUserData(String, Object)} by key
   */
  @Nullable
  @SuppressWarnings("unchecked")
  public <T> T getUserData(@Nonnull final String key) {
    if (userData == null) {
      return null;
    }
    return (T) userData.get(key);
  }

  /**
   * Gets all custom user data keys set with {@link #setUserData(String, Object)}
   */
  @Nonnull
  public Set<String> getUserDataKeys() {
    if (userData != null) {
      return userData.keySet();
    }
    return Collections.emptySet();
  }

  /**
   * This uses the renderOrder attribute of the elements to compare them. If the renderOrder
   * attribute is not set (is 0) then the index of the element in the elements list is used
   * as the renderOrder value. This is done to keep the original sort order of the elements for
   * rendering. The value is not cached and is directly recalculated using the element index in
   * the list. The child count for an element is usually low (< 10) and the comparator is only
   * executed when the child elements change. So this lookup shouldn't hurt performance too much.
   * <p/>
   * If you change the default value of renderOrder then your value is being used. So if you set it
   * to some high value (> 1000 to be save) this element is rendered after all the other elements.
   * If you set it to some very low value (< -1000 to be save) then this element is rendered before
   * all the others.
   */
  private static final class RenderOrderComparator implements Comparator<Element> {
    @Override
    public int compare(@Nonnull final Element o1, @Nonnull final Element o2) {
      if (o1 == o2) {
        return 0;
      }
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
      if (o1Id == null && o2Id != null) {
        return -1;
      } else if (o1Id != null && o2Id == null) {
        return 1;
      } else if (o1Id != null) {
        int idCompareResult = o1Id.compareTo(o2Id);
        if (idCompareResult != 0) {
          return idCompareResult;
        }
      }

      // ids equal or both null use super.toString()
      // hashCode() should return a value thats different for both elements since
      // adding the same element twice to the same parent element is not supported.
      return Integer.valueOf(o1.hashCode()).compareTo(Integer.valueOf(o2.hashCode()));
    }

    private int getRenderOrder(@Nonnull final Element element) {
      if (element.renderOrder != 0) {
        return element.renderOrder;
      }
      return element.getParent().getChildren().indexOf(element);
    }
  }

  // We don't want to give up Java 1.6 compatibility right now.
  @SuppressWarnings("unchecked")
  @Nonnull
  private static <T> Iterator<T> emptyIterator() {
    return (Iterator<T>) EmptyIterator.EMPTY_ITERATOR;
  }

  private static class EmptyIterator<E> implements Iterator<E> {
    static final EmptyIterator<Object> EMPTY_ITERATOR = new EmptyIterator<Object>();

    @Override
    public boolean hasNext() {
      return false;
    }

    @Override
    @Nonnull
    public E next() {
      throw new NoSuchElementException();
    }

    @Override
    public void remove() {
      throw new IllegalStateException();
    }
  }
}
