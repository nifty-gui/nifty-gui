package de.lessvoid.nifty.node;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.spi.node.NiftyNode;
import de.lessvoid.nifty.types.Rect;
import de.lessvoid.nifty.types.Size;

import javax.annotation.Nonnull;
import java.util.Collection;

/**
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
class FixedSizeLayoutNodeImpl extends AbstractLayoutNodeImpl {
  private float height;
  private float width;

  public FixedSizeLayoutNodeImpl(float width, float height) {
    if (!(Math.abs(height) <= Float.MAX_VALUE)) throw new IllegalArgumentException("height is expected to be a finite value.");
    if (!(Math.abs(width) <= Float.MAX_VALUE)) throw new IllegalArgumentException("width is expected to be a finite value.");

    this.height = height;
    this.width = width;
  }

  float getWidth() {
    return width;
  }

  void setWidth(float width) {
    if (!(Math.abs(width) <= Float.MAX_VALUE)) throw new IllegalArgumentException("width is expected to be a finite value.");

    if (this.width != width) {
      this.width = width;

      invalidateMeasure();
    }
  }

  float getHeight() {
    return height;
  }

  void setHeight(float height) {
    if (!(Math.abs(height) <= Float.MAX_VALUE)) throw new IllegalArgumentException("height is expected to be a finite value.");

    if (this.height != height) {
      this.height = height;

      invalidateMeasure();
    }
  }

  @Nonnull
  @Override
  protected Size measureInternal(@Nonnull Size availableSize) {
    return new Size(width, height);
  }

  @Override
  protected void arrangeInternal(@Nonnull Rect area) {
    Collection<NiftyNode> children = getLayout().getDirectChildren(this);
    if (children.isEmpty()) {
      /* No child elements -> We are all done. */
      return;
    }

    for (NiftyNode child : children) {
      getLayout().arrange(child, area);
    }
  }

  @Override
  protected NiftyNode createNode() {
    return null;
  }

  @Override
  public void initialize(Nifty nifty, NiftyNode niftyNode) {

  }
}
