package de.lessvoid.nifty.internal.layout;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.api.Assert;
import de.lessvoid.nifty.internal.Box;
import de.lessvoid.nifty.internal.BoxConstraints;

public class LayoutOverlayTest {
  private final LayoutOverlay layout = new LayoutOverlay();
  private final List<Layoutable> children = new ArrayList<Layoutable>();
  private LayoutableTestImpl root;

  @Before
  public void before() {
    Box box = new Box(0, 0, 640, 480);
    BoxConstraints boxConstraint = new BoxConstraints();
    root = new LayoutableTestImpl(box, boxConstraint);
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
    LayoutableTestImpl child = new LayoutableTestImpl();
    children.add(child);

    layout.layoutElements(root, children);

    Assert.assertBox(child.getLayoutPos(), 0, 0, 640, 480);
  }
}
