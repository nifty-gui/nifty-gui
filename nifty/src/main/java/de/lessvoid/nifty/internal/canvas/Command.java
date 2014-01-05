package de.lessvoid.nifty.internal.canvas;

import de.lessvoid.nifty.spi.NiftyRenderDevice;



public interface Command {

  void execute(NiftyRenderDevice renderDevice, Context context);

}
