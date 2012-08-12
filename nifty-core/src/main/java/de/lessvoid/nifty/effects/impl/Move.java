package de.lessvoid.nifty.effects.impl;

import java.util.logging.Logger;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.EffectProperties;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.tools.TargetElementResolver;

/**
 * Move - move stuff around.
 * @author void
 */
public class Move implements EffectImpl {

  private static Logger log = Logger.getLogger(Move.class.getName());

  private static final String LEFT = "left";
  private static final String RIGHT = "right";
  private static final String TOP = "top";
  private static final String BOTTOM = "bottom";

  private String direction;
  private long offset = 0;
  private long startOffset = 0;
  private int offsetDir = 0;
  private float offsetY;
  private float startOffsetY;
  private int startOffsetX;
  private float offsetX;
  private boolean withTarget = false;
  private boolean fromOffset = false;
  private boolean toOffset = false;

  public void activate(final Nifty nifty, final Element element, final EffectProperties parameter) {
    String mode = parameter.getProperty("mode");
    direction = parameter.getProperty("direction");
    if (LEFT.equals(direction)) {
      offset = element.getX() + element.getWidth();
    } else if (RIGHT.equals(direction)) {
      offset = nifty.getRenderEngine().getWidth() - element.getX();
    } else if (TOP.equals(direction)) {
      offset = element.getY() + element.getHeight();
    } else if (BOTTOM.equals(direction)) {
      offset = nifty.getRenderEngine().getHeight() - element.getY();
    } else {
      offset = 0;
    }

    if ("out".equals(mode)) {
      startOffset = 0;
      offsetDir = -1;
      withTarget = false;
    } else if ("in".equals(mode)) {
      startOffset = offset;
      offsetDir = 1;
      withTarget = false;
    } else if ("fromPosition".equals(mode)) {
      withTarget = true;
    } else if ("toPosition".equals(mode)) {
      withTarget = true;
    } else if ("fromOffset".equals(mode)) {
      fromOffset = true;
      startOffsetX = Integer.valueOf(parameter.getProperty("offsetX", "0"));
      startOffsetY = Integer.valueOf(parameter.getProperty("offsetY", "0"));
      offsetX = startOffsetX * -1;
      offsetY = startOffsetY * -1;
    } else if ("toOffset".equals(mode)) {
      toOffset  = true;
      startOffsetX = 0;
      startOffsetY = 0;
      offsetX = Integer.valueOf(parameter.getProperty("offsetX", "0"));
      offsetY = Integer.valueOf(parameter.getProperty("offsetY", "0"));
    }

    String target = parameter.getProperty("targetElement");
    if (target != null) {
      TargetElementResolver resolver = new TargetElementResolver(nifty.getCurrentScreen(), element);
      Element targetElement = resolver.resolve(target);
      if (targetElement == null) {
        log.warning("move effect for element [" + element.getId() + "] was unable to find target element [" + target + "] at screen [" + nifty.getCurrentScreen().getScreenId() + "]");
        return;
      }

      if ("fromPosition".equals(mode)) {
        startOffsetX = targetElement.getX() - element.getX();
        startOffsetY = targetElement.getY() - element.getY();
        offsetX = -(targetElement.getX() - element.getX());
        offsetY = -(targetElement.getY() - element.getY());
      } else if ("toPosition".equals(mode)) {
        startOffsetX = 0;
        startOffsetY = 0;
        offsetX = (targetElement.getX() - element.getX());
        offsetY = (targetElement.getY() - element.getY());
      }
    }
  }

  public void execute(
      final Element element,
      final float normalizedTime,
      final Falloff falloff,
      final NiftyRenderEngine r) {
    if (fromOffset || toOffset) {
      float moveToX = startOffsetX + normalizedTime * offsetX;
      float moveToY = startOffsetY + normalizedTime * offsetY;
      r.moveTo(moveToX, moveToY);
    } else if (withTarget) {
      float moveToX = startOffsetX + normalizedTime * offsetX;
      float moveToY = startOffsetY + normalizedTime * offsetY;
      r.moveTo(moveToX, moveToY);
    } else {
      if (LEFT.equals(direction)) {
        r.moveTo(-startOffset + offsetDir * normalizedTime * offset, 0);
      } else if (RIGHT.equals(direction)) {
        r.moveTo(startOffset - offsetDir * normalizedTime * offset, 0);
      } else if (TOP.equals(direction)) {
        r.moveTo(0, -startOffset + offsetDir * normalizedTime * offset);
      } else if (BOTTOM.equals(direction)) {
        r.moveTo(0, startOffset - offsetDir * normalizedTime * offset);
      }
    }
  }

  public void deactivate() {
  }
}
