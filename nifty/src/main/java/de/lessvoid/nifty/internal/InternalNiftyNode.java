/*
 * Copyright (c) 2014, Jens Hohmuth 
 * All rights reserved. 
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are 
 * met: 
 * 
 *  * Redistributions of source code must retain the above copyright 
 *    notice, this list of conditions and the following disclaimer. 
 *  * Redistributions in binary form must reproduce the above copyright 
 *    notice, this list of conditions and the following disclaimer in the 
 *    documentation and/or other materials provided with the distribution. 
 * 
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND 
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR 
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF 
 * THE POSSIBILITY OF SUCH DAMAGE.
 */
package de.lessvoid.nifty.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Pattern;

import de.lessvoid.nifty.api.ChildLayout;
import de.lessvoid.nifty.api.HAlign;
import de.lessvoid.nifty.api.Nifty;
import de.lessvoid.nifty.api.NiftyCallback;
import de.lessvoid.nifty.api.NiftyCanvas;
import de.lessvoid.nifty.api.NiftyCanvasPainter;
import de.lessvoid.nifty.api.NiftyCanvasPainterDefault;
import de.lessvoid.nifty.api.NiftyColor;
import de.lessvoid.nifty.api.NiftyCompositeOperation;
import de.lessvoid.nifty.api.NiftyLinearGradient;
import de.lessvoid.nifty.api.NiftyMinSizeCallback;
import de.lessvoid.nifty.api.NiftyMinSizeCallback.Size;
import de.lessvoid.nifty.api.NiftyNode;
import de.lessvoid.nifty.api.NiftyNodeState;
import de.lessvoid.nifty.api.UnitValue;
import de.lessvoid.nifty.api.VAlign;
import de.lessvoid.nifty.api.controls.NiftyControl;
import de.lessvoid.nifty.api.input.NiftyPointerEvent;
import de.lessvoid.nifty.internal.accessor.NiftyCanvasAccessor;
import de.lessvoid.nifty.internal.accessor.NiftyNodeAccessor;
import de.lessvoid.nifty.internal.animate.IntervalAnimator;
import de.lessvoid.nifty.internal.canvas.InternalNiftyCanvas;
import de.lessvoid.nifty.internal.common.Box;
import de.lessvoid.nifty.internal.common.IdGenerator;
import de.lessvoid.nifty.internal.layout.InternalBoxConstraints;
import de.lessvoid.nifty.internal.layout.InternalLayoutable;
import de.lessvoid.nifty.internal.layout.InternalLayoutableScreenSized;
import de.lessvoid.nifty.internal.math.Mat4;
import de.lessvoid.nifty.internal.math.Vec4;

public class InternalNiftyNode implements InternalLayoutable {
  private final StringBuilder builder = new StringBuilder();

  // The Nifty instance this NiftyNode belongs to.
  private final Nifty nifty;

  // The id of this element.
  private final String id;

  // The box.
  private final Box layoutPos = new Box();

  // The box constraints.
  private final InternalBoxConstraints constraints = new InternalBoxConstraints();

  // The child elements of this element and the helper to easily iterator over the list.
  private final List<InternalNiftyNode> children = new CopyOnWriteArrayList<InternalNiftyNode>();

  // everything input related will be forwarded to that class
  private final InternalNiftyNodeInputHandler input = new InternalNiftyNodeInputHandler();

  // The parent node.
  private InternalNiftyNode parentNode;

  // The childLayout.
  private ChildLayout childLayout = ChildLayout.None;

  // Does this node needs layout? This will be set to false when this Node has been laid out by its parent.
  private boolean needsLayout = true;

  // Does this node needs to be redrawn? This will be set to false once the Node content has been drawn.
  private boolean requestRedraw = true;

  // The pseudo ChildLayout if this node is a root node. This field is only used when this node is a root node.
  private final ChildLayout rootNodePseudoParentLayout;

  // The pseudo InternalLayoutable that is always screen size and is only used if this node is a root node.
  private final InternalLayoutable rootNodePseudoLayoutable;

  // The pseudo list of children. Only used when we are a root node.
  private final List<InternalLayoutable> rootNodePseudoChildren = new ArrayList<InternalLayoutable>();

  // The backgroundColor of the NiftyNode. 
  private NiftyColor backgroundColor = NiftyColor.transparent();

  // The background gradient
  private NiftyLinearGradient backgroundGradient;

  // If you don't set a specific NiftyCanvasPainter we use this one
  private static final NiftyCanvasPainterDefault standardPainter = new NiftyCanvasPainterDefault();

  // The canvas.
  private NiftyCanvas canvas;

