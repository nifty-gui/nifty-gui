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
 * Padding and Margin demonstration.
 * @author void
 */
public class UseCase_a06_PaddingAndMargin {
  /* FIXME: There's a new example with padding. This one should be deleted.
  private NiftyFont font;

  public UseCase_a06_PaddingAndMargin(final Nifty nifty) throws IOException {
    nifty.clearScreenBeforeRender();
    nifty.showStatistics(NiftyStatisticsMode.ShowFPS);

    font = nifty.createFont("fonts/aurulent-sans-16.fnt");

    final NiftyNode niftyNode = nifty.createRootNodeFullscreen(ChildLayout.Vertical);
    niftyNode.newChildNode(UnitValue.percent(100), UnitValue.px(10)).setBackgroundColor(NiftyColor.gray());

    firstRowPaddingExamples(niftyNode);
    secondRowPaddingExampels(niftyNode);

    firstRowMarginExamples(niftyNode);
    secondRowMarginExamples(niftyNode);
  }

  private void firstRowPaddingExamples(final NiftyNode niftyNode) {
    final NiftyNode row = addRow(niftyNode, NiftyColor.black());

    row.newChildNode(UnitValue.px(10), UnitValue.px(10));

    node(row, NiftyColor.yellow(), "paddingTop = 25").setPaddingTop(UnitValue.px(25));
    node(row, NiftyColor.red(), "paddingRight = 25").setPaddingRight(UnitValue.px(25));
    node(row, NiftyColor.blue(), "paddingBottom = 25").setPaddingBottom(UnitValue.px(25));
    node(row, NiftyColor.lime(), "paddingLeft = 25").setPaddingLeft(UnitValue.px(25));

    niftyNode.newChildNode(UnitValue.percent(100), UnitValue.px(10)).setBackgroundColor(NiftyColor.gray());
  }

  private void secondRowPaddingExampels(final NiftyNode niftyNode) {
    final NiftyNode row = addRow(niftyNode, NiftyColor.gray());
    row.newChildNode(UnitValue.px(10), UnitValue.px(10));

    node(row, NiftyColor.yellow(), "padding = 10").setPadding(UnitValue.px(10));
    node(row, NiftyColor.red(), "padding = 10 20").setPadding(UnitValue.px(10), UnitValue.px(20));
    node(row, NiftyColor.blue(), "padding = 10 20 30").setPadding(UnitValue.px(10), UnitValue.px(20), UnitValue.px(30));
    node(row, NiftyColor.lime(), "padding = 10 20 30 40").setPadding(UnitValue.px(10), UnitValue.px(20), UnitValue.px(30), UnitValue.px(40));

    niftyNode.newChildNode(UnitValue.percent(100), UnitValue.px(10)).setBackgroundColor(NiftyColor.gray());
  }

  private void firstRowMarginExamples(final NiftyNode niftyNode) {
    final NiftyNode row = addRow(niftyNode, NiftyColor.black());
    row.newChildNode(UnitValue.px(10), UnitValue.px(10));

    node(row, NiftyColor.yellow(), "marginTop = 25").setMarginTop(UnitValue.px(25));
    node(row, NiftyColor.red(), "marginRight = 25").setMarginRight(UnitValue.px(25));
    node(row, NiftyColor.blue(), "marginBottom = 25").setMarginBottom(UnitValue.px(25));
    node(row, NiftyColor.lime(), "marginLeft = 25").setMarginLeft(UnitValue.px(25));
  }

  private void secondRowMarginExamples(final NiftyNode niftyNode) {
    final NiftyNode row = addRow(niftyNode, NiftyColor.gray());
    row.newChildNode(UnitValue.px(10), UnitValue.px(10));

    node(row, NiftyColor.yellow(), "marginTop = 10").setMargin(UnitValue.px(10));
    node(row, NiftyColor.red(), "marginRight = 10 20").setMargin(UnitValue.px(10), UnitValue.px(20));
    node(row, NiftyColor.blue(), "marginBottom = 10 20 30").setMargin(UnitValue.px(10), UnitValue.px(20), UnitValue.px(30));
    node(row, NiftyColor.lime(), "marginLeft = 10 20 30 40").setMargin(UnitValue.px(10), UnitValue.px(20), UnitValue.px(30), UnitValue.px(40));
  }

  private NiftyNode addRow(final NiftyNode niftyNode, final NiftyColor color) {
    NiftyNode row = niftyNode.newChildNode(UnitValue.percent(100), UnitValue.percent(25), ChildLayout.Horizontal);
    row.setBackgroundColor(color);
    row.setHAlign(HAlign.center);
    return row;
  }

  private NiftyNode node(final NiftyNode niftyNode, final NiftyColor color, final String description) {
    NiftyNode node = niftyNode.newChildNode(UnitValue.px(200), UnitValue.px(100), ChildLayout.Center);
    node.setBackgroundColor(color);

    NiftyNode child = node.newChildNode(UnitValue.percent(100), UnitValue.percent(100), ChildLayout.Center);
    child.setBackgroundColor(NiftyColor.fromColorWithAlpha(NiftyColor.black(), 0.5));

    Label label = child.newControl(Label.class);
    label.setText(description);
    label.setFont(font);
    label.setTextColor(NiftyColor.white());

    niftyNode.newChildNode(UnitValue.px(10), UnitValue.px(10));

    return node;
  }

  public static void main(final String[] args) throws Exception {
    UseCaseRunner.run(UseCase_a06_PaddingAndMargin.class, args);
  }
  */
}
