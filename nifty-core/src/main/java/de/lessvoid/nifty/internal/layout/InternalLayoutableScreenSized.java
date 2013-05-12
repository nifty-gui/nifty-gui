package de.lessvoid.nifty.internal.layout;

import de.lessvoid.nifty.api.UnitValue;
import de.lessvoid.nifty.internal.common.Box;

/**
 * This is a helper class so that we can use the Layout mechanism even for positioning the root node.
 * @author void
 */
public class InternalLayoutableScreenSized implements InternalLayoutable {
  private final Box box;
  private final InternalBoxConstraints boxConstraints;

  public InternalLayoutableScreenSized(final int width, final int height) {
    box = new Box(0, 0, width, height);
    boxConstraints = new InternalBoxConstraints();
    boxConstraints.setWidth(UnitValue.px(width));
    boxConstraints.setHeight(UnitValue.px(height));
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
