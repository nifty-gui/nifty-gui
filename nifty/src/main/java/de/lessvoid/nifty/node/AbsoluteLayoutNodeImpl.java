package de.lessvoid.nifty.node;

import de.lessvoid.nifty.spi.node.NiftyLayoutNodeImpl;
import de.lessvoid.nifty.spi.node.NiftyNode;
import de.lessvoid.nifty.spi.node.NiftyNodeImpl;
import de.lessvoid.nifty.types.NiftyPoint;
import de.lessvoid.nifty.types.NiftyRect;
import de.lessvoid.nifty.types.NiftySize;

import javax.annotation.Nonnull;
import java.util.Collection;

import static de.lessvoid.nifty.types.NiftyRect.newNiftyRect;

/**
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
final class AbsoluteLayoutNodeImpl extends AbstractLayoutNodeImpl<AbsoluteLayoutNode> {
  @Nonnull
  @Override
  protected NiftySize measureInternal(@Nonnull NiftySize availableSize) {
    NiftySize resultSize = NiftySize.ZERO;
    for (NiftyLayoutNodeImpl<?> childLayoutNode : getLayout().getChildLayoutNodes(this)) {
      NiftyPoint location = NiftyPoint.ZERO;
      if (childLayoutNode instanceof AbsoluteLayoutChildNodeImpl) {
        AbsoluteLayoutChildNodeImpl absoluteLayoutChildNode = (AbsoluteLayoutChildNodeImpl) childLayoutNode;
        location = absoluteLayoutChildNode.getPoint();
      }
      NiftySize childSize = getLayout().measure(childLayoutNode, availableSize);
      resultSize = NiftySize.max(resultSize, childSize.add(location.getX(), location.getY()));
    }
    return resultSize;
  }

  @Override
  protected void arrangeInternal(@Nonnull NiftyRect area) {
    for (NiftyLayoutNodeImpl<?> childLayoutNode : getLayout().getChildLayoutNodes(this)) {
      NiftyPoint location = NiftyPoint.ZERO;
      if (childLayoutNode instanceof AbsoluteLayoutChildNodeImpl) {
        AbsoluteLayoutChildNodeImpl absoluteLayoutChildNode = (AbsoluteLayoutChildNodeImpl) childLayoutNode;
        location = absoluteLayoutChildNode.getPoint();
      }
      NiftySize desiredSize = childLayoutNode.getDesiredSize();
      getLayout().arrange(childLayoutNode,
          newNiftyRect(area.getOrigin().add(location.getX(), location.getY()), desiredSize));

    }
  }

  @Override
  protected AbsoluteLayoutNode createNode() {
    return new AbsoluteLayoutNode(this);
  }
}
