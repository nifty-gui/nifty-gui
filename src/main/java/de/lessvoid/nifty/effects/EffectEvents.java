package de.lessvoid.nifty.effects;

import java.util.Properties;

import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.xml.tools.MethodInvoker;

public class EffectEvents {
  private MethodInvoker onEndEffect;

  public void init(final ScreenController screenController, final Properties parameter) {
    String onEndEffectString = parameter.getProperty("onEndEffect");
    if (onEndEffectString != null) {
      onEndEffect = new MethodInvoker(onEndEffectString, screenController);
    }
  }

  public void onEndEffect() {
    if (onEndEffect != null) {
      onEndEffect.invoke();
    }
  }
}
