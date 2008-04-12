package de.lessvoid.nifty.render.opengl;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.logging.Logger;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import de.lessvoid.nifty.render.RenderDevice;
import de.lessvoid.nifty.render.RenderFont;
import de.lessvoid.nifty.render.RenderImage;
import de.lessvoid.nifty.render.RenderState;
import de.lessvoid.nifty.tools.Color;

public class RenderDeviceLwjgl implements RenderDevice {

  private final static Logger log = Logger.getLogger(RenderDeviceLwjgl.class.getName());

  private float globalPosX = 0;
  private float globalPosY = 0;
  private float moveX= 0;
  private float moveY= 0;
  private Color fontColor;
  private float imageScale= 1.0f;
  private float textSize= 1.0f;

  private static ByteBuffer byteBuffer = BufferUtils.createByteBuffer(1024);
  private static DoubleBuffer doubleBuffer = byteBuffer.asDoubleBuffer();

  /**
   * font cache.
   */
  private Map < String, RenderFont > fontCache= new Hashtable < String, RenderFont > ();

  /**
   * stack to save data.
   */
  private Stack < Set < RenderStateImpl > > stack = new Stack < Set < RenderStateImpl > >();

  /**
   * renderStates mapping.
   */
  private EnumMap < RenderState, Class < ? extends RenderStateImpl > > renderStatesMap =
    new EnumMap < RenderState, Class < ? extends RenderStateImpl > > (RenderState.class);

  /**
   * create the device.
   */
  public RenderDeviceLwjgl() {
    renderStatesMap.put(RenderState.enable, RenderStateEnable.class);
    renderStatesMap.put(RenderState.fontColor, RenderStateFontColor.class);
    renderStatesMap.put(RenderState.imageScale, RenderStateImageScale.class);
    renderStatesMap.put(RenderState.position, RenderStatePosition.class);
    renderStatesMap.put(RenderState.textSize, RenderStateTextSize.class);
  }


  
  public int getHeight() {
    return Display.getDisplayMode().getHeight();
  }

  public int getWidth() {
    return Display.getDisplayMode().getWidth();
  }
  
  private void testMethod() {
    GL11.glTranslatef(globalPosX, globalPosY, 0.0f);
  }

  /**
   * Render Image.
   * @param image the image to render
   * @param x the x position on the screen
   * @param y the y position on the screen
   * @param width the width
   * @param height the height
   */
  public void renderImage(final RenderImage image, final int x, final int y, final int width, final int height) {
    moveTo( this.moveX, this.moveY );
    GL11.glTranslatef( x+width/2, y+height/2, 0.0f );
    GL11.glScalef( imageScale, imageScale, 1.0f );
    GL11.glTranslatef( -(x+width/2), -(y+height/2), 0.0f );
    testMethod();    
    RenderImageLwjgl internalImage= (RenderImageLwjgl)image;
    internalImage.render( x, y, width, height );
  }

  /**
   * Render a Part of an Image.
   * @param image the image to render
   * @param x the x position on the screen
   * @param y the y position on the screen
   * @param w the width
   * @param h the height
   * @param srcX x position in image to copy from
   * @param srcY y position in image to copy from
   * @param srcW width in image to copy from
   * @param srcH heightin image to coopy from
   */
  public void renderImagePart(
      final RenderImage image,
      final int x,
      final int y,
      final int w,
      final int h,
      final int srcX,
      final int srcY,
      final int srcW,
      final int srcH) {
    moveTo(this.moveX, this.moveY);
    GL11.glTranslatef(x + w / 2, y + h / 2, 0.0f);
    GL11.glScalef(imageScale, imageScale, 1.0f);
    GL11.glTranslatef(-(x + w / 2), -(y + h / 2), 0.0f);
    testMethod();
    RenderImageLwjgl internalImage = (RenderImageLwjgl) image;
    internalImage.render(x, y, w, h, srcX, srcY, srcW, srcH);
  }

