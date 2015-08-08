package de.lessvoid.nifty.internal.common.resourceloader;

import java.io.InputStream;
import java.net.URL;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.WillNotClose;

import de.lessvoid.nifty.internal.NiftyResourceLocation;

/**
 * A resource location that searches the classpath
 *
 * @author kevin
 */
public class ClasspathLocation implements NiftyResourceLocation {
  @Nullable
  @Override
  public URL getResource(@Nonnull final String ref) {
    String cpRef = ref.replace('\\', '/');
    return Thread.currentThread().getContextClassLoader().getResource(cpRef);
  }

  @Nullable
  @Override
  @WillNotClose
  public InputStream getResourceAsStream(@Nonnull final String ref) {
    String cpRef = ref.replace('\\', '/');
    return Thread.currentThread().getContextClassLoader().getResourceAsStream(cpRef);
  }
}
