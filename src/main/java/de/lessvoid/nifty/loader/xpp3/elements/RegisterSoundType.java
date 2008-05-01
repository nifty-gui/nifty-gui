package de.lessvoid.nifty.loader.xpp3.elements;

import de.lessvoid.nifty.sound.SoundSystem;

/**
 * RegisterSoundType.
 * @author void
 */
public class RegisterSoundType {

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
  public RegisterSoundType(final String idParam, final String filenameParam) {
    this.id = idParam;
    this.filename = filenameParam;
  }

  /**
   * register.
   * @param soundSystem soundSystem
   */
  public void register(final SoundSystem soundSystem) {
    soundSystem.addSound(id, filename);
  }
}
