package de.lessvoid.nifty.slick2d.loaders;

/**
 * This is the interface of any loader collection that is used inside the Slick2D render binding.
 *
 * @param <T> The type of loader used by this loader list
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public interface SlickLoaders<T extends SlickLoader> {
  /**
   * Add a loader to the list of loaders that get queried when loading a new resource.
   *
   * @param newLoader the new font loader
   * @param order the loader where the place the new loader on the list
   */
  void addLoader(T newLoader, SlickAddLoaderLocation order);

  /**
   * Add the default implemented loaders to the loader list. This is done automatically in case resources are requested
   * but no loaders got registered. In general using this function should be avoided. Its better to load only the
   * loaders that are actually needed for your resources.
   *
   * @param order the place where the default loaders are added to the list
   */
  void loadDefaultLoaders(SlickAddLoaderLocation order);
}
