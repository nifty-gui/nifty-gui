package de.lessvoid.nifty.slick2d.loaders;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * The abstract implementation of a resource loader.
 * 
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 * @param <T>
 *          The type of loader used by this loader list
 */
public abstract class AbstractSlickLoaders<T extends SlickLoader> {
  /**
   * The list of font loaders that are expected to be queried.
   */
  private final List<T> loaders;

  /**
   * Create and prepare this class.
   */
  protected AbstractSlickLoaders() {
    loaders = new ArrayList<T>();
  }

  /**
   * Add a loader to the list of loaders that get queried when loading a new
   * resource.
   * 
   * @param newLoader
   *          the new font loader
   * @param order
   *          the loader where the place the new loader on the list
   */
  public void addLoader(final T newLoader, final SlickAddLoaderLocation order) {
    if (checkAlreadyLoaded(newLoader)) {
      return;
    }

    switch (order) {
    case first:
      loaders.add(0, newLoader);
      break;
    case last:
    case doNotCare:
    default:
      loaders.add(newLoader);
      break;
    }
  }

  /**
   * Check if the resource loader that is about to be added already a part of
   * the loaders list. This is done be comparing the classes of the loaders.
   * 
   * @param newLoader
   *          the loader that is to be added
   * @return <code>true</code> in case the loader is already added to the
   *         loaders list
   */
  private boolean checkAlreadyLoaded(final T newLoader) {
    final Class<?> newLoaderClass = newLoader.getClass();
    Class<?> currentLoaderClass;
    for (final T currentLoader : loaders) {
      currentLoaderClass = currentLoader.getClass();
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

  /**
   * Add the default implemented loaders to the loader list. This is done
   * automatically in case resources are requested but no loaders got
   * registered. In general using this function should be avoided. Its better to
   * load only the loaders that are actually needed for your resources.
   * 
   * @param order
   *          the place where the default loaders are added to the list
   */
  public abstract void loadDefaultLoaders(final SlickAddLoaderLocation order);
}
