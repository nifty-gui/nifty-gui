package org.jglfont.impl.format;

import org.jglfont.spi.JGLFontRenderer;
import org.jglfont.spi.ResourceLoader;

import java.util.Collections;
import java.util.Hashtable;
import java.util.Map;
import java.util.TreeMap;


/**
 * A Bitmap Font.
 * @author void
 */
public abstract class JGLAbstractFontData {
  /**
   * Font renderer
   */
  protected JGLFontRenderer renderer;

  /**
   * Resource loader
   */
  protected ResourceLoader resourceLoader;

  /**
   * Name of the font.
   */
  protected String name;

  /**
   * All of the bitmaps this font requires (filenames). These are images
   * that are bitmapWidth * bitmapHeight in size each.
   */
  protected Map<Integer, String> bitmaps = new TreeMap<Integer, String>();

  /**
   * Width of a single font bitmap.
   */
  protected int bitmapWidth;

  /**
   * Height of a single font bitmap.
   */
  protected int bitmapHeight;

  /**
   * Height of a single line.
   */
  protected int lineHeight;

  /**
   * CharacterInfo for all characters in the font file.
   */
  protected Map<Integer, JGLFontGlyphInfo> characters = new GlyphTable();

  protected JGLAbstractFontData(JGLFontRenderer renderer, ResourceLoader resourceLoader) {
    this.renderer = renderer;
    this.resourceLoader = resourceLoader;
  }

  /**
   * @return JGLFontRenderer instance
   */
  public JGLFontRenderer getRenderer() {
    return renderer;
  }

  /**
   * @param renderer renderer to use
   */
  public void setRenderer(JGLFontRenderer renderer) {
    this.renderer = renderer;
  }

  /**
   * @return resource loader
   */
  public ResourceLoader getResourceLoader() {
    return resourceLoader;
  }

  /**
   * @param resourceLoader resource loader to use
   */
  public void setResourceLoader(ResourceLoader resourceLoader) {
    this.resourceLoader = resourceLoader;
  }

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @param name the name to set
   */
  public void setName(final String name) {
    this.name = name;
  }

  /**
   * @return the bitmaps
   */
  public Map<Integer, String> getBitmaps() {
    return bitmaps;
  }

  /**
   * @param index the bitmaps to set
   */
  public void addBitmap(final int index, final String name) {
    bitmaps.put(index, name);
  }

  /**
   * @return the bitmapWidth
   */
  public int getBitmapWidth() {
    return bitmapWidth;
  }

  /**
   * @param bitmapWidth the bitmapWidth to set
   */
  public void setBitmapWidth(final int bitmapWidth) {
    this.bitmapWidth = bitmapWidth;
  }

  /**
   * @return the bitmapHeight
   */
  public int getBitmapHeight() {
    return bitmapHeight;
  }

  /**
   * @param bitmapHeight the bitmapHeight to set
   */
  public void setBitmapHeight(final int bitmapHeight) {
    this.bitmapHeight = bitmapHeight;
  }

  /**
   * @return the lineHeight
   */
  public int getLineHeight() {
    return lineHeight;
  }

  /**
   * @param lineHeight the lineHeight to set
   */
  public void setLineHeight(final int lineHeight) {
    this.lineHeight = lineHeight;
  }

  /**
   * @return the characters
   */
  public Map<Integer, JGLFontGlyphInfo> getGlyphs() {
    return Collections.unmodifiableMap(characters);
  }

  /**
   * @param codepoint the characters to set
   */
  public void addGlyph(final Integer codepoint, final JGLFontGlyphInfo glyphInfo) {
    this.characters.put(codepoint, glyphInfo);
  }

  /**
   * @param codepoint the character to preprocess before accessing
   */
  public void preProcessGlyph(final Integer codepoint) {

  }

  /**
   * initialize font data
   */
  public abstract void init();

  /**
   * Glyph table class
   */
  private class GlyphTable extends Hashtable<Integer,JGLFontGlyphInfo> {
    @Override
    public synchronized JGLFontGlyphInfo get(Object key) {
      if (key instanceof Integer) {
        preProcessGlyph((Integer) key);
      }
      return super.get(key);
    }
  }
}
