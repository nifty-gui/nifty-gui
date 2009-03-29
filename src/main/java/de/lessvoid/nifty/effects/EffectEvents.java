package de.lessvoid.nifty.effects;

import java.util.LinkedList;
import java.util.Properties;

import de.lessvoid.xml.tools.MethodInvoker;

public class EffectEvents {
  private MethodInvoker onStartEffect;
  private MethodInvoker onEndEffect;

  public void init(
      final LinkedList < Object > controllers,
      final Properties parameter) {
    String onStartEffectString = parameter.getProperty("onStartEffect");
    if (onStartEffectString != null) {
      onStartEffect = new MethodInvoker(onStartEffectString, controllers.toArray());
    }
    String onEndEffectString = parameter.getProperty("onEndEffect");
    if (onEndEffectString != null) {
      onEndEffect = new MethodInvoker(onEndEffectString, controllers.toArray());
    }
  }

  public void onEndEffect() {
    if (onEndEffect != null) {
      onEndEffect.invoke();
    }
  }

  public void onStartEffect(final Properties parameter) {
    if (onStartEffect != null) {
      onStartEffect.invoke(parameter);
    }
  }
}
