package de.lessvoid.nifty.effects;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import de.lessvoid.xml.xpp3.Attributes;


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
