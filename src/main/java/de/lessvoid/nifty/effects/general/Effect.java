package de.lessvoid.nifty.effects.general;

import de.lessvoid.nifty.render.RenderEngine;

/**
 * Effect interface.
 * @author void
  */
public interface Effect {
    /**
     * start the effect.
     */
    void start();

    /**
     * update effect.
     */
    void update();

    /**
     * execute the effect.
     * @param r RenderDevice
     */
    void execute(final RenderEngine r);

    /**
     * is this effect still active?
     * @return active flag
     */
    boolean isActive();

    /**
     * change the effects active state.
     * @param newActive new active state
     */
    void setActive(final boolean newActive);

    /**
     * is this a post or pre effect.
     * @return true, when post or false, when pre effect
     */
    boolean isPost();

    /**
     * should this effect inherit to children.
     * @return true, when yes and false, when not
     */
    boolean isInherit();

    /**
     * the alternate key or null, when no alternate mode is used.
     * @return the alternateKey, might be null
     */
    String alternateKey();

    /**
     * when alternateKey is not null, this should return the alternate mode.
     * @return true, alternate enable mode, false, alternate disable mode
     */
    boolean isAlternateEnable();

    /**
     * get state string.
     * @return state string
     */
    String getStateString();
}
