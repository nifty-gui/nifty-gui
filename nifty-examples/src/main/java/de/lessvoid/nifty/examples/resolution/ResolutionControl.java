package de.lessvoid.nifty.examples.resolution;

import java.util.Collection;

/**
 * The resolution change example needs a control to actually read and alter the used resolutions. This interface needs
 * to be implemented by the implementing renderers to provide the access to the different functions.
 * 
 * @param <T></T> the type of the objects that are used to identify the different resolution values
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public interface ResolutionControl<T> {
  /**
   * Get a list of resolutions that can be applied.
   *
   * @return the list of valid resolutions
   */
  Collection<T> getResolutions();

  /**
   * Set a resolution that should be used from now on.
   *
   * @param newResolution the new resolution
   */
  void setResolution(T newResolution);

  /**
   * Get the resolution that applies currently.
   *
   * @return the current resolution
   */
  T getCurrentResolution();
}
