package de.lessvoid.nifty.renderer.opengl.font.jglfont.impl.format;

import de.lessvoid.nifty.renderer.opengl.font.jglfont.spi.JGLFontRenderer;
import de.lessvoid.nifty.renderer.opengl.font.jglfont.spi.ResourceLoader;

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
