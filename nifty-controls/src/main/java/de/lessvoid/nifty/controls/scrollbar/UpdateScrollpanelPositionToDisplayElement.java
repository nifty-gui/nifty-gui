package de.lessvoid.nifty.controls.scrollbar;


import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Scrollbar;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.EffectProperties;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.screen.Screen;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.logging.Logger;

public class UpdateScrollpanelPositionToDisplayElement implements EffectImpl {
  @Nonnull
  private final Logger log = Logger.getLogger(UpdateScrollpanelPositionToDisplayElement.class.getName());
  @Nullable
  private Element targetElement;

  @Override
  public void activate(
      @Nonnull final Nifty nifty,
      @Nonnull final Element elementParameter,
      @Nonnull final EffectProperties parameter) {
    String target = parameter.getProperty("target");
    if (target != null) {
      Screen screen = nifty.getCurrentScreen();
      if (screen == null) {
        log.severe("Can't activate the effect while there is no screen selected as current.");
      } else {
        targetElement = screen.findElementById(target);
      }
    }
  }

  @Override
  public void execute(
      @Nonnull final Element element,
      final float normalizedTime,
      @Nullable final Falloff falloff,
      @Nonnull final NiftyRenderEngine r) {
    if (targetElement != null) {
      Scrollbar verticalScrollbar = targetElement.findNiftyControl("#nifty-internal-vertical-scrollbar",
          Scrollbar.class);
      if (verticalScrollbar == null) {
        log.warning("Failed to locate required internal scrollbar.");
        return;
      }

      int minY = (int) verticalScrollbar.getValue();
      int maxY = (int) verticalScrollbar.getValue() + (int) verticalScrollbar.getWorldPageSize();

      int currentMinY = element.getY() - targetElement.getY() + (int) verticalScrollbar.getValue();
      int currentMaxY = element.getY() - targetElement.getY() + element.getHeight() + (int) verticalScrollbar
          .getValue();

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
