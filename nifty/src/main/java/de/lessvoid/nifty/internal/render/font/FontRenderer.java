package de.lessvoid.nifty.internal.render.font;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import org.jglfont.spi.JGLFontRenderer;

import de.lessvoid.nifty.api.NiftyColor;
import de.lessvoid.nifty.api.NiftyMutableColor;
import de.lessvoid.nifty.internal.common.ColorValueParser;
import de.lessvoid.nifty.internal.math.Mat4;
import de.lessvoid.nifty.internal.render.batch.BatchManager;
import de.lessvoid.nifty.spi.NiftyRenderDevice;
import de.lessvoid.nifty.spi.NiftyTexture;

public class FontRenderer implements JGLFontRenderer {
  private final Map<String, BitmapInfo> textureInfos = new HashMap<String, BitmapInfo>();
  private final ColorValueParser colorValueParser = new ColorValueParser();
  private final NiftyRenderDevice renderDevice;
  private final NiftyMutableColor textColor = new NiftyMutableColor(NiftyColor.WHITE());
  private boolean hasColor;
  private BatchManager batchManager;

  public FontRenderer(final NiftyRenderDevice renderDevice) {
    this.renderDevice = renderDevice;
  }

  @Override
  public void registerBitmap(@Nonnull final String bitmapId, final InputStream data, @Nonnull final String filename)
      throws IOException {
    textureInfos.put(bitmapId, new BitmapInfo(renderDevice.loadTexture(filename)));
  }

  @Override
  public void registerBitmap(
      @Nonnull final String bitmapId,
      @Nonnull final ByteBuffer data,
      final int width,
      final int height,
      @Nonnull final String filename) throws IOException {
    textureInfos.put(bitmapId, new BitmapInfo(renderDevice.createTexture(width, height, data)));
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
  public void beforeRender(final Object customRenderState) {
    hasColor = false;
    batchManager = (BatchManager) customRenderState;
  }

  @Override
  public int preProcess(@Nonnull final String text, final int offset) {
    int index = offset;
    colorValueParser.isColor(text, index);
    while (colorValueParser.isColor()) {
      final NiftyColor color = colorValueParser.getColor();
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
    textureInfos.get(bitmapId).renderCharacter(c, x, y, sx, sy, textColor.getColor());
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

  // CharRenderInfo
  class CharRenderInfo {
    final int xoff;
    final int yoff;
    final int w;
    final int h;
    final float u0;
    final float v0;

    public CharRenderInfo(final int xoff, final int yoff, final int w, final int h, final float u0, final float v0) {
      this.xoff = xoff;
      this.yoff = yoff;
      this.w = w;
      this.h = h;
      this.u0 = u0;
      this.v0 = v0;
    }

    public void renderQuad(
        final NiftyTexture image,
        final int x,
        final int y,
        final float sx,
        final float sy,
        final NiftyColor textColor,
        final double u0,
        final double v0,
        final double u1,
        final double v1) {
      batchManager.addTextureQuad(image, Mat4.createIdentity(),
          (double) (x + (float) Math.floor(xoff * sx)),
          (double) (y + (float) Math.floor(yoff * sy)),
          (int) (w * sx),
          (int) (h * sy),
          u0 + this.u0,
          v0 + this.v0,
          u0 + this.u0 + calcU(w, image.getWidth()),
          v0 + this.v0 + calcU(h, image.getHeight()),
          textColor);
    }
  }

  private float calcU(final int value, final int max) {
    return (0.5f / (float) max) + (value / (float) max);
  }

  // BitmapInfo
  class BitmapInfo {
    private final NiftyTexture image;
    private final Map<Integer, CharRenderInfo> characterIndices = new HashMap<Integer, CharRenderInfo>();

    public BitmapInfo(final NiftyTexture image) {
      this.image = image;
    }

    public void addCharRenderInfo(final Integer c, final CharRenderInfo renderInfo) {
      this.characterIndices.put(c, renderInfo);
    }

    public void renderCharacter(
        final int c,
        final int x,
        final int y,
        final float sx,
        final float sy,
        final @Nonnull NiftyColor textColor) {
      characterIndices.get(c).renderQuad(image, x, y, sx, sy, textColor, image.getU0(), image.getV0(), image.getU1(),
          image.getV1());
    }
  }
}
