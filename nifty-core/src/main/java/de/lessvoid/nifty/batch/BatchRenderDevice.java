package de.lessvoid.nifty.batch;

import de.lessvoid.nifty.batch.TextureAtlasGenerator.Result;
import de.lessvoid.nifty.batch.spi.BatchRenderBackend;
import de.lessvoid.nifty.batch.spi.BatchRenderBackend.Image;
import de.lessvoid.nifty.render.BlendMode;
import de.lessvoid.nifty.spi.render.MouseCursor;
import de.lessvoid.nifty.spi.render.RenderDevice;
import de.lessvoid.nifty.spi.render.RenderFont;
import de.lessvoid.nifty.spi.render.RenderImage;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.nifty.tools.ColorValueParser;
import de.lessvoid.nifty.tools.resourceloader.NiftyResourceLoader;
import org.jglfont.JGLFontFactory;
import org.jglfont.spi.JGLFontRenderer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * BatchRenderDevice will try to reduce state changes by storing all of the textures into a single texture atlas and
 * will try to render the whole GUI in very few - at best in a single - draw call.
 *
 * @author void
 */
public class BatchRenderDevice implements RenderDevice {
  @Nonnull
  private static final Logger log = Logger.getLogger(BatchRenderDevice.class.getName());
  @Nullable
  private NiftyResourceLoader resourceLoader;
  private int viewportWidth = -1;
  private int viewportHeight = -1;
  private long time;
  private long frames;
  private int glyphCount;
  private int quadCount;
  private boolean displayFPS = false;
  private boolean logFPS = false;
  @Nullable
  private RenderFont fpsFont;

  @Nonnull
  private BlendMode currentBlendMode = BlendMode.BLEND;
  private boolean currentClipping = false;
  private int currentClippingX0 = 0;
  private int currentClippingY0 = 0;
  private int currentClippingX1 = 0;
  private int currentClippingY1 = 0;

  @Nonnull
  private final StringBuilder buffer = new StringBuilder();
  private int completeClippedCounter;

  @Nonnull
  private final JGLFontFactory factory;
  @Nonnull
  private final BatchRenderBackend renderBackend;
  @Nonnull
  private final TextureAtlasGenerator generator;
  @Nonnull
  private final Color fontColor = new Color("#f00");
  private boolean activeBatch;
  @Nullable
  private BatchRenderImage thePlainImage;
  private final int atlasWidth;
  private final int atlasHeight;
  @Nonnull
  private final FontRenderer fontRenderer;

  /**
   * The standard constructor. You'll use this in production code. Using this
   * constructor will configure the RenderDevice to not log FPS on System.out.
   */
  public BatchRenderDevice(
      @Nonnull final BatchRenderBackend renderBackend,
      final int atlasWidth,
      final int atlasHeight) {
    this.renderBackend = renderBackend;
    this.atlasWidth = atlasWidth;
    this.atlasHeight = atlasHeight;

    time = System.currentTimeMillis();
    frames = 0;
    generator = new TextureAtlasGenerator(atlasWidth, atlasHeight);
    fontRenderer = new FontRenderer(this);
    factory = new JGLFontFactory(fontRenderer);
    renderBackend.createAtlasTexture(atlasWidth, atlasHeight);
  }

  public void enableLogFPS() {
    logFPS = true;
  }

  public void setDisplayFPS(final boolean newValue) {
    if (newValue != displayFPS) {
      displayFPS = newValue;
      if (resourceLoader != null && fpsFont == null && displayFPS) {
        fpsFont = createFont("fps.fnt");
      }
    }
  }

  @Override
  public void setResourceLoader(@Nonnull final NiftyResourceLoader resourceLoader) {
    this.resourceLoader = resourceLoader;

    if (this.displayFPS) {
      fpsFont = createFont("fps.fnt");
    }

    renderBackend.setResourceLoader(resourceLoader);
  }

  /**
   * Get Width.
   *
   * @return width of display mode
   */
  @Override
  public int getWidth() {
    if (viewportWidth == -1) {
      getViewport();
    }
    return viewportWidth;
  }

  /**
   * Get Height.
   *
   * @return height of display mode
   */
  @Override
  public int getHeight() {
    if (viewportHeight == -1) {
      getViewport();
    }
    return viewportHeight;
  }

  private void getViewport() {
    viewportWidth = renderBackend.getWidth();
    viewportHeight = renderBackend.getHeight();
  }

