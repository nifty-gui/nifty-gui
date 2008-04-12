package de.lessvoid.nifty.layout.manager;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import de.lessvoid.nifty.layout.Box;
import de.lessvoid.nifty.layout.BoxConstraints;
import de.lessvoid.nifty.layout.LayoutPart;
import de.lessvoid.nifty.tools.SizeValue;

public class AbsolutePositionLayoutTest extends TestCase {
  
  private AbsolutePositionLayout layout= new AbsolutePositionLayout();
  private LayoutPart rootPanel;
  private List<LayoutPart> elements;
  private LayoutPart element;
  
  protected void setUp() throws Exception {
    Box box = new Box( 0, 0, 640, 480 );
    BoxConstraints boxConstraint = new BoxConstraints();
    rootPanel= new LayoutPart( box, boxConstraint );

    box = new Box();
    boxConstraint = new BoxConstraints();
    element= new LayoutPart( box, boxConstraint );

    elements= new ArrayList<LayoutPart>();
    elements.add( element );
  }

  public void testUpdateEmpty() throws Exception {
    layout.layoutElements( null, null );
  }

  public void testUpdateWithNullEntriesMakeNoTrouble() {
    layout.layoutElements( rootPanel, null );
  }

  public void testLayoutFixedHeight() {
    element.getBoxConstraints().setHeight( new SizeValue( "20px" ));
    layout.layoutElements( rootPanel, elements );
    
    assertEquals( 0, element.getBox().getX() );
    assertEquals( 0, element.getBox().getY() );
    assertEquals( 0, element.getBox().getWidth() );
    assertEquals( 20, element.getBox().getHeight() );
  }

  public void testLayoutFixedWidth() {
    element.getBoxConstraints().setWidth( new SizeValue( "20px" ));
    layout.layoutElements( rootPanel, elements );
    
    assertEquals( 0, element.getBox().getX() );
    assertEquals( 0, element.getBox().getY() );
    assertEquals( 20, element.getBox().getWidth() );
    assertEquals( 0, element.getBox().getHeight() );
  }

  public void testLayoutFixedX() {
    element.getBoxConstraints().setX(new SizeValue("20px"));
    layout.layoutElements( rootPanel, elements );
    
    assertEquals( 20, element.getBox().getX() );
    assertEquals( 0, element.getBox().getY() );
    assertEquals( 0, element.getBox().getWidth() );
    assertEquals( 0, element.getBox().getHeight() );
  }

  public void testLayoutFixedY() {
    element.getBoxConstraints().setX( new SizeValue( "20px" ));
    layout.layoutElements( rootPanel, elements );
    
    assertEquals( 20, element.getBox().getX() );
    assertEquals( 0, element.getBox().getY() );
    assertEquals( 0, element.getBox().getWidth() );
    assertEquals( 0, element.getBox().getHeight() );
  }

  public void testLayoutWithPercentageWidth() throws Exception {
    element.getBoxConstraints().setWidth( new SizeValue( "25%" ));
    layout.layoutElements( rootPanel, elements );
    
    assertEquals( 160, element.getBox().getWidth() );
  }
}
