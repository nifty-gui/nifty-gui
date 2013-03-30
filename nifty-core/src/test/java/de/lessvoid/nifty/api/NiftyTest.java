package de.lessvoid.nifty.api;

import org.junit.Test;

import de.lessvoid.nifty.spi.NiftyRenderDevice;

public class NiftyTest {
  private Nifty nifty = new Nifty(new NiftyRenderDevice() {
    @Override
    public int getWidth() {
      return 0;
    }

    @Override
    public int getHeight() {
      return 0;
    }
  });
}
