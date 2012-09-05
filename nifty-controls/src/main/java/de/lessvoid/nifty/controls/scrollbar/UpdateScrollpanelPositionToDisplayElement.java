package de.lessvoid.nifty.controls.scrollbar;


import java.util.logging.Logger;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Scrollbar;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.EffectProperties;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;

public class UpdateScrollpanelPositionToDisplayElement implements EffectImpl {
  private Logger log = Logger.getLogger(UpdateScrollpanelPositionToDisplayElement.class.getName());
  private Element targetElement;

  @Override
  public void activate(final Nifty nifty, final Element elementParameter, final EffectProperties parameter) {
    String target = parameter.getProperty("target");
    if (target != null) {
      targetElement = nifty.getCurrentScreen().findElementByName(target);
    }
  }

  @Override
  public void execute(
      final Element element,
      final float normalizedTime,
      final Falloff falloff,
      final NiftyRenderEngine r) {
    if (targetElement != null) {
      Scrollbar verticalScrollbar = targetElement.findNiftyControl("#nifty-internal-vertical-scrollbar", Scrollbar.class);

      int minY = (int) verticalScrollbar.getValue();
      int maxY = (int) verticalScrollbar.getValue() + (int) verticalScrollbar.getWorldPageSize();

      int currentMinY = element.getY() - targetElement.getY() + (int) verticalScrollbar.getValue();
      int currentMaxY = element.getY() - targetElement.getY() + element.getHeight() + (int) verticalScrollbar.getValue();

      // below?
      int delta = -1;
      if (currentMinY >= maxY || (currentMinY <= maxY && currentMaxY >= maxY)) {
        // scroll down
        delta = currentMaxY - maxY;
        verticalScrollbar.setValue(minY + delta);
      } else if (currentMaxY <= minY || (currentMinY <= minY && currentMaxY >= minY)) {
        // scroll up
        delta = minY - currentMinY;
        verticalScrollbar.setValue(minY - delta);
      }
      log.fine(minY + ":" + maxY + " - " + currentMinY + ":" + currentMaxY + " - " + delta);
    }
  }

  @Override
  public void deactivate() {
  }
}
