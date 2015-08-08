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

/**
 * Mouse hover over an element with output of local coordinates.
 * @author void
 */
public class UseCase_i01_PointerEventsScreenToLocal {
  /* FIXME
  private NiftyNode parentNode;
  private NiftyNode childNode;
  private NiftyNode grandChildNode;
  private NiftyNode labelParentNode;
  private Label labelParent;
  private Label labelChild;
  private Label labelGrandChild;
  private NiftyFont font;

  public UseCase_i01_PointerEventsScreenToLocal(final Nifty nifty) throws IOException {
    nifty.showStatistics(NiftyStatisticsMode.ShowFPS);
    nifty.clearScreenBeforeRender();
    font = nifty.createFont("fonts/aurulent-sans-16.fnt");

    // the color rectangles in the middle
    parentNode = nifty.createRootNode(UnitValue.px(512), UnitValue.px(512), ChildLayout.Center);
    parentNode.setBackgroundColor(NiftyColor.red());

    childNode = parentNode.newChildNode(UnitValue.percent(50), UnitValue.percent(50), ChildLayout.Center);
    childNode.setBackgroundColor(NiftyColor.yellow());

    grandChildNode = childNode.newChildNode(UnitValue.percent(50), UnitValue.percent(50));
    grandChildNode.setBackgroundColor(NiftyColor.green());

    // status labels to highlight the elements we hover on
    labelParentNode = nifty.createRootNodeFullscreen(ChildLayout.Vertical);
    labelParentNode.setPaddingTop(UnitValue.px(100));
    labelParentNode.setPaddingLeft(UnitValue.px(50));

    labelParent = addLabel(labelParentNode, NiftyColor.red());
    labelChild = addLabel(labelParentNode, NiftyColor.yellow());
    labelGrandChild = addLabel(labelParentNode, NiftyColor.green());

    parentNode.subscribe(new OnHover(labelParent));
    childNode.subscribe(new OnHover(labelChild));
    grandChildNode.subscribe(new OnHover(labelGrandChild));
  }

  private Label addLabel(final NiftyNode parent, final NiftyColor textColor) {
    Label label = parent.newControl(Label.class);
    label.setFont(font);
    label.setTextColor(textColor);
    return label;
  }

  @Listener(references = References.Strong)
  public static class OnHover {
    private final Label label;

    public OnHover(final Label label) {
      this.label = label;
      pointerOutsideLabel();
    }

    @Handler
    public void onPointerHover(final NiftyPointerHoverEvent event) {
      label.setText(event.getNiftyNode().screenToLocal(event.getX(), event.getY()).toString());
    }

    @Handler
    public void onExitNode(final NiftyPointerExitNodeEvent event) {
      pointerOutsideLabel();
    }

    private void pointerOutsideLabel() {
      label.setText("pointer outside");
    }
}

  public static void main(final String[] args) throws Exception {
    UseCaseRunner.run(UseCase_i01_PointerEventsScreenToLocal.class, args);
  }
  */
}
