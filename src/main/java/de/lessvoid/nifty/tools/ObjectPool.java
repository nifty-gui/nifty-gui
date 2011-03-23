package de.lessvoid.nifty.tools;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class ObjectPool<T> {
  private static Logger log = Logger.getLogger(ObjectPool.class.getName());
  private List<T> pool;
  private List<Integer> free;
  private Map<T, Integer> indexLookUp;
  private Factory<T> factory;

  public ObjectPool(final int size, final Factory<T> initialFactory) {
    this.factory = initialFactory;
    pool = new ArrayList<T>(size);
    free = new ArrayList<Integer>(size);
    indexLookUp = new Hashtable<T, Integer>(size);
    for (int i=0; i<size; i++) {
      T item = initialFactory.createNew();
      pool.add(item);
      free.add(i);
      indexLookUp.put(item, i);
    }
  }

  public T allocate() {
    if (free.isEmpty()) {
      // this means we're running out of capacity. duplicate the list and complain in the log...
      int size = pool.size();
      log.warning("running ouf of pool objects! used capacity [" + size + "] new capacity [" + size * 2 + "]");
      for (int i=0; i<size; i++) {
        T item = factory.createNew();
        pool.add(item);
        free.add(size + i);
        indexLookUp.put(item, size + i);
      }
    }
    final Integer index = free.remove(free.size() - 1);
    return pool.get(index);
  }

  public void free(final T item) {
    int index = indexLookUp.get(item);
    free.add(index);
  }

  public int getFreeCount() {
    return pool.size() - free.size();
  }

  public interface Factory<T> {
    T createNew();
  }
}
