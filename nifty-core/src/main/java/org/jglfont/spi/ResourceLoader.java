package org.jglfont.spi;

import java.io.InputStream;

/**
 * This translates a filename (as stored in the angelcode bitmap font format example) into an Inputstream. A
 * ResourceLoaderImpl is provided which will look up the file in the classpath but you can provide your own
 * implementation to customize this behaviour.
 *
 * @author void
 */
public interface ResourceLoader {
  /**
   * Load the resource given by filename using any way you'd like to provide an InputStream.
   *
   * @param filename name of a bitmap
   * @return InputStream to retrieve the resource
   */
  InputStream load(String filename);
}
