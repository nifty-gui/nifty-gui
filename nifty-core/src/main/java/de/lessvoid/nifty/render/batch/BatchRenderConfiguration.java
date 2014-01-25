package de.lessvoid.nifty.render.batch;

/**
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class BatchRenderConfiguration {
  // Sane default batch rendering values
  public static final int DEFAULT_ATLAS_WIDTH = 2048;
  public static final int DEFAULT_ATLAS_HEIGHT = 2048;
  public static final int DEFAULT_ATLAS_PADDING = 5;
  public static final int DEFAULT_INITIAL_ATLAS_COUNT = 1;
  public static final int DEFAULT_INITIAL_BATCH_COUNT = 1;
  public static final float DEFAULT_ATLAS_TOLERANCE = 0.25f;
  public static final boolean DEFAULT_DISPOSE_IMAGES_BETWEEN_SCREENS = true;
  public static final boolean DEFAULT_USE_HIGH_QUALITY_TEXTURES = false;
  public static final boolean DEFAULT_FILL_REMOVED_IMAGES_IN_ATLAS = false;

  /**
   * The width that any and all texture atlases will be created at. The size of any given atlas is constant; that is,
   * it doesn't change size once created.
   */
  public int atlasWidth = DEFAULT_ATLAS_WIDTH;

  /**
   * The height that any and all texture atlases will be created at. The size of any given atlas is constant; that is,
   * it doesn't change size once created.
   */
  public int atlasHeight = DEFAULT_ATLAS_HEIGHT;


  /**
   * The padding in pixels to apply to images in an atlas (the empty space around each image in an atlas that separates
   * them from the surrounding images in that atlas).
   */
  public int atlasPadding = DEFAULT_ATLAS_PADDING;

  /**
   * The initial number of texture atlases to create. You should set this after testing your application with only 1
   * initial atlas so you can see how many texture atlases you require. If {@link #disposeImagesBetweenScreens} is set
   * to {@code true}, and if a texture atlas gets too full when loading images for a new screen so that an image within
   * atlas tolerance will not fit in that atlas, then a new atlas will be created dynamically, which could cause a
   * noticeable delay during a screen transition. If {@link #disposeImagesBetweenScreens} is set to {@code false},
   * dynamic atlas creation shouldn't really be a problem as long as you're creating all of your screens during the
   * loading / initialization phase of your application because that is when the dynamic atlas creation would happen,
   * rather than during a screen transition.
   */
  public int initialAtlasCount = DEFAULT_INITIAL_ATLAS_COUNT;

  /**
   * The percentage (between 0.0f and 1.0f, inclusive) of an atlas's area that an image's packed area (packed = with
   * padding) should be less than in order for the image to even attempt to be added to that atlas. For example, if
   * tolerance is 0.25f, and the width and height of an atlas are 2048, then an image of size 1023 x 1023 packed with a
   * texture atlas padding of 1 would NOT be added to that atlas because it would take up 25% of total atlas space,
   * assuming there is even room for it in the first place. The image will still be rendered by this device, it just
   * won't be anywhere near as efficient performance-wise to not have it in an atlas because it will require a separate
   * draw call.
   */
  public float atlasTolerance = DEFAULT_ATLAS_TOLERANCE;

  /**
   * Whether or not images of a previous screen should be disposed of when that screen ends; if set to {@code true} and
   * you go back to that screen, its images will have to be reloaded; if set to {@code false}, then all images for all
   * created screens will be held in memory simultaneously. Reloading images between screens will significantly
   * decrease performance, but may be necessary if a screens' images change every time that screen becomes active.
   */
  public boolean disposeImagesBetweenScreens = DEFAULT_DISPOSE_IMAGES_BETWEEN_SCREENS;

  /**
   * Whether or not to render textures with high quality settings. Usually, setting to true will result in slower
   * performance, but nicer looking textures, and vice versa. How high quality textures are rendered versus low quality
   * textures will vary depending on the {@link de.lessvoid.nifty.render.batch.spi.BatchRenderBackend} implementation.
   */
  public boolean useHighQualityTextures = DEFAULT_USE_HIGH_QUALITY_TEXTURES;

  /**
   * Whether or not to overwrite previously used atlas space with blank data. Setting to true will result in slower
   * performance, but may be useful in debugging when visually inspecting the atlas, since there will not be portions
   * of old images visible in currently unused atlas space.
   */
  public boolean fillRemovedImagesInAtlas = DEFAULT_FILL_REMOVED_IMAGES_IN_ATLAS;
}
