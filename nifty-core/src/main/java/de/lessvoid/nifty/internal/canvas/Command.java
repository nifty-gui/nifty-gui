package de.lessvoid.nifty.internal.canvas;


import de.lessvoid.nifty.spi.NiftyRenderTarget;

public interface Command {

  void execute(NiftyRenderTarget renderTarget, Context context);

}