  @Override
  public void beginFrame() {
    log.finest("beginFrame()");
    renderBackend.beginFrame();

    currentBlendMode = BlendMode.BLEND;

    currentClipping = false;
    currentClippingX0 = 0;
    currentClippingY0 = 0;
    currentClippingX1 = getWidth();
    currentClippingY1 = getHeight();
    completeClippedCounter = 0;

    activeBatch = false;
    quadCount = 0;
    glyphCount = 0;
  }

  @Override
  public void endFrame() {
    log.finest("endFrame");
    log.fine("completely clipped elements: " + completeClippedCounter);

    if (displayFPS && fpsFont != null) {
      renderFont(fpsFont, buffer.toString(), 10, getHeight() - fpsFont.getHeight() - 10, fontColor, 1.0f, 1.0f);
    }

    int batches = renderBackend.render();

    frames++;
    long diff = System.currentTimeMillis() - time;
    if (diff >= 1000) {
      time += diff;
      long lastFrames = frames;

      buffer.setLength(0);
      buffer.append("FPS: ");
      buffer.append(lastFrames);
      buffer.append(" (");
      buffer.append(String.format("%f", 1000.f / lastFrames));
      buffer.append(" ms)");
      buffer.append(", Total Tri: ");
      buffer.append(quadCount * 2);
      buffer.append(" (Text: ");
      buffer.append(glyphCount * 2);
      buffer.append(")");
      buffer.append(", Total Vert: ");
      buffer.append(quadCount * 4);
      buffer.append(" (Text: ");
      buffer.append(glyphCount * 4);
      buffer.append("), Batches: ");
      buffer.append(batches);

      if (logFPS) {
        System.out.println(buffer.toString());
      }

      frames = 0;
    }

    // currently the RenderDevice interface does not support a way to be notified when the resolution is changed
    // so we reset the viewportWidth and viewportHeight here so that we only call getViewport() once per frame and
    // not each time someone calls getWidth() or getHeight().
    viewportWidth = -1;
    viewportHeight = -1;
  }

  @Override
  public void clear() {
    log.finest("clear()");
    renderBackend.clear();
  }

  @Nullable
  @Override
  public RenderImage createImage(@Nonnull final String filename, final boolean filterLinear) {
    Image image = renderBackend.loadImage(filename);
    if (image == null) {
      return null;
    }
    return new BatchRenderImage(image, generator, filename, renderBackend);
  }

