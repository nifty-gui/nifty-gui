package de.lessvoid.nifty.loaderv2.types;

import static org.junit.Assert.*;

import java.util.Properties;

import org.junit.Test;

import de.lessvoid.nifty.effects.EffectProperties;
import de.lessvoid.nifty.tools.LinearInterpolator;
import de.lessvoid.xml.xpp3.Attributes;

public class EffectTypeWithEffectValueTest {
  private EffectType effectType = new EffectType();
  private EffectProperties effectProperties = new EffectProperties(
      new Properties());
  
  @Test
  public void testNoEffectValues() {
    effectType.applyEffectValues(effectProperties);
    assertNull(effectProperties.getInterpolator());
  }

  @Test
  public void testEffectValues() {
    effectType.addValue(createEffectValueType("0", "0"));
    effectType.addValue(createEffectValueType("100", "1.0"));
    effectType.applyEffectValues(effectProperties);
    assertEquals("100", effectProperties.getProperty("length"));
    LinearInterpolator interpolator = effectProperties.getInterpolator();
    assertEquals(0.0f, interpolator.getValue(0.0f));
    assertEquals(1.0f, interpolator.getValue(1.0f));
  }

  private EffectValueType createEffectValueType(final String time, final String value) {
    EffectValueType effectValueType = new EffectValueType();
    Attributes attributes = new Attributes();
    attributes.set("time", time);
    attributes.set("value", value);
    effectValueType.initFromAttributes(attributes);
    return effectValueType;
  }
}
