package de.lessvoid.nifty.spi;

import java.io.IOException;

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
   * 
   * @param x0
   * @param y0
   * @param x1
   * @param y1
   * @param randomColor
   */
  void filledRect(double x0, double y0, double x1, double y1, NiftyColor randomColor);

  void beginStencil();
  void markStencil(double x, double y, double width, double height);
  void endStencil();
  
  void enableStencil();
  void disableStencil();

  void save(final String filebase);
}
