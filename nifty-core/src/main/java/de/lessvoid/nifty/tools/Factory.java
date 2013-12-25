package de.lessvoid.nifty.tools;

import javax.annotation.Nonnull;

/**
 * This is the common definition for a factory in the Nifty-GUI.
 *
 * @param <T> the type of objects created by this factory
 * @author void
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public interface Factory<T> {
  /**
   * Create a new instance of the pooled objects.
   *
   * @return the new object instance
   */
  @Nonnull
  T createNew();
}
