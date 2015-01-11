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
package de.lessvoid.nifty.examples.usecase;

import de.lessvoid.nifty.api.ChildLayout;
import de.lessvoid.nifty.api.Nifty;
import de.lessvoid.nifty.api.NiftyColor;
import de.lessvoid.nifty.api.NiftyNode;
import de.lessvoid.nifty.api.NiftyNodeState;
import de.lessvoid.nifty.api.NiftyStatisticsMode;
import de.lessvoid.nifty.api.UnitValue;

/**
 * State changes.
 * @author void
 */
public class UseCase_i02_StateChange {

  public UseCase_i02_StateChange(final Nifty nifty) {
    nifty.showStatistics(NiftyStatisticsMode.ShowFPS);
    nifty.clearScreenBeforeRender();

    NiftyNode rootNode = nifty.createRootNode(UnitValue.px(256), UnitValue.px(256), ChildLayout.Horizontal);

    // the regular node
    addNewDemoNode(rootNode);

    // we add the same node again but FORCE it's state to Hover
    NiftyNode hover = addNewDemoNode(rootNode);
    hover.forceStates(NiftyNodeState.Hover);
  }

  private NiftyNode addNewDemoNode(final NiftyNode parent) {
    NiftyNode node = parent.newChildNode(UnitValue.percent(50), UnitValue.percent(50), ChildLayout.Center);
    node.setBackgroundColor(NiftyColor.blue());
    node.setBackgroundColor(NiftyColor.white(), NiftyNodeState.Hover);
    return node;
  }

  public static void main(final String[] args) throws Exception {
    UseCaseRunner.run(UseCase_i02_StateChange.class, args);
  }
}
