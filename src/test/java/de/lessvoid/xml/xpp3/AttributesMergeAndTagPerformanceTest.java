package de.lessvoid.xml.xpp3;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.NiftyStopwatch;

//@Ignore
public class AttributesMergeAndTagPerformanceTest {
  private static final String EXISTING_KEY = "testKey";
  private static final String EXISTING_VALUE = "testValue";
  private static final String NEW_KEY = "newKey";
  private static final String NEW_VALUE = "newValue";
  private static final String TAG = "tag";

  private Attributes attributes = new Attributes();
  private Random r = new Random();

  @Before
  public void setUp() {
    for (int i=0; i<10000; i++) {
      attributes.set(String.valueOf(r.nextInt()), String.valueOf(r.nextInt()));
    }
  }

  @Test
  public void testPerformance() {
    Attributes src = new Attributes();
    for (int i=0; i<10000; i++) {
      src.set(String.valueOf(r.nextInt()), String.valueOf(r.nextInt()));
    }

    NiftyStopwatch.start();
    attributes.mergeAndTag(src, TAG);
    NiftyStopwatch.stop("merge and tag");
  }
}
