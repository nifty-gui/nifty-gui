package de.lessvoid.nifty.render;

import java.util.HashSet;
import java.util.Set;

/**
 * states to save.
 * @author void
 */
public enum RenderState {

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
   * font color.
   */
  fontColor,

  /**
   * image scale.
   */
  imageScale,

  /**
   * current state.
   */
  currentState;


  /**
   * Helper to get all available RenderStates as a set.
   * @return the set of all available render states.
   */
  public static Set < RenderState > allStates() {
    Set < RenderState > allStates = new HashSet < RenderState >();

    for (RenderState state : RenderState.values()) {
      allStates.add(state);
    }

    return allStates;
  }
}
