package de.lessvoid.nifty.internal.canvas;


import de.lessvoid.nifty.spi.NiftyTexture;

public interface Command {

  void execute(NiftyTexture renderTarget, Context context);

}
