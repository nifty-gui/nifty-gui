package de.lessvoid.nifty.internal.layout;

import java.util.List;

import de.lessvoid.nifty.api.HorizontalAlignment;

/**
 * The children elements are arranged in a vertical form in relation to the root element.
 *
 * @author void
 */
public class InternalLayoutVertical implements InternalLayout {

  public void layoutElements(final InternalLayoutable root, final List <? extends InternalLayoutable> children) {
    if (isInvalid(root, children)) {
      return;
    }

    int rootBoxX = getRootBoxX(root);
    int rootBoxY = getRootBoxY(root);
    int rootBoxWidth = getRootBoxWidth(root);
    int rootBoxHeight = getRootBoxHeight(root);

    int y = rootBoxY;
    for (int i = 0; i < children.size(); i++) {
      InternalBox currentBox = children.get(i).getLayoutPos();
      InternalBoxConstraints currentBoxConstraints = children.get(i).getBoxConstraints();

      int elementHeight;

      if (hasHeightConstraint(currentBoxConstraints) && currentBoxConstraints.getHeight().hasWidthSuffix()) {
        int elementWidth = processWidthConstraints(rootBoxWidth, currentBoxConstraints, 0);
        currentBox.setWidth(elementWidth);

        elementHeight = calcElementHeight(children, rootBoxHeight, currentBoxConstraints, elementWidth);
        currentBox.setHeight(elementHeight);
      } else if (hasWidthConstraint(currentBoxConstraints) && currentBoxConstraints.getWidth().hasHeightSuffix()) {
        elementHeight = calcElementHeight(children, rootBoxHeight, currentBoxConstraints, 0);
        currentBox.setHeight(elementHeight);

        int elementWidth = processWidthConstraints(rootBoxWidth, currentBoxConstraints, elementHeight);
        currentBox.setWidth(elementWidth);
      } else {
        int elementWidth = processWidthConstraints(rootBoxWidth, currentBoxConstraints, 0);
        currentBox.setWidth(elementWidth);

        elementHeight = calcElementHeight(children, rootBoxHeight, currentBoxConstraints, 0);
        currentBox.setHeight(elementHeight);
      }

      int x = processHorizontalAlignment(rootBoxX, rootBoxWidth, currentBox.getWidth(), currentBoxConstraints);
      x = x + leftMargin(currentBoxConstraints, rootBoxWidth);
      currentBox.setX(x);

      y = y + topMargin(currentBoxConstraints, rootBoxHeight);
      currentBox.setY(y);

      y += elementHeight + bottomMargin(currentBoxConstraints, rootBoxHeight);
    }
  }

  private int leftMargin(final InternalBoxConstraints boxConstraints, final int rootBoxWidth) {
    return boxConstraints.getMarginLeft().getValueAsInt(rootBoxWidth);
  }

  private int topMargin(final InternalBoxConstraints boxConstraints, final int rootBoxHeight) {
    return boxConstraints.getMarginTop().getValueAsInt(rootBoxHeight);
  }

  private int bottomMargin(final InternalBoxConstraints boxConstraints, final int rootBoxHeight) {
    return boxConstraints.getMarginBottom().getValueAsInt(rootBoxHeight);
  }

  private int processWidthConstraints(final int rootBoxWidth, final InternalBoxConstraints constraints, final int elementHeight) {
    if (hasWidthConstraint(constraints)) {
      if (constraints.getWidth().hasHeightSuffix()) {
        return constraints.getWidth().getValueAsInt(elementHeight);
      }
      return constraints.getWidth().getValueAsInt(rootBoxWidth);
    } else {
      return rootBoxWidth;
    }
  }

  private int processHorizontalAlignment(final int rootBoxX, final int rootBoxWidth, final int currentBoxWidth, final InternalBoxConstraints constraints) {
    if (HorizontalAlignment.center.equals(constraints.getHorizontalAlign())) {
      return rootBoxX + ((rootBoxWidth - currentBoxWidth) / 2);
    } else if (HorizontalAlignment.right.equals(constraints.getHorizontalAlign())) {
      return rootBoxX + (rootBoxWidth - currentBoxWidth);
    } else if (HorizontalAlignment.left.equals(constraints.getHorizontalAlign())) {
      return rootBoxX;
    } else {
      // default = same as left
      return rootBoxX;
    }
  }

  private int calcElementHeight(final List<? extends InternalLayoutable> children, final int rootBoxHeight, final InternalBoxConstraints boxConstraints, final int boxWidth) {
    if (hasHeightConstraint(boxConstraints)) {
      if (boxConstraints.getHeight().hasWidthSuffix()) {
        return boxConstraints.getHeight().getValueAsInt(boxWidth);
      } else if (!boxConstraints.getHeight().hasWildcard()) {
        return boxConstraints.getHeight().getValueAsInt(rootBoxHeight);
      }
    }
    return getMaxNonFixedHeight(children, rootBoxHeight);
  }

  private int getMaxNonFixedHeight(final List<? extends InternalLayoutable> elements, final int parentHeight) {
    int maxFixedHeight = 0;
    int fixedCount = 0;

    for (int i = 0; i < elements.size(); i++) {
      InternalLayoutable p = elements.get(i);
      InternalBoxConstraints original = p.getBoxConstraints();
      if (hasHeightConstraint(original)) {
        if (original.getHeight().isPercentOrPixel()) {
          maxFixedHeight += original.getHeight().getValue(parentHeight);
          fixedCount++;
        }
      }
    }

    int notFixedCount = elements.size() - fixedCount;
    return (parentHeight - maxFixedHeight) / notFixedCount;
  }

  private boolean hasWidthConstraint(final InternalBoxConstraints constraints) {
    return constraints.getWidth() != null && !constraints.getWidth().hasWildcard();
  }

  private boolean hasHeightConstraint(final InternalBoxConstraints boxConstraints) {
    return boxConstraints.getHeight() != null;
  }

  private boolean isInvalid(final InternalLayoutable root, final List <? extends InternalLayoutable> children) {
    return root == null || children == null || children.size() == 0;
  }

  private int getRootBoxX(final InternalLayoutable root) {
    return
        root.getLayoutPos().getX() +
        root.getBoxConstraints().getPaddingLeft().getValueAsInt(root.getLayoutPos().getWidth());
  }

  private int getRootBoxY(final InternalLayoutable root) {
    return
        root.getLayoutPos().getY() +
        root.getBoxConstraints().getPaddingTop().getValueAsInt(root.getLayoutPos().getHeight());
  }

  private int getRootBoxWidth(final InternalLayoutable root) {
    return
        root.getLayoutPos().getWidth() -
        root.getBoxConstraints().getPaddingLeft().getValueAsInt(root.getLayoutPos().getWidth()) -
        root.getBoxConstraints().getPaddingRight().getValueAsInt(root.getLayoutPos().getWidth());
  }

  private int getRootBoxHeight(final InternalLayoutable root) {
    return
        root.getLayoutPos().getHeight() -
        root.getBoxConstraints().getPaddingTop().getValueAsInt(root.getLayoutPos().getHeight()) -
        root.getBoxConstraints().getPaddingBottom().getValueAsInt(root.getLayoutPos().getHeight());
  }
}
