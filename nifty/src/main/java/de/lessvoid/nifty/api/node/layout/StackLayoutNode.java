package de.lessvoid.nifty.api.node.layout;

import de.lessvoid.nifty.api.NiftyLayout;
import de.lessvoid.nifty.api.node.NiftyNode;
import de.lessvoid.nifty.api.types.Point;
import de.lessvoid.nifty.api.types.Rect;
import de.lessvoid.nifty.api.types.Size;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;

/**
 * The layout node implementation for a stack layout.
 *
 * <p>This layout type is able to place multiple child nodes in either horizontal or vertical orientation.</p>
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public final class StackLayoutNode extends AbstractLayoutNode {
  @Nonnull
  private Orientation orientation;
  private boolean stretchLast;

  public StackLayoutNode(@Nonnull final NiftyLayout layout) {
    super(layout);
    orientation = Orientation.Vertical;
  }

  @Nonnull
  @Override
  protected Size measureInternal(@Nonnull final Size availableSize) {
    if (availableSize.isInvalid()) {
      throw new IllegalArgumentException("Supplied size value for measure must not be invalid.");
    }

    Collection<NiftyNode> children = getLayout().getDirectChildren(this);
    if (children.isEmpty()) {
      /* No child elements, means that we do not require any size. */
      return Size.ZERO;
    }

    Size remainingSize = availableSize;
    Size requiredSize = Size.ZERO;
    for (NiftyNode child : children) {
      Size childSize = getLayout().measure(child, remainingSize);
      if (orientation == Orientation.Horizontal) {
        requiredSize = new Size(requiredSize.getWidth() + childSize.getWidth(),
            Math.max(requiredSize.getHeight(), childSize.getHeight()));
        if (!Float.isInfinite(remainingSize.getWidth())) {
          remainingSize = new Size(remainingSize.getWidth() - childSize.getWidth(), remainingSize.getHeight());
        }
      } else {
        requiredSize = new Size(Math.max(requiredSize.getWidth(), childSize.getWidth()),
            requiredSize.getHeight() + childSize.getHeight());
        if (!Float.isInfinite(remainingSize.getHeight())) {
          remainingSize = new Size(remainingSize.getWidth(), remainingSize.getHeight() - childSize.getHeight());
        }
      }
    }

    return requiredSize;
  }

  @Override
  protected void arrangeInternal(@Nonnull final Rect area) {
    List<NiftyNode> children = getLayout().getDirectChildren(this);
    if (children.isEmpty()) {
      /* No child elements -> We are all done. */
      return;
    }

    Point currentOrigin = area.getOrigin();
    Size remainingSize = area.getSize();

    int childrenCount = children.size();
    for (int i = 0; i < childrenCount; i++) {
      NiftyNode child = children.get(i);
      Size childSize = getLayout().getDesiredSize(child);
      Size arrangedSize;
      Point nextOrigin;
      if ((i == (childrenCount - 1)) && stretchLast) {
        arrangedSize = remainingSize;
        nextOrigin = currentOrigin; // never used
      } else if (orientation == Orientation.Horizontal) {
        arrangedSize = new Size(Math.min(remainingSize.getWidth(), childSize.getWidth()), remainingSize.getHeight());
        remainingSize = new Size(remainingSize.getWidth() - arrangedSize.getWidth(), remainingSize.getHeight());
        nextOrigin = new Point(currentOrigin.getX() + arrangedSize.getWidth(), currentOrigin.getY());
      } else {
        arrangedSize = new Size(remainingSize.getWidth(), Math.min(remainingSize.getHeight(), childSize.getWidth()));
        remainingSize = new Size(remainingSize.getWidth(), remainingSize.getHeight() - arrangedSize.getHeight());
        nextOrigin = new Point(currentOrigin.getX(), currentOrigin.getY() + arrangedSize.getHeight());
      }

      getLayout().arrange(child, new Rect(currentOrigin, arrangedSize));
      currentOrigin = nextOrigin;
    }
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

  public boolean isStretchLast() {
    return stretchLast;
  }

  public void setStretchLast(final boolean stretchLast) {
    if (this.stretchLast != stretchLast) {
      this.stretchLast = stretchLast;
      invalidateArrange();
    }
  }
}
