package de.lessvoid.nifty.render;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import de.lessvoid.nifty.render.spi.RenderDevice;
import de.lessvoid.nifty.render.spi.RenderFont;
import de.lessvoid.nifty.tools.Color;

/**
 * The Nifty RenderEngine.
 * @author void
 */
public class NiftyRenderEngineImpl implements NiftyRenderEngine {

  /**
   * RenderDevice.
   */
  private RenderDevice renderDevice;

  /**
   * global position x.
   */
  private float globalPosX = 0;

  /**
   * global position y.
   */
  private float globalPosY = 0;

  /**
   * current x position.
   */
  private float currentX = 0;

  /**
   * current y position.
   */
  private float currentY = 0;

  /**
   * font.
   */
  private RenderFont font;

  /**
   * current color.
   */
  private Color color;

  /**
   * color changed.
   */
  private boolean colorChanged = false;

  /**
   * current imageScale.
   */
  private float imageScale = 1.0f;

  /**
   * current textScale.
   */
  private float textScale = 1.0f;

  /**
   * font cache.
   */
  private Map < String, RenderFont > fontCache = new Hashtable < String, RenderFont >();

  /**
   * stack to save data.
   */
  private Stack < Set < RenderStateSaver > > stack = new Stack < Set < RenderStateSaver > >();

  /**
   * renderStates mapping.
   */
  private EnumMap < RenderStateType, Class < ? extends RenderStateSaver > > renderStatesMap =
    new EnumMap < RenderStateType, Class < ? extends RenderStateSaver > >(RenderStateType.class);

  /**
   * create the device.
   * @param renderDeviceParam RenderDevice
   */
  public NiftyRenderEngineImpl(final RenderDevice renderDeviceParam) {
    renderDevice = renderDeviceParam;
    renderStatesMap.put(RenderStateType.color, RenderStateColor.class);
    renderStatesMap.put(RenderStateType.imageScale, RenderStateImageScale.class);
    renderStatesMap.put(RenderStateType.position, RenderStatePosition.class);
    renderStatesMap.put(RenderStateType.textSize, RenderStateTextSize.class);
    renderStatesMap.put(RenderStateType.font, RenderStateFont.class);
  }

  /**
   * @see de.lessvoid.nifty.render.NiftyRenderEngine#getWidth()
   * @return width
   */
  public int getWidth() {
    return renderDevice.getWidth();
  }

  /**
   * @see de.lessvoid.nifty.render.NiftyRenderEngine#getHeight()
   * @return height
   */
  public int getHeight() {
    return renderDevice.getHeight();
  }

  /**
   * @see de.lessvoid.nifty.render.NiftyRenderEngine#clear()
   */
  public void clear() {
    renderDevice.clear();
    colorChanged = false;
  }

  /**
   * @see de.lessvoid.nifty.render.NiftyRenderEngine#createImage(java.lang.String, boolean)
   * @param filename name
   * @param filterLinear filter
   * @return NiftyImage
   */
  public NiftyImage createImage(final String filename, final boolean filterLinear) {
    return new NiftyImage(renderDevice.createImage(filename, filterLinear));
  }

  /**
   * @see de.lessvoid.nifty.render.NiftyRenderEngine#createFont(java.lang.String)
   * @param filename name
   * @return RenderFont
   */
  public RenderFont createFont(final String filename) {
    if (fontCache.containsKey(filename)) {
      return fontCache.get(filename);
    } else {
      RenderFont newFont = renderDevice.createFont(filename);
      fontCache.put(filename, newFont);
      return newFont;
    }
  }

  /**
   * @see de.lessvoid.nifty.render.NiftyRenderEngine#renderQuad(int, int, int, int)
   * @param x x
   * @param y y
   * @param width width
   * @param height height
   */
  public void renderQuad(final int x, final int y, final int width, final int height) {
    renderDevice.renderQuad(x + getX(), y + getY(), width, height, color);
  }

  /**
   * renderImage.
   * @param image image
   * @param x x
   * @param y y
   * @param width width
   * @param height height
   */
  public void renderImage(final NiftyImage image, final int x, final int y, final int width, final int height) {
    float alpha = 1.0f;
    if (color != null) {
      alpha = color.getAlpha();
    }
    image.render(x + getX(), y + getY(), width, height, new Color(1.0f, 1.0f, 1.0f, alpha), imageScale);
  }

  /**
   * renderText.
   * @param text text
   * @param x x
   * @param y y
   * @param selectionStart selection start
   * @param selectionEnd selection end
   * @param textSelectionColor textSelectionColor
   */
  public void renderText(
      final String text,
      final int x,
      final int y,
      final int selectionStart,
      final int selectionEnd,
      final Color textSelectionColor) {
    if (isSelection(selectionStart, selectionEnd)) {
      renderSelectionText(
          text, x + getX(), y + getY(), color, textSelectionColor, textScale, selectionStart, selectionEnd);
    } else {
      font.render(text, x + getX(), y + getY(), color, textScale);
    }
  }

