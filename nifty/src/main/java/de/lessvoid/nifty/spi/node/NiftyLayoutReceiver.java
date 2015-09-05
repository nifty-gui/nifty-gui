package de.lessvoid.nifty.spi.node;

import de.lessvoid.nifty.types.NiftyRect;

import javax.annotation.Nonnull;

/**
 * This interface defines a node that is able to receive layout data from outer layout nodes.
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
