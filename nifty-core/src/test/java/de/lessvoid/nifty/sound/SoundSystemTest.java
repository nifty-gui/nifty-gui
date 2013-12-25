package de.lessvoid.nifty.sound;

import de.lessvoid.nifty.spi.sound.SoundDevice;
import de.lessvoid.nifty.spi.sound.SoundHandle;
import junit.framework.TestCase;
import org.easymock.EasyMock;

public class SoundSystemTest extends TestCase {
  static {
    System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
    System.setProperty("org.apache.commons.logging.simplelog.defaultlog", "debug");
  }

  private SoundDevice soundLoader;
  private SoundSystem soundSystem;

  @Override
  public void setUp() {
    soundLoader = EasyMock.createMock(SoundDevice.class);
    soundSystem = new SoundSystem(soundLoader);
  }

  public void testAddSoundLoadOk() {
    SoundHandle soundHandle = new SoundHandle() {
      @Override
      public void play() {
      }

      @Override
      public void stop() {
      }

      @Override
      public void setVolume(float volume) {
      }

      @Override
      public float getVolume() {
        return 0.0f;
      }

      @Override
      public boolean isPlaying() {
        return false;
      }

      @Override
      public void dispose() {
      }
    };
    prepareSoundLoader(soundHandle);

    assertTrue(soundSystem.addSound("mySound", "filename"));
    assertEquals(soundHandle, soundSystem.getSound("mySound"));

    verifySoundLoader();
  }

  public void testAddSoundLoadFaied() {
    prepareSoundLoader(null);

    assertFalse(soundSystem.addSound("mySound", "filename"));
    assertNull(soundSystem.getSound("mySound"));

    verifySoundLoader();
  }

  public void testAddMusicLoadOk() {
    SoundHandle soundHandle = new SoundHandle() {
      @Override
      public void play() {
      }

      @Override
      public void stop() {
      }

      @Override
      public void setVolume(float volume) {
      }

      @Override
      public float getVolume() {
        return 0.0f;
      }

      @Override
      public boolean isPlaying() {
        return false;
      }

      @Override
      public void dispose() {
      }
    };
    prepareMusicLoader(soundHandle);

    assertTrue(soundSystem.addMusic("myMusic", "filename"));
    assertEquals(soundHandle, soundSystem.getSound("myMusic"));

    verifySoundLoader();
  }

  public void testAddMusicLoadFaied() {
    prepareMusicLoader(null);

    assertFalse(soundSystem.addMusic("myMusic", "filename"));
    assertNull(soundSystem.getSound("myMusic"));

    verifySoundLoader();
  }

  private void prepareSoundLoader(SoundHandle soundHandle) {
    EasyMock.expect(soundLoader.loadSound(soundSystem, "filename")).andReturn(soundHandle);
    EasyMock.replay(soundLoader);
  }

  private void prepareMusicLoader(SoundHandle musicHandle) {
    EasyMock.expect(soundLoader.loadMusic(soundSystem, "filename")).andReturn(musicHandle);
    EasyMock.replay(soundLoader);
  }

  private void verifySoundLoader() {
    EasyMock.verify(soundLoader);
  }

}
