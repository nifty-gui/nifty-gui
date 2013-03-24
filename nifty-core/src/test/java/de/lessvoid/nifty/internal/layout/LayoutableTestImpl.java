package de.lessvoid.nifty.internal.layout;

import org.junit.Assert;

import de.lessvoid.nifty.internal.Box;
import de.lessvoid.nifty.internal.BoxConstraints;

public class LayoutableTestImpl implements Layoutable {
  private Box box = new Box();
  private BoxConstraints boxConstraints = new BoxConstraints();

  public LayoutableTestImpl() {
  }

  public LayoutableTestImpl(final Box box, final BoxConstraints boxConstraint) {
    Assert.assertNotNull(box);
    Assert.assertNotNull(boxConstraint);

    this.box = box;
    this.boxConstraints = boxConstraint;
  }

  @Override
  public Box getLayoutPos() {
    return box;
  }

  @Override
  public BoxConstraints getBoxConstraints() {
    return boxConstraints;
  }
}
