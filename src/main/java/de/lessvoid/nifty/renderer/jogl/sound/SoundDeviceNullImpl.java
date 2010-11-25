package de.lessvoid.nifty.renderer.jogl.sound;

import de.lessvoid.nifty.sound.SoundSystem;
import de.lessvoid.nifty.spi.sound.SoundDevice;
import de.lessvoid.nifty.spi.sound.SoundHandle;

/**
 * @author acoppes Null implementation of SoundDevice used in java2d-renderer tests
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
