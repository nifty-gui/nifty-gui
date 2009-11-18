package de.lessvoid.nifty.effects;

import java.util.Properties;

import de.lessvoid.nifty.tools.LinearInterpolator;
import de.lessvoid.xml.xpp3.Attributes;

public class EffectProperties extends Properties {
  private static final long serialVersionUID = 1L;
  private EffectPropertiesValues effectValues = new EffectPropertiesValues();
  
  public EffectProperties(final Properties createProperties) {
    super(createProperties);
  }

  public void addEffectValue(final Attributes effectProperties) {
    effectValues.add(effectProperties);
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
