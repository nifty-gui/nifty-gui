package de.lessvoid.nifty.slick2d.input.events;

import de.lessvoid.nifty.input.keyboard.KeyboardInputEvent;
import de.lessvoid.nifty.slick2d.input.InputState;

/**
 * This is the abstract keyboard event that stores the data all keyboard events
 * got in common.
 * 
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public abstract class AbstractKeyboardEvent extends KeyboardInputEvent
    implements InputEvent {
    /**
     * Create the keyboard event and store the ID of the key and the character.
     * 
     * @param keyId the ID of the key that was used
     * @param keyChar the character assigned to the used key
     * @param keyDown <code>true</code> in case the key is pressed down
     * @param shiftDown <code>true</code> in case shift is pressed down at the
     *            same time
     * @param controlDown <code>true</code> in case control is pressed down at
     *            the same time
     */
    protected AbstractKeyboardEvent(final int keyId, final char keyChar,
        final boolean keyDown, final boolean shiftDown,
        final boolean controlDown) {
        super(keyId, keyChar, keyDown, shiftDown, controlDown);
    }
    
    /**
     * {@inheritDoc}
     * Default implementation allows this event to be executed in any case.
     */
    @Override
    public boolean executeEvent(final InputState state) {
        return true;
    }
    
    /**
     * {@inheritDoc}
     * The default implementation never updates the state.
     */
    @Override
    public void updateState(final InputState state, final boolean handledByGUI) {
        // nothing to do
    }
}
