package de.lessvoid.nifty.sound;

import org.easymock.EasyMock;

import de.lessvoid.nifty.sound.SoundSystem;
import de.lessvoid.nifty.spi.sound.SoundDevice;
import de.lessvoid.nifty.spi.sound.SoundHandle;

import junit.framework.TestCase;

public class SoundSystemTest extends TestCase {
  static {
    System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
    System.setProperty("org.apache.commons.logging.simplelog.defaultlog", "debug");
  }

  private SoundDevice soundLoader;
  private SoundSystem soundSystem;
  
  public void setUp() {
    soundLoader = EasyMock.createMock(SoundDevice.class);
    soundSystem = new SoundSystem(soundLoader);
  }
  
  public void testAddSoundLoadOk() {
    SoundHandle soundHandle = new SoundHandle() {
      public void play() {}
      public void stop() {}
      public void setVolume(float volume) {}
      public float getVolume() {return 0.0f;}
      public boolean isPlaying() { return false; }
      public void dispose() {}
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
      public void play() {}
      public void stop() {}
      public void setVolume(float volume) {}
      public float getVolume() {return 0.0f;}
      public boolean isPlaying() { return false; }
      public void dispose() {}
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
