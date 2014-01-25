package de.lessvoid.nifty.render.batch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import de.lessvoid.nifty.render.batch.TextureAtlasGenerator.Result;

import org.junit.Before;
import org.junit.Test;

public class TextureAtlasGeneratorTest {
  private static final int ATLAS_WIDTH = 100;
  private static final int ATLAS_HEIGHT = 100;
  private static final int ATLAS_PADDING = 5;
  private static final float ATLAS_TOLERANCE = 0.125f;
  private static final TextureAtlasGenerator GENERATOR =
          new TextureAtlasGenerator(ATLAS_WIDTH, ATLAS_HEIGHT, ATLAS_PADDING, ATLAS_TOLERANCE);

  @Before
  public void setup() {
    GENERATOR.reset();
  }

  @Test
  public void testAddingValidImageReturnsValidResult() {
    Result result = GENERATOR.addImage(30, 30, "image");
    assertResult(0, 0, 30, 30, result);
  }

  @Test
  public void testAddingTwoValidImagesReturnsValidResultForSecondImage() {
    GENERATOR.addImage(5, 5, "image-1");
    Result result = GENERATOR.addImage(30, 20, "image-2");
    assertResult(0, 10, 30, 20, result);
  }

  @Test
  public void testAddingThreeValidImagesReturnsValidResultForThirdImage() {
    GENERATOR.addImage(5, 5, "image-1");
    GENERATOR.addImage(30, 20, "image-2");
    Result result = GENERATOR.addImage(90, 1, "image-3");
    assertResult(10, 0, 90, 1, result);
  }

  @Test
  public void testRemovingValidImageReturnsValidResult() {
    GENERATOR.addImage(30, 30, "image");
    Result result = GENERATOR.removeImage("image");
    assertResult(0, 0, 30, 30, result);
  }

  @Test
  public void testShouldAddValidImageReturnsTrue() {
    assertTrue(GENERATOR.shouldAddImage(30, 30));
  }

  @Test
  public void testShouldAddImageWiderAndTallerThanAtlasReturnsFalse() {
    assertFalse(GENERATOR.shouldAddImage(101, 101));
  }

  @Test
  public void testShouldAddImageOnlyWiderThanAtlasReturnsFalse() {
    assertFalse(GENERATOR.shouldAddImage(101, 1));
  }

  @Test
  public void testShouldAddImageOnlyTallerThanAtlasReturnsFalse() {
    assertFalse(GENERATOR.shouldAddImage(1, 101));
  }

  @Test
  public void testShouldAddImageThatCouldFitInAtlasButNotWithinToleranceReturnsFalse() {
    assertFalse(GENERATOR.shouldAddImage(50, 50));
  }

  @Test
  public void testShouldAddImageOnlyWiderThanAtlasWithPaddingReturnsFalse() {
    assertFalse(GENERATOR.shouldAddImage(96, 1));
  }

  @Test
  public void testShouldAddImageOnlyTallerThanAtlasWithPaddingReturnsFalse() {
    assertFalse(GENERATOR.shouldAddImage(1, 96));
  }

  @Test
  public void testShouldAddImageWiderAndTallerThanAtlasWithPaddingReturnsFalse() {
    assertFalse(GENERATOR.shouldAddImage(96, 96));
  }

  @Test
  public void testAddingImageWiderAndTallerThanAtlasReturnsNull() {
    assertNull(GENERATOR.addImage(101, 101, "image"));
  }

  @Test
  public void testAddingImageOnlyWiderThanAtlasReturnsNull() {
    assertNull(GENERATOR.addImage(101, 1, "image"));
  }

  @Test
  public void testAddingImageOnlyTallerThanAtlasReturnsNull() {
    assertNull(GENERATOR.addImage(1, 101, "image"));
  }

  @Test
  public void testAddingImageOnlyWiderThanAtlasWithPaddingReturnsNull() {
    assertNull(GENERATOR.addImage(96, 1, "image"));
  }

