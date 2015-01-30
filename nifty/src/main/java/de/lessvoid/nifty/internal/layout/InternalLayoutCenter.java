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
package de.lessvoid.nifty.internal.layout;

import java.util.List;

import de.lessvoid.nifty.api.HAlign;
import de.lessvoid.nifty.api.VAlign;
import de.lessvoid.nifty.internal.common.Box;

/**
 * CenterLayout centers all child elements. If there are more than one child elements all elements will be centered
 * (and over layed above each other). Remember that center probably makes only sense if the centered element has some
 * width and height constraints set.
 *
 * @author void
 */
public class InternalLayoutCenter implements InternalLayout {

  @Override
  public void layoutElements(final InternalLayoutable rootElement, final List <? extends InternalLayoutable> elements) {
    if (rootElement == null || elements == null || elements.size() == 0) {
      return;
    }

    Box rootBox = rootElement.getLayoutPos();
    InternalBoxConstraints rootBoxConstraints = rootElement.getBoxConstraints();

    for (int i=0; i<elements.size(); i++) {
      layoutElement(elements.get(i), rootBox, rootBoxConstraints);
    }
  }

  private int leftMargin(final InternalBoxConstraints boxConstraints, final int rootBoxWidth) {
    return boxConstraints.getMarginLeft().getValueAsInt(rootBoxWidth);
  }

  private int topMargin(final InternalBoxConstraints boxConstraints, final int rootBoxHeight) {
    return boxConstraints.getMarginTop().getValueAsInt(rootBoxHeight);
  }

  private void layoutElement(final InternalLayoutable element, Box rootBox, InternalBoxConstraints rootBoxConstraints) {
    Box box = element.getLayoutPos();
    InternalBoxConstraints constraint = element.getBoxConstraints();

    if (constraint.getWidth() != null && constraint.getWidth().hasHeightSuffix()) {
      handleVerticalAlignment(rootBox, rootBoxConstraints, box, constraint);
      handleHorizontalAlignment(rootBox, rootBoxConstraints, box, constraint);
    } else if (constraint.getHeight() != null && constraint.getHeight().hasWidthSuffix()) {
      handleHorizontalAlignment(rootBox, rootBoxConstraints, box, constraint);
      handleVerticalAlignment(rootBox, rootBoxConstraints, box, constraint);
    } else {
      handleVerticalAlignment(rootBox, rootBoxConstraints, box, constraint);
      handleHorizontalAlignment(rootBox, rootBoxConstraints, box, constraint);
    }

    box.setX(box.getX() + leftMargin(constraint, rootBox.getWidth()));// - rightMargin(constraint, rootBox.getWidth()));
    box.setY(box.getY() + topMargin(constraint, rootBox.getHeight()));// - bottomMargin(constraint, rootBox.getHeight()));
  }

  void handleHorizontalAlignment(
      final Box rootBox,
      final InternalBoxConstraints rootBoxConstraints,
      final Box box,
      final InternalBoxConstraints constraint) {
    if (constraint.getWidth() != null) {
      handleWidthConstraint(rootBox, rootBoxConstraints, box, constraint);
    } else {
      box.setX(
          0
          + rootBoxConstraints.getPaddingLeft().getValueAsInt(rootBox.getWidth()));
      box.setWidth(
          rootBox.getWidth()
          - rootBoxConstraints.getPaddingLeft().getValueAsInt(rootBox.getWidth())
          - rootBoxConstraints.getPaddingRight().getValueAsInt(rootBox.getWidth()));
    }
  }

  void handleVerticalAlignment(
      final Box rootBox,
      final InternalBoxConstraints rootBoxConstraints,
      final Box box,
      final InternalBoxConstraints constraint) {
    if (constraint.getHeight() != null) {
      handleHeightConstraint(rootBox, rootBoxConstraints, box, constraint);
    } else {
      box.setY(
          0
          + rootBoxConstraints.getPaddingTop().getValueAsInt(rootBox.getHeight()));
      box.setHeight(
          rootBox.getHeight()
          - rootBoxConstraints.getPaddingTop().getValueAsInt(rootBox.getHeight())
          - rootBoxConstraints.getPaddingBottom().getValueAsInt(rootBox.getHeight()));
    }
  }

  private void handleWidthConstraint(
      final Box rootBox,
      final InternalBoxConstraints rootBoxConstraints,
      final Box box,
      final InternalBoxConstraints constraint) {
    int rootBoxX = 0 + rootBoxConstraints.getPaddingLeft().getValueAsInt(rootBox.getWidth());
    int rootBoxWidth = rootBox.getWidth() -
                       rootBoxConstraints.getPaddingLeft().getValueAsInt(rootBox.getWidth()) -
                       rootBoxConstraints.getPaddingRight().getValueAsInt(rootBox.getWidth()) -
                       constraint.getMarginLeft().getValueAsInt(rootBox.getWidth()) -
                       constraint.getMarginRight().getValueAsInt(rootBox.getWidth());

    int boxWidth = (int) constraint.getWidth().getValue(rootBoxWidth);
    if (constraint.getWidth().hasHeightSuffix()) {
      boxWidth = (int) constraint.getWidth().getValue(box.getHeight()); 
    }
    box.setWidth(boxWidth);

    if (constraint.getHorizontalAlign() == HAlign.left) {
      box.setX(rootBoxX);
    } else if (constraint.getHorizontalAlign() == HAlign.right) {
      box.setX(rootBoxX +
               rootBox.getWidth() -
               rootBoxConstraints.getPaddingRight().getValueAsInt(rootBox.getWidth()) - boxWidth);
    } else {
      // default and center is the same in here
      box.setX(rootBoxX + (rootBoxWidth - boxWidth) / 2);
    }
  }

  private void handleHeightConstraint(
      final Box rootBox,
      final InternalBoxConstraints rootBoxConstraints,
      final Box box,
      final InternalBoxConstraints constraint) {
    int rootBoxY = 0 + rootBoxConstraints.getPaddingTop().getValueAsInt(rootBox.getHeight());
    int rootBoxHeight = rootBox.getHeight() -
                        rootBoxConstraints.getPaddingTop().getValueAsInt(rootBox.getHeight()) -
                        rootBoxConstraints.getPaddingBottom().getValueAsInt(rootBox.getHeight()) -
                        constraint.getMarginTop().getValueAsInt(rootBox.getHeight()) -
                        constraint.getMarginBottom().getValueAsInt(rootBox.getHeight());

    int boxHeight = (int) constraint.getHeight().getValue(rootBoxHeight);
    if (constraint.getHeight().hasWidthSuffix()) {
      boxHeight = (int) constraint.getHeight().getValue(box.getWidth());
    }
    box.setHeight(boxHeight);

    if (constraint.getVerticalAlign() == VAlign.top) {
      box.setY(rootBoxY);
    } else if (constraint.getVerticalAlign() == VAlign.bottom) {
      box.setY(rootBoxY +
               rootBox.getHeight() -
               rootBoxConstraints.getPaddingBottom().getValueAsInt(rootBox.getHeight()) - boxHeight);
    } else {
      // center is default in here
      box.setY(rootBoxY + (rootBoxHeight - boxHeight) / 2);
    }
  }
}