  /**
   * Render a Text with some text selected.
   * @param text text
   * @param x x
   * @param y y
   * @param textColor color
   * @param textSelectionColor textSelectionColor
   * @param textSize text size
   * @param selectionStartParam selection start
   * @param selectionEndParam selection end
   */
  protected void renderSelectionText(
      final String text,
      final int x,
      final int y,
      final Color textColor,
      final Color textSelectionColor,
      final float textSize,
      final int selectionStartParam,
      final int selectionEndParam) {
    int selectionStart = selectionStartParam;
    int selectionEnd = selectionEndParam;
    if (selectionStart < 0) {
      selectionStart = 0;
    }
    if (selectionEnd < 0) {
      selectionEnd = 0;
    }

    if (isEverythingSelected(text, selectionStart, selectionEnd)) {
      font.render(text, x, y, textSelectionColor, textSize);
    } else if (isSelectionAtBeginning(selectionStart)) {
      String selectedString = text.substring(selectionStart, selectionEnd);
      String unselectedString = text.substring(selectionEnd);

      font.render(selectedString, x, y, textSelectionColor, textSize);
      font.render(unselectedString, x + font.getWidth(selectedString), y, textColor, textSize);
    } else if (isSelectionAtEnd(text, selectionEnd)) {
      String unselectedString = text.substring(0, selectionStart);
      String selectedString = text.substring(selectionStart, selectionEnd);

      font.render(unselectedString, x, y, textColor, textSize);
      font.render(selectedString, x + font.getWidth(unselectedString), y, textSelectionColor, textSize);
    } else {
      String unselectedString1 = text.substring(0, selectionStart);
      String selectedString = text.substring(selectionStart, selectionEnd);
      String unselectedString2 = text.substring(selectionEnd, text.length());

      font.render(unselectedString1, x, y, textColor, textSize);
      int unselectedString1Len = font.getWidth(unselectedString1);
      font.render(selectedString, x + unselectedString1Len, y, textSelectionColor, textSize);
      int selectedStringLen = font.getWidth(selectedString);
      font.render(unselectedString2, x + unselectedString1Len + selectedStringLen, y, textColor, textSize);
    }
  }

  /**
   * Returns true of selection is at the end of the string.
   * @param text text
   * @param selectionEnd selection end
   * @return true or false
   */
  private boolean isSelectionAtEnd(final String text, final int selectionEnd) {
    return selectionEnd == text.length();
  }

  /**
   * Returns true if selection starts at the beginning.
   * @param selectionStart selection start
   * @return true or false
   */
  private boolean isSelectionAtBeginning(final int selectionStart) {
    return selectionStart == 0;
  }

  /**
   * Returns true when everything is selected.
   * @param text text
   * @param selectionStart selection start
   * @param selectionEnd selection end
   * @return true when everything is selected
   */
  private boolean isEverythingSelected(final String text, final int selectionStart, final int selectionEnd) {
    return isSelectionAtBeginning(selectionStart) && isSelectionAtEnd(text, selectionEnd);
  }

  /**
   * set font.
   * @param newFont font
   */
  public void setFont(final RenderFont newFont) {
    this.font = newFont;
  }

  /**
   * get font.
   * @return font
   */
  public RenderFont getFont() {
    return this.font;
  }

  /**
   * @see de.lessvoid.nifty.render.NiftyRenderEngine#setColor(de.lessvoid.nifty.tools.Color)
   * @param colorParam color
   */
  public void setColor(final Color colorParam) {
    color = colorParam;
    colorChanged = true;
  }

  /**
   * get alpha.
   * @return alpha
   */
  public float getColorAlpha() {
    return color.getAlpha();
  }

  /**
   * @see de.lessvoid.nifty.render.NiftyRenderEngine#isColorChanged()
   * @return color changed
   */
  public boolean isColorChanged() {
    return colorChanged;
  }

  /**
   * @see de.lessvoid.nifty.render.NiftyRenderEngine#moveTo(float, float)
   * @param xParam x
   * @param yParam y
   */
  public void moveTo(final float xParam, final float yParam) {
    this.currentX = xParam;
    this.currentY = yParam;
  }

  /**
   * @see de.lessvoid.nifty.render.NiftyRenderEngine#enableClip(int, int, int, int)
   * @param x0 x0
   * @param y0 y0
   * @param x1 x1
   * @param y1 y1
   */
  public void enableClip(final int x0, final int y0, final int x1, final int y1) {
    renderDevice.enableClip(x0 + getX(), y0 + getY(), x1 + getX(), y1 + getY());
  }

