package de.lessvoid.nifty.sound.spi.slick;

import org.easymock.classextension.EasyMock;
import org.newdawn.slick.Music;

import de.lessvoid.nifty.sound.SoundSystem;
import de.lessvoid.nifty.sound.spi.slick.SlickMusicHandle;
import junit.framework.TestCase;

public class SlickMusicHandleTest extends TestCase {
  
  private SoundSystem soundSystem;
  
  public void setUp() {
    soundSystem = EasyMock.createMock(SoundSystem.class);
    EasyMock.expect(soundSystem.getMusicVolume()).andReturn(0.55f);
    EasyMock.replay(soundSystem);
  }
  
  public void testPlay() {
    Music music = EasyMock.createMock(Music.class);
    music.play(1.0f, 0.55f);
    EasyMock.replay(music);

    SlickMusicHandle slickMusicHandle = new SlickMusicHandle(soundSystem, music);
    slickMusicHandle.play();
    
    EasyMock.verify(soundSystem);
    EasyMock.verify(music);
  }
}
