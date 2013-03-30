package de.lessvoid.nifty.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Pattern;

import de.lessvoid.nifty.api.HorizontalAlignment;
import de.lessvoid.nifty.api.Nifty;
import de.lessvoid.nifty.api.NiftyColor;
import de.lessvoid.nifty.api.NiftyNode;
import de.lessvoid.nifty.api.UnitValue;
import de.lessvoid.nifty.api.VerticalAlignment;

public class InternalNiftyNode implements de.lessvoid.nifty.api.NiftyNode, InternalLayoutable {
  private final StringBuilder builder = new StringBuilder();

  // The Nifty instance this NiftyNode belongs to.
  private final Nifty nifty;

  // The id of this element.
  private final String id;

  // The box.
  private final InternalBox layoutPos = new InternalBox();

  // The box constraints.
  private final InternalBoxConstraints constraints = new InternalBoxConstraints();

  // The child elements of this element.
  private final List<InternalNiftyNode> children = new CopyOnWriteArrayList<InternalNiftyNode>();

  // The parent node.
  private InternalNiftyNode parentNode;

  // The childLayout.
  private ChildLayout childLayout = ChildLayout.None;

  // Does this node needs layout? This will be set to false when this Node has been layed out by its parent.
  private boolean needsLayout = true;

  // The pseudo ChildLayout if this node is a root node. This field is only used when this node is a root node.
  private final ChildLayout rootNodePseudoParentLayout;

  // The pseudo InternalLayoutable that is always screen size and is only used if this node is a root node.
  private final InternalLayoutable rootNodePseudoLayoutable;

  // The pseudo list of children. Only used when we are a root node.
  private final List<InternalLayoutable> rootNodePseudoChildren = new ArrayList<InternalLayoutable>();

  // The backgroundColor of the NiftyNode. 
  private NiftyColor backgroundColor = NiftyColor.TRANSPARENT();

  public static InternalNiftyNode newNode(final Nifty nifty, final String id, final InternalNiftyNode parent) {
    return new InternalNiftyNode(
        nifty,
        id,
        parent,
        null,
        null);
  }

  public static InternalNiftyNode newRootNode(final Nifty nifty, final String id, final ChildLayout ueberLayout) {
    return new InternalNiftyNode(
        nifty,
        id,
        null,
        ueberLayout,
        new InternalLayoutableScreenSized(nifty.getScreenWidth(), nifty.getScreenHeight()));
  }

  // Nifty API interface implementation

  @Override
  public void setWidthConstraint(final UnitValue unitValue) {
    constraints.setWidth(unitValue);
  }

  @Override
  public void setHeightConstraint(final UnitValue unitValue) {
    constraints.setHeight(unitValue);
  }

  @Override
  public void setChildLayout(final ChildLayout childLayout) {
    assertNotNull(childLayout);
    this.childLayout = childLayout;
  }

  @Override
  public NiftyNode createChildNode() {
    return createChildNodeInternal();
  }

  @Override
  public NiftyNode createChildNode(final ChildLayout childLayout) {
    NiftyNode result = createChildNodeInternal();
    result.setChildLayout(childLayout);
    return result;
  }

  @Override
  public NiftyNode createChildNode(final UnitValue width, final UnitValue height) {
    NiftyNode result = createChildNodeInternal();
    result.setWidthConstraint(width);
    result.setHeightConstraint(height);
    return result;
  }

  @Override
  public NiftyNode createChildNode(final UnitValue width, final UnitValue height, final ChildLayout childLayout) {
    NiftyNode result = createChildNodeInternal();
    result.setWidthConstraint(width);
    result.setHeightConstraint(height);
    result.setChildLayout(childLayout);
    return result;
  }

  @Override
  public void setHorizontalAlignment(final HorizontalAlignment alignment) {
    constraints.setHorizontalAlign(alignment);
  }

  @Override
  public void setVerticalAlignment(final VerticalAlignment alignment) {
    constraints.setVerticalAlign(alignment);
  }

  /**
   * get x.
   * @return x position of this element.
   */
  @Override
  public int getX() {
    assertLayout();
    return layoutPos.getX();
  }

  /**
   * get y.
   * @return the y position of this element.
   */
  @Override
  public int getY() {
    assertLayout();
    return layoutPos.getY();
  }

  /**
   * get height.
   * @return the height of this element.
   */
  @Override
  public int getHeight() {
    assertLayout();
    return layoutPos.getHeight();
  }

  /**
   * get width.
   * @return the width of this element.
   */
  @Override
  public int getWidth() {
    assertLayout();
    return layoutPos.getWidth();
  }

  @Override
  public void setBackgroundColor(final NiftyColor color) {
    backgroundColor  = color;
  }

  // Layoutable Implementation

  @Override
  public InternalBox getLayoutPos() {
    return layoutPos;
  }

  @Override
  public InternalBoxConstraints getBoxConstraints() {
    return constraints;
  }

  // Object overrides

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

  @Override
  public void getStateInfo(final StringBuilder result) {
    getStateInfo(result, "", Pattern.compile(".*"));
  }

  @Override
  public void getStateInfo(final StringBuilder result, final String pattern) {
    getStateInfo(result, "", Pattern.compile(pattern));
  }

  public void getStateInfo(final StringBuilder result, final String offset, final Pattern pattern) {
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

  // Private Methods

  private InternalNiftyNode(
      final Nifty nifty,
      final String id,
      final InternalNiftyNode parentNode,
      final ChildLayout rootNodePseudoParentLayout,
      final InternalLayoutable rootNodePseudoLayoutable) {
    this.nifty = nifty;
    this.id = id;
    this.parentNode = parentNode;
    this.needsLayout = true;
    this.rootNodePseudoParentLayout = rootNodePseudoParentLayout;
    this.rootNodePseudoLayoutable = rootNodePseudoLayoutable;

    if (this.parentNode == null) {
      rootNodePseudoChildren.add(this);
    } else {
      this.parentNode.children.add(this);
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

  private NiftyNode createChildNodeInternal() {
    return InternalNiftyNode.newNode(nifty, InternalNiftyIdGenerator.generate(), this);
  }

  private void assertNotNull(final ChildLayout param) {
    if (param == null) {
      throw new IllegalArgumentException("ChildLayout must not be null. Use ChildLayout.None instead");
    }
  }
}
