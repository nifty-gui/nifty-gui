package de.lessvoid.nifty.batch;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.jglfont.BitmapFontFactory;
import org.jglfont.spi.BitmapFontRenderer;

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

/**
 * BatchRenderDevice will try to reduce state changes by storing all of the textures into a single texture atlas and
 * will try to render the whole GUI in very few - at best in a single - draw call.
 *
 * @author void
 */
public class BatchRenderDevice implements RenderDevice {
  private static Logger log = Logger.getLogger(BatchRenderDevice.class.getName());
  private NiftyResourceLoader resourceLoader;
  private int viewportWidth = -1;
  private int viewportHeight = -1;
  private long time;
  private long frames;
  private long lastFrames;
  private int glyphCount;
  private int quadCount;
  private boolean displayFPS = false;
  private boolean logFPS = false;
  private RenderFont fpsFont;

  private BlendMode currentBlendMode = null;
  private boolean currentClipping = false;
  private int currentClippingX0 = 0;
  private int currentClippingY0 = 0;
  private int currentClippingX1 = 0;
  private int currentClippingY1 = 0;

  private StringBuilder buffer = new StringBuilder();
  private int completeClippedCounter;

  private final BitmapFontFactory factory;
  private final BatchRenderBackend renderBackend;
  private TextureAtlasGenerator generator;
  private Color fontColor = new Color("#f00");
  private boolean activeBatch;
  private BatchRenderImage thePlainImage;
  private final int atlasWidth;
  private final int atlasHeight;
  private final Set<BatchRenderFont> fontCache = new HashSet<BatchRenderFont>();
  private final FontRenderer fontRenderer;

  /**
   * The standard constructor. You'll use this in production code. Using this
   * constructor will configure the RenderDevice to not log FPS on System.out.
   * @param atlasWidth 
   * @param atlasHeight 
   */
  public BatchRenderDevice(final BatchRenderBackend renderBackend, final int atlasWidth, final int atlasHeight) {
    this.renderBackend = renderBackend;
    this.atlasWidth = atlasWidth;
    this.atlasHeight = atlasHeight;

    time = System.currentTimeMillis();
    frames = 0;
    generator = new TextureAtlasGenerator(atlasWidth, atlasHeight);
    fontRenderer = new FontRenderer(this);
    factory = new BitmapFontFactory(fontRenderer);
    renderBackend.createAtlasTexture(atlasWidth, atlasHeight);
  }

  public void enableLogFPS() {
    logFPS = true;
  }

  @Override
  public void setResourceLoader(final NiftyResourceLoader resourceLoader) {
    this.resourceLoader = resourceLoader;

    if (this.displayFPS) {
      fpsFont = createFont("fps.fnt");
    }

    renderBackend.setResourceLoader(resourceLoader);
  }

