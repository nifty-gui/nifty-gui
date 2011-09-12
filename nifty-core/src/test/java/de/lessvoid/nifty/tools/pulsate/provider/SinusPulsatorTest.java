package de.lessvoid.nifty.tools.pulsate.provider;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Properties;

import junit.framework.TestCase;

public class SinusPulsatorTest extends TestCase {

  public void testDefault() {
    SinusPulsator pulsator = createPulsater(false);
    assertEquals("0.0000", format(pulsator.getValue(0)));
    assertEquals("0.7071", format(pulsator.getValue(250)));
    assertEquals("1.0000", format(pulsator.getValue(500)));
    assertEquals("0.7071", format(pulsator.getValue(750)));
    assertEquals("0.0000", format(pulsator.getValue(1000)));
    assertEquals("0.7071", format(pulsator.getValue(1250)));
    assertEquals("1.0000", format(pulsator.getValue(1500)));
    assertEquals("0.7071", format(pulsator.getValue(1750)));
    assertEquals("0.0000", format(pulsator.getValue(2000)));
  }

  public void testOneShot() {
    SinusPulsator pulsator = createPulsater(true);
    assertEquals("0.0000", format(pulsator.getValue(0)));
    assertEquals("0.7071", format(pulsator.getValue(250)));
    assertEquals("1.0000", format(pulsator.getValue(500)));
    assertEquals("1.0000", format(pulsator.getValue(750)));
    assertEquals("1.0000", format(pulsator.getValue(1000)));
    assertEquals("1.0000", format(pulsator.getValue(1250)));
    assertEquals("1.0000", format(pulsator.getValue(1500)));
    assertEquals("1.0000", format(pulsator.getValue(1750)));
    assertEquals("1.0000", format(pulsator.getValue(2000)));
  }

  /**
   * @param oneShot TODO
   * @return
   */
  private SinusPulsator createPulsater(final boolean oneShot) {
    SinusPulsator pulsator = new SinusPulsator();
    Properties props = new Properties();
    if (oneShot) {
      props.put("cycle", "false");
    }
    pulsator.initialize(props);
    pulsator.reset(0);
    return pulsator;
  }
  
  private String format(final float f) {
    DecimalFormat df = new DecimalFormat("0.0000", new DecimalFormatSymbols(Locale.US));
    return df.format(f);
  }
}
