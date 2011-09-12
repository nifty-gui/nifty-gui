package de.lessvoid.nifty.loaderv2.types;

import de.lessvoid.nifty.sound.SoundSystem;
import de.lessvoid.nifty.tools.StringHelper;

public class RegisterMusicType extends XmlBaseType {
  public String output(final int offset) {
    return StringHelper.whitespace(offset) + "<registerMusic> " + super.output(offset);
  }

  public void materialize(final SoundSystem soundSystem) {
    soundSystem.addMusic(getId(), getFilename());
  }

  private String getId() {
    return getAttributes().get("id");
  }

  private String getFilename() {
    return getAttributes().get("filename");
  }
}
