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
package de.lessvoid.nifty.api;

import de.lessvoid.nifty.api.node.NiftyNode;

/**
 * The DefaultNiftyCanvasPainter will be used when you don't set a specific one for a NiftyNode. It is part of the
 * public API so that you can use it as well (f.i. when you want the default behavior in your own NiftyCanvasPainter.
 * 
 * @author void
 */
public class NiftyCanvasPainterDefault implements NiftyCanvasPainter {

  @Override
  public void paint(final NiftyNode node, final NiftyCanvas canvas) {
    /* FIXME
    canvas.setFillStyle(node.getBackgroundColor());
    canvas.fillRect(0, 0, node.getWidth(), node.getHeight());

    if (node.getBackgroundGradient() != null) {
      canvas.setFillStyle(node.getBackgroundGradient());
      canvas.fillRect(0, 0, node.getWidth(), node.getHeight());
    }
    */
  }
}
