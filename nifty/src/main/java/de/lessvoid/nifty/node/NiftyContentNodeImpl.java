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
import de.lessvoid.nifty.types.NiftyRect;

import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.List;

import static de.lessvoid.nifty.NiftyState.NiftyStandardState.NiftyStateBackgroundColor;
import static de.lessvoid.nifty.NiftyState.NiftyStandardState.NiftyStateTransformation;

/**
 * Created by void on 09.08.15.
 */
class NiftyContentNodeImpl
    implements
      NiftyNodeStateImpl<NiftyContentNode>,
      NiftyNodeContentImpl<NiftyContentNode>,
      NiftyLayoutReceiver<NiftyContentNode> {
  private int layoutWidth;
  private int layoutHeight;

  private NiftyColor backgroundColor;
  private NiftyCanvasPainter canvasPainter = defaultNiftyCanvasPainter();
  private List<TransformationParameters> defaultTransformations = new ArrayList<>();
  private List<TransformationParameters> transformations = new ArrayList<>();
  private float layoutPosX;
  private float layoutPosY;

  public void setCanvasPainter(final NiftyCanvasPainter canvasPainter) {
    this.canvasPainter = canvasPainter;
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // NiftyNodeStateImpl
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  @Override
  public void update(final NiftyState niftyState) {
    backgroundColor = niftyState.getState(NiftyStateBackgroundColor, NiftyColor.purple());

    // get a copy of all the transformations we've collected so far
    transformations.clear();
    transformations.addAll(niftyState.getState(NiftyStateTransformation, defaultTransformations));

    // and then clear the transformations
    niftyState.setState(NiftyStateTransformation, new ArrayList<>());
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
    return layoutWidth;
  }

  @Override
  public int getContentHeight() {
    return layoutHeight;
  }

  @Override
  public List<TransformationParameters> getTransformations() {
    return transformations;
  }

  @Override
  public float getLayoutPosX() {
    return layoutPosX;
  }

  @Override
  public float getLayoutPosY() {
    return layoutPosY;
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
    layoutPosX = rect.getOrigin().getX();
    layoutPosY = rect.getOrigin().getY();
    layoutWidth = Math.round(rect.getSize().getWidth());
    layoutHeight = Math.round(rect.getSize().getHeight());
  }
}
