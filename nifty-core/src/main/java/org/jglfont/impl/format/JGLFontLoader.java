package org.jglfont.impl.format;

import org.jglfont.spi.JGLFontRenderer;
import org.jglfont.spi.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;



/**
 * A JGLFontLoader.
 * @author void
 */
public interface JGLFontLoader {
  /**
   * Load a font.
   * @param in InputStream
   * @return JGLAbstractFontData
   * @throws IOException
   */
  JGLAbstractFontData load(
          final JGLFontRenderer renderer,
          final ResourceLoader resourceLoader,
          final InputStream in,
          final String filename,
          final int size,
          final int style,
          String params
  ) throws IOException;
}
