package de.lessvoid.nifty.processing.renderer;

import de.lessvoid.nifty.spi.render.RenderFont;
import processing.core.PFont;
import processing.core.PGraphics;
import processing.core.PApplet;
import java.io.File;

/**
 * Implementation of Nifty's RenderFont interface using Processing's own
 * VLW bitmap font format.
 * @author Xuanming
 */
public class RenderFontProcessing implements RenderFont {
	
	private PFont font;
	private PGraphics canvas;
	
	/**
	 * Create an instance of RenderFontProcessing.
	 * @param app PApplet instance Processing is currently running in.
	 * @param canvas PGraphics canvas Nifty is being drawn on.
	 * @param filename Path to the .vlw font file.
	 * @throws IOException 
	 */
	public RenderFontProcessing(PApplet app, PGraphics canvas, String filename) {
		
		this.canvas = canvas;
		if (fileExists(filename)) {
			if ((filename.substring(filename.length() - 3)).equals("vlw")) {				
				this.font = app.loadFont(filename);
			} else {
				System.err.println(filename + " is an invalid filetype, only Processing VLW fonts are accepted.");
			}
		} else {			
			System.err.println("File " + filename + " not found.");
		}
	}

	@Override
	public int getWidth(String text) {
		canvas.textFont(font);
		return (int)canvas.textWidth(text);
	}

	@Override
	public int getWidth(String text, float size) {
		canvas.textFont(font);
		return (int)(canvas.textWidth(text) * size);
	}

	@Override
	public int getHeight() {
		canvas.textFont(font);
		return (int)((canvas.textDescent() + canvas.textAscent()) * 1.42);
	}

	@Override
	public int getCharacterAdvance(char currentCharacter, char nextCharacter,
			float size) {
		canvas.textFont(font);
		return (int)(canvas.textWidth(currentCharacter) * size);
	}

	@Override
	public void dispose() { // No dispose method.
	}
	
	public PFont getFont() {
		return font;
	}
	
	public int getSize() {
		return font.getSize();
	}
	
	private boolean fileExists(String filename) {
		File f = new File(filename);
		return f.exists(); 
	}
}
