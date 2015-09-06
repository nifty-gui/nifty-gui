package de.lessvoid.nifty.examples.dummy;

import de.lessvoid.nifty.input.NiftyInputConsumer;
import de.lessvoid.nifty.spi.NiftyInputDevice;
import de.lessvoid.niftyinternal.NiftyResourceLoader;

import javax.annotation.Nonnull;

/**
 * This is the dummy input device implementation. It actually does nothing.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class DummyInputDevice implements NiftyInputDevice {
  @Override
  public void setResourceLoader(@Nonnull NiftyResourceLoader niftyResourceLoader) {
  }

  @Override
  public void forwardEvents(@Nonnull NiftyInputConsumer inputEventConsumer) {
  }

  @Override
  public void setMousePosition(int x, int y) {
  }
}
