package de.lessvoid.nifty.spi;

import de.lessvoid.nifty.api.NiftyColor;
import de.lessvoid.nifty.internal.math.Mat4;

public interface NiftyRenderTarget {

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

}
