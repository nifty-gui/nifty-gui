package de.lessvoid.nifty.effects;

import java.util.List;

import de.lessvoid.nifty.effects.general.Effect;
import de.lessvoid.nifty.effects.hover.HoverEffect;
import de.lessvoid.nifty.elements.Element;

/**
 * An EffectProcessor handles a single effect type. You can have multiple
 * effects of the same type and all these effects are handled by a single
 * EffectProcessor instance.
 * @author void
 */
public class HoverEffectProcessor extends StandardEffectProcessor {

  /**
   * create and initialize a new instance.
   */
  public HoverEffectProcessor() {
    super(false);
  }

  /**
   * process hover effect.
   * @param element the element
   * @param x mouse x position
   * @param y mouse y position
   */
  public final void processHover(final Element element, final int x, final int y) {
    processHoverInternal(element, x, y, getEffects());
  }

  /**
   * process hover effects.
   * @param element the element
   * @param x mouse x position
   * @param y mouse y position
   * @param effectList the effect list to check
   */
  private void processHoverInternal(
      final Element element,
      final int x,
      final int y,
      final List < Effect > effectList) {
    for (Effect e : effectList) {
      // that's kinda crap but can't help it at the moment...
      if (e instanceof HoverEffect) {
        HoverEffect hoverEffect = (HoverEffect) e;
        if (!hoverEffect.isActive()) {
          if (hoverEffect.isInsideFalloff(x, y)) {
            startEffect(hoverEffect, null);
            setActive(true);
          }
        }

        if (hoverEffect.isActive()) {
          if (!hoverEffect.isInsideFalloff(x, y)) {
            hoverEffect.setActive(false);
            activeEffects.remove(hoverEffect);
          } else {
            hoverEffect.hoverDistance(x, y);
          }
        }
      }
    }
  }
}
