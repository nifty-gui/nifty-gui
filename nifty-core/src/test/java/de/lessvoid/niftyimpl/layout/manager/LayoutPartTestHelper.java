package de.lessvoid.niftyimpl.layout.manager;

import org.junit.Assert;

import de.lessvoid.niftyimpl.layout.Box;
import de.lessvoid.niftyimpl.layout.BoxConstraints;
import de.lessvoid.niftyimpl.layout.Layoutable;

public class LayoutPartTestHelper implements Layoutable {
  private Box box = new Box();
  private BoxConstraints boxConstraints = new BoxConstraints();

  public LayoutPartTestHelper() {
  }

  public LayoutPartTestHelper(final Box box, final BoxConstraints boxConstraint) {
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
