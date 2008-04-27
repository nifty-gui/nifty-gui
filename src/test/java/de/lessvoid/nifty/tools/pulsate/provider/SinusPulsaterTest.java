package de.lessvoid.nifty.tools.pulsate.provider;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Properties;

import junit.framework.TestCase;

public class SinusPulsaterTest extends TestCase {

  public void testDefault() {
    SinusPulsater pulsater = createPulsater(false);
    assertEquals("0.0000", format(pulsater.getValue(0)));
    assertEquals("0.7071", format(pulsater.getValue(250)));
    assertEquals("1.0000", format(pulsater.getValue(500)));
    assertEquals("0.7071", format(pulsater.getValue(750)));
    assertEquals("0.0000", format(pulsater.getValue(1000)));
    assertEquals("0.7071", format(pulsater.getValue(1250)));
    assertEquals("1.0000", format(pulsater.getValue(1500)));
    assertEquals("0.7071", format(pulsater.getValue(1750)));
    assertEquals("0.0000", format(pulsater.getValue(2000)));
  }

  public void testOneShot() {
    SinusPulsater pulsater = createPulsater(true);
    assertEquals("0.0000", format(pulsater.getValue(0)));
    assertEquals("0.7071", format(pulsater.getValue(250)));
    assertEquals("1.0000", format(pulsater.getValue(500)));
    assertEquals("1.0000", format(pulsater.getValue(750)));
    assertEquals("1.0000", format(pulsater.getValue(1000)));
    assertEquals("1.0000", format(pulsater.getValue(1250)));
    assertEquals("1.0000", format(pulsater.getValue(1500)));
    assertEquals("1.0000", format(pulsater.getValue(1750)));
    assertEquals("1.0000", format(pulsater.getValue(2000)));
  }

  /**
   * @param oneShot TODO
   * @return
   */
  private SinusPulsater createPulsater(final boolean oneShot) {
    SinusPulsater pulsater = new SinusPulsater();
    Properties props = new Properties();
    if (oneShot) {
      props.put("cycle", "false");
    }
    pulsater.initialize(props);
    pulsater.reset(0);
    return pulsater;
  }
  
  private String format(final float f) {
    DecimalFormat df = new DecimalFormat("0.0000", new DecimalFormatSymbols(Locale.US));
    return df.format(f);
  }
}
