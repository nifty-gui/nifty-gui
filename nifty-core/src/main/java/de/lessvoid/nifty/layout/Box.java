package de.lessvoid.nifty.layout;

/**
 * The Box class represent a rectangular area on the screen.
 * It has a position (x,y) as well as height and weight attributes.
 * The Box represent and already resolved position in pixel.
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
	 * Create a new Box with some default coordinates (x,y) set to (0,0) and
	 * with width and height set to 0.
	 */
	public Box() {
		this.x = 0;
		this.y = 0;
		this.width = 0;
		this.height = 0;
	}

	/**
	 * Create a new Box with the given coordinates.
	 * @param newX the x position of the box
	 * @param newY the y position of the box
	 * @param newWidth the new width of the box
	 * @param newHeight the new height of the box
	 */
	public Box(final int newX, final int newY, final int newWidth, final int newHeight) {
		this.x = newX;
		this.y = newY;
		this.width = newWidth;
		this.height = newHeight;
	}

	/**
	 * copy constructor.
	 * @param src src box to copy from
	 */
	public Box(final Box src) {
		this.x = src.x;
		this.y = src.y;
		this.width = src.width;
		this.height = src.height;
	}

	/**
	 * Get the horizontal position of the box.
	 * @return the horizontal position of the box
	 */
	public final int getX() {
		return x;
	}

	/**
	 * Get the horizontal position of the box.
	 * @param newX the vertical position of the box
	 */
	public final void setX(final int newX) {
		this.x = newX;
	}

	/**
	 * Get the vertical position of the box.
	 * @return the vertical position of the box
	 */
	public final int getY() {
		return y;
	}

	/**
	 * Set the vertical position of the box.
	 * @param newY the vertical position of the box
	 */
	public final void setY(final int newY) {
		this.y = newY;
	}

	/**
	 * Get the current height for the box.
	 * @return the current height of the box
	 */
	public final int getHeight() {
		return height;
	}

	/**
	 * Set a new height for the box.
	 * @param newHeight the new height for the box.
	 */
	public final void setHeight(final int newHeight) {
		this.height = newHeight;
	}

	/**
	 * Get the current width of the box.
	 * @return the current width of the box
	 */
	public final int getWidth() {
		return width;
	}

	/**
	 * Set a new width for the box.
	 * @param newWidth the new width
	 */
	public final void setWidth(final int newWidth) {
		this.width = newWidth;
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
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Box other = (Box) obj;
		if (height != other.height)
			return false;
		if (width != other.width)
			return false;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}
}
