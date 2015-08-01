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
package de.lessvoid.nifty.internal.common;

import java.io.IOException;

import de.lessvoid.nifty.api.ChildLayout;
import de.lessvoid.nifty.api.Nifty;
import de.lessvoid.nifty.api.NiftyCallback;
import de.lessvoid.nifty.api.NiftyColor;
import de.lessvoid.nifty.api.NiftyRuntimeException;
import de.lessvoid.nifty.api.UnitValue;
import de.lessvoid.nifty.api.node.NiftyNode;

public class StatisticsRendererFPS {

  public StatisticsRendererFPS(final Nifty nifty) {
    /* FIXME
    NiftyNode fpsNode = nifty.createRootNode(ChildLayout.Vertical, UnitValue.percent(100), UnitValue.wildcard(), ChildLayout.Horizontal);
    NiftyNodeAccessor.getDefault().getInternalNiftyNode(fpsNode).setNiftyPrivateNode();
    fpsNode.setRenderOrder(1000);

    try {
      final Label label = fpsNode.newControl(Label.class);
      label.setFont(nifty.createFont("fonts/aurulent-sans-16.fnt"));
      label.getNode().setBackgroundColor(NiftyColor.black());
      label.getNode().startAnimated(0, 1000, new NiftyCallback<Float>() {
        @Override
        public void execute(final Float t) {
          label.setText(nifty.getStatistics().getFpsText());
        }
      });
    } catch (IOException e) {
      throw new NiftyRuntimeException(e);
    }
    */
  }
}
