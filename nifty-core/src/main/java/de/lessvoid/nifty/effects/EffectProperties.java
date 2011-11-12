package de.lessvoid.nifty.effects;

import java.util.Map;
import java.util.Properties;

import de.lessvoid.nifty.tools.LinearInterpolator;
import de.lessvoid.xml.xpp3.Attributes;

public class EffectProperties extends Properties {
  private static final long serialVersionUID = 1L;
  private EffectPropertiesValues effectValues = new EffectPropertiesValues();
  
  public EffectProperties(final Properties createProperties) {
    super();

    for (Map.Entry<Object, Object> entry : createProperties.entrySet()) {
      put(entry.getKey(), entry.getValue());
    }
  }

  public void addEffectValue(final Attributes effectProperties) {
    effectValues.add(effectProperties);
  }

  public EffectPropertiesValues getEffectValues() {
    return effectValues;
  }

  public boolean isTimeInterpolator() {
    return effectValues.containsTimeValues();
  }

  public LinearInterpolator getInterpolator() {
    LinearInterpolator interpolator = effectValues.toLinearInterpolator();
    if (interpolator == null) {
      return null;
    }
    interpolator.prepare();
    return interpolator;
  }
}
