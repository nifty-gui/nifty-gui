package de.lessvoid.nifty.spi.sound;

/**
 * @author acoppes Null implementation of SoundHandle used in java2d-renderer
 *         tests
 */
public class SoundHandleNullImpl implements SoundHandle {

	@Override
	public float getVolume() {
		return 0;
	}

	@Override
	public boolean isPlaying() {
		return false;
	}

	@Override
	public void play() {

	}

	@Override
	public void setVolume(float volume) {

	}

	@Override
	public void stop() {

	}

	public void dispose() {

	}
}