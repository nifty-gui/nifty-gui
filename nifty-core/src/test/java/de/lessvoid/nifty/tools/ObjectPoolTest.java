package de.lessvoid.nifty.tools;

import org.junit.Test;

import javax.annotation.Nonnull;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.Date;


public class ObjectPoolTest {
  private static class HelperObject {
    private final Buffer buffer = ByteBuffer.allocate(64 * 1024);
  }

  private ObjectPool<Object> pool = new ObjectPool<Object>(new Factory<Object>() {
    @Nonnull
    @Override
    public Object createNew() {
      return new Object();
    }
  });
  private ObjectPool<Object> pool2 = new ObjectPool<Object>(new Factory<Object>() {
    @Nonnull
    @Override
    public Object createNew() {
      return new HelperObject();
    }
  });

  @Test
  public void testPool() {
    Object[] storage = new Object[200];
    ObjectPool<Object> pool = this.pool;
    long start = new Date().getTime();
    for (int i = 0; i < 100000; i++) {
      for (int j = 0; j < 100; j++) {
        storage[j] = pool.allocate();
      }
      for (int j = 50; j < 100; j++) {
        pool.free(storage[j]);
      }
      for (int j = 50; j < 200; j++) {
        storage[j] = pool.allocate();
      }
      for (int j = 0; j < 200; j++) {
        pool.free(storage[j]);
      }
    }
    long end = new Date().getTime();
    System.out.println("testPool " + Long.toString(end - start) + "ms");
  }

  @Test
  public void testNoPool() {
    long start = new Date().getTime();
    for (int i = 0; i < 100000; i++) {
      for (int j = 0; j < 200; j++) {
        new Object();
      }
      for (int j = 50; j < 200; j++) {
        new Object();
      }
    }
    long end = new Date().getTime();
    System.out.println("testNoPool " + Long.toString(end - start) + "ms");
  }

  @Test
  public void test2Pool() {
    Object[] storage = new Object[200];
    ObjectPool<Object> pool = this.pool2;
    long start = new Date().getTime();
    for (int i = 0; i < 1000; i++) {
      for (int j = 0; j < 100; j++) {
        storage[j] = pool.allocate();
      }
      for (int j = 50; j < 100; j++) {
        pool.free(storage[j]);
      }
      for (int j = 50; j < 200; j++) {
        storage[j] = pool.allocate();
      }
      for (int j = 0; j < 200; j++) {
        pool.free(storage[j]);
      }
    }
    long end = new Date().getTime();
    System.out.println("test2Pool " + Long.toString(end - start) + "ms");
  }

  @Test
  public void test2NoPool() {
    long start = new Date().getTime();
    for (int i = 0; i < 1000; i++) {
      for (int j = 0; j < 200; j++) {
        new HelperObject();
      }
      for (int j = 50; j < 200; j++) {
        new HelperObject();
      }
    }
    long end = new Date().getTime();
    System.out.println("test2NoPool " + Long.toString(end - start) + "ms");
  }
}
