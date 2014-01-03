package de.lessvoid.nifty.spi;


/**
 * A NiftyTexture is a handle to a texture surface. A NiftyTexture can be rendered AND can be rendered to.
 * @author void
 */
public interface NiftyTexture {

  /**
   * Get the width of this texture.
   * @return the texture width
   */
  int getWidth();

  /**
   * Get the height of this texture.
   * @return the texture height
   */
  int getHeight();
}
