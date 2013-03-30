package de.lessvoid.nifty.internal;

import org.junit.Assert;

import de.lessvoid.nifty.internal.InternalBox;
import de.lessvoid.nifty.internal.InternalBoxConstraints;
import de.lessvoid.nifty.internal.InternalLayoutable;

public class InternalLayoutableTestImpl implements InternalLayoutable {
  private InternalBox box = new InternalBox();
  private InternalBoxConstraints boxConstraints = new InternalBoxConstraints();

  public InternalLayoutableTestImpl() {
  }

  public InternalLayoutableTestImpl(final InternalBox box, final InternalBoxConstraints boxConstraint) {
    Assert.assertNotNull(box);
    Assert.assertNotNull(boxConstraint);

    this.box = box;
    this.boxConstraints = boxConstraint;
  }

  @Override
  public InternalBox getLayoutPos() {
    return box;
  }

  @Override
  public InternalBoxConstraints getBoxConstraints() {
    return boxConstraints;
  }
}
