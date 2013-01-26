package de.lessvoid.niftyimpl.layout.manager;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.lessvoid.niftyimpl.layout.Box;
import de.lessvoid.niftyimpl.layout.BoxConstraints;
import de.lessvoid.niftyimpl.layout.Layoutable;

public class OverlayLayoutTest {
  private final OverlayLayout layout = new OverlayLayout();
  private final List<Layoutable> children = new ArrayList<Layoutable>();
  private LayoutPartTestHelper root;

  @Before
  public void before() {
    Box box = new Box(0, 0, 640, 480);
    BoxConstraints boxConstraint = new BoxConstraints();
    root = new LayoutPartTestHelper(box, boxConstraint);
  }

  @Test
  public void testRootNull() {
    layout.layoutElements(null, children);
  }

  @Test
  public void testChildrenNull() {
    layout.layoutElements(root, null);
  }

  @Test
  public void testChildrenEmpty() {
    layout.layoutElements(root, children);
  }

  @Test
  public void testSingleChild() {
    LayoutPartTestHelper child = new LayoutPartTestHelper();
    children.add(child);

    layout.layoutElements(root, children);

    Assert.assertBox(child.getLayoutPos(), 0, 0, 640, 480);
  }
}
