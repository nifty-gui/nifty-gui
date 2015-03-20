package de.lessvoid.nifty.sound.minim;

import ddf.minim.Minim;
import de.lessvoid.nifty.sound.SoundSystem;
import de.lessvoid.nifty.spi.sound.SoundDevice;
import de.lessvoid.nifty.spi.sound.SoundHandle;
import de.lessvoid.nifty.tools.resourceloader.NiftyResourceLoader;

/**
 * Implementation of Nifty's SoundDevice for the Minim Java audio library.
 * Minim is an audio library primarily built for use with Processing.
 * @author Xuanming
 */
public class SoundDeviceMinim implements SoundDevice {
	
	Minim minim;
	NiftyResourceLoader resourceLoader;
	
	/**
	 * Create an instance of SoundDeviceMinim.
	 * @param minim Instance of Minim currently in use by Processing (or Java).
	 */
	public SoundDeviceMinim(Minim minim){
		this.minim = minim;
	}

	@Override
	public void setResourceLoader(NiftyResourceLoader niftyResourceLoader) {
		this.resourceLoader = niftyResourceLoader;
	}

	@Override
	public SoundHandle loadSound(SoundSystem soundSystem, String filename) {
		return new SoundHandleMinim(minim, resourceLoader.getResource(filename).getPath());
	}

	@Override
	public SoundHandle loadMusic(SoundSystem soundSystem, String filename) {
		return new SoundHandleMinim(minim, resourceLoader.getResource(filename).getPath());
	}

	@Override
	public void update(int delta) { // Do nothing.
	}
}
