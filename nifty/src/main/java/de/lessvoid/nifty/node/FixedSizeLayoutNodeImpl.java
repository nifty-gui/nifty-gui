package de.lessvoid.nifty.node;

import de.lessvoid.nifty.spi.node.NiftyNodeImpl;
import de.lessvoid.nifty.types.NiftyRect;
import de.lessvoid.nifty.types.NiftySize;

import javax.annotation.Nonnull;
import java.util.Collection;

/**
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
final class FixedSizeLayoutNodeImpl extends AbstractLayoutNodeImpl<FixedSizeLayoutNode> {
  @Nonnull
  private NiftySize size;

  public FixedSizeLayoutNodeImpl(@Nonnull final NiftySize size) {
    if (size.isInfinite() || size.isInvalid()) throw new IllegalArgumentException("The size has to be a finite value.");

    this.size = size;
  }

  @Nonnull
  public NiftySize getSize() {
    return size;
  }

  public void setSize(@Nonnull NiftySize size) {
    if (size.isInfinite() || size.isInvalid()) throw new IllegalArgumentException("The size has to be a finite value.");

    if (!this.size.equals(size)) {
      this.size = size;
      invalidateMeasure();
    }
  }

  @Nonnull
  @Override
  protected NiftySize measureInternal(@Nonnull NiftySize availableSize) {
    return size;
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
  protected FixedSizeLayoutNode createNode() {
    return new FixedSizeLayoutNode(this);
  }
}
