package de.lessvoid.nifty.render;

import java.util.HashSet;
import java.util.Set;

/**
 * states to save.
 * @author void
 */
public enum RenderStateType {

  /**
   * save position information.
   */
  position,

  /**
   * color.
   */
  color,

  /**
   * alpha.
   */
  alpha,

  /**
   * text size.
   */
  textSize,

  /**
   * image scale.
   */
  imageScale,

  /**
   * font.
   */
  font,

  /**
   * clip.
   */
  clip,

  /**
   * blend mode.
   */
  blendMode;

  /**
   * Helper to get all available RenderStates as a set.
   * @return the set of all available render states.
   */
  public static Set < RenderStateType > allStates() {
    return allStates;
  }

  private static Set < RenderStateType > allStates = new HashSet < RenderStateType >();

  static {
    for (RenderStateType state : RenderStateType.values()) {
      allStates.add(state);
    }
  }
}
