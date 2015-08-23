package de.lessvoid.nifty.node;

import de.lessvoid.nifty.spi.node.NiftyNodeAccessor;

import de.lessvoid.niftyinternal.common.NiftyServiceLoader;

import org.junit.Test;

import java.util.ServiceLoader;

import static org.junit.Assert.assertEquals;

/**
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class FixedSizeLayoutNodeAccessorTest {
  @Test
  public void testPresenceInService() {
    int count = 0;
    for (NiftyNodeAccessor accessor : NiftyServiceLoader.load(NiftyNodeAccessor.class)) {
      if (accessor instanceof FixedSizeLayoutNodeAccessor) {
        count++;
      }
    }
    assertEquals("Expected to find FixedSizeLayoutNodeAccessor once in the service!", 1, count);
  }
}
