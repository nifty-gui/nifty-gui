package de.lessvoid.nifty.nulldevice;

import de.lessvoid.nifty.NiftyInputConsumer;
import de.lessvoid.nifty.spi.input.InputSystem;

public class NullInputSystem implements InputSystem {
  public void forwardEvents(NiftyInputConsumer inputEventConsumer) {
  }

  @Override
  public void setMousePosition(int x, int y) {
  }
}
