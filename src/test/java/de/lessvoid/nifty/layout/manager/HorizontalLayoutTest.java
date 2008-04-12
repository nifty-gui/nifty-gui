package de.lessvoid.nifty.layout.manager;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import de.lessvoid.nifty.layout.Box;
import de.lessvoid.nifty.layout.BoxConstraints;
import de.lessvoid.nifty.layout.LayoutPart;
import de.lessvoid.nifty.layout.align.VerticalAlign;
import de.lessvoid.nifty.tools.SizeValue;

public class HorizontalLayoutTest extends TestCase {
  
  private HorizontalLayout layout= new HorizontalLayout();
  private LayoutPart rootPanel;
  private List<LayoutPart> elements;
  private LayoutPart left;
  private LayoutPart right;
  
  protected void setUp() throws Exception {
    Box box = new Box( 0, 0, 640, 480 );
    BoxConstraints boxConstraint = new BoxConstraints();
    rootPanel= new LayoutPart( box, boxConstraint );

    box = new Box();
    boxConstraint = new BoxConstraints();
    left= new LayoutPart( box, boxConstraint );

    box = new Box();
    boxConstraint = new BoxConstraints();
    right= new LayoutPart( box, boxConstraint );

    elements= new ArrayList<LayoutPart>();
    elements.add( left );
    elements.add( right );
  }

  public void testUpdateEmpty() throws Exception {
    layout.layoutElements( null, null );
  }

  public void testUpdateWithNullEntriesMakeNoTrouble() {
    layout.layoutElements( rootPanel, null );
  }

  public void testLayoutDefault() {
    layout.layoutElements( rootPanel, elements );
    
    assertEquals( 0, left.getBox().getX() );
    assertEquals( 0, left.getBox().getY() );
    assertEquals( 320, left.getBox().getWidth() );
    assertEquals( 480, left.getBox().getHeight() );

    assertEquals( 320, right.getBox().getX() );
    assertEquals( 0, right.getBox().getY() );
    assertEquals( 320, right.getBox().getWidth() );
    assertEquals( 480, right.getBox().getHeight() );
  }

  public void testLayoutFixedHeight() {
    left.getBoxConstraints().setHeight( new SizeValue( "20px" ));
    layout.layoutElements( rootPanel, elements );
    
    assertEquals( 0, left.getBox().getX() );
    assertEquals( 0, left.getBox().getY() );
    assertEquals( 320, left.getBox().getWidth() );
    assertEquals( 20, left.getBox().getHeight() );

    assertEquals( 320, right.getBox().getX() );
    assertEquals( 0, right.getBox().getY() );
    assertEquals( 320, right.getBox().getWidth() );
    assertEquals( 480, right.getBox().getHeight() );
  }

  public void testLayoutFixedWidth() {
    left.getBoxConstraints().setWidth( new SizeValue( "20px" ));
    layout.layoutElements( rootPanel, elements );
    
    assertEquals( 0, left.getBox().getX() );
    assertEquals( 0, left.getBox().getY() );
    assertEquals( 20, left.getBox().getWidth() );
    assertEquals( 480, left.getBox().getHeight() );
  }

  public void testLayoutFixedWidthTopAlign() {
    left.getBoxConstraints().setWidth( new SizeValue( "20px" ));
    left.getBoxConstraints().setVerticalAlign(VerticalAlign.top);
    layout.layoutElements( rootPanel, elements );
    
    assertEquals( 0, left.getBox().getX() );
    assertEquals( 0, left.getBox().getY() );
    assertEquals( 20, left.getBox().getWidth() );
    assertEquals( 480, left.getBox().getHeight() );
  }

  public void testLayoutFixedHeightCenterAlign() {
    left.getBoxConstraints().setHeight( new SizeValue( "20px" ));
    left.getBoxConstraints().setVerticalAlign(VerticalAlign.center);
    layout.layoutElements( rootPanel, elements );
    
    assertEquals( 0, left.getBox().getX() );
    assertEquals( 230, left.getBox().getY() );
    assertEquals( 320, left.getBox().getWidth() );
    assertEquals( 20, left.getBox().getHeight() );
  }

  public void testLayoutWithPercentage() throws Exception {
    left.getBoxConstraints().setWidth( new SizeValue( "25%" ));
    right.getBoxConstraints().setWidth( new SizeValue( "75%" ));

    layout.layoutElements( rootPanel, elements );
    
    assertEquals( 0, left.getBox().getX() );
    assertEquals( 160, left.getBox().getWidth() );

    assertEquals( 160, right.getBox().getX() );
    assertEquals( 480, right.getBox().getWidth() );
  }
}
