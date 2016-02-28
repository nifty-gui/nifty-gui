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

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyNodeCallback;
import de.lessvoid.nifty.control.ButtonNode;
import de.lessvoid.nifty.node.NiftyTransformationNode;
import de.lessvoid.nifty.types.NiftyColor;

import java.io.IOException;

import static de.lessvoid.nifty.control.ButtonNode.button;
import static de.lessvoid.nifty.node.AlignmentLayoutChildNode.alignmentLayoutChildNode;
import static de.lessvoid.nifty.node.AlignmentLayoutNode.alignmentLayoutNode;
import static de.lessvoid.nifty.node.Horizontal.Center;
import static de.lessvoid.nifty.node.NiftyBackgroundFillNode.backgroundFillColor;
import static de.lessvoid.nifty.node.NiftyContentNode.contentNode;
import static de.lessvoid.nifty.node.NiftyTransformationNode.transformationNode;
import static de.lessvoid.nifty.node.Vertical.Middle;

/**
 * Display some simple Button - with style ;)
 * @author void
 */
public class UseCase_c10_ButtonBasic {
  public UseCase_c10_ButtonBasic(final Nifty nifty) throws IOException {
    NiftyTransformationNode rootTransformation = transformationNode();
    ButtonNode button = button(nifty.createFont("fonts/aurulent-sans-16.fnt"), "Hello Nifty GUI");

    nifty.startAnimated(0, 15, rootTransformation, new NiftyNodeCallback<Float, NiftyTransformationNode>() {
      private float angle = 0;
      @Override
      public void execute(final Float time, final NiftyTransformationNode niftyTransformationNode) {
        niftyTransformationNode.setAngleZ(angle++);
      }
    });

    nifty
      .addNode(backgroundFillColor(NiftyColor.fromString("#ccc")))
        .addNode(contentNode())
          .addNode(alignmentLayoutNode())
            .addNode(alignmentLayoutChildNode(Center, Middle))
              .addNode(rootTransformation)
                .addNode(button);
  }

  public static void main(final String[] args) throws Exception {
    UseCaseRunner.run(UseCase_c10_ButtonBasic.class, args);
  }

  /* FIXME: No controls implementation
  private final NiftyNode niftyNode;
  private final Label statusLabel;

  public UseCase_c10_ButtonBasic(final Nifty nifty) throws Exception {
    nifty.showStatistics(NiftyStatisticsMode.ShowFPS);
    NiftyNode rootNode = nifty.createRootNodeFullscreen(ChildLayout.Center);

    NiftyNode centerNode = rootNode.newChildNode(ChildLayout.Vertical);
    centerNode.setWidthConstraint(UnitValue.percent(25));
    centerNode.setHeightConstraint(UnitValue.percent(10));

    niftyNode = centerNode.newChildNode(ChildLayout.Horizontal);

    Button button1 = niftyNode.newControl(Button.class);
    button1.setText("Default");
    button1.subscribe(this);

    niftyNode.newChildNode(UnitValue.px(20), UnitValue.wildcard());

    Button button2 = niftyNode.newControl(Button.class);
    button2.setStyleClass("flat-button");
    button2.setText("Custom");
    button2.subscribe(this);

    statusLabel = centerNode.newControl(Label.class);
    statusLabel.setFont(nifty.createFont("fonts/aurulent-sans-16.fnt"));
    statusLabel.setTextColor(NiftyColor.white());
    statusLabel.setText("Press a button!");
    statusLabel.getNode().setBackgroundColor(NiftyColor.black());
    statusLabel.getNode().setHAlign(HAlign.center);

    nifty.applyStyle(UseCase_c10_ButtonBasic.class.getResourceAsStream("UseCase_c10_ButtonBasic.css"));
  }

  @Handler
  private void onButtonClicked(final ButtonClickedEvent clickedEvent) {
    statusLabel.setText(clickedEvent.getButton().getText() + " clicked");
  }

  public static void main(final String[] args) throws Exception {
    UseCaseRunner.run(UseCase_c10_ButtonBasic.class, args);
  }
  */
}
