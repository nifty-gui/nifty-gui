package de.lessvoid.nifty.spi.impl;

import de.lessvoid.nifty.spi.TimeProvider;

/**
 * This time provider uses a Java native implementation to get the current time.
 * This implementation is faster then
 * {@link de.lessvoid.nifty.spi.impl.time.impl.AccurateTimeProvider}, but less
 * accurate.
 * 
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class FastTimeProvider implements TimeProvider {
    /**
     * {@inheritDoc}
     */
    @Override
    public long getMsTime() {
        return System.currentTimeMillis();
    }
}
