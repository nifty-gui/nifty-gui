package de.lessvoid.nifty.node;

import de.lessvoid.nifty.spi.node.NiftyNode;
import de.lessvoid.nifty.spi.node.NiftyNodeImpl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * This is a layout node for a absolute value. It will place every single child node at 0,0 unless it is a
 * {@link AbsoluteLayoutChildNode} that supports setting a location.
 * <p>
 *   This layout will arrange it's children based on their desired size. So a children are <b>not</b> stretched to the
 *   full available size.
 * </p>
 * <h3>Layout Concept</h3>
 * <p>
 *   <img src="doc-files/AbsoluteLayoutNode-1.svg" alt="Concept Image" />
 * </p>
 * <ul>
 *   <li><b>X:</b> {@link AbsoluteLayoutChildNode#getX()}</li>
 *   <li><b>Y:</b> {@link AbsoluteLayoutChildNode#getY()}</li>
 * </ul>
 * <p>
 *   This node does not ensure that the child nodes are fully within the parent area. In case the X and Y values are
 *   chosen badly, the objects will be placed outside of the area of this node.
 * </p>
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public final class AbsoluteLayoutNode implements NiftyNode {
  @Nonnull
  private final AbsoluteLayoutNodeImpl implementation;

  /**
   * Create a new instance of the absolute layout node.
   * <p />
   * There are no parameters to this node, because the actual placement of the nodes is defined by x and y
   * coordinates defined in the {@link AbsoluteLayoutChildNode}s.
   *
   * @return the new instance
   */
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

  @Override
  public boolean equals(@Nullable  final Object obj) {
    return (obj instanceof AbsoluteLayoutNode) && ((AbsoluteLayoutNode) obj).implementation.equals(implementation);
  }

  @Override
  public int hashCode() {
    return implementation.hashCode();
  }

  @Nonnull
  @Override
  public String toString() {
    return "(AbsoluteLayoutNode)";
  }
}