  @Nonnull
  @Override
  public RenderFont createFont(@Nonnull final String filename) {
    if (resourceLoader == null) {
      throw new RuntimeException("Can't create font without ResourceLoader instance.");
    }
    try {
      return new BatchRenderFont(this, filename, factory, resourceLoader);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void disposeFont(final BatchRenderFont batchRenderFont) {
  }

  @Override
  public void renderQuad(final int x, final int y, final int width, final int height, @Nonnull final Color color) {
    log.finest("renderQuad()");
    BatchRenderImage plainImage = getPlainImage();
    addQuad(x, y, width, height, color, color, color, color, plainImage.getX(), plainImage.getY(),
        plainImage.getWidth(), plainImage.getHeight());
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
    log.finest("renderQuad2()");
    BatchRenderImage plainImage = getPlainImage();
    addQuad(x, y, width, height, topLeft, topRight, bottomLeft, bottomRight, plainImage.getX(), plainImage.getY(),
        plainImage.getWidth(), plainImage.getHeight());
  }

  @Override
  public void renderImage(
      @Nonnull final RenderImage image,
      final int x,
      final int y,
      final int width,
      final int height,
      @Nonnull final Color c,
      final float scale) {
    log.finest("renderImage()");

    if (width < 0) {
      log.warning("Attempt to render image with negative width");
      return;
    }
    if (height < 0) {
      log.warning("Attempt to render image with negative height");
      return;
    }

    BatchRenderImage img = (BatchRenderImage) image;
    if (!img.isUploaded()) {
      img.upload();
    }
    float centerX = x + width / 2.f;
    float centerY = y + height / 2.f;
    int ix = Math.round(centerX - (width * scale) / 2.f);
    int iy = Math.round(centerY - (height * scale) / 2.f);
    int iw = Math.round(width * scale);
    int ih = Math.round(height * scale);
    addQuad(ix, iy, iw, ih, c, c, c, c, img.getX(), img.getY(), img.getWidth(), img.getHeight());
  }

  @Override
  public void renderImage(
      @Nonnull final RenderImage image,
      final int x,
      final int y,
      final int w,
      final int h,
      final int srcX,
      final int srcY,
      final int srcW,
      final int srcH,
      @Nonnull final Color c,
      final float scale,
      final int centerX,
      final int centerY) {
    log.finest("renderImage2()");

    if (w < 0) {
      log.warning("Attempt to render image with negative width");
      return;
    }
    if (h < 0) {
      log.warning("Attempt to render image with negative height");
      return;
    }

    int ix = Math.round(-scale * centerX + scale * x + centerX);
    int iy = Math.round(-scale * centerY + scale * y + centerY);
    int iw = Math.round(w * scale);
    int ih = Math.round(h * scale);

    BatchRenderImage img = (BatchRenderImage) image;
    if (!img.isUploaded()) {
      img.upload();
    }
    addQuad(ix, iy, iw, ih, c, c, c, c, img.getX() + srcX, img.getY() + srcY, srcW, srcH);
  }

  @Override
  public void renderFont(
      @Nonnull final RenderFont font,
      @Nonnull final String text,
      final int x,
      final int y,
      @Nonnull final Color color,
      final float sizeX,
      final float sizeY) {
    log.finest("renderFont()");

    BatchRenderFont renderFont = (BatchRenderFont) font;
    renderFont.getBitmapFont().renderText(x, y, text, sizeX, sizeY, color.getRed(), color.getGreen(),
        color.getBlue(), color.getAlpha());
  }

  @Override
  public void enableClip(final int x0, final int y0, final int x1, final int y1) {
    log.finest("enableClip()");

    if (currentClipping && currentClippingX0 == x0 && currentClippingY0 == y0 && currentClippingX1 == x1 &&
        currentClippingY1 == y1) {
      return;
    }
    currentClipping = true;
    currentClippingX0 = x0;
    currentClippingY0 = y0;
    currentClippingX1 = x1;
    currentClippingY1 = y1;
  }

  @Override
  public void disableClip() {
    log.finest("disableClip()");

    if (!currentClipping) {
      return;
    }
    currentClipping = false;
    currentClippingX0 = 0;
    currentClippingY0 = 0;
    currentClippingX1 = getWidth();
    currentClippingY1 = getHeight();
  }

  @Override
  public void setBlendMode(@Nonnull final BlendMode renderMode) {
    log.finest("setBlendMode()");

    if (renderMode.equals(currentBlendMode)) {
      return;
    }

    currentBlendMode = renderMode;
    addNewBatch();
  }

  @Override
  public MouseCursor createMouseCursor(
      @Nonnull final String filename,
      final int hotspotX,
      final int hotspotY) throws IOException {
    return renderBackend.createMouseCursor(filename, hotspotX, hotspotY);
  }

  @Override
  public void enableMouseCursor(@Nonnull final MouseCursor mouseCursor) {
    renderBackend.enableMouseCursor(mouseCursor);
  }

  @Override
  public void disableMouseCursor() {
    renderBackend.disableMouseCursor();
  }

  public void resetTextureAtlas() {
    if (thePlainImage != null) {
      thePlainImage.unload();
    }
    generator.reset();
    renderBackend.clearAtlasTexture(atlasWidth, atlasHeight);
    fontRenderer.unload();
  }

  // Internal implementations

  @Nonnull
  private BatchRenderImage getPlainImage() {
    if (thePlainImage == null) {
      thePlainImage = (BatchRenderImage) createImage("de/lessvoid/nifty/batch/nifty.png", true);
      if (thePlainImage == null) {
        throw new RuntimeException("The batch renderer requires the plain image in the resources, but its not there.");
      }
    }
    if (!thePlainImage.isUploaded()) {
      thePlainImage.upload();
    }
    return thePlainImage;
  }

  private void addNewBatch() {
    renderBackend.beginBatch(currentBlendMode);
  }

  private void addQuad(
      final float x,
      final float y,
      final float width,
      final float height,
      @Nonnull final Color color1,
      @Nonnull final Color color2,
      @Nonnull final Color color3,
      @Nonnull final Color color4,
      final int textureX,
      final int textureY,
      final int textureWidth,
      final int textureHeight) {
    // if this quad is completely outside the clipping area we don't need to render it at all
    if (isOutsideClippingRectangle(x, y, width, height)) {
      completeClippedCounter++;
      return;
    }

    // if this quad is completely inside the clipping area we can simply render the quad
    if (isInsideClippingRectangle(x, y, width, height)) {
      addQuadInternal(x, y, width, height, color1, color2, color3, color4, textureX, textureY, textureWidth,
          textureHeight);
      return;
    }

    // we need to clip
    float newX;
    float newY;
    float newWidth;
    float newHeight;
    int newTextureX;
    int newTextureY;
    int newTextureWidth;
    int newTextureHeight;

    if (x >= currentClippingX0) {
      newX = x;
      newTextureX = textureX;
    } else {
      newX = currentClippingX0;
      newTextureX = (int) (textureX + (currentClippingX0 - x) / width * textureWidth);
    }

    if (y >= currentClippingY0) {
      newY = y;
      newTextureY = textureY;
    } else {
      newY = currentClippingY0;
      newTextureY = (int) (textureY + (currentClippingY0 - y) / height * textureHeight);
    }

    if (x + width <= currentClippingX1) {
      newWidth = (x + width) - newX;
      newTextureWidth = (textureX + textureWidth) - newTextureX;
    } else {
      newWidth = currentClippingX1 - newX;
      newTextureWidth = (int) (newWidth / width * textureWidth);
    }

    if (y + height <= currentClippingY1) {
      newHeight = y + height - newY;
      newTextureHeight = (textureY + textureHeight) - newTextureY;
    } else {
      newHeight = currentClippingY1 - newY;
      newTextureHeight = (int) (newHeight / height * textureHeight);
    }

    addQuadInternal(newX, newY, newWidth, newHeight, color1, color2, color3, color4, newTextureX, newTextureY,
        newTextureWidth, newTextureHeight);
  }

  private void addQuadInternal(
      final float x,
      final float y,
      final float width,
      final float height,
      @Nonnull final Color color1,
      @Nonnull final Color color2,
      @Nonnull final Color color3,
      @Nonnull final Color color4,
      final int textureX,
      final int textureY,
      final int textureWidth,
      final int textureHeight) {
    if (!activeBatch) {
      renderBackend.beginBatch(currentBlendMode);
      activeBatch = true;
    }
    renderBackend.addQuad(
        x, y,
        width,
        height,
        color1,
        color2,
        color3,
        color4,
        calcU(textureX, atlasWidth),
        calcU(textureY, atlasHeight),
        calcU(textureWidth - 1, atlasWidth),
        calcU(textureHeight - 1, atlasHeight));
    quadCount++;
  }

  private float calcU(final int value, final int max) {
    return (0.5f / (float) max) + (value / (float) max);
  }

  private boolean isOutsideClippingRectangle(final float x, final float y, final float width, final float height) {
    if (x > currentClippingX1) {
      return true;
    }
    if ((x + width) < currentClippingX0) {
      return true;
    }
    if (y > currentClippingY1) {
      return true;
    }
    if ((y + height) < currentClippingY0) {
      return true;
    }
    return false;
  }

  private boolean isInsideClippingRectangle(final float x, final float y, final float width, final float height) {
    if (x >= currentClippingX0 &&
        x <= currentClippingX1 &&
        (x + width) >= currentClippingX0 &&
        (x + width) <= currentClippingX1 &&
        y >= currentClippingY0 &&
        y <= currentClippingY1 &&
        (y + height) >= currentClippingY0 &&
        (y + height) <= currentClippingY1) {
      return true;
    }
    return false;
  }

  private class FontRenderer implements JGLFontRenderer {
    private final Map<String, BitmapInfo> textureInfos = new HashMap<String, BitmapInfo>();
    private final ColorValueParser colorValueParser = new ColorValueParser();
    private final BatchRenderDevice batchRenderDevice;
    private final Color textColor = Color.BLACK;
    private boolean hasColor;

    public FontRenderer(final BatchRenderDevice batchRenderDevice) {
      this.batchRenderDevice = batchRenderDevice;
    }

    public void unload() {
      for (BitmapInfo info : textureInfos.values()) {
        info.unload();
      }
    }

    @Override
    public void registerBitmap(
        @Nonnull final String bitmapId,
        final InputStream data,
        @Nonnull final String filename) throws IOException {
      textureInfos.put(bitmapId, new BitmapInfo((BatchRenderImage) batchRenderDevice.createImage(filename, true)));
    }

    @Override
    public void registerBitmap(
            @Nonnull final String bitmapId,
            @Nonnull final ByteBuffer data,
            final int width,
            final int height,
            @Nonnull final String filename
    ) throws IOException {
      BatchRenderImage batchRenderImage = null;
      Image image = renderBackend.loadImage(data, width, height);
      if (image != null) {
        batchRenderImage = new BatchRenderImage(image, generator, filename, renderBackend);
      }
      textureInfos.put(bitmapId, new BitmapInfo(batchRenderImage));
    }

    @Override
    public void registerGlyph(
        final String bitmapId,
        final int c,
        final int xoff,
        final int yoff,
        final int w,
        final int h,
        final float u0,
        final float v0,
        final float u1,
        final float v1) {
      BitmapInfo textureInfo = textureInfos.get(bitmapId);
      textureInfo.addCharRenderInfo(c, new CharRenderInfo(xoff, yoff, w, h, u0, v0));
    }

    @Override
    public void prepare() {
    }

    @Override
    public void beforeRender() {
      hasColor = false;
      for (BitmapInfo info : textureInfos.values()) {
        info.upload();
      }
    }

    @Override
    public int preProcess(@Nonnull final String text, final int offset) {
      int index = offset;
      colorValueParser.isColor(text, index);
      while (colorValueParser.isColor()) {
        final Color color = colorValueParser.getColor();
        assert color != null;
        textColor.setRed(color.getRed());
        textColor.setGreen(color.getGreen());
        textColor.setBlue(color.getBlue());
        textColor.setAlpha(color.getAlpha());
        hasColor = true;
        index = colorValueParser.getNextIndex();
        if (index >= text.length()) {
          return index;
        }
        colorValueParser.isColor(text, index);
      }
      return index;
    }

    @Override
    public void render(
        final String bitmapId,
        final int x,
        final int y,
        final int c,
        final float sx,
        final float sy,
        final float r,
        final float g,
        final float b,
        final float a) {
      if (!hasColor) {
        textColor.setRed(r);
        textColor.setGreen(g);
        textColor.setBlue(b);
      }
      textColor.setAlpha(a);
      textureInfos.get(bitmapId).renderCharacter(c, x, y, sx, sy, textColor);
    }

    @Override
    public void afterRender() {
    }

    @Override
    public int preProcessForLength(@Nonnull final String text, final int offset) {
      int index = offset;
      colorValueParser.isColor(text, index);
      while (colorValueParser.isColor()) {
        index = colorValueParser.getNextIndex();
        if (index >= text.length()) {
          return index;
        }
        colorValueParser.isColor(text, index);
      }
      return index;
    }
  }

  private class CharRenderInfo {
    final int xoff;
    final int yoff;
    final int w;
    final int h;
    final float u0;
    final float v0;

    public CharRenderInfo(
        final int xoff,
        final int yoff,
        final int w,
        final int h,
        final float u0,
        final float v0) {
      this.xoff = xoff;
      this.yoff = yoff;
      this.w = w;
      this.h = h;
      this.u0 = u0;
      this.v0 = v0;
    }

    public void renderQuad(
        final int x,
        final int y,
        final float sx,
        final float sy,
        @Nonnull final Color textColor,
        final int atlasX0,
        final int atlasY0,
        final int atlasImageW,
        final int atlasImageH) {
      glyphCount++;
      addQuad(
          x + (float) Math.floor(xoff * sx),
          y + (float) Math.floor(yoff * sy),
          (w * sx),
          (h * sy),
          textColor,
          textColor,
          textColor,
          textColor,
          (int) (atlasX0 + u0 * atlasImageW),
          (int) (atlasY0 + v0 * atlasImageH),
          w,
          h);
    }
  }

  private static class BitmapInfo {
    private final BatchRenderImage image;
    private final Map<Integer, CharRenderInfo> characterIndices = new HashMap<Integer, CharRenderInfo>();
    private Result result;

    public BitmapInfo(final BatchRenderImage image) {
      this.image = image;
    }

    private void upload() {
      if (image.isUploaded()) {
        return;
      }
      image.upload();
      result = new Result(image.getX(), image.getY(), image.getWidth(), image.getHeight());
    }

    private void unload() {
      image.markAsUnloaded();
    }

    public void renderCharacter(int c, int x, int y, float sx, float sy, @Nonnull Color textColor) {
      int atlasX0 = result.getX();
      int atlasY0 = result.getY();
      int atlasImageW = result.getOriginalImageWidth();
      int atlasImageH = result.getOriginalImageHeight();
      characterIndices.get(c).renderQuad(x, y, sx, sy, textColor, atlasX0, atlasY0, atlasImageW, atlasImageH);
    }

    public void addCharRenderInfo(final Integer c, final CharRenderInfo renderInfo) {
      this.characterIndices.put(c, renderInfo);
    }
  }
}
