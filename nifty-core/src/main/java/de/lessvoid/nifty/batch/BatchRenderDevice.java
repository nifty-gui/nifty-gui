package de.lessvoid.nifty.batch;

import de.lessvoid.nifty.batch.TextureAtlasGenerator.Result;
import de.lessvoid.nifty.batch.spi.BatchRenderBackend;
import de.lessvoid.nifty.batch.spi.BatchRendererTexture;
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
import org.jglfont.spi.ResourceLoader;

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

  private final BatchRendererTexture atlasTexture;

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
    fontRenderer = new FontRenderer();
    factory = new JGLFontFactory(fontRenderer, new ResourceLoader() {
      @Override
      public InputStream load(String path) {
        if (resourceLoader == null) {
          return null;
        }
        return resourceLoader.getResourceAsStream(path);
      }
    });

    atlasTexture =renderBackend.createAtlasTexture(atlasWidth, atlasHeight);
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
    renderBackend.setResourceLoader(resourceLoader);
    if (this.displayFPS) {
      fpsFont = createFont("fps.fnt");
    }
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

    fontRenderer.deactivateBatches();

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
    renderBackend.endFrame();

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
    BatchRendererTexture.Image image = renderBackend.loadImage(filename);
    if (image == null) {
      return null;
    }
    return new BatchRenderImage(image, filename, atlasTexture);
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
    addQuad(atlasTexture, x, y, width, height, color, color, color, color, plainImage.getX(), plainImage.getY(),
        plainImage.getWidth(), plainImage.getHeight(), atlasWidth, atlasHeight);
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
    addQuad(atlasTexture, x, y, width, height, topLeft, topRight, bottomLeft, bottomRight, plainImage.getX(), plainImage.getY(),
        plainImage.getWidth(), plainImage.getHeight(), atlasWidth, atlasHeight);
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
    addQuad(atlasTexture, ix, iy, iw, ih, c, c, c, c, img.getX(), img.getY(), img.getWidth(), img.getHeight(), atlasWidth, atlasHeight);
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
    addQuad(atlasTexture, ix, iy, iw, ih, c, c, c, c, img.getX() + srcX, img.getY() + srcY, srcW, srcH, atlasWidth, atlasHeight);
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
    atlasTexture.clear();
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
    renderBackend.beginBatch(atlasTexture, currentBlendMode);
  }

  private void addQuad(
      @Nonnull final BatchRendererTexture texture,
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
      final int textureHeight,
      final int atlasImageW,
      final int atlasImageH) {
    // if this quad is completely outside the clipping area we don't need to render it at all
    if (isOutsideClippingRectangle(x, y, width, height)) {
      completeClippedCounter++;
      return;
    }

    // if this quad is completely inside the clipping area we can simply render the quad
    if (isInsideClippingRectangle(x, y, width, height)) {
      addQuadInternal(texture, x, y, width, height, color1, color2, color3, color4, textureX, textureY, textureWidth,
          textureHeight, atlasImageW, atlasImageH);
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

    addQuadInternal(texture, newX, newY, newWidth, newHeight, color1, color2, color3, color4, newTextureX, newTextureY,
        newTextureWidth, newTextureHeight, atlasImageW, atlasImageH);
  }

  private void addQuadInternal(
      BatchRendererTexture texture,
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
      final int textureHeight,
      final int textureAtlasWidth,
      final int textureAtlasHeight) {
    if (!activeBatch) {
      renderBackend.beginBatch(atlasTexture, currentBlendMode);
      activeBatch = true;
    }
    renderBackend.addQuad(
        texture,
        x, y,
        width,
        height,
        color1,
        color2,
        color3,
        color4,
        calcU(textureX, textureAtlasWidth),
        calcU(textureY, textureAtlasHeight),
        calcU(textureWidth - 1, textureAtlasWidth),
        calcU(textureHeight - 1, textureAtlasHeight));
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

  private class BitmapResizableTexture {
    private BatchRendererTexture texture;
    private boolean batchActive = false;
    private int textureWidth;
    private int textureHeight;
    private ByteBuffer textureBufer;
    private Map<String, BitmapInfo> bitmapInfos = new HashMap<String, BitmapInfo>();

    private BitmapResizableTexture(BatchRendererTexture.Image image, String filename) {
      int dim = 2;
      while (dim < image.getWidth() || dim < image.getHeight()) {
        dim <<= 1;
      }
      textureWidth = dim;
      textureHeight = dim;
      this.textureBufer = ByteBuffer.allocateDirect(textureWidth * textureHeight * 4);

      image.getData().rewind();
      byte[] exchange = new byte[image.getWidth() * 4];
      for (int i = 0; i < image.getHeight(); i++) {
        image.getData().get(exchange);
        textureBufer.position(i * textureWidth * 4);
        textureBufer.put(exchange);
      }

      blitRefit(image, filename, true);
    }

    public BitmapInfo getBitmapInfo(String filename) {
      return bitmapInfos.get(filename);
    }

    public void blit(BatchRendererTexture.Image image, String filename) {
      try {
        Result result = texture.getGenerator().addImage(image.getWidth(), image.getHeight(), filename, 5);
        blit(image, filename, result);
      } catch (TextureAtlasGeneratorException e) {
        blitRefit(image, filename, false);
      }
    }

    private void blit(BatchRendererTexture.Image image, String filename, Result result) {
      image.getData().rewind();
      textureBufer.rewind();
      byte[] exchange = new byte[image.getWidth() * 4];
      for (int i = 0; i < image.getHeight(); i++) {
        image.getData().get(exchange);
        textureBufer.position(((result.getY() + i) * textureWidth * 4) + (result.getX() * 4));
        textureBufer.put(exchange);
      }
      image.getData().rewind();
      texture.addImageToTexture(image, result.getX(), result.getY());
      bitmapInfos.put(filename, new BitmapInfo(this, result.getX(), result.getY()));
    }

    private void blitRefit(BatchRendererTexture.Image image, String filename, boolean firstPlace) {
      int newWidth  = textureWidth;
      int newHeight = textureHeight;
      ByteBuffer newBuffer = textureBufer;
      if (texture != null) {
        texture.dispose();
        newWidth  <<= 1;
        newHeight <<= 1;

        newBuffer = ByteBuffer.allocateDirect(newWidth * newHeight * 4);
        textureBufer.rewind();
        byte[] exchange = new byte[textureWidth*4];
        for (int i = 0; i < textureHeight; i++) {
          textureBufer.get(exchange);
          newBuffer.position(i * newWidth * 4);
          newBuffer.put(exchange);
        }
      }

      newBuffer.rewind();
      texture = renderBackend.createFontTexture(newBuffer, newWidth, newHeight);

      try {
        Result result = texture.getGenerator().addImage(textureWidth, textureHeight, "texture-" + textureWidth + "-" + textureHeight, 5);
        if (!firstPlace) {
          result = texture.getGenerator().addImage(image.getWidth(), image.getHeight(), filename, 5);
        }
        textureWidth = newWidth;
        textureHeight = newHeight;
        textureBufer = newBuffer;

        blit(image, filename, result);
      } catch (TextureAtlasGeneratorException e1) {
        texture.dispose();
        bitmapInfos.clear();
        log.severe("Could not resize atlas texture!");
        return;
      }
    }

    public int getTextureWidth() {
      return textureWidth;
    }

    public int getTextureHeight() {
      return textureHeight;
    }

    public BatchRendererTexture getTexture() {
      return texture;
    }

    public void setTexture(BatchRendererTexture texture) {
      this.texture = texture;
    }

    public boolean isBatchActive() {
      return batchActive;
    }

    public void setBatchActive(boolean batchActive) {
      this.batchActive = batchActive;
    }
  }

  private class FontRenderer implements JGLFontRenderer {
    private final ColorValueParser colorValueParser = new ColorValueParser();
    private final Color textColor = Color.BLACK;
    private boolean hasColor;
    private final Map<String, BitmapResizableTexture> textures = new HashMap<String, BitmapResizableTexture>();


    public FontRenderer() {
    }

    public void tryActivateBatch(String fontName) {
      BitmapResizableTexture info = textures.get(fontName);
      if (info != null) {
        if (!info.isBatchActive()) {
          renderBackend.beginBatch(info.getTexture(), currentBlendMode);
          info.setBatchActive(true);
        }
      }
    }

    public void unload() {
      for (BitmapResizableTexture info : textures.values()) {
        info.getTexture().dispose();
      }
    }

    @Override
    public void registerBitmap(
        @Nonnull final String fontName,
        @Nonnull final String bitmapId,
        final InputStream data,
        @Nonnull final String filename) throws IOException {
      BatchRendererTexture.Image image = renderBackend.loadImage(filename);
      if (image != null) {
        BitmapResizableTexture info = textures.get(fontName);
        if (info == null) {
          textures.put(fontName, new BitmapResizableTexture(image, bitmapId));
        } else {
          info.blit(image, bitmapId);
        }
      }
    }

    @Override
    public void registerBitmap(
        @Nonnull final String fontName,
        @Nonnull final String bitmapId,
        @Nonnull final ByteBuffer data,
        final int width,
        final int height,
        @Nonnull final String filename) throws IOException {
      BatchRendererTexture.Image image = renderBackend.loadImage(data, width, height);
      if (image != null) {
        BitmapResizableTexture info = textures.get(fontName);
        if (info == null) {
          textures.put(fontName, new BitmapResizableTexture(image, bitmapId));
        } else {
          info.blit(image, bitmapId);
        }
      }
    }

    @Override
    public void registerGlyph(
        final String fontName,
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
      BitmapResizableTexture fontTexture = textures.get(fontName);
      if (fontTexture != null) {
        BitmapInfo textureInfo = fontTexture.getBitmapInfo(bitmapId);
        if (textureInfo != null) {
          textureInfo.addCharRenderInfo(c, new CharRenderInfo(xoff, yoff, w, h, u0, v0));
        }
      }
    }

    @Override
    public void prepare() {
    }

    @Override
    public void beforeRender(String fontName) {
      hasColor = false;
      tryActivateBatch(fontName);
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
        final String fontName,
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
      BitmapResizableTexture info = textures.get(fontName);
      if (info != null) {
        info.getBitmapInfo(bitmapId).renderCharacter(info.getTexture(), c, x, y, sx, sy, textColor);
      }
    }

    @Override
    public void afterRender(String fontName) {

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

    public void deactivateBatches() {
      for (BitmapResizableTexture info : textures.values()) {
        info.setBatchActive(false);
      }
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
        @Nonnull final BatchRendererTexture texture,
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
          texture,
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
          h,
          atlasImageW,
          atlasImageH);
    }
  }

  private static class BitmapInfo {
    private final Map<Integer, CharRenderInfo> characterIndices = new HashMap<Integer, CharRenderInfo>();
    private final BitmapResizableTexture resizableTexture;
    private final int posX;
    private final int posY;

    public BitmapInfo(BitmapResizableTexture texture, int x, int y) {
      this.resizableTexture = texture;
      this.posX = x;
      this.posY = y;
    }

    public void renderCharacter(@Nonnull final BatchRendererTexture texture, int c, int x, int y, float sx, float sy, @Nonnull Color textColor) {
      int atlasImageW = resizableTexture.getTextureWidth();
      int atlasImageH = resizableTexture.getTextureHeight();
      characterIndices.get(c).renderQuad(texture, x, y, sx, sy, textColor, posX, posY, atlasImageW, atlasImageH);
    }

    public void addCharRenderInfo(final Integer c, final CharRenderInfo renderInfo) {
      this.characterIndices.put(c, renderInfo);
    }
  }
}
