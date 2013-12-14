package de.lessvoid.nifty.tools.resourceloader;

import javax.annotation.Nonnull;

/**
 * Exception thrown in case the resource loader fails to load a resource.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class ResourceNotFoundException extends RuntimeException {
  public ResourceNotFoundException(@Nonnull final String resourceName) {
    super("Failed to load resource \"" + resourceName + "\"");
  }
}
