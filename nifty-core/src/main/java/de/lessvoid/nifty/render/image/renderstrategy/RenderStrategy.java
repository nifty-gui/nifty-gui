package de.lessvoid.nifty.render.image.renderstrategy;

import de.lessvoid.nifty.Parameterizable;
import de.lessvoid.nifty.layout.Box;
import de.lessvoid.nifty.spi.render.RenderDevice;
import de.lessvoid.nifty.spi.render.RenderImage;
import de.lessvoid.nifty.tools.Color;

import javax.annotation.Nonnull;

public interface RenderStrategy extends Parameterizable {
  void render(
      @Nonnull RenderDevice renderDevice,
      @Nonnull RenderImage image,
      @Nonnull Box sourceArea,
      int x,
      int y,
      int width,
      int height,
      @Nonnull Color color,
      float scale);
}
