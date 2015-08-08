package de.lessvoid.nifty.node;

import de.lessvoid.nifty.api.NiftyLayout;
import de.lessvoid.nifty.api.node.NiftyLayoutNode;
import de.lessvoid.nifty.api.types.Rect;
import de.lessvoid.nifty.api.types.Size;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
abstract class AbstractLayoutNode implements NiftyLayoutNode {
  @Nullable
  private NiftyLayout layout;
  private boolean measureValid;
  private boolean arrangeValid;
  @Nullable
  private Size desiredSize;
  @Nullable
  private Rect arrangeRect;

  protected AbstractLayoutNode() {}

  @Override
  public void activate(@Nonnull final NiftyLayout layout) {
    if (this.layout != null) {
      throw new IllegalStateException("This node was already activated.");
    }
    this.layout = layout;
    invalidateMeasure();
    invalidateArrange();
  }

  @Override
  public boolean isMeasureValid() {
    return measureValid;
  }

  @Override
  public boolean isArrangeValid() {
    return arrangeValid;
  }

  @Override
  public void invalidateMeasure() {
    if ((layout != null) && isMeasureValid()) {
      measureValid = false;
      layout.reportMeasureInvalid(this);
    }
  }

  @Override
  public void invalidateArrange() {
    if ((layout != null) && isArrangeValid()) {
      arrangeValid = false;
      layout.reportArrangeInvalid(this);
    }
  }

  @Nonnull
  @Override
  public Size getDesiredSize() {
    return (desiredSize != null) && isMeasureValid() ? desiredSize : Size.INVALID;
  }

  @Nonnull
  @Override
  public Rect getArrangedRect() {
    return (arrangeRect == null) ? Rect.INVALID : arrangeRect;
  }

  @Nonnull
  @Override
  public final Size measure(@Nonnull final Size availableSize) {
    Size size = measureInternal(availableSize);
    if (!size.equals(desiredSize)) {
      invalidateArrange();
    }
    desiredSize = size;
    measureValid = true;
    return size;
  }

  @Nonnull
  protected abstract Size measureInternal(@Nonnull Size availableSize);

  @Override
  public final void arrange(@Nonnull final Rect area) {
    arrangeInternal(area);
    arrangeRect = area;
    arrangeValid = true;
  }

  protected abstract void arrangeInternal(@Nonnull Rect area);

  @Nonnull
  protected final NiftyLayout getLayout() {
    return layout;
  }
}
