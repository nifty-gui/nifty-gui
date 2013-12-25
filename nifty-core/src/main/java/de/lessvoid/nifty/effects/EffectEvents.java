package de.lessvoid.nifty.effects;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyMethodInvoker;

import javax.annotation.Nonnull;
import java.util.Properties;

public class EffectEvents {
  private NiftyMethodInvoker onStartEffect;
  private NiftyMethodInvoker onEndEffect;

  public void init(final Nifty nifty, final Object[] controllers, @Nonnull final Properties parameter) {
    String onStartEffectString = parameter.getProperty("onStartEffect");
    if (onStartEffectString != null) {
      onStartEffect = new NiftyMethodInvoker(nifty, onStartEffectString, controllers);
    }
    String onEndEffectString = parameter.getProperty("onEndEffect");
    if (onEndEffectString != null) {
      onEndEffect = new NiftyMethodInvoker(nifty, onEndEffectString, controllers);
    }
  }

  public void onStartEffect(final Properties parameter) {
    if (onStartEffect != null) {
      onStartEffect.invoke(parameter);
    }
  }

  public void onEndEffect() {
    if (onEndEffect != null) {
      onEndEffect.invoke();
    }
  }
}
