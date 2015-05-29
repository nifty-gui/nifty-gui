package de.lessvoid.nifty.processing.time;

import de.lessvoid.nifty.spi.time.TimeProvider;
import processing.core.*;

/**
 * Temporary jury-rigging. Sneaked a function to return a refernce to PApplet here.
 * For use whenever a class or function requires Nifty to return instance of PApplet.
 * @author Xuanming
 */
public class TimeProviderProcessing implements TimeProvider {
	
	private final PApplet app;
	
	public TimeProviderProcessing(final PApplet app) {
		this.app = app;
	}
	
	/**
	 * Get the currently-running PApplet instance.
	 * @return PApplet instance currently in use.
	 */
	public PApplet getPApplet() {
		return app;
	}

	@Override
	public long getMsTime() {
		return (long)app.millis();
	}
}