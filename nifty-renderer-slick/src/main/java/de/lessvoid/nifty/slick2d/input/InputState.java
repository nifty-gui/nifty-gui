package de.lessvoid.nifty.slick2d.input;

/**
 * This small class is able to store some states that are needed for the
 * communication between different input events.
 * 
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public final class InputState {
    /**
     * In case this state is switched to <code>true</code> the next click events
     * needs to be consumed entirely.
     */
    private boolean consumeNextClick = false;

    /**
     * Check if the next click event needs to be consumed.
     * 
     * @return <code>true</code> if the next click event needs to be consumed
     */
    public boolean isConsumeNextClick() {
        return consumeNextClick;
    }

    /**
     * Set the consume next input event flag.
     * 
     * @param consumeNextClick <code>true</code> in case the next input event is
     *            supposed to be consumed
     */
    public void setConsumeNextClick(boolean consumeNextClick) {
        this.consumeNextClick = consumeNextClick;
    }
}
