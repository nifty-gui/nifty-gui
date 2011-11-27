package de.lessvoid.nifty.nulldevice;

import de.lessvoid.nifty.NiftyInputConsumer;
import de.lessvoid.nifty.spi.input.InputSystem;
import de.lessvoid.nifty.tools.resourceloader.NiftyResourceLoader;

public class NullInputSystem implements InputSystem {
  @Override
  public void setResourceLoader(NiftyResourceLoader niftyResourceLoader) {
  }

  @Override
  public void forwardEvents(NiftyInputConsumer inputEventConsumer) {
  }

  @Override
  public void setMousePosition(int x, int y) {
  }
}
