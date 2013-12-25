package de.lessvoid.nifty.loaderv2.types;

import de.lessvoid.nifty.sound.SoundSystem;
import de.lessvoid.nifty.tools.StringHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RegisterMusicType extends XmlBaseType {
  @Override
  @Nonnull
  public String output(final int offset) {
    return StringHelper.whitespace(offset) + "<registerMusic> " + super.output(offset);
  }

  public void materialize(@Nonnull final SoundSystem soundSystem) {
    String fileName = getFilename();
    if (fileName == null) {
      return;
    }
    soundSystem.addMusic(getId(), fileName);
  }

  @Nullable
  private String getId() {
    return getAttributes().get("id");
  }

  @Nullable
  private String getFilename() {
    return getAttributes().get("filename");
  }
}
