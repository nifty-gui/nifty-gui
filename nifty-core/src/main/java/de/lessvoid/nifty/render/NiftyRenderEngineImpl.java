package de.lessvoid.nifty.render;

import de.lessvoid.nifty.NiftyStopwatch;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.spi.render.RenderDevice;
import de.lessvoid.nifty.spi.render.RenderFont;
import de.lessvoid.nifty.spi.render.RenderImage;
import de.lessvoid.nifty.tools.Color;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.logging.Logger;

/**
 * This is the default implementation of the render engine.
 *
 * @author void
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class NiftyRenderEngineImpl implements NiftyRenderEngine {
  @Nonnull
  private static final Logger log = Logger.getLogger(NiftyRenderEngineImpl.class.getName());

  /**
   * RenderDevice.
   */
  @Nonnull
  private final RenderDevice renderDevice;

  /**
   * Display width and height. This is always the base resolution (when scaling is enabled).
   */
  private int displayWidth;
  private int displayHeight;

  /**
   * This is always the native display resolution.
   */
  private int nativeDisplayWidth;
  private int nativeDisplayHeight;
  private boolean autoScaling = false;
  @Nullable
  private Float autoScalingScaleX = null;
  @Nullable
  private Float autoScalingScaleY = null;
  private float autoScalingOffsetX = 0;
  private float autoScalingOffsetY = 0;

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
  @Nullable
  private RenderFont font;

  /**
   * current color.
   */
  @Nonnull
  private final Color color = new Color(1.0f, 1.0f, 1.0f, 1.0f);

  /**
   * color changed.
   */
  private boolean colorChanged = false;

  /**
   * color alpha changed.
   */
  private boolean colorAlphaChanged = false;

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
  @Nonnull
  private final Map<String, RenderFont> fontCache = new HashMap<String, RenderFont>();

  /**
   * stack to save data.
   */
  @Nonnull
  private final Deque<SavedRenderState> stack = new ArrayDeque<SavedRenderState>(20);
  @Nonnull
  private final Color whiteColor = new Color("#ffff");

  private boolean clipEnabled;
  @Nonnull
  private final Clip clip = new Clip(0, 0, 0, 0);
  @Nonnull
  private BlendMode blendMode = BlendMode.BLEND;
  @Nonnull
  private final NiftyImageManager imageManager;

  /**
   * create the device.
   *
   * @param renderDeviceParam RenderDevice
   */
  public NiftyRenderEngineImpl(@Nonnull final RenderDevice renderDeviceParam) {
    renderDevice = new ScalingRenderDevice(this, renderDeviceParam);
    displayWidth = renderDevice.getWidth();
    displayHeight = renderDevice.getHeight();
    nativeDisplayWidth = renderDevice.getWidth();
    nativeDisplayHeight = renderDevice.getHeight();
    imageManager = new NiftyImageManager(renderDeviceParam);
  }

  @Override
  public int getWidth() {
    return displayWidth;
  }

  @Override
  public int getHeight() {
    return displayHeight;
  }

  @Override
  public void beginFrame() {
    renderDevice.beginFrame();
    colorChanged = false;
  }

  @Override
  public void endFrame() {
    renderDevice.endFrame();
  }

  @Override
  public void clear() {
    renderDevice.clear();
  }

  @Nullable
  @Override
  public NiftyImage createImage(
      @Nonnull final Screen screen,
      @Nonnull final String filename,
      final boolean filterLinear) {
    final RenderImage image = imageManager.registerImage(filename, filterLinear, screen);
    if (image == null) {
      return null;
    }
    return new NiftyImage(this, image);
  }

  @Override
  @Nullable
  public RenderFont createFont(@Nonnull final String filename) {
    if (fontCache.containsKey(filename)) {
      return fontCache.get(filename);
    } else {
      NiftyStopwatch.start();
      RenderFont newFont = renderDevice.createFont(filename);
      fontCache.put(filename, newFont);
      NiftyStopwatch.stop("RenderDevice.createFont(" + filename + ")");
      return newFont;
    }
  }

  @Nonnull
  @Override
  public String getFontname(@Nonnull final RenderFont font) {
    for (Map.Entry<String, RenderFont> entry : fontCache.entrySet()) {
      if (entry.getValue().equals(font)) {
        return entry.getKey();
      }
    }
    throw new IllegalArgumentException("Font not found in storage of this render engine. Maybe it was load by another" +
        " engine?");
  }

  @Override
  public void renderQuad(final int x, final int y, final int width, final int height) {
    renderDevice.renderQuad(x + getX(), y + getY(), width, height, color);
  }

  @Override
  public void renderQuad(
      final int x,
      final int y,
      final int width,
      final int height,
      @Nonnull final Color topLeft,
      @Nonnull final Color topRight,
      @Nonnull final Color bottomRight,
      @Nonnull final Color bottomLeft) {
    if (isColorAlphaChanged()) {
      Color a = new Color(topLeft, color.getAlpha());
      Color b = new Color(topRight, color.getAlpha());
      Color c = new Color(bottomRight, color.getAlpha());
      Color d = new Color(bottomLeft, color.getAlpha());
      renderDevice.renderQuad(x + getX(), y + getY(), width, height, a, b, c, d);
    } else {
      renderDevice.renderQuad(x + getX(), y + getY(), width, height, topLeft, topRight, bottomRight, bottomLeft);
    }
  }

  /**
   * renderImage.
   *
   * @param image  image
   * @param x      x
   * @param y      y
   * @param width  width
   * @param height height
   */
  @Override
  public void renderImage(
      @Nonnull final NiftyImage image,
      final int x,
      final int y,
      final int width,
      final int height) {
    Color c = image.getColor();
    if (c == null) {
      float alpha = color.getAlpha();
      whiteColor.setAlpha(alpha);
      c = whiteColor;
    }
    image.render(x + getX(), y + getY(), width, height, c, imageScale);
  }

  /**
   * renderText.
   *
   * @param text               text
   * @param x                  x
   * @param y                  y
   * @param selectionStart     selection start
   * @param selectionEnd       selection end
   * @param textSelectionColor textSelectionColor
   */
  @Override
  public void renderText(
      @Nonnull final String text,
      final int x,
      final int y,
      final int selectionStart,
      final int selectionEnd,
      @Nonnull final Color textSelectionColor) {
    if (isSelection(selectionStart, selectionEnd)) {
      renderSelectionText(
          text, x + getX(), y + getY(), color, textSelectionColor, textScale, textScale, selectionStart, selectionEnd);
    } else {
      if (font == null) {
        log.warning("missing font in renderText! could it be that you're using <text> elements without a font or " +
            "style attribute? in case you've replaced <label> with <text> you're probably missing style='nifty-label'" +
            " :)");
        return;
      }
      renderDevice.renderFont(font, text, x + getX(), y + getY(), color, textScale, textScale);
    }
  }

  /**
   * Render a Text with some text selected.
   *
   * @param text                text
   * @param x                   x
   * @param y                   y
   * @param textColor           color
   * @param textSelectionColor  textSelectionColor
   * @param textSizeX           text size
   * @param textSizeY           text size
   * @param selectionStartParam selection start
   * @param selectionEndParam   selection end
   */
  protected void renderSelectionText(
      @Nonnull final String text,
      final int x,
      final int y,
      @Nonnull final Color textColor,
      @Nonnull final Color textSelectionColor,
      final float textSizeX,
      final float textSizeY,
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
      renderDevice.renderFont(font, text, x, y, textSelectionColor, textSizeX, textSizeY);
    } else if (isSelectionAtBeginning(selectionStart)) {
      String selectedString = text.substring(selectionStart, selectionEnd);
      String unselectedString = text.substring(selectionEnd);

      renderDevice.renderFont(font, selectedString, x, y, textSelectionColor, textSizeX, textSizeY);
      renderDevice.renderFont(font, unselectedString, x + font.getWidth(selectedString), y, textColor, textSizeX,
          textSizeY);
    } else if (isSelectionAtEnd(text, selectionEnd)) {
      String unselectedString = text.substring(0, selectionStart);
      String selectedString = text.substring(selectionStart, selectionEnd);

      renderDevice.renderFont(font, unselectedString, x, y, textColor, textSizeX, textSizeY);
      renderDevice.renderFont(font, selectedString, x + font.getWidth(unselectedString), y, textSelectionColor,
          textSizeX, textSizeY);
    } else {
      String unselectedString1 = text.substring(0, selectionStart);
      String selectedString = text.substring(selectionStart, selectionEnd);
      String unselectedString2 = text.substring(selectionEnd, text.length());

      renderDevice.renderFont(font, unselectedString1, x, y, textColor, textSizeX, textSizeY);
      int unselectedString1Len = font.getWidth(unselectedString1);
      renderDevice.renderFont(font, selectedString, x + unselectedString1Len, y, textSelectionColor, textSizeX,
          textSizeY);
      int selectedStringLen = font.getWidth(selectedString);
      renderDevice.renderFont(font, unselectedString2, x + unselectedString1Len + selectedStringLen, y, textColor,
          textSizeX, textSizeY);
    }
  }

  /**
   * Returns true of selection is at the end of the string.
   *
   * @param text         text
   * @param selectionEnd selection end
   * @return true or false
   */
  private boolean isSelectionAtEnd(@Nonnull final String text, final int selectionEnd) {
    return selectionEnd == text.length();
  }

  /**
   * Returns true if selection starts at the beginning.
   *
   * @param selectionStart selection start
   * @return true or false
   */
  private boolean isSelectionAtBeginning(final int selectionStart) {
    return selectionStart == 0;
  }

  /**
   * Returns true when everything is selected.
   *
   * @param text           text
   * @param selectionStart selection start
   * @param selectionEnd   selection end
   * @return true when everything is selected
   */
  private boolean isEverythingSelected(@Nonnull final String text, final int selectionStart, final int selectionEnd) {
    return isSelectionAtBeginning(selectionStart) && isSelectionAtEnd(text, selectionEnd);
  }

  /**
   * set font.
   *
   * @param newFont font
   */
  @Override
  public void setFont(@Nullable final RenderFont newFont) {
    this.font = newFont;
  }

  /**
   * get font.
   *
   * @return font
   */
  @Override
  public RenderFont getFont() {
    return this.font;
  }

  @Override
  public void setColor(@Nonnull final Color colorParam) {
    color.setRed(colorParam.getRed());
    color.setGreen(colorParam.getGreen());
    color.setBlue(colorParam.getBlue());
    color.setAlpha(colorParam.getAlpha());
    colorChanged = true;
    colorAlphaChanged = true;
  }

  /**
   * set only the color alpha.
   *
   * @param newColorAlpha new alpha value
   */
  @Override
  public void setColorAlpha(final float newColorAlpha) {
    color.setAlpha(newColorAlpha);
    colorAlphaChanged = true;
  }

  /**
   * Set only the color component of the given color. This assumes that alpha has already been changed.
   *
   * @param newColor color
   */
  @Override
  public void setColorIgnoreAlpha(@Nonnull final Color newColor) {
    color.setRed(newColor.getRed());
    color.setGreen(newColor.getGreen());
    color.setBlue(newColor.getBlue());
    colorChanged = true;

    if (colorAlphaChanged && color.getAlpha() > newColor.getAlpha()) {
      color.setAlpha(newColor.getAlpha());
      colorAlphaChanged = true;
    }
  }

  /**
   * return true when color has been changed.
   *
   * @return color changed
   */
  @Override
  public boolean isColorChanged() {
    return colorChanged;
  }

  @Override
  public boolean isColorAlphaChanged() {
    return colorAlphaChanged;
  }

  @Override
  public void moveTo(final float xParam, final float yParam) {
    this.currentX = xParam;
    this.currentY = yParam;
  }

  @Override
  public void enableClip(final int cx0, final int cy0, final int cx1, final int cy1) {
    // Issue #138:
    // In case there already is a clipping area we change it to the intersection of the existing with the requested new
    // one. This way you can further restrict a clipping rectangle but can't override (e.g. make it bigger).
    int x0 = cx0 + getX();
    int y0 = cy0 + getY();
    int x1 = cx1 + getX();
    int y1 = cy1 + getY();

    if (clipEnabled) {
      // if the new clip rectangle is completely outside the current clipping area we don't modify the existing one
      if (isOutsideClippingRectangle(x0, y0, x1, y1)) {
        return;
      }

      // if the new clip rectangle is completely inside the current clipping area we can use it as-is directly
      if (isInsideClippingRectangle(x0, y0, x1, y1)) {
        updateClip(true, x0, y0, x1, y1);
        return;
      }

      // we need to clip
      int newX0 = Math.max(x0, clip.x0);
      int newY0 = Math.max(y0, clip.y0);
      int newX1 = Math.min(x1, clip.x1);
      int newY1 = Math.min(y1, clip.y1);

      updateClip(true, newX0, newY0, newX1, newY1);
      return;
    }
    updateClip(true, x0, y0, x1, y1);
  }

  @Override
  public void disableClip() {
    updateClip(false, 0, 0, 0, 0);
  }

  void updateClip(final boolean enabled, final int x0, final int y0, final int x1, final int y1) {
    clipEnabled = enabled;
    clip.init(x0, y0, x1, y1);
    if (!clipEnabled) {
      renderDevice.disableClip();
    } else {
      clip.apply();
    }
  }

  @Override
  public void setRenderTextSize(final float size) {
    this.textScale = size;
  }

  @Override
  public void setImageScale(final float scale) {
    this.imageScale = scale;
  }


  @Override
  public void setGlobalPosition(final float xPos, final float yPos) {
    globalPosX = xPos;
    globalPosY = yPos;
  }

  @Override
  public void displayResolutionChanged() {
    if (!autoScaling) {
      displayWidth = renderDevice.getWidth();
      displayHeight = renderDevice.getHeight();
    }
    nativeDisplayWidth = renderDevice.getWidth();
    nativeDisplayHeight = renderDevice.getHeight();
  }

  /**
   * get x.
   *
   * @return x
   */
  private int getX() {
    return (int) (globalPosX + currentX);
  }

  /**
   * get y.
   *
   * @return y
   */
  private int getY() {
    return (int) (globalPosY + currentY);
  }

  /**
   * has selection.
   *
   * @param selectionStart selection start
   * @param selectionEnd   selection end
   * @return true or false
   */
  private boolean isSelection(final int selectionStart, final int selectionEnd) {
    return !(selectionStart == -1 && selectionEnd == -1);
  }

  @Override
  public void saveState(@Nullable final RenderStates statesToSave) {
    SavedRenderState savedRenderState = new SavedRenderState();
    savedRenderState.save(statesToSave);
    stack.push(savedRenderState);
  }

  @Override
  public void restoreState() {
    SavedRenderState restored = stack.pop();
    restored.restore();
  }

  @Override
  public void setBlendMode(@Nonnull final BlendMode blendModeParam) {
    blendMode = blendModeParam;
    renderDevice.setBlendMode(blendModeParam);
  }

  @Override
  @Nonnull
  public RenderDevice getRenderDevice() {
    return renderDevice;
  }

  @Override
  public void disposeImage(@Nonnull final RenderImage image) {
    imageManager.unregisterImage(image);
  }

  @Override
  @Nonnull
  public RenderImage reload(@Nonnull final RenderImage image) {
    return imageManager.reload(image);
  }

  private class SavedRenderState {
    private float x;
    private float y;
    private boolean statePositionChanged;

    private float colorR;
    private float colorG;
    private float colorB;
    private boolean colorChanged;
    private boolean stateColorChanged;

    private float colorAlpha;
    private boolean colorAlphaChanged;
    private boolean stateAlphaChanged;

    @Nullable
    private RenderFont font;
    private boolean stateFontChanged;

    private float textSize;
    private boolean stateTextSizeChanged;

    private float imageScale;
    private boolean stateImageScaleChanged;

    private boolean clipEnabled;
    @Nonnull
    private final Clip clip = new Clip(0, 0, 0, 0);
    private boolean stateClipChanged;

    private BlendMode blendMode;
    private boolean stateBlendModeChanged;

    private boolean restoreAll = false;

    public SavedRenderState() {
    }

    public void save(@Nullable final RenderStates statesToSave) {
      statePositionChanged = false;
      stateColorChanged = false;
      stateAlphaChanged = false;
      stateFontChanged = false;
      stateTextSizeChanged = false;
      stateImageScaleChanged = false;
      stateClipChanged = false;
      stateBlendModeChanged = false;
      restoreAll = false;

      if (statesToSave == null) {
        savePosition();
        saveColor();
        saveColorAlpha();
        saveTextSize();
        saveImageSize();
        saveFont();
        saveClipEnabled();
        saveBlendMode();
        restoreAll = true;
        return;
      }
      if (statesToSave.hasPosition()) {
        savePosition();
      } else if (statesToSave.hasColor()) {
        saveColor();
      } else if (statesToSave.hasAlpha()) {
        saveColorAlpha();
      } else if (statesToSave.hasTextSize()) {
        saveTextSize();
      } else if (statesToSave.hasImageScale()) {
        saveImageSize();
      } else if (statesToSave.hasFont()) {
        saveFont();
      } else if (statesToSave.hasClip()) {
        saveClipEnabled();
      } else if (statesToSave.hasBlendMode()) {
        saveBlendMode();
      }
    }

    public void restore() {
      if (restoreAll) {
        restorePosition();
        restoreColor();
        restoreAlpha();
        restoreFont();
        restoreTextSize();
        restoreImageScale();
        restoreClip();
        restoreBlend();
        return;
      }
      if (statePositionChanged) {
        restorePosition();
      }
      if (stateColorChanged) {
        restoreColor();
      }
      if (stateAlphaChanged) {
        restoreAlpha();
      }
      if (stateFontChanged) {
        restoreFont();
      }
      if (stateTextSizeChanged) {
        restoreTextSize();
      }
      if (stateImageScaleChanged) {
        restoreImageScale();
      }
      if (stateClipChanged) {
        restoreClip();
      }
      if (stateBlendModeChanged) {
        restoreBlend();
      }
    }

    private void saveBlendMode() {
      blendMode = NiftyRenderEngineImpl.this.blendMode;
      stateBlendModeChanged = true;
    }

    private void saveClipEnabled() {
      clipEnabled = NiftyRenderEngineImpl.this.clipEnabled;
      clip.init(NiftyRenderEngineImpl.this.clip.x0, NiftyRenderEngineImpl.this.clip.y0,
          NiftyRenderEngineImpl.this.clip.x1, NiftyRenderEngineImpl.this.clip.y1);
      stateClipChanged = true;
    }

    private void saveFont() {
      font = NiftyRenderEngineImpl.this.font;
      stateFontChanged = true;
    }

    private void saveImageSize() {
      imageScale = NiftyRenderEngineImpl.this.imageScale;
      stateImageScaleChanged = true;
    }

    private void saveTextSize() {
      textSize = NiftyRenderEngineImpl.this.textScale;
      stateTextSizeChanged = true;
    }

    private void saveColorAlpha() {
      colorAlpha = NiftyRenderEngineImpl.this.color.getAlpha();
      colorAlphaChanged = NiftyRenderEngineImpl.this.colorAlphaChanged;
      stateAlphaChanged = true;
    }

    private void saveColor() {
      colorR = NiftyRenderEngineImpl.this.color.getRed();
      colorG = NiftyRenderEngineImpl.this.color.getGreen();
      colorB = NiftyRenderEngineImpl.this.color.getBlue();
      colorChanged = NiftyRenderEngineImpl.this.colorChanged;
      stateColorChanged = true;
    }

    private void savePosition() {
      x = NiftyRenderEngineImpl.this.currentX;
      y = NiftyRenderEngineImpl.this.currentY;
      statePositionChanged = true;
    }

    private void restoreBlend() {
      NiftyRenderEngineImpl.this.setBlendMode(blendMode);
    }

    private void restoreClip() {
      NiftyRenderEngineImpl.this.updateClip(clipEnabled, clip.x0, clip.y0, clip.x1, clip.y1);
    }

    private void restoreImageScale() {
      NiftyRenderEngineImpl.this.imageScale = this.imageScale;
    }

    private void restoreTextSize() {
      NiftyRenderEngineImpl.this.textScale = this.textSize;
    }

    private void restoreFont() {
      NiftyRenderEngineImpl.this.font = font;
    }

    private void restoreAlpha() {
      NiftyRenderEngineImpl.this.color.setAlpha(colorAlpha);
      NiftyRenderEngineImpl.this.colorAlphaChanged = colorAlphaChanged;
    }

    private void restoreColor() {
      NiftyRenderEngineImpl.this.color.setRed(colorR);
      NiftyRenderEngineImpl.this.color.setGreen(colorG);
      NiftyRenderEngineImpl.this.color.setBlue(colorB);
      NiftyRenderEngineImpl.this.colorChanged = colorChanged;
    }

    private void restorePosition() {
      NiftyRenderEngineImpl.this.currentX = this.x;
      NiftyRenderEngineImpl.this.currentY = this.y;
    }
  }

  public class Clip {
    private int x0;
    private int y0;
    private int x1;
    private int y1;

    public Clip(final int x0, final int y0, final int x1, final int y1) {
      init(x0, y0, x1, y1);
    }

    public void init(final int x0, final int y0, final int x1, final int y1) {
      this.x0 = x0;
      this.y0 = y0;
      this.x1 = x1;
      this.y1 = y1;
    }

    public void apply() {
      renderDevice.enableClip(x0, y0, x1, y1);
    }
  }

  @Override
  public int getNativeWidth() {
    return nativeDisplayWidth;
  }

  @Override
  public int getNativeHeight() {
    return nativeDisplayHeight;
  }

  @Override
  public int convertToNativeX(final int x) {
    return (int) Math.floor(x * getScaleX() + autoScalingOffsetX);
  }

  @Override
  public int convertToNativeY(final int y) {
    return (int) Math.floor(y * getScaleY() + autoScalingOffsetY);
  }

  @Override
  public int convertToNativeWidth(final int x) {
    return (int) Math.ceil(x * getScaleX());
  }

  @Override
  public int convertToNativeHeight(final int y) {
    return (int) Math.ceil(y * getScaleY());
  }

  @Override
  public int convertFromNativeX(final int x) {
    return (int) Math.ceil((x - autoScalingOffsetX) * (1.0f / getScaleX()));
  }

  @Override
  public int convertFromNativeY(final int y) {
    return (int) Math.ceil((y - autoScalingOffsetY) * (1.0f / getScaleY()));
  }

  @Override
  public float convertToNativeTextSizeX(final float size) {
    return size * getScaleX();
  }

  @Override
  public float convertToNativeTextSizeY(final float size) {
    return size * getScaleY();
  }

  private float getScaleX() {
    if (autoScalingScaleX != null) {
      return autoScalingScaleX;
    }
    return (float) getNativeWidth() / getWidth();
  }

  private float getScaleY() {
    if (autoScalingScaleY != null) {
      return autoScalingScaleY;
    }
    return (float) getNativeHeight() / getHeight();
  }

  @Override
  public void enableAutoScaling(final int baseResolutionX, final int baseResolutionY) {
    autoScaling = true;
    displayWidth = baseResolutionX;
    displayHeight = baseResolutionY;
    autoScalingScaleX = null;
    autoScalingScaleY = null;
    autoScalingOffsetX = 0;
    autoScalingOffsetY = 0;
  }

  @Override
  public void enableAutoScaling(
      final int baseResolutionX,
      final int baseResolutionY,
      final float scaleX,
      final float scaleY) {
    autoScaling = true;
    displayWidth = baseResolutionX;
    displayHeight = baseResolutionY;
    autoScalingScaleX = ((float) getNativeWidth() / getWidth()) * scaleX;
    autoScalingScaleY = ((float) getNativeHeight() / getHeight()) * scaleY;
    autoScalingOffsetX = getNativeWidth() / 2 - getNativeWidth() / 2 * scaleX;
    autoScalingOffsetY = getNativeHeight() / 2 - getNativeHeight() / 2 * scaleY;
  }

  @Override
  public void disableAutoScaling() {
    autoScaling = false;
    displayWidth = nativeDisplayWidth;
    displayHeight = nativeDisplayHeight;
    autoScalingScaleX = null;
    autoScalingScaleY = null;
    autoScalingOffsetX = 0;
    autoScalingOffsetY = 0;
  }

  @Override
  public void screenStarted(@Nonnull final Screen screen) {
    imageManager.uploadScreenImages(screen);
  }

  @Override
  public void screenEnded(@Nonnull final Screen screen) {
    imageManager.unloadScreenImages(screen);
  }

  @Override
  public void screensClear(@Nonnull final Collection<Screen> screens) {
    for (Screen screen : screens) {
      imageManager.unloadScreenImages(screen);
      imageManager.screenRemoved(screen);
    }
  }

  @Override
  public void screenAdded(@Nonnull final Screen screen) {
    imageManager.screenAdded(screen);
  }

  @Override
  public void screenRemoved(@Nonnull final Screen screen) {
    imageManager.screenRemoved(screen);
  }

  private boolean isOutsideClippingRectangle(final int x0, final int y0, final int x1, final int y1) {
    if (x0 > clip.x1) {
      return true;
    }
    if (x1 < clip.x0) {
      return true;
    }
    if (y0 > clip.y1) {
      return true;
    }
    if (y1 < clip.y0) {
      return true;
    }
    return false;
  }

  private boolean isInsideClippingRectangle(final int x0, final int y0, final int x1, final int y1) {
    if (x0 >= clip.x0 &&
        x0 <= clip.x1 &&
        x1 >= clip.x0 &&
        x1 <= clip.x1 &&
        y0 >= clip.y0 &&
        y0 <= clip.y1 &&
        y1 >= clip.y0 &&
        y1 <= clip.y1) {
      return true;
    }
    return false;
  }
}