  public void setColor( float colorR, float colorG, float colorB, float colorA ) {
    GL11.glColor4f( colorR, colorG, colorB, colorA );  
  }

  public void renderQuad( int x, int y, int width, int height ) {
    testMethod();
    GL11.glBegin( GL11.GL_QUADS );
    
      GL11.glVertex2i( x,         y );
      GL11.glVertex2i( x + width, y );
      GL11.glVertex2i( x + width, y + height );
      GL11.glVertex2i( x,         y + height );

    GL11.glEnd();
  }

  /**
   * Create a new Image.
   * @param name file name to use
   * @param filter filter
   * @return RenderImage instance
   */
  public RenderImage createImage(final String name, final boolean filter) {
    return new RenderImageLwjgl(name, filter);
  }

  public void moveTo( float x, float y ) {
    GL11.glLoadIdentity();
    GL11.glTranslatef( x, y, 0.0f );
    
    this.moveX= x;
    this.moveY= y;
  }


  public void clear() {
    GL11.glClearColor( 0.0f, 0.0f, 0.0f, 0.0f );
    GL11.glClear( GL11.GL_COLOR_BUFFER_BIT );
  }
  
  public RenderFont createFont( String name ) {
    if( fontCache.containsKey( name )) {
      return fontCache.get( name );
    } else {
      RenderFont font= new RenderFontLwjgl( name );
      fontCache.put( name, font );
      return font;
    }
    
  }
  
  public void renderText( RenderFont font, String text, int x, int y ) {
    RenderFontLwjgl internalFont= (RenderFontLwjgl)font;
    if( fontColor == null ) {
      internalFont.renderWithSize( text, x, y, textSize );  
    } else {
      internalFont.renderWithSizeAndColor( text, x, y, textSize, fontColor.getRed(), fontColor.getGreen(), fontColor.getBlue(), fontColor.getAlpha() );
    }
  }

  public void setRenderTextSize( float size ) {
    this.textSize= size;
  }
  
  public float getMoveToX() {
    return moveX;
  }
  
  public float getMoveToY() {
    return moveY;
  }

  public void disableTexture() {
    GL11.glDisable( GL11.GL_TEXTURE_2D );
  }

  public void enableBlend() {
    GL11.glEnable( GL11.GL_BLEND );
    GL11.glBlendFunc( GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA );
  }

  /**
   * set the font color.
   */
  public void setFontColor(final float r, final float g, final float b, final float a) {
    fontColor = new Color(r, g, b, a);
  }

  public void setImageScale( float scale ) {
    this.imageScale= scale;
  }
  
  public void setGlobalPosition(float x, float y) {
    globalPosX = x;
    globalPosY = y;
  }

