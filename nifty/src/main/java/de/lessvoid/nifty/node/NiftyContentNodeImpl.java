/*
 * Copyright (c) 2015, Nifty GUI Community
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
package de.lessvoid.nifty.node;

import de.lessvoid.nifty.NiftyCanvas;
import de.lessvoid.nifty.NiftyCanvasPainter;
import de.lessvoid.nifty.NiftyState;
import de.lessvoid.nifty.spi.node.NiftyLayoutReceiver;
import de.lessvoid.nifty.spi.node.NiftyNodeContentImpl;
import de.lessvoid.nifty.spi.node.NiftyNodeStateImpl;
import de.lessvoid.nifty.types.NiftyColor;
import de.lessvoid.nifty.types.NiftyPoint;
import de.lessvoid.nifty.types.NiftyRect;
import de.lessvoid.nifty.types.NiftySize;
import de.lessvoid.niftyinternal.math.Mat4;

import javax.annotation.Nonnull;

import static de.lessvoid.nifty.NiftyState.NiftyStandardState.NiftyStateBackgroundColor;
import static de.lessvoid.nifty.NiftyState.NiftyStandardState.NiftyStateTransformation;
import static de.lessvoid.nifty.NiftyState.NiftyStandardState.NiftyStateTransformationChanged;
import static de.lessvoid.nifty.NiftyState.NiftyStandardState.NiftyStateTransformationLayoutRect;

/**
 * Created by void on 09.08.15.
 */
class NiftyContentNodeImpl
    implements
      NiftyNodeStateImpl<NiftyContentNode>,
      NiftyNodeContentImpl<NiftyContentNode>,
      NiftyLayoutReceiver<NiftyContentNode> {
  private final StringBuilder builder = new StringBuilder();

  private final Mat4 identity = Mat4.createIdentity();
  private final Mat4 localToScreen = Mat4.createIdentity();
  private final Mat4 contentLayoutPosTransformation = Mat4.createIdentity();

  private NiftyColor backgroundColor;
  private NiftyCanvasPainter canvasPainter = defaultNiftyCanvasPainter();
  private NiftyRect layoutRect = NiftyRect.newNiftyRect(NiftyPoint.ZERO, NiftySize.ZERO);
  private boolean layoutRectChanged = true;

  public void setCanvasPainter(final NiftyCanvasPainter canvasPainter) {
    this.canvasPainter = canvasPainter;
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // NiftyNodeStateImpl
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  @Override
  public void update(final NiftyState niftyState) {
    backgroundColor = niftyState.getState(NiftyStateBackgroundColor, NiftyColor.purple());

    NiftyRect parentLayoutRect = niftyState.getState(NiftyStateTransformationLayoutRect);
    float parentRelativeLayoutPosX = layoutRect.getOrigin().getX();
    float parentRelativeLayoutPosY = layoutRect.getOrigin().getY();
    if (parentLayoutRect != null) {
      parentRelativeLayoutPosX -= parentLayoutRect.getOrigin().getX();
      parentRelativeLayoutPosY -= parentLayoutRect.getOrigin().getY();
    }

    Mat4 parentLocalToScreen = niftyState.getState(NiftyStateTransformation, identity);
    boolean parentLocalToScreenChanged = niftyState.getState(NiftyStateTransformationChanged, false);
    boolean thisStateChanged = layoutRectChanged || parentLocalToScreenChanged;

    if (thisStateChanged) {
      contentLayoutPosTransformation.setTranslate(parentRelativeLayoutPosX, parentRelativeLayoutPosY, 0.f);
      Mat4.mul(parentLocalToScreen, contentLayoutPosTransformation, localToScreen);
    }

    niftyState.setState(NiftyStateTransformationLayoutRect, layoutRect);
    niftyState.setState(NiftyStateTransformation, localToScreen);
    niftyState.setState(NiftyStateTransformationChanged, thisStateChanged);
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // NiftyNodeContentImpl
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  @Override
  public void updateCanvas(final NiftyCanvas niftyCanvas) {
    canvasPainter.paint(getNiftyNode(), niftyCanvas);
  }

  @Override
  public int getContentWidth() {
    return ((Float) layoutRect.getSize().getWidth()).intValue();
  }

  @Override
  public int getContentHeight() {
    return ((Float) layoutRect.getSize().getHeight()).intValue();
  }

  @Override
  public Mat4 getLocalToScreen() {
    return localToScreen;
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // NiftyNodeImpl
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  @Override
  public NiftyContentNode getNiftyNode() {
    return new NiftyContentNode(this);
  }

  private NiftyCanvasPainter defaultNiftyCanvasPainter() {
    return new NiftyCanvasPainter() {
      @Override
      public void paint(final NiftyContentNode node, final NiftyCanvas niftyCanvas) {
        niftyCanvas.setFillStyle(backgroundColor);
        niftyCanvas.fillRect(0., 0., getContentWidth(), getContentHeight());
      }
    };
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // NiftyLayoutReceiver
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  @Override
  public void setLayoutResult(@Nonnull NiftyRect rect) {
    layoutRectChanged = !layoutRect.equals(rect);
    layoutRect = rect;
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // Other
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  public String toString() {
    builder.setLength(0);
    builder.append("{");
    builder.append("layoutPosX [").append(layoutRect.getOrigin().getX()).append("] ");
    builder.append("layoutPosY [").append(layoutRect.getOrigin().getY()).append("] ");
    builder.append("layoutWidth [").append(layoutRect.getSize().getWidth()).append("] ");
    builder.append("layoutHeight [").append(layoutRect.getSize().getHeight()).append("] ");
    builder.append("backgroundColor [").append(backgroundColor).append("]");
    builder.append("}");
    return builder.toString();
  }

}
