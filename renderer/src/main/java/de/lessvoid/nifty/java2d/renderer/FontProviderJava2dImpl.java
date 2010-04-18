package de.lessvoid.nifty.java2d.renderer;

import java.awt.Font;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class FontProviderJava2dImpl {

	protected static final Logger logger = Logger
			.getLogger(FontProviderJava2dImpl.class.getName());

	private Map<String, java.awt.Font> fonts = new HashMap<String, Font>();

	/**
	 * to be returned if getFont fail to get font
	 */
	private java.awt.Font defaultFont = new Font("arial", Font.PLAIN, 17);

	public void setDefaultFont(java.awt.Font defaultFont) {
		this.defaultFont = defaultFont;
	}

	public void addFont(String name, java.awt.Font font) {
		fonts.put(name, font);
	}

	public Font getFont(String name) {
		if (!fonts.containsKey(name))
			return getDefaultFont(name);
		return fonts.get(name);
	}

	private Font getDefaultFont(String name) {
		logger.info("cannot found " + name
				+ " font, using default font instead");
		if (defaultFont != null)
			return defaultFont;
		throw new RuntimeException("cannot found " + name + " font");
	}

}