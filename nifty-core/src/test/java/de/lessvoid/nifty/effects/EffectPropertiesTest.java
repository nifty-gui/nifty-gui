package de.lessvoid.nifty.effects;

import de.lessvoid.xml.xpp3.Attributes;
import org.junit.Before;
import org.junit.Test;

import java.util.Properties;

import static org.junit.Assert.*;


public class EffectPropertiesTest {
  private EffectProperties effectProperties;

  @Before
  public void before() {
    effectProperties = new EffectProperties(new Properties());
  }

  @Test
  public void testEmpty() {
    assertNull(effectProperties.getInterpolator());
    assertFalse(effectProperties.isTimeInterpolator());
  }

  @Test
  public void testAddWithTime() {
    Attributes p = new Attributes();
    p.set("time", "1");
    p.set("value", "2");
    effectProperties.addEffectValue(p);
    assertTrue(effectProperties.isTimeInterpolator());
    assertNotNull(effectProperties.getInterpolator());
  }

  @Test
  public void testAddWithoutTime() {
    Attributes p = new Attributes();
    p.set("t", "1");
    p.set("value", "2");
    effectProperties.addEffectValue(p);
    assertFalse(effectProperties.isTimeInterpolator());
    assertNull(effectProperties.getInterpolator());
  }
}
