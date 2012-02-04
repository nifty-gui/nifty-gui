package de.lessvoid.nifty.slick2d.loaders;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * The abstract implementation of a resource loader.
 *
 * @param <T> The type of loader used by this loader list
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public abstract class AbstractSlickLoaders<T extends SlickLoader> implements SlickLoaders<T> {
  /**
   * The list of font loaders that are expected to be queried.
   */
  private final List<T> loaders;

  /**
   * Create and prepare this class.
   */
  protected AbstractSlickLoaders() {
    loaders = new ArrayList<T>(1);
  }

  /**
   * Add a loader to the list of loaders that get queried when loading a new resource.
   *
   * @param newLoader the new font loader
   * @param order the loader where the place the new loader on the list
   */
  @Override
  public final void addLoader(final T newLoader, final SlickAddLoaderLocation order) {
    if (checkAlreadyLoaded(newLoader)) {
      return;
    }

    switch (order) {
      case first:
        loaders.add(0, newLoader);
        break;
      case last:
      case doNotCare:
        loaders.add(newLoader);
        break;
    }
  }

  /**
   * Check if the resource loader that is about to be added already a part of the loaders list. This is done be
   * comparing the classes of the loaders.
   *
   * @param newLoader the loader that is to be added
   * @return {@code true} in case the loader is already added to the loaders list
   */
  private boolean checkAlreadyLoaded(final T newLoader) {
    final Class<?> newLoaderClass = newLoader.getClass();
    for (final T currentLoader : loaders) {
      final Class<?> currentLoaderClass = currentLoader.getClass();
      if (newLoaderClass.equals(currentLoaderClass)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Get the iterator that can be used to access all the loaders.
   *
   * @return the loader iterator
   */
  protected final Iterator<T> getLoaderIterator() {
    if (loaders.isEmpty()) {
      loadDefaultLoaders(SlickAddLoaderLocation.first);
    }

    return loaders.iterator();
  }
}
