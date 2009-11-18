package de.lessvoid.nifty.effects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.lessvoid.xml.xpp3.Attributes;

public class EffectPropertiesValuesTest {
  private EffectPropertiesValues effectValues;

  @Before
  public void before() {
    effectValues = new EffectPropertiesValues();
  }

  @Test
  public void testEmpty() {
    assertTrue(effectValues.getValues().isEmpty());
  }

  @Test
  public void testAdd() {
    Attributes p = new Attributes();
    effectValues.add(p);

    List < Attributes > values = effectValues.getValues();
    assertEquals(1, values.size());
    assertEquals(p, values.iterator().next());
  }

  @Test
  public void testConvertToLinearInterpolatorEmpty() {
    assertNull(effectValues.toLinearInterpolator());
  }

  @Test
  public void testConvertToLinearInterpolator() {
    Attributes p = new Attributes();
    p.set("time", "2");
    p.set("value", "4");
    effectValues.add(p);
    assertNotNull(effectValues.toLinearInterpolator());
  }

  @Test
  public void testContainsNoTimeValues() {
    Attributes p = new Attributes();
    p.set("suff", "2");
    p.set("value", "4");
    effectValues.add(p);
    assertFalse(effectValues.containsTimeValues());
  }

  @Test
  public void testContainsTimeValues() {
    Attributes p = new Attributes();
    p.set("time", "2");
    p.set("value", "4");
    effectValues.add(p);
    assertTrue(effectValues.containsTimeValues());
  }
}
