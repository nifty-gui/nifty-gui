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
  font;

  /**
   * Helper to get all available RenderStates as a set.
   * @return the set of all available render states.
   */
  public static Set < RenderStateType > allStates() {
    Set < RenderStateType > allStates = new HashSet < RenderStateType >();

    for (RenderStateType state : RenderStateType.values()) {
      allStates.add(state);
    }

    return allStates;
  }
}
