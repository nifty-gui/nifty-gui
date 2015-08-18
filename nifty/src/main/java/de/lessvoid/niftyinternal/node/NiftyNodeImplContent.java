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
package de.lessvoid.niftyinternal.node;

import de.lessvoid.nifty.NiftyState;
import de.lessvoid.nifty.canvas.NiftyCanvas;
import de.lessvoid.nifty.node.NiftyContentNode;
import de.lessvoid.nifty.spi.NiftyNodeImpl;
import de.lessvoid.nifty.spi.NiftyNodeRenderImpl;
import de.lessvoid.nifty.spi.NiftyNodeStateImpl;
import de.lessvoid.nifty.spi.NiftyRenderDevice;
import de.lessvoid.nifty.types.NiftyColor;
import de.lessvoid.nifty.types.NiftyCompositeOperation;
import de.lessvoid.niftyinternal.accessor.NiftyCanvasAccessor;
import de.lessvoid.niftyinternal.canvas.InternalNiftyCanvas;
import de.lessvoid.niftyinternal.math.Mat4;
import de.lessvoid.niftyinternal.render.RenderNode;

import static de.lessvoid.nifty.NiftyState.NiftyStandardState.NiftyStateBackgroundColor;

/**
 * Created by void on 09.08.15.
 */
public class NiftyNodeImplContent implements NiftyNodeStateImpl, NiftyNodeRenderImpl, NiftyNodeImpl<NiftyContentNode> {
  private NiftyRenderDevice niftyRenderDevice;
  private NiftyContentNode niftyNode;
  private NiftyCanvas niftyCanvas;

  @Override
  public void initialize(final NiftyContentNode niftyNode) {
    this.niftyNode = niftyNode;
    this.niftyCanvas = NiftyCanvasAccessor.getDefault().newNiftyCanvas();
  }

  @Override
  public void initialize(final NiftyRenderDevice niftyRenderDevice) {
    this.niftyRenderDevice = niftyRenderDevice;
  }

  @Override
  public NiftyContentNode getNiftyNode() {
    return niftyNode;
  }

  @Override
  public void update(final NiftyState niftyState) {
    NiftyColor color = niftyState.getState(NiftyStateBackgroundColor);

    getCanvas().reset();
    niftyCanvas.setFillStyle(color);
    niftyCanvas.fillRect(0., 0., 1024, 768.);
  }

  @Override
  public RenderNode convert(final RenderNode renderNode) {
    if (renderNode != null) {
      if (getCanvas().isChanged()) {
        renderNode.needsContentUpdate(getCanvas().getCommands());
      }
      // TODO check for resize and forward to RenderNode
      return renderNode;
    }
    return new RenderNode(
        new Mat4(),
        1024,
        768,
        getCanvas().getCommands(),
        niftyRenderDevice.createTexture(1024, 768, NiftyRenderDevice.FilterMode.Linear),
        niftyRenderDevice.createTexture(1024, 768, NiftyRenderDevice.FilterMode.Linear),
        NiftyCompositeOperation.SourceOver,
        0);
  }

  private InternalNiftyCanvas getCanvas() {
    return NiftyCanvasAccessor.getDefault().getInternalNiftyCanvas(niftyCanvas);
  }
}
