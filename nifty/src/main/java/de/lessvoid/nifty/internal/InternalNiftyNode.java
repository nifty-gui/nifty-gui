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
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import de.lessvoid.nifty.api.NiftyStateSetter;
import de.lessvoid.nifty.api.UnitValue;
import de.lessvoid.nifty.api.VAlign;
import de.lessvoid.nifty.api.controls.NiftyControl;
import de.lessvoid.nifty.api.event.NiftyEvent;
import de.lessvoid.nifty.api.input.NiftyPointerEvent;
import de.lessvoid.nifty.internal.accessor.NiftyAccessor;
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
import de.lessvoid.nifty.internal.style.NiftyStyleClassInfo;

public class InternalNiftyNode implements InternalLayoutable {
  private final static Logger log = Logger.getLogger(InternalNiftyNode.class.getName());
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
  private final InternalStateManager stateManager = new InternalStateManager();

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

  // This is the original padding String so that we can return the same String in the getter if necessary. The actual
  // value will be parsed and translated into the individual left, right, top, bottom calls.
  private UnitValue[] padding = new UnitValue[0];

  // This is the original margin String so that we can return the same String in the getter if necessary. The actual
  // value will be parsed and translated into the individual left, right, top, bottom calls.
  private UnitValue[] margin = new UnitValue[0];

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

  public void setXConstraint(final UnitValue value, final NiftyNodeState ... states) {
    stateManager.setValue(this, value, stateSetterXConstraint, states);
  }

  public void setYConstraint(final UnitValue value, final NiftyNodeState ... states) {
    stateManager.setValue(this, value, stateSetterYConstraint, states);
  }

  public void setWidthConstraint(final UnitValue value, final NiftyNodeState ... states) {
    stateManager.setValue(this, value, stateSetterWidth, states);
  }

  public void setHeightConstraint(final UnitValue value, final NiftyNodeState ... states) {
    stateManager.setValue(this, value, stateSetterHeight, states);
  }

