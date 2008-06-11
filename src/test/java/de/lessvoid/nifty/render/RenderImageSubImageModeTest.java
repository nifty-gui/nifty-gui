package de.lessvoid.nifty.render;

import junit.framework.TestCase;

public class RenderImageSubImageModeTest extends TestCase {
  
  public void testNORMAL() {
    assertTrue(RenderImageSubImageMode.NORMAL().equals(RenderImageSubImageMode.valueOf("normal")));
  }

  public void testSCALE() {
    assertTrue(RenderImageSubImageMode.SCALE().equals(RenderImageSubImageMode.valueOf("scale")));
  }

  public void testRESIZE() {
    assertTrue(RenderImageSubImageMode.RESIZE().equals(RenderImageSubImageMode.valueOf("resizeHint")));
  }

  public void testNotFound() {
    assertTrue(RenderImageSubImageMode.NORMAL().equals(RenderImageSubImageMode.valueOf("bla")));
  }

}
