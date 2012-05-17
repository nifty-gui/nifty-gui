package de.lessvoid.nifty.renderer.jogl.render.io;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Hashtable;

import javax.imageio.ImageIO;

/**
 * An image data provider that uses ImageIO to retrieve image data in a format
 * suitable for creating OpenGL textures. This implementation is used when
 * formats not natively supported by the library are required.
 * 
 * @author kevin
 */
public class ImageIOImageData implements ImageData {
  private static final ColorModel glAlphaColorModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB), new int[] { 8, 8, 8, 8 }, true, false, ComponentColorModel.TRANSLUCENT, DataBuffer.TYPE_BYTE);
  private static final ColorModel glColorModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB), new int[] { 8, 8, 8, 0 }, false, false, ComponentColorModel.OPAQUE, DataBuffer.TYPE_BYTE);
  private int depth;
  private int height;
  private int width;
  private int texWidth;
  private int texHeight;
  private boolean edging = true;
  
  /* (non-Javadoc)
   * @see de.lessvoid.nifty.renderer.lwjgl.render.io.ImageData#getDepth()
   */
  public int getDepth() {
    return depth;
  }
  
  /* (non-Javadoc)
   * @see de.lessvoid.nifty.renderer.lwjgl.render.io.ImageData#getHeight()
   */
  public int getHeight() {
    return height;
  }
  
  /* (non-Javadoc)
   * @see de.lessvoid.nifty.renderer.lwjgl.render.io.ImageData#getTexHeight()
   */
  public int getTexHeight() {
    return texHeight;
  }
  
  /* (non-Javadoc)
   * @see de.lessvoid.nifty.renderer.lwjgl.render.io.ImageData#getTexWidth()
   */
  public int getTexWidth() {
    return texWidth;
  }
  
  /* (non-Javadoc)
   * @see de.lessvoid.nifty.renderer.lwjgl.render.io.ImageData#getWidth()
   */
  public int getWidth() {
    return width;
  }
  
  public ByteBuffer loadImage(InputStream fis) throws IOException {
    return loadImage(fis, false, null);
  }
  
  public ByteBuffer loadImage(InputStream fis, boolean flipped, int[] transparent) throws IOException {
    return loadImage(fis, flipped, false, transparent);
  }
  
  public ByteBuffer loadImage(InputStream fis, boolean flipped, boolean forceAlpha, int[] transparent) throws IOException {
    if (transparent != null) {
      forceAlpha = true;
    }

    BufferedImage bufferedImage = ImageIO.read(fis);
    return imageToByteBuffer(bufferedImage, flipped, forceAlpha, transparent, true, false);
  }
  
  public BufferedImage loadMouseCursorImage(InputStream fis) throws IOException {
    return ImageIO.read(fis);
  }
  
  public ByteBuffer imageToByteBuffer(BufferedImage image, boolean flipped, boolean forceAlpha, int[] transparent, boolean powerOfTwoSupport, boolean modeARGB) {
      ByteBuffer imageBuffer = null;
      WritableRaster raster;
      BufferedImage texImage;

      int texWidth = image.getWidth();
      int texHeight = image.getHeight();

      if (powerOfTwoSupport) {
        // find the closest power of 2 for the width and height
        // of the produced texture
        texWidth = 2;
        texHeight = 2;

        while (texWidth < image.getWidth()) {
          texWidth *= 2;
        }
        while (texHeight < image.getHeight()) {
          texHeight *= 2;
        }
      }
      
      this.width = image.getWidth();
      this.height = image.getHeight();
      this.texHeight = texHeight;
      this.texWidth = texWidth;
      
      // create a raster that can be used by OpenGL as a source
      // for a texture
      boolean useAlpha = image.getColorModel().hasAlpha() || forceAlpha;
      
      if (useAlpha) {
        depth = 32;
        raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE, texWidth, texHeight, 4, null);
        texImage = new BufferedImage(glAlphaColorModel, raster, false, new Hashtable<Object, Object>());
      } else {
        depth = 24;
        raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE, texWidth, texHeight, 3, null);
        texImage = new BufferedImage(glColorModel, raster, false, new Hashtable<Object, Object>());
      }
      
      // copy the source image into the produced image
      Graphics2D g = (Graphics2D) texImage.getGraphics();
      
      // only need to blank the image for mac compatibility if we're using alpha
      if (useAlpha) {
        g.setColor(new Color(0f, 0f, 0f, 0f));
        g.fillRect(0, 0, texWidth, texHeight);
      }
      
      if (flipped) {
        g.scale(1, -1);
        g.drawImage(image, 0, -height, null);
      } else {
        g.drawImage(image, 0, 0, null);
      }
      
      if (edging) {
        if (height < texHeight - 1) {
          copyArea(texImage, 0, 0, width, 1, 0, texHeight - 1);
          copyArea(texImage, 0, height - 1, width, 1, 0, 1);
        }
        if (width < texWidth - 1) {
          copyArea(texImage, 0, 0, 1, height, texWidth - 1, 0);
          copyArea(texImage, width - 1, 0, 1, height, 1, 0);
        }
      }
      
      // build a byte buffer from the temporary image
      // that be used by OpenGL to produce a texture.
      byte[] data = ((DataBufferByte) texImage.getRaster().getDataBuffer()).getData();
      if (transparent != null) {
        for (int i = 0; i < data.length; i += 4) {
          boolean match = true;
          for (int c = 0; c < 3; c++) {
            int value = data[i + c] < 0 ? 256 + data[i + c] : data[i + c];
            if (value != transparent[c]) {
              match = false;
            }
          }
          
          if (match) {
            data[i + 3] = 0;
          }
        }
      }
      if (modeARGB) {
        for (int i = 0; i < data.length; i += 4) {
          byte rr = data[i + 0];
          byte gg = data[i + 1];
          byte bb = data[i + 2];
          byte aa = data[i + 3];
          data[i + 0] = bb;
          data[i + 1] = gg;
          data[i + 2] = rr;
          data[i + 3] = aa;
        }
      }
      
      imageBuffer = ByteBuffer.allocateDirect(data.length);
      imageBuffer.order(ByteOrder.nativeOrder());
      imageBuffer.put(data, 0, data.length);
      imageBuffer.flip();
      g.dispose();

      return imageBuffer;
    }
  
  /* (non-Javadoc)
   * @see de.lessvoid.nifty.renderer.lwjgl.render.io.ImageData#getImageBufferData()
   */
  public ByteBuffer getImageBufferData() {
    throw new RuntimeException("ImageIOImageData doesn't store it's image.");
  }
  
  private void copyArea(BufferedImage image, int x, int y, int width, int height, int dx, int dy) {
    Graphics2D g = (Graphics2D) image.getGraphics();
   g.drawImage(image.getSubimage(x, y, width, height), x + dx, y + dy, null);
  }
}
