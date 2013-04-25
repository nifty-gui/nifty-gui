package de.lessvoid.nifty.internal;

import de.lessvoid.nifty.api.UnitValue;

/**
 * This is a helper class so that we can use the Layout mechanism even for positioning the root node.
 * @author void
 */
class InternalLayoutableScreenSized implements InternalLayoutable {
  private final InternalBox box;
  private final InternalBoxConstraints boxConstraints;

  InternalLayoutableScreenSized(final int width, final int height) {
    box = new InternalBox(0, 0, width, height);
    boxConstraints = new InternalBoxConstraints();
    boxConstraints.setWidth(UnitValue.px(width));
    boxConstraints.setHeight(UnitValue.px(height));
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
