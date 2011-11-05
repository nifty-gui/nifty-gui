package de.lessvoid.nifty.java2d.renderer;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import de.lessvoid.nifty.spi.render.RenderFont;

public class RenderFontJava2dImpl implements RenderFont {

	private final Font font;

	private final RenderDeviceJava2dImpl renderDevice;

	Font getFont() {
		return font;
	}

	public RenderFontJava2dImpl(RenderDeviceJava2dImpl renderDevice, Font font) {
		this.renderDevice = renderDevice;
		this.font = font;
	}

	@Override
	public int getCharacterAdvance(char currentCharacter,
			char nextCharacter, float size) {
		// I don't know exactly what to do here.
		return getFont().getSize();
	}

	@Override
	public int getHeight() {
		Graphics graphics = renderDevice.getGraphics();

		if (graphics == null)
			return 0;

		FontMetrics fontMetrics = graphics.getFontMetrics(font);
		double height = fontMetrics.getStringBounds("A", graphics).getHeight();
		return (int) height;
	}

  @Override
  public int getWidth(String text) {
    return getWidth(text, 1.f);
  }

	@Override
	public int getWidth(String text, float size) {
		Graphics graphics = renderDevice.getGraphics();

		if (graphics == null)
			return 0;

		FontMetrics fontMetrics = graphics.getFontMetrics(font);
		double width = fontMetrics.getStringBounds(text, graphics).getWidth();
		return (int) width;
	}

	public void dispose() {
		
	}
}