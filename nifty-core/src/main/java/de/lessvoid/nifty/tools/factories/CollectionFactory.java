package de.lessvoid.nifty.tools.factories;

import de.lessvoid.nifty.tools.Factory;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;

/**
 * This is the shared implementation for a factory that creates {@link java.util.Collection} instances.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public abstract class CollectionFactory<T> implements Factory<Collection<T>> {
  /**
   * The singleton instance of this class.
   */
  @Nonnull
  private static final CollectionFactory<?> ARRAY_LIST_INSTANCE = new CollectionFactory<Object>() {
    @Nonnull
    @Override
    public Collection<Object> createNew() {
      return new ArrayList<Object>();
    }
  };

  /**
   * Get a instance of this class.
   *
   * @param <T> the type of the array lists created with this factory
   * @return the array list factory
   */
  @SuppressWarnings("unchecked")
  @Nonnull
  public static <T> Factory<Collection<T>> getArrayListInstance() {
    return (CollectionFactory<T>) ARRAY_LIST_INSTANCE;
  }

  @Nonnull
  @Override
  public abstract Collection<T> createNew();
}
