package de.lessvoid.nifty.node;

import de.lessvoid.nifty.spi.node.NiftyLayoutNodeImpl;
import de.lessvoid.nifty.spi.node.NiftyNodeImpl;
import de.lessvoid.nifty.types.NiftyPoint;
import de.lessvoid.nifty.types.NiftyRect;
import de.lessvoid.nifty.types.NiftySize;

import javax.annotation.Nonnull;
import java.util.Collection;

/**
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
class AbsoluteLayoutChildNodeImpl extends AbstractLayoutNodeImpl<AbsoluteLayoutChildNode> {
  @Nonnull
  private NiftyPoint point;

  AbsoluteLayoutChildNodeImpl(@Nonnull final NiftyPoint point) {
    this.point = point;
  }

  @Nonnull
  public NiftyPoint getPoint() {
    return point;
  }

  public void setPoint(@Nonnull NiftyPoint point) {
    if (!this.point.equals(point)) {
      this.point = point;
      invalidateMeasure();
    }
  }

  @Nonnull
  @Override
  protected NiftySize measureInternal(@Nonnull NiftySize availableSize) {
    Collection<NiftyNodeImpl<?>> children = getLayout().getDirectChildren(this);
    if (children.isEmpty()) {
      /* No child elements, means that we do not require any size. */
      return NiftySize.ZERO;
    }

    NiftySize childSize = NiftySize.ZERO;
    for (NiftyNodeImpl<?> child : children) {
      childSize = NiftySize.max(getLayout().measure(child, availableSize), childSize);
    }
    return childSize;
  }

  @Override
  protected void arrangeInternal(@Nonnull NiftyRect area) {
    Collection<NiftyNodeImpl<?>> children = getLayout().getDirectChildren(this);
    if (children.isEmpty()) {
      /* No child elements -> We are all done. */
      return;
    }

    for (NiftyNodeImpl<?> child : children) {
      getLayout().arrange(child, area);
    }
  }

  @Override
  protected AbsoluteLayoutChildNode createNode() {
    return new AbsoluteLayoutChildNode(this);
  }
}
