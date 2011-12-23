package de.lessvoid.nifty.spi.time;

/**
 * The time provider interface provides Nifty with the current time as the name
 * suggests.
 * 
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public interface TimeProvider {
    /**
     * Get the current time in milliseconds.
     * 
     * @return the current time in milliseconds
     */
    long getMsTime();
}
