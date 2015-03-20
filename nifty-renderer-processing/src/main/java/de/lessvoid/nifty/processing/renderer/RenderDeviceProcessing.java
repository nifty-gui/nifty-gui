package de.lessvoid.nifty.processing.renderer;

import java.io.File;
import java.net.URISyntaxException;

import de.lessvoid.nifty.render.BlendMode;
import de.lessvoid.nifty.spi.render.*;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.nifty.tools.resourceloader.NiftyResourceLoader;
import de.lessvoid.nifty.tools.resourceloader.FileSystemLocation;
import processing.core.*;

/**
 * Implementation of Nifty's RenderDevice for Processing.
 * @author Xuanming
 */
public class RenderDeviceProcessing implements RenderDevice {
	
	private final PGraphics graphics;
	private final PApplet app;
	private NiftyResourceLoader resourceLoader;
	private MouseCursor mouseCursor;
	
	/**
	 * Instantiate RenderDeviceProcessing.
	 * @param app PApplet instance that Processing is currently running in.
	 */
	public RenderDeviceProcessing(PApplet app) {
		this(app, app.width, app.height, true);
	}
	
	/**
	 * Instantiate RenderDeviceProcessing (verbose version)
	 * @param app PApplet instance that Processing is currently running in.
	 * @param width Desired width of Nifty instance.
	 * @param height Desired height of Nifty instance.
	 */
	public RenderDeviceProcessing(PApplet app, int width, int height, boolean render) {
		this.graphics = app.createGraphics(width, height);
		this.app = app;
		
		// If self render is turned on, register this object with Processing draw method.
		if (render) {
			app.registerMethod("draw", this);
		}
		
		/* 
		 * All classes in Processing are inner classes of the Processing PApplet instance.
		 * Creating a helper property to assist in finding inner ScreenController classes from XML.
		 * Using this property, users just need to specify 'controller="${PROP.APP}ControllerName"' in
		 * their XML layouts, where ControllerName is the name of the actual ScreenController impl.
		 */
		System.setProperty("APP", app.getClass().getName() + "$");
	}
	
	/**
	 * Draw method called from Processing if self render is turned on.
	 * Draw canvas onto Processing window.
	 */
	public void draw() {
		app.image(graphics, 0, 0);
	}

	@Override
	public void setResourceLoader(NiftyResourceLoader niftyResourceLoader) {
		this.resourceLoader = niftyResourceLoader;
		resourceLoader.addResourceLocation(new FileSystemLocation(new File(app.dataPath(""))));
	}

	@Override
	public RenderImage createImage(String filename, boolean filterLinear) {
		return new RenderImageProcessing(app.dataPath(filename), app);
	}

	@Override
	public RenderFont createFont(String filename) {
		try {
			return new RenderFontProcessing(app, graphics, resourceLoader.getResource(filename).toURI().getPath());
		} catch (URISyntaxException e) {
			return new RenderFontProcessing(app, graphics, resourceLoader.getResource(filename).getPath());
		}
	}

	@Override
	public int getWidth() {
		return graphics.width;
	}

	@Override
	public int getHeight() {
		return graphics.height;
	}

	@Override
	public void beginFrame() {
		graphics.beginDraw();
		clear();
	}

	@Override
	public void endFrame() {
		graphics.endDraw();
	}

	@Override
	public void clear() {
		graphics.clear();
	}

	@Override
	public void setBlendMode(BlendMode renderMode) {
		switch (renderMode) {
		case BLEND:
			graphics.blendMode(PConstants.BLEND);
			break;
			
		case MULIPLY:
			graphics.blendMode(PConstants.MULTIPLY);
			break;			
		}
	}

	@Override
	public void renderQuad(int x, int y, int width, int height, Color color) {
		
		// Draw rectangle.
		graphics.noStroke();
		graphics.fill(convertColor(color));
		graphics.rect(x, y, width, height);
	}

