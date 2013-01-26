package de.lessvoid.nifty.tools.resourceloader;

import java.io.InputStream;
import java.net.URL;

/**
 * A resource location that searches the classpath
 * 
 * @author kevin
 */
public class ClasspathLocation implements ResourceLocation {
  /**
   * @see org.newdawn.slick.util.ResourceLocation#getResource(java.lang.String)
   */
  public URL getResource(String ref) {
    String cpRef = ref.replace('\\', '/');
    return Thread.currentThread().getContextClassLoader().getResource(cpRef);
  }

  /**
   * @see org.newdawn.slick.util.ResourceLocation#getResourceAsStream(java.lang.String)
   */
  public InputStream getResourceAsStream(String ref) {
    String cpRef = ref.replace('\\', '/');
    return Thread.currentThread().getContextClassLoader().getResourceAsStream(cpRef);  
  }
}
