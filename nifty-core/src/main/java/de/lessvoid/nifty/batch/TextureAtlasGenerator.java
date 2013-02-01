package de.lessvoid.nifty.batch;

import java.util.Map;
import java.util.TreeMap;

/**
 * This class tries to fit TextureSource data into a single TextureDestination.
 *
 * This work is based on https://github.com/lukaszpaczkowski/texture-atlas-generator by lukaszpaczkowski which is based
 * on the popular packing algorithm http://www.blackpawn.com/texts/lightmaps/ by jimscott@blackpawn.com.
 *
 * This class tries to separate the actual algorithm from the image manipulating code so that this can be used with
 * different rendering/image frameworks.
 *
 * @author void
 */
public class TextureAtlasGenerator {
  private Node root;
  private Map<String, Rectangle> rectangleMap;

  /**
   * You'll get an instance of this class back when you add an image. This class will show you where you'll need to
   * put the current image into the target texture.
   * @author void
   */
  public static class Result {
    private int x;
    private int y;
    private int originalImageWidth;
    private int originalImageHeight;

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
   * Create a new TextureAtlasGenerator 
   * @param destination
   * @param width
   * @param height
   */
  public TextureAtlasGenerator(final int width, final int height) {
    this.root = new Node(0, 0, width, height);
    this.rectangleMap = new TreeMap<String, Rectangle>();
  }

  /**
   * Adds an image and calculates the target position of the image in the bigger texture as a Result instance.
   * Please note that it is up to you to position the image data. This will just give you the coordinates.
   *
   * @param imageWidth image width to add
   * @param imageHeight image height to add
   * @param name name of the image this is used to track the individual images you can see this as the id of the image
   * @param padding padding to apply
   * @return the position of the image in the bigger texture taking all other previously added images into account
   * @throws TextureAtlasGeneratorException when the image could not be added
   */
  public Result addImage(final int imageWidth, final int imageHeight, final String name, final int padding)
      throws TextureAtlasGeneratorException {
      Node node = root.insert(imageWidth, imageHeight, padding);
      if (node == null) {
        throw new TextureAtlasGeneratorException(imageWidth, imageHeight, name);
      }

      rectangleMap.put(name, node.rect);
      return new Result(node.rect.x, node.rect.y, imageWidth, imageHeight);
  }

  private static class Rectangle {
    private int x;
    private int y;
    private int width;
    private int height;

    public Rectangle(final int x, final int y, final int width, final int height) {
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
    }
  }

  private class Node {
    public Rectangle rect;
    public Node child[];
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
    public Node insert(final int imageWidth, final int imageHeight, final int padding) {
      if (!isLeaf()) {
        Node newNode = child[0].insert(imageWidth, imageHeight, padding);
        if (newNode != null) {
          return newNode;
        }
        return child[1].insert(imageWidth, imageHeight, padding);
      } else {
        if (this.occupied) {
          return null; // occupied
        }

        if (imageWidth > rect.width || imageHeight > rect.height) {
          return null; // does not fit
        }

        if (imageWidth == rect.width && imageHeight == rect.height) {
          this.occupied = true; // perfect fit
          return this;
        }

        int dw = rect.width - imageWidth;
        int dh = rect.height - imageHeight;

        if (dw > dh) {
          child[0] = new Node(rect.x, rect.y, imageWidth, rect.height);
          child[1] = new Node(padding + rect.x + imageWidth, rect.y, rect.width - imageWidth - padding,
              rect.height);
        } else {
          child[0] = new Node(rect.x, rect.y, rect.width, imageHeight);
          child[1] = new Node(rect.x, padding + rect.y + imageHeight, rect.width, rect.height
              - imageHeight - padding);
        }
        return child[0].insert(imageWidth, imageHeight, padding);
      }
    }
  }
}
