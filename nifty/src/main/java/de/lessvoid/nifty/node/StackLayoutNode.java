package de.lessvoid.nifty.node;

import de.lessvoid.nifty.api.Nifty;
import de.lessvoid.nifty.api.NiftyNodeBuilder;
import de.lessvoid.nifty.api.node.NiftyNode;

import javax.annotation.Nonnull;

/**
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public final class StackLayoutNode {
  @Nonnull
  private final StackLayoutNodeImpl implementation;

  public StackLayoutNode() {
    this(Orientation.Vertical);
  }

  public StackLayoutNode(@Nonnull final Orientation orientation) {
    this(orientation, false);
  }

  public StackLayoutNode(@Nonnull final Orientation orientation, final boolean stretchLast) {
    implementation = new StackLayoutNodeImpl(orientation, stretchLast);
  }

  @Nonnull
  public Orientation getOrientation() {
    return implementation.getOrientation();
  }

  public void setOrientation(@Nonnull final Orientation orientation) {
    implementation.setOrientation(orientation);
  }

  public boolean isStretchLast() {
    return implementation.isStretchLast();
  }

  public void setStretchLast(final boolean stretchLast) {
    implementation.setStretchLast(stretchLast);
  }

  public void attachNode(@Nonnull final NiftyNodeBuilder builder) {
    builder.addChildNode(implementation);
  }

  public void attachNode(@Nonnull final Nifty nifty, @Nonnull NiftyNode parentNode) {
    nifty.addNode(parentNode, implementation);
  }
}
