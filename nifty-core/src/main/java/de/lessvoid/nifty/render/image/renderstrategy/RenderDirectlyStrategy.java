package de.lessvoid.nifty.render.image.renderstrategy;

import de.lessvoid.nifty.layout.Box;
import de.lessvoid.nifty.spi.render.RenderDevice;
import de.lessvoid.nifty.spi.render.RenderImage;
import de.lessvoid.nifty.tools.Color;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RenderDirectlyStrategy implements RenderStrategy {

  @Override
  public void setParameters(@Nullable String parameters) {
    if (parameters != null) {
      throw new IllegalArgumentException("Trying to parse [" + this.getClass().getName()
          + "] : expected no parameters, found [" + parameters + "].");
    }
  }

  @Override
  public void render(
      @Nonnull final RenderDevice renderDevice,
      @Nonnull final RenderImage image,
      @Nonnull final Box sourceArea,
      final int x,
      final int y,
      final int width,
      final int height,
      @Nonnull final Color color,
      final float scale) {
    final int centerX = x + width / 2;
    final int centerY = y + height / 2;

    renderDevice.renderImage(
        image,
        sourceArea.getX() + x, sourceArea.getY() + y, sourceArea.getWidth(), sourceArea.getHeight(),
        sourceArea.getX(), sourceArea.getY(), sourceArea.getWidth(), sourceArea.getHeight(),
        color, scale, centerX, centerY);
  }
}
