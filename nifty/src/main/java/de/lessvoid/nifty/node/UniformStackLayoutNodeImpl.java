package de.lessvoid.nifty.node;

import de.lessvoid.nifty.api.node.NiftyNode;
import de.lessvoid.nifty.api.types.Point;
import de.lessvoid.nifty.api.types.Rect;
import de.lessvoid.nifty.api.types.Size;

import javax.annotation.Nonnull;
import java.util.Collection;

/**
 * The layout node implementation for a uniform stack layout.
 *
 * <p>This layout type is able to place multiple child nodes in either horizontal or vertical orientation. Every
 * child element is the same size assigned</p>
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
final class UniformStackLayoutNodeImpl extends AbstractLayoutNodeImpl {
  @Nonnull
  private Orientation orientation;

  public UniformStackLayoutNodeImpl(@Nonnull final Orientation orientation) {
    this.orientation = orientation;
  }

  @Override
  @Nonnull
  protected Size measureInternal(@Nonnull final Size availableSize) {
    if (availableSize.isInvalid()) {
      throw new IllegalArgumentException("Supplied size value for measure must not be invalid.");
    }

    Collection<NiftyNode> children = getLayout().getDirectChildren(this);
    if (children.isEmpty()) {
      /* No child elements, means that we do not require any size. */
      return Size.ZERO;
    }

    Size sizePerChild = getSizePerChild(availableSize, children.size());

    Size largestSizeRequested = Size.ZERO;
    for (NiftyNode node : children) {
      Size nodeSize = getLayout().measure(node, sizePerChild);
      largestSizeRequested = Size.max(nodeSize, largestSizeRequested);
    }

    if (orientation == Orientation.Horizontal) {
      return new Size(largestSizeRequested.getWidth() * children.size(), largestSizeRequested.getHeight());
    } else {
      return new Size(largestSizeRequested.getWidth(), largestSizeRequested.getHeight() * children.size());
    }
  }

  @Override
  protected void arrangeInternal(@Nonnull final Rect area) {
    Collection<NiftyNode> children = getLayout().getDirectChildren(this);
    if (children.isEmpty()) {
      /* No child elements -> We are all done. */
      return;
    }

    Size sizePerChild = getSizePerChild(area.getSize(), children.size());
    Point currentOrigin = area.getOrigin();
    for (NiftyNode child : children) {
      getLayout().arrange(child, new Rect(currentOrigin, sizePerChild));

      /* Move the origin along the orientation */
      if (orientation == Orientation.Horizontal) {
        currentOrigin = new Point(currentOrigin.getX() + sizePerChild.getWidth(), currentOrigin.getY());
      } else {
        currentOrigin = new Point(currentOrigin.getX(), currentOrigin.getY() + sizePerChild.getHeight());
      }
    }
  }

  @Nonnull
  private Size getSizePerChild(@Nonnull final Size outerSize, final int childCount) {
    Size sizePerChild;
    if (orientation == Orientation.Horizontal) {
      /* Uniform Horizontal Stacking. Each element is assigned a equal share of the total width */
      if (Float.isInfinite(outerSize.getWidth())) {
        sizePerChild = outerSize;
      } else {
        sizePerChild = new Size(outerSize.getWidth() / childCount, outerSize.getHeight());
      }
    } else {
      /* Uniform Vertical Stacking. Each element is assigned a equal share of the total height */
      if (Float.isInfinite(outerSize.getHeight())) {
        sizePerChild = outerSize;
      } else {
        sizePerChild = new Size(outerSize.getWidth(), outerSize.getHeight() / childCount);
      }
    }
    return sizePerChild;
  }

  @Nonnull
  public Orientation getOrientation() {
    return orientation;
  }

  public void setOrientation(@Nonnull final Orientation orientation) {
    if (this.orientation != orientation) {
      this.orientation = orientation;
      invalidateMeasure();
      invalidateArrange();
    }
  }

  @Override
  public void initialize(final NiftyNode niftyNode) {

  }

  @Override
  public NiftyNode getNiftyNode() {
    return null;
  }
}
