package de.lessvoid.nifty.sound.paulssoundsystem;

import paulscode.sound.SoundSystemConfig;
import paulscode.sound.SoundSystemException;
import paulscode.sound.codecs.CodecJOrbis;
import paulscode.sound.codecs.CodecWav;
import de.lessvoid.nifty.sound.SoundSystem;
import de.lessvoid.nifty.spi.sound.SoundDevice;
import de.lessvoid.nifty.spi.sound.SoundHandle;
import de.lessvoid.nifty.tools.resourceloader.NiftyResourceLoader;

public class PaulsSoundsystemSoundDevice implements SoundDevice {
  private paulscode.sound.SoundSystem soundSystem;
  private int counter = 0;

  public PaulsSoundsystemSoundDevice(final Class<?> libraryClass, final SupportedCodec ... additionalCodecs) throws SoundSystemException {
    SoundSystemConfig.setSoundFilesPackage("");
    SoundSystemConfig.addLibrary(libraryClass);

    // we decided that ogg and wav are build in
    SoundSystemConfig.setCodec("ogg", CodecJOrbis.class);
    SoundSystemConfig.setCodec("wav", CodecWav.class);

    addAdditionalCodecs(additionalCodecs);
    soundSystem = new paulscode.sound.SoundSystem();
  }

  @Override
  public void setResourceLoader(final NiftyResourceLoader resourceLoader) {
  }

  private void addAdditionalCodecs(final SupportedCodec... codecs) throws SoundSystemException {
    if (codecs != null) {
      for (SupportedCodec codec : codecs) {
        SoundSystemConfig.setCodec(codec.getExtension(), codec.getCodecClass());
      }
    }
  }

  public SoundHandle loadSound(final SoundSystem soundSystem, final String filename) {
    return new PaulsSoundHandle(this.soundSystem, filename);
  }

  public SoundHandle loadMusic(final SoundSystem soundSystem, final String filename) {
    return new PaulsMusicHandle(this.soundSystem, generateId(), filename);
  }

  public void update(final int delta) {
  }

  private String generateId() {
    return String.valueOf(counter++);
  }
}
