package de.lessvoid.nifty.slick2d.render;

import java.io.IOException;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.fills.GradientFill;
import org.newdawn.slick.geom.Rectangle;

import de.lessvoid.nifty.render.BlendMode;
import de.lessvoid.nifty.spi.render.MouseCursor;
import de.lessvoid.nifty.spi.render.RenderDevice;
import de.lessvoid.nifty.spi.render.RenderFont;
import de.lessvoid.nifty.spi.render.RenderImage;
import de.lessvoid.nifty.tools.Color;

public class SlickRenderDevice implements RenderDevice {
  private GameContainer gameContainer;

  public SlickRenderDevice(final GameContainer gameContainer) {
    this.gameContainer = gameContainer;
  }

  public int getWidth() {
    return gameContainer.getWidth();
  }

  public int getHeight() {
    return gameContainer.getHeight();
  }

  public void beginFrame() {
    gameContainer.getGraphics().clearClip();
  }

  public void endFrame() {
  }

  public void clear() {
    gameContainer.getGraphics().clear();
  }

  public RenderImage createImage(final String filename, final boolean filterLinear) {
    return new SlickRenderImage(filename, filterLinear);
  }

  public RenderFont createFont(final String filename) {
    return new SlickRenderFont(filename, this);
  }

  public void renderQuad(final int x, final int y, final int width, final int height, final Color color) {
    gameContainer.getGraphics().setColor(convertToSlickColor(color));
    gameContainer.getGraphics().fillRect(x, y, width, height);
  }

  public void renderQuad(final int x, final int y, final int width, final int height, final Color topLeft, final Color topRight, final Color bottomRight, final Color bottomLeft) {
    GradientFill gradient = null;
    if (sameColor(topLeft, topRight)) {
      gradient = new GradientFill(x, y, convertToSlickColor(topLeft), x, y + height, convertToSlickColor(bottomLeft));
    } else {
      gradient = new GradientFill(x, y, convertToSlickColor(topLeft), x + width, y, convertToSlickColor(bottomRight));
    }
    Rectangle rect = new Rectangle(x, y, width, height);
    gameContainer.getGraphics().fill(rect, gradient);
  }

  public void renderImage(final RenderImage image, final int x, final int y, final int width, final int height, final Color color, final float scale) {
//    log.fine("renderImage()");
//
//    if (!currentTexturing) {
//      GL11.glEnable(GL11.GL_TEXTURE_2D);
//      currentTexturing = true;
//    }
//    GL11.glPushMatrix();
//    GL11.glTranslatef(x + width / 2, y + height / 2, 0.0f);
//    GL11.glScalef(scale, scale, 1.0f);
//    GL11.glTranslatef(-(x + width / 2), -(y + height / 2), 0.0f);
//    ((RenderImageSlick)image).getImage().bind();
//    ((RenderImageSlick)image).getImage().draw(x, y, width, height, convertToSlickColor(color));
//    GL11.glPopMatrix();
  }

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
      final Color color,
      final float scale,
      final int centerX,
      final int centerY) {
//    log.fine("renderImage2()");
//
//    if (!currentTexturing) {
//      GL11.glEnable(GL11.GL_TEXTURE_2D);
//      currentTexturing = true;
//    }
//    GL11.glPushMatrix();
//    GL11.glTranslatef(centerX, centerY, 0.0f);
//    GL11.glScalef(scale, scale, 1.0f);
//    GL11.glTranslatef(-(centerX), -(centerY), 0.0f);
//    ((RenderImageSlick)image).getImage().bind();
//    ((RenderImageSlick)image).getImage().draw(x, y, x + w, y + h, srcX, srcY, srcX + srcW, srcY + srcH, convertToSlickColor(color));
//    GL11.glPopMatrix();
  }

  public void renderFont(final RenderFont font, final String text, final int x, final int y, final Color color, final float fontSizeX, final float fontSizeY) {
//    log.fine("renderFont()");
//
//    if (!currentTexturing) {
//      GL11.glEnable(GL11.GL_TEXTURE_2D);
//      currentTexturing = true;
//    }
//    setBlendMode(BlendMode.BLEND);
//    if (color == null) {
//      ((RenderFontSlick)font).getFont().drawStringWithSize(x, y, text, fontSize);
//    } else {
//      ((RenderFontSlick)font).getFont().renderWithSizeAndColor(x, y, text, fontSize, color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
//    }
  }

  public void enableClip(final int x0, final int y0, final int x1, final int y1) {
    gameContainer.getGraphics().setClip(x0, y0, x1 - x0, y1 - y0);
  }

  public void disableClip() {
    gameContainer.getGraphics().clearClip();
  }

  public void setBlendMode(final BlendMode renderMode) {
    if (BlendMode.BLEND.equals(renderMode)) {
      gameContainer.getGraphics().setDrawMode(Graphics.MODE_NORMAL);
    } else if (BlendMode.MULIPLY.equals(renderMode)) {
      gameContainer.getGraphics().setDrawMode(Graphics.MODE_COLOR_MULTIPLY);
    }
  }

  private org.newdawn.slick.Color convertToSlickColor(final Color color) {
    if (color != null) {
      return new org.newdawn.slick.Color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    } else {
      return new org.newdawn.slick.Color(1.0f, 1.0f, 1.0f, 1.0f);
    }
  }

  private boolean sameColor(final Color color1, final Color color2) {
    if ( color1.getRed() == color2.getRed() &&
         color1.getGreen() == color2.getGreen() &&
         color1.getBlue() == color2.getBlue() &&
         color1.getAlpha() == color2.getAlpha()) {
      return true;
    }
    return false;
  }

  @Override
  public MouseCursor createMouseCursor(String filename, int hotspotX, int hotspotY) throws IOException {
    return null;
  }

  @Override
  public void enableMouseCursor(MouseCursor mouseCursor) {
  }

  @Override
  public void disableMouseCursor() {
  }
}
