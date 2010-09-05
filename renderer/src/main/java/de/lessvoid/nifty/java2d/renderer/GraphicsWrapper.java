package de.lessvoid.nifty.java2d.renderer;

import java.awt.Graphics2D;

/**
 * Used by RenderDeviceJava2dImpl to render without knowing about which drawing
 * strategy is being used.
 * 
 * @author acoppes
 */
public interface GraphicsWrapper {

	Graphics2D beginFrame();

	int getHeight();

	int getWidth();

	void endFrame();

}