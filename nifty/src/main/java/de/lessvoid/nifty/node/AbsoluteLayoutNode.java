package de.lessvoid.nifty.node;

import de.lessvoid.nifty.spi.node.NiftyNode;
import de.lessvoid.nifty.spi.node.NiftyNodeImpl;

import javax.annotation.Nonnull;

/**
 * This is a layout node for a absolute value. It will place every single child node at 0,0 unless it is a
 * {@link AbsoluteLayoutChildNode} that supports setting a location.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public final class AbsoluteLayoutNode implements NiftyNode {
  @Nonnull
  private final AbsoluteLayoutNodeImpl implementation;

  @Nonnull
  public static AbsoluteLayoutNode absoluteLayoutNode() {
    return new AbsoluteLayoutNode();
  }

  private AbsoluteLayoutNode() {
    this(new AbsoluteLayoutNodeImpl());
  }

  AbsoluteLayoutNode(@Nonnull final AbsoluteLayoutNodeImpl implementation) {
    this.implementation = implementation;
  }

  @Nonnull
  NiftyNodeImpl<AbsoluteLayoutNode> getImpl() {
    return implementation;
  }

  @Nonnull
  @Override
  public String toString() {
    return "(AbsoluteLayoutNode)";
  }
}
