package de.lessvoid.nifty.render;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import de.lessvoid.nifty.tools.Color;

/**
 * The Nifty RenderEngine.
 * @author void
 */
public class RenderEngine {

  /**
   * RenderDevice.
   */
  private RenderDevice renderDevice;

  private float globalPosX = 0;
  private float globalPosY = 0;
  private float x= 0;
  private float y= 0;
  private Color fontColor;
  private Color color;
  private float imageScale= 1.0f;
  private float textSize= 1.0f;
  private boolean colorChanged = false;

  /**
   * font cache.
   */
  private Map < String, RenderFont > fontCache = new Hashtable < String, RenderFont >();

  /**
   * stack to save data.
   */
  private Stack < Set < RenderStateImpl > > stack = new Stack < Set < RenderStateImpl > >();

  /**
   * renderStates mapping.
   */
  private EnumMap < RenderState, Class < ? extends RenderStateImpl > > renderStatesMap =
    new EnumMap < RenderState, Class < ? extends RenderStateImpl > >(RenderState.class);

  /**
   * create the device.
   * @param renderDeviceParam RenderDevice
   */
  public RenderEngine(final RenderDevice renderDeviceParam) {
    renderDevice = renderDeviceParam;
    renderStatesMap.put(RenderState.color, RenderStateColor.class);
    renderStatesMap.put(RenderState.fontColor, RenderStateFontColor.class);
    renderStatesMap.put(RenderState.imageScale, RenderStateImageScale.class);
    renderStatesMap.put(RenderState.position, RenderStatePosition.class);
    renderStatesMap.put(RenderState.textSize, RenderStateTextSize.class);
  }

  /**
   * Get Width of Display mode.
   * @return width of display mode
   */
  public int getWidth() {
    return renderDevice.getWidth();
  }

  /**
   * Get Height of Display mode.
   * @return height of display mode
   */
  public int getHeight() {
    return renderDevice.getHeight();
  }

  /**
   * Clear the screen.
   */
  public void clear() {
    renderDevice.clear();
    colorChanged = false;
  }

  /**
   * Create a new Image.
   * @param name file name to use
   * @param filterLinear filter
   * @return RenderImage instance
   */
  public RenderImage createImage(final String name, final boolean filterLinear) {
    return renderDevice.createImage(name, filterLinear);
  }

  /**
   * Create a new RenderFont.
   * @param name name of the font
   * @return RenderFont instance
   */
  public RenderFont createFont(final String name) {
    if (fontCache.containsKey(name)) {
      return fontCache.get(name);
    } else {
      RenderFont font = renderDevice.createFont(name);
      fontCache.put(name, font);
      return font;
    }
  }

  /**
   * render a quad.
   * @param x x
   * @param y y
   * @param width width
   * @param height height
   */
  public void renderQuad(final int x, final int y, final int width, final int height) {
    // TODO: apply global position
    renderDevice.renderQuad(x, y, width, height);
  }

  /**
   * Render Image.
   * @param image the image to render
   * @param xParam the x position on the screen
   * @param yParam the y position on the screen
   * @param width the width
   * @param height the height
   */
  public void renderImage(final RenderImage image, final int xParam, final int yParam, final int width, final int height) {
    // moveTo( this.x, this.y );
    // GL11.glTranslatef( xParam+width/2, yParam+height/2, 0.0f );
    // GL11.glScalef( imageScale, imageScale, 1.0f );
    // GL11.glTranslatef( -(xParam+width/2), -(yParam+height/2), 0.0f );
    // testMethod();    
    // RenderImageLwjgl internalImage= (RenderImageLwjgl)image;
    // internalImage.render( xParam, yParam, width, height );
    image.render(xParam, yParam, width, height);
  }

  /**
   * renderText.
   * @param font font
   * @param text text
   * @param xPos x
   * @param yPos y
   */
  public void renderText(final RenderFont font, final String text, final int xPos, final int yPos) {
    if (fontColor != null) {
      font.setColor(1.0f, 1.0f, 1.0f, 1.0f);
    } else {
      font.setDefaultColor();
    }
    font.setSize(textSize);
    font.render(text, xPos, yPos);
  }

