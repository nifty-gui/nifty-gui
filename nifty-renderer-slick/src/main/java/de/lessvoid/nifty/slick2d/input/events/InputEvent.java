package de.lessvoid.nifty.slick2d.input.events;

import org.newdawn.slick.InputListener;

import de.lessvoid.nifty.NiftyInputConsumer;

public interface InputEvent {
    /**
     * Send the event to a nifty input consumer.
     * 
     * @param consumer the consumer the event needs to be send to
     * @return <code>true</code> in case the event was handled by the consumer
     *         and must not be forwarded to any other event handlers
     */
    boolean sendToNifty(final NiftyInputConsumer consumer);

    /**
     * Send the event to a slick input consumer.
     * 
     * @param listener the input listener to receive this event
     * @return <code>true</code> in case the event was handled by the consumer
     *         and must not be forwarded to any other event handlers
     */
    boolean sendToSlick(final InputListener listener);
}
