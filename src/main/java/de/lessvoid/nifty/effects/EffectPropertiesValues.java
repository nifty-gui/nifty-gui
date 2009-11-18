package de.lessvoid.nifty.effects;

import java.util.ArrayList;
import java.util.List;

import de.lessvoid.nifty.tools.LinearInterpolator;
import de.lessvoid.xml.xpp3.Attributes;

public class EffectPropertiesValues {
  private List < Attributes > values = new ArrayList < Attributes > ();

  public void add(final Attributes p) {
    values.add(p);
  }

  public List < Attributes > getValues() {
    return values;
  }

  public LinearInterpolator toLinearInterpolator() {
    if (values.isEmpty()) {
      return null;
    }
    if (!containsTimeValues()) {
      return null;
    }
    LinearInterpolator interpolator = new LinearInterpolator();
    for (Attributes p : values) {
        interpolator.addPoint(p.getAsFloat("time"), p.getAsFloat("value"));
      }
    return interpolator;
  }

  public boolean containsTimeValues() {
    if (values.isEmpty()) {
      return false;
    }
    for (Attributes p : values) {
      if (p.isSet("time")) {
        return true;
      }
    }
    return false;
  }      
}
