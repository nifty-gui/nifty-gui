package de.lessvoid.nifty.internal.layout;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.internal.layout.InternalBox;
import de.lessvoid.nifty.internal.layout.InternalBoxConstraints;
import de.lessvoid.nifty.internal.layout.InternalLayoutOverlay;
import de.lessvoid.nifty.internal.layout.InternalLayoutable;

public class InternalLayoutOverlayTest {
  private final InternalLayoutOverlay layout = new InternalLayoutOverlay();
  private final List<InternalLayoutable> children = new ArrayList<InternalLayoutable>();
  private InternalLayoutableTestImpl root;

  @Before
  public void before() {
    InternalBox box = new InternalBox(0, 0, 640, 480);
    InternalBoxConstraints boxConstraint = new InternalBoxConstraints();
    root = new InternalLayoutableTestImpl(box, boxConstraint);
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
    InternalLayoutableTestImpl child = new InternalLayoutableTestImpl();
    children.add(child);

    layout.layoutElements(root, children);

    Assert.assertBox(child.getLayoutPos(), 0, 0, 640, 480);
  }
}
