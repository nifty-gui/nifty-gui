package de.lessvoid.nifty.render.image.renderstrategy;

import de.lessvoid.nifty.layout.Box;
import de.lessvoid.nifty.spi.render.RenderDevice;
import de.lessvoid.nifty.spi.render.RenderImage;
import de.lessvoid.nifty.tools.Color;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RepeatStrategy implements RenderStrategy {

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
    int centerX = x + width / 2;
    int centerY = y + height / 2;

    int endX = x + width;
    int endY = y + height;

    int tileY = y;
    while (tileY < endY) {
      int tileHeight = Math.min(sourceArea.getHeight(), endY - tileY);

      int tileX = x;
      while (tileX < endX) {
        int tileWidth = Math.min(sourceArea.getWidth(), endX - tileX);

        renderDevice.renderImage(image, tileX, tileY, tileWidth, tileHeight, sourceArea.getX(),
            sourceArea.getY(), tileWidth, tileHeight, color, scale, centerX, centerY);

        tileX += tileWidth;
      }

      tileY += tileHeight;
    }
  }
}
