package de.lessvoid.nifty.elements;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.layout.manager.AbsolutePositionLayout;
import de.lessvoid.nifty.layout.manager.CenterLayout;
import de.lessvoid.nifty.layout.manager.HorizontalLayout;
import de.lessvoid.nifty.layout.manager.LayoutManager;
import de.lessvoid.nifty.layout.manager.OverlayLayout;
import de.lessvoid.nifty.layout.manager.VerticalLayout;
import de.lessvoid.nifty.tools.SizeValue;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class ExplicitSumTest
{
  private Nifty niftyMock;
  private Element root;
  private Element a;
  private Element b;
  private Element c;
  private LayoutManager[] layouts;

  @Before
  public void before() {
    niftyMock = createNiceMock(Nifty.class);
    replay(niftyMock);
    
    root = new Element(niftyMock, null, null, null, null, false, null);
    root.setConstraintWidth(SizeValue.sum());
    root.setConstraintHeight(SizeValue.sum());
    a = new Element(niftyMock, null, null, null, null, false, null);
    b = new Element(niftyMock, null, null, null, null, false, null);
    c = new Element(niftyMock, null, null, null, null, false, null);
    c.setConstraintWidth(SizeValue.percent(50)); // should be ignored
    c.setConstraintHeight(SizeValue.percent(70));
    
    layouts = new LayoutManager[] { 
      new AbsolutePositionLayout(),
      new CenterLayout(),
      new HorizontalLayout(),
      new OverlayLayout(),
      new VerticalLayout()
    };
  }
  
  @Test
  public void testSingleChildWidth() throws Exception {
    a.setConstraintWidth(SizeValue.px(25));
    root.addChild(a);
    root.addChild(c);
    
    for (LayoutManager layout : layouts) {
      root.setLayoutManager(layout);
      root.layoutElements();
      assertEquals(25, root.getConstraintWidth().getValueAsInt(0));
    }
  }
  
  @Test
  public void testSingleChildHeight() throws Exception {
    a.setConstraintHeight(SizeValue.px(25));
    root.addChild(a);
    root.addChild(c);
    
    for (LayoutManager layout : layouts) {
      root.setLayoutManager(layout);
      root.layoutElements();
      assertEquals(25, root.getConstraintHeight().getValueAsInt(0));
    }
  }

  @Test
  public void testWidthIsSum() throws Exception {
    a.setConstraintWidth(SizeValue.px(25));
    b.setConstraintWidth(SizeValue.px(75));
    root.addChild(a);
    root.addChild(b);
    root.addChild(c);
    
    for (LayoutManager layout : layouts) {
      root.setLayoutManager(layout);
      root.layoutElements();
      assertEquals(100, root.getConstraintWidth().getValueAsInt(0));
    }
  }

  @Test
  public void testHeightIsSum() throws Exception {
    a.setConstraintHeight(SizeValue.px(75));
    b.setConstraintHeight(SizeValue.px(25));
    root.addChild(a);
    root.addChild(b);
    root.addChild(c);
    
    for (LayoutManager layout : layouts) {
      root.setLayoutManager(layout);
      root.layoutElements();
      assertEquals(100, root.getConstraintHeight().getValueAsInt(0));
    }
  }
}