  /**
   * save given states.
   * @param statesToSave set of renderstates to save
   */
  public void saveState(final Set < RenderState > statesToSave) {
    Set < RenderStateImpl > renderStateImpl = new HashSet < RenderStateImpl > ();

    for (RenderState state : statesToSave) {
      try {
        Class < ? extends RenderStateImpl > clazz = renderStatesMap.get(state);
        renderStateImpl.add(clazz.getConstructor(new Class[] {RenderDeviceLwjgl.class }).newInstance(this));
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    stack.push(renderStateImpl);
  }

  /**
   * restore states.
   */
  public void restoreState() {
    Set < RenderStateImpl > renderStateImpl = stack.pop();

    for (RenderStateImpl impl : renderStateImpl) {
      impl.restore();
    }
  }

  /**
   * RenderStatePositionImpl.
   * @author void
   */
  public final class RenderStatePosition implements RenderStateImpl {

    /**
     * saved x.
     */
    private float x;

    /**
     * saved y.
     */
    private float y;

    /**
     * store this state.
     */
    public RenderStatePosition() {
      GL11.glPushMatrix();
      this.x = RenderDeviceLwjgl.this.moveX;
      this.y = RenderDeviceLwjgl.this.moveY;
    }

    /**
     * restore this state.
     */
    public void restore() {
      GL11.glPopMatrix();
      RenderDeviceLwjgl.this.moveX = this.x;
      RenderDeviceLwjgl.this.moveY = this.y;
    }
  }

  /**
   * RenderStateEnable.
   * @author void
   */
  public final class RenderStateEnable implements RenderStateImpl {

    /**
     * save.
     */
    public RenderStateEnable() {
      GL11.glPushAttrib(GL11.GL_CURRENT_BIT | GL11.GL_ENABLE_BIT);
    }

    /**
     * restore.
     */
    public void restore() {
      GL11.glPopAttrib();
    }
  }

  /**
   * RenderStateTextSize.
   * @author void
   */
  public final class RenderStateTextSize implements RenderStateImpl {

    /**
     * textSize.
     */
    private float textSize;

    /**
     * save.
     */
    public RenderStateTextSize() {
      this.textSize = RenderDeviceLwjgl.this.textSize;
    }

    /**
     * restore.
     */
    public void restore() {
      RenderDeviceLwjgl.this.textSize = this.textSize;
    }
  }

  /**
   * RenderStateFontColor.
   * @author void
   */
  public final class RenderStateFontColor implements RenderStateImpl {

    /**
     * fontColor.
     */
    private Color fontColor;

    /**
     * save.
     */
    public RenderStateFontColor() {
      this.fontColor = RenderDeviceLwjgl.this.fontColor;
    }

    /**
     * restore.
     */
    public void restore() {
      RenderDeviceLwjgl.this.fontColor = this.fontColor;
    }
  }

  /**
   * RenderStateImageScale.
   * @author void
   */
  public final class RenderStateImageScale implements RenderStateImpl {

    /**
     * imageScale.
     */
    private float imageScale;

    /**
     * save.
     */
    public RenderStateImageScale() {
      this.imageScale = RenderDeviceLwjgl.this.imageScale;
    }

    /**
     * restore.
     */
    public void restore() {
      RenderDeviceLwjgl.this.imageScale = this.imageScale;
    }
  }

  /**
   * Disable the clipping.
   */
  public void disableClip() {
    GL11.glDisable(GL11.GL_CLIP_PLANE0);
    GL11.glDisable(GL11.GL_CLIP_PLANE1);
    GL11.glDisable(GL11.GL_CLIP_PLANE2);
    GL11.glDisable(GL11.GL_CLIP_PLANE3);
  }

  /**
   * Enable clipping to the given region.
   * @param x0 x0
   * @param y0 y0
   * @param x1 x1
   * @param y1 y1
   */
  public void enableClip(final int x0, final int y0, final int x1, final int y1) {
    GL11.glEnable(GL11.GL_CLIP_PLANE0);
    GL11.glEnable(GL11.GL_CLIP_PLANE1);
    GL11.glEnable(GL11.GL_CLIP_PLANE2);
    GL11.glEnable(GL11.GL_CLIP_PLANE3);

    doubleBuffer.clear();
    doubleBuffer.put(1).put(0).put(0).put(-x0).flip();
    GL11.glClipPlane(GL11.GL_CLIP_PLANE0, doubleBuffer);
    doubleBuffer.clear();
    doubleBuffer.put(-1).put(0).put(0).put(x1).flip();
    GL11.glClipPlane(GL11.GL_CLIP_PLANE1, doubleBuffer);
    doubleBuffer.clear();
    doubleBuffer.put(0).put(1).put(0).put(-y0).flip();
    GL11.glClipPlane(GL11.GL_CLIP_PLANE2, doubleBuffer);
    doubleBuffer.clear();
    doubleBuffer.put(0).put(-1).put(0).put(y1).flip();
    GL11.glClipPlane(GL11.GL_CLIP_PLANE3, doubleBuffer);
  }
}
