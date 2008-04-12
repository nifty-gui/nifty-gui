package de.lessvoid.nifty.sound.slick;

import junit.framework.TestCase;

import org.easymock.classextension.EasyMock;
import org.newdawn.slick.Sound;

import de.lessvoid.nifty.sound.SoundSystem;
import de.lessvoid.nifty.sound.slick.SlickSoundHandle;

public class SlickSoundHandleTest extends TestCase {
  
  private SoundSystem soundSystem;
  
  public void setUp() {
    soundSystem = EasyMock.createMock(SoundSystem.class);
    EasyMock.expect(soundSystem.getSoundVolume()).andReturn(0.55f);
    EasyMock.replay(soundSystem);
  }
  
  public void testPlay() {
    Sound sound = EasyMock.createMock(Sound.class);
    sound.play(1.0f, 0.55f);
    EasyMock.replay(sound);

    SlickSoundHandle slickSoundHandle = new SlickSoundHandle(soundSystem, sound);
    slickSoundHandle.play();
    
    EasyMock.verify(soundSystem);
    EasyMock.verify(sound);
  }
}
