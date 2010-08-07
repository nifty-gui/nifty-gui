package de.lessvoid.nifty.java2d.renderer;

import java.awt.Canvas;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.IOException;
import java.util.Stack;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import de.lessvoid.nifty.java2d.renderer.fonts.AngelCodeFont;
import de.lessvoid.nifty.java2d.renderer.fonts.CharacterInfo;
import de.lessvoid.nifty.render.BlendMode;
import de.lessvoid.nifty.spi.render.RenderDevice;
import de.lessvoid.nifty.spi.render.RenderFont;
import de.lessvoid.nifty.spi.render.RenderImage;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.nifty.tools.resourceloader.ResourceLoader;

public class RenderDeviceJava2dImpl implements RenderDevice {

	protected static final Logger logger = Logger
			.getLogger(RenderDeviceJava2dImpl.class.getName());

	Graphics2D graphics;

	private final Canvas canvas;

	private BufferedImage offscreenImage;

	private Rectangle clipRectangle = null;

	private FontProviderJava2dImpl fontProvider = new FontProviderJava2dImpl();

	// TODO: BlendMode?

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
		graphics = (Graphics2D) offscreenImage.getGraphics();
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

	public void setFontProvider(FontProviderJava2dImpl fontProvider) {
		this.fontProvider = fontProvider;
	}

	@Override
	public RenderFont createFont(String filename) {

		return new RenderFontJava2dImpl(this, fontProvider.getFont(filename));

		// AngelCodeFont angelCodeFont = new AngelCodeFont();
		// angelCodeFont.load(filename);
		//
		// String[] textures = angelCodeFont.getTextures();
		// Map<String, RenderImage> textureImages = new HashMap<String,
		// RenderImage>();
		//
		// for (String texture : textures)
		// textureImages.put(texture, createImage(texture, true));
		//
		// return new RenderFontJava2dWithAngelCodeImpl(this, angelCodeFont,
		// textureImages);
	}

	@Override
	public RenderImage createImage(String filename, boolean filterLinear) {
		try {
			BufferedImage image = ImageIO.read(ResourceLoader
					.getResource(filename));
			// convert the image to ARGB model
			BufferedImage bufferedImage = new BufferedImage(image
					.getWidth(null), image.getHeight(null),
					BufferedImage.TYPE_INT_ARGB);
			Graphics g = bufferedImage.getGraphics();
			g.drawImage(image, 0, 0, null);
			return new RenderImageJava2dImpl(bufferedImage);
		} catch (IOException e) {
			throw new RuntimeException("failed to create image " + filename, e);
		}
	}

	@Override
	public void disableClip() {
		clipRectangle = null;
	}

	@Override
	public void enableClip(int x0, int y0, int x1, int y1) {
		clipRectangle = new Rectangle(x0, y0, x1 - x0, y1 - y0);
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
		graphics.setColor(convertNiftyColor(color));

		width = renderImage.getWidth();
		height = renderImage.getHeight();

		RescaleOp rop = new RescaleOp(getColorScales(color), new float[4],
				new RenderingHints(RenderingHints.KEY_RENDERING,
						RenderingHints.VALUE_RENDER_SPEED));

		AffineTransform transform = new AffineTransform();

		AffineTransform translateTransform = AffineTransform
				.getTranslateInstance(-width / 2, -height / 2);
		AffineTransform scaleTransform = AffineTransform.getScaleInstance(
				imageScale, imageScale);
		AffineTransform inverseTranslateTransform = AffineTransform
				.getTranslateInstance(x + width / 2, y + height / 2);

		transform.concatenate(inverseTranslateTransform);
		transform.concatenate(scaleTransform);
		transform.concatenate(translateTransform);

		pushTransform();
		{
			graphics.transform(transform);
			graphics.drawImage(renderImage.image, rop, 0, 0);
		}
		popTransform();
	}

	Stack<AffineTransform> transformStack = new Stack<AffineTransform>();

	private void pushTransform() {
		transformStack.push(graphics.getTransform());
	}

	private void popTransform() {
		graphics.setTransform(transformStack.pop());
	}

	private float[] getColorScales(Color color) {
		float[] scales = { color.getRed(), color.getGreen(), color.getBlue(),
				color.getAlpha() };
		return scales;
	}

