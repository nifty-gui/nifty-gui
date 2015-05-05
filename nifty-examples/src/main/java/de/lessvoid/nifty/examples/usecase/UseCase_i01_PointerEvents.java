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
package de.lessvoid.nifty.examples.usecase;

import net.engio.mbassy.listener.Handler;
import de.lessvoid.nifty.api.ChildLayout;
import de.lessvoid.nifty.api.Nifty;
import de.lessvoid.nifty.api.NiftyCallback;
import de.lessvoid.nifty.api.NiftyColor;
import de.lessvoid.nifty.api.NiftyNode;
import de.lessvoid.nifty.api.NiftyStatisticsMode;
import de.lessvoid.nifty.api.UnitValue;
import de.lessvoid.nifty.api.event.NiftyPointerClickedEvent;
import de.lessvoid.nifty.api.event.NiftyPointerDraggedEvent;
import de.lessvoid.nifty.api.event.NiftyPointerEnterNodeEvent;
import de.lessvoid.nifty.api.event.NiftyPointerExitNodeEvent;
import de.lessvoid.nifty.api.event.NiftyPointerHoverEvent;
import de.lessvoid.nifty.api.event.NiftyPointerPressedEvent;

/**
 * Mouse hover over an element.
 * @author void
 */
public class UseCase_i01_PointerEvents {
  private NiftyNode niftyNode;
  private NiftyNode childNode;
  private int mouseStartX;
  private int mouseStartY;

  public UseCase_i01_PointerEvents(final Nifty nifty) {
    nifty.showStatistics(NiftyStatisticsMode.ShowFPS);
    nifty.clearScreenBeforeRender();

    niftyNode = nifty.createRootNode(UnitValue.px(256), UnitValue.px(256), ChildLayout.Absolute);
    niftyNode.setBackgroundColor(NiftyColor.blue());

    childNode = niftyNode.newChildNode(UnitValue.px(100), UnitValue.px(100));
    childNode.setXConstraint(UnitValue.px(100));
    childNode.setYConstraint(UnitValue.px(100));
    childNode.setBackgroundColor(NiftyColor.red());
    childNode.startAnimated(0, 16, new NiftyCallback<Float>() {
      @Override
      public void execute(final Float t) {
        childNode.setRotationZ(t * 100.f);
      }
    });
    childNode.subscribe(this);
  }

  @Handler
  private void onPointerEnter(final NiftyPointerEnterNodeEvent event) {
    childNode.setBackgroundColor(NiftyColor.green());
  }

  @Handler
  private void onPointerHover(final NiftyPointerHoverEvent event) {
  }

  @Handler
  private void onPointerLeave(final NiftyPointerExitNodeEvent event) {
    childNode.setBackgroundColor(NiftyColor.red());
  }

  @Handler
  private void onPointerPressed(final NiftyPointerPressedEvent event) {
    childNode.setBackgroundColor(NiftyColor.yellow());
    mouseStartX = event.getX();
    mouseStartY = event.getY();
  }

  @Handler
  private void onPointerLeave(final NiftyPointerClickedEvent event) {
    childNode.setBackgroundColor(NiftyColor.white());
  }

  @Handler
  private void onPointerDragged(final NiftyPointerDraggedEvent event) {
    childNode.setBackgroundColor(NiftyColor.black());
    
    int dx = event.getX() - mouseStartX;
    int dy = event.getY() - mouseStartY;


    childNode.setXConstraint(UnitValue.px(childNode.getX() + dx));
    childNode.setYConstraint(UnitValue.px(childNode.getY() + dy));

    mouseStartX = event.getX();
    mouseStartY = event.getY();
  }

  public static void main(final String[] args) throws Exception {
    UseCaseRunner.run(UseCase_i01_PointerEvents.class, args);
  }
}
