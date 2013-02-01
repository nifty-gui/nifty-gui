package de.lessvoid.nifty.batch;

import de.lessvoid.nifty.spi.render.RenderImage;

/**
 * This only really carries the x and y position of the image in the texture atlas as well as the width and height of
 * the image.
 * @author void
 */
public class BatchRenderImage implements RenderImage {
  private final int x;
  private final int y;
  private final int width;
  private final int height;

  public BatchRenderImage(final int x, final int y, final int width, final int height) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public void dispose() {
  }  

  public int getX() {
    return x;
  }
  
  public int getY() {
    return y;
  }
}