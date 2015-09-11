package de.lessvoid.nifty.node;

import de.lessvoid.nifty.spi.node.NiftyNode;
import de.lessvoid.nifty.spi.node.NiftyNodeImpl;

import javax.annotation.Nonnull;

/**
 * The padding layout nodes applies a padding to the inner layout nodes. Each direct inner layout node is subject to
 * the padding applied.
 * <p />
 * Each child node will be stretched to the full available size minus the padding. The desired size will be the
 * maximum size requested by the child nodes plus the padding.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public final class PaddingLayoutNode implements NiftyNode {
  @Nonnull
  private final PaddingLayoutNodeImpl implementation;

  @Nonnull
  public static PaddingLayoutNode paddingLayoutNode(final float allSides) {
    return paddingLayoutNode(allSides, allSides);
  }

  @Nonnull
  public static PaddingLayoutNode paddingLayoutNode(final float topBottom, final float rightLeft) {
    return paddingLayoutNode(topBottom, rightLeft, topBottom);
  }

  @Nonnull
  public static PaddingLayoutNode paddingLayoutNode(final float top, final float rightLeft, final float bottom) {
    return paddingLayoutNode(top, rightLeft, bottom, rightLeft);
  }

  @Nonnull
  public static PaddingLayoutNode paddingLayoutNode(final float top, final float right, final float bottom, final float left) {
    return new PaddingLayoutNode(top, right, bottom, left);
  }

  private PaddingLayoutNode(final float top, final float right, final float bottom, final float left) {
    this(new PaddingLayoutNodeImpl(top, right, bottom, left));
  }

  PaddingLayoutNode(@Nonnull final PaddingLayoutNodeImpl implementation) {
    this.implementation = implementation;
  }

  public float getLeft() {
    return implementation.getLeft();
  }

  public void setLeft(float left) {
    implementation.setLeft(left);
  }

  public float getRight() {
    return implementation.getRight();
  }

  public void setRight(float right) {
    implementation.setRight(right);
  }

  public float getTop() {
    return implementation.getTop();
  }

  public void setTop(float top) {
    implementation.setTop(top);
  }

  public float getBottom() {
    return implementation.getBottom();
  }

  public void setBottom(float bottom) {
    implementation.setBottom(bottom);
  }

  @Nonnull
  NiftyNodeImpl<PaddingLayoutNode> getImpl() {
    return implementation;
  }
}