  public void setChildLayout(final ChildLayout childLayout, final NiftyNodeState ... states) {
    assertNotNull(childLayout);
    stateManager.setValue(this, childLayout, stateSetterChildLayout, states);
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
      result.init(childNode, stateManager);
      initDefaultProperties(result);
      return result;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public void setHAlign(final HAlign alignment, final NiftyNodeState ... states) {
    stateManager.setValue(this, alignment, stateSetterHAlign, states);
  }

  public void setVAlign(final VAlign alignment, final NiftyNodeState ... states) {
    stateManager.setValue(this, alignment, stateSetterVAlign, states);
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
    stateManager.setValue(this, color, stateSetterBackgroundColor, states);
  }

  public void setBackgroundGradient(final NiftyLinearGradient gradient, final NiftyNodeState ... states) {
    stateManager.setValue(this, gradient, stateSetterLinearGradient, states);
  }

  public void setCompositeOperation(final NiftyCompositeOperation compositeOperation, final NiftyNodeState ... states) {
    stateManager.setValue(this, compositeOperation, stateSetterCompositeOperation, states);
  }

  public NiftyCompositeOperation getCompositeOperation() {
    return compositeOperation;
  }

  public double getScaleX() {
    return scaleX;
  }

  public void setScaleX(final double factor, final NiftyNodeState ... states) {
    stateManager.setValue(this, factor, stateSetterScaleX, states);
  }

  public double getScaleY() {
    return scaleY;
  }

  public void setScaleY(final double factor, final NiftyNodeState ... states) {
    stateManager.setValue(this, factor, stateSetterScaleY, states);
  }

  public double getScaleZ() {
    return scaleZ;
  }

  public void setScaleZ(final double factor, final NiftyNodeState ... states) {
    stateManager.setValue(this, factor, stateSetterScaleZ, states);
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

  public void setRotationX(final double angle, final NiftyNodeState ... states) {
    stateManager.setValue(this, angle, stateSetterRotationX, states);
  }

  public double getRotationY() {
    return angleY;
  }

  public void setRotationY(final double angle, final NiftyNodeState ... states) {
    stateManager.setValue(this, angle, stateSetterRotationY, states);
  }

  public double getRotationZ() {
    return angleZ;
  }

  public void setRotationZ(final double angle, final NiftyNodeState ... states) {
    stateManager.setValue(this, angle, stateSetterRotationZ, states);
  }

  public void setCanvasPainter(final NiftyCanvasPainter painter, final NiftyNodeState ... states) {
    stateManager.setValue(this, painter, stateSetterCanvasPainter, states);
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

  public void publish(final NiftyEvent message) {
    eventBus().publish(message);
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

  public UnitValue[] getPadding() {
    return padding;
  }

  public void setPadding(final UnitValue[] padding, final NiftyNodeState ... states) {
    stateManager.setValue(this, padding, stateSetterConstraintPadding, states);
  }

  public UnitValue getPaddingTop() {
    return constraints.getPaddingTop();
  }

  public void setPaddingTop(final UnitValue paddingTop, final NiftyNodeState ... states) {
    stateManager.setValue(this, paddingTop, stateSetterConstraintPaddingTop, states);
  }

  public UnitValue getPaddingRight() {
    return constraints.getPaddingRight();
  }

  public void setPaddingRight(final UnitValue paddingRight, final NiftyNodeState ... states) {
    stateManager.setValue(this, paddingRight, stateSetterConstraintPaddingRight, states);
  }

  public UnitValue getPaddingBottom() {
    return constraints.getPaddingBottom();
  }

  public void setPaddingBottom(final UnitValue paddingBottom, final NiftyNodeState ... states) {
    stateManager.setValue(this, paddingBottom, stateSetterConstraintPaddingBottom, states);
  }

  public UnitValue getPaddingLeft() {
    return constraints.getPaddingLeft();
  }

  public void setPaddingLeft(final UnitValue paddingLeft, final NiftyNodeState ... states) {
    stateManager.setValue(this, paddingLeft, stateSetterConstraintPaddingLeft, states);
  }

  public UnitValue[] getMargin() {
    return margin;
  }

  public void setMargin(final UnitValue[] margin, final NiftyNodeState ... states) {
    stateManager.setValue(this, margin, stateSetterConstraintMargin, states);
  }

  public UnitValue getMarginTop() {
    return constraints.getMarginTop();
  }

  public void setMarginTop(final UnitValue marginTop, final NiftyNodeState ... states) {
    stateManager.setValue(this, marginTop, stateSetterConstraintMarginTop, states);
  }

  public UnitValue getMarginRight() {
    return constraints.getMarginRight();
  }

  public void setMarginRight(final UnitValue marginRight, final NiftyNodeState ... states) {
    stateManager.setValue(this, marginRight, stateSetterConstraintMarginRight, states);
  }

  public UnitValue getMarginBottom() {
    return constraints.getMarginBottom();
  }

  public void setMarginBottom(final UnitValue marginBottom, final NiftyNodeState ... states) {
    stateManager.setValue(this, marginBottom, stateSetterConstraintMarginBottom, states);
  }

  public UnitValue getMarginLeft() {
    return constraints.getMarginLeft();
  }

  public void setMarginLeft(final UnitValue marginLeft, final NiftyNodeState ... states) {
    stateManager.setValue(this, marginLeft, stateSetterConstraintMarginLeft, states);
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

  public void setRenderOrder(final int renderOrder, final NiftyNodeState ... states) {
    stateManager.setValue(this, renderOrder, stateSetterRenderOrder, states);
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

  void onHover(final NiftyPointerEvent pointerEvent) {
    setStatesInternal(NiftyNodeState.Hover);
  }

  void onExit() {
    setStatesInternal();
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

  public void initDefaultProperties(final Object instance) {
    try {
      NiftyStyleClassInfo styleClassInfo = NiftyAccessor.getDefault().getNiftyStyleClassInfo(nifty, instance.getClass());
      Map<String, String> properties = styleClassInfo.getProperties(instance);
      for (Map.Entry<String, String> entry : properties.entrySet()) {
        try {
          styleClassInfo.writeValue(instance, entry.getKey(), entry.getValue(), Arrays.asList(NiftyNodeState.Regular));
        } catch (Exception e) {
          log.log(Level.WARNING, "writeValue exception", e);
        }
      }
    } catch (Exception e) {
      log.log(Level.WARNING, "unable to init default properties for " + instance.getClass() + ", instance: " + instance, e);
    }
  }

  // StateSetters for all properties that take part in the StateManager business. These are declared static so that we
  // can reuse them in all instances and don't generate additional GC stress.
  private static NiftyStateSetter<InternalNiftyNode, NiftyColor> stateSetterBackgroundColor = new NiftyStateSetter<InternalNiftyNode, NiftyColor>() {
    @Override
    public void set(final InternalNiftyNode target, final NiftyColor value, final NiftyNodeState state) {
      target.backgroundColor = value;
      target.requestRedraw = true;
    }
  };
  private static NiftyStateSetter<InternalNiftyNode, NiftyLinearGradient> stateSetterLinearGradient = new NiftyStateSetter<InternalNiftyNode, NiftyLinearGradient>() {
    @Override
    public void set(final InternalNiftyNode target, final NiftyLinearGradient value, final NiftyNodeState state) {
      target.backgroundGradient = value;
      target.requestRedraw = true;
    }
  };
  private static NiftyStateSetter<InternalNiftyNode, UnitValue> stateSetterXConstraint = new NiftyStateSetter<InternalNiftyNode, UnitValue>() {
    @Override
    public void set(final InternalNiftyNode target, final UnitValue value, final NiftyNodeState state) {
      target.constraints.setX(value);
      target.needsLayout = true;
    }
  };
  private static NiftyStateSetter<InternalNiftyNode, UnitValue> stateSetterYConstraint = new NiftyStateSetter<InternalNiftyNode, UnitValue>() {
    @Override
    public void set(final InternalNiftyNode target, final UnitValue value, final NiftyNodeState state) {
      target.constraints.setY(value);
      target.needsLayout = true;
    }
  };
  private static NiftyStateSetter<InternalNiftyNode, UnitValue> stateSetterWidth = new NiftyStateSetter<InternalNiftyNode, UnitValue>() {
    @Override
    public void set(final InternalNiftyNode target, final UnitValue value, final NiftyNodeState state) {
      target.constraints.setWidth(value);
      target.needsLayout = true;
    }
  };
  private static NiftyStateSetter<InternalNiftyNode, UnitValue> stateSetterHeight = new NiftyStateSetter<InternalNiftyNode, UnitValue>() {
    @Override
    public void set(final InternalNiftyNode target, final UnitValue value, final NiftyNodeState state) {
      target.constraints.setHeight(value);
      target.needsLayout = true;
    }
  };
  private static NiftyStateSetter<InternalNiftyNode, ChildLayout> stateSetterChildLayout = new NiftyStateSetter<InternalNiftyNode, ChildLayout>() {
    @Override
    public void set(final InternalNiftyNode target, final ChildLayout childLayout, final NiftyNodeState state) {
      target.childLayout = childLayout;
      target.needsLayout = true;
    }
  };
  private static NiftyStateSetter<InternalNiftyNode, HAlign> stateSetterHAlign = new NiftyStateSetter<InternalNiftyNode, HAlign>() {
    @Override
    public void set(final InternalNiftyNode target, final HAlign alignment, final NiftyNodeState state) {
      target.constraints.setHorizontalAlign(alignment);
      target.needsLayout = true;
    }
  };
  private static NiftyStateSetter<InternalNiftyNode, VAlign> stateSetterVAlign = new NiftyStateSetter<InternalNiftyNode, VAlign>() {
    @Override
    public void set(final InternalNiftyNode target, final VAlign alignment, final NiftyNodeState state) {
      target.constraints.setVerticalAlign(alignment);
      target.needsLayout = true;
    }
  };
  private static NiftyStateSetter<InternalNiftyNode, NiftyCompositeOperation> stateSetterCompositeOperation = new NiftyStateSetter<InternalNiftyNode, NiftyCompositeOperation>() {
    @Override
    public void set(final InternalNiftyNode target, final NiftyCompositeOperation compositeOperation, final NiftyNodeState state) {
      target.compositeOperation = compositeOperation;
    }
  };
  private static NiftyStateSetter<InternalNiftyNode, Double> stateSetterScaleX = new NiftyStateSetter<InternalNiftyNode, Double>() {
    @Override
    public void set(final InternalNiftyNode target, final Double factor, final NiftyNodeState state) {
      target.updateTransformationChanged(target.scaleX, factor);
      target.scaleX = factor;
    }
  };
  private static NiftyStateSetter<InternalNiftyNode, Double> stateSetterScaleY = new NiftyStateSetter<InternalNiftyNode, Double>() {
    @Override
    public void set(final InternalNiftyNode target, final Double factor, final NiftyNodeState state) {
      target.updateTransformationChanged(target.scaleY, factor);
      target.scaleY = factor;
    }
  };
  private static NiftyStateSetter<InternalNiftyNode, Double> stateSetterScaleZ = new NiftyStateSetter<InternalNiftyNode, Double>() {
    @Override
    public void set(final InternalNiftyNode target, final Double factor, final NiftyNodeState state) {
      target.updateTransformationChanged(target.scaleZ, factor);
      target.scaleZ = factor;
    }
  };
  private static NiftyStateSetter<InternalNiftyNode, Double> stateSetterRotationX = new NiftyStateSetter<InternalNiftyNode, Double>() {
    @Override
    public void set(final InternalNiftyNode target, final Double angle, final NiftyNodeState state) {
      target.updateTransformationChanged(target.angleX, angle);
      target.angleX = angle;
    }
  };
  private static NiftyStateSetter<InternalNiftyNode, Double> stateSetterRotationY = new NiftyStateSetter<InternalNiftyNode, Double>() {
    @Override
    public void set(final InternalNiftyNode target, final Double angle, final NiftyNodeState state) {
      target.updateTransformationChanged(target.angleY, angle);
      target.angleY = angle;
    }
  };
  private static NiftyStateSetter<InternalNiftyNode, Double> stateSetterRotationZ = new NiftyStateSetter<InternalNiftyNode, Double>() {
    @Override
    public void set(final InternalNiftyNode target, final Double angle, final NiftyNodeState state) {
      target.updateTransformationChanged(target.angleZ, angle);
      target.angleZ = angle;
    }
  };
  private NiftyStateSetter<InternalNiftyNode, Integer> stateSetterRenderOrder = new NiftyStateSetter<InternalNiftyNode, Integer>() {
    @Override
    public void set(final InternalNiftyNode target, final Integer renderOrder, final NiftyNodeState state) {
      target.renderOrder = renderOrder;
    }
  };
  private NiftyStateSetter<InternalNiftyNode, NiftyCanvasPainter> stateSetterCanvasPainter = new NiftyStateSetter<InternalNiftyNode, NiftyCanvasPainter>() {
    @Override
    public void set(final InternalNiftyNode target, final NiftyCanvasPainter canvasPainter, final NiftyNodeState state) {
      target.canvasPainters.clear();
      target.canvasPainters.add(canvasPainter);
    }
  };
  private static NiftyStateSetter<InternalNiftyNode, UnitValue[]> stateSetterConstraintPadding = new NiftyStateSetter<InternalNiftyNode, UnitValue[]>() {
    @Override
    public void set(final InternalNiftyNode target, final UnitValue[] value, final NiftyNodeState state) {
      if (value == null) {
        target.padding = new UnitValue[0];
        target.constraints.setPadding(UnitValue.px(0));
        target.needsLayout = true;
        return;
      }
      int len = value.length;
      if (len == 0) {
        target.padding = new UnitValue[0];
        target.constraints.setPadding(UnitValue.px(0));
        target.needsLayout = true;
        return;
      }
      if (len == 1) {
        target.padding = value;
        target.constraints.setPadding(value[0]);
        target.needsLayout = true;
        return;
      }
      if (len == 2) {
        target.padding = value;
        target.constraints.setPadding(value[0], value[1]);
        target.needsLayout = true;
        return;
      }
      if (len == 3) {
        target.padding = value;
        target.constraints.setPadding(value[0], value[1], value[2]);
        target.needsLayout = true;
        return;
      }
      if (len == 4) {
        target.padding = value;
        target.constraints.setPadding(value[0], value[1], value[2], value[3]);
        target.needsLayout = true;
        return;
      }
    }
  };
  private static NiftyStateSetter<InternalNiftyNode, UnitValue> stateSetterConstraintPaddingTop = new NiftyStateSetter<InternalNiftyNode, UnitValue>() {
    @Override
    public void set(final InternalNiftyNode target, final UnitValue value, final NiftyNodeState state) {
      target.constraints.setPaddingTop(value);
      target.needsLayout = true;
    }
  };
  private static NiftyStateSetter<InternalNiftyNode, UnitValue> stateSetterConstraintPaddingRight = new NiftyStateSetter<InternalNiftyNode, UnitValue>() {
    @Override
    public void set(final InternalNiftyNode target, final UnitValue value, final NiftyNodeState state) {
      target.constraints.setPaddingRight(value);
      target.needsLayout = true;
    }
  };
  private static NiftyStateSetter<InternalNiftyNode, UnitValue> stateSetterConstraintPaddingBottom = new NiftyStateSetter<InternalNiftyNode, UnitValue>() {
    @Override
    public void set(final InternalNiftyNode target, final UnitValue value, final NiftyNodeState state) {
      target.constraints.setPaddingBottom(value);
      target.needsLayout = true;
    }
  };
  private static NiftyStateSetter<InternalNiftyNode, UnitValue> stateSetterConstraintPaddingLeft = new NiftyStateSetter<InternalNiftyNode, UnitValue>() {
    @Override
    public void set(final InternalNiftyNode target, final UnitValue value, final NiftyNodeState state) {
      target.constraints.setPaddingLeft(value);
      target.needsLayout = true;
    }
  };
  private static NiftyStateSetter<InternalNiftyNode, UnitValue[]> stateSetterConstraintMargin = new NiftyStateSetter<InternalNiftyNode, UnitValue[]>() {
    @Override
    public void set(final InternalNiftyNode target, final UnitValue[] value, final NiftyNodeState state) {
      if (value == null) {
        target.margin = new UnitValue[0];
        target.constraints.setMargin(UnitValue.px(0));
        target.needsLayout = true;
        return;
      }
      int len = value.length;
      if (len == 0) {
        target.margin = new UnitValue[0];
        target.constraints.setMargin(UnitValue.px(0));
        target.needsLayout = true;
        return;
      }
      if (len == 1) {
        target.margin = value;
        target.constraints.setMargin(value[0]);
        target.needsLayout = true;
        return;
      }
      if (len == 2) {
        target.margin = value;
        target.constraints.setMargin(value[0], value[1]);
        target.needsLayout = true;
        return;
      }
      if (len == 3) {
        target.margin = value;
        target.constraints.setMargin(value[0], value[1], value[2]);
        target.needsLayout = true;
        return;
      }
      if (len == 4) {
        target.margin = value;
        target.constraints.setMargin(value[0], value[1], value[2], value[3]);
        target.needsLayout = true;
        return;
      }
    }
  };
  private static NiftyStateSetter<InternalNiftyNode, UnitValue> stateSetterConstraintMarginTop = new NiftyStateSetter<InternalNiftyNode, UnitValue>() {
    @Override
    public void set(final InternalNiftyNode target, final UnitValue value, final NiftyNodeState state) {
      target.constraints.setMarginTop(value);
      target.needsLayout = true;
    }
  };
  private static NiftyStateSetter<InternalNiftyNode, UnitValue> stateSetterConstraintMarginRight = new NiftyStateSetter<InternalNiftyNode, UnitValue>() {
    @Override
    public void set(final InternalNiftyNode target, final UnitValue value, final NiftyNodeState state) {
      target.constraints.setMarginRight(value);
      target.needsLayout = true;
    }
  };
  private static NiftyStateSetter<InternalNiftyNode, UnitValue> stateSetterConstraintMarginBottom = new NiftyStateSetter<InternalNiftyNode, UnitValue>() {
    @Override
    public void set(final InternalNiftyNode target, final UnitValue value, final NiftyNodeState state) {
      target.constraints.setMarginBottom(value);
      target.needsLayout = true;
    }
  };
  private static NiftyStateSetter<InternalNiftyNode, UnitValue> stateSetterConstraintMarginLeft = new NiftyStateSetter<InternalNiftyNode, UnitValue>() {
    @Override
    public void set(final InternalNiftyNode target, final UnitValue value, final NiftyNodeState state) {
      target.constraints.setMarginLeft(value);
      target.needsLayout = true;
    }
  };
}
