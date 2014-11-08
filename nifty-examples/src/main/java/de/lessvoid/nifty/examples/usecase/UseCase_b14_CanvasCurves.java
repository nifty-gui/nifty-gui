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

import java.io.IOException;

import net.engio.mbassy.listener.Handler;
import de.lessvoid.nifty.api.ChildLayout;
import de.lessvoid.nifty.api.Nifty;
import de.lessvoid.nifty.api.NiftyCanvas;
import de.lessvoid.nifty.api.NiftyCanvasPainter;
import de.lessvoid.nifty.api.NiftyColor;
import de.lessvoid.nifty.api.NiftyNode;
import de.lessvoid.nifty.api.NiftyStatisticsMode;
import de.lessvoid.nifty.api.UnitValue;
import de.lessvoid.nifty.api.event.NiftyPointerDraggedEvent;
import de.lessvoid.nifty.api.event.NiftyPointerEnterNodeEvent;
import de.lessvoid.nifty.api.event.NiftyPointerExitNodeEvent;
import de.lessvoid.nifty.api.event.NiftyPointerPressedEvent;

/**
 * An example how to draw bezier curves.
 * @author void
 */
public class UseCase_b14_CanvasCurves {
  ControlPoint cp0;
  ControlPoint cp1;
  ControlPoint cp2;
  ControlPoint cp3;

  private static class ControlPoint {
    private float x;
    private float y;
    private NiftyNode handle;
    private NiftyNode canvasNode;
    private NiftyColor c;
    private int mouseStartX;
    private int mouseStartY;

    public ControlPoint(final float x, final float y, final NiftyNode canvasNode, final NiftyColor color) {
      this.x = x;
      this.y = y;
      this.c = color;
      this.canvasNode = canvasNode;

      handle = canvasNode.newChildNode(UnitValue.px(16), UnitValue.px(16));
      handle.addCanvasPainter(new NiftyCanvasPainter() {
        @Override
        public void paint(final NiftyNode node, final NiftyCanvas canvas) {
          canvas.setFillStyle(NiftyColor.TRANSPARENT());
          canvas.fillRect(0, 0, node.getWidth(), node.getHeight());
          canvas.beginPath();
          canvas.setStrokeColor(c);
          canvas.setLineWidth(1.f);
          canvas.arc(node.getWidth() / 2.f, node.getHeight() / 2.f, node.getWidth() / 2.f, 0, 2*Math.PI);
          canvas.stroke();
        }
      });
      handle.setXConstraint(UnitValue.px((int) x - handle.getWidth() / 2));
      handle.setYConstraint(UnitValue.px((int) y - handle.getHeight() / 2));
      handle.subscribe(this);
    }

    public float getX() {
      return x;
    }

    public float getY() {
      return y;
    }

    @Handler
    private void onMouseEnter(final NiftyPointerEnterNodeEvent event) {
      c = NiftyColor.GREEN();
      handle.requestRedraw();
    }

    @Handler
    private void onMouseLeave(final NiftyPointerExitNodeEvent event) {
      c = NiftyColor.RED();
      handle.requestRedraw();
    }

    @Handler
    private void onPointerPressed(final NiftyPointerPressedEvent event) {
      mouseStartX = event.getX();
      mouseStartY = event.getY();
    }

    @Handler
    private void onDragged(final NiftyPointerDraggedEvent event) {
      int dx = event.getX() - mouseStartX;
      int dy = event.getY() - mouseStartY;

      x = x + dx;
      y = y + dy;

      handle.setXConstraint(UnitValue.px((int) x - handle.getWidth() / 2));
      handle.setYConstraint(UnitValue.px((int) y - handle.getHeight() / 2));
      canvasNode.requestRedraw();

      mouseStartX = event.getX();
      mouseStartY = event.getY();
    }
  }

  public UseCase_b14_CanvasCurves(final Nifty nifty) throws IOException {
    nifty.showStatistics(NiftyStatisticsMode.ShowFPS);

    NiftyNode rootNode = nifty.createRootNodeFullscreen(ChildLayout.Center);
    rootNode.setBackgroundColor(NiftyColor.BLACK());

    NiftyNode canvasNode = rootNode.newChildNode(UnitValue.percent(50), UnitValue.percent(50), ChildLayout.Absolute);

    cp0 = new ControlPoint( 50.f,  50.f, canvasNode, NiftyColor.RED());
    cp1 = new ControlPoint( 50.f, 325.f, canvasNode, NiftyColor.RED());
    cp2 = new ControlPoint(450.f, 325.f, canvasNode, NiftyColor.RED());
    cp3 = new ControlPoint(450.f,  50.f, canvasNode, NiftyColor.RED());

    canvasNode.addCanvasPainter(new NiftyCanvasPainter() {
      @Override
      public void paint(final NiftyNode node, final NiftyCanvas canvas) {
        canvas.setFillStyle(NiftyColor.WHITE());
        canvas.fillRect(0, 0, node.getWidth(), node.getHeight());

        canvas.setStrokeColor(NiftyColor.BLACK());
        canvas.setLineWidth(5);
        canvas.beginPath();
        canvas.moveTo(cp0.getX(), cp0.getY());
        canvas.bezierCurveTo(cp1.getX(), cp1.getY(), cp2.getX(), cp2.getY(), cp3.getX(), cp3.getY());
        canvas.stroke();
      }
    });
  }

  public static void main(final String[] args) throws Exception {
    UseCaseRunner.run(UseCase_b14_CanvasCurves.class, args);
  }
}
