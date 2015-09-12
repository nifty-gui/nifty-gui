package de.lessvoid.nifty.node;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.spi.node.NiftyLayoutNodeImpl;
import de.lessvoid.nifty.types.NiftyPoint;
import de.lessvoid.nifty.types.NiftyRect;
import de.lessvoid.nifty.types.NiftySize;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.List;

import static de.lessvoid.nifty.types.NiftyPoint.newNiftyPoint;
import static de.lessvoid.nifty.types.NiftyRect.newNiftyRect;
import static de.lessvoid.nifty.types.NiftySize.newNiftySize;

/**
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
final class DockLayoutNodeImpl extends AbstractLayoutNodeImpl<DockLayoutNode> {
  private boolean lastNodeFill;

  DockLayoutNodeImpl(final boolean lastNodeFill) {
    this.lastNodeFill = lastNodeFill;
  }

  public boolean isLastNodeFill() {
    return lastNodeFill;
  }

  public void setLastNodeFill(final boolean lastNodeFill) {
    this.lastNodeFill = lastNodeFill;
  }

  @Nonnull
  @Override
  protected NiftySize measureInternal(@Nonnull final NiftySize availableSize) {
    List<NiftyLayoutNodeImpl<?>> tmpList = new ArrayList<>();
    for (NiftyLayoutNodeImpl<?> childLayoutNode : getLayout().getChildLayoutNodes(this)) {
      tmpList.add(childLayoutNode);
    }

    NiftySize remainingSize = availableSize;
    NiftySize northSouthSize = NiftySize.ZERO;
    NiftySize eastWestSize = NiftySize.ZERO;
    NiftySize otherSize = NiftySize.ZERO;

    for (int i = 0, tmpListSize = tmpList.size(); i < tmpListSize; i++) {
      NiftyLayoutNodeImpl<?> childLayoutNode = tmpList.get(i);
      NiftySize nodeSize = getLayout().measure(childLayoutNode, remainingSize);

      if ((i == (tmpListSize - 1)) && lastNodeFill) {
        northSouthSize = newNiftySize(
            Math.max(northSouthSize.getWidth(), nodeSize.getWidth()),
            northSouthSize.getHeight() + nodeSize.getHeight());
        eastWestSize = newNiftySize(
            eastWestSize.getWidth() + nodeSize.getWidth(),
            Math.max(eastWestSize.getHeight(), nodeSize.getHeight()));
      } else {

        /*
         Now the child node is either the proper child node, or it is just expanded to what ever size is remaining at
         this point. So that may cause some strange effects, but we really do not have any choice.
         */
        if (childLayoutNode instanceof DockLayoutChildNodeImpl) {
          DockLayoutChildNodeImpl dockChild = (DockLayoutChildNodeImpl) childLayoutNode;
          switch (dockChild.getDock()) {
            case North:
            case South:
              remainingSize = remainingSize.add(0, -nodeSize.getHeight());
              northSouthSize = newNiftySize(
                  Math.max(northSouthSize.getWidth(), nodeSize.getWidth()),
                  northSouthSize.getHeight() + nodeSize.getHeight());
              break;
            case East:
            case West:
              remainingSize = remainingSize.add(-nodeSize.getWidth(), 0);
              eastWestSize = newNiftySize(
                  eastWestSize.getWidth() + nodeSize.getWidth(),
                  Math.max(eastWestSize.getHeight(), nodeSize.getHeight()));
              break;
            default:
              throw new NotImplementedException();
          }
        } else {
         /*
         No idea what this node is doing here. We just see if it would fit into the remaining size and be good with it.
         */
          otherSize = NiftySize.max(nodeSize, otherSize);
        }
      }
    }

    return NiftySize.max(northSouthSize, eastWestSize, otherSize);
  }

  @Override
  protected void arrangeInternal(@Nonnull final NiftyRect area) {
    List<NiftyLayoutNodeImpl<?>> tmpList = new ArrayList<>();
    for (NiftyLayoutNodeImpl<?> childLayoutNode : getLayout().getChildLayoutNodes(this)) {
      tmpList.add(childLayoutNode);
    }

    NiftyRect remainingRect = area;
    for (int i = 0, tmpListSize = tmpList.size(); i < tmpListSize; i++) {
      NiftyLayoutNodeImpl<?> childLayoutNode = tmpList.get(i);

      if ((i == (tmpListSize - 1)) && lastNodeFill) {
        childLayoutNode.arrange(remainingRect);
      } else {
        if (childLayoutNode instanceof DockLayoutChildNodeImpl) {
          DockLayoutChildNodeImpl dockChild = (DockLayoutChildNodeImpl) childLayoutNode;
          NiftySize desiredSize = dockChild.getDesiredSize();
          NiftyRect childRect;
          switch (dockChild.getDock()) {
            case North:
              float childHeight = Math.min(remainingRect.getSize().getHeight(), desiredSize.getHeight());

              childRect = newNiftyRect(remainingRect.getOrigin(),
                  newNiftySize(remainingRect.getSize().getWidth(), childHeight));

              remainingRect = newNiftyRect(remainingRect.getOrigin().add(0.f, childHeight),
                  remainingRect.getSize().add(0.f, -childHeight));
              break;
            case South:
              childHeight = Math.min(remainingRect.getSize().getHeight(), desiredSize.getHeight());

              childRect = newNiftyRect(newNiftyPoint(remainingRect.getOrigin().getX(),
                      remainingRect.getSize().getHeight() - childHeight),
                  newNiftySize(remainingRect.getSize().getWidth(), childHeight));

              remainingRect = newNiftyRect(remainingRect.getOrigin(),
                  remainingRect.getSize().add(0.f, -childHeight));
              break;
            case East:
              float childWidth = Math.min(remainingRect.getSize().getWidth(), desiredSize.getWidth());

              childRect = newNiftyRect(newNiftyPoint(remainingRect.getSize().getWidth() - childWidth,
                      remainingRect.getOrigin().getY()),
                  newNiftySize(childWidth, remainingRect.getSize().getHeight()));

              remainingRect = newNiftyRect(remainingRect.getOrigin(),
                  remainingRect.getSize().add(-childWidth, 0.f));
              break;
            case West:
              childWidth = Math.min(remainingRect.getSize().getWidth(), desiredSize.getWidth());

              childRect = newNiftyRect(remainingRect.getOrigin(),
                  newNiftySize(childWidth, remainingRect.getSize().getHeight()));

              remainingRect = newNiftyRect(remainingRect.getOrigin().add(childWidth, 0.f),
                  remainingRect.getSize().add(-childWidth, 0.f));
              break;
            default:
              throw new NotImplementedException();
          }
          childLayoutNode.arrange(childRect);
        } else {
          /* Some other layout node. That can't be part of the normal layout, so it is just stretched. */
          childLayoutNode.arrange(area);
        }
      }
    }
  }

  @Override
  protected DockLayoutNode createNode() {
    return new DockLayoutNode(this);
  }
}
