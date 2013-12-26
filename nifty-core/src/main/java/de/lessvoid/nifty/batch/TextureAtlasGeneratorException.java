package de.lessvoid.nifty.batch;

/**
 * You'll get this Exception when the current image does not fit into the texture anymore.
 *
 * @author void
 */
public class TextureAtlasGeneratorException extends Exception {
  private static final long serialVersionUID = 1L;

  private final String name;
  private final int width;
  private final int height;

  public TextureAtlasGeneratorException(final int width, final int height, final String name) {
    this.name = name;
    this.width = width;
    this.height = height;
  }

  public String getName() {
    return name;
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }
}
