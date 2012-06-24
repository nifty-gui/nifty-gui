package de.lessvoid.nifty.effects;

import java.util.Map;
import java.util.Properties;

import de.lessvoid.nifty.tools.LinearInterpolator;
import de.lessvoid.xml.xpp3.Attributes;

public class EffectProperties extends Properties {
  private static final long serialVersionUID = 1L;
  private EffectPropertiesValues effectValues;
  
  public EffectProperties(final Properties createProperties) {
    super();

    for (Map.Entry<Object, Object> entry : createProperties.entrySet()) {
      put(entry.getKey(), entry.getValue());
    }
  }

  public void addEffectValue(final Attributes effectProperties) {
    getEffectPropertiesValueLazy().add(effectProperties);
  }

  public EffectPropertiesValues getEffectValues() {
    return getEffectPropertiesValueLazy();
  }

  public boolean isTimeInterpolator() {
    return getEffectPropertiesValueLazy().containsTimeValues();
  }

  public LinearInterpolator getInterpolator() {
    if (effectValues == null) {
      return null;
    }
    LinearInterpolator interpolator = getEffectPropertiesValueLazy().toLinearInterpolator();
    if (interpolator == null) {
      return null;
    }
    interpolator.prepare();
    return interpolator;
  }

  private EffectPropertiesValues getEffectPropertiesValueLazy() {
    if (effectValues != null) {
      return effectValues;
    }
    effectValues = new EffectPropertiesValues();
    return effectValues;
  }
}
