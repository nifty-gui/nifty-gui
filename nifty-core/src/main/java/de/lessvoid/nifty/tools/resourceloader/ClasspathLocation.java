package de.lessvoid.nifty.tools.resourceloader;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.WillNotClose;
import java.io.InputStream;
import java.net.URL;

/**
 * A resource location that searches the classpath
 *
 * @author kevin
 */
public class ClasspathLocation implements ResourceLocation {
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