  /**
   * Get Width.
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

    if (displayFPS) {
      renderFont(fpsFont, buffer.toString(), 10, getHeight() - fpsFont.getHeight() - 10, fontColor , 1.0f, 1.0f);
    }

    int batches = renderBackend.render();

    frames++;
    long diff = System.currentTimeMillis() - time;
    if (diff >= 1000) {
      time += diff;
      lastFrames = frames;

      buffer.setLength(0);
      buffer.append("FPS: ");
      buffer.append(lastFrames);
      buffer.append(" (");
      buffer.append(String.format("%f", 1000.f / lastFrames));
      buffer.append(" ms)");
      buffer.append(", Total Tri: ");
      buffer.append(quadCount*2);
      buffer.append(" (Text: ");
      buffer.append(glyphCount*2);
      buffer.append(")");
      buffer.append(", Total Vert: ");
      buffer.append(quadCount*4);
      buffer.append(" (Text: ");
      buffer.append(glyphCount*4);
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

  @Override
  public RenderImage createImage(final String filename, final boolean filterLinear) {
    Image image = renderBackend.loadImage(filename);
    return new BatchRenderImage(image, generator, filename, renderBackend);
  }

  @Override
  public RenderFont createFont(final String filename) {
    try {
      BatchRenderFont batchRenderFont = new BatchRenderFont(this, filename, factory, resourceLoader);
      fontCache.add(batchRenderFont);
      return batchRenderFont;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void disposeFont(final BatchRenderFont batchRenderFont) {
    fontCache.remove(batchRenderFont);
  }

  @Override
  public void renderQuad(final int x, final int y, final int width, final int height, final Color color) {
    log.finest("renderQuad()");
    BatchRenderImage plainImage = getPlainImage();
    addQuad(x, y, width, height, color, color, color, color, plainImage.getX(), plainImage.getY(), plainImage.getWidth(), plainImage.getHeight());
  }

  @Override
  public void renderQuad(final int x, final int y, final int width, final int height, final Color topLeft, final Color topRight, final Color bottomRight, final Color bottomLeft) {
    log.finest("renderQuad2()");
    BatchRenderImage plainImage = getPlainImage();
    addQuad(x, y, width, height, topLeft, topRight, bottomLeft, bottomRight, plainImage.getX(), plainImage.getY(), plainImage.getWidth(), plainImage.getHeight());
  }

  @Override
  public void renderImage(final RenderImage image, final int x, final int y, final int width, final int height, final Color c, final float scale) {
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
      final RenderImage image,
      final int x,
      final int y,
      final int w,
      final int h,
      final int srcX,
      final int srcY,
      final int srcW,
      final int srcH,
      final Color c,
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
  public void renderFont(final RenderFont font, final String text, final int x, final int y, final Color color, final float sizeX, final float sizeY) {
    log.finest("renderFont()");

    BatchRenderFont renderFont = (BatchRenderFont) font;
    renderFont.getBitmapFont().renderText(x, y, text, sizeX, sizeY, color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
  }

  @Override
  public void enableClip(final int x0, final int y0, final int x1, final int y1) {
    log.finest("enableClip()");

    if (currentClipping && currentClippingX0 == x0 && currentClippingY0 == y0 && currentClippingX1 == x1 && currentClippingY1 == y1) {
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
  public void setBlendMode(final BlendMode renderMode) {
    log.finest("setBlendMode()");

    if (renderMode.equals(currentBlendMode)) {
      return;
    }

    currentBlendMode = renderMode;
    addNewBatch();
  }

  @Override
  public MouseCursor createMouseCursor(final String filename, final int hotspotX, final int hotspotY) throws IOException {
    return renderBackend.createMouseCursor(filename, hotspotX, hotspotY);
  }

  @Override
  public void enableMouseCursor(final MouseCursor mouseCursor) {
    renderBackend.enableMouseCursor(mouseCursor);
  }

  @Override
  public void disableMouseCursor() {
    renderBackend.disableMouseCursor();
  }

  public void resetTextureAtlas() {
    thePlainImage.unload();
    generator.reset();
    renderBackend.clearAtlasTexture(atlasWidth, atlasHeight);
    fontRenderer.unload();
  }

  // Internal implementations

  private BatchRenderImage getPlainImage() {
    if (thePlainImage == null) {
      thePlainImage = (BatchRenderImage) createImage("de/lessvoid/nifty/batch/nifty.png", true);
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
      final Color color1,
      final Color color2,
      final Color color3,
      final Color color4,
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
      addQuadInternal(x, y, width, height, color1, color2, color3, color4, textureX, textureY, textureWidth, textureHeight);
      return;
    }

    // we need to clip
    float newX;
    float newY;
    float newWidth;
    float newHeight;
    int newTextureX = textureX;
    int newTextureY = textureY;
    int newTextureWidth = textureWidth;
    int newTextureHeight = textureHeight;

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

    addQuadInternal(newX, newY, newWidth, newHeight, color1, color2, color3, color4, newTextureX, newTextureY, newTextureWidth, newTextureHeight);
  }

  private void addQuadInternal(
      final float x,
      final float y,
      final float width,
      final float height,
      final Color color1,
      final Color color2,
      final Color color3,
      final Color color4,
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

  private class FontRenderer implements BitmapFontRenderer {
    private final Map<String, BitmapInfo> textureInfos = new HashMap<String, BitmapInfo>();
    private final ColorValueParser colorValueParser = new ColorValueParser();
    private BatchRenderDevice batchRenderDevice;
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
    public void registerBitmap(final String bitmapId, final InputStream data, final String filename) throws IOException {
      textureInfos.put(bitmapId, new BitmapInfo((BatchRenderImage) batchRenderDevice.createImage(filename, true)));
    }

    @Override
    public void registerGlyph(
        final String bitmapId,
        final char c,
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
      for (BitmapInfo info : textureInfos.values()) {
        info.upload();
      }
    }

    @Override
    public int preProcess(final String text, final int offset) {
      hasColor = false;
      int index = offset;
      colorValueParser.isColor(text, index);
      while (colorValueParser.isColor()) {
        textColor.setRed(colorValueParser.getColor().getRed());
        textColor.setGreen(colorValueParser.getColor().getGreen());
        textColor.setBlue(colorValueParser.getColor().getBlue());
        textColor.setAlpha(colorValueParser.getColor().getAlpha());
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
        final char c,
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
        textColor.setAlpha(a);
      }
      textureInfos.get(bitmapId).renderCharacter(c, x, y, sx, sy, textColor);
    }

    @Override
    public void afterRender() {
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
        final Color textColor,
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
    private final Map<Character, CharRenderInfo> characterIndices = new Hashtable<Character, CharRenderInfo>();
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

    public void renderCharacter(char c, int x, int y, float sx, float sy, Color textColor) {
      int atlasX0 = result.getX();
      int atlasY0 = result.getY();
      int atlasImageW = result.getOriginalImageWidth();
      int atlasImageH = result.getOriginalImageHeight();
      characterIndices.get(c).renderQuad(x, y, sx, sy, textColor, atlasX0, atlasY0, atlasImageW, atlasImageH);
    }

    public void addCharRenderInfo(final Character c, final CharRenderInfo renderInfo) {
      this.characterIndices.put(c, renderInfo);
    }
  }
}
