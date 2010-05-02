package de.lessvoid.nifty.render;

import de.lessvoid.nifty.spi.render.RenderDevice;
import de.lessvoid.nifty.spi.render.RenderImage;

public class NiftyImageManager {
  private RenderDevice renderDevice;

  public NiftyImageManager(final RenderDevice renderDevice) {
    this.renderDevice = renderDevice;
  }

  public RenderImage getImage(String filename, boolean filterLinear) {
//    System.out.println("register image: " + filename + " with filter: " + filterLinear);
    return renderDevice.createImage(filename, filterLinear);
  }  
}
