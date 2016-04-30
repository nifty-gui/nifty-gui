package org.jglfont.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.jglfont.spi.ResourceLoader;

public class ResourceLoaderImpl implements ResourceLoader {

  @Override
  public InputStream load(final String filename) {
    InputStream is = Thread.currentThread().getClass().getResourceAsStream("/" + filename);
    if (is == null) {
      File file = new File(filename);
      if (file.exists()) {
        try {
          is = new FileInputStream(file);
        } catch (FileNotFoundException ignore) {
        }
      }
    }
    return is;
  }
}
