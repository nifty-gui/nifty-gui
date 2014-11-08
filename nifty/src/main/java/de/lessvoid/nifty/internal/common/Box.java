package de.lessvoid.nifty.internal.common;

/**
 * The Box class represent a rectangular area on the screen. It has a position (x,y) as well as height and weight
 * attributes. The Box represent an already resolved position in pixels.
 *
 * @author void
 */
public class Box {

  // Horizontal Position of the box.
  private int x;

  // Vertical Position of the box.
  private int y;

  // Width of the box.
  private int width;

  // Height of the box.
  private int height;

  // this will be set to true whenever x, y, width or height changed
  private boolean transformationChanged = true;

  /**
   * Create a new Box with some default coordinates (x,y) set to (0,0) and with width and height set to 0.
   */
  public Box() {
    this.x = 0;
    this.y = 0;
    this.width = 0;
    this.height = 0;
  }

  /**
   * copy constructor.
   * 
   * @param src
   *          src box to copy from
   */
  public Box(final Box src) {
    this.x = src.x;
    this.y = src.y;
    this.width = src.width;
    this.height = src.height;
  }

  /**
   * Create a new Box with the given coordinates.
   * 
   * @param newX
   *          the x position of the box
   * @param newY
   *          the y position of the box
   * @param newWidth
   *          the new width of the box
   * @param newHeight
   *          the new height of the box
   */
  public Box(final int newX, final int newY, final int newWidth, final int newHeight) {
    this.x = newX;
    this.y = newY;
    this.width = newWidth;
    this.height = newHeight;
  }

  /**
   * Copy all the Box data from the given parameter to this without creating a new instance.
   * @param src the source box to copy data from
   */
  public void from(final Box src) {
    updateTransformationChanged(this.x, src.x);
    updateTransformationChanged(this.y, src.y);
    updateTransformationChanged(this.width, src.width);
    updateTransformationChanged(this.height, src.height);

    this.x = src.x;
    this.y = src.y;
    this.width = src.width;
    this.height = src.height;
  }

  /**
   * Get the current height for the box.
   * 
   * @return the current height of the box
   */
  public int getHeight() {
    return height;
  }

  /**
   * Get the current width of the box.
   * 
   * @return the current width of the box
   */
  public int getWidth() {
    return width;
  }

  /**
   * Get the horizontal position of the box.
   * 
   * @return the horizontal position of the box
   */
  public int getX() {
    return x;
  }

  /**
   * Get the vertical position of the box.
   * 
   * @return the vertical position of the box
   */
  public int getY() {
    return y;
  }

  /**
   * Set a new height for the box.
   * 
   * @param newHeight
   *          the new height for the box.
   */
  public void setHeight(final int newHeight) {
    updateTransformationChanged(this.height, newHeight);
    this.height = newHeight;
  }

  /**
   * Set a new width for the box.
   * 
   * @param newWidth
   *          the new width
   */
  public void setWidth(final int newWidth) {
    updateTransformationChanged(this.width, newWidth);
    this.width = newWidth;
  }

  /**
   * Set both, width and height in one method.
   * @param width the new width
   * @param height the new height
   */
  public void setDimension(final int width, final int height) {
    updateTransformationChanged(this.width, width);
    updateTransformationChanged(this.height, height);
    this.width = width;
    this.height = height;
  }

  /**
   * Get the horizontal position of the box.
   * 
   * @param newX
   *          the vertical position of the box
   */
  public void setX(final int newX) {
    updateTransformationChanged(this.x, newX);
    this.x = newX;
  }

  /**
   * Set the vertical position of the box.
   * 
   * @param newY
   *          the vertical position of the box
   */
  public void setY(final int newY) {
    updateTransformationChanged(this.y, newY);
    this.y = newY;
  }

  /**
   * This will return true when x, y, width or height has been changed.
   * @return true when any values have been changed and false if not
   */
  public boolean isTransformationChanged() {
    return transformationChanged;
  }

  /**
   * Reset the transformation changed flag to unchanged.
   */
  public void resetTransformationChanged() {
    transformationChanged = false;
  }

  private void updateTransformationChanged(final int oldValue, final int newValue) {
    if (newValue != oldValue) {
      transformationChanged = true;
    }
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Box other = (Box) obj;
    if (height != other.height) {
      return false;
    }
    if (width != other.width) {
      return false;
    }
    if (x != other.x) {
      return false;
    }
    if (y != other.y) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + height;
    result = prime * result + width;
    result = prime * result + x;
    result = prime * result + y;
    return result;
  }

  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();
    result.append("InternalBox [x=");
    result.append(x);
    result.append(", y=");
    result.append(y);
    result.append(", width=");
    result.append(width);
    result.append(", height=");
    result.append(height);
    result.append("] ");
    result.append(super.toString());
    return result.toString();
  }

  public StringBuilder toString(final StringBuilder result) {
    result.append("[x=");
    result.append(x);
    result.append(", y=");
    result.append(y);
    result.append(", width=");
    result.append(width);
    result.append(", height=");
    result.append(height);
    result.append("] ");
    result.append(super.toString());
    return result;
  }
}
