package de.lessvoid.nifty.spi.impl;

import de.lessvoid.nifty.spi.TimeProvider;

/**
 * This time provider uses a Java native implementation to get the current time.
 * This implementation is more accurate then
 * {@link de.lessvoid.nifty.spi.impl.time.impl.FastTimeProvider}, but on most systems
 * slower.
 * 
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class AccurateTimeProvider implements TimeProvider {
    /**
     * Conversation factor from nanoseconds time to milliseconds time.
     */
    private static final long NANO_TO_MS_CONVERSION = 1000000L;

    /**
     * {@inheritDoc}
     */
    @Override
    public long getMsTime() {
        return System.nanoTime() / NANO_TO_MS_CONVERSION;
    }
}