  @Test
  public void testAddingImageOnlyTallerThanAtlasWithPaddingReturnsNull() {
    assertNull(GENERATOR.addImage(1, 96, "image"));
  }

  @Test
  public void testAddingImageWiderAndTallerThanAtlasWithPaddingReturnsNull() {
    assertNull(GENERATOR.addImage(96, 96, "image"));
  }

  @Test
  public void testAddingImageThatCouldFitInAtlasButNotWithinToleranceReturnsNull() {
    assertNull(GENERATOR.addImage(50, 50, "image"));
  }

  @Test
  public void testRebuildingAtlasAtSameSizePreservesAddedImages() {
    GENERATOR.addImage(5, 5, "image-1");
    GENERATOR.addImage(30, 20, "image-2");
    GENERATOR.addImage(90, 1, "image-3");
    GENERATOR.rebuild(ATLAS_WIDTH, ATLAS_HEIGHT);
    assertResult(0, 0, 5, 5, GENERATOR.removeImage("image-1"));
    assertResult(0, 10, 30, 20, GENERATOR.removeImage("image-2"));
    assertResult(10, 0, 90, 1, GENERATOR.removeImage("image-3"));
  }

  @Test
  public void testRebuildingAtlasAtLargerSizePreservesAddedImages() {
    GENERATOR.addImage(5, 5, "image-1");
    GENERATOR.addImage(30, 20, "image-2");
    GENERATOR.addImage(90, 1, "image-3");
    GENERATOR.rebuild(ATLAS_WIDTH * 5, ATLAS_HEIGHT * 5);
    assertNotNull(GENERATOR.removeImage("image-1"));
    assertNotNull(GENERATOR.removeImage("image-2"));
    assertNotNull(GENERATOR.removeImage("image-3"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreatingGeneratorWithZeroWidthThrowsIllegalArgumentException() {
    int atlasWidth = 0;
    new TextureAtlasGenerator(atlasWidth, ATLAS_HEIGHT, ATLAS_PADDING, ATLAS_TOLERANCE);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreatingGeneratorWithZeroHeightThrowsIllegalArgumentException() {
    int atlasHeight = 0;
    new TextureAtlasGenerator(ATLAS_WIDTH, atlasHeight, ATLAS_PADDING, ATLAS_TOLERANCE);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreatingGeneratorWithNegativeWidthThrowsIllegalArgumentException() {
    int atlasWidth = -1;
    new TextureAtlasGenerator(atlasWidth, ATLAS_HEIGHT, ATLAS_PADDING, ATLAS_TOLERANCE);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreatingGeneratorWithNegativeHeightThrowsIllegalArgumentException() {
    int atlasHeight = -1;
    new TextureAtlasGenerator(ATLAS_WIDTH, atlasHeight, ATLAS_PADDING, ATLAS_TOLERANCE);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreatingGeneratorWithNegativePaddingThrowsIllegalArgumentException() {
    int atlasPadding = -1;
    new TextureAtlasGenerator(ATLAS_WIDTH, ATLAS_HEIGHT, atlasPadding, ATLAS_TOLERANCE);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreatingGeneratorWithNegativeToleranceThrowsIllegalArgumentException() {
    float atlasTolerance = -0.001f;
    new TextureAtlasGenerator(ATLAS_WIDTH, ATLAS_HEIGHT, ATLAS_PADDING, atlasTolerance);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreatingGeneratorWithToleranceGreaterThanOneThrowsIllegalArgumentException() {
    float atlasTolerance = 1.001f;
    new TextureAtlasGenerator(ATLAS_WIDTH, ATLAS_HEIGHT, ATLAS_PADDING, atlasTolerance);
  }

  private void assertResult(final int x, final int y, final int w, final int h, final Result result) {
    assertEquals(x, result.getX());
    assertEquals(y, result.getY());
    assertEquals(w, result.getOriginalImageWidth());
    assertEquals(h, result.getOriginalImageHeight());
  }
}
