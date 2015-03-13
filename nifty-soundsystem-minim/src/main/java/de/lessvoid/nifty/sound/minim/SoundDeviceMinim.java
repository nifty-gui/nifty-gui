package de.lessvoid.nifty.sound.minim;

import ddf.minim.Minim;
import de.lessvoid.nifty.sound.SoundSystem;
import de.lessvoid.nifty.spi.sound.SoundDevice;
import de.lessvoid.nifty.spi.sound.SoundHandle;
import de.lessvoid.nifty.tools.resourceloader.NiftyResourceLoader;

public class SoundDeviceMinim implements SoundDevice {
	
	Minim minim;
	
	public SoundDeviceMinim(Minim minim){
		this.minim = minim;
	}

	@Override
	public void setResourceLoader(NiftyResourceLoader niftyResourceLoader) {
	}

	@Override
	public SoundHandle loadSound(SoundSystem soundSystem, String filename) {
		return new SoundHandleMinim(minim, filename);
	}

	@Override
	public SoundHandle loadMusic(SoundSystem soundSystem, String filename) {
		return new SoundHandleMinim(minim, filename);
	}

	@Override
	public void update(int delta) {
	}
}
