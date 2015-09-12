package de.lessvoid.nifty.node;

import de.lessvoid.nifty.spi.node.NiftyNode;
import de.lessvoid.nifty.spi.node.NiftyNodeImpl;

import javax.annotation.Nonnull;

/**
 * This is the layout node for a docking layout. This means that child nodes are docked to one of the four sides of
 * this layout. The order of the child nodes does matter, because it determines how much space is left for the node.
 * Also multiple nodes can be docked to one side to get a similar effect like a stack layout.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public final class DockLayoutNode implements NiftyNode {
  @Nonnull
  private final DockLayoutNodeImpl implementation;

  @Nonnull
  public static DockLayoutNode dockLayoutNode() {
    return dockLayoutNode(false);
  }

  @Nonnull
  public static DockLayoutNode dockLayoutNode(final boolean lastNodeFill) {
    return new DockLayoutNode(lastNodeFill);
  }

  private DockLayoutNode(final boolean lastNodeFill) {
    this(new DockLayoutNodeImpl(lastNodeFill));
  }

  public boolean isLastNodeFill() {
    return implementation.isLastNodeFill();
  }

  public void setLastNodeFill(final boolean lastNodeFill) {
    implementation.setLastNodeFill(lastNodeFill);
  }

  DockLayoutNode(@Nonnull final DockLayoutNodeImpl implementation) {
    this.implementation = implementation;
  }

  @Nonnull
  NiftyNodeImpl<DockLayoutNode> getImpl() {
    return implementation;
  }

  @Nonnull
  @Override
  public String toString() {
    return "(DockLayoutNode)";
  }
}
