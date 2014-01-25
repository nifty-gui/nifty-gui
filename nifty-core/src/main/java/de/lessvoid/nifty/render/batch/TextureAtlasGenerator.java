package de.lessvoid.nifty.render.batch;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * This class tries to fit source texture data into a single destination texture.
 * <p/>
 * This work is based on https://github.com/lukaszpaczkowski/texture-atlas-generator by lukaszpaczkowski which is based
 * on the popular packing algorithm http://www.blackpawn.com/texts/lightmaps/ by jimscott@blackpawn.com.
 * <p/>
 * This class tries to separate the actual algorithm from the image manipulating code so that this can be used with
 * different rendering/image frameworks.
 *
 * @author void
 */
public class TextureAtlasGenerator {
  private final int atlasWidth;
  private final int atlasHeight;
  private final int atlasPadding;
  private final float atlasTolerance;
  @Nonnull
  private Node root;
  // for easy access we keep each node in a map with the passed name as the key so we can look up a Node directly
  @Nonnull
  private Map<String, Node> rectangleMap;

  /**
   * You'll get an instance of this class back when you add an image. This class will show you where you'll need to
   * put the current image into the target texture.
   *
   * @author void
   */
  public static class Result {
    private final int x;
    private final int y;
    private final int originalImageWidth;
    private final int originalImageHeight;

    public Result(final int x, final int y, final int originalImageWidth, final int originalImageHeight) {
      this.x = x;
      this.y = y;
      this.originalImageWidth = originalImageWidth;
      this.originalImageHeight = originalImageHeight;
    }

    public int getX() {
      return x;
    }

    public int getY() {
      return y;
    }

    public int getOriginalImageWidth() {
      return originalImageWidth;
    }

    public int getOriginalImageHeight() {
      return originalImageHeight;
    }
  }

  /**
   * Creates a new TextureAtlasGenerator.
   *
   * @param atlasWidth the width of the atlas, must be > 0
   * @param atlasHeight the height of the atlas, must be > 0
   * @param atlasPadding the padding in pixels to apply to images in the atlas (the empty space around the image in the
   *                     atlas that separates it from the surrounding images in the atlas), must be >= 0,
   *                     must be < atlasWidth, must be < atlasHeight
   * @param atlasTolerance the percentage (between 0.0f and 1.0f, inclusive) of atlas area that an image's area (with
   *                       padding) should be less than in order for the image to even attempt to be added to the atlas
   *                       when calling {@link #addImage(int, int, String)}. For example, if tolerance is 0.25f, and
   *                       the width and height of the atlas are 2048, then an image of size 1023 x 1023 with a padding
   *                       of 1 would NOT be added to the atlas when calling {@link #addImage(int, int, String)}
   *                       because it would take up 25% (or 0.25f) of total atlas space, assuming there is even room
   *                       for it in the first place, must be >= 0.0f and <= 1.0f
   */
  public TextureAtlasGenerator(final int atlasWidth,
                               final int atlasHeight,
                               final int atlasPadding,
                               final float atlasTolerance) {
    if (atlasWidth <= 0) {
      throw new IllegalArgumentException("atlas width must be greater than 0");
    } else if (atlasHeight <= 0) {
      throw new IllegalArgumentException("atlas height must be greater than 0");
    } else if (atlasPadding < 0) {
      throw new IllegalArgumentException("atlas padding must be non-negative");
    } else if (atlasPadding >= atlasWidth) {
      throw new IllegalArgumentException("atlas padding must be less than atlas width");
    } else if (atlasPadding >= atlasHeight) {
      throw new IllegalArgumentException("atlas padding must be less than atlas height");
    } else if (atlasTolerance < 0.0 || atlasTolerance > 1.0) {
      throw new IllegalArgumentException("tolerance must be >= 0.0f and <= 1.0f");
    }
    this.atlasWidth = atlasWidth;
    this.atlasHeight = atlasHeight;
    this.atlasPadding = atlasPadding;
    this.atlasTolerance = atlasTolerance;
    reset();
  }

  /**
   * Attempts to add an image and if successful, calculates the target position of the image in the bigger texture as a
   * Result instance. Please note that it is up to you to position the image data. This will just give you the
   * coordinates.
   *
   * @param imageWidth the width of the image, must be >= 0
   * @param imageHeight the height of the image, must be >= 0
   * @param imageName the name of the image (used to track the individual images in the atlas - you can see this as the
   *                  id of the image)
   *
   * @return the position of the image in the bigger texture taking all other previously added images into account,
   *         or null if the image could not be added to the atlas (either because the atlas was too full, or because
   *         the image's area with padding exceeded the tolerance specified in
   *         {@link #TextureAtlasGenerator(int, int, int, float)}).
   */
  @Nullable
  public Result addImage(final int imageWidth, final int imageHeight, @Nonnull final String imageName) {
    if (imageWidth < 0) {
      throw new IllegalArgumentException("image width must be non-negative");
    } else if (imageHeight < 0) {
      throw new IllegalArgumentException("image height must be non-negative");
    }

    if (! shouldAddImage(imageWidth, imageHeight)) {
      return null;
    }

    Node node = root.insert(imageWidth, imageHeight, atlasPadding);
    if (node == null) {
      return null;
    }

    rectangleMap.put(imageName, node);
    return new Result(node.rect.x, node.rect.y, imageWidth, imageHeight);
  }

