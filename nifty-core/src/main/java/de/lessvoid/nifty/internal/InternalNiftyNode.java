package de.lessvoid.nifty.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Pattern;

import de.lessvoid.nifty.api.HorizontalAlignment;
import de.lessvoid.nifty.api.Nifty;
import de.lessvoid.nifty.api.NiftyCanvas;
import de.lessvoid.nifty.api.NiftyCanvasPainter;
import de.lessvoid.nifty.api.NiftyColor;
import de.lessvoid.nifty.api.NiftyNode.ChildLayout;
import de.lessvoid.nifty.api.UnitValue;
import de.lessvoid.nifty.api.VerticalAlignment;
import de.lessvoid.nifty.internal.accessor.NiftyCanvasAccessor;
import de.lessvoid.nifty.internal.accessor.NiftyNodeAccessor;
import de.lessvoid.nifty.internal.canvas.InternalNiftyCanvas;
import de.lessvoid.nifty.internal.canvas.InternalNiftyCanvasPainterStandard;
import de.lessvoid.nifty.internal.common.Box;
import de.lessvoid.nifty.internal.common.InternalIdGenerator;
import de.lessvoid.nifty.internal.layout.InternalBoxConstraints;
import de.lessvoid.nifty.internal.layout.InternalLayoutable;
import de.lessvoid.nifty.internal.layout.InternalLayoutableScreenSized;

public class InternalNiftyNode implements InternalLayoutable {
  private final StringBuilder builder = new StringBuilder();

  // The Nifty instance this NiftyNode belongs to.
  private final Nifty nifty;

  // The id of this element.
  private final int id = InternalIdGenerator.generate();

  // The box.
  private final Box layoutPos = new Box();

  // The box constraints.
  private final InternalBoxConstraints constraints = new InternalBoxConstraints();

  // The child elements of this element and the helper to easily iterator over the list.
  private final List<InternalNiftyNode> children = new CopyOnWriteArrayList<InternalNiftyNode>();

  // The parent node.
  private InternalNiftyNode parentNode;

  // The childLayout.
  private ChildLayout childLayout = ChildLayout.None;

  // Does this node needs layout? This will be set to false when this Node has been layed out by its parent.
  private boolean needsLayout = true;

  // Does this node needs to be redrawn? This will be set to false once the Node content has been drawn.
  private boolean needsRedraw = true;

  // The pseudo ChildLayout if this node is a root node. This field is only used when this node is a root node.
  private final ChildLayout rootNodePseudoParentLayout;

  // The pseudo InternalLayoutable that is always screen size and is only used if this node is a root node.
  private final InternalLayoutable rootNodePseudoLayoutable;

  // The pseudo list of children. Only used when we are a root node.
  private final List<InternalLayoutable> rootNodePseudoChildren = new ArrayList<InternalLayoutable>();

  // The backgroundColor of the NiftyNode. 
  private NiftyColor backgroundColor = NiftyColor.TRANSPARENT();

  // If you don't set a specific NiftyCanvasPainter we use this one
  private static InternalNiftyCanvasPainterStandard standardCanvasPainter = new InternalNiftyCanvasPainterStandard();

  // The canvas.
  private NiftyCanvas canvas;

  // When set to true, Nifty will render all childrens into a texture.
  private boolean cache;

  private double angleZ;

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // Factory methods
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  public static InternalNiftyNode newNode(final Nifty nifty, final InternalNiftyNode parent) {
    return new InternalNiftyNode(
        nifty,
        parent,
        null,
        null);
  }

