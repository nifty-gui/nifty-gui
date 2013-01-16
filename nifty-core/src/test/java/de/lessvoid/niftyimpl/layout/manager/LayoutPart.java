package de.lessvoid.niftyimpl.layout.manager;

import de.lessvoid.niftyimpl.layout.Box;
import de.lessvoid.niftyimpl.layout.BoxConstraints;
import de.lessvoid.niftyimpl.layout.Layoutable;

public class LayoutPart implements Layoutable {
  private Box box = new Box();
  private BoxConstraints boxConstraints = new BoxConstraints();

  public LayoutPart() {
  }

  public LayoutPart(final Box box, final BoxConstraints boxConstraint) {
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
