package de.lessvoid.nifty.tools;

import de.lessvoid.nifty.spi.time.impl.AccurateTimeProvider;

/**
 * provides the current time :).
 * 
 * @author void
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 * @deprecated This implementation only exists for legacy reasons and is likely
 *             to be dropped in future versions. Better use the implementations
 *             in the package {@link de.lessvoid.nifty.spi.time.impl}
 */
@Deprecated
public class TimeProvider extends AccurateTimeProvider {
}
