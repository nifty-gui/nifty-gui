package de.lessvoid.nifty.node;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.spi.node.NiftyNode;
import de.lessvoid.nifty.spi.node.NiftyNodeImpl;
import de.lessvoid.nifty.types.Point;
import de.lessvoid.nifty.types.Rect;
import de.lessvoid.nifty.types.Size;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;

/**
 * This is the implementation of a layout node that support padding. It will apply some padding to any child layout
 * node.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
class PaddingLayoutNodeImpl extends AbstractLayoutNodeImpl<PaddingLayoutNode> {
  /**
   * The padding applied to the bottom.
   */
  private float bottom;
  /**
   * The padding applied to the left side.
   */
  private float left;
  /**
   * The padding applied to the right side.
   */
  private float right;
  /**
   * The padding applied to the top.
   */
  private float top;

  PaddingLayoutNodeImpl(final float top, final float right, final float bottom, final float left) {
    if (!(Math.abs(top) <= Float.MAX_VALUE)) throw new IllegalArgumentException("top is expected to be a finite value.");
    if (!(Math.abs(right) <= Float.MAX_VALUE)) throw new IllegalArgumentException("right is expected to be a finite value.");
    if (!(Math.abs(bottom) <= Float.MAX_VALUE)) throw new IllegalArgumentException("bottom is expected to be a finite value.");
    if (!(Math.abs(left) <= Float.MAX_VALUE)) throw new IllegalArgumentException("left is expected to be a finite value.");

    this.top = top;
    this.right = right;
    this.bottom = bottom;
    this.left = left;
  }

  float getBottom() {
    return bottom;
  }

  void setBottom(float bottom) {
    if (!(Math.abs(bottom) <= Float.MAX_VALUE)) throw new IllegalArgumentException("bottom is expected to be a finite value.");

    if (this.bottom != bottom){
      this.bottom = bottom;

      invalidateMeasure();
    }
  }

  float getLeft() {
    return left;
  }

  void setLeft(float left) {
    if (!(Math.abs(left) <= Float.MAX_VALUE)) throw new IllegalArgumentException("left is expected to be a finite value.");

    if (this.left != left) {
      this.left = left;

      invalidateMeasure();
    }
  }

  float getRight() {
    return right;
  }

  void setRight(float right) {
    if (!(Math.abs(right) <= Float.MAX_VALUE)) throw new IllegalArgumentException("right is expected to be a finite value.");

    if (this.right != right) {
      this.right = right;

      invalidateMeasure();
    }
  }

  float getTop() {
    return top;
  }

  void setTop(float top) {
    if (!(Math.abs(top) <= Float.MAX_VALUE)) throw new IllegalArgumentException("top is expected to be a finite value.");

    if (this.top != top) {
      this.top = top;

      invalidateMeasure();
    }
  }

  @Nonnull
  @Override
  protected Size measureInternal(@Nonnull Size availableSize) {
    Collection<NiftyNodeImpl<?>> children = getLayout().getDirectChildren(this);
    if (children.isEmpty()) {
      /* No child elements, means that we do not require any size. */
      return new Size(left + right, top + bottom);
    }

    Size childSize = Size.ZERO;
    for (NiftyNodeImpl<?> child : children) {
      childSize = Size.max(getLayout().measure(child, availableSize), childSize);
    }

    return new Size(left + right + childSize.getWidth(), top + bottom + childSize.getHeight());
  }

  @Override
  protected void arrangeInternal(@Nonnull Rect area) {
    Collection<NiftyNodeImpl<?>> children = getLayout().getDirectChildren(this);
    if (children.isEmpty()) {
      /* No child elements -> We are all done. */
      return;
    }

    Point newOrigin = new Point(area.getOrigin(), left, top);
    Size newSize = new Size(area.getSize().getWidth() - left - right, area.getSize().getHeight() - top - bottom);
    Rect newArea = new Rect(newOrigin, newSize);

    for (NiftyNodeImpl<?> child : children) {
      getLayout().arrange(child, newArea);
    }
  }

  @Override
  protected PaddingLayoutNode createNode() {
    return new PaddingLayoutNode(this);
  }
}
