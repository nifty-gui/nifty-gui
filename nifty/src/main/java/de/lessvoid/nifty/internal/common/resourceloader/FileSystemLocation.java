package de.lessvoid.nifty.internal.common.resourceloader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.WillNotClose;

import de.lessvoid.nifty.internal.NiftyResourceLocation;

/**
 * A resource loading location that searches somewhere on the classpath
 *
 * @author kevin
 */
public class FileSystemLocation implements NiftyResourceLocation {
  /**
   * The root of the file system to search
   */
  @Nonnull
  private final File root;

  /**
   * Create a new resource location based on the file system
   *
   * @param root The root of the file system to search
   */
  public FileSystemLocation(@Nonnull final File root) {
    this.root = root;
  }

  @Nullable
  @Override
  public URL getResource(@Nonnull final String ref) {
    try {
      File file = new File(root, ref);
      if (!file.exists()) {
        file = new File(ref);
      }
      if (!file.exists()) {
        return null;
      }

      return file.toURI().toURL();
    } catch (IOException e) {
      return null;
    }
  }

  @Nullable
  @Override
  @WillNotClose
  public InputStream getResourceAsStream(@Nonnull final String ref) {
    try {
      File file = new File(root, ref);
      if (!file.exists()) {
        file = new File(ref);
      }
      return new FileInputStream(file);
    } catch (IOException e) {
      return null;
    }
  }

}
