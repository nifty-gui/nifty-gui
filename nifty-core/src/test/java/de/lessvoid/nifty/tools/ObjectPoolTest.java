package de.lessvoid.nifty.tools;

import java.util.Date;

import org.junit.Test;

import de.lessvoid.nifty.tools.ObjectPool.Factory;


public class ObjectPoolTest {
  private ObjectPool<Object> pool = new ObjectPool<Object>(200, new Factory<Object>() {

    @Override
    public Object createNew() {
      return new Object();
    }
  });

  @Test
  public void test() {
    long start = new Date().getTime();
    for (int i=0; i<100000; i++) {
      for (int j=0; j<200; j++) {
        Object o1 = pool.allocate();
        pool.free(o1);
      }
    }
    long end = new Date().getTime();
    System.out.println(end - start);
  } 
}
