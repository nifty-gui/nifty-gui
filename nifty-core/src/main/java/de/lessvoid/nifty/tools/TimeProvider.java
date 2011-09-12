package de.lessvoid.nifty.tools;

/**
 * provides the current time :).
 * @author void
 */
public class TimeProvider {

    /**
     * convert factor for conversion from nano to ms time.
     */
    private static final int NANO_TO_MS_CONVERSION = 1000000;

    /**
     * get current time in ms.
     * @return current time in ms
     */
    public long getMsTime() {
        return System.nanoTime() / NANO_TO_MS_CONVERSION;
    }
}