  /**
   * @see de.lessvoid.nifty.render.NiftyRenderEngine#disableClip()
   */
  public void disableClip() {
    renderDevice.disableClip();
  }

  /**
   * @see de.lessvoid.nifty.render.NiftyRenderEngine#setRenderTextSize(float)
   * @param size size
   */
  public void setRenderTextSize(final float size) {
    this.textScale = size;
  }

  /**
   * @see de.lessvoid.nifty.render.NiftyRenderEngine#setImageScale(float)
   * @param scale scale
   */
  public void setImageScale(final float scale) {
    this.imageScale = scale;
  }
  /**
   * @see de.lessvoid.nifty.render.NiftyRenderEngine#setGlobalPosition(float, float)
   * @param xPos x
   * @param yPos y
   */
  public void setGlobalPosition(final float xPos, final float yPos) {
    globalPosX = xPos;
    globalPosY = yPos;
  }

  /**
   * get x.
   * @return x
   */
  private int getX() {
    return (int) (globalPosX + currentX);
  }

  /**
   * get y.
   * @return y
   */
  private int getY() {
    return (int) (globalPosY + currentY);
  }

  /**
   * has selection.
   * @param selectionStart selection start
   * @param selectionEnd selection end
   * @return true or false
   */
  private boolean isSelection(final int selectionStart, final int selectionEnd) {
    return !(selectionStart == -1 && selectionEnd == -1);
  }

  /**
   * @see de.lessvoid.nifty.render.NiftyRenderEngine#saveState(java.util.Set)
   * @param statesToSave states to save
   */
  public void saveState(final Set < RenderStateType > statesToSave) {
    Set < RenderStateSaver > renderStateImpl = new HashSet < RenderStateSaver >();

    for (RenderStateType state : statesToSave) {
      try {
        Class < ? extends RenderStateSaver > clazz = renderStatesMap.get(state);
        renderStateImpl.add(clazz.getConstructor(new Class[] {NiftyRenderEngineImpl.class }).newInstance(this));
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    stack.push(renderStateImpl);
  }

  /**
   * @see de.lessvoid.nifty.render.NiftyRenderEngine#restoreState()
   */
  public void restoreState() {
    Set < RenderStateSaver > renderStateImpl = stack.pop();

    for (RenderStateSaver impl : renderStateImpl) {
      impl.restore();
    }
  }

  /**
   * RenderStatePositionImpl.
   * @author void
   */
  public final class RenderStatePosition implements RenderStateSaver {

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
      this.x = NiftyRenderEngineImpl.this.currentX;
      this.y = NiftyRenderEngineImpl.this.currentY;
    }

    /**
     * restore this state.
     */
    public void restore() {
      NiftyRenderEngineImpl.this.currentX = this.x;
      NiftyRenderEngineImpl.this.currentY = this.y;
    }
  }

  /**
   * RenderStateColor.
   * @author void
   */
  public final class RenderStateColor implements RenderStateSaver {
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
      this.color = NiftyRenderEngineImpl.this.color;
      this.colorChanged = NiftyRenderEngineImpl.this.colorChanged;
    }

    /**
     * restore.
     */
    public void restore() {
      NiftyRenderEngineImpl.this.color = color;
      NiftyRenderEngineImpl.this.colorChanged = colorChanged;
    }
  }
  
  /**
   * RenderStateFont.
   * @author void
   */
  public final class RenderStateFont implements RenderStateSaver {
    /**
     * font.
     */
    private RenderFont font;


    /**
     * save.
     */
    public RenderStateFont() {
      this.font = NiftyRenderEngineImpl.this.font;
    }

    /**
     * restore.
     */
    public void restore() {
      NiftyRenderEngineImpl.this.font = font;
    }
  }

  /**
   * RenderStateTextSize.
   * @author void
   */
  public final class RenderStateTextSize implements RenderStateSaver {

    /**
     * textSize.
     */
    private float textSize;

    /**
     * save.
     */
    public RenderStateTextSize() {
      this.textSize = NiftyRenderEngineImpl.this.textScale;
    }

    /**
     * restore.
     */
    public void restore() {
      NiftyRenderEngineImpl.this.textScale = this.textSize;
    }
  }

  /**
   * RenderStateImageScale.
   * @author void
   */
  public final class RenderStateImageScale implements RenderStateSaver {

    /**
     * imageScale.
     */
    private float imageScale;

    /**
     * save.
     */
    public RenderStateImageScale() {
      this.imageScale = NiftyRenderEngineImpl.this.imageScale;
    }

    /**
     * restore.
     */
    public void restore() {
      NiftyRenderEngineImpl.this.imageScale = this.imageScale;
    }
  }
}
