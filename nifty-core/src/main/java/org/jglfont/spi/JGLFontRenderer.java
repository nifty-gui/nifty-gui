package org.jglfont.spi;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * The interface necessary to let org.jglfont render a Bitmap. The actual resource loading of bitmap files needs to be done
 * in here. org.jglfont acts as a provider of the glyph data and JGLFontRenderer implementations can use whatever way
 * they seem fit to display this data.
 *
 * A single JGLFontRenderer can be used to render multiple fonts.
 *
 * @author void
 */
public interface JGLFontRenderer {
  /**
   * Register the bitmap with the given data under the given key. This is a texture that contains all the font glyph
   * data. It's not necessary that this call directly maps to a single actual texture since implementation can decide
   * to pack multiple textures into a single one.
   *
   * @param key the key for this specific bitmap
   * @param data the inputstream to the data
   */
  void registerBitmap(final String key, InputStream data, String filename) throws IOException;

  /**
   * Registers the bitmap with the given data in form of bytebuffer, having 32 bit per pixel layout (r,g,b,a)
   *
   * @param key the key for this specific bitmap
   * @param data the direct-allocated ByteBuffer having (R,G,B,A) pixel components
   * @param width width of the image represented by ByteBuffer
   * @param height height of the image represented by ByteBuffer
   * @param filename name of file
   * @throws IOException
   */
  void registerBitmap(final String key, ByteBuffer data, int width, int height, String filename) throws IOException;

  /**
   * Register a single Character Glyph for later rendering.
   * @param c the character
   * @param xoff xoffset in the bitmap
   * @param yoff yoffset in the bitmap
   * @param w the width of the glyph
   * @param h the height of the glyph
   * @param u0 the x texture coordinates of the upper left point
   * @param v0 the y texture coordinates of the upper left point
   * @param u1 the x texture coordinates of the bottom right point
   * @param v1 the y texture coordinates of the bottom right point
   */
  void registerGlyph(String bitmapId, int c, int xoff, int yoff, int w, int h, float u0, float v0, float u1, float v1);

  /**
   * This is called after all registerBitmap() and registerGlyph() calls are done. This can be used to do more
   * initialization prior to actual rendering calls.
   */
  void prepare();
  
  /**
   * This is called before several render() calls are happening. This method can be used to set up state for upcoming
   * text rendering calls or it might allow the implementation to cache state between render() states
   * (f.i. texture state).
   *
   * @param customRenderState some user object provided by JGLFont.setCustomRenderState() or null
   */
  void beforeRender(Object customRenderState);

  /**
   * This allows any pre-processing of the next characters to happen. You're given the complete text and the
   * current offset. You should then process the text beginning from the given offset. If you find something
   * interesting you can do whatever you like with the characters. You can even skip characters that should be
   * excluded from the rendering by return a new offset where the rendering should continue.
   *
   * Nifty-GUI uses this feature to handle the Nifty-GUI specific color encoding that starts with a \# sequence.
   *
   * @param text the complete text to render
   * @param offset the offset where the next characters will get rendered
   * @return the new offset
   */
  int preProcess(String text, int offset);

  /**
   * Render a single character at the given position with the given color using the given bitmapId.
   *
   * @param bitmapId the bitmapId the corresponding character is on
   * @param x the x position
   * @param y the y position
   * @param c the character codepoint to output
   * @param sx x scale factor 
   * @param sy y scale factor
   * @param r red
   * @param g green
   * @param b blue
   * @param a alpha
   */
  void render(String bitmapId, int x, int y, int c, float sx, float sy, float r, float g, float b, float a);

  /**
   * This is called after several render() calls.
   */
  void afterRender();

  /**
   * This allows any pre-processing of the next characters to happen when the width of a text is to be calculated.
   * This is mainly used to skip characters f.i. when color encoded characters are contained in the String that should
   * not be used for string width calculations.
   *
   * @param text the complete text to render
   * @param offset the offset where the next characters will get rendered
   * @return the new offset
   */
  int preProcessForLength(String text, int offset);
}
