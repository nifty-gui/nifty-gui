package de.lessvoid.font;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.opengl.ImageData;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureImpl;

/**
 * TextData helper class contains the actual texture and the alpha map.
 * @author void
 */
class TexData {

  /**
   * the logger for this class.
   */
  private static Logger log = Logger.getLogger(Font.class.getName());

  /**
   * the texture.
   */
  private Image tex;

  /**
   * the texture alpha.
   */
  private Texture texAlpha;

  /**
   * construct a new TexData instance from the given texture file.
   * @param texName the textures name to read
   */
  public TexData(final String texName) {
    try {
      tex = new Image(texName, false);

      final ByteBuffer imageBuffer = BufferUtils.createByteBuffer(tex.getWidth() * tex.getHeight());
      byte[] data = tex.getTexture().getTextureData();
      for (int i = 0; i < data.length; i++) {
        imageBuffer.put(i / 4, data[i]);
      }

      ImageData tempData = new ImageData() {
        public int getDepth() {
          return 8;
        }

        public int getHeight() {
          return tex.getHeight();
        }

        public ByteBuffer getImageBufferData() {
          return imageBuffer;
        }

        public int getTexHeight() {
          return tex.getHeight();
        }

        public int getTexWidth() {
          return tex.getWidth();
        }

        public int getWidth() {
          return tex.getWidth();
        }
      };
      texAlpha = createTexture(tempData, GL11.GL_LINEAR);

    } catch (SlickException e) {
      log.log(Level.WARNING, "unable to load: " + texName, e);
    }
  }

  /**
   * get texture.
   * @return the texture.
   */
  public final Image getTex() {
    return tex;
  }

  /**
   * set texture.
   * @param newTex the texture
   */
  public final void setTex(final Image newTex) {
    this.tex = newTex;
  }

  /**
   * get texture alpha.
   * @return alpha texture.
   */
  public final Texture getTexAlpha() {
    return texAlpha;
  }

  /**
   * set texture alpha.
   * @param newTexAlpha the new alpha texture.
   */
  public void setTexAlpha(final Texture newTexAlpha) {
    this.texAlpha = newTexAlpha;
  }

  /**
   * activate the texture.
   * @param useAlphaTexture use alpha = true, or normal texture = false
   */
  public final void activate(final boolean useAlphaTexture) {
    if (useAlphaTexture) {
      texAlpha.bind();
    } else {
      tex.bind();
    }
  }
  
  private Texture createTexture( ImageData dataSource, int filter ) throws SlickException {
    int target = GL11.GL_TEXTURE_2D;
    
    // create the texture ID for this texture 
    int textureID = createTextureID(); 
    TextureImpl texture = new TextureImpl("generated:"+dataSource, target ,textureID); 
    
    int minFilter = filter;
    int magFilter = filter;
    
    // bind this texture 
    GL11.glBindTexture(target, textureID); 

    ByteBuffer textureBuffer;
    int width;
    int height;
    int texWidth;
    int texHeight;
    
  textureBuffer = dataSource.getImageBufferData();
  
  width = dataSource.getWidth();
  height = dataSource.getHeight();
  
  texture.setTextureWidth(dataSource.getTexWidth());
  texture.setTextureHeight(dataSource.getTexHeight());

    texWidth = texture.getTextureWidth();
    texHeight = texture.getTextureHeight();
    
    int srcPixelFormat = GL11.GL_ALPHA;
    int componentCount = 1;
    
    texture.setWidth(width);
    texture.setHeight(height);
     
    IntBuffer temp = BufferUtils.createIntBuffer(16);
    GL11.glGetInteger(GL11.GL_MAX_TEXTURE_SIZE, temp);
    int max = temp.get(0);
    if ((texWidth > max) || (texHeight > max)) {
      throw new SlickException("Attempt to allocate a texture to big for the current hardware");
    }
    
    GL11.glTexParameteri(target, GL11.GL_TEXTURE_MIN_FILTER, minFilter); 
    GL11.glTexParameteri(target, GL11.GL_TEXTURE_MAG_FILTER, magFilter); 
    
    if (minFilter == GL11.GL_LINEAR_MIPMAP_NEAREST) {
      // generate a mip map textur
      GLU.gluBuild2DMipmaps(
          GL11.GL_TEXTURE_2D,
          componentCount, 
          texWidth,
          texHeight,
          srcPixelFormat, 
          GL11.GL_UNSIGNED_BYTE,
          textureBuffer);
    } else {
      // produce a texture from the byte buffer
      GL11.glTexImage2D(target, 
                    0, 
                    GL11.GL_ALPHA, 
                    get2Fold(width), 
                    get2Fold(height), 
                    0, 
                    srcPixelFormat, 
                    GL11.GL_UNSIGNED_BYTE, 
                    textureBuffer); 
    }
    
    return texture; 
  }
  
  private int createTextureID() 
  { 
     IntBuffer tmp = createIntBuffer(1); 
     GL11.glGenTextures(tmp); 
     return tmp.get(0);
  } 

  protected IntBuffer createIntBuffer(int size) {
    ByteBuffer temp = ByteBuffer.allocateDirect(4 * size);
    temp.order(ByteOrder.nativeOrder());

    return temp.asIntBuffer();
  }    

  private int get2Fold(int fold) {
    int ret = 2;
    while (ret < fold) {
        ret *= 2;
    }
    return ret;
} 
  
}
