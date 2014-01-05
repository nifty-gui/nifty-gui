package de.lessvoid.nifty.internal.layout;

import org.junit.Assert;

import de.lessvoid.nifty.internal.common.Box;
import de.lessvoid.nifty.internal.layout.InternalBoxConstraints;
import de.lessvoid.nifty.internal.layout.InternalLayoutable;

public class InternalLayoutableTestImpl implements InternalLayoutable {
  private Box box = new Box();
  private InternalBoxConstraints boxConstraints = new InternalBoxConstraints();

  public InternalLayoutableTestImpl() {
  }

  public InternalLayoutableTestImpl(final Box box, final InternalBoxConstraints boxConstraint) {
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
  public InternalBoxConstraints getBoxConstraints() {
    return boxConstraints;
  }
}