  // The canvas painters. Initialized with a single entry, the NiftyCanvasPainterDefault.
  private List<NiftyCanvasPainter> canvasPainters = new ArrayList<NiftyCanvasPainter>(Arrays.asList(standardPainter));

  // The public Node that this node is linked to.
  private NiftyNode niftyNode;

  // IntervalAnimator will execute given NiftyCallbacks at given intervals
  private List<IntervalAnimator> animators = new ArrayList<IntervalAnimator>();

  // in case an animatedRequestRedraw is set we remember it here so that we can remove it later
  private IntervalAnimator animatedRequestIntervalAnimator;

  // style class applied to that node (can be multiple classes in which case they should be whitespace seperated)
  private String styleClasses;

  // The StateManager to manage all of the states for this node.
  private final StateManager<InternalNiftyNode> stateManager = new StateManager(this);

  private boolean transformationChanged = true;
  private double pivotX = 0.5;
  private double pivotY = 0.5;
  private double angleX = 0.0;
  private double angleY = 0.0;
  private double angleZ = 0.0;
  private double scaleX = 1.0;
  private double scaleY = 1.0;
  private double scaleZ = 1.0;

  private NiftyCompositeOperation compositeOperation = NiftyCompositeOperation.SourceOver;
  private NiftyMinSizeCallback minSizeCallback;

  // this will be set to true when the constraints of this node has been calculated by this node itself using the
  // NiftyMinSizeCallback
  private boolean calculatedMinSize;

  // nodes will be sorted ascending by this int
  private int renderOrder;

  // the local transformation matrix
  // when accessing this always use the getLocalTransformation() method! it will make sure it's recalculated. 
  private Mat4 localTransformation = new Mat4();
  private Mat4 localTransformationInverse = new Mat4();
  private Mat4 screenToLocalTransformation = new Mat4();
  private Mat4 screenToLocalTransformationInverse = new Mat4();

  // when this node is stored in the list of all input event receiving list we'll store the index in that list in this
  // member variable. this will later be used as the default sort order.
  private int inputOrderIndex;

  // the EventBus for this Node - this will be lazily instantiated if it is actually needed
  private InternalNiftyEventBus eventBus;

  // if this node is the parent node of a control node hierachy then control keeps a reference to the NiftyControl
  private NiftyControl control;

  // Nifty will use NiftyNodes to display some optional data like the FPS counter. These nodes will be hidden.
  private boolean niftyPrivateNode = false;

  // As a feature Nifty supports the forcing of states through its public API. What that means, however, is that
  // Nifty will not update the acual states anymore. So if this array here contains not null than these states will
  // be used.
  private List<NiftyNodeState> forcedStates;

  // StateSetters for all properties that take part in the StateManager business. These are declared static so that we
  // can reuse them in all instances and don't generate additional GC stress.
  private static StateSetter<InternalNiftyNode, NiftyColor> stateSetterBackgroundColor = new StateSetter<InternalNiftyNode, NiftyColor>() {
    @Override
    public void set(final InternalNiftyNode target, final NiftyColor value) {
      target.backgroundColor = value;
      target.requestRedraw = true;
    }
  };
  private static StateSetter<InternalNiftyNode, NiftyLinearGradient> stateSetterLinearGradient = new StateSetter<InternalNiftyNode, NiftyLinearGradient>() {
    @Override
    public void set(final InternalNiftyNode target, final NiftyLinearGradient value) {
      target.backgroundGradient = value;
      target.requestRedraw = true;
    }
  };

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // Factory methods
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  public static InternalNiftyNode newNode(final Nifty nifty, final InternalNiftyNode parent) {
    return new InternalNiftyNode(
        String.valueOf(IdGenerator.generate()),
        nifty,
        parent,
        null,
        null);
  }

  public static InternalNiftyNode newRootNode(final Nifty nifty, final ChildLayout ueberLayout) {
    return new InternalNiftyNode(
        String.valueOf(IdGenerator.generate()),
        nifty,
        null,
        ueberLayout,
        new InternalLayoutableScreenSized(nifty.getScreenWidth(), nifty.getScreenHeight()));
  }

  public static InternalNiftyNode newNode(final String id, final Nifty nifty, final InternalNiftyNode parent) {
    return new InternalNiftyNode(
        id,
        nifty,
        parent,
        null,
        null);
  }

