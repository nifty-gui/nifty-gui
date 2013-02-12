package de.lessvoid.nifty.batch;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.lessvoid.nifty.batch.TextureAtlasGenerator.Result;

public class TextureAtlasGeneratorTest {
  private TextureAtlasGenerator generator = new TextureAtlasGenerator(100, 100);

  @Test
  public void testAdd() throws Exception {
    Result result = generator.addImage(20, 20, "name-1", 5);
    assertResult(0, 0, 20, 20, result);

    result = generator.addImage(50, 50, "name-2", 5);
    assertResult(0, 25, 50, 50, result);

    generator.removeImage("name-1");
    generator.rebuild(100, 100, 5);

    result = generator.addImage(5, 5, "name-3", 5);
    assertResult(55, 0, 5, 5, result);
  }

  private void assertResult(final int x, final int y, final int w, final int h, final Result result) {
    assertEquals(x, result.getX());
    assertEquals(y, result.getY());
    assertEquals(w, result.getOriginalImageWidth());
    assertEquals(h, result.getOriginalImageHeight());
  }

}