package de.lessvoid.nifty.processing;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.processing.input.InputSystemProcessing;
import de.lessvoid.nifty.processing.renderer.RenderDeviceProcessing;
import de.lessvoid.nifty.spi.sound.SoundDevice;
import de.lessvoid.nifty.spi.time.TimeProvider;
import processing.core.*;

/**
 * Nifty wrapper and helper class for Processing.
 * @author Xuanming
 */
public class NiftyProcessingWrapper {
	
	private final PGraphics graphics;
	private final PApplet app;
	private final Nifty nifty;
	
	/**
	 * Instantiate the Nifty wrapper for Processing.
	 * @param app PApplet instance Processing is running in.
	 * @param soundDevice Implementation of Nifty's SoundDevice interface.
	 * @param timeProvider Implementation of Nifty's TimeProvider interface.
	 */
	public NiftyProcessingWrapper(final PApplet app, final SoundDevice soundDevice, final TimeProvider timeProvider) {		
				
		// Grab PApplet and register draw() method.
		this.app = app;
		app.registerMethod("draw", this);
		
		// Create RenderDeviceProcessing and grab the PGraphics object from it.
		RenderDeviceProcessing renderDevice = new RenderDeviceProcessing(app);
		this.graphics = renderDevice.getCanvas();
		
		// Initate Nifty.
		this.nifty = new Nifty(renderDevice, soundDevice, new InputSystemProcessing(app), timeProvider);
	}
	
	/**
	 * Grab the instance of Nifty contained in this class.
	 * @return The Nifty instance.
	 */
	public Nifty getNifty() {
		return nifty;
	}
	
	/**
	 * Method attached to Processing's native draw() method using registerMethod().
	 * This gets called every frame before drawing ends in Processing.
	 * @see <a href="http://processing.org/reference/javadoc/core/processing/core/PApplet.html#registerMethod(java.lang.String,%20java.lang.Object)">PApplet.registerMethod(String, Object)</a> 
	 * @see <a href="http://processing.org/reference/javadoc/core/processing/core/PApplet.html#draw()">PApplet.draw()</a>
	 */
	public void draw() {
		
		// Update canvas.
		graphics.clear();
		graphics.beginDraw();
		nifty.update();
		nifty.render(false);
		graphics.endDraw();
		
		// Draw canvas onto screen.
		app.image(graphics, 0, 0);
	}
}
