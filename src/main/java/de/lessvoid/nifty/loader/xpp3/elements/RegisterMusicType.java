package de.lessvoid.nifty.loader.xpp3.elements;

import de.lessvoid.nifty.sound.SoundSystem;

/**
 * RegisterSoundType.
 * @author void
 */
public class RegisterMusicType {

  /**
   * id.
   */
  private String id;

  /**
   * filename.
   */
  private String filename;

  /**
   * @param idParam id
   * @param filenameParam filename
   */
  public RegisterMusicType(final String idParam, final String filenameParam) {
    this.id = idParam;
    this.filename = filenameParam;
  }

  /**
   * register music in soundSystem.
   * @param soundSystem soundSystem
   */
  public void register(final SoundSystem soundSystem) {
    soundSystem.addMusic(id, filename);
  }
}
