package de.lessvoid.nifty.spi.sound;

import de.lessvoid.nifty.sound.SoundSystem;

/**
 * @author acoppes Null implementation of SoundDevice
 */
public class SoundDeviceNullImpl implements SoundDevice {

	@Override
	public SoundHandle loadMusic(SoundSystem soundSystem, String filename) {
		return new SoundHandleNullImpl();
	}

	@Override
	public SoundHandle loadSound(SoundSystem soundSystem, String filename) {
		return new SoundHandleNullImpl();
	}

	@Override
	public void update(int delta) {

	}

}