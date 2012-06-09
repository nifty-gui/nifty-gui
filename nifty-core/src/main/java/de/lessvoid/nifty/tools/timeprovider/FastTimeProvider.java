package de.lessvoid.nifty.tools.timeprovider;

import de.lessvoid.nifty.spi.time.TimeProvider;

/**
 * This time provider uses a Java native implementation to get the current time.
 * This implementation is faster then
 * {@link de.lessvoid.nifty.spi.time.impl.AccurateTimeProvider}, but less
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
