package de.lessvoid.nifty.layout.manager;

import static de.lessvoid.nifty.layout.manager.BoxTestHelper.assertBox;

import java.util.ArrayList;

import junit.framework.TestCase;
import de.lessvoid.nifty.layout.Box;
import de.lessvoid.nifty.layout.BoxConstraints;
import de.lessvoid.nifty.layout.LayoutPart;
import de.lessvoid.nifty.layout.align.HorizontalAlign;
import de.lessvoid.nifty.tools.SizeValue;
public class CenterLayoutWithPaddingTest extends TestCase {

    private LayoutPart rootPanel;
    private CenterLayout layout;
    private BoxConstraints constraint;

    protected void setUp() throws Exception {
      Box box = new Box(0, 0, 640, 480);
      BoxConstraints boxConstraint = new BoxConstraints();

      rootPanel = new LayoutPart(box, boxConstraint);
      rootPanel.getBoxConstraints().setPadding(new SizeValue("50px"));

      layout = new CenterLayout();
      constraint = new BoxConstraints();
    }

    public void testHorizontalAlignCenterWithBorder() throws Exception {
      constraint.setWidth(new SizeValue("100px"));
      constraint.setHeight(new SizeValue("100px"));
      constraint.setHorizontalAlign(HorizontalAlign.center);

      LayoutPart child = prepare(constraint);
      assertBox(child.getBox(), 270, 190, 100, 100);
    }

    public void testHorizontalAlignCenterWithBorderNoConstraint() throws Exception {
      constraint.setHorizontalAlign(HorizontalAlign.center);

      LayoutPart child = prepare(constraint);
      assertBox(child.getBox(), 50, 50, 540, 380);
    }

    private LayoutPart prepare(final BoxConstraints constraint) {
      Box box = new Box();
      LayoutPart child = new LayoutPart(box, constraint);
      ArrayList < LayoutPart > elements = new ArrayList < LayoutPart >();
      elements.add(child);
      layout.layoutElements(rootPanel, elements);
      return child;
    }
}
