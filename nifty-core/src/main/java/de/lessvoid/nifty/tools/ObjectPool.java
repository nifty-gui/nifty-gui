package de.lessvoid.nifty.tools;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.Deque;
import java.util.LinkedList;

/**
 * This is a object pooling utility class. Its used to improve the object handling performance for large object.
 * <p/>
 * Using this class to pool very small objects, <b>will</b> decrease the performance. How ever in case the objects
 * are large and contain for example large buffers, this class can improve the performance.
 * <p/>
 * The pool uses a volatile storage system for the unused instances. It will keep objects as long as Java has a
 * sufficient amount of memory at hand. How ever it will remove instances if the memory is running low.
 *
 * @param <T> the type of object pooled by this class
 */
public class ObjectPool<T> {
  /**
   * The storage pool of the unused instances.
   */
  @Nonnull
  private final Deque<Reference<T>> pool;

  /**
   * The factory instance used to create more instances.
   */
  @Nonnull
  private final Factory<T> factory;

  /**
   * New instance of the object pool.
   *
   * @param factory the factory that is used to create new instances of the pooled object
   */
  public ObjectPool(@Nonnull final Factory<T> factory) {
    this.factory = factory;
    pool = new LinkedList<Reference<T>>();
  }

  /**
   * Fetch a new or unused instance of the pool object.
   *
   * @return a pool object instance
   */
  @Nonnull
  public T allocate() {
    while (!pool.isEmpty()) {
      @Nullable T obj = pool.removeLast().get();
      if (obj != null) {
        return obj;
      }
    }
    return factory.createNew();
  }

  /**
   * Mark a object as unused and send it back into the object pool. This object must not be used anymore after the
   * call of this function until its received again by the {@link #allocate()} function.
   *
   * @param item the object to send to the pool
   */
  public void free(@Nonnull final T item) {
    pool.addFirst(new SoftReference<T>(item));
  }

}
