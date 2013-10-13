package de.lessvoid.nifty.spi;

import de.lessvoid.nifty.api.NiftyColor;
import de.lessvoid.nifty.internal.math.Mat4;

public interface NiftyRenderTarget {

  int getWidth();
  int getHeight();

  /**
   * 
   * @param matrix
   */
  void setMatrix(Mat4 matrix);

  /**
   * Draw a rectangle with the given coordinates and fill it with the given colors.
   *
   * @param x0 x position of top left corner
   * @param y0 y position of top left corner
   * @param x1 x position of top right corner
   * @param y1 y position of top right corner
   * @param x2 x position of bottom right corner
   * @param y2 y position of bottom right corner
   * @param x3 x position of bottom left corner
   * @param y3 y position of bottom left corner
   * @param color the color to fill the rectangle with
   */
  void filledRect(double x0, double y0, double x1, double y1, double x2, double y2, double x3, double y3, NiftyColor color);

  void beginStencil();
  void markStencil(double x, double y, double width, double height);
  void endStencil();
  
  void enableStencil();
  void disableStencil();

  void save(final String filebase);
}
