package de.lessvoid.nifty.java2d.renderer;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.net.URL;

import de.lessvoid.nifty.render.BlendMode;
import de.lessvoid.nifty.spi.render.RenderDevice;
import de.lessvoid.nifty.spi.render.RenderFont;
import de.lessvoid.nifty.spi.render.RenderImage;
import de.lessvoid.nifty.tools.Color;

public class RenderDeviceJava2dImpl implements RenderDevice {

	Graphics graphics;

	private final Canvas canvas;

	private BufferedImage offscreenImage;

	private Rectangle clipRectangle = null;

	private java.awt.Color convertNiftyColor(Color color) {
		return new java.awt.Color(color.getRed(), color.getGreen(), color
				.getBlue(), color.getAlpha());
	}

	public RenderDeviceJava2dImpl(Canvas canvas) {
		this.canvas = canvas;
		offscreenImage = (BufferedImage) canvas.createImage(getWidth(),
				getHeight());
	}

	@Override
	public void beginFrame() {

		if (screenSizeHasChanged())
			offscreenImage = (BufferedImage) canvas.createImage(getWidth(),
					getHeight());
		graphics = offscreenImage.getGraphics();
	}

	private boolean screenSizeHasChanged() {
		return (offscreenImage.getWidth() != getWidth() || offscreenImage
				.getHeight() != getHeight());
	}

	@Override
	public void endFrame() {
		canvas.getGraphics().drawImage(offscreenImage, 0, 0, null);
	}

	@Override
	public void clear() {
		graphics.clearRect(0, 0, getWidth(), getHeight());
	}

	private FontProviderJava2dImpl fontProvider = new FontProviderJava2dImpl();
	
	public void setFontProvider(FontProviderJava2dImpl fontProvider) {
		this.fontProvider = fontProvider;
	}

	@Override
	public RenderFont createFont(String filename) {
		return new RenderFontJava2dImpl(this, fontProvider.getFont(filename));
	}

	@Override
	public RenderImage createImage(String filename, boolean filterLinear) {
		URL imageUrl = Thread.currentThread().getContextClassLoader()
				.getResource(filename);
		Toolkit tk = Toolkit.getDefaultToolkit();
		Image image = tk.getImage(imageUrl);
		return new RenderImageJava2dImpl(image);
	}

	@Override
	public void disableClip() {
		clipRectangle = null;
	}

	@Override
	public void enableClip(int x0, int y0, int x1, int y1) {
		clipRectangle = new Rectangle(x0, y1, x1 - x0, y1 - y0);
	}

	@Override
	public int getHeight() {
		return canvas.getSize().height;
	}

	@Override
	public int getWidth() {
		return canvas.getSize().width;
	}

	@Override
	public void renderImage(RenderImage image, int x, int y, int width,
			int height, Color color, float imageScale) {

		if (!(image instanceof RenderImageJava2dImpl))
			return;

		RenderImageJava2dImpl renderImage = (RenderImageJava2dImpl) image;
		graphics.setClip(clipRectangle);
		graphics.drawImage(renderImage.image, x, y, x + width, y + height, 0,
				0, renderImage.getWidth(), renderImage.getHeight(), null);
	}

	@Override
	public void renderImage(RenderImage image, int x, int y, int w, int h,
			int srcX, int srcY, int srcW, int srcH, Color color, float scale,
			int centerX, int centerY) {

		if (!(image instanceof RenderImageJava2dImpl))
			return;

		RenderImageJava2dImpl renderImage = (RenderImageJava2dImpl) image;
		graphics.setClip(clipRectangle);
		graphics.drawImage(renderImage.image, x, y, x + w, y + h, srcX, srcY,
				srcX + srcW, srcY + srcH, null);

	}

	@Override
	public void renderQuad(int x, int y, int width, int height, Color color) {
		graphics.setClip(clipRectangle);
		graphics.setColor(convertNiftyColor(color));
		graphics.fillRect(x, y, width, height);
	}

	@Override
	public void renderQuad(int x, int y, int width, int height, Color topLeft,
			Color topRight, Color bottomRight, Color bottomLeft) {
		graphics.setClip(clipRectangle);
		graphics.setColor(convertNiftyColor(topLeft));
		graphics.fillRect(x, y, width, height);
	}

	@Override
	public void setBlendMode(BlendMode renderMode) {

	}

	@Override
	public void renderFont(RenderFont font, String text, int x, int y,
			Color fontColor, float size) {

		if (!(font instanceof RenderFontJava2dImpl))
			return;

		RenderFontJava2dImpl renderFont = (RenderFontJava2dImpl) font;
		graphics.setClip(clipRectangle);
		graphics.setFont(renderFont.getFont());
		graphics.setColor(convertNiftyColor(fontColor));
		graphics.drawString(text, x, y);
	}

}