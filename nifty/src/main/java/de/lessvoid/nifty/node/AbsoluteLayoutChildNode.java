package de.lessvoid.nifty.node;

import de.lessvoid.nifty.spi.node.NiftyNode;
import de.lessvoid.nifty.spi.node.NiftyNodeImpl;
import de.lessvoid.nifty.types.NiftyPoint;

import javax.annotation.Nonnull;

import static de.lessvoid.nifty.types.NiftyPoint.newNiftyPoint;

/**
 * This is a child layout node for the {@link AbsoluteLayoutNode}. This node allows to set the x and y coordinate
 * where the node is placed.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public final class AbsoluteLayoutChildNode implements NiftyNode {
  @Nonnull
  private final AbsoluteLayoutChildNodeImpl implementation;

  public AbsoluteLayoutChildNode() {
    this(0.f, 0.f);
  }

  public AbsoluteLayoutChildNode(final float x, final float y) {
    this(newNiftyPoint(x, y));
  }

  public AbsoluteLayoutChildNode(@Nonnull final NiftyPoint point) {
    this(new AbsoluteLayoutChildNodeImpl(point));
  }

  AbsoluteLayoutChildNode(@Nonnull final AbsoluteLayoutChildNodeImpl implementation) {
    this.implementation = implementation;
  }

  public float getX() {
    return getPoint().getX();
  }

  public void setX(final float x) {
    setPoint(newNiftyPoint(x, getY()));
  }

  public float getY() {
    return getPoint().getY();
  }

  public void setY(final float y) {
    setPoint(newNiftyPoint(getX(), y));
  }

  @Nonnull
  public NiftyPoint getPoint() {
    return implementation.getPoint();
  }

  public void setPoint(@Nonnull final NiftyPoint point) {
    implementation.setPoint(point);
  }

  @Nonnull
  NiftyNodeImpl<AbsoluteLayoutChildNode> getImpl() {
    return implementation;
  }

  @Nonnull
  @Override
  public String toString() {
    return "(AbsoluteLayoutChildNodeImpl) x [" + getX() + "px] y [" + getY() + "px]" ;
  }
}
