package de.lessvoid.nifty.batch.spi;

import de.lessvoid.nifty.batch.TextureAtlasGenerator;

import javax.annotation.Nonnull;
import java.nio.ByteBuffer;

/**
 * User: iamtakingiteasy
 * Date: 2014-01-05
 * Time: 22:19
 */
public interface BatchRendererTexture {
  /**
   * Returns texture width
   *
   * @return texture width
   */
  int getWidth();

  /**
   * Returns texture height
   *
   * @return texture height
   */
  int getHeight();

  /**
   * binds current 2D texture to this BatchRenderTexture' id
   */
  void bind();

  /**
   * Returns texture atlas generator
   *
   * @return texture atlas generator
   */
  TextureAtlasGenerator getGenerator();

  /**
   * Dispose this texture, free allocated resources
   */
  void dispose();

  /**
   * Clear the texture, stub it with dummy data
   */
  void clear();

  /**
   * Adds the given image to the main texture atlas at the given position.
   *
   * @param image the Image data loaded by loadImage()
   * @param x     the x position where to put the image
   * @param y     the y position where to put the image
   */
  void addImageToTexture(@Nonnull Image image, int x, int y);

  /**
   * Remove the image from the texture atlas. This really could be an empty implementation because there really is
   * nothing that this method needs to do. After a call to this method Nifty might reuse the place in the texture
   * with other calls to addImageToTexture().
   *
   * @param image image to remove
   * @param x     x position in texture atlas
   * @param y     y position in texture atlas
   * @param w     width in texture atlas
   * @param h     height in texture atlas
   */
  void removeFromTexture(@Nonnull Image image, int x, int y, int w, int h);


  /**
   * Helper interface to allow the provideImageDimensions() method to return the image dimension and if necessary
   * additional data not visible to Nifty.
   *
   * @author void
   */
  public interface Image {
    int getWidth();
    int getHeight();
    ByteBuffer getData();
  }
}
