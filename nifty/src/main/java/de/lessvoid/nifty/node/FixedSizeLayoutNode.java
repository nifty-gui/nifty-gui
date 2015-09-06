package de.lessvoid.nifty.node;

import de.lessvoid.nifty.spi.node.NiftyNode;
import de.lessvoid.nifty.spi.node.NiftyNodeImpl;
import de.lessvoid.nifty.types.NiftySize;

import javax.annotation.Nonnull;

import static de.lessvoid.nifty.types.NiftySize.newNiftySize;

/**
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public final class FixedSizeLayoutNode implements NiftyNode {
  @Nonnull
  private final FixedSizeLayoutNodeImpl implementation;

  @Nonnull
  public static FixedSizeLayoutNode fixedSizeLayoutNode(@Nonnull final NiftySize size) {
    return new FixedSizeLayoutNode(size);
  }

  @Nonnull
  public static FixedSizeLayoutNode fixedSizeLayoutNode(final float width, final float height) {
    return fixedSizeLayoutNode(newNiftySize(width, height));
  }

  private FixedSizeLayoutNode(@Nonnull final NiftySize size) {
    this(new FixedSizeLayoutNodeImpl(size));
  }

  FixedSizeLayoutNode(@Nonnull final FixedSizeLayoutNodeImpl implementation) {
    this.implementation = implementation;
  }

  public float getWidth() {
    return getSize().getWidth();
  }

  public void setWidth(final float width) {
    setSize(newNiftySize(width, getHeight()));
  }

  public float getHeight() {
    return getSize().getHeight();
  }

  public void setHeight(final float height) {
    setSize(newNiftySize(getWidth(), height));
  }

  public void setSize(@Nonnull final NiftySize size) {
    implementation.setSize(size);
  }

  @Nonnull
  public NiftySize getSize() {
    return implementation.getSize();
  }

  @Nonnull
  NiftyNodeImpl<FixedSizeLayoutNode> getImpl() {
    return implementation;
  }

  @Nonnull
  @Override
  public String toString() {
    return "(FixedSizeLayoutNode) width [" + getWidth() + "px] height [" + getHeight() + "px]";
  }
}
