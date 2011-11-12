package de.lessvoid.nifty.java2d.renderer;

import java.util.Map;

import de.lessvoid.nifty.java2d.renderer.fonts.AngelCodeFont;
import de.lessvoid.nifty.spi.render.RenderFont;
import de.lessvoid.nifty.spi.render.RenderImage;

public class RenderFontJava2dWithAngelCodeImpl implements RenderFont {

	private final RenderDeviceJava2dImpl renderDevice;

	private final AngelCodeFont angelCodeFont;
	
	/**
	 * preloaded images for each texture id of AngelCodeFont.
	 */
	private final Map<String, RenderImage> textureImages;
	
	public AngelCodeFont getAngelCodeFont() {
		return angelCodeFont;
	}

	public RenderFontJava2dWithAngelCodeImpl(
			RenderDeviceJava2dImpl renderDevice, AngelCodeFont angelCodeFont, Map<String, RenderImage> textureImages) {
		this.renderDevice = renderDevice;
		this.angelCodeFont = angelCodeFont;
		this.textureImages = textureImages;
	}

	@Override
	public int getCharacterAdvance(char currentCharacter,
			char nextCharacter, float size) {
		return 0;
	}

	@Override
	public int getHeight() {
		return 0;
	}

  @Override
  public int getWidth(String text) {
    return getWidth(text, 1.f);
  }

	@Override
	public int getWidth(String text, float size) {
		return 0;
	}

	public void dispose() {
		
	}

	public RenderImage getRenderImage(String texture) {
		return textureImages.get(texture);
	}
}