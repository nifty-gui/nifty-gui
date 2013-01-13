package de.lessvoid.niftyimpl.layout;

/**
 * The Box class represent a rectangular area on the screen. It has a position (x,y) as well as height and weight
 * attributes. The Box represent an already resolved position in pixels.
 *
 * @author void
 */
public class Box {

  /**
   * Horizontal Position of the box.
   */
  private int x;

  /**
   * Vertical Position of the box.
   */
  private int y;

  /**
   * Width of the box.
   */
  private int width;

  /**
   * Height of the box.
   */
  private int height;

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
    this.height = newHeight;
  }

  /**
   * Set a new width for the box.
   * 
   * @param newWidth
   *          the new width
   */
  public void setWidth(final int newWidth) {
    this.width = newWidth;
  }

  /**
   * Get the horizontal position of the box.
   * 
   * @param newX
   *          the vertical position of the box
   */
  public void setX(final int newX) {
    this.x = newX;
  }

  /**
   * Set the vertical position of the box.
   * 
   * @param newY
   *          the vertical position of the box
   */
  public void setY(final int newY) {
    this.y = newY;
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
}
