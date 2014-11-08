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

import de.lessvoid.nifty.api.VAlign;
import de.lessvoid.nifty.internal.common.Box;

/**
 * The children elements are arranged in a horizontal form in relation to the root element.
 * 
 * @author void
 */
public class InternalLayoutHorizontal implements InternalLayout {

  /**
   * Layout the elements.
   * 
   * @param root
   *          the root element
   * @param children
   *          the children
   */
  public final void layoutElements(final InternalLayoutable root, final List<? extends InternalLayoutable> children) {
    if (isInvalid(root, children)) {
      return;
    }

    int rootBoxX = getRootBoxX(root);
    int rootBoxY = getRootBoxY(root);
    int rootBoxWidth = getRootBoxWidth(root);
    int rootBoxHeight = getRootBoxHeight(root);

    int x = rootBoxX;
    for (int i = 0; i < children.size(); i++) {
      InternalLayoutable current = children.get(i);
      Box box = current.getLayoutPos();
      InternalBoxConstraints boxConstraints = current.getBoxConstraints();

      int elementWidth;
      if (boxConstraints.getWidth() != null && boxConstraints.getWidth().hasHeightSuffix()) {
        int elementHeight = processHeightConstraint(rootBoxHeight, box, boxConstraints, 0);
        box.setHeight(elementHeight);

        elementWidth = calcElementWidth(children, rootBoxWidth, rootBoxHeight, boxConstraints, elementHeight);
        box.setWidth(elementWidth);
      } else if (hasHeightConstraint(boxConstraints) && boxConstraints.getHeight().hasWidthSuffix()) {
        elementWidth = calcElementWidth(children, rootBoxWidth, rootBoxHeight, boxConstraints, 0);
        box.setWidth(elementWidth);

        int elementHeight = processHeightConstraint(rootBoxHeight, box, boxConstraints, elementWidth);
        box.setHeight(elementHeight);
      } else {
        elementWidth = calcElementWidth(children, rootBoxWidth, rootBoxHeight, boxConstraints, 0);
        box.setWidth(elementWidth);

        int elementHeight = processHeightConstraint(rootBoxHeight, box, boxConstraints, 0);
        box.setHeight(elementHeight);
      }

      int y = processVerticalAlignment(rootBoxY, rootBoxHeight, box, boxConstraints);
      y = y + topMargin(boxConstraints, rootBoxHeight);
      box.setY(y);

      x = x + leftMargin(boxConstraints, rootBoxWidth);
      box.setX(x);

      x += elementWidth + rightMargin(boxConstraints, rootBoxWidth);
    }
  }

  private int leftMargin(final InternalBoxConstraints boxConstraints, final int rootBoxWidth) {
    return boxConstraints.getMarginLeft().getValueAsInt(rootBoxWidth);
  }

  private int rightMargin(final InternalBoxConstraints boxConstraints, final int rootBoxWidth) {
    return boxConstraints.getMarginRight().getValueAsInt(rootBoxWidth);
  }

  private int topMargin(final InternalBoxConstraints boxConstraints, final int rootBoxHeight) {
    return boxConstraints.getMarginTop().getValueAsInt(rootBoxHeight);
  }

  private int processHeightConstraint(
      final int rootBoxHeight,
      final Box box,
      final InternalBoxConstraints constraint,
      final int elementWidth) {
    if (hasHeightConstraint(constraint)) {
      if (constraint.getHeight().hasWidthSuffix()) {
        return constraint.getHeight().getValueAsInt(elementWidth);
      }
      return constraint.getHeight().getValueAsInt(rootBoxHeight);
    } else {
      return rootBoxHeight;
    }
  }

  private boolean hasHeightConstraint(final InternalBoxConstraints constraint) {
    return constraint.getHeight() != null && !constraint.getHeight().hasWildcard();
  }

  private int calcElementWidth(
      final List<? extends InternalLayoutable> children,
      final int rootBoxWidth,
      final int rootBoxHeight,
      final InternalBoxConstraints boxConstraints,
      final int elementHeight) {
    if (boxConstraints.getWidth() != null) {
      int h = (int) boxConstraints.getWidth().getValue(rootBoxWidth);
      if (boxConstraints.getWidth().hasHeightSuffix()) {
        h = (int) boxConstraints.getWidth().getValue(elementHeight);
      }
      if (h != -1) {
        return h;
      }
    }
    return getMaxNonFixedWidth(children, rootBoxWidth, rootBoxHeight);
  }

  private int processVerticalAlignment(
      final int rootBoxY,
      final int rootBoxHeight,
      final Box box,
      final InternalBoxConstraints boxConstraints) {
    if (VAlign.center.equals(boxConstraints.getVerticalAlign())) {
      return rootBoxY + ((rootBoxHeight - box.getHeight()) / 2);
    } else if (VAlign.top.equals(boxConstraints.getVerticalAlign())) {
      return rootBoxY;
    } else if (VAlign.bottom.equals(boxConstraints.getVerticalAlign())) {
      return rootBoxY + (rootBoxHeight - box.getHeight());
    } else {
      // top is default in here
      return rootBoxY;
    }
  }

  private int getMaxNonFixedWidth(final List<? extends InternalLayoutable> elements, final int parentWidth, final int parentHeight) {
    int maxFixedWidth = 0;
    int fixedCount = 0;
    for (int i = 0; i < elements.size(); i++) {
      InternalLayoutable p = elements.get(i);
      InternalBoxConstraints original = p.getBoxConstraints();

      if (original.getWidth() != null) {
        if (original.getWidth().isPercentOrPixel()) {
          if (original.getWidth().hasHeightSuffix()) {
            int elementHeight = processHeightConstraint(parentHeight, p.getLayoutPos(), original, 0);
            maxFixedWidth += (int) original.getWidth().getValue(elementHeight);
          } else {
            maxFixedWidth += original.getWidth().getValue(parentWidth);
          }
          fixedCount++;
        }
      }
    }

    int notFixedCount = elements.size() - fixedCount;
    return (parentWidth - maxFixedWidth) / notFixedCount;
  }

  private boolean isInvalid(final InternalLayoutable root, final List<? extends InternalLayoutable> children) {
    return root == null || children == null || children.size() == 0;
  }

  private int getRootBoxX(final InternalLayoutable root) {
    return 0 + root.getBoxConstraints().getPaddingLeft().getValueAsInt(root.getLayoutPos().getWidth());
  }

  private int getRootBoxY(final InternalLayoutable root) {
    return 0 + root.getBoxConstraints().getPaddingTop().getValueAsInt(root.getLayoutPos().getHeight());
  }

  private int getRootBoxWidth(final InternalLayoutable root) {
    return root.getLayoutPos().getWidth()
        - root.getBoxConstraints().getPaddingLeft().getValueAsInt(root.getLayoutPos().getWidth())
        - root.getBoxConstraints().getPaddingRight().getValueAsInt(root.getLayoutPos().getWidth());
  }

  private int getRootBoxHeight(final InternalLayoutable root) {
    return root.getLayoutPos().getHeight()
        - root.getBoxConstraints().getPaddingTop().getValueAsInt(root.getLayoutPos().getHeight())
        - root.getBoxConstraints().getPaddingBottom().getValueAsInt(root.getLayoutPos().getHeight());
  }
}
