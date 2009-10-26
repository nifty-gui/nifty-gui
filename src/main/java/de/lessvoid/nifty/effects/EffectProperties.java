package de.lessvoid.nifty.effects;

import java.util.Properties;

import de.lessvoid.nifty.tools.LinearInterpolator;

public class EffectProperties extends Properties {
  private static final long serialVersionUID = 1L;
  private LinearInterpolator interpolator;
  
  public EffectProperties(final Properties createProperties) {
    super(createProperties);
  }
  
  public void setInterpolator(final LinearInterpolator interpolator) {
    this.interpolator = interpolator;
  }
  
  public LinearInterpolator getInterpolator() {
    return interpolator;
  }
}