	@Override
	public void renderQuad(int x, int y, int width, int height, Color topLeft,
			Color topRight, Color bottomRight, Color bottomLeft) {
		
		// Convert colors.
		int topLeftC = convertColor(topLeft);
		int topRightC = convertColor(topRight);
		int bottomLeftC = convertColor(bottomLeft);
		int bottomRightC = convertColor(bottomRight);		
		
		// Draw rectangle using pixels[] array.
		graphics.loadPixels();
		for (int k = y; k < y + height; k++) {
			for (int i = x; i < x + width; i++) {
				float xRange = PApplet.map(i, x, x + width, 0, 1);
				float yRange = PApplet.map(k, y, y + height, 0, 1);
				graphics.pixels[(k * graphics.width) + i] = 
					app.lerpColor(
						app.lerpColor(bottomLeftC, bottomRightC, xRange),
						app.lerpColor(topLeftC, topRightC, xRange),
						yRange
					)
				;
			}
		}
		graphics.updatePixels();
	}

	@Override
	public void renderImage(RenderImage image, int x, int y, int width,
			int height, Color color, float imageScale) {
		PImage img;	
		if (width > 0 && height > 0 && imageScale > 0.0) {
			if (image instanceof RenderImageProcessing){
				img = ((RenderImageProcessing) image).resize(width, height, imageScale);
				graphics.tint(convertColor(color));
				graphics.image(img, x, y);
				graphics.noTint();
			}
		}
	}

	@Override
	public void renderImage(RenderImage image, int x, int y, int w, int h,
			int srcX, int srcY, int srcW, int srcH, Color color, float scale,
			int centerX, int centerY) {
		PImage img;
		if (w > 0 && h > 0 && scale > 0.0) {
			if (image instanceof RenderImageProcessing) {
				img = ((RenderImageProcessing) image).crop(srcX, srcY, srcW, srcH);
				img.resize(w, h);
				img.resize(Math.round(img.width * scale), Math.round(img.height * scale));
				graphics.tint(convertColor(color));
				graphics.image(img, x, y);
				graphics.noTint();
			}
		}
	}

	@Override
	public void renderFont(RenderFont font, String text, int x, int y,
			Color fontColor, float sizeX, float sizeY) {
		if (font instanceof RenderFontProcessing){
			graphics.textFont(((RenderFontProcessing) font).getFont());
			graphics.textSize(((RenderFontProcessing) font).getSize() * sizeX);
			graphics.fill(convertColor(fontColor));
			graphics.text(text, x, y + (int)((graphics.textDescent() + graphics.textAscent()) * 1.21));
		}
	}

	@Override
	public void enableClip(int x0, int y0, int x1, int y1) {
		graphics.clip(x0, y0, x1 - x0, y1 - y0);
		
	}

	@Override
	public void disableClip() {
		graphics.noClip();
	}

	@Override
	public MouseCursor createMouseCursor(String filename, int hotspotX, int hotspotY) {
		return new MouseCursorProcessing(app, app.dataPath(filename), hotspotX, hotspotY);
	}

	@Override
	public void enableMouseCursor(MouseCursor mouseCursor) {
		this.mouseCursor = mouseCursor;
		mouseCursor.enable();
	}

	@Override
	public void disableMouseCursor() {
		if (mouseCursor != null) {
			mouseCursor.disable();
		}
	}
	
	/**
	 * Get the Processing PGraphics object for external uses.
	 * @return PGraphics object that Nifty draws on.
	 */
	public PGraphics getCanvas() {
		return graphics;
	}
	
	/**
	 * Convert the Nifty Color data type to the Processing one.
	 * @param c
	 * @return
	 */
	private int convertColor(Color c) {
		return 
			app.color(
				c.getRed() * 255, 
				c.getGreen() * 255,
				c.getBlue() * 255,
				c.getAlpha() * 255
			)
		;
	}
}