  public static InternalNiftyNode newRootNode(final Nifty nifty, final ChildLayout ueberLayout) {
    return new InternalNiftyNode(
        nifty,
        null,
        ueberLayout,
        new InternalLayoutableScreenSized(nifty.getScreenWidth(), nifty.getScreenHeight()));
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // Nifty API "interface" implementation
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  public void setXConstraint(final UnitValue value) {
    constraints.setX(value);
    needsLayout = true;
  }

  public void setYConstraint(final UnitValue value) {
    constraints.setY(value);
    needsLayout = true;
  }

  public void setWidthConstraint(final UnitValue unitValue) {
    constraints.setWidth(unitValue);
    needsLayout = true;
  }

  public void setHeightConstraint(final UnitValue unitValue) {
    constraints.setHeight(unitValue);
    needsLayout = true;
  }

  public void setChildLayout(final ChildLayout childLayout) {
    assertNotNull(childLayout);
    this.childLayout = childLayout;
    needsLayout = true;
  }

  public InternalNiftyNode newChildNode() {
    return createChildNodeInternal();
  }

  public InternalNiftyNode newChildNode(final ChildLayout childLayout) {
    InternalNiftyNode result = createChildNodeInternal();
    result.setChildLayout(childLayout);
    return result;
  }

  public InternalNiftyNode newChildNode(final UnitValue width, final UnitValue height) {
    InternalNiftyNode result = createChildNodeInternal();
    result.setWidthConstraint(width);
    result.setHeightConstraint(height);
    return result;
  }

  public InternalNiftyNode newChildNode(final UnitValue width, final UnitValue height, final ChildLayout childLayout) {
    InternalNiftyNode result = createChildNodeInternal();
    result.setWidthConstraint(width);
    result.setHeightConstraint(height);
    result.setChildLayout(childLayout);
    return result;
  }

  public void setHorizontalAlignment(final HorizontalAlignment alignment) {
    constraints.setHorizontalAlign(alignment);
    needsLayout = true;
  }

  public void setVerticalAlignment(final VerticalAlignment alignment) {
    constraints.setVerticalAlign(alignment);
    needsLayout = true;
  }

  public int getX() {
    assertLayout();
    return layoutPos.getX();
  }

  public int getY() {
    assertLayout();
    return layoutPos.getY();
  }

  public int getHeight() {
    assertLayout();
    return layoutPos.getHeight();
  }

  public int getWidth() {
    assertLayout();
    return layoutPos.getWidth();
  }

  public void setBackgroundColor(final NiftyColor color) {
    backgroundColor  = color;
    needsRedraw = true;
  }

  public void setRotation(final double angle) {
    angleZ = angle;
  }

  public double getRotationZ() {
    return angleZ;
  }

  public void setContent(final NiftyCanvasPainter painter) {
    
  }

  public void getStateInfo(final StringBuilder result) {
    getStateInfo(result, "", Pattern.compile(".*"));
  }

  public void getStateInfo(final StringBuilder result, final String pattern) {
    getStateInfo(result, "", Pattern.compile(pattern));
  }

  public void setCache(final boolean cache) {
    this.cache = cache;
  }

  public boolean isCache() {
    return cache;
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // Layoutable Implementation
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  @Override
  public Box getLayoutPos() {
    return layoutPos;
  }

  @Override
  public InternalBoxConstraints getBoxConstraints() {
    return constraints;
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // Object overrides
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  @Override
  public String toString() {
    builder.setLength(0);
    builder.append("NiftyNode [");
    builder.append(id);
    builder.append("] (");
    builder.append(super.toString());
    builder.append(")");
    return builder.toString();
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // Other stuff
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  public int getId() {
    return id;
  }

  public InternalNiftyNode getParent() {
    return parentNode;
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // Private Methods and package private stuff
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  public void updateContent() {
    if (needsRedraw) {
      InternalNiftyCanvas internalNiftyCanvas = NiftyCanvasAccessor.getDefault().getInternalNiftyCanvas(canvas);
      internalNiftyCanvas.reset();
      standardCanvasPainter.paint(this, internalNiftyCanvas);
      needsRedraw = false;
    }

    for (int i=0; i<children.size(); i++) {
      children.get(i).updateContent();
    }
  }

  public List<InternalNiftyNode> getChildren() {
    return children;
  }

  private void getStateInfo(final StringBuilder result, final String offset, final Pattern pattern) {
    String rootNodeString = "";
    if (parentNode == null) {
      rootNodeString = " {rootNode} ";
    }
    result.append(offset).append("- ").append("[").append(id).append("]").append(rootNodeString).append("\n");
    result.append(matches(pattern, statePosition(), offset + "  "));
    result.append(matches(pattern, stateConstraints(), offset + "  "));
    result.append(matches(pattern, stateBackgroundColor(), offset + "  "));
    result.append(matches(pattern, stateChildLayout(), offset + "  "));

    for (int i=0; i<children.size(); i++) {
      children.get(i).getStateInfo(result, offset + "  ", pattern);
    }
  }

  private InternalNiftyNode(
      final Nifty nifty,
      final InternalNiftyNode parentNode,
      final ChildLayout rootNodePseudoParentLayout,
      final InternalLayoutable rootNodePseudoLayoutable) {
    this.nifty = nifty;
    this.parentNode = parentNode;
    this.needsLayout = true;
    this.rootNodePseudoParentLayout = rootNodePseudoParentLayout;
    this.rootNodePseudoLayoutable = rootNodePseudoLayoutable;
    this.canvas = NiftyNodeAccessor.getDefault().createNiftyCanvas(new InternalNiftyCanvas());

    if (this.parentNode == null) {
      rootNodePseudoChildren.add(this);
    } else {
      parentNode.children.add(this);
    }
  }

  private void layoutChildren() {
    assertLayout();
    if (children.isEmpty()) {
      return;
    }
    assertChildLayout();
    childLayout.getLayout().layoutElements(this, children);

    for (int i=0; i<children.size(); i++) {
      InternalNiftyNode node = children.get(i);
      node.needsLayout = false;
      node.layoutChildren();
    }
  }

  private void assertChildLayout() {
    if (childLayout == null) {
      throw new IllegalArgumentException("childLayout for node [" + toString() + "] must not be null (has children)");
    }
  }

  private void assertLayout() {
    if (needsLayout) {
      // this node needs to be layed out. This means we'll need to call our parent node (if we have one) to layout
      // it's child elements which includes us. In case we are a rootNode we'll need to use some special layout.
      if (isRootNode()) {
        // we are a root node. we use the rootNodePseudoParentLayout for layout.
        rootNodePseudoParentLayout.getLayout().layoutElements(rootNodePseudoLayoutable, rootNodePseudoChildren);
        needsLayout = false;
        layoutChildren();
      } else {
        parentNode.layoutChildren();        
      }
    }
  }

  private boolean isRootNode() {
    return parentNode == null;
  }

  private String statePosition() {
    return "position [x=" + getX() + ", y=" + getY() + ", width=" + getWidth() + ", height=" + getHeight() + "]\n";
  }

  private String stateConstraints() {
    return
        "constraints [" +
        "x=" + constraints.getX() + ", " +
        "y=" + constraints.getY() + ", " +
        "width=" + constraints.getWidth() + ", " +
        "height=" + constraints.getHeight() + ", " +
        "horizontalAlign=" + constraints.getHorizontalAlign() + ", " +
        "verticalAlign=" + constraints.getVerticalAlign() + ", " +
        "paddingLeft=" + constraints.getPaddingLeft() + ", " +
        "paddingRight=" + constraints.getPaddingRight() + ", " +
        "paddingTop=" + constraints.getPaddingTop() + ", " +
        "paddingBottom=" + constraints.getPaddingBottom() + ", " +
        "marginLeft=" + constraints.getMarginLeft() + ", " +
        "marginRight=" + constraints.getMarginRight() + ", " +
        "marginTop=" + constraints.getMarginTop() + ", " +
        "marginBottom=" + constraints.getMarginBottom() +
        "]\n";
  }

  private String stateBackgroundColor() {
    return "backgroundColor [" + backgroundColor + "]\n";
  }

  private String stateChildLayout() {
    return "childLayout [" + childLayout + "]\n";
  }

  private String matches(final Pattern pattern, final String data, final String offset) {
    if (pattern.matcher(offset + data).find()) {
      return offset + data;
    }
    return "";
  }

  private InternalNiftyNode createChildNodeInternal() {
    return InternalNiftyNode.newNode(nifty, this);
  }

  private void assertNotNull(final ChildLayout param) {
    if (param == null) {
      throw new IllegalArgumentException("ChildLayout must not be null. Use ChildLayout.None instead");
    }
  }

  public NiftyColor getBackgroundColor() {
    return backgroundColor;
  }

  public InternalNiftyCanvas getCanvas() {
    return NiftyCanvasAccessor.getDefault().getInternalNiftyCanvas(canvas);
  }
}