  /**
   * Set a new color.
   * @param colorParam new current color to set
   */
  public void setColor(final Color colorParam) {
    // TODO: fixme GL11.glColor4f(colorR, colorG, colorB, colorA);
    color = colorParam;
    colorChanged = true;
  }

  /**
   * return true when color has been changed.
   * @return color changed
   */
  public boolean isColorChanged() {
    return colorChanged;
  }

  /**
   * Move to the given x/y position.
   * @param xParam x
   * @param yParam y
   */
  public void moveTo(final float xParam, final float yParam) {
    // TODO: GL11.glLoadIdentity();
    // TODO: GL11.glTranslatef( x, y, 0.0f );
    this.x = xParam;
    this.y = yParam;
  }

  /**
   * enable alpha blend.
   */
  public void enableBlend() {
    renderDevice.enableBlend();
  }

  /**
   * Enable clipping to the given region.
   * @param x0 x0
   * @param y0 y0
   * @param x1 x1
   * @param y1 y1
   */
  public void enableClip(final int x0, final int y0, final int x1, final int y1) {
    renderDevice.enableClip(x0, y0, x1, y1);
  }

  /**
   * Disable the clipping.
   */
  public void disableClip() {
    renderDevice.disableClip();
  }

  /**
   * Set RenderTextSize.
   * @param size size
   */
  public void setRenderTextSize(final float size) {
    this.textSize = size;
  }

  /**
   * set the font color.
   * @param fontColorParam new font color
   */
  public void setFontColor(final Color fontColorParam) {
    fontColor = fontColorParam;
  }

  /**
   * set image size.
   * @param scale new image size
   */
  public void setImageScale(final float scale) {
    this.imageScale = scale;
  }

  /**
   * set global position.
   * @param xPos x
   * @param yPos y
   */
  public void setGlobalPosition(final float xPos, final float yPos) {
    globalPosX = xPos;
    globalPosY = yPos;
  }

  /**
   * save given states.
   * @param statesToSave set of renderstates to save
   */
  public void saveState(final Set < RenderState > statesToSave) {
    Set < RenderStateImpl > renderStateImpl = new HashSet < RenderStateImpl >();

    for (RenderState state : statesToSave) {
      try {
        Class < ? extends RenderStateImpl > clazz = renderStatesMap.get(state);
        renderStateImpl.add(clazz.getConstructor(new Class[] {RenderEngine.class }).newInstance(this));
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
      this.x = RenderEngine.this.x;
      this.y = RenderEngine.this.y;
    }

    /**
     * restore this state.
     */
    public void restore() {
      RenderEngine.this.x = this.x;
      RenderEngine.this.y = this.y;
    }
  }

  /**
   * RenderStateColor.
   * @author void
   */
  public final class RenderStateColor implements RenderStateImpl {
    /**
     * Color.
     */
    private Color color;

    /**
     * color changed.
     */
    private boolean colorChanged;

    /**
     * save.
     */
    public RenderStateColor() {
      this.color = RenderEngine.this.color;
      this.colorChanged = RenderEngine.this.colorChanged;
    }

    /**
     * restore.
     */
    public void restore() {
      RenderEngine.this.color = color;
      RenderEngine.this.colorChanged = colorChanged;
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
      this.textSize = RenderEngine.this.textSize;
    }

    /**
     * restore.
     */
    public void restore() {
      RenderEngine.this.textSize = this.textSize;
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
      this.fontColor = RenderEngine.this.fontColor;
    }

    /**
     * restore.
     */
    public void restore() {
      RenderEngine.this.fontColor = this.fontColor;
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
      this.imageScale = RenderEngine.this.imageScale;
    }

    /**
     * restore.
     */
    public void restore() {
      RenderEngine.this.imageScale = this.imageScale;
    }
  }

}
