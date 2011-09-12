package de.lessvoid.nifty.layout.manager;

import java.util.ArrayList;

import junit.framework.TestCase;
import de.lessvoid.nifty.layout.Box;
import de.lessvoid.nifty.layout.BoxConstraints;
import de.lessvoid.nifty.layout.LayoutPart;
import de.lessvoid.nifty.layout.align.HorizontalAlign;
import de.lessvoid.nifty.layout.align.VerticalAlign;
import de.lessvoid.nifty.tools.SizeValue;

public class CenterLayoutTest extends TestCase {

    private LayoutPart rootPanel;
    private CenterLayout layout;

    protected void setUp() throws Exception {
      Box box = new Box(0, 0, 640, 480);
      BoxConstraints boxConstraint = new BoxConstraints();
      rootPanel = new LayoutPart(box, boxConstraint);

      layout = new CenterLayout();
    }

    public void testEmpty() throws Exception {
      layout.layoutElements(null, null);
    }

    public void testEmptyList() throws Exception {
      layout.layoutElements(null, new ArrayList<LayoutPart>());
    }

    public void testCenterSingle() throws Exception {
      ArrayList < LayoutPart > elements = new ArrayList<LayoutPart>();

      Box box = new Box();
      BoxConstraints constraint = new BoxConstraints();
      constraint.setWidth(new SizeValue("100px"));
      constraint.setHeight(new SizeValue("100px"));
      constraint.setHorizontalAlign(HorizontalAlign.center);
      constraint.setVerticalAlign(VerticalAlign.center);

      LayoutPart child = new LayoutPart(box, constraint);
      elements.add(child);

      layout.layoutElements(rootPanel, elements);

      assertEquals(270, child.getBox().getX());
      assertEquals(190, child.getBox().getY());
    }

    public static void assertBoxLeftWidth(final Box box, final int left, final int width) {
      assertEquals(left, box.getX());
      assertEquals(width, box.getWidth());
    }

    public static void assertBoxTopHeight(final Box box, final int top, final int height) {
      assertEquals(top, box.getY());
      assertEquals(height, box.getHeight());
    }
}
