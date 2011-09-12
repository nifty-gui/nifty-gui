package de.lessvoid.nifty.loaderv2.types;

import de.lessvoid.nifty.sound.SoundSystem;
import de.lessvoid.nifty.tools.StringHelper;

public class RegisterSoundType extends XmlBaseType {
  public String output(final int offset) {
    return StringHelper.whitespace(offset) + "<registerSound> " + super.output(offset);
  }

  public void materialize(final SoundSystem soundSystem) {
    soundSystem.addSound(getId(), getFilename());
  }

  private String getId() {
    return getAttributes().get("id");
  }

  private String getFilename() {
    return getAttributes().get("filename");
  }
}