	@Override
	public void renderImage(RenderImage image, int x, int y, int w, int h,
			int srcX, int srcY, int srcW, int srcH, Color color, float scale,
			int centerX, int centerY) {

		if (!(image instanceof RenderImageJava2dImpl))
			return;

		RenderImageJava2dImpl renderImage = (RenderImageJava2dImpl) image;
		graphics.setClip(clipRectangle);
		graphics.setColor(convertNiftyColor(color));
		graphics.drawImage(renderImage.image, x, y, x + w, y + h, srcX, srcY,
				srcX + srcW, srcY + srcH, null);

	}

	@Override
	public void renderQuad(int x, int y, int width, int height, Color color) {
		graphics.setClip(clipRectangle);
		graphics.setColor(convertNiftyColor(color));
		graphics.fillRect(x, y, width, height);
	}

	public static boolean sameColor(Color color1, Color color2) {
		if (color1.getRed() == color2.getRed()
				&& color1.getGreen() == color2.getGreen()
				&& color1.getBlue() == color2.getBlue()
				&& color1.getAlpha() == color2.getAlpha()) {
			return true;
		}
		return false;
	}

	@Override
	public void renderQuad(int x, int y, int width, int height, Color topLeft,
			Color topRight, Color bottomRight, Color bottomLeft) {
		graphics.setClip(clipRectangle);

		// TODO: know how to do gradient of 4 colors
		Graphics2D graphics2d = (Graphics2D) graphics;

		// vertical gradient is by default
		GradientPaint grad = new GradientPaint(new Point(x, y),
				convertNiftyColor(topLeft), new Point(x + width, y),
				convertNiftyColor(bottomRight));

		// else horizontal gradient
		if (sameColor(topLeft, topRight)) {
			grad = new GradientPaint(new Point(x, y),
					convertNiftyColor(topLeft), new Point(x, y + height),
					convertNiftyColor(bottomLeft));
		}

		graphics2d.setPaint(grad);
		graphics.fillRect(x, y, width, height);
	}

	@Override
	public void setBlendMode(BlendMode blendMode) {

	}

	@Override
	public void renderFont(RenderFont font, String text, int x, int y,
			Color fontColor, float size) {

		if (font instanceof RenderFontJava2dImpl) {
			renderFontJava2dImpl(text, x, y, fontColor,
					(RenderFontJava2dImpl) font);
		} else if (font instanceof RenderFontJava2dWithAngelCodeImpl) {

			RenderFontJava2dWithAngelCodeImpl renderFont = (RenderFontJava2dWithAngelCodeImpl) font;
			AngelCodeFont angelCodeFont = renderFont.getAngelCodeFont();

			// TODO: use
			// http://goldenstudios.or.id/forum/showthread.php?tid=1529 as
			// reference implementation or
			// http://slick.javaunlimited.net/viewtopic.php?p=844&sid=9bfbbee23dd97e95b10ead16f9976a81

			char[] charArray = text.toCharArray();
			String[] textures = angelCodeFont.getTextures();

			for (int i = 0; i < text.length(); i++) {

				char c = text.charAt(i);
				char nextCharacter = i + 1 < text.length() ? text.charAt(i + 1)
						: 0;

				CharacterInfo charInfo = angelCodeFont.getChar(c);

				int kerning = 0;
				float characterWidth = 0;

				if (charInfo == null)
					break;

				int textureId = charInfo.getPage();

				String texture = textures[textureId];

				RenderImage renderImage = renderFont.getRenderImage(texture);

				// kerning = charInfo.getKerning().get(nextCharacter);
				// characterWidth = (float) (charInfo.getXadvance() * size +
				// kerning);

				renderImage(renderImage, x, y, 200, // 
						200, charInfo.getX(), charInfo.getY(), // 
						charInfo.getWidth(), charInfo.getHeight(), //  
						fontColor, size, 0, 0);
			}

		}
	}

	private void renderFontJava2dImpl(String text, int x, int y,
			Color fontColor, RenderFontJava2dImpl renderFont) {
		graphics.setClip(clipRectangle);
		graphics.setFont(renderFont.getFont());
		graphics.setColor(convertNiftyColor(fontColor));
		graphics.drawString(text, x, y);
	}

}