  /**
   * Determines whether the image (with padding) would take up too much atlas area if it were to be added. If this
   * method returns false, it guarantees that the image will not be added to the atlas if
   * {@link #addImage(int, int, String)} is called. If this method return true, however, it DOES NOT GUARANTEE that the
   * image will fit in the atlas - the atlas could already be so full that even a relatively small image will still not
   * fit when calling {@link #addImage(int, int, String)}.
   *
   * @param imageWidth the width of the image, must be >= 0
   * @param imageHeight the height of the image, must be >= 0
   *
   * @return Whether an image of size (imageWidth + padding) x (imageHeight + padding) would take up too much area for
   * it to be practical to have it in the atlas (based on the tolerance specified in
   * {@link #TextureAtlasGenerator(int, int, int, float)}).
   */
  public boolean shouldAddImage(final int imageWidth, final int imageHeight) {
    if (imageWidth < 0) {
      throw new IllegalArgumentException("image width must be non-negative");
    } else if (imageHeight < 0) {
      throw new IllegalArgumentException("image height must be non-negative");
    }
    return (imageWidth + atlasPadding) <= atlasWidth &&
            (imageHeight + atlasPadding) <= atlasHeight &&
            (imageWidth + atlasPadding) * (imageHeight + atlasPadding) / (float) (atlasWidth * atlasHeight) < atlasTolerance;
  }

  @Nullable
  public Result removeImage(@Nonnull final String name) {
    Node node = rectangleMap.remove(name);
    if (node == null) {
      return null;
    }

    node.occupied = false;
    node.child[0] = null;
    node.child[1] = null;
    return new Result(node.rect.x, node.rect.y, node.rect.width, node.rect.height);
  }

  public int getAtlasWidth() {
    return atlasWidth;
  }

  public int getAtlasHeight() {
    return atlasHeight;
  }

  @Nonnull
  public List<Result> rebuild(
      final int width,
      final int height) {
    List<Result> results = new ArrayList<Result>();
    root = new Node(0, 0, width, height);
    for (Map.Entry<String, Node> entry : rectangleMap.entrySet()) {
      Rectangle rect = entry.getValue().rect;
      results.add(addImage(rect.width, rect.height, entry.getKey()));
    }
    return results;
  }

  private static class Rectangle {
    private final int x;
    private final int y;
    private final int width;
    private final int height;

    public Rectangle(final int x, final int y, final int width, final int height) {
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
    }
  }

  private static class Node {
    @Nonnull
    public final Rectangle rect;
    @Nonnull
    public final Node[] child;
    public boolean occupied;

    public Node(final int x, final int y, final int width, final int height) {
      rect = new Rectangle(x, y, width, height);
      child = new Node[2];
      child[0] = null;
      child[1] = null;
      occupied = false;
    }

    public boolean isLeaf() {
      return child[0] == null && child[1] == null;
    }

    // Algorithm from http://www.blackpawn.com/texts/lightmaps/
    @Nullable
    public Node insert(final int imageWidth, final int imageHeight, final int padding) {
      if (!isLeaf()) {
        Node newNode = child[0].insert(imageWidth, imageHeight, padding);
        if (newNode != null) {
          return newNode;
        }
        return child[1].insert(imageWidth, imageHeight, padding);
      }

      if (occupied) {
        return null; // occupied
      }

      if (imageWidth > rect.width || imageHeight > rect.height) {
        return null; // does not fit
      }

      if (imageWidth == rect.width && imageHeight == rect.height) {
        occupied = true; // perfect fit
        return this;
      }

      int dw = rect.width - imageWidth;
      int dh = rect.height - imageHeight;

      if (dw > dh) {
        child[0] = new Node(rect.x, rect.y, imageWidth, rect.height);
        child[1] = new Node(padding + rect.x + imageWidth, rect.y, rect.width - imageWidth - padding, rect.height);
      } else {
        child[0] = new Node(rect.x, rect.y, rect.width, imageHeight);
        child[1] = new Node(rect.x, padding + rect.y + imageHeight, rect.width, rect.height - imageHeight - padding);
      }
      return child[0].insert(imageWidth, imageHeight, padding);
    }
  }

  public void reset() {
    root = new Node(0, 0, atlasWidth, atlasHeight);
    rectangleMap = new TreeMap<String, Node>();
  }
}
