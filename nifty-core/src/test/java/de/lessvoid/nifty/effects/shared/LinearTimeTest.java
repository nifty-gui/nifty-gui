package de.lessvoid.nifty.effects.shared;

import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.expect;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;

import java.util.Properties;

import junit.framework.TestCase;
import de.lessvoid.nifty.spi.time.TimeProvider;
import de.lessvoid.nifty.tools.time.TimeInterpolator;

public class LinearTimeTest extends TestCase {

  private TimeProvider timeProvider;

  public void setUp() {
    timeProvider = createMock(TimeProvider.class);
  }

  public void tearDown() {
    verify(timeProvider);
  }

  public void testDefault() throws Exception {
    expect(timeProvider.getMsTime()).andReturn(0l);
    expect(timeProvider.getMsTime()).andReturn(500l);
    expect(timeProvider.getMsTime()).andReturn(1000l);
    expect(timeProvider.getMsTime()).andReturn(2000l);
    replay(timeProvider);

    TimeInterpolator linearTime = new TimeInterpolator(new Properties(), timeProvider, false);
    linearTime.start();
    assertEquals(0.0f, linearTime.getValue());

    linearTime.update();
    assertEquals(0.5f, linearTime.getValue());

    linearTime.update();
    assertEquals(1.0f, linearTime.getValue());

    linearTime.update();
    assertEquals(1.0f, linearTime.getValue());
  }

  public void testParameter() throws Exception {
    expect(timeProvider.getMsTime()).andReturn(0l);
    expect(timeProvider.getMsTime()).andReturn(500l);
    expect(timeProvider.getMsTime()).andReturn(1000l);
    expect(timeProvider.getMsTime()).andReturn(1100l);
    expect(timeProvider.getMsTime()).andReturn(1200l);
    expect(timeProvider.getMsTime()).andReturn(2000l);
    replay(timeProvider);

    Properties prop = new Properties();
    prop.setProperty("length", "200");
    prop.setProperty("startDelay", "1000");

    TimeInterpolator linearTime = new TimeInterpolator(prop, timeProvider, false);
    linearTime.start();
    assertEquals(0.0f, linearTime.getValue());

    linearTime.update();
    assertEquals(0.0f, linearTime.getValue());

    linearTime.update();
    assertEquals(0.0f, linearTime.getValue());

    linearTime.update();
    assertEquals(0.5f, linearTime.getValue());

    linearTime.update();
    assertEquals(1.0f, linearTime.getValue());

    linearTime.update();
    assertEquals( 1.0f, linearTime.getValue());
  }
}
