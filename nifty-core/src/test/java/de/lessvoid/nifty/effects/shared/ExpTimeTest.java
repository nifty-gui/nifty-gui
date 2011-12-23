package de.lessvoid.nifty.effects.shared;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;

import java.util.Properties;

import de.lessvoid.nifty.spi.time.TimeProvider;
import de.lessvoid.nifty.tools.time.TimeInterpolator;
import junit.framework.TestCase;

public class ExpTimeTest extends TestCase {
  private TimeProvider timeProvider;

  public void setUp() {
    timeProvider= createMock( TimeProvider.class );
  }

  public void tearDown() {
    verify( timeProvider );
  }
  
  public void testParameterTwo() throws Exception {
    expect( timeProvider.getMsTime()).andReturn( 0l );
    expect( timeProvider.getMsTime()).andReturn( 500l );
    expect( timeProvider.getMsTime()).andReturn( 1000l );
    expect( timeProvider.getMsTime()).andReturn( 1100l );
    expect( timeProvider.getMsTime()).andReturn( 1200l );
    expect( timeProvider.getMsTime()).andReturn( 2000l );
    replay( timeProvider );
    
    Properties prop= new Properties();
    prop.setProperty( "timeType", "exp" );
    prop.setProperty( "length", "200" );
    prop.setProperty( "startDelay", "1000" );
    prop.setProperty( "factor", "2" );
    
    TimeInterpolator time= new TimeInterpolator(prop, timeProvider, false);
    time.start();
    assertEquals( 0.0f, time.getValue());
    
    time.update();
    assertEquals( 0.0f, time.getValue());

    time.update();
    assertEquals( 0.0f, time.getValue());

    time.update();
    assertEquals( 0.25f, time.getValue());

    time.update();
    assertEquals( 1.0f, time.getValue());

    time.update();
    assertEquals( 1.0f, time.getValue());
  }

  public void testParameter0point5() throws Exception {
    expect( timeProvider.getMsTime()).andReturn( 0l );
    expect( timeProvider.getMsTime()).andReturn( 500l );
    expect( timeProvider.getMsTime()).andReturn( 1000l );
    expect( timeProvider.getMsTime()).andReturn( 1100l );
    expect( timeProvider.getMsTime()).andReturn( 1200l );
    expect( timeProvider.getMsTime()).andReturn( 2000l );
    replay( timeProvider );
    
    Properties prop= new Properties();
    prop.setProperty( "timeType", "exp" );
    prop.setProperty( "length", "200" );
    prop.setProperty( "startDelay", "1000" );
    prop.setProperty( "factor", "0.5" );

    TimeInterpolator time = new TimeInterpolator(prop, timeProvider, false);
    time.start();
    assertEquals( 0.0f, time.getValue());

    time.update();
    assertEquals( 0.0f, time.getValue());

    time.update();
    assertEquals( 0.0f, time.getValue());

    time.update();
    assertEquals( 0.70710677f, time.getValue());

    time.update();
    assertEquals( 1.0f, time.getValue());

    time.update();
    assertEquals( 1.0f, time.getValue());
  }

}
