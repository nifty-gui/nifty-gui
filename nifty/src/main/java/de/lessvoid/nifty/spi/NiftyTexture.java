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

  /**
   * Get the id of the atlas this texture is a part of. In case there is no support for Texture atlas in an SPI
   * implementation then this can be the texture id or anything else that identifies this texture.
   *
   * Please note: -1 is reserved and should not be returned by this method
   *
   * @return the texture id of the atlas this texture is a part of
   */
  int getAtlasId();

  /**
   * Get texture coordinate of top left corner of this texture in the texture atlas or 0 for non texture atlas texture.
   * @return texture coordinate U of this textures position in the texture atlas
   */
  double getU0();

  /**
   * Get texture coordinate of top left corner of this texture in the texture atlas or 0 for non texture atlas texture.
   * @return texture coordinate V of this textures position in the texture atlas
   */
  double getV0();

  /**
   * Get texture coordinate of bottom right corner of this texture in the texture atlas or 0 for non texture atlas
   * texture.
   * @return texture coordinate U of this textures position in the texture atlas
   */
  double getV1();

  /**
   * Get texture coordinate of bottom right corner of this texture in the texture atlas or 0 for non texture atlas
   * texture.
   * @return texture coordinate V of this textures position in the texture atlas
   */
  double getU1();
}
