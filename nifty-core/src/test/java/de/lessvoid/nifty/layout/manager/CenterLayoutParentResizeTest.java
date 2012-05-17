package de.lessvoid.nifty.layout.manager;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.layout.Box;
import de.lessvoid.nifty.layout.BoxConstraints;
import de.lessvoid.nifty.layout.LayoutPart;
import de.lessvoid.nifty.tools.SizeValue;
public class CenterLayoutParentResizeTest {
    private LayoutPart root;
    private LayoutPart a;
    private LayoutPart b;
    private LayoutManager layout;

    @Before
    public void before() {
      root = new LayoutPart(new Box(), new BoxConstraints());
      a = new LayoutPart();
      b = new LayoutPart();
      layout = new CenterLayout();
    }

    @Test
    public void testSingleChildWidth() throws Exception {
      a.getBoxConstraints().setWidth(SizeValue.px(25));
      assertEquals(SizeValue.px(25), layout.calculateConstraintWidth(root, Arrays.asList(a)));
    }

    @Test
    public void testSingleChildHeight() throws Exception {
      a.getBoxConstraints().setHeight(SizeValue.px(25));
      assertEquals(SizeValue.px(25), layout.calculateConstraintHeight(root, Arrays.asList(a)));
    }

    @Test
    public void testWidthIsMax() throws Exception {
      a.getBoxConstraints().setWidth(SizeValue.px(25));
      b.getBoxConstraints().setWidth(SizeValue.px(75));
      assertEquals(SizeValue.px(75), layout.calculateConstraintWidth(root, Arrays.asList(a, b)));
    }

    @Test
    public void testHeightIsMax() throws Exception {
      a.getBoxConstraints().setHeight(SizeValue.px(25));
      b.getBoxConstraints().setHeight(SizeValue.px(75));
      assertEquals(SizeValue.px(75), layout.calculateConstraintHeight(root, Arrays.asList(a, b)));
    }
}
