package de.lessvoid.nifty.layout.manager;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import de.lessvoid.nifty.layout.Box;
import de.lessvoid.nifty.layout.BoxConstraints;
import de.lessvoid.nifty.layout.LayoutPart;
import de.lessvoid.nifty.layout.align.HorizontalAlign;
import de.lessvoid.nifty.tools.SizeValue;

public class VerticalLayoutTest extends TestCase {
  
  private VerticalLayout layout= new VerticalLayout();
  private LayoutPart rootPanel;
  private List<LayoutPart> elements;
  private LayoutPart top;
  private LayoutPart bottom;
  
  protected void setUp() throws Exception {
    Box box = new Box( 0, 0, 640, 480 );
    BoxConstraints boxConstraint = new BoxConstraints();
    rootPanel= new LayoutPart( box, boxConstraint );

    box = new Box();
    boxConstraint = new BoxConstraints();
    top= new LayoutPart( box, boxConstraint );

    box = new Box();
    boxConstraint = new BoxConstraints();
    bottom= new LayoutPart( box, boxConstraint );

    elements= new ArrayList<LayoutPart>();
    elements.add( top );
    elements.add( bottom );
  }

  public void testUpdateEmpty() throws Exception {
    layout.layoutElements( null, null );
  }

  public void testUpdateWithNullEntriesMakeNoTrouble() {
    layout.layoutElements( rootPanel, null );
  }

  public void testLayoutDefault() {
    layout.layoutElements( rootPanel, elements );
    
    assertEquals( 0, top.getBox().getX() );
    assertEquals( 0, top.getBox().getY() );
    assertEquals( 640, top.getBox().getWidth() );
    assertEquals( 240, top.getBox().getHeight() );

    assertEquals( 0, bottom.getBox().getX() );
    assertEquals( 240, bottom.getBox().getY() );
    assertEquals( 640, bottom.getBox().getWidth() );
    assertEquals( 240, bottom.getBox().getHeight() );
  }

  public void testLayoutFixedHeight() {
    top.getBoxConstraints().setHeight( new SizeValue( "20px" ));
    layout.layoutElements( rootPanel, elements );
    
    assertEquals( 0, top.getBox().getX() );
    assertEquals( 0, top.getBox().getY() );
    assertEquals( 640, top.getBox().getWidth() );
    assertEquals( 20, top.getBox().getHeight() );

    assertEquals( 0, bottom.getBox().getX() );
    assertEquals( 20, bottom.getBox().getY() );
    assertEquals( 640, bottom.getBox().getWidth() );
    assertEquals( 460, bottom.getBox().getHeight() );
  }

  public void testLayoutFixedWidth() {
    top.getBoxConstraints().setWidth( new SizeValue( "20px" ));
    layout.layoutElements( rootPanel, elements );
    
    assertEquals( 0, top.getBox().getX() );
    assertEquals( 0, top.getBox().getY() );
    assertEquals( 20, top.getBox().getWidth() );
    assertEquals( 240, top.getBox().getHeight() );
  }

  public void testLayoutFixedWidthRightAlign() {
    top.getBoxConstraints().setWidth( new SizeValue( "20px" ));
    top.getBoxConstraints().setHorizontalAlign( HorizontalAlign.right );
    layout.layoutElements( rootPanel, elements );
    
    assertEquals( 620, top.getBox().getX() );
    assertEquals( 0, top.getBox().getY() );
    assertEquals( 20, top.getBox().getWidth() );
    assertEquals( 240, top.getBox().getHeight() );
  }

  public void testLayoutFixedWidthCenterAlign() {
    top.getBoxConstraints().setWidth( new SizeValue( "20px" ));
    top.getBoxConstraints().setHorizontalAlign( HorizontalAlign.center );
    layout.layoutElements( rootPanel, elements );
    
    assertEquals( 310, top.getBox().getX() );
    assertEquals( 0, top.getBox().getY() );
    assertEquals( 20, top.getBox().getWidth() );
    assertEquals( 240, top.getBox().getHeight() );
  }

  public void testLayoutWithPercentage() throws Exception {
    top.getBoxConstraints().setHeight( new SizeValue( "25%" ));
    bottom.getBoxConstraints().setHeight( new SizeValue( "75%" ));

    layout.layoutElements( rootPanel, elements );
    
    assertEquals( 0, top.getBox().getY() );
    assertEquals( 120, top.getBox().getHeight() );

    assertEquals( 120, bottom.getBox().getY() );
    assertEquals( 360, bottom.getBox().getHeight() );
  }
}