  public static InternalNiftyNode newRootNode(final String id, final Nifty nifty, final ChildLayout ueberLayout) {
    return new InternalNiftyNode(
        id,
        nifty,
        null,
        ueberLayout,
        new InternalLayoutableScreenSized(nifty.getScreenWidth(), nifty.getScreenHeight()));
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // Nifty API "interface" implementation
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  public String getId() {
    return id;
  }

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

  public InternalNiftyNode newChildNode(final String id) {
    return createChildNodeInternal(id);
  }

  public InternalNiftyNode newChildNode(final String id, final ChildLayout childLayout) {
    InternalNiftyNode result = createChildNodeInternal(id);
    result.setChildLayout(childLayout);
    return result;
  }

  public InternalNiftyNode newChildNode(final String id, final UnitValue width, final UnitValue height) {
    InternalNiftyNode result = createChildNodeInternal(id);
    result.setWidthConstraint(width);
    result.setHeightConstraint(height);
    return result;
  }

  public InternalNiftyNode newChildNode(final String id, final UnitValue width, final UnitValue height, final ChildLayout childLayout) {
    InternalNiftyNode result = createChildNodeInternal(id);
    result.setWidthConstraint(width);
    result.setHeightConstraint(height);
    result.setChildLayout(childLayout);
    return result;
  }

  public <T extends NiftyControl> T newControl(final Class<T> controlClass, final NiftyNode childNode) {
    try {
      T result = controlClass.newInstance();
      result.init(childNode);
      return result;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public void setHAlign(final HAlign alignment) {
    constraints.setHorizontalAlign(alignment);
    needsLayout = true;
  }

  public void setVAlign(final VAlign alignment) {
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

  public void setBackgroundColor(final NiftyColor color, final NiftyNodeState ... states) {
    stateManager.setValue("background-color", color, stateSetterBackgroundColor, states);
  }

  public void setBackgroundGradient(final NiftyLinearGradient gradient, final NiftyNodeState ... states) {
    stateManager.setValue("background-gradient", gradient, stateSetterLinearGradient, states);
  }

  public void setCompositeOperation(final NiftyCompositeOperation compositeOperation) {
    this.compositeOperation = compositeOperation;
  }

  public NiftyCompositeOperation getCompositeOperation() {
    return compositeOperation;
  }

  public double getScaleX() {
    return scaleX;
  }

  public void setScaleX(final double factor) {
    updateTransformationChanged(scaleX, factor);
    scaleX = factor;
  }

  public double getScaleY() {
    return scaleY;
  }

  public void setScaleY(final double factor) {
    updateTransformationChanged(scaleY, factor);
    scaleY = factor;
  }

  public double getScaleZ() {
    return scaleZ;
  }

  public void setScaleZ(final double factor) {
    updateTransformationChanged(scaleZ, factor);
    scaleZ = factor;
  }

  public void setPivot(final double x, final double y) {
    updateTransformationChanged(pivotX, x);
    updateTransformationChanged(pivotY, y);
    pivotX = x;
    pivotY = y;
  }

  public double getPivotX() {
    return pivotX;
  }

  public double getPivotY() {
    return pivotY;
  }

  public double getRotationX() {
    return angleX;
  }

  public void setRotationX(final double angle) {
    updateTransformationChanged(angleX, angle);
    angleX = angle;
  }

  public double getRotationY() {
    return angleY;
  }

  public void setRotationY(final double angle) {
    updateTransformationChanged(angleY, angle);
    angleY = angle;
  }

  public double getRotationZ() {
    return angleZ;
  }

  public void setRotationZ(final double angle) {
    updateTransformationChanged(angleZ, angle);
    angleZ = angle;
  }

  public void setCanvasPainter(final NiftyCanvasPainter painter) {
    canvasPainters.clear();
    canvasPainters.add(painter);
  }

  public void addCanvasPainter(final NiftyCanvasPainter painter) {
    canvasPainters.add(painter);
  }

  public void requestRedraw() {
    requestRedraw = true;
  }

  public void requestLayout() {
    needsLayout = true;
  }

  public void getStateInfo(final StringBuilder result) {
    getStateInfo(result, "", Pattern.compile(".*"));
  }

  public void getStateInfo(final StringBuilder result, final String pattern) {
    getStateInfo(result, "", Pattern.compile(pattern));
  }

  public void enableMinSize(final NiftyMinSizeCallback minSizeCallback) {
    this.minSizeCallback = minSizeCallback;
  }

  public NiftyColor getBackgroundColor() {
    return backgroundColor;
  }

  public InternalNiftyCanvas getCanvas() {
    return NiftyCanvasAccessor.getDefault().getInternalNiftyCanvas(canvas);
  }

  public boolean isTransformationChanged() {
    return transformationChanged || layoutPos.isTransformationChanged();
  }

  public void startAnimated(final long delay, final long interval, final NiftyCallback<Float> callback) {
    animators.add(new IntervalAnimator(nifty.getTimeProvider(), delay, interval, callback));
  }

  public void stopAnimatedRedraw() {
    if (animatedRequestIntervalAnimator == null) {
      return;
    }
    animators.remove(animatedRequestIntervalAnimator);
    animatedRequestIntervalAnimator = null;
  }

  public void subscribe(final Object object) {
    eventBus().subscribe(object);
  }

  public void forceStates(final NiftyNodeState ... newStates) {
    if (newStates == null || newStates.length == 0) {
      forcedStates = null;
    } else {
      forcedStates = Arrays.asList(newStates);
    }
    stateManager.activateStates(forcedStates.toArray(new NiftyNodeState[0]));
  }

  // This method is called internally by methods in this class to set new states
  private void setStatesInternal(final NiftyNodeState ... newStates) {
    // if we have forcedStates than we will ignore any calls
    if (forcedStates != null) {
      return;
    }
    stateManager.activateStates(newStates);
  }

  public void setStyleClass(final String classes) {
    this.styleClasses = classes;
  }

  public String getStyleClass() {
    return styleClasses;
  }

  public NiftyLinearGradient getBackgroundGradient() {
    return backgroundGradient;
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
    builder.append(id);
    builder.append("@");
    builder.append(super.hashCode());
    return builder.toString();
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // Other stuff
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  public InternalNiftyNode getParent() {
    return parentNode;
  }

  public void setNiftyNode(final NiftyNode niftyNode) {
    this.niftyNode = niftyNode;
  }

  public NiftyNode getNiftyNode() {
    return niftyNode;
  }

  public boolean isRootNode() {
    return parentNode == null;
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // Private Methods and package private stuff
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  public void update() {
    assertLayout();

    for (int i=0; i<animators.size(); i++) {
      animators.get(i).update();
    }

    if (requestRedraw) {
      InternalNiftyCanvas internalCanvas = NiftyCanvasAccessor.getDefault().getInternalNiftyCanvas(canvas);
      internalCanvas.reset();

      for (int i=0; i<canvasPainters.size(); i++) {
        canvasPainters.get(i).paint(niftyNode, canvas);
      }

      requestRedraw = false;
    }

    for (int i=0; i<children.size(); i++) {
      children.get(i).update();
    }
  }

  public void addInputNodes(final List<NiftyNode> nodesToReceiveEvents) {
    // FIXME actually check if this node is able to receive input events .. for now we add all nodes
    nodesToReceiveEvents.add(niftyNode);
    NiftyNodeAccessor.getDefault().getInternalNiftyNode(niftyNode).setInputOrderIndex(nodesToReceiveEvents.size() - 1);

    for (int i=0; i<children.size(); i++) {
      children.get(i).addInputNodes(nodesToReceiveEvents);
    }
  }

  private void setInputOrderIndex(final int inputOrderIndex) {
    this.inputOrderIndex = inputOrderIndex;
  }

  public int getInputOrderIndex() {
    return inputOrderIndex;
  }

  public boolean pointerEvent(final NiftyPointerEvent niftyPointerEvent) {
    return input.pointerEvent(eventBus, this, niftyPointerEvent);
  }

  public boolean capturedPointerEvent(final NiftyPointerEvent niftyPointerEvent) {
    return input.capturedPointerEvent(eventBus, this, niftyPointerEvent);
  }

  public List<InternalNiftyNode> getChildren() {
    return children;
  }

  public int getRenderOrder() {
    return renderOrder;
  }

  public void setRenderOrder(final int renderOrder) {
    this.renderOrder = renderOrder;
  }

  public Mat4 getLocalTransformation() {
    updateTransformation();
    return localTransformation;
  }

  public Mat4 getLocalTransformationInverse() {
    updateTransformation();
    return localTransformationInverse;
  }

  public Mat4 getScreenToLocalTransformation() {
    updateTransformation();
    return screenToLocalTransformation;
  }

  public Mat4 getScreenToLocalTransformationInverse() {
    updateTransformation();
    return screenToLocalTransformationInverse;
  }

  public Vec4 screenToLocal(final int x, final int y) {
    Mat4 inverseTrans = getScreenToLocalTransformationInverse();
    return Mat4.transform(inverseTrans, new Vec4(x, y, 0.f, 1.f));
  }

  public Vec4 localToScreen(final int x, final int y) {
    Mat4 inverseTrans = getScreenToLocalTransformation();
    return Mat4.transform(inverseTrans, new Vec4(x, y, 0.f, 1.f));
  }

  public void setControl(final NiftyControl control) {
    this.control = control;
  }

  public NiftyControl getControl() {
    return control;
  }

  public void setNiftyPrivateNode() {
    niftyPrivateNode = true;
  }

  public boolean isNiftyPrivateNode() {
    return niftyPrivateNode;
  }

  private void getStateInfo(final StringBuilder result, final String offset, final Pattern pattern) {
    if (niftyPrivateNode) {
      return;
    }
    String rootNodeString = "";
    if (parentNode == null) {
      rootNodeString = " {rootNode}";
    }
    result.append(offset).append("- ");
    if (control != null) {
      result.append(control.toString());
    } else {
      result.append("NiftyNode[");
      result.append(toString());
      result.append("]");
    }
    result.append(rootNodeString);
    result.append(" {");
    result.append(stateManager.toString());
    result.append("}");
    result.append("\n");
    result.append(matches(pattern, statePosition(), offset + "  "));
    result.append(matches(pattern, stateConstraints(), offset + "  "));
    result.append(matches(pattern, stateBackgroundColor(), offset + "  "));
    result.append(matches(pattern, stateChildLayout(), offset + "  "));
    result.append(matches(pattern, stateStyleClasses(), offset + "  "));

    for (int i=0; i<children.size(); i++) {
      children.get(i).getStateInfo(result, offset + "  ", pattern);
    }
  }

  private InternalNiftyEventBus eventBus() {
    if (eventBus == null) {
      eventBus = new InternalNiftyEventBus();
    }
    return eventBus;
  }

  private InternalNiftyNode(
      final String id,
      final Nifty nifty,
      final InternalNiftyNode parentNode,
      final ChildLayout rootNodePseudoParentLayout,
      final InternalLayoutable rootNodePseudoLayoutable) {
    this.id = id;
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

    for (int i=0; i<children.size(); i++) {
      InternalNiftyNode node = children.get(i);
      if (node.calculatedMinSize
          ||
          (node.constraints.getWidth() == null &&
           node.constraints.getHeight() == null &&
           node.minSizeCallback != null)) {
        Size size = node.minSizeCallback.calculateMinSize(node.niftyNode);
        if (size != null) {
          node.setWidthConstraint(UnitValue.px(Math.round(size.width)));
          node.setHeightConstraint(UnitValue.px(Math.round(size.height)));
          node.calculatedMinSize = true;
          node.requestRedraw();
        }
      }
    }

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

  private String stateStyleClasses() {
    return "styleClasses [" + styleClasses + "]\n";
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

  private InternalNiftyNode createChildNodeInternal(final String id) {
    return InternalNiftyNode.newNode(id, nifty, this);
  }

  private void assertNotNull(final ChildLayout param) {
    if (param == null) {
      throw new IllegalArgumentException("ChildLayout must not be null. Use ChildLayout.None instead");
    }
  }

  private void updateTransformationChanged(final double oldValue, final double newValue) {
    if (newValue != oldValue) {
      transformationChanged = true;
    }
  }

  private Mat4 buildLocalTransformation() {
    float pivotX = (float) getPivotX() * getWidth();
    float pivotY = (float) getPivotY() * getHeight();
    return Mat4.mul(Mat4.mul(Mat4.mul(Mat4.mul(Mat4.mul(Mat4.mul(
        Mat4.createTranslate(getX(), getY(), 0.f),
        Mat4.createTranslate(pivotX, pivotY, 0.0f)),
        Mat4.createRotate((float) getRotationX(), 1.f, 0.f, 0.f)),
        Mat4.createRotate((float) getRotationY(), 0.f, 1.f, 0.f)),
        Mat4.createRotate((float) getRotationZ(), 0.f, 0.f, 1.f)),
        Mat4.createScale((float) getScaleX(), (float) getScaleY(), (float) getScaleZ())),
        Mat4.createTranslate(-pivotX, -pivotY, 0.0f));
  }

  private void updateTransformation() {
    if (!isTransformationChanged()) {
      return;
    }
    localTransformation = new Mat4(buildLocalTransformation());
    localTransformationInverse = Mat4.invert(localTransformation, new Mat4());

    Mat4 parentTransformation = getParentLocalTransformation();
    screenToLocalTransformation = Mat4.mul(parentTransformation, localTransformation);
    screenToLocalTransformationInverse = Mat4.invert(screenToLocalTransformation, new Mat4());

    transformationChanged = false;
    layoutPos.resetTransformationChanged();
  }

  private Mat4 getParentLocalTransformation() {
    if (parentNode == null) {
      return new Mat4();
    }
    return parentNode.getLocalTransformation();
  }

  public void onHover(final NiftyPointerEvent pointerEvent) {
    setStatesInternal(NiftyNodeState.Hover);
  }

  public void onExit() {
    setStatesInternal();
  }
}
