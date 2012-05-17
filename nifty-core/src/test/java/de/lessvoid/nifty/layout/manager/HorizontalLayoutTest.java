package de.lessvoid.nifty.layout.manager;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import de.lessvoid.nifty.layout.Box;
import de.lessvoid.nifty.layout.BoxConstraints;
import de.lessvoid.nifty.layout.LayoutPart;
import de.lessvoid.nifty.layout.align.VerticalAlign;
import de.lessvoid.nifty.tools.SizeValue;

import static de.lessvoid.nifty.layout.manager.BoxTestHelper.*;

public class HorizontalLayoutTest extends TestCase {
  private HorizontalLayout layout = new HorizontalLayout();
  private LayoutPart rootPanel;
  private List <LayoutPart> elements;
  private LayoutPart left;
  private LayoutPart right;
  
  protected void setUp() throws Exception {
    rootPanel= new LayoutPart(new Box(0, 0, 640, 480), new BoxConstraints());
    left = new LayoutPart(new Box(), new BoxConstraints());
    right = new LayoutPart(new Box(), new BoxConstraints());

    elements = new ArrayList <LayoutPart>();
    elements.add(left);
    elements.add(right);
  }

  public void testUpdateEmpty() throws Exception {
    layout.layoutElements(null, null);
  }

  public void testUpdateWithNullEntriesMakeNoTrouble() {
    layout.layoutElements(rootPanel, null);
  }

  public void testLayoutDefault() {
    layout.layoutElements(rootPanel, elements);

    assertBox(left.getBox(), 0, 0, 320, 480);
    assertBox(right.getBox(), 320, 0, 320, 480);
  }

  public void testLayoutFixedHeight() {
    left.getBoxConstraints().setHeight( new SizeValue( "20px" ));
    layout.layoutElements( rootPanel, elements );

    assertBox(left.getBox(), 0, 0, 320, 20);
    assertBox(right.getBox(), 320, 0, 320, 480);
  }

  public void testLayoutMaxHeight() {
    left.getBoxConstraints().setHeight(new SizeValue("100%"));
    layout.layoutElements( rootPanel, elements );

    assertBox(left.getBox(), 0, 0, 320, 480);
    assertBox(right.getBox(), 320, 0, 320, 480);
  }

  public void testLayoutMaxHeightWildcard() {
    left.getBoxConstraints().setHeight(new SizeValue("*"));
    layout.layoutElements( rootPanel, elements );

    assertBox(left.getBox(), 0, 0, 320, 480);
    assertBox(right.getBox(), 320, 0, 320, 480);
  }

  public void testLayoutFixedWidth() {
    left.getBoxConstraints().setWidth( new SizeValue( "20px" ));
    layout.layoutElements( rootPanel, elements );

    assertBox(left.getBox(), 0, 0, 20, 480);
  }

  public void testLayoutFixedWidthTopAlign() {
    left.getBoxConstraints().setWidth( new SizeValue( "20px" ));
    left.getBoxConstraints().setVerticalAlign(VerticalAlign.top);
    layout.layoutElements( rootPanel, elements );

    assertBox(left.getBox(), 0, 0, 20, 480);
  }

  public void testLayoutFixedHeightCenterAlign() {
    left.getBoxConstraints().setHeight( new SizeValue( "20px" ));
    left.getBoxConstraints().setVerticalAlign(VerticalAlign.center);
    layout.layoutElements( rootPanel, elements );

    assertBox(left.getBox(), 0, 230, 320, 20);
  }

  public void testLayoutWithPercentage() throws Exception {
    left.getBoxConstraints().setWidth( new SizeValue( "25%" ));
    right.getBoxConstraints().setWidth( new SizeValue( "75%" ));

    layout.layoutElements( rootPanel, elements );

    assertBox(left.getBox(), 0, 0, 160, 480);
    assertBox(right.getBox(), 160, 0, 480, 480);
  }

  public void testLayoutWithMixedFixedAndPercentage() throws Exception {
    left.getBoxConstraints().setWidth( new SizeValue( "40px" ));
    right.getBoxConstraints().setWidth( new SizeValue( "*" ));

    layout.layoutElements( rootPanel, elements );

    assertBox(left.getBox(), 0, 0, 40, 480);
    assertBox(right.getBox(), 40, 0, 600, 480);
  }

  public void testLayoutDefaultWithAllEqualPadding() {
    rootPanel.getBoxConstraints().setPadding(new SizeValue("10px"));
    layout.layoutElements(rootPanel, elements);

    assertBox(left.getBox(), 10, 10, 310, 460);
    assertBox(right.getBox(), 320, 10, 310, 460);
  }
}
