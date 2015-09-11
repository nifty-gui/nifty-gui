package de.lessvoid.nifty.spi.node;

import de.lessvoid.nifty.types.NiftyRect;

import javax.annotation.Nonnull;

/**
 * This interface defines a node that is able to receive layout data from outer layout nodes.
 * <p />
 * The node implementing his class will receive the arrangement rectangle of the first layout node in the tree
 * above this node. It will not be subject to the layout operations normally performed by the outer node. That means
 * that the stack layout applied by a {@link de.lessvoid.nifty.node.StackLayoutNode} will not effect a layout
 * receiver that is located directly below this node. Instead it will receive the arrangement rectangle that was
 * applied to his {@link de.lessvoid.nifty.node.StackLayoutNode}, means it will cover the same space as the
 * {@link de.lessvoid.nifty.node.StackLayoutNode} itself.
 * <p />
 * If you want to apply the layout operation of the {@link de.lessvoid.nifty.node.StackLayoutNode} to the receiver
 * you have to place a additional layout node in the tree between so the layout node is able to receive the data.
 * Most of the time a {@link de.lessvoid.nifty.node.SimpleLayoutNode} is a good candidate for that task.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public interface NiftyLayoutReceiver<T extends NiftyNode> extends NiftyNodeImpl<T> {
  /**
   * The rectangle where the node is located at.
   *
   * @param rect the rectangle for this node. This includes the origin location and the size.
   */
  void setLayoutResult(@Nonnull NiftyRect rect);
}
