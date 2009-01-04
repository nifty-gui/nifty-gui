package de.lessvoid.nifty.effects.impl;

import java.util.Properties;
import java.util.logging.Logger;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.scrollbar.VerticalScrollbar;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;

public class UpdateScrollpanelPositionToDisplayElement implements EffectImpl {
  private Logger log = Logger.getLogger(UpdateScrollpanelPositionToDisplayElement.class.getName());
  private Element targetElement;

  public void activate(final Nifty nifty, final Element elementParameter, final Properties parameter) {
    String target = parameter.getProperty("target");
    if (target != null) {
      targetElement = nifty.getCurrentScreen().findElementByName(target);
    }
  }

  public void execute(
      final Element element,
      final float normalizedTime,
      final Falloff falloff,
      final NiftyRenderEngine r) {
    if (targetElement != null) {
      VerticalScrollbar verticalScrollbar = targetElement.findControl("nifty-internal-vertical-scrollbar", VerticalScrollbar.class);

      int minY = (int)verticalScrollbar.getCurrentValue();
      int maxY = (int)verticalScrollbar.getCurrentValue() + (int)verticalScrollbar.getViewMaxValue();

      int currentMinY = element.getY() - targetElement.getY() + (int)verticalScrollbar.getCurrentValue();
      int currentMaxY = element.getY() - targetElement.getY() + element.getHeight() + (int)verticalScrollbar.getCurrentValue();

      // below?
      int delta = -1;
      if (currentMinY >= maxY || (currentMinY <= maxY && currentMaxY >= maxY)) {
        // scroll down
        delta = currentMaxY - maxY;
        verticalScrollbar.setCurrentValue(minY + delta);
      } else if (currentMaxY <= minY || (currentMinY <= minY && currentMaxY >= minY)) {
        // scroll up
        delta = minY - currentMinY;
        verticalScrollbar.setCurrentValue(minY - delta);
      }
      log.fine(minY + ":" + maxY + " - " + currentMinY + ":" + currentMaxY + " - " + delta);
    }
  }

  public void deactivate() {
  }
}
