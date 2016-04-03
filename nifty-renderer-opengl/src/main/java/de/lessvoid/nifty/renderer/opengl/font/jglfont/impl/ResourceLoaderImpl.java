package de.lessvoid.nifty.renderer.opengl.font.jglfont.impl;

import de.lessvoid.nifty.renderer.opengl.font.jglfont.spi.ResourceLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

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
