package de.lessvoid.nifty.render.image.renderstrategy;

import de.lessvoid.nifty.layout.Box;
import de.lessvoid.nifty.spi.render.RenderDevice;
import de.lessvoid.nifty.spi.render.RenderImage;
import de.lessvoid.nifty.tools.Color;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ClampStrategy implements RenderStrategy {

  @Override
  public void setParameters(@Nullable String parameters) {
    if (parameters != null) {
      throw new IllegalArgumentException("Trying to parse [" + this.getClass().getName()
          + "] : expected no parameters, found [" + parameters + "].");
    }
  }

  @Override
  public void render(
      @Nonnull RenderDevice renderDevice, @Nonnull RenderImage image, @Nonnull Box sourceArea, int x, int y, int width,
      int height, @Nonnull Color color, float scale) {
    final int centerX = x + width / 2;
    final int centerY = y + height / 2;

    final int srcW = sourceArea.getWidth();
    final int srcH = sourceArea.getHeight();

    renderDevice.renderImage(image, x, y, srcW, srcH, sourceArea.getX(), sourceArea.getY(), srcW, srcH, color,
        scale, centerX, centerY);
  }
}
